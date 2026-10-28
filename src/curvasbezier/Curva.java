/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package curvasbezier;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author duban
 */
public class Curva {
    private List<Punto> Puntos = new ArrayList<>();
    private Color color;
    private int grado; 
    private int grosor;
    
    public int getGrado() {
        return grado;
    }

    public int getGrosor() {
        return grosor;
    }

    public void setGrosor(int grosor) {
        this.grosor = grosor;
    }

    public void setGrado(int grado) {
        this.grado = grado;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Curva() {
    }

    public List<Punto> getPuntos() {
        return Puntos;
    }

    public void setPuntos(List<Punto> Puntos) {
        this.Puntos = Puntos;
    }
    
    public void agregarPunto(Punto punto){
        Puntos.add(punto);
    }
    
}
