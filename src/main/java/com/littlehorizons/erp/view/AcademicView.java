package com.littlehorizons.erp.view;

import com.littlehorizons.erp.model.AcademicRecord;
import com.littlehorizons.erp.model.Student;
import com.littlehorizons.erp.service.AcademicService.AbsenceAlertDetail;
import com.littlehorizons.erp.service.AcademicService.SubjectGradeSummary;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AcademicView extends BaseView {

    public AcademicView(Scanner scanner) {
        super(scanner);
    }

    public void showAcademicMenu() {
        showMenu(
                "Gestión Académica",
                "Registrar nota",
                "Ver historial académico",
                "Ver promedios",
                "Registrar asistencia del curso",
                "Reporte de inasistencias"
        );
    }

    public void showAveragesMenu() {
        showMenu(
                "Consultas de Promedios",
                "Promedios por materia de un estudiante",
                "Promedio general de un estudiante en un período",
                "Promedio del curso por materia"
        );
    }

    public int readStudentId() {
        return readInt("ID del estudiante: ", 1, Integer.MAX_VALUE);
    }

    public int readSubjectId() {
        return readInt("ID de la materia: ", 1, Integer.MAX_VALUE);
    }

    public int readTeacherId() {
        return readInt("ID del profesor: ", 1, Integer.MAX_VALUE);
    }

    public int readAcademicYear(int defaultYear) {
        String input = readOptional("Año académico [" + defaultYear + "]");
        if (input == null || input.isBlank()) {
            return defaultYear;
        }
        try {
            return Integer.parseInt(input.trim());
        } catch (NumberFormatException e) {
            showError("Año inválido. Se usará " + defaultYear + ".");
            return defaultYear;
        }
    }

    public String readPeriod(boolean allowFinal) {
        List<String> options = allowFinal
                ? List.of("I", "II", "III", "IV", "Final")
                : List.of("I", "II", "III", "IV");
        int index = readChoice("Seleccione el período académico: ", options);
        return options.get(index);
    }

    public double readGradeValue() {
        return readDouble("Nota (0.0 a 5.0): ", 0.0, 5.0);
    }

    public String readObservations() {
        return readOptional("Observaciones");
    }

    public String readAttendanceStatus() {
        List<String> options = List.of("Presente", "Ausente", "Tardanza", "Excusa Médica");
        int index = readChoice("Estado de asistencia: ", options);
        return options.get(index);
    }

    public LocalDate readAttendanceDate() {
        return readDate("Fecha de la clase");
    }

    public String readSection() {
        String sec = readOptional("Sección [A]");
        return (sec == null || sec.isBlank()) ? "A" : sec.trim().toUpperCase();
    }

    public boolean confirmUpdate(String message) {
        return readYesNo(message);
    }

    public void displayAcademicHistory(Student student, List<AcademicRecord> history, Map<Integer, String> subjectNames) {
        banner("Historial Académico", "Estudiante: " + student.fullName() + " (ID: " + student.id() + ")");
        if (history.isEmpty()) {
            showInfo("El estudiante no tiene registros académicos.");
            return;
        }
        String[] headers = {"Año", "Período", "Materia", "Nota", "Observaciones", "Fecha"};
        List<String[]> rows = new ArrayList<>();
        for (AcademicRecord r : history) {
            String subject = subjectNames.getOrDefault(r.subjectId(), "Materia #" + r.subjectId());
            String dateStr = r.recordDate() != null ? r.recordDate().toString() : "";
            String obs = r.observations() != null ? r.observations() : "";
            rows.add(new String[]{
                    String.valueOf(r.year()),
                    r.period(),
                    subject,
                    r.grade().toPlainString(),
                    obs,
                    dateStr
            });
        }
        printTable(headers, rows);
    }

    public void displaySubjectSummaries(Student student, int year, List<SubjectGradeSummary> summaries) {
        banner("Promedios por Materia (" + year + ")", "Estudiante: " + student.fullName() + " (ID: " + student.id() + ")");
        if (summaries.isEmpty()) {
            showInfo("No hay notas registradas para este estudiante en el año " + year + ".");
            return;
        }
        String[] headers = {"Materia", "Promedio (I-IV)", "Nota Final"};
        List<String[]> rows = new ArrayList<>();
        for (SubjectGradeSummary s : summaries) {
            String avgStr = s.average() != null ? s.average().toPlainString() : "N/A";
            String finalStr = s.finalGrade() != null ? s.finalGrade().toPlainString() : "-";
            rows.add(new String[]{s.subjectName(), avgStr, finalStr});
        }
        printTable(headers, rows);
    }

    public void displayAbsenceAlerts(List<AbsenceAlertDetail> alerts, int threshold) {
        banner("Reporte de Inasistencias Semanales", "Umbral semanal: más de " + threshold + " días ausente");
        if (alerts.isEmpty()) {
            showInfo("No se encontraron estudiantes que superen el umbral de ausencias semanales.");
            return;
        }
        String[] headers = {"ID", "Estudiante", "Semana", "Días Ausente", "Total Inasistencias (Año)"};
        List<String[]> rows = new ArrayList<>();
        for (AbsenceAlertDetail a : alerts) {
            rows.add(new String[]{
                    String.valueOf(a.studentId()),
                    a.studentName(),
                    String.valueOf(a.yearWeek()),
                    String.valueOf(a.weeklyAbsenceDays()),
                    String.valueOf(a.totalYearAbsences())
            });
        }
        printTable(headers, rows);
    }
}
