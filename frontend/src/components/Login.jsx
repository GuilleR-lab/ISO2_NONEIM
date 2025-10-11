import React from "react";
import "../App.css";

const Login = () => {
    // Estados para los campos y mensajes
    const [username, setUsername] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [message, setMessage] = React.useState("");

    // Maneja el envío del formulario
    const handleSubmit = async (e) => {
    e.preventDefault();

    // Validación rápida
    if (!username || !password) {
        setMessage("⚠️ Debes introducir usuario y contraseña.");
      return;
    }

    try {
      // Petición al backend (simulada o real si ya existe endpoint)
      const response = await fetch("http://localhost:8090/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        throw new Error("Error en la autenticación o servidor no disponible");
      }

      const data = await response.json();

      setMessage(data.message || "✅ Inicio de sesión correcto");
      
      // window.location.href = "/home";

    } catch (error) {
      console.error(error);
      setMessage("⚠️ Error al conectar con el servidor o credenciales inválidas");
    }
  };

    return (
        <>
            <h2>Iniciar sesión</h2>
            <div className="form-container">
                <form onSubmit={handleSubmit}>
                <input type="text" placeholder="Usuario" value={username} onChange={(e) => setUsername(e.target.value)}/>

                <input type="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)}/>

                <button style={{ backgroundColor: "#a1eafb" }}>Log in</button>

                </form>

                {/* Mensaje de estado */}
                {message && <p>{message}</p>}

                {/* Enlace al registro */}
                <p>
                    ¿No tienes cuenta?{" "}
                    <a href="/register" style={{ color: "#007bff" }}>
                        Regístrate aquí
                    </a>
                </p>

            </div>
        </>
    );

};

export default Login;