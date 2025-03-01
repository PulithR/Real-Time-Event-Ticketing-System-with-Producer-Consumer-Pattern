import { useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs"; // Use the browser-compatible Client from @stomp/stompjs

const WebSocketComponent = () => {
  const [messages, setMessages] = useState([]); // Store multiple messages

  useEffect(() => {
    const socket = new SockJS("http://localhost:8080/websocket");
    const stompClient = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        stompClient.subscribe(
          "/topic/simulation",
          (response) => {
            setMessages((prevMessages) => [response.body, ...prevMessages]); // Add new message to the top
          }
        );
      },
      onStompError: (error) => {
        console.error("STOMP error:", error);
      },
    });

    stompClient.activate(); // Start the connection

    return () => {
      if (stompClient) {
        stompClient.deactivate(); // Close the connection on unmount
      }
    };
  }, []);

  return (
    <div>
      <div
        style={{
          maxHeight: "300px",
          overflowY: "auto",
          padding: "10px",
        }}
      >
        <ul style={{ listStyleType: "none", padding: 0 }}>
          {messages.map((msg, index) => (
            <li key={index} style={{ marginBottom: "10px" }}>
              {msg}
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default WebSocketComponent;