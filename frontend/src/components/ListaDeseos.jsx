import "../App.css";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const safeDecodeToken = (token) => {
    try {
        return token ? jwtDecode(token) : null;
    } catch {
        return null;
    }
};

const ListaDeseos = () => {
    const navigate = useNavigate();
    const token = localStorage.getItem("token");
    const decodedToken = safeDecodeToken(token);
    const usuarioId = decodedToken?.id || null;
    const [listaDeseos, setListaDeseos] = useState([]);
    const [listaId, setListaId] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState("");

    const listaIdCacheKey = usuarioId ? `lista_deseos_id_${usuarioId}` : null;

    const obtenerListaDeseosBackend = async () => {
        if (!usuarioId) return null;

        const cachedId = listaIdCacheKey ? localStorage.getItem(listaIdCacheKey) : null;
        if (cachedId) {
            const responseById = await fetch(`http://localhost:8090/api/listas-deseos/${cachedId}`);
            if (responseById.ok) {
                return responseById.json();
            }
            localStorage.removeItem(listaIdCacheKey);
        }

        const responseUser = await fetch(`http://localhost:8090/api/listas-deseos/usuario/${usuarioId}`);
        if (!responseUser.ok) {
            throw new Error("No se pudo cargar la lista de deseos");
        }

        const listaExistente = await responseUser.json();

        if (listaExistente?.idLista && listaIdCacheKey) {
            localStorage.setItem(listaIdCacheKey, String(listaExistente.idLista));
        }

        return listaExistente;
    };

    const crearListaSiNoExiste = async () => {
        const lista = await obtenerListaDeseosBackend();
        if (lista?.idLista) return lista;

        const usuarioCompletoResponse = await fetch(`http://localhost:8090/api/auth/usuarios/${usuarioId}`);
        if (!usuarioCompletoResponse.ok) {
            throw new Error("No se pudo crear la lista de deseos");
        }

        const usuarioCompleto = await usuarioCompletoResponse.json();

        const response = await fetch("http://localhost:8090/api/listas-deseos", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ usuario: { id: usuarioCompleto.id } })
        });

        if (!response.ok) {
            const errorBody = await response.json().catch(() => null);
            throw new Error(errorBody?.message || "No se pudo crear la lista de deseos");
        }

        const nuevaLista = await response.json();
        if (nuevaLista?.idLista && listaIdCacheKey) {
            localStorage.setItem(listaIdCacheKey, String(nuevaLista.idLista));
        }

        return nuevaLista;
    };

    useEffect(() => {
        const cargarLista = async () => {
            if (!usuarioId) {
                setCargando(false);
                return;
            }

            setCargando(true);
            setError("");

            try {
                const lista = await crearListaSiNoExiste();
                setListaId(lista?.idLista || null);
                setListaDeseos(Array.isArray(lista?.inmuebles) ? lista.inmuebles : []);
            } catch (err) {
                setError(err.message || "No se pudo cargar la lista de deseos");
                setListaDeseos([]);
            } finally {
                setCargando(false);
            }
        };

        cargarLista();
    }, [usuarioId, listaIdCacheKey]);

    const eliminarInmueble = async (inmueble) => {
        if (!usuarioId) return;

        setError("");

        try {
            const listaActual = await crearListaSiNoExiste();

            const response = await fetch(`http://localhost:8090/api/listas-deseos/${listaActual.idLista}/eliminar-inmueble`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ idInmueble: inmueble.idInmueble })
            });

            if (!response.ok) throw new Error("No se pudo quitar el inmueble de la lista de deseos");

            setListaDeseos(prev => prev.filter(item => String(item.idInmueble) !== String(inmueble.idInmueble)));
        } catch (err) {
            setError(err.message || "No se pudo quitar el inmueble de la lista de deseos");
        }
    };

    if (!token) {
        return (
            <div className="resultados-container">
                <div className="resultados-header wishlist-header">
                    <button className="btn-volver" onClick={() => navigate(-1)}>
                        ← Volver
                    </button>
                    <h2>Lista de deseos</h2>
                </div>

                <div className="sin-resultados">
                    <p>Debes iniciar sesión para ver tu lista de deseos.</p>
                    <button className="btn-buscar" onClick={() => navigate("/auth")}>Iniciar sesión</button>
                </div>
            </div>
        );
    }

    return (
        <div className="resultados-container">
            <div className="resultados-header wishlist-header">
                <button className="btn-volver" onClick={() => navigate(-1)}>
                    ← Volver
                </button>
                <div>
                    <h2>Lista de deseos</h2>
                    <p className="wishlist-subtitle">Inmuebles guardados para revisarlos más tarde</p>
                </div>
            </div>

            {cargando && <p>Cargando lista de deseos...</p>}
            {error && <p className="login-message">{error}</p>}

            {!cargando && !error && listaDeseos.length === 0 ? (
                <div className="sin-resultados">
                    <p>No has guardado ningún inmueble todavía.</p>
                    <button className="btn-buscar" onClick={() => navigate("/")}>Buscar inmuebles</button>
                </div>
            ) : (
                <div className="inmuebles-grid">
                    {listaDeseos.map((inmueble) => (
                        <div key={inmueble.idInmueble} className="inmueble-card">
                            <div className="inmueble-card-header">
                                <span className="inmueble-tipo">
                                    {inmueble.tipo === "VIVIENDA_COMPLETA" ? "🏠 Vivienda completa" : "🛏️ Habitación"}
                                </span>
                                {inmueble.directa && (
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

                            <div className="wishlist-card-actions">
                                <button className="btn-reservar" onClick={() => navigate(`/inmueble/${inmueble.idInmueble}`)}>
                                    Ver detalles
                                </button>
                                <button className="btn-secondary" onClick={() => eliminarInmueble(inmueble)}>
                                    Quitar
                                </button>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default ListaDeseos;
