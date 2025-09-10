import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import Home from './components/Home.jsx'
import { BrowserRouter, Route, Routes } from 'react-router'
import 'bootstrap/dist/css/bootstrap.css'
import Navbar from './components/common/Navbar.jsx'

createRoot(document.getElementById('root')).render(
  // <StrictMode>
  //   <App />
  // </StrictMode>,
   <BrowserRouter>
   <Navbar/>
    <Routes>
      <Route path="/" element={<Home />} />
      {/* <Route path="/about" element={<About />} />
      <Route path="/trips" element={<ViewTrips />} />
      <Route path="/addtrip" element={<AddTrip />} /> */}
    </Routes>
  </BrowserRouter>,
)
