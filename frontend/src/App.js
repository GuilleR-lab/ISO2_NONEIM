import React from 'react';
import './App.css';
import Auth from './components/Auth';
import HomePage from './components/HomePage';
import Perfil , {AreaPersonal, MisPropiedades} from './components/Perfil';
import Resultados from './components/Resultados';
import DetalleInmueble from './components/DetalleInmueble';
import PropiedadForm from './components/PropiedadForm';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/auth" element={<Auth />} />
        <Route path="/perfil" element={<Perfil />}>
          {/*Subrutas para propiedades y area personal*/}
          <Route path="area-personal" element={<AreaPersonal />} />
          <Route path="propiedades" element={<MisPropiedades />} />
        </Route>
        <Route path="/resultados" element={<Resultados />} />
        <Route path="/inmueble/:id" element={<DetalleInmueble />} />
        <Route path="/propiedadform/" element={<PropiedadForm />} />
        <Route path="/propiedadform/:id" element={<PropiedadForm />} />
      </Routes>
    </Router>
  );
}

export default App;