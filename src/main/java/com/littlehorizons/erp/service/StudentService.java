package com.littlehorizons.erp.service;

import com.littlehorizons.erp.config.AppConfig;
import com.littlehorizons.erp.dao.EnrollmentDao;
import com.littlehorizons.erp.dao.GradeLevelDao;
import com.littlehorizons.erp.dao.GuardianDao;
import com.littlehorizons.erp.dao.StudentDao;
import com.littlehorizons.erp.dao.StudentRegistrationDao;
import com.littlehorizons.erp.dao.SubjectDao;
import com.littlehorizons.erp.dao.SubjectEnrollmentDao;
import com.littlehorizons.erp.model.Enrollment;
import com.littlehorizons.erp.model.GradeLevel;
import com.littlehorizons.erp.model.Guardian;
import com.littlehorizons.erp.model.GuardianLink;
import com.littlehorizons.erp.model.Student;
import com.littlehorizons.erp.model.Subject;
import com.littlehorizons.erp.model.SubjectEnrollment;
import com.littlehorizons.erp.util.CustomExceptions.BusinessException;
import com.littlehorizons.erp.util.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentService {

    public record StudentProfile(
            Student student,
            GradeLevel currentGrade,
            Enrollment currentEnrollment,
            List<Enrollment> enrollmentHistory,
            List<GuardianLink> guardians
    ) {
    }

    private final StudentDao studentDao;
    private final GuardianDao guardianDao;
    private final EnrollmentDao enrollmentDao;
    private final GradeLevelDao gradeLevelDao;
    private final StudentRegistrationDao studentRegistrationDao;
    private final SubjectDao subjectDao;
    private final SubjectEnrollmentDao subjectEnrollmentDao;

    public StudentService(
            StudentDao studentDao,
            GuardianDao guardianDao,
            EnrollmentDao enrollmentDao,
            GradeLevelDao gradeLevelDao,
            StudentRegistrationDao studentRegistrationDao,
            SubjectDao subjectDao,
            SubjectEnrollmentDao subjectEnrollmentDao) {
        this.studentDao = studentDao;
        this.guardianDao = guardianDao;
        this.enrollmentDao = enrollmentDao;
        this.gradeLevelDao = gradeLevelDao;
        this.studentRegistrationDao = studentRegistrationDao;
        this.subjectDao = subjectDao;
        this.subjectEnrollmentDao = subjectEnrollmentDao;
    }

    public int registerStudent(Student student, List<GuardianLink> guardians, int gradeId, String notes) {
        if (!InputValidator.isNotBlank(student.firstName())) {
            throw new BusinessException("El primer nombre del estudiante es obligatorio.");
        }
        if (!InputValidator.isNotBlank(student.lastName())) {
            throw new BusinessException("El primer apellido del estudiante es obligatorio.");
        }
        if (!InputValidator.isValidDocument(student.documentNumber())) {
            throw new BusinessException("El documento del estudiante no es válido.");
        }
        StudentRules.validateBirthDate(student.birthDate());

        if (studentDao.findByDocument(student.documentNumber()).isPresent()) {
            throw new BusinessException("Ya existe un estudiante con ese número de documento.");
        }

        StudentRules.validateGuardians(guardians);
        for (GuardianLink link : guardians) {
            Guardian g = link.guardian();
            if (!InputValidator.isNotBlank(g.firstName())) {
                throw new BusinessException("El nombre del acudiente es obligatorio.");
            }
            if (!InputValidator.isNotBlank(g.lastName())) {
                throw new BusinessException("El apellido del acudiente es obligatorio.");
            }
            if (!InputValidator.isValidDocument(g.documentNumber())) {
                throw new BusinessException("El documento del acudiente no es válido.");
            }
            if (!InputValidator.isNotBlank(g.phone())) {
                throw new BusinessException("El teléfono principal del acudiente es obligatorio.");
            }
        }

        int academicYear = AppConfig.getInstance().getAcademicYear();
        GradeLevel grade = gradeLevelDao.findById(gradeId)
                .orElseThrow(() -> new BusinessException("El grado seleccionado no existe."));

        StudentRules.validateAgeConsistency(student.birthDate(), grade.numericLevel(), academicYear);

        Enrollment enrollment = new Enrollment(
                0,
                0,
                gradeId,
                academicYear,
                "Activa",
                LocalDate.now(),
                null,
                notes
        );

        return studentRegistrationDao.register(student, guardians, enrollment);
    }

    public int updateStudent(Student student) {
        if (!InputValidator.isNotBlank(student.firstName())) {
            throw new BusinessException("El primer nombre del estudiante es obligatorio.");
        }
        if (!InputValidator.isNotBlank(student.lastName())) {
            throw new BusinessException("El primer apellido del estudiante es obligatorio.");
        }
        if (!InputValidator.isValidDocument(student.documentNumber())) {
            throw new BusinessException("El documento del estudiante no es válido.");
        }
        StudentRules.validateBirthDate(student.birthDate());

        Optional<Student> existing = studentDao.findByDocument(student.documentNumber());
        if (existing.isPresent() && existing.get().id() != student.id()) {
            throw new BusinessException("Ya existe un estudiante con ese número de documento.");
        }

        return studentDao.update(student);
    }

    public Student getStudent(int id) {
        return studentDao.findById(id)
                .orElseThrow(() -> new BusinessException("Estudiante no encontrado."));
    }

    public List<Student> searchStudents(String text) {
        return studentDao.search(text);
    }

    public List<Student> getStudentsByGrade(int gradeId, int year) {
        return studentDao.findByGrade(gradeId, year);
    }

    public List<GradeLevel> findAllGrades() {
        return gradeLevelDao.findAll();
    }

    public Optional<GradeLevel> findGradeById(int id) {
        return gradeLevelDao.findById(id);
    }

    public StudentProfile getStudentProfile(int studentId) {
        Student student = getStudent(studentId);
        int academicYear = AppConfig.getInstance().getAcademicYear();
        Optional<Enrollment> currentEnrollment = enrollmentDao.findByStudentAndYear(studentId, academicYear);
        GradeLevel currentGrade = null;
        if (currentEnrollment.isPresent()) {
            currentGrade = gradeLevelDao.findById(currentEnrollment.get().gradeId()).orElse(null);
        }
        List<Enrollment> history = enrollmentDao.findByStudent(studentId);
        List<GuardianLink> guardians = guardianDao.findByStudent(studentId);
        return new StudentProfile(student, currentGrade, currentEnrollment.orElse(null), history, guardians);
    }

    public void changeStudentStatus(int studentId, String targetStatus) {
        Student student = getStudent(studentId);
        int academicYear = AppConfig.getInstance().getAcademicYear();
        Optional<Enrollment> currentEnrollment = enrollmentDao.findByStudentAndYear(studentId, academicYear);

        int numericLevel = -1;
        if (currentEnrollment.isPresent()) {
            numericLevel = gradeLevelDao.findById(currentEnrollment.get().gradeId())
                    .map(GradeLevel::numericLevel)
                    .orElse(-1);
        }

        StudentRules.validateStatusTransition(student.status(), targetStatus, numericLevel);

        if ("Graduado".equalsIgnoreCase(targetStatus)) {
            if (currentEnrollment.isEmpty()) {
                throw new BusinessException("El estudiante no cuenta con matrícula activa para ser graduado.");
            }
            studentDao.updateStatus(studentId, "Graduado");
            enrollmentDao.updateStatus(currentEnrollment.get().id(), "Completada", null);
        } else if ("Retirado".equalsIgnoreCase(targetStatus)) {
            studentDao.updateStatus(studentId, "Retirado");
            if (currentEnrollment.isPresent()) {
                enrollmentDao.updateStatus(currentEnrollment.get().id(), "Retirada", LocalDate.now());
            }
        } else if ("Suspendido".equalsIgnoreCase(targetStatus)) {
            studentDao.updateStatus(studentId, "Suspendido");
        } else if ("Activo".equalsIgnoreCase(targetStatus)) {
            studentDao.updateStatus(studentId, "Activo");
            if (currentEnrollment.isPresent() && !"Activa".equals(currentEnrollment.get().status())) {
                enrollmentDao.updateStatus(currentEnrollment.get().id(), "Activa", null);
            }
        }
    }

    public int enrollOrChangeGrade(int studentId, int gradeId, int year, String notes) {
        Student student = getStudent(studentId);
        if ("Graduado".equalsIgnoreCase(student.status())) {
            throw new BusinessException("Un estudiante graduado no puede ser matriculado.");
        }
        if ("Retirado".equalsIgnoreCase(student.status())) {
            throw new BusinessException("Un estudiante retirado no puede ser matriculado directamente; reactive su estado primero.");
        }

        GradeLevel grade = gradeLevelDao.findById(gradeId)
                .orElseThrow(() -> new BusinessException("El grado seleccionado no existe."));
        StudentRules.validateAgeConsistency(student.birthDate(), grade.numericLevel(), year);

        Optional<Enrollment> existing = enrollmentDao.findByStudentAndYear(studentId, year);
        if (existing.isPresent()) {
            enrollmentDao.updateGrade(existing.get().id(), gradeId);
            return existing.get().id();
        } else {
            Enrollment newEnrollment = new Enrollment(
                    0,
                    studentId,
                    gradeId,
                    year,
                    "Activa",
                    LocalDate.now(),
                    null,
                    notes
            );
            return enrollmentDao.insert(newEnrollment);
        }
    }

    public List<GuardianLink> getGuardians(int studentId) {
        getStudent(studentId);
        return guardianDao.findByStudent(studentId);
    }

    public Optional<Guardian> findGuardianByDocument(String documentNumber) {
        return guardianDao.findByDocument(documentNumber);
    }

    public int saveGuardian(Guardian guardian) {
        if (guardian.id() <= 0) {
            return guardianDao.insert(guardian);
        } else {
            guardianDao.update(guardian);
            return guardian.id();
        }
    }

    public void linkGuardian(int studentId, int guardianId, boolean primary, boolean emergencyContact) {
        getStudent(studentId);
        guardianDao.findById(guardianId)
                .orElseThrow(() -> new BusinessException("El acudiente no existe."));
        guardianDao.link(studentId, guardianId, primary, emergencyContact);
        if (primary) {
            guardianDao.setPrimary(studentId, guardianId);
        }
    }

    public void unlinkGuardian(int studentId, int guardianId) {
        getStudent(studentId);
        List<GuardianLink> links = guardianDao.findByStudent(studentId);
        if (links.size() <= 1) {
            throw new BusinessException("No se puede desvincular al único acudiente del estudiante.");
        }
        GuardianLink link = links.stream()
                .filter(l -> l.guardian().id() == guardianId)
                .findFirst()
                .orElseThrow(() -> new BusinessException("El acudiente no está vinculado al estudiante."));
        if (link.primary()) {
            throw new BusinessException("No se puede desvincular al acudiente principal sin designar otro antes.");
        }
        guardianDao.unlink(studentId, guardianId);
    }

    public void setPrimaryGuardian(int studentId, int guardianId) {
        getStudent(studentId);
        List<GuardianLink> links = guardianDao.findByStudent(studentId);
        boolean found = links.stream().anyMatch(l -> l.guardian().id() == guardianId);
        if (!found) {
            throw new BusinessException("El acudiente no está vinculado al estudiante.");
        }
        guardianDao.setPrimary(studentId, guardianId);
    }

    public void enrollSubjects(int studentId, List<Integer> subjectIds, int year, String period, String section) {
        StudentRules.validatePeriod(period);
        String sec = (section == null || section.isBlank()) ? "A" : section.trim();

        Student student = getStudent(studentId);
        StudentRules.validateCanEnrollSubjects(student.status());

        Optional<Enrollment> enrollmentOpt = enrollmentDao.findByStudentAndYear(studentId, year);
        if (enrollmentOpt.isEmpty() || !"Activa".equalsIgnoreCase(enrollmentOpt.get().status())) {
            throw new BusinessException("El estudiante no tiene una matrícula activa en el año académico.");
        }

        int studentGradeId = enrollmentOpt.get().gradeId();
        if (subjectIds == null || subjectIds.isEmpty()) {
            throw new BusinessException("Debe seleccionar al menos una materia para inscribir.");
        }

        List<SubjectEnrollment> toEnroll = new ArrayList<>();
        for (int subjectId : subjectIds) {
            Subject subject = subjectDao.findById(subjectId)
                    .orElseThrow(() -> new BusinessException("La materia no existe."));
            if (!subject.active()) {
                throw new BusinessException("La materia no se encuentra activa.");
            }
            if (subject.gradeId() != studentGradeId) {
                throw new BusinessException("Solo se pueden inscribir materias correspondientes al grado actual del estudiante.");
            }
            if (subjectEnrollmentDao.exists(studentId, subjectId, year, period)) {
                throw new BusinessException("El estudiante ya está inscrito en la materia para este período.");
            }
            toEnroll.add(new SubjectEnrollment(
                    0,
                    studentId,
                    subjectId,
                    year,
                    period,
                    sec,
                    LocalDate.now(),
                    "Activa"
            ));
        }

        subjectEnrollmentDao.insertAll(toEnroll);
    }

    public List<Subject> getSubjectsForGrade(int gradeId) {
        return subjectDao.findByGrade(gradeId);
    }

    public List<SubjectEnrollment> getSubjectEnrollments(int studentId, int year, String period) {
        return subjectEnrollmentDao.findByStudent(studentId, year, period);
    }
}
