import { useState, useEffect } from "react";
import axios from "axios";

const AddTicketForm = () => {
  // Get userId from local storage
  const storedUser = JSON.parse(localStorage.getItem("user"));
  const loggedInUserId = storedUser?.id;

  const [formData, setFormData] = useState({
    title: "",
    description: "",
    category: "IT",
    priority: "LOW",
    createdByUserId: loggedInUserId || null,
  });

  useEffect(() => {
    // Update createdByUserId whenever loggedInUserId changes
    if (loggedInUserId) {
      setFormData((prev) => ({ ...prev, createdByUserId: loggedInUserId }));
    }
  }, [loggedInUserId]);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post("http://localhost:9090/tickets", formData);
      alert("Ticket created successfully! ID: " + response.data.id);

      // reset form
      setFormData({
        title: "",
        description: "",
        category: "IT",
        priority: "LOW",
        createdByUserId: loggedInUserId,
      });
    } catch (error) {
      console.error("Error creating ticket:", error);
      alert("Failed to create ticket");
    }
  };

  if (!loggedInUserId) {
    return <p className="text-red-500">⚠️ You must be logged in to create a ticket.</p>;
  }

  return (
    <form onSubmit={handleSubmit} className="p-4 max-w-md mx-auto bg-white shadow rounded">
      <h4 className="text-xl font-bold mb-4">Create New Ticket</h4>

      <label className="block mb-2">
        Title:
        <input
          type="text"
          name="title"
          value={formData.title}
          onChange={handleChange}
          required
          className="w-full border px-2 py-1 rounded"
        />
      </label>

      <label className="block mb-2">
        Description:
        <textarea
          name="description"
          value={formData.description}
          onChange={handleChange}
          required
          className="w-full border px-2 py-1 rounded"
        />
      </label>

      <label className="block mb-2">
        Category:
        <select
          name="category"
          value={formData.category}
          onChange={handleChange}
          className="w-full border px-2 py-1 rounded"
        >
          <option value="HR">HR</option>
          <option value="IT">IT</option>
          <option value="FINANCE">Finance</option>
        </select>
      </label>

      <label className="block mb-2">
        Priority:
        <select
          name="priority"
          value={formData.priority}
          onChange={handleChange}
          className="w-full border px-2 py-1 rounded"
        >
          <option value="LOW">Low</option>
          <option value="MEDIUM">Medium</option>
          <option value="HIGH">High</option>
        </select>
      </label>

      <button
        type="submit"
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
      >
        Submit
      </button>
    </form>
  );
};

export default AddTicketForm;
