import React from "react";
import "../App.css";

const Login = () => {
    // Estados para los campos y mensajes
    const [phase, setPhase] = React.useState("login"); //states login, register
    const [username, setUsername] = React.useState("");
    const [address, setAddress] = React.useState("");
    const [email, setEmail] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [message, setMessage] = React.useState("");
    
    //2º estado Register
    if(phase === "register"){
      const handleSubmit = async (e) => {
        e.preventDefault();

        // Validación rápida
        if (!username || !password || !email) {
            setMessage("⚠️ Debes introducir todos los datos en el formulario.");
          return;
        }

        try {
          const response = await fetch("http://localhost:8090/api/auth", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, email, password }),
          });

          const data = await response.json();

          setMessage(data.message || "✅ Usuario registrado correctamente");

          if(data.message !== "✅ Usuario registrado correctamente"){
            setPhase("login"); //cambiar estado
          }

        } catch (error) {
          console.error(error);
          setMessage("⚠️ Error al conectar con el servidor");
        }
      };
    }

    // Maneja el envío del formulario(login)
    const handleSubmit = async (e) => {
    e.preventDefault();
    
    // cambia color cuando introducimos algo en el input
    const inputUsername = document.getElementById("username");
    inputUsername.addEventListener("input", function() {
      if (this.value.length > 0) {
        this.style.borderColor = "black";
        this.style.background = "white";
      }
    });

    if (!username) {
      inputUsername.style.borderColor = "red";
      inputUsername.style.background = "#FFF3F3";
      setMessage("⚠️ Es obligatorio introducir el nombre o correo")
    }

    const inputPassword = document.getElementById("password");
    inputPassword.addEventListener("input", function() {
      if (this.value.length > 0) {
        this.style.borderColor = "black";
        this.style.background = "white";
      }
    });

    if (!password) {
      inputPassword.style.borderColor = "red";
      inputPassword.style.background = "#FFF3F3"
      setMessage("⚠️ Es obligatorio introducir la contraseña")
    }

    if (!username && !password) {
      setMessage("⚠️ Es obligatorio rellenar todos los campos");
      return;
    }


    try {
      // Petición al backend (simulada o real si ya existe endpoint)
      const response = await fetch("http://localhost:8090/api/auth", {
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

      if(data.message !== "✅ Inicio de sesión correcto"){
        setPhase("register"); //cambiar estado
      }
      
      // window.location.href = "/home";

    } catch (error) {
      console.error(error);
      //setMessage("⚠️ Error al conectar con el servidor");
    }
  };

    return (
        <>
            <h2>Iniciar sesión / Registrarse</h2>
            <div className="form-container">
                <form onSubmit={handleSubmit}>  
                  
                  {phase === "register" && 
                  <button>
                    <span>
                      <svg>
                      </svg>
                    </span>
                  </button>}

                  { phase === "login" && <input type="text" id="username" placeholder="Usuario o correo" value={username} onChange={(e) => setUsername(e.target.value)}/> } {/*change to recive email too*/}

                  { phase === "login" && <input type="password" id="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)}/> }

                  { phase === "register" && <input type="text" placeholder="Nombre de usuario" value={username} onChange={(e) => setUsername(e.target.value)}/> }

                  { phase === "register" && <input type="text" placeholder="Dirección" value={address} onChange={(e) => setAddress(e.target.value)}/> }

                  { phase === "register" && <input type="text" placeholder="Correo electrónico" value={email} onChange={(e) => setEmail(e.target.value)}/> }

                  { phase === "register" && <input type="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)}/> }
                  
                  { phase === "register" && <input type="password" placeholder="Confirmar contraseña" value={password} onChange={(e) => setPassword(e.target.value)}/> }
                  
                  {/* Mensaje de estado */}
                  {message && <p class="login-message" >{message}</p>}

                  <button style={{ backgroundColor: "#a1eafb" }}>
                    { phase === "login" ? "Iniciar sesión" : "Registrarme" }
                  </button>

                  { phase === "login" && <a href="#">Has olvidado tu contraseña?</a>} {/*show in Login*/}

                </form>
            </div>
        </>
    );

};

export default Login;