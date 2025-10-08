import React from "react";
import "../App.css";

const Register = () => {

    //obtain email and password from form
    const [email, setEmail] = React.useState("");
    const [password, setPassword] = React.useState("");
    const [message, setMessage] = React.useState("");

    const handleSubmit = async (e) => {
        e.preventDefault();
        try{
            const response = await fetch("http://localhost:8090/api/usuarios" , {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({email, password}),
            });

            const data = await response.json();

            setMessage(data.message);
        } catch (error){
            console.error( error);
            setMessage("⚠️ Error de conexión con el servidor");
        }
    };

    return(
        <>
            <h2>Register</h2>
            <div className="form-container">
                <form onSubmit={handleSubmit}>
                    <input type="text" placeholder="Username"/>
                    <input type="text" placeholder="Address"/>
                    <input type="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)}/>
                    <input type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
                    <button style={{ backgroundColor: "#a1eafb" }}>Register</button>
                    <p>Si ya estas registardo <a href="#">Login</a></p>
                    {message && <p>{message}</p>}
                </form>
            </div>
        </>
    );
}


export default Register;