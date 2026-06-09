import "../App.css";
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode";

const DetalleInmueble = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [inmueble, setInmueble] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [enListaDeseos, setEnListaDeseos] = useState(false);
    const [cargandoListaDeseos, setCargandoListaDeseos] = useState(false);
    const [mensajeListaDeseos, setMensajeListaDeseos] = useState("");
    
    const token = localStorage.getItem("token");
    const usuarioLogueado = token ? jwtDecode(token).id : null;
    //const rol = sessionStorage.getItem("rol");
    const listaIdCacheKey = usuarioLogueado ? `lista_deseos_id_${usuarioLogueado}` : null;

    const leerListaDesdeBackend = async () => {
        if (!usuarioLogueado) return null;

        const cachedId = listaIdCacheKey ? localStorage.getItem(listaIdCacheKey) : null;
        if (cachedId) {
            const responseById = await fetch(`http://localhost:8090/api/listas-deseos/${cachedId}`);
            if (responseById.ok) {
                return responseById.json();
            }
            localStorage.removeItem(listaIdCacheKey);
        }

        const responseUser = await fetch(`http://localhost:8090/api/listas-deseos/usuario/${usuarioLogueado}`);
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
        const lista = await leerListaDesdeBackend();
        if (lista?.idLista) return lista;

        const usuarioCompletoResponse = await fetch(`http://localhost:8090/api/auth/usuarios/${usuarioLogueado}`);
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
        fetch(`http://localhost:8090/api/inmuebles/${id}`)
            .then(res => {
                if (!res.ok) throw new Error("Inmueble no encontrado");
                return res.json();
            })
            .then(data => {
                setInmueble(data);
                setCargando(false);
            })
            .catch(() => {
                setCargando(false);
            });
    }, [id]);

    useEffect(() => {
        const cargarListaDeseos = async () => {
            if (!usuarioLogueado || !inmueble) {
                setEnListaDeseos(false);
                return;
            }

            setCargandoListaDeseos(true);
            setMensajeListaDeseos("");

            try {
                const lista = await crearListaSiNoExiste();
                const inmuebles = Array.isArray(lista?.inmuebles) ? lista.inmuebles : [];
                setEnListaDeseos(inmuebles.some(item => String(item.idInmueble) === String(inmueble.idInmueble)));
            } catch (error) {
                setMensajeListaDeseos(error.message || "No se pudo cargar la lista de deseos");
            } finally {
                setCargandoListaDeseos(false);
            }
        };

        cargarListaDeseos();
    }, [usuarioLogueado, inmueble]);

    const actualizarListaDeseos = async () => {
        if (!usuarioLogueado || !inmueble) return;

        setCargandoListaDeseos(true);
        setMensajeListaDeseos("");

        try {
            const lista = await crearListaSiNoExiste();

            if (enListaDeseos) {
                if (lista?.idLista) {
                    await fetch(`http://localhost:8090/api/listas-deseos/${lista.idLista}/eliminar-inmueble`, {
                        method: "PUT",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({ idInmueble: inmueble.idInmueble })
                    });
                }

                setEnListaDeseos(false);
            } else {
                const response = await fetch(`http://localhost:8090/api/listas-deseos/${lista.idLista}/agregar-inmueble`, {
                    method: "PUT",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ idInmueble: inmueble.idInmueble })
                });

                if (!response.ok) throw new Error("No se pudo actualizar la lista de deseos");

                const listaActualizada = await response.json();
                const inmuebles = Array.isArray(listaActualizada?.inmuebles) ? listaActualizada.inmuebles : [];
                setEnListaDeseos(inmuebles.some(item => String(item.idInmueble) === String(inmueble.idInmueble)));
            }
        } catch (error) {
            setMensajeListaDeseos(error.message || "No se pudo actualizar la lista de deseos");
        } finally {
            setCargandoListaDeseos(false);
        }
    };

    if (cargando) return <p style={{ padding: 30 }}>Cargando...</p>;
    if (!inmueble) return <p style={{ padding: 30 }}>Inmueble no encontrado.</p>;

    const disponibilidad = inmueble.disponibilidades?.[0];

    return (
        <div className="detalle-container">
            <button className="btn-volver" onClick={() => navigate(-1)}>← Volver</button>

            <div className="detalle-card">
                <div className="detalle-header">
                    <span className="inmueble-tipo">
                        {inmueble.tipo === "VIVIENDA_COMPLETA" ? "🏠 Vivienda completa" : "🛏️ Habitación"}
                    </span>
                    {disponibilidad?.directa && (
                        <span className="badge-directa">⚡ Reserva inmediata</span>
                    )}
                </div>

                <h2>{inmueble.ciudad}</h2>
                <p className="inmueble-direccion">📍 {inmueble.direccion}</p>

                {inmueble.descripcion && (
                    <p className="inmueble-descripcion">{inmueble.descripcion}</p>
                )}

                <div className="detalle-info">
                    <div className="detalle-precio">
                        <strong>{inmueble.precioNoche} €</strong> / noche
                    </div>

                    {disponibilidad && (
                        <div className="detalle-fechas">
                            <p>📅 Disponible del <strong>{disponibilidad.fechaInicio}</strong> al <strong>{disponibilidad.fechaFin}</strong></p>
                            <p>Tipo de reserva: <strong>{disponibilidad.directa ? "Inmediata" : "Por solicitud"}</strong></p>
                        </div>
                    )}
                </div>

                {/* Lógica de comparaciones: Si no hay usuario logueado sse envía al Auth,
                si el usuario logueado es el propietario del inmuble no puede reservar ese inmueble,
                ya no se tiene en cuenta si tiene el rol de propietario o inquilino (si es propietario de otro
                inmueble sí podría reservarlo)
                */}
                {!usuarioLogueado ? (
                    <div className="detalle-aviso">
                        <p>Debes iniciar sesión para hacer una reserva.</p>
                        <button className="btn-buscar" onClick={() => navigate("/auth")}>
                            Iniciar sesión
                        </button>
                    </div>
                ) : (
                    <>
                        <div className="detalle-actions">
                            <button className="btn-secondary" onClick={actualizarListaDeseos} disabled={cargandoListaDeseos}>
                                {cargandoListaDeseos ? "Procesando..." : enListaDeseos ? "Quitar de lista de deseos" : "Añadir a lista de deseos"}
                            </button>
                            <button className="btn-secondary btn-lista-deseos" onClick={() => navigate("/lista-deseos")}>
                                Ver lista de deseos
                            </button>
                        </div>

                        {mensajeListaDeseos && (
                            <p className="login-message" style={{ marginTop: 8 }}>{mensajeListaDeseos}</p>
                        )}

                        {String(usuarioLogueado) === String(inmueble.propietario.id) ? (

                            <div className="detalle-aviso">
                                <p>Eres propietario, no puedes reservar inmuebles.</p>

                                <button className="btn-secondary" onClick={() => navigate(`/propiedadform/${id}`)}>
                                    Editar inmueble
                                </button>
                            </div>

                        ) : (
                            <button className="btn-reservar" onClick={() => navigate(`/reservar/${id}`)}>
                                {disponibilidad?.directa ? "⚡ Reservar ahora" : "📩 Solicitar reserva"}
                            </button>
                        )}
                    </>
                )}

            </div>
        </div>
    );
};

export default DetalleInmueble;
