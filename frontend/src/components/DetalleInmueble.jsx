import "../App.css";
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

const DetalleInmueble = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [inmueble, setInmueble] = useState(null);
    const [cargando, setCargando] = useState(true);

    const usuarioLogueado = sessionStorage.getItem("userId");
    //const rol = sessionStorage.getItem("rol");

    useEffect(() => {
        fetch(`http://localhost:8090/api/inmuebles/${id}`)
            .then(res => {
                if (!res.ok) throw new Error("Inmueble no encontrado");
                return res.json();
            })
            .then(data => {
                setInmueble(data);
                setCargando(false);
            })
            .catch(() => {
                setCargando(false);
            });
    }, [id]);

    if (cargando) return <p style={{ padding: 30 }}>Cargando...</p>;
    if (!inmueble) return <p style={{ padding: 30 }}>Inmueble no encontrado.</p>;

    const disponibilidad = inmueble.disponibilidades?.[0];

    return (
        <div className="detalle-container">
            <button className="btn-volver" onClick={() => navigate(-1)}>← Volver</button>

            <div className="detalle-card">
                <div className="detalle-header">
                    <span className="inmueble-tipo">
                        {inmueble.tipo === "VIVIENDA_COMPLETA" ? "🏠 Vivienda completa" : "🛏️ Habitación"}
                    </span>
                    {disponibilidad?.directa && (
                        <span className="badge-directa">⚡ Reserva inmediata</span>
                    )}
                </div>

                <h2>{inmueble.ciudad}</h2>
                <p className="inmueble-direccion">📍 {inmueble.direccion}</p>

                {inmueble.descripcion && (
                    <p className="inmueble-descripcion">{inmueble.descripcion}</p>
                )}

                <div className="detalle-info">
                    <div className="detalle-precio">
                        <strong>{inmueble.precioNoche} €</strong> / noche
                    </div>

                    {disponibilidad && (
                        <div className="detalle-fechas">
                            <p>📅 Disponible del <strong>{disponibilidad.fechaInicio}</strong> al <strong>{disponibilidad.fechaFin}</strong></p>
                            <p>Tipo de reserva: <strong>{disponibilidad.directa ? "Inmediata" : "Por solicitud"}</strong></p>
                        </div>
                    )}
                </div>

                {/* Lógica de comparaciones: Si no hay usuario logueado sse envía al Auth,
                si el usuario logueado es el propietario del inmuble no puede reservar ese inmueble,
                ya no se tiene en cuenta si tiene el rol de propietario o inquilino (si es propietario de otro
                inmueble sí podría reservarlo)
                */}
                {!usuarioLogueado ? (
                    <div className="detalle-aviso">
                        <p>Debes iniciar sesión para hacer una reserva.</p>
                        <button className="btn-buscar" onClick={() => navigate("/auth")}>
                            Iniciar sesión
                        </button>
                    </div>
                ) : String(usuarioLogueado) === String(inmueble.propietario.id) ? (

                    <div className="detalle-aviso">
                        <p>Eres propietario, no puedes reservar inmuebles.</p>

                        <button className="btn-secondary" onClick={() => navigate(`/propiedadform/${id}`)}>
                            Editar inmueble
                        </button>
                    </div>

                ) : (
                    <button className="btn-reservar" onClick={() => navigate(`/reservar/${id}`)}>
                        {disponibilidad?.directa ? "⚡ Reservar ahora" : "📩 Solicitar reserva"}
                    </button>
                )}

            </div>
        </div>
    );
};

export default DetalleInmueble;