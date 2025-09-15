import { createRoot } from 'react-dom/client'
import './index.css'
import Home from './components/Home.jsx'
import { BrowserRouter, Route, Routes } from 'react-router'
import 'bootstrap/dist/css/bootstrap.css'
import Navbar from './components/common/Navbar.jsx'
import Tickets from './components/tickets/Tickets.jsx'
import Clients from './components/users/Clients.jsx'
import AddUser from './components/users/AddUser.jsx'
import AddTicket from './components/tickets/AddTicket.jsx'

createRoot(document.getElementById('root')).render(
   <BrowserRouter>
   <Navbar/>
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/tickets" element={<Tickets />}></Route>
      <Route path="/clients" element={<Clients />}></Route>

      <Route path="/addUser" element={<AddUser />}></Route>

      <Route path="/addTicket" element={<AddTicket />}></Route>

      {/* <Route path="/addticket" element={<AddTicket />}></Route> */}
    </Routes>
  </BrowserRouter>,
)
