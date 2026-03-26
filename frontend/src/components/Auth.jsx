import React from "react";
import "../App.css";
import { useNavigate } from 'react-router-dom';

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms));
}

const Auth = () => {
  const [phase, setPhase] = React.useState("login");
  const [username, setUsername] = React.useState("");
  const [name, setName] = React.useState("");
  const [surname, setSurname] = React.useState("");
  const [address, setAddress] = React.useState({
    pais: "",
    ciudad: "",
    codigoPostal: "",
    calle: "",
    edificio: "",
    piso: ""
  }

  );
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [confirmPassword, setConfirmPassword] = React.useState("");
  const [rol, setRol] = React.useState("INQUILINO");
  const [message, setMessage] = React.useState("");
  const [messageColor, setMessageColor] = React.useState("red");
  const [identifier, setIdentifier] = React.useState("");
  const navigate = useNavigate();

  const resetFields = () => {
    setUsername(""); setName(""); setSurname(""); setEmail(""); 
    setPassword(""); setConfirmPassword("");
    setIdentifier(""); setMessage(""); setRol("INQUILINO");
    setAddress({
      pais: "",
      ciudad: "",
      codigoPostal: "",
      calle: "",
      edificio: "",
      piso: ""
    });
  };

  const handleAddressChange = (e) => {
    const {name, value} = e.target;
    setAddress({...address, [name]: value});
  };

  const handleSubmitLogin = async (e) => {
    e.preventDefault();
    if (!identifier || !password) {
      setMessage("Es obligatorio rellenar todos los campos");
      setMessageColor("red");
      return;
    }

    try {
      const isEmail = identifier.includes("@");
      const body = isEmail ? { email: identifier, password } : { username: identifier, password };

      const response = await fetch("http://localhost:8090/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      const data = await response.json();

      if (!response.ok) {
        setMessage(data.message || "Error al iniciar sesión");
        setMessageColor("red");
        return;
      }

      sessionStorage.setItem("userId", data.id);
      sessionStorage.setItem("username", data.username);
      sessionStorage.setItem("email", data.email);
      sessionStorage.setItem("rol", data.rol);

      setMessage("Inicio de sesión correcto");
      setMessageColor("green");
      await sleep(800);
      navigate('/');

    } catch (error) {
      setMessage("Error al conectar con el servidor");
      setMessageColor("red");
    }
  };

  const handleSubmitRegister = async (e) => {
    e.preventDefault();
    // Validación de campos (en el caso del piso, puede estar vacío, pero el resto no)
    if (!username || !name || !surname || !email || !password || !confirmPassword || !address.pais || 
      !address.ciudad || !address.codigoPostal || !address.calle || !address.edificio) {
      setMessage("Es obligatorio rellenar todos los campos excepto el piso");
      setMessageColor("red");
      return;
    }
    if (password !== confirmPassword) {
      setMessage("Las contraseñas no coinciden");
      setMessageColor("red");
      return;
    }

    try {
      const response = await fetch("http://localhost:8090/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, name, surname, address, email, password, rol }),
      });

      const data = await response.json();

      if (!response.ok) {
        setMessage(data.message || "Error al registrarse");
        setMessageColor("red");
        return;
      }

      setMessage("Usuario registrado correctamente");
      setMessageColor("green");
      await sleep(1000);
      resetFields();
      setPhase("login");

    } catch (error) {
      setMessage("Error al conectar con el servidor");
      setMessageColor("red");
    }
  };

  return (
    <div className="form-container">
      <header>
        {phase === "register" && (
          <button className="return_button" onClick={() => { setPhase("login"); resetFields(); }}>
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
              <path fillRule="evenodd" d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5"/>
            </svg>
          </button>
        )}
        <h2>{phase === "login" ? "Iniciar sesión" : "Registrarse"}</h2>
      </header>

      <form onSubmit={phase === "login" ? handleSubmitLogin : handleSubmitRegister}>

        {phase === "login" && (
          <>
            <input type="text" placeholder="Usuario o correo" value={identifier}
              onChange={(e) => setIdentifier(e.target.value)} />
            <input type="password" placeholder="Contraseña" value={password}
              onChange={(e) => setPassword(e.target.value)} />
          </>
        )}

        {phase === "register" && (
          <>
            <input type="text" placeholder="Nombre de usuario" value={username}
              onChange={(e) => setUsername(e.target.value)} />
            <input type="text" placeholder="Nombre" value={name}
              onChange={(e) => setName(e.target.value)} />
            <input type="text" placeholder="Apellidos" value={surname}
              onChange={(e) => setSurname(e.target.value)} />
            
            <input type="text" placeholder="Pais" name="pais" value={address.pais}
              onChange={handleAddressChange} />
            <input type="text" placeholder="Ciudad" name="ciudad" value={address.ciudad}
              onChange={handleAddressChange} />
            <input type="text" placeholder="Codigo postal" name="codigoPostal" value={address.codigoPostal}
              onChange={handleAddressChange} />
            <input type="text" placeholder="Calle" name="calle" value={address.calle}
              onChange={handleAddressChange} />
            <input type="text" placeholder="Edificio" name="edificio" value={address.edificio}
              onChange={handleAddressChange} />
            <input type="text" placeholder="Piso" name="piso" value={address.piso}
              onChange={handleAddressChange} />

            <input type="email" placeholder="Correo electrónico" value={email}
              onChange={(e) => setEmail(e.target.value)} />
            <input type="password" placeholder="Contraseña" value={password}
              onChange={(e) => setPassword(e.target.value)} />
            <input type="password" placeholder="Confirmar contraseña" value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)} />

            <div style={{ margin: "8px 0", display: "flex", alignItems: "center", gap: "16px", flexWrap: "wrap" }}>
              <label>Tipo de cuenta:</label>
              <label style={{ display: "flex", alignItems: "center", gap: "4px" }}>
                <input type="radio" value="INQUILINO" checked={rol === "INQUILINO"}
                  onChange={() => setRol("INQUILINO")} /> Inquilino
              </label>
              <label style={{ display: "flex", alignItems: "center", gap: "4px" }}>
                <input type="radio" value="PROPIETARIO" checked={rol === "PROPIETARIO"}
                  onChange={() => setRol("PROPIETARIO")} /> Propietario
              </label>
            </div>
          </>
        )}

        {message && (
          <div style={{ color: messageColor, fontSize: "0.9em", margin: "8px 0" }}>
            {message}
          </div>
        )}

        <button style={{ backgroundColor: "#a1eafb" }}>
          {phase === "login" ? "Iniciar sesión" : "Registrarme"}
        </button>

        {phase === "login" && (
          <p style={{ textAlign: "center", marginTop: 10 }}>
            ¿No tienes cuenta?{" "}
            <span style={{ color: "#007bff", cursor: "pointer" }}
              onClick={() => { resetFields(); setPhase("register"); }}>
              Regístrate
            </span>
          </p>
        )}
      </form>
    </div>
  );
};

export default Auth;