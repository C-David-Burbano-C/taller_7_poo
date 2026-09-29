/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.builder;

/**
 *
 * @author daferarte
 */
public class PaqueteServicio {
    private final boolean incluyeDesayuno;
    private final boolean incluyeSpa;
    private final boolean incluyeTransporteAeropuerto;
    private final int numeroPersonas;

    // Constructor restrictivo, accesible solo desde el Builder en el mismo paquete
    PaqueteServicio(boolean incluyeDesayuno, boolean incluyeSpa, boolean incluyeTransporte, int numeroPersonas) {
        this.incluyeDesayuno = incluyeDesayuno;
        this.incluyeSpa = incluyeSpa;
        this.incluyeTransporteAeropuerto = incluyeTransporte;
        this.numeroPersonas = numeroPersonas;
    }

    @Override
    public String toString() {
        return "PaqueteServicio [Desayuno=" + incluyeDesayuno + ", Spa=" + incluyeSpa + 
               ", Transporte=" + incluyeTransporteAeropuerto + ", Personas=" + numeroPersonas + "]";
    }
}
