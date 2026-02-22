import "../App.css";
import { FaUserCircle } from "react-icons/fa"; // Icono de usuario
import { useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";

const HomePage = () => {

    const [username, setUsername] = useState(null);
    const [menuOpen, setMenuOpen] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const storedUser = localStorage.getItem("username");
        if(storedUser){
            setUsername(storedUser);
        }
    }, []);

    const toggleMenu = () => {
        if(!username){
            navigate("/auth");
        }else{
            setMenuOpen(!menuOpen);
        }
    }; 

    const handleLogout = () => {
        localStorage.clear();
        setUsername(null);
        setMenuOpen(false);
        navigate("/");
    };

    /*const handleClick = () => {
        if(username){
            //Podría redirigirse al perfil
            navigate("/");
        }else{
            navigate("/auth");
        }
    };*/

    return (
        
        <div className="home-container">

            <h1>🏠 Bienvenido a NONEIM</h1>

            <p>Tu portal de alquileres y viviendas.</p>

            {/* Icono de login */}
            <div className="login-icon" >
                <FaUserCircle size={60} 
                color={username ? "#28a745" : "#007bff"} 
                onClick={toggleMenu}
            />
            <p onClick={toggleMenu}>
                {username ? username : "Iniciar sesión"}
            </p>

            {menuOpen && (
                <div className="user-menu">
                    <p className="menu-username">{username}</p>
                    <hr />
                    <button onClick={() => navigate("/perfil")}>
                        Perfil (próximamente)
                    </button>
                    <button onClick={handleLogout}>
                        Cerrar sesión
                    </button>
                </div>
            )}
            </div>

        </div>
        
    );
};

export default HomePage;