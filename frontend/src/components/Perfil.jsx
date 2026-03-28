import "../App.css";
import { useEffect, useState } from "react";
import { useNavigate, Outlet, useLocation, useOutletContext } from "react-router-dom";
import { Menu, User, Home, X, LogOut, Sofa } from "lucide-react";

// Componente de reservas del inquilino
const ReservasInquilino = ({ userId }) => {
    const [reservas, setReservas] = useState([]);
    const [cargando, setCargando] = useState(true);

    useEffect(() => {
        fetch(`http://localhost:8090/api/reservas/inquilino/${userId}`)
            .then(res => res.json())
            .then(data => {
                setReservas(data);
                setCargando(false);
            })
            .catch(() => setCargando(false));
    }, [userId]);

    if (cargando) return <p>Cargando reservas...</p>;

    if (reservas.length === 0) return (
        <div className="dash-card">
            <div className="empty-state">
                <h3>No tienes reservas todavía</h3>
                <p>Busca alojamiento y haz tu primera reserva.</p>
            </div>
        </div>
    );

    return (
        <div className="grid-dashboard">
            {reservas.map(reserva => (
                <div key={reserva.idReserva} className="dash-card">
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <h3>Reserva #{reserva.idReserva}</h3>
                        <span className={reserva.activa ? "badge-directa" : "badge-pendiente"}>
                            {reserva.activa ? "✅ Confirmada" : "⏳ Pendiente"}
                        </span>
                    </div>
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
    );
};

const Perfil = () => {
    const [section, setSection] = useState("area-personal");
    const [user, setUser] = useState(null);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const navigate = useNavigate();
    const location = useLocation();

    const isAreaPersonal = location.pathname.includes("area-personal");
    const isPropiedades = location.pathname.includes("propiedades");

    useEffect(() => {
        const userId = sessionStorage.getItem("userId");

        if (!userId) {
            navigate("/auth");
            return;
        }

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
                        className={isAreaPersonal || location.pathname === "/perfil" ? "active" : ""}
                        onClick={() => { navigate("/perfil/area-personal"); setIsSidebarOpen(false); }}
                    >
                        <User size={18} /> Área Personal
                    </button>

                    {user.rol === "INQUILINO" && (
                        <button
                            className={section === "reservas" ? "active" : ""}
                            onClick={() => { setSection("reservas"); setIsSidebarOpen(false); }}
                        >
                            <Sofa size={18} /> Mis Reservas
                        </button>
                    )}

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

            {isSidebarOpen && (
                <div className="dashboard-overlay" onClick={() => setIsSidebarOpen(false)} />
            )}

            <main className="dashboard-main">
                <header className="main-header">
                    <h1>
                        {isAreaPersonal && "Mi Área Personal"}
                        {isPropiedades && "Mis Propiedades"}
                        {section === "reservas" && "Mis Reservas"}
                        {location.pathname === "/perfil" && "Mi Perfil"}
                    </h1>
                </header>

                <h3>Bienvenido de nuevo, {user.name} 😎👍</h3>

                <section className="dashboard-content">
                    {section === "reservas" ? (
                        <ReservasInquilino userId={user.id} />
                    ) : (
                        <Outlet context={{ user, toggleRol }} />
                    )}
                </section>
            </main>
        </div>
    );
};

export const AreaPersonal = () => {
    const { user, toggleRol } = useOutletContext();
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
                <button className="btn-secondary">Cambiar Contraseña</button>
            </div>
            <div className="dash-card address-info">
                <h3>Dirección</h3>
                <div><span><strong>País:</strong></span> {user.address?.pais}</div>
                <div><span><strong>Ciudad:</strong></span> {user.address?.ciudad}, {user.address?.codigoPostal}</div>
                <div><span><strong>Dirección:</strong></span> {user.address?.calle}, {user.address?.edificio}{user.address?.piso ? `, ${user.address.piso}` : ""}</div>
                <button className="btn-secondary">Editar Dirección</button>
            </div>
        </div>
    );
};

export const MisPropiedades = () => {
    const [propiedades, setPropiedades] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    const { user } = useOutletContext();

    const handleDelete = async (idInmueble) => {
        const confirmacion = window.confirm("¿Estás seguro de que quieres eliminar esta propiedad?");
        if (!confirmacion) return;

        try {
            const response = await fetch(`http://localhost:8090/api/inmuebles/${idInmueble}`, {
                method: "DELETE",
            });

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
                .then(data => {
                    setPropiedades(data);
                    setLoading(false);
                })
                .catch(error => {
                    console.error("Error cargando propiedades:", error);
                    setLoading(false);
                });
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
                                    <button className="btn-edit" onClick={() => navigate(`/inmueble/${prop.idInmueble}`)}>
                                        Detalles
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

export default Perfil;