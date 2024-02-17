import { Navbar, Container, NavItem, Nav } from 'react-bootstrap';
import { Link } from 'react-router-dom';
import { LogoutButton } from './AuthComponents';

function NavHeader(props) {
  return (
  <Navbar bg="primary" variant="dark">
    <Container fluid>
      <Link to='/' className='navbar-brand'>HeapOverrun</Link>
      <Navbar.Collapse>
      <Nav>
      <NavItem><Link to='/comments/addComment' className='nav-link' style={{color: "white"}}>Add comment</Link></NavItem>
      <NavItem><Link to='/checkapi'className='nav-link' style={{color: "white"}}>Check API</Link></NavItem>
      <NavItem><Link to='/ping'className='nav-link' style={{color: "white"}}>Ping test</Link></NavItem>
      </Nav>
      </Navbar.Collapse>
      {props.loggedIn ? 
        <LogoutButton logout={props.handleLogout} /> :
        <Link to='/login'className='btn btn-outline-light'>Login</Link>
         }
    </Container>
  </Navbar>
  );
}

export default NavHeader;