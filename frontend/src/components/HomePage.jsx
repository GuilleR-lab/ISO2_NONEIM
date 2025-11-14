import { useEffect, useState } from "react";
import "../App.css";
import { FaUserCircle } from "react-icons/fa";

const HomePage = () => {
  const [usuario, setUsuario] = useState(null);
  const [loading, setLoading] = useState(true);
  const [menuOpen, setMenuOpen] = useState(false);

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

      {/* ICONO DEL USUARIO */}
      <div
        className="login-icon"
        onClick={() => setMenuOpen(!menuOpen)}
      >
        <FaUserCircle size={50} color="#007bff" />
        <p style={{ fontSize: "14px" }}>
          {usuario ? usuario.username : "Iniciar sesión"}
        </p>

        {/* MENÚ DESPLEGABLE */}
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

      <h1>🏠 Bienvenido a NONEIM</h1>
      <p>Tu portal de alquileres y viviendas.</p>
    </div>
  );
};

export default HomePage;
