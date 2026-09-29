/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.singleton;

/**
 *
 * @author daferarte
 */
public class ConfiguracionSistema {
    private double tasaIva;
    private String monedaPorDefecto;

    private ConfiguracionSistema() {
        // Carga inicial simulada
        this.tasaIva = 0.19;
        this.monedaPorDefecto = "USD";
    }

    // Holder estático: garantiza inicialización segura y perezosa (Lazy loading)
    private static class Holder {
        private static final ConfiguracionSistema INSTANCIA = new ConfiguracionSistema();
    }

    public static ConfiguracionSistema getInstancia() {
                
        return Holder.INSTANCIA;
    }

    public double getTasaIva() { return tasaIva; }
    public void setTasaIva(double tasaIva) { this.tasaIva = tasaIva; }
    public String getMonedaPorDefecto() { return monedaPorDefecto; }
}
