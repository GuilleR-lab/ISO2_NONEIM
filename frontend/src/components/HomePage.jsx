import "../App.css";
import { FaUserCircle } from "react-icons/fa"; // Icono de usuario

const HomePage = () => {
    return (
        <>
            <div className="home-container">

                <h1>🏠 Bienvenido a NONEIM</h1>

                <p>Tu portal de alquileres y viviendas.</p>

                {/* Icono de login */}
                <div className="login-icon" onClick={() => (window.location.href = "/auth")}>
                    <FaUserCircle size={60} color="#007bff" />
                    <p>Iniciar sesión</p>
                </div>

            </div>
        </>
    );
};

export default HomePage;