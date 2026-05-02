package com.example.backend.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PagoTest {

    @Test
    void testConstructorYGetters() {
        Reserva reserva = new Reserva();
        Pago pago = new Pago("TARJETA_CREDITO", 250.0, reserva);

        assertEquals("TARJETA_CREDITO", pago.getMetodoPago());
        assertEquals(250.0, pago.getImporte());
        assertEquals(reserva, pago.getReserva());
    }

    @Test
    void testConstructorVacio() {
        Pago pago = new Pago();
        assertNull(pago.getMetodoPago());
        assertEquals(0.0, pago.getImporte());
    }

    @Test
    void testSetters() {
        Pago pago = new Pago();
        pago.setMetodoPago("TARJETA_DEBITO");
        pago.setImporte(150.0);
        pago.setReferencia(1L);

        assertEquals("TARJETA_DEBITO", pago.getMetodoPago());
        assertEquals(150.0, pago.getImporte());
        assertEquals(1L, pago.getReferencia());
    }
}