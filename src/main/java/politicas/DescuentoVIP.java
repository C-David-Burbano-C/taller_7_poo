/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package politicas;

/**
 *
 * @author daferarte
 */
public class DescuentoVIP implements PoliticaDescuento {
    @Override
    public double aplicarDescuento(double montoBase) {
        return montoBase * 0.80; // 20% descuento
    }
}