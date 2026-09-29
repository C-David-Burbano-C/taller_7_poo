/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructurales.adapter;

import com.mycompany.sistema.reservas.dominio.modelo.Reserva;

/**
 *
 * @author daferarte
 */
public class DianInvoiceAdapter implements FacturadorElectronico {
    private final DianInvoiceSDK dianSdk;
    private final String nitHotel = "900.123.456-7";

    public DianInvoiceAdapter(DianInvoiceSDK dianSdk) {
        this.dianSdk = dianSdk;
    }

    @Override
    public String emitirFactura(Reserva reserva, double total) {
        // Traducción de modelo de objetos a XML en bytes
        String xml = "<factura><cliente>" + reserva.getCliente().getNombre() + "</cliente><total>" + total + "</total></factura>";
        byte[] payload = xml.getBytes();

        long codigoTransaccion = dianSdk.sendXmlDocument(nitHotel, payload);
        return "RADICADO-" + codigoTransaccion;
    }
}
