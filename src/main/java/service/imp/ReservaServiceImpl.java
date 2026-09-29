package service.imp;


import com.mycompany.sistema.reservas.dominio.modelo.Cliente;
import com.mycompany.sistema.reservas.dominio.modelo.Habitacion;
import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import com.mycompany.sistema.reservas.dominio.modelo.Reserva;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import repository.ReservaRepository;
import repository.ClienteRepository;
import repository.HabitacionRepository;

import service.ReservaService;
import service.ReservaService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author daferarte
 */
@Service
public class ReservaServiceImpl implements ReservaService {

    // Dependencias declaradas como interfaces inmutables
    private final ReservaRepository reservaRepository;
    private final HabitacionRepository habitacionRepository;
    private final ClienteRepository clienteRepository;

    // Inyección de dependencias por constructor (Buena práctica: sin @Autowired explícito en Spring moderno)
    public ReservaServiceImpl(ReservaRepository reservaRepository,
                              HabitacionRepository habitacionRepository,
                              ClienteRepository clienteRepository) {
        this.reservaRepository = reservaRepository;
        this.habitacionRepository = habitacionRepository;
        this.clienteRepository = clienteRepository;
    }

    @Override
    @Transactional
    public Reserva crearReserva(UUID clienteId, UUID habitacionId, RangoFechas periodo) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

        Habitacion habitacion = habitacionRepository.findById(habitacionId)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada"));

        // El dominio rico protege sus propias reglas al instanciarse
        Reserva nuevaReserva = new Reserva(cliente, habitacion, periodo);
        return reservaRepository.save(nuevaReserva);
    }

    @Override
    @Transactional
    public Reserva confirmarReserva(UUID reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        // Delegación del comportamiento a la entidad rica (Semana 4 y 5)
        reserva.confirmar();

        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public Reserva cancelarReserva(UUID reservaId) {
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new IllegalArgumentException("Reserva no encontrada"));

        reserva.cancelar();

        return reservaRepository.save(reserva);
    }
}
