import { useEffect, useState } from "react";
import axios from "axios";

// ✅ Utility to format date/time (dd-MM-yyyy HH:mm)
const formatDateTime = (dateStr) => {
  if (!dateStr) return "-";
  return new Date(dateStr).toLocaleString("en-IN", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  });
};

function Tickets() {
  const [tickets, setTickets] = useState([]);
  const [userCache, setUserCache] = useState({});

  useEffect(() => {
    const fetchTicketsAndUsers = async () => {
      try {
        // 1. Fetch all tickets
        const { data: ticketsData } = await axios.get("http://localhost:9090/tickets");
        setTickets(ticketsData);

        // 2. Extract all unique user IDs
        const userIds = [
          ...new Set(
            ticketsData.flatMap((t) => [t.createdBy?.id, t.assignedTo?.id].filter(Boolean))
          ),
        ];

        // 3. Fetch all user details in parallel
        const userMap = {};
        await Promise.all(
          userIds.map(async (id) => {
            try {
              const res = await axios.get(`http://localhost:9090/users/${id}`);
              userMap[id] = res.data.name;
            } catch {
              userMap[id] = "Unknown";
            }
          })
        );

        setUserCache(userMap);
      } catch (err) {
        console.error("Error fetching tickets or users", err);
      }
    };

    fetchTicketsAndUsers();
  }, []);

  return (
    <table className="table-auto border-collapse border border-gray-300 w-full">
      <thead>
        <tr className="bg-gray-200">
          {[
            "ID",
            "Title",
            "Created By",
            "Assigned To",
            "Category",
            "Priority",
            "Status",
            "Created At",
            "Updated At",
          ].map((heading) => (
            <th key={heading} className="border px-4 py-2">
              {heading}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {tickets.map((t) => (
          <tr key={t.id}>
            <td className="border px-4 py-2">{t.id}</td>
            <td className="border px-4 py-2">{t.title}</td>
            <td className="border px-4 py-2">
              {userCache[t.createdBy?.id] || "Loading..."}
            </td>
            <td className="border px-4 py-2">
              {t.assignedTo ? userCache[t.assignedTo.id] || "Loading..." : "Unassigned"}
            </td>
            <td className="border px-4 py-2">{t.category}</td>
            <td className="border px-4 py-2">{t.priority}</td>
            <td className="border px-4 py-2">{t.status}</td>
            <td className="border px-4 py-2">{formatDateTime(t.createdAt)}</td>
            <td className="border px-4 py-2">{formatDateTime(t.updatedAt)}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default Tickets;
