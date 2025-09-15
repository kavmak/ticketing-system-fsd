import { useEffect } from "react";
import { useState } from "react";
import Card from "../common/Card";



function ViewAllTickets() {
    const [tickets, setTickets] = useState([]);

    useEffect(() => {
        loadTickets();
    }, []);

    function loadTickets() {
        console.log("load tickets");
        //fetch tickets from backend
        fetch("http://localhost:8080/tickets")
            .then(response => response.json())
            .then(data => {
                console.log("Tickets data:", data);
                setTrips(data);
                // Handle the trips data (e.g., update state, display in UI)
            })
            .catch(error => {
                console.error("Error fetching tickets:", error);
            });
    }
    const listTickets = tickets.map((ticket, i) => {
        return (
            <Card key={i} ticket={ticket}></Card>
        );
    });

    return (
        <div className='container'>
            <h1>View Tickets Component</h1>

            <div className="container">
                <div className="row">
                    {listTickets}
                </div>
                <div className='row'>
                    <table className="table">
                        <thead>
                            <tr>
                                <th scope="col">#</th>
                                <th scope="col">Title</th>
                                <th scope="col">Description</th>
                            </tr>
                        </thead>
                        <tbody>
                            {tickets.map((ticket, index) => (
                                <tr key={ticket.id}>
                                    <th scope="row">{index + 1}</th>
                                    <td>{ticket.title}</td>
                                    <td>{ticket.description}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}
export default ViewAllTickets;