import React from 'react';
import "./App.css";
import Register from "./components/Register";
import Login from "./components/Login";
import HomePage from "./components/HomePage";
import { BrowserRouter as Router, Routes, Route} from 'react-router-dom';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/register" element = {<Register/>}/>
        <Route path="/login" element = {<Login/>}/>
        <Route path="/" element = {<HomePage/>}/>
      </Routes>
    </Router>
  );
}

export default App;
