import "../App.css";
import { HiArrowSmallLeft } from "react-icons/hi2";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

const PropiedadForm = () => {
    // Mantengo tus estados originales
    const [precioNoche, setPrecioNoche] = useState("");
    const [tipo, setTipo] = useState("APARTAMENTO");
    const [ciudad, setCiudad] = useState("");
    const [direccion, setDireccion] = useState("");
    const [descripcion, setDescripcion] = useState("");
    const [reservaDirecta, setReservaDirecta] = useState(false);

    const [message, setMessage] = useState("");
    const [messageColor, setMessageColor] = useState("red");
    
    const navigate = useNavigate();
    const { id } = useParams(); // Detectamos si hay ID en la URL
    const isEditing = Boolean(id);

    // EFECTO PARA CARGAR DATOS SI ESTAMOS EDITANDO
    useEffect(() => {
        if (isEditing) {
            fetch(`http://localhost:8090/api/inmuebles/${id}`)
                .then(res => res.json())
                .then(data => {
                    setPrecioNoche(data.precioNoche);
                    setTipo(data.tipo);
                    setCiudad(data.ciudad);
                    setDireccion(data.direccion);
                    setDescripcion(data.descripcion);
                    setReservaDirecta(data.reservaDirecta);
                })
                .catch(error => console.error("Error al cargar:", error));
        }
    }, [id, isEditing]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!precioNoche || !tipo || !ciudad || !direccion || !descripcion) {
            setMessage("Rellene todos los campos");
            setMessageColor("red");
            return;
        }

        const propietarioId = sessionStorage.getItem("userId");

        // Fechas automáticas para el backend
        const fechaInicio = new Date().toISOString().split("T")[0];
        const plusYear = new Date();
        plusYear.setFullYear(plusYear.getFullYear() + 1);
        const fechaFin = plusYear.toISOString().split("T")[0];

        const body = {
            precioNoche: parseFloat(precioNoche),
            tipo: tipo,
            ciudad: ciudad,
            direccion: direccion,
            descripcion: descripcion,
            propietarioId: propietarioId,
            fechaInicio: fechaInicio,
            fechaFin: fechaFin,
            reservaDirecta: reservaDirecta
        };

        // LÓGICA DINÁMICA: Si editamos usamos PUT a /{id}, si es alta usamos POST a /alta
        const url = isEditing 
            ? `http://localhost:8090/api/inmuebles/${id}` 
            : "http://localhost:8090/api/inmuebles/alta";
        
        const method = isEditing ? "PUT" : "POST";

        try {
            const response = await fetch(url, {
                method: method,
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body)
            });

            if (!response.ok) {
                const errorData = await response.json();
                setMessage(errorData.message || "Error al procesar la propiedad");
                setMessageColor("red");
                return;
            }

            setMessage(isEditing ? "Propiedad actualizada correctamente" : "Propiedad registrada correctamente");
            setMessageColor("green");
            await sleep(1000);
            navigate("/perfil/propiedades");

        } catch (error) {
            setMessage("Error al conectar con el servidor");
            setMessageColor("red");
        }
    };

    return (
        <div className="form-container">
            <header className="form-header">
                <button className="return_button" onClick={() => navigate(-1)}>
                    <HiArrowSmallLeft size={28} />
                </button>
                <h2> {isEditing ? "Editar Propiedad" : "Alta de Propiedad"} </h2>
            </header>

            <form onSubmit={handleSubmit}>
                <label style={{ fontWeight: "bold", display: "block", marginBottom: "5px" }}>Precio por noche (€)</label>
                <input 
                    type="number" 
                    placeholder="Precio por noche (€)" 
                    value={precioNoche}
                    onChange={(e) => setPrecioNoche(e.target.value)} 
                />

                <label style={{ fontWeight: "bold", display: "block", marginBottom: "5px" }}>Tipo de propiedad</label>
                <select 
                    value={tipo} 
                    onChange={(e) => setTipo(e.target.value)}
                    style={{ padding: "10px", borderRadius: "5px", border: "1px solid black", width: "100%", marginBottom: "10px" }}
                >
                    <option value="APARTAMENTO">Apartamento</option>
                    <option value="VIVIENDA_COMPLETA">Casa</option>
                    <option value="HABITACION">Habitación</option>
                    <option value="ESTUDIO">Estudio</option>
                </select>

                <label style={{ fontWeight: "bold", display: "block", marginBottom: "5px" }}>Ciudad</label>
                <input
                    type="text"
                    placeholder="Ciudad"
                    value={ciudad}
                    onChange={(e) => setCiudad(e.target.value)}
                />

                <label style={{ fontWeight: "bold", display: "block", marginBottom: "5px" }}>Dirección completa</label>
                <input 
                    type="text" 
                    placeholder="Dirección completa" 
                    value={direccion}
                    onChange={(e) => setDireccion(e.target.value)} 
                />

                <div style={{ margin: "10px 0", display: "flex", alignItems: "center", gap: "10px" }}>
                    <input
                        type="checkbox"
                        checked={reservaDirecta}
                        onChange={(e) => setReservaDirecta(e.target.checked)}
                        id="reservaCheck"
                    />
                    <label htmlFor="reservaCheck">Permitir reserva directa</label>
                </div>

                <label style={{ fontWeight: "bold", display: "block", marginBottom: "5px" }}>Descripción</label>
                <textarea 
                    placeholder="Descripción de la propiedad" 
                    style={{ 
                        width: "100%", padding: "10px", borderRadius: "5px", 
                        marginBottom: "10px", border: "1px solid black",
                        boxSizing: "border-box", fontFamily: "inherit"
                    }}
                    rows="4"
                    value={descripcion}
                    onChange={(e) => setDescripcion(e.target.value)} 
                />

                {message && (
                    <div style={{ color: messageColor, fontSize: "0.9em", margin: "8px 0", textAlign: "center" }}>
                        {message}
                    </div>
                )}

                <button type="submit" style={{ backgroundColor: "#a1eafb", marginTop: "10px", width: "100%", padding: "10px", fontWeight: "bold", cursor: "pointer" }}>
                    {isEditing ? "Guardar Cambios" : "Publicar Propiedad"}
                </button>
            </form>
        </div>
    );
};

export default PropiedadForm;