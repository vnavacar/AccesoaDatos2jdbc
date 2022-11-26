package org.sfaci.ejemplobases.util;

import javax.swing.*;

public class Util {

    public static void mensajeError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null,
                mensaje, titulo,
                JOptionPane.ERROR_MESSAGE);
    }

    public static void mensajeInformacion(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(null,
                mensaje, titulo,
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void mensaje(String mensaje) {
        JOptionPane.showMessageDialog(null,
                mensaje, "Información",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
