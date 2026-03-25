import React from 'react';
import './App.css';
import Auth from './components/Auth';
import HomePage from './components/HomePage';
import Perfil from './components/Perfil';
import Resultados from './components/Resultados';
import DetalleInmueble from './components/DetalleInmueble';
import Reservar from './components/Reservar';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/auth" element={<Auth />} />
        <Route path="/perfil" element={<Perfil />} />
        <Route path="/resultados" element={<Resultados />} />
        <Route path="/inmueble/:id" element={<DetalleInmueble />} />
        <Route path="/reservar/:id" element={<Reservar />} />
      </Routes>
    </Router>
  );
}

export default App;