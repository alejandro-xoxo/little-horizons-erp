package com.littlehorizons.erp.controller;

import com.littlehorizons.erp.config.AppConfig;
import com.littlehorizons.erp.model.GradeLevel;
import com.littlehorizons.erp.model.Guardian;
import com.littlehorizons.erp.model.GuardianLink;
import com.littlehorizons.erp.model.Student;
import com.littlehorizons.erp.model.Subject;
import com.littlehorizons.erp.service.StudentService;
import com.littlehorizons.erp.service.StudentService.StudentProfile;
import com.littlehorizons.erp.view.StudentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentController extends BaseController implements MenuModule {

    private final StudentView view;
    private final StudentService service;

    public StudentController(StudentView view, StudentService service) {
        super(view);
        this.view = view;
        this.service = service;
    }

    @Override
    public int position() {
        return 1;
    }

    @Override
    public void run() {
        while (true) {
            view.showMenu(
                    "Gestión de Estudiantes",
                    "Registrar estudiante",
                    "Buscar estudiante",
                    "Listar por grado",
                    "Gestionar acudientes",
                    "Matricular / cambiar de grado",
                    "Cambiar estado",
                    "Ver ficha completa",
                    "Inscribir en materias"
            );
            int option = view.readOption(8);
            if (option == 0) {
                return;
            }
            safely(() -> handleOption(option));
        }
    }

    private void handleOption(int option) {
        switch (option) {
            case 1 -> registerStudent();
            case 2 -> searchStudent();
            case 3 -> listByGrade();
            case 4 -> manageGuardians();
            case 5 -> enrollOrChangeGrade();
            case 6 -> changeStatus();
            case 7 -> viewProfile();
            case 8 -> enrollSubjects();
            default -> view.showError("Opción no reconocida.");
        }
    }

    private void registerStudent() {
        view.banner("REGISTRO DE ESTUDIANTE");
        Student student = view.readStudentForm();

        List<GradeLevel> grades = service.findAllGrades();
        GradeLevel grade = view.selectGrade(grades);

        List<GuardianLink> guardians = new ArrayList<>();
        boolean adding = true;
        while (adding) {
            view.showInfo("Opciones de acudiente:");
            int opt = view.readChoice("Seleccione: ", List.of("Buscar acudiente existente por documento", "Registrar nuevo acudiente"));
            Guardian guardian = null;
            if (opt == 0) {
                String doc = view.readRequired("Documento del acudiente: ");
                Optional<Guardian> existing = service.findGuardianByDocument(doc);
                if (existing.isPresent()) {
                    guardian = existing.get();
                    view.showInfo("Acudiente encontrado: " + guardian.fullName());
                } else {
                    view.showInfo("No existe un acudiente con ese documento. Proceda a registrarlo.");
                    guardian = view.readGuardianForm(doc);
                }
            } else {
                guardian = view.readGuardianForm(null);
            }

            boolean isPrimary;
            if (guardians.isEmpty()) {
                isPrimary = true;
                view.showInfo("Este acudiente será registrado como acudiente principal.");
            } else {
                isPrimary = view.readYesNo("¿Es acudiente principal?");
            }

            boolean emergency = view.readYesNo("¿Es contacto de emergencia?");
            guardians.add(new GuardianLink(guardian, isPrimary, emergency));

            adding = view.readYesNo("¿Desea agregar otro acudiente?");
        }

        long primaryCount = guardians.stream().filter(GuardianLink::primary).count();
        if (primaryCount != 1) {
            view.showInfo("Ajustando acudiente principal: se designará al primer acudiente como principal.");
            List<GuardianLink> adjusted = new ArrayList<>();
            for (int i = 0; i < guardians.size(); i++) {
                GuardianLink gl = guardians.get(i);
                adjusted.add(new GuardianLink(gl.guardian(), i == 0, gl.emergencyContact()));
            }
            guardians = adjusted;
        }

        String notes = view.readOptional("Observaciones de matrícula");
        int studentId = service.registerStudent(student, guardians, grade.id(), notes);
        view.showSuccess("Estudiante registrado exitosamente con ID " + studentId + ".");
    }

    private void searchStudent() {
        view.banner("BÚSQUEDA DE ESTUDIANTES");
        String text = view.readRequired("Ingrese nombre o documento a buscar: ");
        List<Student> results = service.searchStudents(text);
        view.showStudentTable(results, this::resolveGradeName);
    }

    private void listByGrade() {
        view.banner("LISTADO DE ESTUDIANTES POR GRADO");
        List<GradeLevel> grades = service.findAllGrades();
        GradeLevel grade = view.selectGrade(grades);
        int currentYear = AppConfig.getInstance().getAcademicYear();
        List<Student> students = service.getStudentsByGrade(grade.id(), currentYear);
        view.showStudentTable(students, id -> grade.name());
    }

    private void manageGuardians() {
        view.banner("GESTIÓN DE ACUDIENTES");
        int studentId = view.readInt("Ingrese el ID del estudiante: ", 1, Integer.MAX_VALUE);
        Student student = service.getStudent(studentId);
        view.showInfo("Estudiante: " + student.fullName() + " (Estado: " + student.status() + ")");

        List<GuardianLink> links = service.getGuardians(studentId);
        view.showGuardianList(links);

        view.showInfo("Acciones:");
        int choice = view.readChoice("Seleccione una acción: ", List.of(
                "Vincular acudiente existente",
                "Registrar y vincular nuevo acudiente",
                "Desvincular acudiente",
                "Designar acudiente principal"
        ));

        switch (choice) {
            case 0 -> {
                String doc = view.readRequired("Documento del acudiente: ");
                Optional<Guardian> gOpt = service.findGuardianByDocument(doc);
                if (gOpt.isEmpty()) {
                    view.showError("No se encontró ningún acudiente con ese documento.");
                    return;
                }
                boolean primary = view.readYesNo("¿Es acudiente principal?");
                boolean emergency = view.readYesNo("¿Es contacto de emergencia?");
                service.linkGuardian(studentId, gOpt.get().id(), primary, emergency);
                view.showSuccess("Acudiente vinculado con éxito.");
            }
            case 1 -> {
                Guardian g = view.readGuardianForm(null);
                int guardianId = service.saveGuardian(g);
                boolean primary = view.readYesNo("¿Es acudiente principal?");
                boolean emergency = view.readYesNo("¿Es contacto de emergencia?");
                service.linkGuardian(studentId, guardianId, primary, emergency);
                view.showSuccess("Acudiente registrado y vinculado con éxito.");
            }
            case 2 -> {
                if (links.isEmpty()) {
                    view.showInfo("El estudiante no tiene acudientes para desvincular.");
                    return;
                }
                int idx = view.readInt("Seleccione el número de acudiente a desvincular (1-" + links.size() + "): ", 1, links.size()) - 1;
                int guardianId = links.get(idx).guardian().id();
                service.unlinkGuardian(studentId, guardianId);
                view.showSuccess("Acudiente desvinculado con éxito.");
            }
            case 3 -> {
                if (links.isEmpty()) {
                    view.showInfo("El estudiante no tiene acudientes.");
                    return;
                }
                int idx = view.readInt("Seleccione el número de acudiente que será el principal (1-" + links.size() + "): ", 1, links.size()) - 1;
                int guardianId = links.get(idx).guardian().id();
                service.setPrimaryGuardian(studentId, guardianId);
                view.showSuccess("Acudiente principal actualizado con éxito.");
            }
        }
    }

    private void enrollOrChangeGrade() {
        view.banner("MATRICULAR / CAMBIAR DE GRADO");
        int studentId = view.readInt("Ingrese el ID del estudiante: ", 1, Integer.MAX_VALUE);
        Student student = service.getStudent(studentId);
        view.showInfo("Estudiante: " + student.fullName() + " (Estado: " + student.status() + ")");

        List<GradeLevel> grades = service.findAllGrades();
        GradeLevel grade = view.selectGrade(grades);
        int year = AppConfig.getInstance().getAcademicYear();
        String notes = view.readOptional("Observaciones");

        service.enrollOrChangeGrade(studentId, grade.id(), year, notes);
        view.showSuccess("Matrícula / cambio de grado procesado con éxito.");
    }

    private void changeStatus() {
        view.banner("CAMBIAR ESTADO DE ESTUDIANTE");
        int studentId = view.readInt("Ingrese el ID del estudiante: ", 1, Integer.MAX_VALUE);
        Student student = service.getStudent(studentId);
        view.showInfo("Estudiante: " + student.fullName() + " | Estado actual: " + student.status());

        String newStatus = view.selectStudentStatus();
        service.changeStudentStatus(studentId, newStatus);
        view.showSuccess("Estado del estudiante actualizado a: " + newStatus);
    }

    private void viewProfile() {
        view.banner("VER FICHA COMPLETA");
        int studentId = view.readInt("Ingrese el ID del estudiante: ", 1, Integer.MAX_VALUE);
        StudentProfile profile = service.getStudentProfile(studentId);
        view.showStudentProfile(profile);
    }

    private void enrollSubjects() {
        view.banner("INSCRIBIR EN MATERIAS");
        int studentId = view.readInt("Ingrese el ID del estudiante: ", 1, Integer.MAX_VALUE);
        StudentProfile profile = service.getStudentProfile(studentId);
        if (profile.currentEnrollment() == null || !"Activa".equalsIgnoreCase(profile.currentEnrollment().status())) {
            view.showError("El estudiante no tiene una matrícula activa en el año académico actual.");
            return;
        }

        int currentGradeId = profile.currentEnrollment().gradeId();
        List<Subject> subjects = service.getSubjectsForGrade(currentGradeId);
        if (subjects.isEmpty()) {
            view.showInfo("No hay materias registradas para el grado del estudiante.");
            return;
        }

        String period = view.selectAcademicPeriod();
        String section = view.readOptional("Sección (Enter para 'A')");

        view.showInfo("Materias disponibles:");
        for (int i = 0; i < subjects.size(); i++) {
            Subject sub = subjects.get(i);
            view.showInfo((i + 1) + ". " + sub.code() + " - " + sub.name() + " (" + sub.weeklyHours() + "h)"
                    + (!sub.active() ? " [Inactiva]" : ""));
        }

        String sel = view.readRequired("Ingrese números separados por coma (ej: 1, 2) o 'todas': ");
        List<Integer> selectedSubjectIds = new ArrayList<>();
        if ("todas".equalsIgnoreCase(sel.trim())) {
            for (Subject sub : subjects) {
                if (sub.active()) {
                    selectedSubjectIds.add(sub.id());
                }
            }
        } else {
            String[] parts = sel.split(",");
            for (String p : parts) {
                try {
                    int num = Integer.parseInt(p.trim());
                    if (num >= 1 && num <= subjects.size()) {
                        selectedSubjectIds.add(subjects.get(num - 1).id());
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        if (selectedSubjectIds.isEmpty()) {
            view.showError("No se seleccionó ninguna materia válida.");
            return;
        }

        service.enrollSubjects(studentId, selectedSubjectIds, profile.currentEnrollment().year(), period, section);
        view.showSuccess("Inscripción realizada con éxito (" + selectedSubjectIds.size() + " materias).");
    }

    private String resolveGradeName(int studentId) {
        try {
            StudentProfile profile = service.getStudentProfile(studentId);
            return profile.currentGrade() != null ? profile.currentGrade().name() : "-";
        } catch (Exception e) {
            return "-";
        }
    }
}
