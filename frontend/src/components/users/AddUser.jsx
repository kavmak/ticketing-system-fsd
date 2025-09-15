import React, { useState } from "react";
import axios from "axios";

const AddUser = () => {
  // Store form input values in state
  const [user, setUser] = useState({
    name: "",
    email: "",
    password: "",
    role: "USER",
  });

  const [message, setMessage] = useState("");

  // Update state when input changes
  const handleChange = (e) => {
    setUser({
      ...user,
      [e.target.name]: e.target.value,
    });
  };

  // Submit form → save in database
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await axios.post("http://localhost:8080/users", user);
      setMessage("✅ User added successfully!");
      // Reset form after successful save
      setUser({
        name: "",
        email: "",
        password: "",
        role: "USER",
      });
    } catch (error) {
      console.error(error);
      setMessage("❌ Error: Could not add user!");
    }
  };

  return (
    <div className="container mt-4">
      <h2>Add User</h2>
      {message && <p>{message}</p>}

      <form onSubmit={handleSubmit} className="p-3 border rounded shadow-sm">
        <div className="mb-3">
          <label className="form-label">Name</label>
          <input
            type="text"
            name="name"
            value={user.name}
            onChange={handleChange}
            className="form-control"
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Email</label>
          <input
            type="email"
            name="email"
            value={user.email}
            onChange={handleChange}
            className="form-control"
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Password</label>
          <input
            type="password"
            name="password"
            value={user.password}
            onChange={handleChange}
            className="form-control"
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Role</label>
          <select
            name="role"
            value={user.role}
            onChange={handleChange}
            className="form-select"
          >
            <option value="USER">USER</option>
            <option value="AGENT">AGENT</option>
            <option value="ADMIN">ADMIN</option>
          </select>
        </div>

        <button type="submit" className="btn btn-primary">
          Add User
        </button>
      </form>
    </div>
  );
};

export default AddUser;
