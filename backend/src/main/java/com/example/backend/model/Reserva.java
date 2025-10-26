package com.example.backend.model;



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

    // Relación con Disponibilidad
    @ManyToOne
    @JoinColumn(name = "idDisponibilidad", nullable = false)
    private Disponibilidad disponibilidad;

    // Relación con Pago (1:1)
    @OneToOne(mappedBy = "reserva", cascade = CascadeType.ALL)
    private Pago pago;

    // Relación con PoliticaCancelacion (N:1)
    @ManyToOne
    @JoinColumn(name = "idPolitica", nullable = false)
    private PoliticaCancelacion politicaCancelacion;

    public Reserva() {}

    public Reserva(LocalDate fechaInicio, LocalDate fechaFin, boolean isPagado,
                   boolean isActiva, Disponibilidad disponibilidad) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.isPagado = isPagado;
        this.isActiva = isActiva;
        this.disponibilidad = disponibilidad;
    }

    // Getters y Setters
    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isPagado() {
        return isPagado;
    }

    public void setPagado(boolean pagado) {
        isPagado = pagado;
    }

    public boolean isActiva() {
        return isActiva;
    }

    public void setActiva(boolean activa) {
        isActiva = activa;
    }

    public Disponibilidad getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Disponibilidad disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

    public PoliticaCancelacion getPoliticaCancelacion() {
        return politicaCancelacion;
    }

    public void setPoliticaCancelacion(PoliticaCancelacion politicaCancelacion) {
        this.politicaCancelacion = politicaCancelacion;
    }
}
