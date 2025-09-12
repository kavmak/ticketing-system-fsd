import { useEffect, useState } from "react";
import axios from "axios";
import { Row, Col, Pagination } from "react-bootstrap";
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
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [pageSize, setPageSize] = useState(10);
  const [totalElements, setTotalElements] = useState(0);
  const [sortBy, setSortBy] = useState("createdAt");
  const [sortDirection, setSortDirection] = useState("desc");

 const API_URL = import.meta.env.VITE_API_URL;

  const fetchTickets = async (type, value, page = 0) => {
    try {
      let url = `${API_URL}/tickets`;
      let params = {
        page: page,
        size: pageSize,
        sortBy: sortBy,
        direction: sortDirection
      };

      if (type && value) {
        url = `${API_URL}/tickets/${type}/${value}`;
      }

      const response = await axios.get(url, { params });
      
      if (response.data && response.data.content) {
        // For paginated responses
        setTickets(response.data.content);
        setTotalPages(response.data.totalPages);
        setTotalElements(response.data.totalElements);
        setCurrentPage(response.data.number);
      } else {
        // For non-paginated responses (fallback)
        setTickets(response.data);
        setTotalPages(1);
        setTotalElements(response.data.length);
        setCurrentPage(0);
      }

      // Cache user names
      const userIds = [
        ...new Set(
          response.data.content
            ? response.data.content.flatMap((t) =>
                [t.createdByUserId, t.assignedToUserId].filter(Boolean)
              )
            : response.data.flatMap((t) =>
                [t.createdByUserId, t.assignedToUserId].filter(Boolean)
              )
        ),
      ];

      const userMap = {};
      await Promise.all(
        userIds.map(async (id) => {
          try {
            const res = await axios.get(`${API_URL}/tickets/users/${id}`);
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
      setTotalPages(0);
      setTotalElements(0);
    }
  };

  useEffect(() => {
    fetchTickets(filterType, filterValue, currentPage);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [currentPage, pageSize, sortBy, sortDirection]);

  const handleFilterClick = (type, value) => {
    setCurrentPage(0);
    if (filterType === type && filterValue === value) {
      setFilterType("");
      setFilterValue("");
      fetchTickets("", "", 0);
    } else {
      setFilterType(type);
      setFilterValue(value);
      fetchTickets(type, value, 0);
    }
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handlePageSizeChange = (e) => {
    setPageSize(parseInt(e.target.value));
    setCurrentPage(0);
  };

  const handleSort = (column) => {
    if (sortBy === column) {
      setSortDirection(sortDirection === "asc" ? "desc" : "asc");
    } else {
      setSortBy(column);
      setSortDirection("desc");
    }
    setCurrentPage(0);
  };

  const SortableHeader = ({ column, label }) => (
    <th 
      onClick={() => handleSort(column.toLowerCase().replace(" ", ""))}
      style={{ cursor: "pointer" }}
      className={sortBy === column.toLowerCase().replace(" ", "") ? "sort-active" : ""}
    >
      {label}
      {sortBy === column.toLowerCase().replace(" ", "") && (
        <span className="ms-1">
          {sortDirection === "asc" ? "↑" : "↓"}
        </span>
      )}
    </th>
  );

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

  // Generate pagination items
  const paginationItems = [];
  const maxVisiblePages = 5;
  let startPage = Math.max(0, currentPage - Math.floor(maxVisiblePages / 2));
  let endPage = Math.min(totalPages - 1, startPage + maxVisiblePages - 1);
  
  if (endPage - startPage + 1 < maxVisiblePages) {
    startPage = Math.max(0, endPage - maxVisiblePages + 1);
  }
  
  for (let i = startPage; i <= endPage; i++) {
    paginationItems.push(
      <Pagination.Item
        key={i}
        active={i === currentPage}
        onClick={() => handlePageChange(i)}
      >
        {i + 1}
      </Pagination.Item>
    );
  }

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
                  setCurrentPage(0);
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
            <div className="d-flex justify-content-between align-items-center mb-3">
              <h2 className="h4 mb-0">Tickets</h2>
              <div className="d-flex align-items-center">
                <span className="me-2">Show:</span>
                <select 
                  className="form-select form-select-sm w-auto" 
                  value={pageSize}
                  onChange={handlePageSizeChange}
                >
                  <option value="5">5</option>
                  <option value="10">10</option>
                  <option value="20">20</option>
                  <option value="50">50</option>
                </select>
                <span className="ms-2 text-muted">
                  {totalElements} total tickets
                </span>
              </div>
            </div>
            
            {/* Scrollable table container */}
            <div className="table-container flex-grow-1" style={{ overflowY: 'auto' }}>
              <table className="table table-bordered table-striped align-middle mb-0">
                <thead className="table-light">
                  <tr>
                    <SortableHeader column="id" label="ID" />
                    <SortableHeader column="title" label="Title" />
                    <th>Created By</th>
                    <th>Assigned To</th>
                    <SortableHeader column="category" label="Category" />
                    <SortableHeader column="priority" label="Priority" />
                    <SortableHeader column="status" label="Status" />
                    <SortableHeader column="createdAt" label="Created At" />
                    <SortableHeader column="updatedAt" label="Updated At" />
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
                        <td>{userCache[t.createdByUserId] || "Loading..."}{`(ID: ${t.createdByUserId})`}</td>
                        <td>{userCache[t.assignedToUserId] ? userCache[t.assignedToUserId] : "Unassigned"}</td>
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
            
            {/* Pagination */}
            {totalPages > 1 && (
              <div className="d-flex justify-content-center mt-3">
                <Pagination>
                  <Pagination.First 
                    onClick={() => handlePageChange(0)} 
                    disabled={currentPage === 0}
                  />
                  <Pagination.Prev 
                    onClick={() => handlePageChange(currentPage - 1)} 
                    disabled={currentPage === 0}
                  />
                  
                  {startPage > 0 && (
                    <>
                      <Pagination.Item onClick={() => handlePageChange(0)}>1</Pagination.Item>
                      {startPage > 1 && <Pagination.Ellipsis />}
                    </>
                  )}
                  
                  {paginationItems}
                  
                  {endPage < totalPages - 1 && (
                    <>
                      {endPage < totalPages - 2 && <Pagination.Ellipsis />}
                      <Pagination.Item onClick={() => handlePageChange(totalPages - 1)}>
                        {totalPages}
                      </Pagination.Item>
                    </>
                  )}
                  
                  <Pagination.Next 
                    onClick={() => handlePageChange(currentPage + 1)} 
                    disabled={currentPage === totalPages - 1}
                  />
                  <Pagination.Last 
                    onClick={() => handlePageChange(totalPages - 1)} 
                    disabled={currentPage === totalPages - 1}
                  />
                </Pagination>
              </div>
            )}
          </div>
        </Col>
      </Row>
    </div>
  );
}

export default Tickets;