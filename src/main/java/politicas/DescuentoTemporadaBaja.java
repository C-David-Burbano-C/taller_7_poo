/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package politicas;

/**
 *
 * @author daferarte
 */
public class DescuentoTemporadaBaja implements PoliticaDescuento {
    @Override
    public double aplicarDescuento(double montoBase) {
        return montoBase * 0.85; // 15% descuento
    }
}
