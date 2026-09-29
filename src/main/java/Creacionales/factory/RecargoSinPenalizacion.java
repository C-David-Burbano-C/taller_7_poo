/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.factory;

/**
 *
 * @author daferarte
 */
public class RecargoSinPenalizacion implements PoliticaRecargo {
    @Override public double calcularRecargo(double montoBase) { return 0.0; }
}
