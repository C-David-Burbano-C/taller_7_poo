package com.mycompany.hotel.web.mapper;

import com.mycompany.hotel.dominio.modelo.CapacidadMaxima;
import com.mycompany.hotel.dominio.modelo.Dinero;
import com.mycompany.hotel.dominio.modelo.Email;
import com.mycompany.hotel.dominio.modelo.NumeroHabitacion;
import java.math.BigDecimal;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

/** Conversiones Value Object → tipo primitivo/JSON compartidas por todos los mappers. */
@Component
public class ConversionesDominio {

    public String texto(NumeroHabitacion numero) {
        return numero.valor();
    }

    public String texto(Email email) {
        return email.valor();
    }

    public int entero(CapacidadMaxima capacidad) {
        return capacidad.valor();
    }

    public BigDecimal cantidad(Dinero dinero) {
        return dinero.cantidad();
    }

    /** Variante calificada (@Named) para los agregados calculados del resumen. */
    @Named("dineroACantidad")
    public BigDecimal dineroACantidad(Dinero dinero) {
        return dinero.cantidad();
    }
}
