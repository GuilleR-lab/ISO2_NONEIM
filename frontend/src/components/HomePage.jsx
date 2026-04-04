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
            // Solo hacemos el fetch si hay un usuario logueado Y es propietario
            if (!usuarioId || !esPropietario) return;

            try {
                const res = await fetch(`http://localhost:8090/api/solicitudes/propietario/${usuarioId}/pendientes/count`);
                const data = await res.json(); // ¡IMPORTANTE: res.json(), no formData!
                
                setSolicitudesPendientes(data.count); 
            } catch (error) {
                console.error("Error al cargar solicitudes:", error);
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
                <div className="solicitud-icon" onClick={() => navigate("/solicitudes")}>
                    <BsBell size={30} color="#007bff" />

                    {/*Numero de solicitudes pendientes*/}
                   {solicitudesPendientes > 0 && (
                        <span className="solicitud-badge">
                            {solicitudesPendientes > 9 ? "9+" : solicitudesPendientes}
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