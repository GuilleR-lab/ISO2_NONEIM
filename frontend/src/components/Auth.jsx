import React from "react";
import "../App.css";
import { useNavigate } from 'react-router-dom';

function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms))
}


const Auth = () => {
    // Estados para los campos y mensajes
    const [phase, setPhase] = React.useState("login"); //phases login, register
    const [username, setUsername] = React.useState("");
    const [surname, setSurname] = React.useState("");
    const [address, setAddress] = React.useState("");
    const [email, setEmail] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [message, setMessage] = React.useState("");
    const [showAuth, setShowAuth] = React.useState(true);
    const [identifier, setIdentifier] = React.useState(""); // estado
    
    //2º Phase Register
    let handleSubmitRegister;
    if(phase === "register"){
      handleSubmitRegister = async (e) => {
        e.preventDefault();
        
        const inputUsernameRegister = document.getElementById("username");
        inputUsernameRegister.addEventListener("input", function() {
          if (this.value.length > 0) {
            this.style.borderColor = "black";
            this.style.background = "white";
          }
        });

        if (!username) {
          inputUsernameRegister.style.borderColor = "red";
          inputUsernameRegister.style.background = "#FFF3F3";
        }

        const inputSurnameRegister = document.getElementById("surname");
        inputSurnameRegister.addEventListener("input", function() {
          if (this.value.length > 0) {
            this.style.borderColor = "black";
            this.style.background = "white";
          }
        });

        if (!surname) {
          inputSurnameRegister.style.borderColor = "red";
          inputSurnameRegister.style.background = "#FFF3F3";
        }

        const inputAddressRegister = document.getElementById("address");
        inputAddressRegister.addEventListener("input", function() {
          if (this.value.length > 0) {
            this.style.borderColor = "black";
            this.style.background = "white";
          }
        });

        if (!address) {
          inputAddressRegister.style.borderColor = "red";
          inputAddressRegister.style.background = "#FFF3F3";
        }

        const inputEmailRegister = document.getElementById("email");
        inputEmailRegister.addEventListener("input", function() {
          if (this.value.length > 0) {
            this.style.borderColor = "black";
            this.style.background = "white";
          }
        });

        if (!email) {
          inputEmailRegister.style.borderColor = "red";
          inputEmailRegister.style.background = "#FFF3F3";
        }

        const inputPasswordRegister = document.getElementById("password");
        inputPasswordRegister.addEventListener("input", function() {
          if (this.value.length > 0) {
            this.style.borderColor = "black";
            this.style.background = "white";
          }
        });

        if (!password) {
          inputPasswordRegister.style.borderColor = "red";
          inputPasswordRegister.style.background = "#FFF3F3";
        }

        if (!password || !username || !surname || !email || !address) {
          setMessage("Es obligatorio rellenar todos los campos");
          return;
        }
        
        try {
          const response = await fetch("http://localhost:8090/api/auth/register", {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, surname, address, email, password }),
          });

          const data = await response.json();
          
          setMessage(data.message);

          if(data.message !== "Usuario registrado correctamente"){
            return;
          }else{
            const messageAuth = document.getElementById("auth-message");
            messageAuth.style.color = "green";
            await sleep(1000); //wait 1 second
            navigate('/');
            return;
          }

        } catch (error) {
          console.error(error);
          setMessage("Error al conectar con el servidor");
        }
      };
    }

    // Mannage the request by the form(login)
  const navigate = useNavigate();

  const handleSubmitLogin = async (e) => {
    e.preventDefault();
    
    // change color when put anything in the input
    const inputUsername = document.getElementById("identifier");
    inputUsername.addEventListener("input", function() {
      if (this.value.length > 0) {
        this.style.borderColor = "black";
        this.style.background = "white";
      }
    });

    if(!identifier){
      inputUsername.style.borderColor = "red";
      inputUsername.style.background = "#FFF3F3";
    }

    if ((!identifier && password)) {
      setMessage("Es obligatorio introducir el nombre o correo")
      return;
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
    }
    
    if (!password && identifier) {
      setMessage("Es obligatorio introducir la contraseña")
      return;
    }

    if (!identifier && !password) {
      setMessage("Es obligatorio rellenar todos los campos"); 
      return;
    }

    try {
      // Backend request
      const isEmail = identifier.includes("@"); // o usar regex más estricto
      const body = isEmail ? { email: identifier, password } : { username: identifier, password };
      const response = await fetch("http://localhost:8090/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      const data = await response.json();

      setMessage(data.message);

      if(data.message !== "Inicio de sesion correcto"){
        const messageAuth = document.getElementById("auth-message");
        messageAuth.style.color = "red";
        
        await sleep(1000); //wait 1 second
        setMessage("");
        setPhase("register"); //change phase
        
        //clean data put in login phase
        setEmail("");
        setPassword("");
        setUsername("");
      } 
      else {
        const messageAuth = document.getElementById("auth-message");
        messageAuth.style.color = "green";
        
        await sleep(1000); //wait 1 second
        navigate('/');
        return;
      }

    } catch (error) {
      console.error(error);
      setMessage("Error al conectar con el servidor");
    }
  };

    return (
        <>
            {/* Only for test(delete after test) */}
            <button class="button_change" id="button_id" onClick={() => {
              setPhase(phase === "register" ? "login" : "register");
              setMessage("");
            }}>cambiar estado</button>


            {showAuth && <div className="form-container">
                <header>
                  {phase === "register" && <button class="return_button" onClick={() => {
                      setPhase(phase === "register" ? "login" : "register");
                      setMessage("");

                      //clean data put in register when back to login
                      setPassword("");
                      setUsername("");
                      setEmail("");
                    }}>
                    <span>
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-arrow-left-short" viewBox="0 0 16 16">
                        <path fill-rule="evenodd" d="M12 8a.5.5 0 0 1-.5.5H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H11.5a.5.5 0 0 1 .5.5"/>
                      </svg>
                    </span>
                  </button>}

                  {phase === "login" && 
                  <button class="quit_button" onClick={() => {
                    setShowAuth(false);
                  }}>
                    <span>
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-x" viewBox="0 0 16 16">
                        <path d="M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708"/>
                      </svg>
                    </span>
                  </button>}

                  <h2>Iniciar sesión / Registrarse</h2>
                </header>

                <form onSubmit={phase === "login" ? handleSubmitLogin : handleSubmitRegister}>  

                  {/*Ya se puede introducir un correo en el login, es decir que admita correo o usuario*/}
                  { phase === "login" &&
                    <input
                      type="text"
                      id="identifier"
                      placeholder="Usuario o correo"
                      value={identifier}
                      onChange={(e) => setIdentifier(e.target.value)}
                    />
                  }

                  { phase === "login" && <input type="password" id="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)}/> }

                  { phase === "register" && <input type="text" id="username" placeholder="Nombre de usuario" value={username} onChange={(e) => setUsername(e.target.value)}/> }

                  { phase === "register" && <input type="text" id="surname" placeholder="Apellidos" value={surname} onChange={(e) => setSurname(e.target.value)}/> }

                  { phase === "register" && <input type="text" id="address" placeholder="Dirección" value={address} onChange={(e) => setAddress(e.target.value)}/> }

                  { phase === "register" && <input type="email" id="email" placeholder="Correo electrónico" value={email} onChange={(e) => setEmail(e.target.value)}/> }

                  { phase === "register" && <input type="password" id="password" placeholder="Contraseña" value={password} onChange={(e) => setPassword(e.target.value)}/> }
                  
                  { phase === "register" && <input type="password" id="password" placeholder="Confirmar contraseña" value={password} onChange={(e) => setPassword(e.target.value)}/> }
                  
                  {/* Phase message */}
                  {message && (
                    <div id="auth-message" className="login-message" role="alert" aria-live="polite">
                      <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                          className="bi bi-exclamation-circle-fill" viewBox="0 0 16 16" style={{marginRight:6}}>
                        <path d="M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0M8 4a.905.905 0 0 0-.9.995l.35 3.507a.552.552 0 0 0 1.1 0l.35-3.507A.905.905 0 0 0 8 4m.002 6a1 1 0 1 0 0 2 1 1 0 0 0 0-2"/>
                      </svg>
                      <span>{message}</span>
                    </div>
                  )}

                  <button style={{ backgroundColor: "#a1eafb" }}>
                    { phase === "login" ? "Iniciar sesión" : "Registrarme" }
                  </button>

                  { phase === "login" && <a href="#">Has olvidado tu contraseña?</a>} {/*show in Login*/}

                </form>
            </div>}
        </>
    );

};

export default Auth;