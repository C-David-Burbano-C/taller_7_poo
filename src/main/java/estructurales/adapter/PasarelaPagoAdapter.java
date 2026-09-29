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
public class PasarelaPagoAdapter implements ProcesadorPago {
    private final PasarelaExternaPago pasarelaExterna;

    public PasarelaPagoAdapter(PasarelaExternaPago pasarelaExterna) {
        this.pasarelaExterna = pasarelaExterna;
    }

    @Override
    public boolean cobrar(Cliente cliente, double monto) {
        // Adaptación: Convertir el tipo de dato y transformar dólares/pesos a centavos
        // 15.50 = 1550
        String customerRef = cliente.getEmail().valor();
        long amountInCents = Math.round(monto * 100);

        return pasarelaExterna.executeTransaction(customerRef, amountInCents);
    }
}
