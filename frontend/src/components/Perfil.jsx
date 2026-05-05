import "../App.css";
import { useEffect, useState } from "react";
import { useNavigate, Outlet, useLocation, useOutletContext } from "react-router-dom";
import { Menu, User, Home, X, LogOut, Sofa } from "lucide-react";

// ─── MODAL CAMBIAR CONTRASEÑA ───────────────────────────────────────────────
const ModalCambiarPassword = ({ userId, onClose }) => {
    const [passwordActual, setPasswordActual] = useState("");
    const [passwordNueva, setPasswordNueva] = useState("");
    const [passwordConfirm, setPasswordConfirm] = useState("");
    const [msg, setMsg] = useState("");
    const [msgColor, setMsgColor] = useState("red");
    const [loading, setLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMsg("");

        if (!passwordActual || !passwordNueva || !passwordConfirm) {
            setMsg("Rellena todos los campos");
            setMsgColor("red");
            return;
        }
        if (passwordNueva !== passwordConfirm) {
            setMsg("Las contraseñas nuevas no coinciden");
            setMsgColor("red");
            return;
        }
        if (passwordNueva.length < 4) {
            setMsg("La contraseña debe tener al menos 4 caracteres");
            setMsgColor("red");
            return;
        }

        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8090/api/auth/${userId}/password`, {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ passwordActual, passwordNueva })
            });
            const data = await response.json();
            if (!response.ok) {
                setMsg(data.message || "Error al cambiar la contraseña");
                setMsgColor("red");
            } else {
                setMsg("Contraseña actualizada correctamente");
                setMsgColor("green");
                setTimeout(onClose, 1200);
            }
        } catch {
            setMsg("Error al conectar con el servidor");
            setMsgColor("red");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={overlayStyle} onClick={onClose}>
            <div style={modalStyle} onClick={e => e.stopPropagation()}>
                <div style={modalHeaderStyle}>
                    <h3 style={{ margin: 0 }}>Cambiar contraseña</h3>
                    <button style={closeBtnStyle} onClick={onClose}><X size={18} /></button>
                </div>
                <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                    <input
                        type="password"
                        placeholder="Contraseña actual"
                        value={passwordActual}
                        onChange={e => setPasswordActual(e.target.value)}
                        style={inputStyle}
                    />
                    <input
                        type="password"
                        placeholder="Nueva contraseña"
                        value={passwordNueva}
                        onChange={e => setPasswordNueva(e.target.value)}
                        style={inputStyle}
                    />
                    <input
                        type="password"
                        placeholder="Confirmar nueva contraseña"
                        value={passwordConfirm}
                        onChange={e => setPasswordConfirm(e.target.value)}
                        style={inputStyle}
                    />
                    {msg && <div style={{ color: msgColor, fontSize: "0.9em" }}>{msg}</div>}
                    <button
                        type="submit"
                        disabled={loading}
                        style={{ backgroundColor: "#a1eafb", padding: "10px", borderRadius: "6px", border: "none", fontWeight: "bold", cursor: "pointer" }}
                    >
                        {loading ? "Guardando..." : "Guardar contraseña"}
                    </button>
                </form>
            </div>
        </div>
    );
};

// ─── MODAL EDITAR DIRECCIÓN ──────────────────────────────────────────────────
const ModalEditarDireccion = ({ userId, addressActual, onClose, onUpdated }) => {
    const [address, setAddress] = useState({
        pais: addressActual?.pais || "",
        ciudad: addressActual?.ciudad || "",
        codigoPostal: addressActual?.codigoPostal || "",
        calle: addressActual?.calle || "",
        edificio: addressActual?.edificio || "",
        piso: addressActual?.piso || ""
    });
    const [msg, setMsg] = useState("");
    const [msgColor, setMsgColor] = useState("red");
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setAddress(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMsg("");

        if (!address.pais || !address.ciudad || !address.codigoPostal || !address.calle || !address.edificio) {
            setMsg("Rellena todos los campos obligatorios (el piso es opcional)");
            setMsgColor("red");
            return;
        }

        setLoading(true);
        try {
            const response = await fetch(`http://localhost:8090/api/auth/${userId}/direccion`, {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(address)
            });
            const data = await response.json();
            if (!response.ok) {
                setMsg(data.message || "Error al actualizar la dirección");
                setMsgColor("red");
            } else {
                setMsg("Dirección actualizada correctamente");
                setMsgColor("green");
                onUpdated(address);
                setTimeout(onClose, 1200);
            }
        } catch {
            setMsg("Error al conectar con el servidor");
            setMsgColor("red");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={overlayStyle} onClick={onClose}>
            <div style={modalStyle} onClick={e => e.stopPropagation()}>
                <div style={modalHeaderStyle}>
                    <h3 style={{ margin: 0 }}>Editar dirección</h3>
                    <button style={closeBtnStyle} onClick={onClose}><X size={18} /></button>
                </div>
                <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
                    <input type="text" placeholder="País *" name="pais" value={address.pais} onChange={handleChange} style={inputStyle} />
                    <input type="text" placeholder="Ciudad *" name="ciudad" value={address.ciudad} onChange={handleChange} style={inputStyle} />
                    <input type="text" placeholder="Código postal *" name="codigoPostal" value={address.codigoPostal} onChange={handleChange} style={inputStyle} />
                    <input type="text" placeholder="Calle *" name="calle" value={address.calle} onChange={handleChange} style={inputStyle} />
                    <input type="text" placeholder="Edificio / número *" name="edificio" value={address.edificio} onChange={handleChange} style={inputStyle} />
                    <input type="text" placeholder="Piso (opcional)" name="piso" value={address.piso} onChange={handleChange} style={inputStyle} />
                    {msg && <div style={{ color: msgColor, fontSize: "0.9em" }}>{msg}</div>}
                    <button
                        type="submit"
                        disabled={loading}
                        style={{ backgroundColor: "#a1eafb", padding: "10px", borderRadius: "6px", border: "none", fontWeight: "bold", cursor: "pointer" }}
                    >
                        {loading ? "Guardando..." : "Guardar dirección"}
                    </button>
                </form>
            </div>
        </div>
    );
};

// ─── ESTILOS MODALES ──────────────────────────────────────────────────────────
const overlayStyle = {
    position: "fixed", inset: 0,
    backgroundColor: "rgba(0,0,0,0.45)",
    display: "flex", alignItems: "center", justifyContent: "center",
    zIndex: 1000
};
const modalStyle = {
    background: "#fff", borderRadius: "12px",
    padding: "24px", width: "100%", maxWidth: "420px",
    boxShadow: "0 8px 32px rgba(0,0,0,0.2)",
    display: "flex", flexDirection: "column", gap: "16px"
};
const modalHeaderStyle = {
    display: "flex", justifyContent: "space-between", alignItems: "center"
};
const closeBtnStyle = {
    background: "none", border: "none", cursor: "pointer", padding: "4px"
};
const inputStyle = {
    padding: "10px", borderRadius: "6px",
    border: "1px solid #ccc", fontSize: "0.95em", width: "100%",
    boxSizing: "border-box"
};

// ─── COMPONENTE ÁREA PERSONAL ─────────────────────────────────────────────────
export const AreaPersonal = () => {
    const { user, toggleRol, setUser } = useOutletContext();
    const [showModalPassword, setShowModalPassword] = useState(false);
    const [showModalDireccion, setShowModalDireccion] = useState(false);

    const handleDireccionUpdated = (newAddress) => {
        setUser(prev => ({ ...prev, address: newAddress }));
    };

    return (
        <div className="grid-dashboard">
            <div className="dash-card profile-info">
                <h3>Información de Perfil</h3>
                <div className="info-row"><span><strong>Nombre:</strong></span> {user.name} {user.surname}</div>
                <div className="info-row"><span><strong>Usuario:</strong></span> {user.username}</div>
                <div className="info-row"><span><strong>Email:</strong></span> {user.email}</div>
                <div className="info-row"><span><strong>Rol:</strong></span> {user.rol}</div>
                <button className="btn-add" onClick={toggleRol}>
                    {user.rol === "INQUILINO" ? "Hacerme Propietario" : "Volver a ser Inquilino"}
                </button>
                <button className="btn-secondary" onClick={() => setShowModalPassword(true)}>
                    Cambiar Contraseña
                </button>
            </div>

            <div className="dash-card address-info">
                <h3>Dirección</h3>
                <div><span><strong>País:</strong></span> {user.address?.pais}</div>
                <div><span><strong>Ciudad:</strong></span> {user.address?.ciudad}, {user.address?.codigoPostal}</div>
                <div><span><strong>Dirección:</strong></span> {user.address?.calle}, {user.address?.edificio}{user.address?.piso ? `, ${user.address.piso}` : ""}</div>
                <button className="btn-secondary" onClick={() => setShowModalDireccion(true)}>
                    Editar Dirección
                </button>
            </div>

            {showModalPassword && (
                <ModalCambiarPassword
                    userId={user.id}
                    onClose={() => setShowModalPassword(false)}
                />
            )}

            {showModalDireccion && (
                <ModalEditarDireccion
                    userId={user.id}
                    addressActual={user.address}
                    onClose={() => setShowModalDireccion(false)}
                    onUpdated={handleDireccionUpdated}
                />
            )}
        </div>
    );
};

// ─── COMPONENTE MIS RESERVAS ──────────────────────────────────────────────────
export const MisReservas = () => {
    const [reservas, setReservas] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [errorReservas, setErrorReservas] = useState("");
    const { user } = useOutletContext();

    useEffect(() => {
        if (user?.id) {
            fetch(`http://localhost:8090/api/reservas/inquilino/${user.id}`)
                .then(async res => {
                    const data = await res.json();
                    if (!res.ok) {
                        const backendMsg = data?.message || data?.error || `Error ${res.status}`;
                        throw new Error(backendMsg);
                    }
                    return data;
                })
                .then(data => {
                    if (Array.isArray(data)) {
                        setReservas(data);
                        setErrorReservas("");
                    } else {
                        setReservas([]);
                        setErrorReservas("La respuesta del servidor no tiene el formato esperado.");
                    }
                    setCargando(false);
                })
                .catch((error) => {
                    setReservas([]);
                    setErrorReservas(error.message || "No se pudieron cargar las reservas.");
                    setCargando(false);
                });
        }
    }, [user?.id]);

    if (cargando) return <p>Cargando reservas...</p>;

    if (errorReservas) return (
        <div className="dash-card">
            <div className="empty-state">
                <h3>No se pudieron cargar las reservas</h3>
                <p>{errorReservas}</p>
            </div>
        </div>
    );

    return (
        <div className="seccion-reservas-wrapper">
            {reservas.length > 0 ? (
                <div className="grid-dashboard">
                    {reservas.map(reserva => (
                        <div key={reserva.idReserva} className="dash-card">
                            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                                <h3>Reserva #{reserva.idReserva}</h3>
                                <span className={reserva.activa ? "badge-directa" : "badge-pendiente"}>
                                    {reserva.activa ? "✅ Confirmada" : "⏳ Pendiente"}
                                </span>
                            </div>
                            {reserva.inmueble && (
                                <div className="info-row">
                                    <span><strong>Inmueble:</strong></span> {reserva.inmueble.ciudad} — {reserva.inmueble.direccion}
                                </div>
                            )}
                            <div className="info-row"><span><strong>Entrada:</strong></span> {reserva.fechaInicio}</div>
                            <div className="info-row"><span><strong>Salida:</strong></span> {reserva.fechaFin}</div>
                            <div className="info-row"><span><strong>Pagado:</strong></span> {reserva.pagado ? "Sí" : "No"}</div>
                            {reserva.pago && (
                                <div className="info-row">
                                    <span><strong>Método de pago:</strong></span> {reserva.pago.metodoPago}
                                </div>
                            )}
                        </div>
                    ))}
                </div>
            ) : (
                <div className="dash-card">
                    <div className="empty-state">
                        <h3>No tienes reservas todavía</h3>
                        <p>Busca alojamiento y haz tu primera reserva.</p>
                    </div>
                </div>
            )}
        </div>
    );
};

// ─── COMPONENTE MIS PROPIEDADES ───────────────────────────────────────────────
export const MisPropiedades = () => {
    const [propiedades, setPropiedades] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const { user } = useOutletContext();

    const handleDelete = async (idInmueble) => {
        const confirmacion = window.confirm("¿Estás seguro de que quieres eliminar esta propiedad?");
        if (!confirmacion) return;

        try {
            const response = await fetch(`http://localhost:8090/api/inmuebles/${idInmueble}`, { method: "DELETE" });
            if (response.ok) {
                setPropiedades(propiedades.filter(prop => prop.idInmueble !== idInmueble));
                alert("Propiedad eliminada correctamente");
            } else {
                alert("Error al eliminar la propiedad");
            }
        } catch (error) {
            console.error("Error al realizar la solicitud:", error);
            alert("No se pudo conectar con el servidor");
        }
    };

    useEffect(() => {
        if (user?.id) {
            fetch(`http://localhost:8090/api/inmuebles/propietario/${user.id}`)
                .then(response => {
                    if (!response.ok) throw new Error("Error al obtener inmuebles");
                    return response.json();
                })
                .then(data => { setPropiedades(data); setLoading(false); })
                .catch(error => { console.error("Error cargando propiedades:", error); setLoading(false); });
        }
    }, [user?.id]);

    if (loading) return <p>Cargando tus propiedades...</p>;

    return (
        <div className="seccion-propiedades-wrapper">
            <div className="propiedades-header">
                <h1>Tus Propiedades</h1>
            </div>

            {propiedades.length > 0 ? (
                <>
                    <div className="grid-propiedades">
                        {propiedades.map(prop => (
                            <div key={prop.idInmueble} className="dash-card-property">
                                <div className="property-type-tag">{prop.tipo}</div>
                                <div className="property-content">
                                    <h4>{prop.direccion}</h4>
                                    <p className="property-city">{prop.ciudad}</p>
                                    <p className="property-desc">{prop.descripcion}</p>
                                    <div className="property-footer">
                                        <span className="price"><strong>{prop.precioNoche}€</strong> / noche</span>
                                    </div>
                                </div>
                                <div className="card-actions">
                                    <button className="btn-edit" onClick={() => navigate(`/propiedadform/${prop.idInmueble}`)}>
                                        Editar
                                    </button>
                                    <button className="btn-delete" onClick={() => handleDelete(prop.idInmueble)}>
                                        Eliminar
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                    <div className="add-property-container">
                        <button className="btn-add-large" onClick={() => navigate("/propiedadform")}>
                            + Añadir otra propiedad
                        </button>
                    </div>
                </>
            ) : (
                <div className="dash-card">
                    <div className="empty-state">
                        <h3>No tienes propiedades registradas</h3>
                        <p>Comienza a monetizar tus propiedades en nuestra plataforma.</p>
                        <button className="btn-add" onClick={() => navigate("/propiedadform")}>
                            + Añadir Propiedad
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

// ─── COMPONENTE PRINCIPAL PERFIL ──────────────────────────────────────────────
const Perfil = () => {
    const [user, setUser] = useState(null);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const navigate = useNavigate();
    const location = useLocation();

    const isAreaPersonal = location.pathname.includes("area-personal");
    const isPropiedades = location.pathname.includes("propiedades");
    const isReservas = location.pathname.includes("reservas");

    useEffect(() => {
        const userId = sessionStorage.getItem("userId");
        if (!userId) { navigate("/auth"); return; }

        fetch(`http://localhost:8090/api/auth/${userId}`)
            .then(response => {
                if (!response.ok) throw new Error("Error al obtener el usuario");
                return response.json();
            })
            .then(data => setUser(data))
            .catch(error => console.error(error));
    }, [navigate]);

    if (!user) return <p>Cargando perfil...</p>;

    const toggleRol = async () => {
        const userId = sessionStorage.getItem("userId");
        const nuevoRol = user.rol === "INQUILINO" ? "PROPIETARIO" : "INQUILINO";

        try {
            const response = await fetch(`http://localhost:8090/api/auth/${userId}/rol`, {
                method: "PATCH",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ rol: nuevoRol }),
            });

            if (response.ok) {
                sessionStorage.setItem("rol", nuevoRol);
                setUser({ ...user, rol: nuevoRol });
                alert(`Ahora eres ${nuevoRol.toLowerCase()}`);
            } else {
                alert("Error en el servidor al cambiar el rol");
            }
        } catch (error) {
            console.error("Error al conectar:", error);
            alert("No se pudo conectar con el servidor");
        }
    };

    return (
        <div className="perfil-dashbord">
            <button
                className={`dashboard-menu-btn ${isSidebarOpen ? "open" : ""}`}
                onClick={() => setIsSidebarOpen(!isSidebarOpen)}
            >
                {isSidebarOpen ? <X size={20} /> : <Menu size={20} />}
            </button>

            <button className="dashboard-home-btn" onClick={() => navigate("/")}>
                <Home size={20} /> NONEIM
            </button>

            <aside className={`dashboard-sidebar ${isSidebarOpen ? "open" : ""}`}>
                <div className="sidebar-header">
                    <h2>Panel de Control</h2>
                </div>
                <nav className="sidebar-nav">
                    <button
                        className={isAreaPersonal ? "active" : ""}
                        onClick={() => { navigate("/perfil/area-personal"); setIsSidebarOpen(false); }}
                    >
                        <User size={18} /> Área Personal
                    </button>
                    <button
                        className={isReservas ? "active" : ""}
                        onClick={() => { navigate("/perfil/reservas"); setIsSidebarOpen(false); }}
                    >
                        <Sofa size={18} /> Mis Reservas
                    </button>
                    {user.rol === "PROPIETARIO" && (
                        <button
                            className={isPropiedades ? "active" : ""}
                            onClick={() => { navigate("/perfil/propiedades"); setIsSidebarOpen(false); }}
                        >
                            <Sofa size={18} /> Mis Propiedades
                        </button>
                    )}
                    <button onClick={() => { sessionStorage.clear(); navigate("/"); }}>
                        <LogOut size={18} /> Cerrar Sesión
                    </button>
                </nav>
            </aside>

            <main className="dashboard-main">
                <header className="main-header">
                    <h1>
                        {isAreaPersonal && "Mi Área Personal"}
                        {isPropiedades && "Mis Propiedades"}
                        {isReservas && "Mis Reservas"}
                        {location.pathname === "/perfil" && "Mi Perfil"}
                    </h1>
                </header>
                <h3>Bienvenid@ de nuevo, {user.name} 😎👍</h3>
                <section className="dashboard-content">
                    <Outlet context={{ user, toggleRol, setUser }} />
                </section>
            </main>
        </div>
    );
};

export default Perfil;