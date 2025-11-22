import { useEffect, useState } from "react";
import "../App.css";
import { FaUserCircle } from "react-icons/fa";

const HomePage = () => {
  const [usuario, setUsuario] = useState(null);
  const [loading, setLoading] = useState(true);
  const [menuOpen, setMenuOpen] = useState(false);
  const [isPropietario, setIsPropietario] = useState(false);

  // Consultar sesión mediante cookie
  useEffect(() => {
    fetch("http://localhost:8090/api/auth/me", {
      method: "GET",
      credentials: "include"
    })
      .then(res => res.status === 401 ? null : res.json())
      .then(data => {
        if (data && data.usuario) {
          setUsuario(data.usuario);
        }
      })
      .finally(() => setLoading(false));
  }, []);

  //COnsultar si es propietario
  useEffect(() => {
    if (usuario){
        fetch("http://localhost:8090/api/propietarios/esPropietario", {
            method: "GET",
            credentials: "include"
        })
        .then(res => {
            if(!res.ok) return null;
            return res.json();
        })
        .then(data => {
            if (data) setIsPropietario(data.isPropietario);
        })
        .catch(err => console.error("Error al verificar propietario:", err));
    }
  }, [usuario]);

  const handleHaztePropietario = () => {
    fetch("http://localhost:8090/api/propietarios/activar", {
        method: "POST",
        credentials: "include"
    })
    .then(res => {
        if (res.ok){
            setIsPropietario(true);
            alert("Ahora eres propietario.");
        }else{
            alert("Error al activarte como propietario.");
        }
    })
    .catch(err => console.error("Error al activar propietario:", err));
  }

  const handleLogout = async () => {
    await fetch("http://localhost:8090/api/auth/logout", {
      method: "POST",
      credentials: "include"
    });

    window.location.reload(); // refrescar estado
  };

  if (loading) return <p>Cargando...</p>;

  return (
    <div className="home-container">
        {/*Contenerdor superior derecha*/}
        <div className = "top-right-container">
            {/*Botón Hazte Propietario*/}
            {usuario && !isPropietario && (
                <button
                    onClick={handleHaztePropietario}
                    className = "hazte-prop-btn"
                >
                    Hazte Propietario
                </button>
            )}
            {/*Icono de usuario*/}
            <div
                className="login-icon"
                onClick={() => setMenuOpen(!menuOpen)}
            >
                <FaUserCircle size={50} color="#007bff" />
                <p style={{ fontSize: "14px" }}>
                    {usuario ? usuario.username : "Iniciar sesión"}
                </p>

                {/*Menú desplegable*/}
                {menuOpen && (
                <div className="user-menu">
                    {usuario ? (
                        <button onClick={handleLogout} className="logout-btn">
                            Cerrar sesión
                        </button>
                    ) : (
                        <button onClick={() => window.location.href = "/auth"} className="logout-btn">
                            Iniciar sesión
                        </button>
                    )}
                </div>
                )}
            </div>
        </div>

        {/*Mensaje si ya es propietario*/}
        {isPropietario && (
            <p className = "prop-ya">
                🎉¡Ya eres propietario!
            </p>
        )}

        <h1>🏠 Bienvenido a NONEIM</h1>
        <p>Tu portal de alquileres y viviendas.</p>
    </div>
  );
};

export default HomePage;