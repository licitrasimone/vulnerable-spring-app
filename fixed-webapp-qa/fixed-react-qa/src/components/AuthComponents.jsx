import { useEffect, useState } from 'react';
import {Form, Button} from 'react-bootstrap';

function LoginForm(props) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [csrf, setCsrf] = useState('');

  useEffect(() => {
    const getCSRF = async () => {
      try {
        const response = await fetch('http://localhost:3001/api/sessions/csrf');
        const data = await response.text();
        setCsrf(data);
      } catch (error) {
        setCsrf('Error!');
      }
    };
    getCSRF();
  }, []);
  
  const handleSubmit = (event) => {
      event.preventDefault();
      const credentials = { username, password, csrf };
      
      props.login(credentials);
  };

  return (
    <Form onSubmit={handleSubmit}>
      <Form.Group controlId='username'>
          <Form.Label>email</Form.Label>
          <Form.Control type='email' value={username} onChange={ev => setUsername(ev.target.value)} required={true} />
      </Form.Group>

      <Form.Group controlId='password'>
          <Form.Label>Password</Form.Label>
          <Form.Control type='password' value={password} onChange={ev => setPassword(ev.target.value)} required={true} minLength={6}/>
      </Form.Group>

      <Form.Group controlId='csrf'>
          <Form.Control type='text' hidden value={csrf} readOnly/>
      </Form.Group>

      <Button type="submit">Login</Button>
  </Form>
  )
};

function LogoutButton(props) {
  return(
    <Button variant='outline-light' onClick={props.logout}>Logout</Button>
  )
}

export { LoginForm, LogoutButton };