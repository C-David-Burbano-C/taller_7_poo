/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sistema.reservas.dominio;

import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Email;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.NumeroHabitacion;
import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import com.mycompany.sistema.reservas.dominio.modelo.SuitePresidencial;
import comportamentales.observer.GestorEventosReserva;
import comportamentales.observer.ReservaObserver;
import comportamentales.strategy.CancelacionModerada;
import comportamentales.strategy.EstrategiaCancelacion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 *
 * @author daferarte
 */
public class MainSemana5 {
    public static void main(String[] args) {
        System.out.println("   SEMANA 5: PERSISTENCIA ORIENTADA A OBJETOS (JPA / POSTGRESQL)");
        // 1. Inicialización del contexto de persistencia JPA
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HotelPU");
        EntityManager em = emf.createEntityManager();

        UUID idReservaPersistida = null;
        try {
            // =========================================================================
            // PASO 1: TRANSACCIÓN DE CREACIÓN Y PERSISTENCIA DE ENTIDADES RICAS
            // =========================================================================
            em.getTransaction().begin();

            System.out.println(">> Creando entidades del dominio...");
            Cliente cliente = new Cliente("Valentina Duque", new Email("valentina.duque@empresa.com"));
            
            // Jerarquía polimórfica: SuitePresidencial hereda de Habitacion (JOINED)
            Habitacion suite = new SuitePresidencial(
                    new NumeroHabitacion("P10-101"), 
                    4, 
                    300.0, 
                    true,  // incluyeMayordomo
                    true   // jacuzziPrivado
            );

            RangoFechas periodo = new RangoFechas(
                    LocalDateTime.now().plusDays(2), 
                    LocalDateTime.now().plusDays(7)
            );

            // Se instancia la entidad rica (nace con EstadoPendiente y RangoFechas incrustado)
            Reserva reserva = new Reserva(cliente, suite, periodo);
            idReservaPersistida = reserva.getId();

            // Configuración de colaboradores volátiles (@Transient en memoria)
            GestorEventosReserva gestorEventos = new GestorEventosReserva();
            gestorEventos.suscribir(new ReservaObserver() {
                @Override
                public void onReservaCancelada(Reserva r) {
                    System.out.println("[OBSERVER NOTIFICACIÓN] Reserva cancelada: " + r.getId());
                }
            });
            reserva.setGestorEventos(gestorEventos);

            EstrategiaCancelacion moderada = new CancelacionModerada();
            reserva.setEstrategiaCancelacion(moderada);

            // Ejecución de método de negocio antes de persistir (Cero setters anémicos)
            System.out.println(">> Confirmando reserva según lógica de dominio...");
            reserva.confirmar(); // Cambia estado a EstadoConfirmada y la habitación a OCUPADA

            // Guardamos las entidades en cascada y orden referencial
            em.persist(suite);   // Inserta en 'habitaciones' y 'suites_presidenciales'
            em.persist(cliente); // Inserta en 'clientes'
            em.persist(reserva); // Inserta en 'reservas' con FKs y convertidor de State

            em.getTransaction().commit();
            System.out.println("Transacción 1 completada: Datos persistidos en PostgreSQL.\n");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            System.err.println("Error en Transacción 1: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close(); // Cerramos el primer EntityManager para vaciar la memoria caché de nivel 1
        }
        
        // =============================================================================
        // PASO 2: RECUPERACIÓN DESDE POSTGRESQL Y EJECUCIÓN DE COMPORTAMIENTO POLIMÓRFICO
        // =============================================================================
        EntityManager emLectura = emf.createEntityManager();
        try {
            emLectura.getTransaction().begin();

            System.out.println(">> Recuperando la Reserva desde PostgreSQL por UUID: " + idReservaPersistida);
            Reserva reservaRecuperada = emLectura.find(Reserva.class, idReservaPersistida);

            System.out.println("\n--- VERIFICACIÓN DE MAPEO Y RECONSTITUCIÓN DE OBJETOS ---");
            System.out.println("Cliente titular: " + reservaRecuperada.getCliente().getNombre());
            System.out.println("Correo del titular (Value Object): " + reservaRecuperada.getCliente().getEmail().valor());
            System.out.println("Habitación asignada: " + reservaRecuperada.getHabitacion().getNumero().valor());
            System.out.println("Tipo concreto recuperado (Herencia): " + reservaRecuperada.getHabitacion().getClass().getSimpleName());
            System.out.println("Instancia de Estado (Patrón State recuperado vía Converter): " + reservaRecuperada.getEstado().getClass().getSimpleName());
            System.out.println("Total calculado: $" + reservaRecuperada.getTotal());

            // Re-inyectamos los colaboradores de memoria (@Transient) para procesar reglas
            reservaRecuperada.setEstrategiaCancelacion(new CancelacionModerada());
            GestorEventosReserva gestor = new GestorEventosReserva();
            gestor.suscribir(r -> System.out.println("[HOUSEKEEPING OBSERVER] Liberando suite " + r.getHabitacion().getNumero().valor()));
            reservaRecuperada.setGestorEventos(gestor);

            System.out.println("\n>> Cancelando la reserva cargada de base de datos...");
            int diasRestantes = 2;
            reservaRecuperada.cancelar(diasRestantes); // Dispara State, Strategy y Observer

            System.out.println("Nuevo estado en memoria: " + reservaRecuperada.getEstado().getClass().getSimpleName());
            System.out.println("Estado de la habitación en memoria: " + reservaRecuperada.getHabitacion().getEstado());

            // Al hacer commit, Hibernate detecta los cambios y sincroniza el UPDATE en PostgreSQL
            emLectura.getTransaction().commit();
            System.out.println("\nTransacción 2 completada: Estado actualizado en PostgreSQL mediante Dirty Checking.");

        } catch (Exception e) {
            if (emLectura.getTransaction().isActive()) emLectura.getTransaction().rollback();
            System.err.println("Error en Transacción 2: " + e.getMessage());
            e.printStackTrace();
        } finally {
            emLectura.close();
            emf.close();
        }
    }
}
