import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import AddConfiguration from './AddConfiguration';

function ConfigurationList() {
  const [configurations, setConfigurations] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchConfigurations();
  }, []);

  const fetchConfigurations = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/configurations/getAll');
      setConfigurations(response.data);
    } catch (error) {
      console.error('Error fetching configurations:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/api/configurations/${id}`);
      setConfigurations(configurations.filter(config => config.id !== id));
    } catch (error) {
      console.error('Error deleting configuration:', error);
    }
  };

  const handleConfigurationAdded = (newConfig) => {
    setConfigurations([...configurations, newConfig]);
  };

  return (
    <div className="configuration-dashboard">
      <AddConfiguration onConfigurationAdded={handleConfigurationAdded} />

      <div className="configurations-grid">
        {configurations.map(config => (
          <div key={config.id} className="configuration-card">
            <div className="configuration-details">
              <h3>Configuration Details</h3>
              <p>Total Tickets: {config.totalTickets}</p>
              <p>Number of Vendors: {config.vendorNum}</p>
              <p>Number of Customers: {config.customerNum}</p>
              <p>Ticket Release Rate: {config.ticketReleaseRate}</p>
              <p>Customer Retrieval Rate: {config.customerRetrievalRate}</p>
              <p>Max Ticket Capacity: {config.maxTicketCapacity}</p>
            </div>
            <div className="configuration-actions">
              <button
                className="delete-button hover-effect"
                onClick={() => handleDelete(config.id)}
              >
                Delete
              </button>
              <button
                className="simulate-button hover-effect"
                onClick={() => navigate(`/dashboard/${config.id}`)}
              >
                Go to Dashboard
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ConfigurationList;
