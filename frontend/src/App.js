import React from 'react';
import './App.css';
import Auth from './components/Auth';
import HomePage from './components/HomePage';
import Perfil from './components/Perfil';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/auth" element={<Auth />} />
        <Route path="/perfil" element={<Perfil />} />
        <Route path="/" element={<HomePage />} />
      </Routes>
    </Router>
  );
}

export default App;
