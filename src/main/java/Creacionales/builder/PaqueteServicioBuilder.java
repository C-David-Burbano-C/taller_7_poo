/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.builder;

/**
 *
 * @author daferarte
 */

public class PaqueteServicioBuilder {
    private boolean incluyeDesayuno = false;
    private boolean incluyeSpa = false;
    private boolean incluyeTransporteAeropuerto = false;
    private int numeroPersonas = 1;

    public PaqueteServicioBuilder conDesayuno() {
        this.incluyeDesayuno = true;
        return this;
    }

    public PaqueteServicioBuilder conSpa() {
        this.incluyeSpa = true;
        return this;
    }

    public PaqueteServicioBuilder conTransporte() {
        this.incluyeTransporteAeropuerto = true;
        return this;
    }

    public PaqueteServicioBuilder paraPersonas(int numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
        return this;
    }

    public PaqueteServicio build() {
        if (this.numeroPersonas < 1) {
            throw new IllegalArgumentException("El paquete debe ser para al menos 1 persona.");
        }
        if (this.incluyeTransporteAeropuerto && this.numeroPersonas > 4) {
            throw new IllegalStateException("Error: El vehículo estándar admite máximo 4 personas.");
        }
        return new PaqueteServicio(incluyeDesayuno, incluyeSpa, incluyeTransporteAeropuerto, numeroPersonas);
    }
}
