import "../App.css";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
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
                    <div className="info-row">
                        <span><strong>Entrada:</strong></span> {reserva.fechaInicio}
                    </div>
                    <div className="info-row">
                        <span><strong>Salida:</strong></span> {reserva.fechaFin}
                    </div>
                    <div className="info-row">
                        <span><strong>Pagado:</strong></span> {reserva.pagado ? "Sí" : "No"}
                    </div>
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

    return (
        <div className="perfil-dashbord">
            {/* Botón menú */}
            <button
                className={`dashboard-menu-btn ${isSidebarOpen ? "open" : ""}`}
                onClick={() => setIsSidebarOpen(!isSidebarOpen)}
            >
                {isSidebarOpen ? <X size={20} /> : <Menu size={20} />}
            </button>

            {/* Botón homepage */}
            <button className="dashboard-home-btn" onClick={() => navigate("/")}>
                <Home size={20} /> NONEIM
            </button>

            {/* Sidebar */}
            <aside className={`dashboard-sidebar ${isSidebarOpen ? "open" : ""}`}>
                <div className="sidebar-header">
                    <h2>Panel de Control</h2>
                </div>

                <nav className="sidebar-nav">
                    <button
                        className={section === "area-personal" ? "active" : ""}
                        onClick={() => { setSection("area-personal"); setIsSidebarOpen(false); }}
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
                            className={section === "propiedades" ? "active" : ""}
                            onClick={() => { setSection("propiedades"); setIsSidebarOpen(false); }}
                        >
                            <Sofa size={18} /> Mis Propiedades
                        </button>
                    )}

                    <button
                        onClick={() => {
                            sessionStorage.clear();
                            navigate("/");
                        }}
                    >
                        <LogOut size={18} /> Cerrar Sesión
                    </button>
                </nav>
            </aside>

            {/* Overlay */}
            {isSidebarOpen && (
                <div className="dashboard-overlay" onClick={() => setIsSidebarOpen(false)} />
            )}

            {/* Contenido principal */}
            <main className="dashboard-main">
                <header className="main-header">
                    <h1>
                        {section === "area-personal" && "Mi Área Personal"}
                        {section === "reservas" && "Mis Reservas"}
                        {section === "propiedades" && "Mis Propiedades"}
                    </h1>
                </header>

                <h3>Bienvenido de nuevo, {user.name} 😎👍</h3>

                <section className="dashboard-content">
                    {section === "area-personal" && (
                        <div className="grid-dashboard">
                            {/* Tarjeta de Perfil */}
                            <div className="dash-card profile-info">
                                <h3>Información de Perfil</h3>
                                <div className="info-row">
                                    <span><strong>Nombre:</strong></span> {user.name} {user.surname}
                                </div>
                                <div className="info-row">
                                    <span><strong>Usuario:</strong></span> {user.username}
                                </div>
                                <div className="info-row">
                                    <span><strong>Email:</strong></span> {user.email}
                                </div>
                                <div className="info-row">
                                    <span><strong>Rol:</strong></span> {user.rol}
                                </div>
                                <button className="btn-secondary">Cambiar Contraseña</button>
                            </div>

                            {/* Tarjeta de Dirección */}
                            <div className="dash-card address-info">
                                <h3>Dirección</h3>
                                <div>
                                    <span><strong>País:</strong></span> {user.address?.pais}
                                </div>
                                <div>
                                    <span><strong>Ciudad:</strong></span> {user.address?.ciudad}, {user.address?.codigoPostal}
                                </div>
                                <div>
                                    <span><strong>Dirección:</strong></span> {user.address?.calle}, {user.address?.edificio}{user.address?.piso ? `, ${user.address.piso}` : ""}
                                </div>
                                <button className="btn-secondary">Editar Dirección</button>
                            </div>
                        </div>
                    )}

                    {section === "reservas" && (
                        <ReservasInquilino userId={user.id} />
                    )}

                    {section === "propiedades" && (
                        <div className="dash-card">
                            <div className="empty-state">
                                <h3>No tienes propiedades registradas</h3>
                                <p>Comienza a monetizar tus propiedades en nuestra plataforma.</p>
                                <button className="btn-add">+ Añadir Propiedad</button>
                            </div>
                        </div>
                    )}
                </section>
            </main>
        </div>
    );
};

export default Perfil;