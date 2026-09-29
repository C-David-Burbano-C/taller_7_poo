/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructurales.adapter;

/**
 *
 * @author daferarte
 */
class DianInvoiceSDK {
    public long sendXmlDocument(String nitEmpresa, byte[] xmlPayload) {
        System.out.println("[DIAN SDK] Documento procesado para NIT: " + nitEmpresa);
        return System.currentTimeMillis(); 
    }
}
