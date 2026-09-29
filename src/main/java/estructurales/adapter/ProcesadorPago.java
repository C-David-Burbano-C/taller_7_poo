/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructurales.adapter;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;

/**
 *
 * @author daferarte
 */
public interface ProcesadorPago {
    boolean cobrar(Cliente cliente, double monto);
}
