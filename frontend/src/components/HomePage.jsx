import "../App.css";
import { FaUserCircle } from "react-icons/fa"; // Icono de usuario

const HomePage = () => {
    let usuario = null;

    try {
        const usuarioData = localStorage.getItem("usuario");
        usuario = usuarioData ? JSON.parse(usuarioData) : null;
    } catch (e) {
        console.error("⚠ Error parseando usuario en localStorage:", e);
        localStorage.removeItem("usuario");
    }

    return (
        <div className="home-container">

            {/* Icono de usuario */}
            <div
                className="login-icon"
                onClick={() => (window.location.href = usuario ? "/perfil" : "/auth")}
                style={{ position: "absolute", top: 20, right: 20, cursor: "pointer", textAlign: "center" }}
            >
                <FaUserCircle size={50} color="#007bff" />
                <p style={{ fontSize: "14px" }}>
                    {usuario ? usuario.username : "Iniciar sesión"}
                </p>
            </div>

            <h1>🏠 Bienvenido a NONEIM</h1>
            <p>Tu portal de alquileres y viviendas.</p>

        </div>
    );
};

export default HomePage;
