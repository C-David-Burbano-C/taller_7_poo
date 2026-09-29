/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructurales.adapter;

/**
 *
 * @author daferarte
 */
public class PasarelaExternaPago {
    // SDK cerrado: cobra en centavos y pide un identificador en String
    public boolean executeTransaction(String customerRef, long amountInCents) {
        System.out.println("PasarelaExterna SDK Cobrando " + amountInCents + " centavos a ref: " + customerRef);
        return true;
    }
}
