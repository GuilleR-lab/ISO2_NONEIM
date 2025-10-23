import React from "react";
import "../App.css";

const Login = () => {
  const [phase, setPhase] = React.useState("login"); // "login" o "register"
  const [nombre, setNombre] = React.useState("");
  const [apellidos, setApellidos] = React.useState("");
  const [direccion, setDireccion] = React.useState("");
  const [email, setEmail] = React.useState("");
  const [username, setUsername] = React.useState(""); // para login por nombre
  const [password, setPassword] = React.useState("");
  const [message, setMessage] = React.useState("");

  const clearFields = () => {
    setNombre("");
    setApellidos("");
    setDireccion("");
    setEmail("");
    setUsername("");
    setPassword("");
  };

  // Registro
  const handleSubmitRegister = async (e) => {
    e.preventDefault();

    if (!nombre || !apellidos || !direccion || !email || !password) {
      setMessage("⚠️ Es obligatorio rellenar todos los campos");
      return;
    }

    try {
      const response = await fetch("http://localhost:8090/api/usuarios", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          nombre,
          apellidos,
          direccion,
          email,
          password,
        }),
      });

      const data = await response.json();
      setMessage(data.message || "✅ Usuario registrado correctamente");

      if (data.message === "✅ Usuario creado correctamente") {
        clearFields();
        setPhase("login"); // cambiar a login tras registro exitoso
      }
    } catch (error) {
      console.error(error);
      setMessage("⚠️ Error al conectar con el servidor");
    }
  };

  // Login
  const handleSubmitLogin = async (e) => {
    e.preventDefault();

    if ((!username && !email) || !password) {
      setMessage("⚠️ Es obligatorio rellenar todos los campos");
      return;
    }

    try {
      const body = email ? { email, password } : { username, password };
      const response = await fetch("http://localhost:8090/api/usuarios/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      const data = await response.json();
      setMessage(data.message || "✅ Inicio de sesión correcto");

      if (data.message === "✅ Inicio de sesión exitoso") {
        clearFields();
        // Aquí se podría redirigir a un dashboard o mantener fase login
      }
    } catch (error) {
      console.error(error);
      setMessage("⚠️ Error al conectar con el servidor");
    }
  };

  return (
    <>
      <h2>Iniciar sesión / Registrarse</h2>
      <button
        className="button_change"
        onClick={() => {
          setPhase(phase === "register" ? "login" : "register");
          setMessage("");
          clearFields(); // limpiar al cambiar de fase
        }}
      >
        Cambiar estado
      </button>

      <div className="form-container">
        <form onSubmit={phase === "login" ? handleSubmitLogin : handleSubmitRegister}>
          {phase === "login" && (
            <>
              <input
                type="text"
                placeholder="Usuario o correo"
                value={username || email}
                onChange={(e) => {
                  const value = e.target.value;
                  if (value.includes("@")) {
                    setEmail(value);
                    setUsername("");
                  } else {
                    setUsername(value);
                    setEmail("");
                  }
                }}
              />
              <input
                type="password"
                placeholder="Contraseña"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </>
          )}

          {phase === "register" && (
            <>
              <input
                type="text"
                placeholder="Nombre de usuario"
                value={nombre}
                onChange={(e) => setNombre(e.target.value)}
              />
              <input
                type="text"
                placeholder="Apellidos"
                value={apellidos}
                onChange={(e) => setApellidos(e.target.value)}
              />
              <input
                type="text"
                placeholder="Dirección"
                value={direccion}
                onChange={(e) => setDireccion(e.target.value)}
              />
              <input
                type="email"
                placeholder="Correo electrónico"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
              />
              <input
                type="password"
                placeholder="Contraseña"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </>
          )}

          {message && <p className="login-message">{message}</p>}

          <button type="submit" style={{ backgroundColor: "#a1eafb" }}>
            {phase === "login" ? "Iniciar sesión" : "Registrarme"}
          </button>
        </form>
      </div>
    </>
  );
};

export default Login;
