package com.example.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReserva;

    @Column(nullable = false)
    private LocalDate fechaInicio;

    @Column(nullable = false)
    private LocalDate fechaFin;

    @Column(nullable = false)
    private boolean isPagado;

    @Column(nullable = false)
    private boolean isActiva;

    // Inquilino que hace la reserva
    @ManyToOne
    @JoinColumn(name = "inquilino_id", nullable = false)
    private Usuario inquilino;

    @ManyToOne
    @JoinColumn(name = "idDisponibilidad", nullable = false)
    private Disponibilidad disponibilidad;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "inmueble_id", nullable = false)
    private Inmueble inmueble;

    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Pago pago;

    @ManyToOne
    @JoinColumn(name = "idPolitica")  // opcional
    private PoliticaCancelacion politicaCancelacion;

    @OneToOne
    @JoinColumn(name = "idSolicitud")
    private SolicitudReserva solicitudReserva;

    public Reserva() {}

    public Long getIdReserva() { return idReserva; }
    public void setIdReserva(Long idReserva) { this.idReserva = idReserva; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public boolean isPagado() { return isPagado; }
    public void setPagado(boolean pagado) { isPagado = pagado; }
    public boolean isActiva() { return isActiva; }
    public void setActiva(boolean activa) { isActiva = activa; }
    public Usuario getInquilino() { return inquilino; }
    public void setInquilino(Usuario inquilino) { this.inquilino = inquilino; }
    public Disponibilidad getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Disponibilidad disponibilidad) { this.disponibilidad = disponibilidad; }
    public Inmueble getInmueble() { return inmueble; }
    public void setInmueble(Inmueble inmueble) { this.inmueble = inmueble; }
    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }
    public PoliticaCancelacion getPoliticaCancelacion() { return politicaCancelacion; }
    public void setPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) { this.politicaCancelacion = politicaCancelacion; }
    public SolicitudReserva getSolicitudReserva() { return solicitudReserva; }
    public void setSolicitudReserva(SolicitudReserva solicitudReserva) { this.solicitudReserva = solicitudReserva; }
}