package comportamentales.strategy;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;

/**
 * Estrategia de cancelación moderada:
 * - Si se cancela con menos de 3 días: penalización del 25% del total.
 * - Si se cancela con 3 o más días: penalización del 5% por gastos administrativos.
 */
public class CancelacionModerada implements EstrategiaCancelacion {
    @Override
    public double calcularMulta(Reserva reserva, int diasRestantes) {
        if (diasRestantes < 3) {
            return reserva.getTotal() * 0.25;
        }
        return reserva.getTotal() * 0.05;
    }
}
