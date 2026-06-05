import "../App.css";
import { FaUserCircle } from "react-icons/fa";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BsBell } from "react-icons/bs";

const HomePage = () => {
    const [username, setUsername] = useState(null);
    const [usuarioId, setUsuarioId] = useState(null);
    const [esPropietario, setEsPropietario] = useState(false);
    const [menuOpen, setMenuOpen] = useState(false);
    const [ciudad, setCiudad] = useState("");
    const [fechaInicio, setFechaInicio] = useState("");
    const [fechaFin, setFechaFin] = useState("");
    const [tipo, setTipo] = useState("");
    const [soloDirecta, setSoloDirecta] = useState(false);
    const [solicitudesPendientes, setSolicitudesPendientes] = useState(0);

    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = sessionStorage.getItem("username");
        const storedId = sessionStorage.getItem("userId");
        const storedRol = sessionStorage.getItem("rol");

        if (storedUser) setUsername(storedUser);
        if (storedId) setUsuarioId(storedId);
        if (storedRol === "PROPIETARIO") setEsPropietario(true);
    }, []);

    useEffect(() => {
        const fetchSolicitudes = async () => {
            if (!usuarioId) return;

            try {
                // --- 1. LÓGICA INQUILINO (Mis peticiones enviadas) ---
                const resInq = await fetch(`http://localhost:8090/api/solicitudes/inquilino/usuario/${usuarioId}`);
                const dataInq = await resInq.json();
                const listaInq = Array.isArray(dataInq) ? dataInq : [];
                
                // Notificamos si hay alguna que NO esté pendiente (es decir, ya respondida)
                // Esto solo se mostrará si el usuario no ha hecho click en la campana aún
                const countInq = listaInq.filter(sol => sol.estado !== "PENDIENTE").length;

                // --- 2. LÓGICA PROPIETARIO (Gestión de mis alquileres) ---
                let countProp = 0;
                if (esPropietario) {
                    // CAMBIO CLAVE: En lugar de usar el endpoint /count que falla,
                    // usamos el endpoint que trae la lista y filtramos aquí por PENDIENTE.
                    const resProp = await fetch(`http://localhost:8090/api/solicitudes/propietario/${usuarioId}/pendientes`);
                    const dataProp = await resProp.json();
                    const listaProp = Array.isArray(dataProp) ? dataProp : [];
                    
                    // Forzamos que solo cuente las que realmente están PENDIENTES
                    countProp = listaProp.filter(sol => sol.estado === "PENDIENTE").length;
                }

                setSolicitudesPendientes(countInq + countProp);

            } catch (error) {
                console.error("Error al sincronizar notificaciones:", error);
            }
        };

        fetchSolicitudes();
    }, [usuarioId, esPropietario]);
    
    const toggleMenu = () => {
        if (!username) navigate("/auth");
        else setMenuOpen(!menuOpen);
    };

    const handleLogout = () => {
        sessionStorage.clear();
        setUsername(null);
        setUsuarioId(null);
        setEsPropietario(false);
        setSolicitudesPendientes(0);
        setMenuOpen(false);
        navigate("/");
    };

    const handleBuscar = () => {
        const params = new URLSearchParams();
        if (ciudad) params.append("ciudad", ciudad);
        if (fechaInicio) params.append("fechaInicio", fechaInicio);
        if (fechaFin) params.append("fechaFin", fechaFin);
        if (tipo) params.append("tipo", tipo);
        if (soloDirecta) params.append("soloDirecta", "true");
        navigate(`/resultados?${params.toString()}`);
    };

    return (
        <div className="home-container">
            <h1>🏠 NONEIM</h1>
            <p>Tu portal de alquileres y viviendas.</p>

            {/*Contenedor de iconos del header*/}
            <div className="header-icons">
                {/* Icono solicitudes */}
                <div className="solicitud-icon" onClick={() => {
                    // Marcamos que el usuario ya "limpió" las notificaciones de esta sesión
                    sessionStorage.setItem("notificacionesLeidas", "true");
                    navigate("/solicitudes");
                }}>
                    <BsBell size={30} color="#007bff" />

                    {/* Lógica: Mostramos el badge si hay algo pendiente Y si no se han marcado como leídas */}
                    {(solicitudesPendientes > 0 && !sessionStorage.getItem("notificacionesLeidas")) && (
                        <span className="solicitud-badge">
                            {/* {solicitudesPendientes > 9 ? "9+" : solicitudesPendientes} */}
                        </span>
                    )}
                </div>


                {/* Icono usuario */}
                <div className="login-icon">
                    <FaUserCircle size={60} color={username ? "#28a745" : "#007bff"} onClick={toggleMenu} />
                    <p onClick={toggleMenu}>{username ? username : "Iniciar sesión"}</p>
                    {menuOpen && (
                        <div className="user-menu">
                            <p className="menu-username">{username}</p>
                            <hr />
                            <button onClick={() => navigate("/perfil")}>Mi perfil</button>
                            <button onClick={handleLogout}>Cerrar sesión</button>
                        </div>
                    )}
                </div>
            </div>

           

            {/* Buscador */}
            <div className="buscador-container">
                <h2>Busca tu alojamiento</h2>

                <div className="buscador-campos">
                    <input
                        type="text"
                        placeholder="🏙️ Ciudad o destino"
                        value={ciudad}
                        onChange={(e) => setCiudad(e.target.value)}
                    />
                    <input
                        type="date"
                        value={fechaInicio}
                        onChange={(e) => setFechaInicio(e.target.value)}
                    />
                    <input
                        type="date"
                        value={fechaFin}
                        onChange={(e) => setFechaFin(e.target.value)}
                    />
                    <select value={tipo} onChange={(e) => setTipo(e.target.value)}>
                        <option value="">Cualquier tipo</option>
                        <option value="VIVIENDA_COMPLETA">Vivienda completa</option>
                        <option value="HABITACION">Habitación</option>
                    </select>
                    <label className="checkbox-label">
                        <input
                            type="checkbox"
                            checked={soloDirecta}
                            onChange={(e) => setSoloDirecta(e.target.checked)}
                        />
                        Solo reserva inmediata
                    </label>
                </div>

                <button className="btn-buscar" onClick={handleBuscar}>
                    🔍 Buscar
                </button>
            </div>
        </div>
    );
};

export default HomePage;