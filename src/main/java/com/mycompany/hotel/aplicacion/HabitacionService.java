package com.mycompany.hotel.aplicacion;

import com.mycompany.hotel.dominio.excepcion.ReglaNegocioException;
import com.mycompany.hotel.dominio.modelo.Habitacion;
import com.mycompany.hotel.dominio.puertos.HabitacionRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HabitacionService {

    private final HabitacionRepository habitaciones;

    public HabitacionService(HabitacionRepository habitaciones) {
        this.habitaciones = habitaciones;
    }

    @Transactional
    public Habitacion crear(ComandoCrearHabitacion comando) {
        Habitacion nueva = comando.construir();
        if (habitaciones.existePorNumero(nueva.getNumero())) {
            throw new ReglaNegocioException("Ya existe la habitación " + nueva.getNumero().valor());
        }
        return habitaciones.guardar(nueva);
    }

    @Transactional(readOnly = true)
    public List<Habitacion> listar() {
        return habitaciones.buscarTodas();
    }
}
