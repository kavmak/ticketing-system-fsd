import { useState,useEffect } from "react";
import axios from "axios";

const AddTicketForm = () => {
 
  //setting userID from local storage for now
  const storedUser= JSON.parse(localStorage.getItem("user"));
  const loggedInUser=storedUser?.id;

  const[formData, setFormData]= useState({
    title:"",
    description:"",
    category:"IT",
    priority:"LOW",
    createdByUserId:loggedInUser || null,
  });


  const handleChange =(e)=>{
    setFormData({
        ...formData,
        [e.target.name]:e.target.value,
    });
  };
    return (
//    <div className="p-6">
//       <h1 className="text-2xl font-bold mb-4">Add Ticket</h1>
//     </div>
    <form className="p-4 max-w-md mx-auto bg-white shadow rounded">
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
           type="text"
           name="description"
          value={formData.description}
         onChange={handleChange}
           required
           className="w-full border px-2 py-1 rounded"
           >
        </textarea>
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