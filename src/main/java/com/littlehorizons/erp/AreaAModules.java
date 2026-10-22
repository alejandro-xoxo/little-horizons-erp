package com.littlehorizons.erp;

import com.littlehorizons.erp.controller.MenuModule;

import java.util.List;
import java.util.Scanner;

/**
 * Registro de módulos del Área A (Dev A): posiciones 1, 2 y 3 del menú.
 * Dueño: Dev A. Para registrar un módulo, agregue su instancia a la lista, por ejemplo:
 * {@code new StudentController(new StudentView(scanner), new StudentService(new StudentDaoImpl()))}.
 */
public final class AreaAModules {

    private AreaAModules() {
    }

    public static List<MenuModule> create(Scanner scanner) {
        return List.of(
                // posición 1: Estudiantes
                // posición 2: Académica
                // posición 3: Profesores
        );
    }
}
