import "../App.css";
import { HiArrowSmallLeft } from "react-icons/hi2";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

const PropiedadForm = () => {
    const [precioNoche, setPrecioNoche] = useState("");
    const [tipo, setTipo] = useState("VIVIENDA_COMPLETA");
    const [descripcion, setDescripcion] = useState("");
    const [reservaDirecta, setReservaDirecta] = useState(false);

    const [direccion, setDireccion] = useState({
        pais: "",
        ciudad: "",
        codigoPostal: "",
        calle: "",
        edificio: "",
        piso: ""
    });

    const [message, setMessage] = useState("");
    const [messageColor, setMessageColor] = useState("red");

    const navigate = useNavigate();
    const { id } = useParams();
    const isEditing = Boolean(id);

    const handleDireccionChange = (e) => {
        const { name, value } = e.target;
        setDireccion(prev => ({ ...prev, [name]: value }));
    };

    useEffect(() => {
        if (isEditing) {
            fetch(`http://localhost:8090/api/inmuebles/${id}`)
                .then(res => res.json())
                .then(data => {
                    setPrecioNoche(data.precioNoche);
                    setTipo(data.tipo);
                    setDescripcion(data.descripcion);
                    const disp = data.disponibilidades?.[0];
                    setReservaDirecta(disp?.directa || false);
                    if (data.direccion && typeof data.direccion === "object") {
                        setDireccion({
                            pais: data.direccion.pais || "",
                            ciudad: data.direccion.ciudad || "",
                            codigoPostal: data.direccion.codigoPostal || "",
                            calle: data.direccion.calle || "",
                            edificio: data.direccion.edificio || "",
                            piso: data.direccion.piso || ""
                        });
                    } else {
                        setDireccion(prev => ({
                            ...prev,
                            ciudad: data.ciudad || "",
                            calle: data.direccion || ""
                        }));
                    }
                })
                .catch(error => console.error("Error al cargar:", error));
        }
    }, [id, isEditing]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!precioNoche || !tipo || !descripcion ||
            !direccion.pais || !direccion.ciudad || !direccion.codigoPostal ||
            !direccion.calle || !direccion.edificio) {
            setMessage("Rellene todos los campos obligatorios (el piso es opcional)");
            setMessageColor("red");
            return;
        }

        const propietarioId = sessionStorage.getItem("userId");
        const fechaInicio = new Date().toISOString().split("T")[0];
        const plusYear = new Date();
        plusYear.setFullYear(plusYear.getFullYear() + 1);
        const fechaFin = plusYear.toISOString().split("T")[0];

        const direccionStr = [
            direccion.calle,
            direccion.edificio,
            direccion.piso ? `Piso ${direccion.piso}` : null,
            direccion.codigoPostal
        ].filter(Boolean).join(", ");

        const body = {
            precioNoche: parseFloat(precioNoche),
            tipo: tipo,
            ciudad: direccion.ciudad,
            direccion: direccionStr,
            descripcion: descripcion,
            propietarioId: propietarioId,
            fechaInicio: fechaInicio,
            fechaFin: fechaFin,
            reservaDirecta: reservaDirecta
        };

        const url = isEditing
            ? `http://localhost:8090/api/inmuebles/alta/${id}`
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

    const labelStyle = { fontWeight: "bold", display: "block", marginBottom: "5px" };

    return (
        <div className="form-container">
            <header className="form-header">
                <button className="return_button" onClick={() => navigate(-1)}>
                    <HiArrowSmallLeft size={28} />
                </button>
                <h2>{isEditing ? "Editar Propiedad" : "Alta de Propiedad"}</h2>
            </header>

            <form onSubmit={handleSubmit}>
                <label style={labelStyle}>Precio por noche (€)</label>
                <input
                    type="number"
                    placeholder="Precio por noche (€)"
                    value={precioNoche}
                    onChange={(e) => setPrecioNoche(e.target.value)}
                />

                <label style={labelStyle}>Tipo de propiedad</label>
                <select
                    value={tipo}
                    onChange={(e) => setTipo(e.target.value)}
                    style={{ padding: "10px", borderRadius: "5px", border: "1px solid black", width: "100%", marginBottom: "10px" }}
                >
                    <option value="VIVIENDA_COMPLETA">Vivienda completa</option>
                    <option value="HABITACION">Habitación</option>
                    <option value="APARTAMENTO">Apartamento</option>
                    <option value="ESTUDIO">Estudio</option>
                </select>

                <label style={{ ...labelStyle, marginTop: "6px" }}>Dirección de la propiedad</label>
                <input type="text" placeholder="País *" name="pais" value={direccion.pais} onChange={handleDireccionChange} />
                <input type="text" placeholder="Ciudad *" name="ciudad" value={direccion.ciudad} onChange={handleDireccionChange} />
                <input type="text" placeholder="Código postal *" name="codigoPostal" value={direccion.codigoPostal} onChange={handleDireccionChange} />
                <input type="text" placeholder="Calle *" name="calle" value={direccion.calle} onChange={handleDireccionChange} />
                <input type="text" placeholder="Edificio / número *" name="edificio" value={direccion.edificio} onChange={handleDireccionChange} />
                <input type="text" placeholder="Piso (opcional)" name="piso" value={direccion.piso} onChange={handleDireccionChange} />

                <div style={{ margin: "10px 0", display: "flex", alignItems: "center", gap: "10px" }}>
                    <input
                        type="checkbox"
                        checked={reservaDirecta}
                        onChange={(e) => setReservaDirecta(e.target.checked)}
                        id="reservaCheck"
                    />
                    <label htmlFor="reservaCheck">Permitir reserva directa</label>
                </div>

                <label style={labelStyle}>Descripción</label>
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

                <button
                    type="submit"
                    style={{ backgroundColor: "#a1eafb", marginTop: "10px", width: "100%", padding: "10px", fontWeight: "bold", cursor: "pointer" }}
                >
                    {isEditing ? "Guardar Cambios" : "Publicar Propiedad"}
                </button>
            </form>
        </div>
    );
};

export default PropiedadForm;