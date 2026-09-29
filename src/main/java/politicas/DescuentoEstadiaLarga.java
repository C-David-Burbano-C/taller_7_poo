/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package politicas;

import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;

/**
 *
 * @author daferarte
 */
public class DescuentoEstadiaLarga implements PoliticaDescuento {
    private final RangoFechas periodo;

    public DescuentoEstadiaLarga(RangoFechas periodo) {
        if (periodo == null) {
            throw new IllegalArgumentException("El periodo es obligatorio para calcular la estadía");
        }
        this.periodo = periodo;
    }

    @Override
    public double aplicarDescuento(double montoBase) {
        if (periodo.duracionEnDias() > 7) {
            return montoBase * 0.75; // 25% descuento
        }
        return montoBase;
    }
}
