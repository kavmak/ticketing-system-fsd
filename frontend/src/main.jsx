import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import Header from './components/common/Navbar.jsx'

createRoot(document.getElementById('root')).render(
  <BrowserRouter>
    <Header/>
    {/* <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/about" element={<About />} />
      <Route path="/trips" element={<ViewTrips />} />
      <Route path="/addtrip" element={<AddTrip />} />
    </Routes> */}
  </BrowserRouter>,
)
