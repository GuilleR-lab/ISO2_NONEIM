import React from 'react';
import './App.css';
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Register from './components/Register';
import Login from './components/Login';
import HomePage from './components/HomePage';

function App() {
  return (
    //<Register/>
    <Router>
      <Routes>
        <Route path = "/" element = {<HomePage/>} />
        <Route path = "/login" element = {<Login/>} />
        <Route path = "/register" element = {<Register/>} />
      </Routes>
    </Router>
  );
}

export default App;
