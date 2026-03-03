import "../App.css";
import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {Menu, User, Home, X, LogOut, Sofa} from "lucide-react";

const Perfil = () => {

    const [section, setSection] = useState("info");
    const [user, setUser] = useState(null);
    //const [rol, setRol] = useState(null);
    const [isSidebarOpen, setIsSidebarOpen] = useState(false);

    const navigate = useNavigate();

    useEffect(() => {
        const userId = sessionStorage.getItem("userId");
        //const storedRol = sessionStorage.getItem("rol");


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

    }, [navigate]);
    if (!user){
        return <p> Cargando perfil ...</p>
    }

    return (
        <div className="perfil-dashbord">
            {/* --- Boton de ajustes --- */}
            <button className={`dashboard-menu-btn ${isSidebarOpen ? "open" : ""}`}
            onClick={() => setIsSidebarOpen(!isSidebarOpen)}>
                {isSidebarOpen ? <X size={20} /> : <Menu size={20} />} <span>{isSidebarOpen ? "" : ""}</span>
            </button>

            {/* --- Boton a homepage --- */}
            <button className="dashboard-home-btn" onClick={() => navigate("/")}>
                <Home size={20} />NONEIM  
            </button>

            {/* --- SIDEBAR DESPLEGABLE --- */}
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

                    {user.rol === "PROPIETARIO" && (
                        <button 
                            className={section === "propiedades" ? "active" : ""}
                            onClick={() => { setSection("propiedades"); setIsSidebarOpen(false); }}
                        >
                            <Sofa size={18} /> Mis Propiedades
                        </button>
                    )}

                    <button
                        className={section === "logout" ? "active" : ""}
                        onClick={() => { setSection("logout"); 
                            sessionStorage.clear();
                            navigate("/");
                        }}
                    >
                        <LogOut size={18} /> Cerrar Sesión
                    </button>
                </nav>
            </aside>

            {/* --- OVERLAY (Cierra el menú al hacer clic fuera) --- */}
            {isSidebarOpen && <div className="dashboard-overlay" onClick={() => setIsSidebarOpen(false)}></div>}

            {/* --- CONTENIDO PRINCIPAL --- */}
            <main className="dashboard-main">
                <header className="main-header">
                    <h1>{section === "area-personal" ? "Mi Área Personal" : "Mis propiedades"}</h1>
                </header>

                 <h3> BIenvenido de nuevo, {user.name} 😎👍</h3>

                <section className="dashboard-content">
                    {section === "area-personal" && (
                        <div className="grid-dashboard">
                            {/*Tarjeta de Perfil*/}
                            <div className="dash-card profile-info">
                                <h3>Información de Perfil</h3>
                                <div className="info-row"><span><strong>Nombre:</strong></span> {user.name} {user.surname}</div>
                                <div className="info-row"><span><strong>Email:</strong></span> {user.email}</div>
                                <div className="info-row"><span><strong>Rol:</strong></span> {user.rol}</div>
                                
                                <button className="btn-add" onClick={() => {
                                    if(user.rol === "INQUILINO"){
                                        setUser({...user, rol: "PROPIETARIO"});
                                    }else{
                                        setUser({...user, rol: "INQUILINO"});
                                    }
                                }}>
                                        Cambiar Rol
                                </button>
                                
                                <button className="btn-secondary"> Cambiar Contraseña</button>
                            </div>

                            {/*Tarjeta de Dirección*/}
                            <div className="dash-card address-info">
                                <h3> Dirección </h3>
                                <div><span><strong>Pais:</strong></span> {user.address?.pais}</div>
                                <div><span><strong>Ciudad:</strong></span> {user.address?.ciudad}, {user.address?.codigoPostal}</div>
                                <div><span><strong>Puerta:</strong></span> {user.address?.calle}, {user.address?.edificio}, {user.address?.piso}</div>
                                <button className="btn-secondary"> Editar Dirección</button>
                            </div>
                        </div>
                    )}
                        {section === "propiedades" && (
                            <div className="dash-card">
                                <div className="empty-state">
                                    <h3>No tienes propiedades registradas</h3>
                                    <p> Comienza a monetizar tus propiedades en nuestra plataforma.</p>
                                    <button className="btn-add"> + Añadir Propiedad</button>
                                </div>
                            </div>
                        )}
                </section>
                
            </main>
        </div>
    );
};

export default Perfil;