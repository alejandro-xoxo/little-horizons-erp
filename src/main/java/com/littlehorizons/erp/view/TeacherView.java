package com.littlehorizons.erp.view;

import com.littlehorizons.erp.model.ClassSchedule;
import com.littlehorizons.erp.model.Teacher;
import com.littlehorizons.erp.service.TeacherService.TeacherAssignmentDetail;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class TeacherView extends BaseView {

    private static final List<String> DOC_TYPES = List.of("CC", "CE", "Pasaporte");
    private static final List<String> STATUS_OPTIONS = List.of("Activo", "Inactivo", "Licencia");
    private static final List<String> DAYS_OF_WEEK = List.of(
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"
    );

    public TeacherView(Scanner scanner) {
        super(scanner);
    }

    public void showTeachers(List<Teacher> teachers) {
        String[] headers = {"ID", "Nombre", "Especialización", "Estado"};
        List<String[]> rows = teachers.stream()
                .map(t -> new String[]{
                        String.valueOf(t.id()),
                        t.fullName(),
                        t.specialization() != null ? t.specialization() : "",
                        t.status()
                })
                .toList();
        printTable(headers, rows);
    }

    public void showWorkload(Teacher teacher, List<TeacherAssignmentDetail> workload) {
        banner("Carga Académica: " + teacher.fullName(),
                "ID: " + teacher.id() + " | Especialización: " + (teacher.specialization() != null ? teacher.specialization() : "N/A"));

        if (workload.isEmpty()) {
            showInfo("El profesor no tiene materias asignadas en el año académico.");
            return;
        }

        String[] headers = {"ID Asig", "Materia", "Código", "Sección", "Horas Sem.", "Horarios"};
        List<String[]> rows = workload.stream()
                .map(detail -> {
                    String subjectName = detail.subject() != null ? detail.subject().name() : "Materia #" + detail.assignment().subjectId();
                    String subjectCode = detail.subject() != null ? detail.subject().code() : "-";
                    String weeklyHours = detail.subject() != null ? String.valueOf(detail.subject().weeklyHours()) : "-";
                    String schedulesText = detail.schedules().isEmpty()
                            ? "(Sin horario)"
                            : detail.schedules().stream()
                            .map(s -> s.day() + " " + s.startTime() + "-" + s.endTime())
                            .collect(Collectors.joining("; "));

                    return new String[]{
                            String.valueOf(detail.assignment().id()),
                            subjectName,
                            subjectCode,
                            detail.assignment().section(),
                            weeklyHours,
                            schedulesText
                    };
                })
                .toList();

        printTable(headers, rows);
    }

    public String readDocumentType() {
        System.out.println("Seleccione el tipo de documento:");
        int idx = readChoice("Opción: ", DOC_TYPES);
        return DOC_TYPES.get(idx);
    }

    public String readDocumentNumber() {
        return readRequired("Número de documento: ");
    }

    public String readFirstName() {
        return readRequired("Primer nombre: ");
    }

    public String readMiddleName() {
        return readOptional("Segundo nombre");
    }

    public String readLastName() {
        return readRequired("Primer apellido: ");
    }

    public String readSecondLastName() {
        return readOptional("Segundo apellido");
    }

    public String readEmail() {
        return readRequired("Correo electrónico: ");
    }

    public String readPhone() {
        return readOptional("Teléfono");
    }

    public String readSpecialization() {
        return readOptional("Especialización");
    }

    public LocalDate readHireDate() {
        return readDate("Fecha de contratación");
    }

    public int readTeacherId() {
        return readInt("ID del profesor: ", 1, Integer.MAX_VALUE);
    }

    public int readSubjectId() {
        return readInt("ID de la materia: ", 1, Integer.MAX_VALUE);
    }

    public String readSection() {
        return readOptional("Sección");
    }

    public int readAssignmentId() {
        return readInt("ID de la asignación: ", 1, Integer.MAX_VALUE);
    }

    public String readDayOfWeek() {
        System.out.println("Seleccione el día de la semana:");
        int idx = readChoice("Opción: ", DAYS_OF_WEEK);
        return DAYS_OF_WEEK.get(idx);
    }

    public LocalTime readTime(String prompt) {
        while (true) {
            String text = ask(prompt);
            try {
                return LocalTime.parse(text);
            } catch (DateTimeParseException e) {
                showError("Hora inválida. Use el formato HH:mm (por ejemplo 08:00 o 14:30).");
            }
        }
    }

    public String readStatus() {
        System.out.println("Seleccione el nuevo estado:");
        int idx = readChoice("Opción: ", STATUS_OPTIONS);
        return STATUS_OPTIONS.get(idx);
    }
}
