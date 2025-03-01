// FirstPage.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import 'FirstPage.css';

const FirstPage = () => {
    const [configurations, setConfigurations] = useState([]);
    const [newConfig, setNewConfig] = useState({ maxTickets: '', totalTickets: '' });
    const navigate = useNavigate();

    useEffect(() => {
        axios.get('/api/configurations/getAll')
            .then(response => setConfigurations(response.data))
            .catch(error => console.error('Error fetching configurations:', error));
    }, []);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setNewConfig({ ...newConfig, [name]: value });
    };

    const addConfiguration = () => {
        const { maxTickets, totalTickets } = newConfig;
        if (parseInt(totalTickets) > parseInt(maxTickets)) {
            alert('Total tickets cannot exceed max ticket capacity.');
            return;
        }
        axios.post('/api/configurations/save', newConfig)
            .then(response => {
                setConfigurations([...configurations, response.data]);
                setNewConfig({ maxTickets: '', totalTickets: '' });
            })
            .catch(error => alert('Error saving configuration:', error.message));
    };

    const deleteConfiguration = (id) => {
        axios.delete(`/api/configurations/${id}`)
            .then(() => setConfigurations(configurations.filter(config => config.id !== id)))
            .catch(error => alert('Error deleting configuration:', error.message));
    };

    return (
        <div className="first-page">
            <h1>Configurations</h1>
            <div className="add-config-container">
                <h3>Add Configuration</h3>
                <input type="number" name="maxTickets" placeholder="Max Tickets" value={newConfig.maxTickets} onChange={handleInputChange} />
                <input type="number" name="totalTickets" placeholder="Total Tickets" value={newConfig.totalTickets} onChange={handleInputChange} />
                <button onClick={addConfiguration}>Add</button>
            </div>
            <div className="config-list">
                {configurations.map(config => (
                    <div key={config.id} className="config-container">
                        <p>Max Tickets: {config.maxTickets}</p>
                        <p>Total Tickets: {config.totalTickets}</p>
                        <button onClick={() => deleteConfiguration(config.id)}>Delete</button>
                        <button onClick={() => navigate(`/simulation-dashboard/${config.id}`)}>Simulation Dashboard</button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default FirstPage;
