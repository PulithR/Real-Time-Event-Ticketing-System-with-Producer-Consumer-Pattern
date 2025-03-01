import React, { useState } from 'react';
import axios from 'axios';

function AddConfiguration({ onConfigurationAdded }) {
  const [newConfig, setNewConfig] = useState({
    totalTickets: '',
    vendorNum: '',
    customerNum: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: ''
  });

  const [errors, setErrors] = useState({});

  const validateInput = () => {
    const newErrors = {};

    // Validate integer inputs
    const intFields = ['totalTickets', 'vendorNum', 'customerNum', 'ticketReleaseRate', 'customerRetrievalRate', 'maxTicketCapacity'];

    intFields.forEach(field => {
      const value = newConfig[field];
      if (!value) {
        newErrors[field] = 'Field is required';
      } else if (isNaN(value) || !Number.isInteger(Number(value))) {
        newErrors[field] = 'Must be an integer';
      }
    });

    // Validate total tickets less than max capacity
    if (newConfig.totalTickets && newConfig.maxTicketCapacity && Number(newConfig.totalTickets) > Number(newConfig.maxTicketCapacity)) {
      newErrors.totalTickets = 'Total tickets must be less than max capacity';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (validateInput()) {
      try {
        // Convert string inputs to numbers
        const configToSubmit = Object.fromEntries(
          Object.entries(newConfig).map(([key, value]) => [key, Number(value)])
        );

        const response = await axios.post('http://localhost:8080/api/configurations/save', configToSubmit);

        if (response.data) {
          onConfigurationAdded(response.data);
          // Reset form
          setNewConfig({
            totalTickets: '',
            vendorNum: '',
            customerNum: '',
            ticketReleaseRate: '',
            customerRetrievalRate: '',
            maxTicketCapacity: ''
          });
        }
      } catch (error) {
        console.error('Error adding configuration:', error);
        alert('Failed to add configuration');
      }
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setNewConfig(prev => ({
      ...prev,
      [name]: value
    }));
  };

  return (
    <div className="add-configuration-container">
      <h2>Add New Configuration</h2>
      <form onSubmit={handleSubmit} className="add-config-form">
        <div className="form-grid">
          {Object.keys(newConfig).map(field => (
            <div key={field} className="form-field">
              <label htmlFor={field}>
                {field.replace(/([A-Z])/g, ' $1').replace(/^./, str => str.toUpperCase())}
              </label>
              <input
                type="text"
                id={field}
                name={field}
                value={newConfig[field]}
                onChange={handleChange}
                className={errors[field] ? 'input-error' : ''}
              />
              {errors[field] && <span className="error-text">{errors[field]}</span>}
            </div>
          ))}
        </div>
        <button type="submit" className="submit-button">Add Configuration</button>
      </form>
    </div>
  );
}

export default AddConfiguration;