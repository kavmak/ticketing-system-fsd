import { Link } from "react-router";
function Navbar() {
    return (
        
   <nav className="navbar navbar-expand-lg bg-body-tertiary">
  <div className="container-fluid">
    <a className="navbar-brand" href="#">Navbar</a>
    <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span className="navbar-toggler-icon"></span>
    </button>
    <div className="collapse navbar-collapse" id="navbarNav">
      <ul className="navbar-nav">
        <li className="nav-item">
          <Link className="nav-link active" aria-current="page"to="/">Home</Link>
          {/* <a  href="#">Home</a> */}
        </li>
        <li className="nav-item">
          <Link className="nav-link" aria-current="page"to="/about">About Us</Link>
        </li>
        <li className="nav-item">
          <Link className="nav-link" aria-current="page"to="/trips">Trips</Link>
        </li>
        <li className="nav-item">
            <Link className="nav-link" aria-current="page"to="/addtrip">Add Trip</Link>
          {/* <a className="nav-link disabled" aria-disabled="true">Disabled</a> */}
        </li>
      </ul>
    </div>
  </div>
</nav>
    )
}
export default Navbar;