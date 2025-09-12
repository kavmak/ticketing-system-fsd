import { useEffect, useState } from "react";
import axios from "axios";
import { Row, Col } from "react-bootstrap";
import './Tickets.css';

// Format date/time
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
  const [filterType, setFilterType] = useState(""); 
  const [filterValue, setFilterValue] = useState("");

  const fetchTickets = async (type, value) => {
    try {
      const url =
        type && value
          ? `http://localhost:8080/tickets/${type}/${value}`
          : "http://localhost:8080/tickets";
      const { data: ticketsData } = await axios.get(url);
      setTickets(ticketsData);

      const userIds = [
        ...new Set(
          ticketsData.flatMap((t) =>
            [t.createdBy?.id, t.assignedTo?.id].filter(Boolean)
          )
        ),
      ];

      const userMap = {};
      await Promise.all(
        userIds.map(async (id) => {
          try {
            const res = await axios.get(`http://localhost:8080/users/${id}`);
            userMap[id] = res.data.name;
          } catch {
            userMap[id] = "Unknown";
          }
        })
      );
      setUserCache(userMap);
    } catch (err) {
      console.error("Error fetching tickets", err);
      setTickets([]);
      setUserCache({});
    }
  };

  useEffect(() => {
    fetchTickets();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleFilterClick = (type, value) => {
    if (filterType === type && filterValue === value) {
      setFilterType("");
      setFilterValue("");
      fetchTickets();
    } else {
      setFilterType(type);
      setFilterValue(value);
      fetchTickets(type, value);
    }
  };

  const FilterSection = ({ title, type, options }) => (
    <div className="mb-4">
      <h3 className="fw-semibold mb-2">{title}</h3>
      <div className="d-flex flex-column gap-2">
        {options.map((opt) => {
          const selected = filterType === type && filterValue === opt;
          return (
            <button
              key={opt}
              onClick={() => handleFilterClick(type, opt)}
              className={
                "text-start btn btn-light border " +
                (selected ? "border-primary bg-light shadow-sm" : "")
              }
            >
              <input
                type="radio"
                name={type}
                readOnly
                checked={selected}
                className="me-2"
              />
              {opt}
            </button>
          );
        })}
      </div>
    </div>
  );

  return (
    <div className="px-4 py-3">
      <Row className="g-4 align-items-start">
        {/* Left: Filters */}
        <Col md={3} className="filter-panel">
          <aside className="bg-white rounded shadow-sm p-3 border h-100 d-flex flex-column">
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h5 className="mb-0">Filters</h5>
              <button
                onClick={() => {
                  setFilterType("");
                  setFilterValue("");
                  fetchTickets();
                }}
                className="btn btn-link btn-sm text-decoration-none"
              >
                Reset
              </button>
            </div>
            
            {/* Scrollable filter content */}
            <div className="filter-content flex-grow-1" style={{ overflowY: 'auto' }}>
              <FilterSection title="Status" type="status" options={["OPEN","IN_PROGRESS","CLOSED"]} />
              <FilterSection title="Priority" type="priority" options={["HIGH","MEDIUM","LOW"]} />
              <FilterSection title="Category" type="category" options={["HR","IT","FINANCE"]} />
            </div>
          </aside>
        </Col>

        {/* Right: Tickets Table */}
        <Col md={9} className="tickets-panel">
          <div className="bg-white rounded shadow-sm p-3 border h-100 d-flex flex-column">
            <h2 className="h4 mb-3">Tickets</h2>
            
            {/* Scrollable table container */}
            <div className="table-container flex-grow-1" style={{ overflowY: 'auto' }}>
              <table className="table table-bordered table-striped align-middle mb-0">
                <thead className="table-light">
                  <tr>
                    {[
                      "ID","Title","Created By","Assigned To","Category","Priority",
                      "Status","Created At","Updated At"
                    ].map((heading) => (
                      <th key={heading}>{heading}</th>
                    ))}
                  </tr>
                </thead>
                <tbody>
                  {tickets.length === 0 ? (
                    <tr>
                      <td colSpan={9} className="text-center text-muted py-4">
                        No tickets found.
                      </td>
                    </tr>
                  ) : (
                    tickets.map((t) => (
                      <tr key={t.id}>
                        <td>{t.id}</td>
                        <td>{t.title}</td>
                        <td>{userCache[t.createdBy?.id] ?? "Loading..."}</td>
                        <td>{t.assignedTo ? userCache[t.assignedTo.id] ?? "Loading..." : "Unassigned"}</td>
                        <td>{t.category}</td>
                        <td>{t.priority}</td>
                        <td>{t.status}</td>
                        <td>{formatDateTime(t.createdAt)}</td>
                        <td>{formatDateTime(t.updatedAt)}</td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </Col>
      </Row>
    </div>
  );
}

export default Tickets;