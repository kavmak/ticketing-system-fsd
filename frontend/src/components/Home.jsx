import { useState,useEffect } from "react";
import axios from "axios";
import Tickets from "./tickets/Tickets";

const API_URL = import.meta.env.VITE_API_URL;




function Home() {

  const [tickets, setTickets] = useState([]);

  useEffect(() => {
    axios
      .get(`${API_URL}/tickets`)
      .then((response) => {
        setTickets(response.data);
      })
      .catch((error) => {
        console.error("Error fetching tickets:", error);
      });
  }, []);

  return (
    <div className="p-6">
      <h1 className="text-2xl font-bold mb-4">Tickets</h1>
      <Tickets tickets={tickets} />
    </div>
  );
}
export default Home;