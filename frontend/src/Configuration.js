import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

const Configurations = () => {
  const [configurations, setConfigurations] = useState([]);
  const [newConfig, setNewConfig] = useState({
    totalTickets: "",
    vendorNum: "",
    customerNum: "",
    ticketReleaseRate: "",
    customerRetrievalRate: "",
    maxTicketCapacity: "",
  });
  const navigate = useNavigate();

  useEffect(() => {
    fetchConfigurations();
  }, []);

  const fetchConfigurations = async () => {
    try {
      const response = await axios.get("/api/configurations/getAll");
      setConfigurations(response.data);
    } catch (error) {
      console.error("Error fetching configurations:", error);
    }
  };

  const addConfiguration = async () => {
    const { totalTickets, maxTicketCapacity } = newConfig;
    if (parseInt(totalTickets) > parseInt(maxTicketCapacity)) {
      alert("Total Tickets cannot exceed Max Ticket Capacity.");
      return;
    }

    try {
      const response = await axios.post("/api/configurations/save", newConfig);
      setConfigurations([...configurations, response.data]);
      setNewConfig({
        totalTickets: "",
        vendorNum: "",
        customerNum: "",
        ticketReleaseRate: "",
        customerRetrievalRate: "",
        maxTicketCapacity: "",
      });
    } catch (error) {
      console.error("Error adding configuration:", error);
      alert("Invalid input or server error.");
    }
  };

  const deleteConfiguration = async (id) => {
    try {
      await axios.delete(`/api/configurations/${id}`);
      setConfigurations(configurations.filter((config) => config.id !== id));
    } catch (error) {
      console.error("Error deleting configuration:", error);
    }
  };

  return (
    <div style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h1>Configurations</h1>
      <div style={{ marginBottom: "20px", border: "1px solid #ccc", padding: "10px", borderRadius: "5px" }}>
        <h2>Add New Configuration</h2>
        <input
          type="number"
          placeholder="Total Tickets"
          value={newConfig.totalTickets}
          onChange={(e) => setNewConfig({ ...newConfig, totalTickets: e.target.value })}
        />
        <input
          type="number"
          placeholder="Number of Vendors"
          value={newConfig.vendorNum}
          onChange={(e) => setNewConfig({ ...newConfig, vendorNum: e.target.value })}
        />
        <input
          type="number"
          placeholder="Number of Customers"
          value={newConfig.customerNum}
          onChange={(e) => setNewConfig({ ...newConfig, customerNum: e.target.value })}
        />
        <input
          type="number"
          placeholder="Ticket Release Rate"
          value={newConfig.ticketReleaseRate}
          onChange={(e) => setNewConfig({ ...newConfig, ticketReleaseRate: e.target.value })}
        />
        <input
          type="number"
          placeholder="Customer Retrieval Rate"
          value={newConfig.customerRetrievalRate}
          onChange={(e) => setNewConfig({ ...newConfig, customerRetrievalRate: e.target.value })}
        />
        <input
          type="number"
          placeholder="Max Ticket Capacity"
          value={newConfig.maxTicketCapacity}
          onChange={(e) => setNewConfig({ ...newConfig, maxTicketCapacity: e.target.value })}
        />
        <button onClick={addConfiguration}>Add Configuration</button>
      </div>

      {configurations.map((config) => (
        <div key={config.id} style={{ marginBottom: "20px", border: "1px solid #000", padding: "10px", borderRadius: "5px" }}>
          <p>Total Tickets: {config.totalTickets}</p>
          <p>Number of Vendors: {config.vendorNum}</p>
          <p>Number of Customers: {config.customerNum}</p>
          <p>Ticket Release Rate: {config.ticketReleaseRate}</p>
          <p>Customer Retrieval Rate: {config.customerRetrievalRate}</p>
          <p>Max Ticket Capacity: {config.maxTicketCapacity}</p>
          <button onClick={() => navigate(`/simulation/${config.id}`)}>Simulation Dashboard</button>
          <button onClick={() => deleteConfiguration(config.id)}>Delete</button>
        </div>
      ))}
    </div>
  );
};

export default Configurations;
