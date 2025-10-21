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
    let handleSubmitRegister;
    if(phase === "register"){
      handleSubmitRegister = async (e) => {
        e.preventDefault();

        // Validación rápida
        if (!username || !password || !email || !address) {
          setMessage("⚠️ Debes introducir todos los datos en el formulario.");
          
          return;
        }

        try {
          const response = await fetch("http://localhost:8090/api/auth", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, address, email, password }),
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
    const handleSubmitLogin = async (e) => {
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
      //    <span>
      //      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-exclamation-circle-fill" viewBox="0 0 16 16">
      //        <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M8 4a.905.905 0 0 0-.9.995l.35 3.507a.552.552 0 0 0 1.1 0l.35-3.507A.905.905 0 0 0 8 4m.002 6a1 1 0 1 0 0 2 1 1 0 0 0 0-2"/>
      //      </svg>
      //      <span>Es obligatorio rellenar todos los campos</span>
      //    </span> 
      
      return;
    }

    try {
      // Petición al backend (simulada o real si ya existe endpoint)
      const isEmail = email === "" ? { username, password } : { email, password };
      const response = await fetch("http://localhost:8090/api/auth", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(isEmail),
      });

      if (!response.ok) {
        throw new Error("Error en la autenticación o servidor no disponible");
      }

      const data = await response.json();

      setMessage(data.message || "✅ Inicio de sesión correcto");

      if(data.message !== "✅ Inicio de sesión correcto"){
        setPhase("register"); //cambiar estado
      }

    } catch (error) {
      console.error(error);
      //setMessage("⚠️ Error al conectar con el servidor");
    }
  };

    return (
        <>
            <h2>Iniciar sesión / Registrarse</h2>

            {/* Only for test */}
            <button class="button_change" id="button_id" onClick={() => {
              setPhase(phase === "register" ? "login" : "register");
              setMessage("");
            }}>cambiar estado</button>


            <div className="form-container">
                <form onSubmit={phase === "login" ? handleSubmitLogin : handleSubmitRegister}>  
                  
                  {phase === "register" && 
                  <button class="return_button" onClick={() => {
                    setPhase(phase === "register" ? "login" : "register");
                    setMessage("");
                  }}>
                    <span>
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left-short" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5"/>
                      </svg>
                    </span>
                  </button>}

                  {phase === "login" && 
                  <button class="quit_button" onClick={() => {
                    {/* TODO: quit login form */}
                  }}>
                    <span>
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                      </svg>
                    </span>
                  </button>}

                  { phase === "login" && <input type="text" id="username" placeholder="Usuario o correo" value={username} onChange={(e) => {
                    const value = e.target.value;

                    if(value.includes("@")){
                      setEmail(value);
                      setUsername("");
                    }else{
                      setUsername(value);
                      setEmail("");
                    }

                  }}/> }

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