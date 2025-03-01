import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import ConfigurationList from './components/ConfigurationList';
import SimulationDashboard from './components/SimulationDashboard';
import './index.css';

function App() {
  return (
    <Router>
      <div className="app-container">
        <Routes>
          <Route path="/" element={<ConfigurationList />} />
          <Route path="/dashboard/:id" element={<SimulationDashboard />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
