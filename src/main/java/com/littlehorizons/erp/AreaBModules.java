package com.littlehorizons.erp;

import com.littlehorizons.erp.controller.MenuModule;

import java.util.List;
import java.util.Scanner;

/**
 * Registro de módulos del Área B (Dev B): posiciones 4, 5, 6 y 7 del menú.
 * Dueño: Dev B. Para registrar un módulo, agregue su instancia a la lista, por ejemplo:
 * {@code new ClubController(new ClubView(scanner), new ExtracurricularService(new ClubDaoImpl()))}.
 */
public final class AreaBModules {

    private AreaBModules() {
    }

    public static List<MenuModule> create(Scanner scanner) {
        return List.of(
                // posición 4: Extracurriculares
                // posición 5: Financiera
                // posición 6: Recursos
                // posición 7: Reportes
        );
    }
}
