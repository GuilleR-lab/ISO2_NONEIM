import "../App.css";
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

const Reservar = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const [inmueble, setInmueble] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [fechaInicio, setFechaInicio] = useState("");
    const [fechaFin, setFechaFin] = useState("");
    const [metodoPago, setMetodoPago] = useState("TARJETA_CREDITO");
    const [mensaje, setMensaje] = useState("");
    const [mensajeColor, setMensajeColor] = useState("red");
    const [procesando, setProcesando] = useState(false);

    const inquilinoId = sessionStorage.getItem("userId");
    const rol = sessionStorage.getItem("rol");

    useEffect(() => {
        if (!inquilinoId) {
            navigate("/auth");
            return;
        }

        fetch(`http://localhost:8090/api/inmuebles/${id}`)
            .then(res => res.json())
            .then(data => {
                setInmueble(data);
                setCargando(false);
            })
            .catch(() => setCargando(false));
    }, [id, inquilinoId, rol, navigate]);

    const calcularImporte = () => {
        if (!fechaInicio || !fechaFin) return 0;
        const dias = (new Date(fechaFin) - new Date(fechaInicio)) / (1000 * 60 * 60 * 24);
        return dias > 0 ? dias * inmueble.precioNoche : 0;
    };

    const handleReservar = async () => {
        if (!fechaInicio || !fechaFin) {
            setMensaje("Debes seleccionar las fechas de entrada y salida");
            setMensajeColor("red");
            return;
        }

        if (new Date(fechaFin) <= new Date(fechaInicio)) {
            setMensaje("La fecha de salida debe ser posterior a la de entrada");
            setMensajeColor("red");
            return;
        }

        setProcesando(true);

        try {
            const response = await fetch("http://localhost:8090/api/reservas/reservar", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    inquilinoId: parseInt(inquilinoId),
                    inmuebleId: parseInt(id),
                    fechaInicio,
                    fechaFin,
                    metodoPago
                })
            });

            const data = await response.json();

            if (!response.ok) {
                setMensaje(data.message || "Error al procesar la reserva");
                setMensajeColor("red");
                setProcesando(false);
                return;
            }

            setMensajeColor("green");
            if (data.tipo === "DIRECTA") {
                setMensaje(`✅ Reserva confirmada. Importe pagado: ${data.importe}€`);
            } else {
                setMensaje(`📩 Solicitud enviada al propietario. Importe retenido: ${data.importe}€. Te notificaremos cuando confirme.`);
            }

        } catch (error) {
            setMensaje("Error al conectar con el servidor");
            setMensajeColor("red");
            setProcesando(false);
        }
    };

    if (cargando) return <p style={{ padding: 30 }}>Cargando...</p>;
    if (!inmueble) return <p style={{ padding: 30 }}>Inmueble no encontrado.</p>;

    const disponibilidad = inmueble.disponibilidades?.[0];
    const importe = calcularImporte();

    return (
        <div className="detalle-container">
            <button className="btn-volver" onClick={() => navigate(-1)}>← Volver</button>

            <div className="detalle-card">
                <h2>{disponibilidad?.directa ? "⚡ Reserva inmediata" : "📩 Solicitar reserva"}</h2>
                <p className="inmueble-direccion">📍 {inmueble.ciudad} — {inmueble.direccion}</p>
                <p><strong>{inmueble.precioNoche}€</strong> / noche</p>

                <div className="reserva-form">
                    <div className="reserva-fechas">
                        <div>
                            <label>Fecha de entrada</label>
                            <input
                                type="date"
                                value={fechaInicio}
                                min={new Date().toISOString().split("T")[0]}
                                onChange={(e) => setFechaInicio(e.target.value)}
                            />
                        </div>
                        <div>
                            <label>Fecha de salida</label>
                            <input
                                type="date"
                                value={fechaFin}
                                min={fechaInicio || new Date().toISOString().split("T")[0]}
                                onChange={(e) => setFechaFin(e.target.value)}
                            />
                        </div>
                    </div>

                    <div style={{ margin: "16px 0" }}>
                        <label><strong>Método de pago</strong></label>
                        <div style={{ display: "flex", gap: "16px", marginTop: 8, flexWrap: "wrap" }}>
                            {["TARJETA_CREDITO", "TARJETA_DEBITO", "PAYPAL"].map(m => (
                                <label key={m} style={{ display: "flex", alignItems: "center", gap: 6 }}>
                                    <input
                                        type="radio"
                                        value={m}
                                        checked={metodoPago === m}
                                        onChange={() => setMetodoPago(m)}
                                    />
                                    {m === "TARJETA_CREDITO" ? "Tarjeta crédito" :
                                     m === "TARJETA_DEBITO" ? "Tarjeta débito" : "PayPal"}
                                </label>
                            ))}
                        </div>
                    </div>

                    {importe > 0 && (
                        <div className="reserva-resumen">
                            <p>📅 {Math.round((new Date(fechaFin) - new Date(fechaInicio)) / (1000 * 60 * 60 * 24))} noches × {inmueble.precioNoche}€</p>
                            <p><strong>Total: {importe}€</strong></p>
                        </div>
                    )}

                    {mensaje && (
                        <div style={{ color: mensajeColor, margin: "12px 0", fontSize: "0.95em" }}>
                            {mensaje}
                        </div>
                    )}

                    {!mensaje && (
                        <button
                            className="btn-reservar"
                            onClick={handleReservar}
                            disabled={procesando}
                        >
                            {procesando ? "Procesando..." :
                             disponibilidad?.directa ? "⚡ Confirmar reserva" : "📩 Enviar solicitud"}
                        </button>
                    )}

                    {mensaje && mensajeColor === "green" && (
                        <button className="btn-buscar" onClick={() => navigate("/")}>
                            Volver al inicio
                        </button>
                    )}
                </div>
            </div>
        </div>
    );
};

export default Reservar;