/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.factory;

import com.mycompany.sistema.reservas.dominio.modelo.RangoFechas;
import politicas.DescuentoCorporativo;
import politicas.DescuentoEstadiaLarga;
import politicas.DescuentoRegular;
import politicas.DescuentoTemporadaBaja;
import politicas.DescuentoVIP;
import politicas.PoliticaDescuento;

/**
 *
 * @author daferarte
 */
public class PoliticaDescuentoFactory {
    public enum TipoPolitica {
        VIP, CORPORATIVO, TEMPORADA_BAJA, ESTADIA_LARGA, REGULAR
    }

    public static PoliticaDescuento crearPolitica(TipoPolitica tipo, RangoFechas periodo) {
        return switch (tipo) {
            case VIP -> new DescuentoVIP();
            case CORPORATIVO -> new DescuentoCorporativo();
            case TEMPORADA_BAJA -> new DescuentoTemporadaBaja();
            case ESTADIA_LARGA -> {
                if (periodo == null) {
                    throw new IllegalArgumentException("Se requiere el periodo para la política de estadía larga");
                }
                yield new DescuentoEstadiaLarga(periodo);
            }
            case REGULAR -> new DescuentoRegular();
        };
    }
}
