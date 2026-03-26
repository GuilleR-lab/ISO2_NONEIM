import "../App.css";
import {useEffect, useState} from "react";
import {useNavigate, Outlet, useLocation, useOutletContext} from "react-router-dom";
import {Menu, User, Home, X, LogOut, Sofa} from "lucide-react";

const Perfil = () => {

    const [section, setSection] = useState("info");
    const [user, setUser] = useState(null);
    //const [rol, setRol] = useState(null);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);
    const [propiedades, setPropiedades] = useState([]);

    const navigate = useNavigate();
    const location = useLocation();

    const isAreaPersonal = location.pathname.includes("area-personal");
    const isPropiedades = location.pathname.includes("propiedades");

    useEffect(() => {
        const userId = sessionStorage.getItem("userId");
        //const storedRol = sessionStorage.getItem("rol");

        if (section === "propiedades"){
            
        }

        if(!userId){
            navigate("/auth");
            return;
        }
    

        fetch (`http://localhost:8090/api/auth/${userId}`)
            .then (response => {
                if (!response.ok){
                    throw new Error("Error al obtener el usuario");
                }
                return response.json();
            })
            .then (data => {
                setUser(data);
            })
            .catch (error => {
                console.error(error);
            });

    }, [navigate], [section]);

    useEffect(() => {
        if (section === "propiedades"){
            const userId = sessionStorage.getItem("userId");
            fetch(`http://localhost:8090/api/inmuebles/propietario/${userId}`)
                .then(response => response.json())
                .then(data => {setPropiedades(data);})

                .catch(error => console.error("Error al cargar propiedades:", error));
        }
    }, [section]);

    if (!user){
        return <p> Cargando perfil ...</p>
    }

    const toggleRol = async () => {
        const userId = sessionStorage.getItem("userId");
        // Calculamos cuál es el nuevo rol basándonos en el actual
        const nuevoRol = user.rol === "INQUILINO" ? "PROPIETARIO" : "INQUILINO";

        try {
            // IMPORTANTE: Asegúrate de que esta URL coincida con tu @PatchMapping en el Backend
            // Según tu código anterior: http://localhost:8090/api/auth/${userId}/rol
            console.log("Enviando a ID:", userId, "Rol:", nuevoRol);
            const response = await fetch(`http://localhost:8090/api/auth/${userId}/rol`, {
            method: "PATCH",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ rol: nuevoRol }),
            });

            if (response.ok) {
            // 1. Actualizamos el sessionStorage
            sessionStorage.setItem("rol", nuevoRol);

            // 2. Actualizamos el estado del objeto 'user' para que la UI se refresque
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
            {/* --- Botones Superiores (Ajustes y Home) --- */}
            <button className={`dashboard-menu-btn ${isSidebarOpen ? "open" : ""}`}
                onClick={() => setIsSidebarOpen(!isSidebarOpen)}>
                {isSidebarOpen ? <X size={20} /> : <Menu size={20} />}
            </button>

            <button className="dashboard-home-btn" onClick={() => navigate("/")}>
                <Home size={20} />NONEIM  
            </button>

            {/* --- SIDEBAR --- */}
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

            {isSidebarOpen && <div className="dashboard-overlay" onClick={() => setIsSidebarOpen(false)}></div>}

            {/* --- CONTENIDO PRINCIPAL --- */}
            <main className="dashboard-main">
                <header className="main-header">
                    {/* Si no hay subruta específica, muestra "Perfil" */}
                    {location.pathname === "/perfil" ? <h1>Perfil</h1> : 
                     <h2>{isAreaPersonal ? "Área Personal" : "Mis Propiedades"}</h2>}
                </header>

                <h3>Bienvenido de nuevo, {user.name} 😎👍</h3>

                <section className="dashboard-content">
                    {/* AQUÍ es donde React Router meterá el componente AreaPersonal o MisPropiedades */}
                    <Outlet context={{ user, toggleRol }} /> 
                </section>
            </main>
        </div>
    );
};

export const AreaPersonal = () => {
    const { user, toggleRol } = useOutletContext(); // Recupera los datos del padre (Perfil)
    return (
        <div className="grid-dashboard">
            <div className="dash-card profile-info">
                <h3>Información de Perfil</h3>
                <div className="info-row"><span><strong>Nombre:</strong></span> {user.name} {user.surname}</div>
                <div className="info-row"><span><strong>Email:</strong></span> {user.email}</div>
                <div className="info-row"><span><strong>Rol:</strong></span> {user.rol}</div>
                <button className="btn-add" onClick={toggleRol}>
                    {user.rol === "INQUILINO" ? "Hacerme Propietario" : "Volver a ser Inquilino"}
                </button>
                <button className="btn-secondary">Cambiar Contraseña</button>
            </div>
            {/* Tarjeta de dirección aquí... */}
        </div>
    );
};

export const MisPropiedades = () => {
    const [propiedades, setPropiedades] = useState([]);
    const [loading, setLoading] = useState(true);
    const navigate = useNavigate();
    
    // Obtenemos el usuario del contexto del Outlet (definido en Perfil.jsx)
    const { user } = useOutletContext();

    const handleDelete = async (idInmueble) => {
        const confirmacion = window.confirm("¿Estás seguro de que quieres eliminar esta propiedad?");
        if (!confirmacion) return;

        try{
            const response = await fetch(`http://localhost:8090/api/inmuebles/${idInmueble}`, {
                method: "DELETE",
            });
            
            if (response.ok){
                setPropiedades(propiedades.filter(prop => prop.idInmueble !== idInmueble));
                alert("Propiedad eliminada correctamente");
            }else{
                alert("Error al eliminar la propiedad");
            }
        }catch(error){
            console.error("Error al realizar la solicitud:", error);
            alert("No se pudo conectar con el servidor");
        }
    }

    useEffect(() => {
        // Solo hacemos la petición si tenemos el ID del usuario
        if (user?.id) {
            fetch(`http://localhost:8090/api/inmuebles/propietario/${user.id}`)
                .then((response) => {
                    if (!response.ok) throw new Error("Error al obtener inmuebles");
                    return response.json();
                })
                .then((data) => {
                    setPropiedades(data);
                    setLoading(false);
                })
                .catch((error) => {
                    console.error("Error cargando propiedades:", error);
                    setLoading(false);
                });
        }
    }, [user?.id]);

    if (loading) return <p>Cargando tus propiedades...</p>;

    return (
        <div className="seccion-propiedades-wrapper">
            {/* Encabezado de la sección */}
            <div className="propiedades-header">
                <h1>Tus Propiedades</h1>
            </div>

            {/* Contenido dinámico */}
            {propiedades.length > 0 ? (
                <>
                    <div className="grid-propiedades">
                        {propiedades.map((prop) => (
                            <div key={prop.idInmueble} className="dash-card-property">
                                <div className="property-type-tag">{prop.tipo}</div>
                                <div className="property-content">
                                    <h4>{prop.direccion}</h4>
                                    <p className="property-city">{prop.ciudad}</p>
                                    <p className="property-desc">{prop.descripcion}</p>
                                    <div className="property-footer">
                                        <span className="price">
                                            <strong>{prop.precioNoche}€</strong> / noche
                                        </span>
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

                    {/* Botón para añadir (Debajo del grid) */}
                    <div className="add-property-container">
                        <button className="btn-add-large" onClick={() => navigate("/propiedadform")}>
                            + Añadir otra propiedad
                        </button>
                    </div>
                </>
            ) : (
                /* Estado vacío si no hay propiedades */
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