/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Creacionales.singleton;

import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author daferarte
 */
public class GeneradorFolioFiscal {
    private final AtomicLong contador;

    private GeneradorFolioFiscal() {
        this.contador = new AtomicLong(10000); 
    }

    // Patrón Holder estático para inicialización segura (Lazy Load + Thread Safe)
    private static class Holder {
        private static final GeneradorFolioFiscal INSTANCE = new GeneradorFolioFiscal();
    }

    public static GeneradorFolioFiscal getInstance() {
        return Holder.INSTANCE;
    }

    public String generarFolio() {
        return "FAC-2026-" + contador.getAndIncrement();
    }
}
