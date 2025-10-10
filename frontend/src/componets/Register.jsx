import React from "react";
import "../App.css";

const Register = () => {

    //obtain email and password from form
    const [name, setName] = React.useState("");
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
                body: JSON.stringify({name, email, password}),
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
                    <input type="text" placeholder="Usuario" onChange={(e) => setName(e.target.value)}/>
                    <br/>
                    <input type="email" placeholder="Email" onChange={(e) => setEmail(e.target.value)}/>
                    <br/>
                    <input type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} />
                    <br/>
                    <button style={{ backgroundColor: "#a1eafb" }}>Register</button>
                    {message && <p>{message}</p>}
                </form>
            </div>
        </>
    );
}


export default Register;
