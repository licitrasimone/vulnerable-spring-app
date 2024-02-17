import { useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import API from '../API';


function PingComponents(props) {
    const [result, setResult] = useState('');
    const [address, setAddress] = useState('google.com');       

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await fetch('http://localhost:3001/api/services/ping', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/text',
                },
                body: address
            });
            const data = await response.text();
            setResult(data);
          } catch (error) {
            setResult('Error!');
          }
    }

    return (
        <>
            <h3>Ping for FREE</h3> 
            <Form onSubmit={handleSubmit}>
                <Form.Group className='mb-3'>
                    <Form.Label>Enter an IP address below:</Form.Label>
                    <Form.Control type="text" minLength={2} required={true} value={address} onChange={(event) => setAddress(event.target.value)}></Form.Control>
                </Form.Group>
                <><Button variant="secondary" type="submit">Submit</Button></>
            </Form>
            <br></br>
            <div style={{color:"red"}}>{result}</div>
        </>
    )
}

export default PingComponents;