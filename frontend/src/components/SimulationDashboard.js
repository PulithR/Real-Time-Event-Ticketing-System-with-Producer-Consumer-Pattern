// SimulationDashboard.js
import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import WebSocketComponent from './WebSocketComponent';
import './SimulationDashboard.css';

const SimulationDashboard = () => {
    const { id } = useParams();
    const [configuration, setConfiguration] = useState(null);
    const [logs, setLogs] = useState([]);

    useEffect(() => {
        axios.get(`http://localhost:8080/api/configurations/${id}`)
            .then(response => setConfiguration(response.data))
            .catch(error => alert('Error fetching configuration:', error.message));
    }, [id]);

    const startSimulation = () => {
        axios.post(`http://localhost:8080/simulation/start/${id}`)
            .then(response => alert(response.data))
            .catch(error => alert('Error starting simulation:', error.message));
    };

    const stopSimulation = () => {
        axios.post('http://localhost:8080/simulation/stop')
            .then(response => alert(response.data))
            .catch(error => alert('Error stopping simulation:', error.message));
    };

    return (
        <div className="simulation-dashboard">
            <h1>Simulation Dashboard</h1>
            {configuration && (
                <div className="config-details">
                    <p>Max Tickets: {configuration.maxTicketCapacity}</p>
                    <p>Total Tickets: {configuration.totalTickets}</p>
                    <p>Vendors: {configuration.vendorNum}</p>
                    <p>Customers: {configuration.customerNum}</p>
                    <p>Ticket Release Rate: {configuration.ticketReleaseRate} ms</p>
                    <p>Customer Retrieval Rate: {configuration.customerRetrievalRate} ms</p>
                </div>
            )}
            <div className="simulation-controls">
                <button onClick={startSimulation}>Start</button>
                <button onClick={stopSimulation}>Stop</button>
            </div>
            <div className="log-container">
                <WebSocketComponent logs={logs} setLogs={setLogs} />
            </div>
        </div>
    );
};

export default SimulationDashboard;
