import React from "react";
import "../App.css";
/*import { useNavigate } from 'react-router-dom';

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms))
}

const Auth = () => {
  const [phase, setPhase] = React.useState("login");
  const [username, setUsername] = React.useState("");
  const [surname, setSurname] = React.useState("");
  const [address, setAddress] = React.useState("");
  const [email, setEmail] = React.useState("");
  const [password, setPassword] = React.useState("");
  const [message, setMessage] = React.useState("");
  const [showAuth, setShowAuth] = React.useState(true);
  const [identifier, setIdentifier] = React.useState("");
  
  const navigate = useNavigate();

  // --- REGISTRO ---
  const handleSubmitRegister = async (e) => {
    e.preventDefault();

    if (!password || !username || !surname || !email || !address) {
      setMessage("Es obligatorio rellenar todos los campos");
      return;
    }
    
    try {
      const response = await fetch("http://localhost:8090/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: 'include',
        body: JSON.stringify({ username, surname, address, email, password }),
      });

      const data = await response.json();
      setMessage(data.message);

      if(data.message !== "Usuario registrado correctamente"){
        setMessage("Error ya existe un usuario con ese nombre o correo");
        return;
      } else {
        const messageAuth = document.getElementById("auth-message");
        messageAuth.style.color = "green";
        await sleep(1000);
        navigate('/');
      }

    } catch (error) {
      console.error(error);
      setMessage("Error al conectar con el servidor");
    }
  };



// --- LOGIN ---
const handleSubmitLogin = async (e) => {
  e.preventDefault();

  if (!identifier || !password) {
    setMessage("Es obligatorio rellenar todos los campos");
    return;
  }

  try {
    const isEmail = identifier.includes("@");
    const body = isEmail ? { email: identifier, password } : { username: identifier, password };

    const response = await fetch("http://localhost:8090/api/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: 'include',
      body: JSON.stringify(body),
    });

    const data = await response.json();
    console.log("Respuesta login:", data); 

    setMessage(data.message);

    if(data.message !== "Inicio de sesion correcto"){
      const messageAuth = document.getElementById("auth-message");
      messageAuth.style.color = "red";
      await sleep(1000);
      setMessage("");
      setPhase("register");
    } 
    else {

      //  Guardar sesión si existe usuario en la respuesta
      if (data.usuario) {
        localStorage.setItem("usuario", JSON.stringify(data.usuario));
      } else {
        console.warn("⚠ El backend no envía 'usuario'. Revisa respuesta del login.");
      }

      const messageAuth = document.getElementById("auth-message");
      messageAuth.style.color = "green";
      await sleep(800);

      // Volver a HomePage
      navigate('/');
    }

  } catch (error) {
    console.error(error);
    setMessage("Error al conectar con el servidor");
  }
};


  return (
    <>
      {showAuth && (
        <div className="form-container">
          <header>
            {phase === "register" && (
              <button className="return_button" onClick={() => {
                setPhase("login");
                setMessage("");
                setPassword(""); setUsername(""); setEmail("");
              }}>
                <span>&larr;</span>
              </button>
            )}

            {phase === "login" && (
              <button className="quit_button" onClick={() => setShowAuth(false)}>
                <span>X</span>
              </button>
            )}

            <h2>Iniciar sesión / Registrarse</h2>
          </header>

          <form onSubmit={phase === "login" ? handleSubmitLogin : handleSubmitRegister}>
            
            {phase === "login" && (
              <input type="text" id="identifier" placeholder="Usuario o correo"
                value={identifier} onChange={(e) => setIdentifier(e.target.value)} />
            )}

            {phase === "login" && (
              <input type="password" id="password" placeholder="Contraseña"
                value={password} onChange={(e) => setPassword(e.target.value)} />
            )}

            {phase === "register" && (
              <>
                <input type="text" id="username" placeholder="Nombre de usuario"
                  value={username} onChange={(e) => setUsername(e.target.value)} />

                <input type="text" id="surname" placeholder="Apellidos"
                  value={surname} onChange={(e) => setSurname(e.target.value)} />

                <input type="text" id="address" placeholder="Dirección"
                  value={address} onChange={(e) => setAddress(e.target.value)} />

                <input type="email" id="email" placeholder="Correo electrónico"
                  value={email} onChange={(e) => setEmail(e.target.value)} />

                <input type="password" id="password" placeholder="Contraseña"
                  value={password} onChange={(e) => setPassword(e.target.value)} />
              </>
            )}

            <div id="auth-message" className="login-message">
              <span>{message}</span>
            </div>

            <button style={{ backgroundColor: "#a1eafb" }}>
              {phase === "login" ? "Iniciar sesión" : "Registrarme"}
            </button>
          </form>
        </div>
      )}
    </>
  );
};

export default Auth;*/
import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function Auth() {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(true);

  const [username, setUsername] = useState("");
  const [surname, setSurname] = useState("");
  const [address, setAddress] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const sleep = (ms) => new Promise((res) => setTimeout(res, ms));

  const handleLogin = async (e) => {
    e.preventDefault();

    const body = {
      email: email.includes("@") ? email : null,
      username: !email.includes("@") ? email : null,
      password,
    };

    try {
      const res = await fetch("http://localhost:8090/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify(body),
      });

      const data = await res.json();
      const msg = document.getElementById("auth-message");
      msg.textContent = data.message;

      if (data.message === "Inicio de sesion correcto") {
        msg.style.color = "green";
        await sleep(800);
        navigate("/");
      } else msg.style.color = "red";
    } catch (err) {
      console.error("Error en login", err);
    }
  };

  const handleRegister = async (e) => {
    e.preventDefault();

    try {
      const res = await fetch("http://localhost:8090/api/auth/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        credentials: "include",
        body: JSON.stringify({ username, surname, address, email, password }),
      });

      const data = await res.json();
      const msg = document.getElementById("auth-message");
      msg.textContent = data.message;

      if (data.message === "Usuario registrado correctamente") {
        msg.style.color = "green";
        await sleep(800);
        setIsLogin(true);
      } else msg.style.color = "red";
    } catch (err) {
      console.error("Error en registro", err);
    }
  };

  return (
    <div className="auth-container">
      
      <h2>{isLogin ? "Iniciar Sesión" : "Registrarse"}</h2>

      <div className="form-container">
        <form onSubmit={isLogin ? handleLogin : handleRegister}>

          {!isLogin && (
            <>
              <input
                type="text"
                placeholder="Nombre de usuario"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />

              <input
                type="text"
                placeholder="Apellidos"
                value={surname}
                onChange={(e) => setSurname(e.target.value)}
                required
              />

              <input
                type="text"
                placeholder="Dirección"
                value={address}
                onChange={(e) => setAddress(e.target.value)}
                required
              />

              <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </>
          )}

          {isLogin && (
            <input
              type="text"
              placeholder="Email o Usuario"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          )}

          <input
            type="password"
            placeholder="Contraseña"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit">
            {isLogin ? "Entrar" : "Crear Cuenta"}
          </button>
        </form>
      </div>

      <p id="auth-message" className="login-message"></p>

      <p
        onClick={() => {
          setIsLogin(!isLogin);
          setEmail("");
          setPassword("");
        }}
        style={{ cursor: "pointer", textDecoration: "underline" }}
      >
        {isLogin
          ? "¿No tienes cuenta? Regístrate"
          : "¿Ya tienes cuenta? Inicia sesión"}
      </p>
    </div>
  );
}
