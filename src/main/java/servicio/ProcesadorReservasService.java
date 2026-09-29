/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

/**
 *
 * @author daferarte
 */


import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import java.io.FileWriter;
import java.io.IOException;

public class ProcesadorReservasService {
    
    public void procesarConfirmacion(Reserva reserva, String tipoCliente, String canalNotificacion) {
        // 1. Violación SRP & OCP: Lógica de cálculo de precios acoplada y con switch rígido
        double precioBase = 100.0;
        double precioFinal = precioBase;

        if (tipoCliente.equals("VIP")) {
            precioFinal = precioBase * 0.80; // 20% descuento
        } else if (tipoCliente.equals("CORPORATIVO")) {
            precioFinal = precioBase * 0.70; // 30% descuento
        } else if (tipoCliente.equals("REGULAR")) {
            precioFinal = precioBase;
        }

        System.out.println("Precio calculado: " + precioFinal);

        // 2. Modificación de estado
        reserva.confirmar();

        // 3. Violación DIP & SRP: Persistencia directa acoplada a archivo físico (sin abstracción)
        try (FileWriter writer = new FileWriter("reservas.txt", true)) {
            writer.write("Reserva ID: " + reserva.getId() + " - Cliente: " + reserva.getCliente().getNombre() + "\n");
        } catch (IOException e) {
            System.err.println("Error guardando en disco: " + e.getMessage());
        }

        // 4. Violación OCP, LSP e ISP: Notificación rígida por condicionales
        if (canalNotificacion.equalsIgnoreCase("EMAIL")) {
            System.out.println("Enviando Email a: " + reserva.getCliente().getEmail().valor());
        } else if (canalNotificacion.equalsIgnoreCase("SMS")) {
            System.out.println("Enviando SMS al número del cliente...");
        }
    }    
}
