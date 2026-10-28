/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package curvasbezier;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 *
 * @author duban
 */
public class CurvasBezier extends JFrame {

    private Lienzo lienzo;
    private JButton botonColorActual;

    public CurvasBezier() {
        setTitle("Dibujado de Curvas De Beízer Pro Max Ultra 3000");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        lienzo = new Lienzo();
        add(lienzo, BorderLayout.CENTER);
        add(crearBarraHerramientas(), BorderLayout.NORTH);
    }

    private JToolBar crearBarraHerramientas() {
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);

        ButtonGroup grupoHerramientas = new ButtonGroup();

        agregarBotonHerramienta(barra, grupoHerramientas, "Nueva", Herramienta.NUEVO, false);
        agregarBotonHerramienta(barra, grupoHerramientas, "Grosor +", Herramienta.GROSOR, false);
        agregarBotonHerramienta(barra, grupoHerramientas, "Grosor -", Herramienta.GROSOR, false);
        agregarBotonHerramienta(barra, grupoHerramientas, "Seleccionar Curva", Herramienta.SELECCIONAR, true);

        barra.addSeparator();

        botonColorActual = new JButton("Color");
        botonColorActual.setOpaque(true);
        botonColorActual.setBackground(Color.BLACK);
        botonColorActual.setForeground(Color.WHITE);
        botonColorActual.addActionListener(e -> {
            Color nuevoColor = JColorChooser.showDialog(this, "Elegir color", lienzo.getColorActual());
            if (nuevoColor != null) {
                lienzo.setColorActual(nuevoColor);
                botonColorActual.setBackground(nuevoColor);
                // Texto en blanco o negro segun que tan claro sea el color, para que se lea bien
                double luminancia = 0.299 * nuevoColor.getRed() + 0.587 * nuevoColor.getGreen() + 0.114 * nuevoColor.getBlue();
                botonColorActual.setForeground(luminancia > 150 ? Color.BLACK : Color.WHITE);
            }
        });
        barra.add(botonColorActual);

        barra.addSeparator();

        JButton botonLimpiar = new JButton("Borrar todo");
        botonLimpiar.addActionListener(e -> lienzo.limpiarLienzo());
        barra.add(botonLimpiar);

        JButton botonGrosorMas = new JButton("Grosor +");
        botonGrosorMas.addActionListener(e -> lienzo.cambiarGrosor(2));
        barra.add(botonGrosorMas);

        JButton botonGrosorMenos = new JButton("Grosor -");
        botonGrosorMenos.addActionListener(e -> lienzo.cambiarGrosor(-2));
        barra.add(botonGrosorMenos);

        return barra;
    }

    private void agregarBotonHerramienta(JToolBar barra, ButtonGroup grupo, String texto,
            Herramienta herramienta, boolean seleccionado) {
        JToggleButton boton = new JToggleButton(texto, seleccionado);
        boton.addActionListener(e -> lienzo.setHerramienta(herramienta));
        grupo.add(boton);
        barra.add(boton);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CurvasBezier ventana = new CurvasBezier();
            ventana.setVisible(true);
        });
    }

}
