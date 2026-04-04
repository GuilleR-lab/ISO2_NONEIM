import "../App.css"
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { Home } from "lucide-react";



const SolicitudesReservas = () => {
    const [solicitudesN, setSolicitudesN] = useState(0);
    const [solicitudesList, setSolicitudesList] = useState([]);
    const navigate = useNavigate();
    const [username, setUsername] = useState(null);
    const [usuarioId, setUsuarioId] = useState(null);
    const [esPropietario, setEsPropietario] = useState(false);

    useEffect(() => {
        const storedUser = sessionStorage.getItem("username");
        const storedId = sessionStorage.getItem("userId");
        const storedRol = sessionStorage.getItem("rol");

        if (storedUser) setUsername(storedUser);
        if (storedId) setUsuarioId(storedId);
        if (storedRol === "PROPIETARIO") setEsPropietario(true);
    }, []);


    useEffect(() => { 
        const fetchSolicitudesN = async () => { 
            if (!usuarioId || !esPropietario) return;

            try {
                const res = await fetch(`http://localhost:8090/api/solicitudes/propietario/${usuarioId}/pendientes/count`);
                const data = await res.json();
                setSolicitudesN(data.count);
            } catch (error) {
                console.error("Error al cargar solicitudes:", error);
            }
        };

        const fetchSolicitudesList = async () => {
            if (!usuarioId || !esPropietario) return;

            try {
                const res = await fetch(`http://localhost:8090/api/solicitudes/propietario/${usuarioId}/pendientes`);
                const data = await res.json();
                setSolicitudesList(data);
             } catch (error) {
                console.error("Error al cargar solicitudes:", error);
            }
        }

    fetchSolicitudesN(); 
    fetchSolicitudesList();
}, [usuarioId, esPropietario]);

    const handleAccion = async (idSolicitud, nuevoEstado) => {
        const mensaje = nuevoEstado === "ACEPTADA" ? "¿Aceptar esta reserva?" : "¿Rechazar esta reserva?";
        if(!window.confirm(mensaje)) return;

        try{
            const response = await fetch(`http://localhost:8090/api/solicitudes/${idSolicitud}/estado`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    // Si usas autenticación, añade el token aquí:
                    // 'Authorization': `Bearer ${sessionStorage.getItem("token")}`
                },
                body: JSON.stringify({ 
                    estado: nuevoEstado // Enviamos el string "ACEPTADA" o "RECHAZADA"
                })
            });

            if (response.ok) {
                // OPTIMISMO: Eliminamos la solicitud de la lista local para que desaparezca visualmente
                setSolicitudesList((prevSolicitudes) => 
                    prevSolicitudes.filter(sol => sol.idSolicitud !== idSolicitud)
                );

                // Opcional: Feedback visual
                alert(`Solicitud ${nuevoEstado === "ACEPTADA" ? "aceptada" : "rechazada"} correctamente.`);
            } else {
                const errorData = await response.json();
                alert(`Error: ${errorData.message || "No se pudo actualizar la solicitud"}`);
            }
        }catch (error) {
        console.error("Error en la conexión:", error);
        alert("Error de conexión con el servidor.");
        }
    };

    return (
        <div className="solicitudes-container">
        {/* Estos elementos siempre estarán visibles */}
        <header className="solicitudes-header">
            <button className="dashboard-home-btn" onClick={() => navigate("/")}>
                    <Home size={20} /> NONEIM
                </button>
            <h1>Solicitudes de Reserva</h1>
        </header>
        <h3>Aquí podrás ver y gestionar tus solicitudes de reserva.</h3>

        <div className="grid-dashboard">
            
            {solicitudesList.length === 0 ? (
                /* Este mensaje solo sale si el array está vacío, 
                pero debajo del título anterior */
                <p className="no-results">No tienes solicitudes de reserva pendientes.</p>
            ) : (
                solicitudesList.map(solicitud => (
                    <div key={solicitud.idSolicitud} className="dash-card">
                        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                            <h3>Solicitud #{solicitud.idSolicitud}</h3>
                            <span className="badge-pendiente">⏳ Pendiente</span>
                        </div>

                        {/* Datos del Inmueble */}
                        {solicitud.disponibilidad?.inmueble && (
                            <div className="info-row">
                                <span><strong>Inmueble:</strong></span> {solicitud.disponibilidad.inmueble.ciudad} — {solicitud.disponibilidad.inmueble.direccion}
                            </div>
                        )}

                        {/* Datos del Inquilino */}
                        <div className="info-row">
                            <span><strong>Inquilino:</strong></span> {solicitud.usuario?.username || "Usuario desconocido"}
                        </div>

                        <div className="info-row"><span><strong>Entrada:</strong></span> {solicitud.fechaInicio}</div>
                        <div className="info-row"><span><strong>Salida:</strong></span> {solicitud.fechaFin}</div>
                        
                        {/* Botones de acción */}
                        <div className="acciones-solicitud" style={{ marginTop: "15px", display: "flex", gap: "10px" }}>
                            <button 
                                className="btn-aceptar" 
                                onClick={() => handleAccion(solicitud.idSolicitud, "ACEPTADA")}
                                style={{ backgroundColor: "#28a745", color: "white", border: "none", padding: "8px 15px", borderRadius: "5px", cursor: "pointer" }}
                            >
                                ✅ Aceptar
                            </button>
                            <button 
                                className="btn-rechazar" 
                                onClick={() => handleAccion(solicitud.idSolicitud, "RECHAZADA")}
                                style={{ backgroundColor: "#dc3545", color: "white", border: "none", padding: "8px 15px", borderRadius: "5px", cursor: "pointer" }}
                            >
                                ❌ Rechazar
                            </button>
                        </div>
                    </div>
                ))
            )}
        </div>
    </div>
    );
	
}
export default SolicitudesReservas;