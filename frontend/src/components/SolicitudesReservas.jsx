import "../App.css";
import { useEffect, useState, useRef, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { Home, ClipboardList, Key, Check, X, Hourglass } from "lucide-react";

// --- SUB-COMPONENTE 1:  (VISTA INQUILINO) ---
const MisSolicitudesEnviadas = ({ userId, onUpdate }) => {
    const [solicitudes, setSolicitudes] = useState([]);

    const cargarSolicitudes = useCallback(() => {
        fetch(`http://localhost:8090/api/solicitudes/inquilino/usuario/${userId}`)
            .then(res => res.json())
            .then(data => {
                const listaRaw = Array.isArray(data) ? data : [];
                const soloMias = listaRaw.filter(sol => Number(sol.usuario?.id) === Number(userId));
                setSolicitudes(soloMias);
            })
            .catch(err => console.error(err));
    }, [userId]);

    useEffect(() => {
        cargarSolicitudes();
    }, [cargarSolicitudes]); 

    useEffect(() => {
        if(onUpdate) onUpdate(cargarSolicitudes); 
    }, [onUpdate, cargarSolicitudes]);

    return (
        <div className="seccion-solicitudes">
            <h3>Peticiones de Alquiler Enviadas</h3>
            <p className="instrucciones-vista">Aquí ves el estado de las casas que tú has solicitado alquilar.</p>
            
            {solicitudes.length === 0 ? (
                <p className="no-results">No has enviado ninguna solicitud aún.</p>
            ) : (
                <div className="grid-dashboard">
                    {solicitudes.map(sol => (
                        <div key={sol.idSolicitud} className="dash-card tenant-view">
                            <div className="card-header-flex">
                                <span className={`badge-${sol.estado.toLowerCase()}`}>
                                    {sol.estado === 'PENDIENTE' ? "⏳ Pendiente" : ""}
                                    {sol.estado === 'ACEPTADA' ? "✅ Aceptada" : sol.estado === 'RECHAZADA' ? "❌ Rechazada" : ""}
                                </span>
                            </div>

                            <h4>{sol.disponibilidad?.inmueble?.ciudad}</h4>
                            <p className="direccion-texto">📍 {sol.disponibilidad?.inmueble?.direccion}</p>
                            
                            <div className="info-estancia">
                                <strong>Tu estancia:</strong>
                                <p>{sol.fechaInicio} hasta {sol.fechaFin}</p>
                            </div>

                            {sol.estado === 'ACEPTADA' ? (
                                <div className="msg-exito">
                                    <p style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                                        <Check size={18} color="green" /> 
                                        <span>¡El propietario aceptó tu solicitud!</span>
                                    </p>
                                </div>
                            ) : sol.estado === 'RECHAZADA' ? (
                                <div className="msg-error">
                                    <p style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                                        <X size={18} color="red" /> 
                                        <span>Lo sentimos, el propietario rechazó tu solicitud.</span>
                                    </p>
                                    {/*Debería redirigir a una pagina para la debolución del importe*/}
                                </div>
                            ) : sol.estado === 'PENDIENTE' ? (
                                <div className="msg-pendiente">
                                    <p style={{ display: "flex", alignItems: "center", gap: "8px" }}>
                                        <Hourglass size={18} color="orange" /> 
                                        <span> Tu solicitud está pendiente de respuesta.</span>
                                    </p>
                                </div>
                            ) : null}

                        
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

// --- SUB-COMPONENTE 2:  (VISTA PROPIETARIO) ---
const GestionSolicitudesRecibidas = ({ userId, handleAccion, onUpdate }) => {
    const [pendientes, setPendientes] = useState([]);

    const cargarPendientes = useCallback(() => {
        fetch(`http://localhost:8090/api/solicitudes/propietario/${userId}/pendientes`)
            .then(res => res.json())
            .then(data => setPendientes(Array.isArray(data) ? data : []))
            .catch(err => console.error(err));
    }, [userId]);

    useEffect(() => {
        cargarPendientes();
    }, [cargarPendientes]);

    useEffect(() => {
        if(onUpdate) onUpdate(cargarPendientes); // Si se pasó una función de actualización, la registramos para que el padre pueda llamar a esta función y refrescar la lista.
    }, [onUpdate, cargarPendientes]);

    return (
        <div className="seccion-solicitudes">
            <h3>Solicitudes por Aprobar</h3>
            {pendientes.length === 0 ? (
                <p className="no-results">No tienes solicitudes pendientes de tus inmuebles.</p>
            ) : (
                <div className="grid-dashboard">
                    {pendientes.map(sol => (
                        <div key={sol.idSolicitud} className="dash-card owner-view">
                            <p> <strong>Inquilino: </strong> {sol.usuario?.username}, {sol.usuario?.name} {sol.usuario?.surname}</p>
                            <p><strong>Propiedad:</strong> {sol.disponibilidad?.inmueble?.direccion}, {sol.disponibilidad?.inmueble?.ciudad}</p>
                            <div className="acciones-solicitud" style={{ display: "flex", justifyContent: "space-between", marginTop: "20px", width: "100%" }}>
                                <button className="btn-aceptar" onClick={() => handleAccion(sol.idSolicitud, "ACEPTADA")}>Aceptar</button>
                                <button className="btn-rechazar" onClick={() => handleAccion(sol.idSolicitud, "RECHAZADA")}>Rechazar</button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

// --- COMPONENTE PRINCIPAL ---
const SolicitudesReservas = () => {
    const [activeTab, setActiveTab] = useState("enviadas");
    const [usuarioId, setUsuarioId] = useState(sessionStorage.getItem("userId"));
    const [esPropietario, setEsPropietario] = useState(sessionStorage.getItem("rol") === "PROPIETARIO");
    const [cargando, setCargando] = useState(true);
    const navigate = useNavigate();
    const refreshFuncRef = useRef(null);

    useEffect(() => {
        const storedId = sessionStorage.getItem("userId");
        const storedRol = sessionStorage.getItem("rol");

        if (!storedId) {
            // Si no hay ID, redirigimos inmediatamente al login
            navigate("/auth");
        } else {
            // Si hay sesión, configuramos los datos y quitamos el estado de carga
            setUsuarioId(storedId);
            setEsPropietario(storedRol === "PROPIETARIO");
            setCargando(false);
        }
    }, [navigate]);

    if (cargando) {
        return (
            <div className="loading-screen">
                <p>Verificando sesión...</p>
            </div>
        );
    }

    const handleAccion = async (idSolicitud, nuevoEstado) => {
        const mensaje = nuevoEstado === "ACEPTADA" ? "¿Aceptar esta reserva?" : "¿Rechazar esta reserva?";
        if(!window.confirm(mensaje)) return;

        try {
            const response = await fetch(`http://localhost:8090/api/solicitudes/${idSolicitud}/estado`, {
                method: 'PATCH',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ estado: nuevoEstado })
            });

            if (response.ok) {
                alert(`Solicitud gestionada con éxito.`);

                if (refreshFuncRef.current) {
                    refreshFuncRef.current(); 
                } 
            }
        } catch (error) {
            console.error("Error en la conexión:", error);
        }
    };

    return (
        
        <div className="solicitudes-container">
            <header className="solicitudes-header">
                <button className="dashboard-home-btn" onClick={() => navigate("/")}><Home size={20} /> NONEIM</button>
                <h2>Centro de Solicitudes</h2>
            </header>

            {/* Selector de Vistas (Tabs) */}
            <div className="tabs-selector">
                <button 
                    className={`btn-tab-reservas ${activeTab === "enviadas" ? "active" : ""}`} 
                    onClick={() => setActiveTab("enviadas")}
                >
                    <ClipboardList size={18} /> Mis Peticiones
                </button>
                
                {esPropietario && (
                    <button
                        className={`btn-tab-gestion ${activeTab === "recibidas" ? "active" : ""}`} 
                        onClick={() => setActiveTab("recibidas")}
                    >
                        <Key size={18} /> Gestionar mis Alquileres
                    </button>
                )}
            </div>

            <main className="dashboard-content">
                {activeTab === "enviadas" ? (
                    <MisSolicitudesEnviadas userId={usuarioId} onUpdate={(fn) => { refreshFuncRef.current = fn; }}/>
                ) : (
                    <GestionSolicitudesRecibidas userId={usuarioId} handleAccion={handleAccion} onUpdate={(fn) => { refreshFuncRef.current = fn; }}/>
                )}
            </main>
        </div>
    );
};

export default SolicitudesReservas;