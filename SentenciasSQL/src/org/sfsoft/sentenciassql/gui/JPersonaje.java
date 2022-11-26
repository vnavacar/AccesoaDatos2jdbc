package org.sfsoft.sentenciassql.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Diálogo para recoger información sobre un personaje (para altas y modificaciones)
 * @author Santiago Faci
 * @version curso 2014-2015
 */
public class JPersonaje extends JDialog {
    private JPanel panel1;
    private JTextField tfNombre;
    private JTextField tfNivel;
    private JTextField tfEnergia;
    private JTextField tfPuntos;
    private JButton btCancelar;
    private JButton btAceptar;

    private String nombre;
    private int nivel;
    private int energia;
    private int puntos;
    public enum Accion {
        ACEPTAR, CANCELAR;
    }
    private Accion accion;

    public JPersonaje() {

        setContentPane(panel1);
        pack();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setModal(true);
        setLocationRelativeTo(null);

        btAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptar();
            }
        });


        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelar();
            }
        });
    }

    private void aceptar() {

        if (tfNombre.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "El nombre no puede estar vacío", "Aceptar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tfPuntos.getText().equals(""))
            tfPuntos.setText("0");

        if (tfEnergia.getText().equals(""))
            tfEnergia.setText("0");

        if (tfNivel.getText().equals(""))
            tfNivel.setText("0");

        nombre = tfNombre.getText();
        nivel = Integer.parseInt(tfNivel.getText());
        energia = Integer.parseInt(tfEnergia.getText());
        puntos = Integer.parseInt(tfPuntos.getText());

        accion = Accion.ACEPTAR;
        setVisible(false);
    }

    private void cancelar() {

        accion = Accion.CANCELAR;
        setVisible(false);
    }

    public Accion mostrarDialogo() {
        setVisible(true);

        return accion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        tfNombre.setText(nombre);
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        tfNivel.setText(String.valueOf(nivel));
    }

    public int getEnergia() {
        return energia;
    }

    public void setEnergia(int energia) {
        tfEnergia.setText(String.valueOf(energia));
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        tfPuntos.setText(String.valueOf(puntos));
    }
}
