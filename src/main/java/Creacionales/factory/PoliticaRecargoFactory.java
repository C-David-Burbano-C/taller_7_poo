/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.factory;

/**
 *
 * @author daferarte
 */
public class PoliticaRecargoFactory {
    public static PoliticaRecargo crearPolitica(TipoRecargo tipo) {
        return switch (tipo) {
            case TEMPORADA_ALTA -> new RecargoTemporadaAlta();
            case CANCELACION_TARDIA -> new RecargoCancelacionTardia();
            case NINGUNO -> new RecargoSinPenalizacion();
        };
    }
}
