/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.factory;

/**
 *
 * @author daferarte
 */
public class RecargoCancelacionTardia implements PoliticaRecargo {
    @Override public double calcularRecargo(double montoBase) { return montoBase * 0.10; }
}