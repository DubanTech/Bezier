package curvasbezier;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author duban
 */
public class Lienzo extends JPanel {

    private int grosorActual = 6; // Grosor que tendrán las NUEVAS curvas
    private List<Curva> curvas = new ArrayList<>();
    private float incremento = 0.001f;
    private Color colorActual = Color.BLACK;
    private Herramienta herramientaActual = Herramienta.NUEVO;
    private Curva CurvaActual;
    private int Npuntos;
    private final int TAMANO_PUNTO_VISUAL = 8; 

    private Punto puntoSeleccionadoParaArrastrar = null;
    private Curva curvaDelPuntoSeleccionado = null;

    public Lienzo() {
        setBackground(Color.WHITE);

        MouseAdapter mouseHandler = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                if (herramientaActual == Herramienta.SELECCIONAR) {
                    detectarPuntoCercano(x, y);
                    repaint();
                    return; // No crea curvas ni añade puntos
                }

                if (herramientaActual == Herramienta.NUEVO) {
                    if (CurvaActual == null) {
                        String input = JOptionPane.showInputDialog(
                                Lienzo.this,
                                "Ingrese la cantidad de puntos para la curva de Bézier:",
                                "Nueva Curva",
                                JOptionPane.QUESTION_MESSAGE
                        );

                        if (input == null) return;

                        try {
                            int cantidad = Integer.parseInt(input);
                            if (cantidad < 2) {
                                JOptionPane.showMessageDialog(
                                        Lienzo.this,
                                        "La cantidad de puntos debe ser >= 2",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                return;
                            }
                            Npuntos = cantidad;
                            
                            CurvaActual = new Curva();
                            CurvaActual.setColor(colorActual);
                            CurvaActual.setGrosor(grosorActual);
                            curvas.add(CurvaActual);

                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(Lienzo.this, "Ingrese un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    CurvaActual.agregarPunto(new Punto(x, y));
                    Npuntos--;

                    if (Npuntos == 0) {
                        CurvaActual = null; // Curva completada
                    }

                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (herramientaActual == Herramienta.SELECCIONAR && puntoSeleccionadoParaArrastrar != null) {
                    puntoSeleccionadoParaArrastrar.setX(e.getX());
                    puntoSeleccionadoParaArrastrar.setY(e.getY());
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                puntoSeleccionadoParaArrastrar = null;
                curvaDelPuntoSeleccionado = null;
                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
    }

    private boolean detectarPuntoCercano(int x, int y) {
        int tolerancia = TAMANO_PUNTO_VISUAL; 
        for (int i = curvas.size() - 1; i >= 0; i--) {
            Curva curva = curvas.get(i);
            for (Punto p : curva.getPuntos()) {
                double distancia = Math.sqrt(Math.pow(p.getX() - x, 2) + Math.pow(p.getY() - y, 2));
                if (distancia <= tolerancia) {
                    puntoSeleccionadoParaArrastrar = p;
                    curvaDelPuntoSeleccionado = curva;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Curva curva : curvas) {
            dibujarFiguraEnPantalla(curva, g);
        }
    }

    private void dibujarFiguraEnPantalla(Curva curva, Graphics g) {
        g.setColor(curva.getColor());
        
        int gCurva = curva.getGrosor();
        PintorDePunto pintor = (x, y) -> g.fillRect(x - gCurva / 2, y - gCurva / 2, gCurva, gCurva);
        
        dibujarCurva(curva, pintor);

        for (Punto p : curva.getPuntos()) {
            if (p == puntoSeleccionadoParaArrastrar) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.BLUE);
            }
            g.fillRect(p.getX() - TAMANO_PUNTO_VISUAL / 2, p.getY() - TAMANO_PUNTO_VISUAL / 2, TAMANO_PUNTO_VISUAL, TAMANO_PUNTO_VISUAL);
        }
    }

    public void dibujarCurva(Curva curva, PintorDePunto pintor) {
        List<Punto> puntos = curva.getPuntos();
        int numPuntos = puntos.size();

        if (numPuntos < 2) {
            return;
        }

        int n = numPuntos - 1;
        int pasos = (int) (1.0f / incremento);

        for (int step = 0; step <= pasos; step++) {
            float tLocal = (float) step / pasos;
            double x = 0;
            double y = 0;

            for (int i = 0; i <= n; i++) {
                double bernstein = combinatoria(n, i) * Math.pow(1 - tLocal, n - i) * Math.pow(tLocal, i);
                x += bernstein * puntos.get(i).getX();
                y += bernstein * puntos.get(i).getY();
            }

            pintor.pintar((int) Math.round(x), (int) Math.round(y));
        }
    }

    private long combinatoria(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k == 0 || k == n) return 1;
        if (k > n / 2) k = n - k;

        long res = 1;
        for (int i = 1; i <= k; i++) {
            res = res * (n - i + 1) / i;
        }
        return res;
    }


    public void setHerramienta(Herramienta herramienta) {
        this.herramientaActual = herramienta;
    }

    public void cambiarGrosor(int delta) {
        this.grosorActual = Math.max(1, this.grosorActual + delta);
    }

    public void setColorActual(Color color) {
        this.colorActual = color;
    }

    public Color getColorActual() {
        return colorActual;
    }

    public void limpiarLienzo() {
        curvas.clear();
        CurvaActual = null;
        repaint();
    }
}