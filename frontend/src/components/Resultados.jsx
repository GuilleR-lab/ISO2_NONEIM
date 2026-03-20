import "../App.css";
import { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const Resultados = () => {
    const [inmuebles, setInmuebles] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);

    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const params = location.search;
        setCargando(true);

        fetch(`http://localhost:8090/api/inmuebles/buscar${params}`)
            .then((res) => {
                if (!res.ok) throw new Error("Error al buscar inmuebles");
                return res.json();
            })
            .then((data) => {
                setInmuebles(data);
                setCargando(false);
            })
            .catch((err) => {
                setError(err.message);
                setCargando(false);
            });
    }, [location.search]);

    const usuarioLogueado = sessionStorage.getItem("userId");

    return (
        <div className="resultados-container">
            <div className="resultados-header">
                <button className="btn-volver" onClick={() => navigate("/")}>
                    ← Volver
                </button>
                <h2>Resultados de búsqueda</h2>
            </div>

            {cargando && <p>Buscando inmuebles...</p>}
            {error && <p style={{ color: "red" }}>{error}</p>}

            {!cargando && !error && inmuebles.length === 0 && (
                <div className="sin-resultados">
                    <p>😔 No se encontraron inmuebles con esos filtros.</p>
                    <button className="btn-buscar" onClick={() => navigate("/")}>
                        Modificar búsqueda
                    </button>
                </div>
            )}

            <div className="inmuebles-grid">
                {inmuebles.map((inmueble) => (
                    <div key={inmueble.idInmueble} className="inmueble-card">
                        <div className="inmueble-card-header">
                            <span className="inmueble-tipo">
                                {inmueble.tipo === "VIVIENDA_COMPLETA" ? "🏠 Vivienda completa" : "🛏️ Habitación"}
                            </span>
                            {inmueble.disponibilidades?.some(d => d.directa) && (
                                <span className="badge-directa">⚡ Reserva inmediata</span>
                            )}
                        </div>

                        <h3>{inmueble.ciudad}</h3>
                        <p className="inmueble-direccion">📍 {inmueble.direccion}</p>

                        {inmueble.descripcion && (
                            <p className="inmueble-descripcion">{inmueble.descripcion}</p>
                        )}

                        <div className="inmueble-precio">
                            <strong>{inmueble.precioNoche} €</strong> / noche
                        </div>

                        <button
                            className="btn-reservar"
                            onClick={() => {
                                if (!usuarioLogueado) {
                                    navigate("/auth");
                                } else {
                                    navigate(`/inmueble/${inmueble.idInmueble}`);
                                }
                            }}
                        >
                            Ver detalle
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default Resultados;