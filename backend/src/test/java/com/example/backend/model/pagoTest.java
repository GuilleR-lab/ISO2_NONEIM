package com.example.backend.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PagoTest {

    @Test
    void testConstructorYGetters() {
        Reserva reserva = new Reserva();
        Pago pago = new Pago("TARJETA_CREDITO", 250.0, reserva);

        assertEquals("TARJETA_CREDITO", pago.getMetodoPago(), "El método de pago debe ser TARJETA_CREDITO");
        assertEquals(250.0, pago.getImporte(), "El importe debe ser 250.0");
        assertEquals(reserva, pago.getReserva(), "La reserva debe coincidir");
    }

    @Test
    void testConstructorVacio() {
        Pago pago = new Pago();
        assertNull(pago.getMetodoPago(), "El método de pago debe ser nulo en constructor vacío");
        assertEquals(0.0, pago.getImporte(), "El importe debe ser 0.0 en constructor vacío");
    }

    @Test
    void testSetters() {
        Pago pago = new Pago();
        pago.setMetodoPago("TARJETA_DEBITO");
        pago.setImporte(150.0);
        pago.setReferencia(1L);

        assertEquals("TARJETA_DEBITO", pago.getMetodoPago(), "El método de pago debe haberse actualizado a TARJETA_DEBITO");
        assertEquals(150.0, pago.getImporte(), "El importe debe haberse actualizado a 150.0");
        assertEquals(1L, pago.getReferencia(), "La referencia debe ser 1");
    }
}