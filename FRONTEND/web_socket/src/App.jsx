import { useRef, useState } from 'react'
import './App.css'

const App = () => {
  const [userId, setUserId] = useState("");
  const [toUser, setToUser] = useState("");
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([]);
  const socketRef = useRef(null);

  const connectSocket = () => {
    socketRef.current = new WebSocket(`ws://localhost:8080/ws?userId=${userId}`);

    socketRef.current.onopen = () => console.log("Connected");
    socketRef.current.onmessage = (e) => setMessages((prev) => [...prev, e.data]);
  };

  const sendMessage = () => {
    socketRef.current.send(
      JSON.stringify({ from: userId, to: toUser, message: input })
    );
    setInput("");
  };

  return (
    <div style={{ padding: 20 }}>
      <h2>Chat WebSocket App</h2>
      <input placeholder="Your ID" value={userId} onChange={e => setUserId(e.target.value)} />
      <button onClick={connectSocket}>Connect</button>
      <br /><br />
      <input placeholder="To (User ID)" value={toUser} onChange={e => setToUser(e.target.value)} />
      <input value={input} onChange={e => setInput(e.target.value)} placeholder="Message" />
      <button onClick={sendMessage}>Send</button>
      <ul>
        {messages.map((msg, i) => <li key={i}>{msg}</li>)}
      </ul>
    </div>
  );
};

export default App
