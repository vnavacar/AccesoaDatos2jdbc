package org.sfsoft.sentenciassql.gui;

import org.sfsoft.sentenciassql.base.Personaje;
import static org.sfsoft.sentenciassql.util.Constantes.AccionDialogo;

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

    private AccionDialogo accion;
    private Personaje personaje;

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

        personaje = new Personaje();
        personaje.setNombre(tfNombre.getText());
        personaje.setNivel(Integer.parseInt(tfNivel.getText()));
        personaje.setEnergia(Integer.parseInt(tfEnergia.getText()));
        personaje.setPuntos(Integer.parseInt(tfPuntos.getText()));

        accion = AccionDialogo.ACEPTAR;
        setVisible(false);
    }

    private void cancelar() {

        accion = AccionDialogo.CANCELAR;
        setVisible(false);
    }

    public AccionDialogo mostrarDialogo() {
        setVisible(true);

        return accion;
    }

    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;

        tfNombre.setText(personaje.getNombre());
        tfPuntos.setText(String.valueOf(personaje.getPuntos()));
        tfEnergia.setText(String.valueOf(personaje.getEnergia()));
        tfNivel.setText(String.valueOf(personaje.getNivel()));
    }

    public Personaje getPersonaje() {
        return personaje;
    }
}
