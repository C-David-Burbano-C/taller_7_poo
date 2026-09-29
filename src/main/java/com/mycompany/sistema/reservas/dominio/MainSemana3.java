/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio;

import Creacionales.builder.ReservaBuilder;
import Creacionales.factory.PoliticaDescuentoFactory;
import Creacionales.singleton.ConfiguracionSistema;
import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import estructurales.adapter.PasarelaExternaPago;
import estructurales.adapter.PasarelaPagoAdapter;
import estructurales.adapter.ProcesadorPago;
import estructurales.decorator.NotificadorConCopiaAdminDecorator;
import estructurales.decorator.NotificadorUrgenteDecorator;
import java.time.LocalDateTime;
import notificacion.EmailNotificador;
import notificacion.NotificadorService;
import politicas.PoliticaDescuento;

/**
 *
 * @author daferarte
 */
public class MainSemana3 {
    public static void main(String[] args) {
        System.out.println("=== 1. PATRÓN SINGLETON ===");
        ConfiguracionSistema config = ConfiguracionSistema.getInstancia();
        System.out.println("Moneda configurada: " + config.getMonedaPorDefecto() + " | IVA: " + (config.getTasaIva() * 100) + "%");

        System.out.println("\n=== 2. PATRÓN BUILDER ===");
        Cliente cliente = new Cliente("Carolina Herrera", new Email("carolina@moda.com"));
        Habitacion habitacion = new Habitacion(new NumeroHabitacion("P04-401"), 2);

        Reserva reserva = new ReservaBuilder()
                .paraCliente(cliente)
                .enHabitacion(habitacion)
                .desde(LocalDateTime.now().plusDays(3))
                .hasta(LocalDateTime.now().plusDays(12))
                .build();
        System.out.println("Reserva creada exitosamente con Builder para: " + reserva.getCliente().getNombre());

        System.out.println("\n=== 3. PATRÓN FACTORY METHOD ===");
        PoliticaDescuento politica = PoliticaDescuentoFactory.crearPolitica(
                PoliticaDescuentoFactory.TipoPolitica.ESTADIA_LARGA,
                reserva.getPeriodo()
        );
        double totalConDescuento = politica.aplicarDescuento(500.0);
        System.out.println("Precio final procesado por fábrica: $" + totalConDescuento);

        System.out.println("\n=== 4. PATRÓN ADAPTER ===");
        PasarelaExternaPago sdkTercero = new PasarelaExternaPago();
        ProcesadorPago procesador = new PasarelaPagoAdapter(sdkTercero);
        boolean pagoExitoso = procesador.cobrar(cliente, totalConDescuento);
        System.out.println("¿Pago procesado vía Adapter?: " + pagoExitoso);

        System.out.println("\n=== 5. PATRÓN DECORATOR ===");
        // Combinamos dinámicamente Email + Decorador Urgente + Decorador Copia Administrador
        NotificadorService notificadorDecorado = new NotificadorConCopiaAdminDecorator(
                new NotificadorUrgenteDecorator(
                        new EmailNotificador()
                )
        );

        notificadorDecorado.notificar(
                cliente, 
                "Su reserva " + reserva.getId() + " fue pagada y confirmada."
        );
    }
}
