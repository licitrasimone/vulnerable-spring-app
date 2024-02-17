import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap';

const CheckComponents = () => {
  const [dbResponse, setDbResponse] = useState('');

  const handleCheckDB = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch('http://localhost:3001/api/services/checkapi', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams({
          apipath: 'http://127.0.0.1:3001/api/questions',
        }),
      });

      const data = await response.text();
      setDbResponse(data);
    } catch (error) {
      console.error('Error checking API:', error);
      setDbResponse('Error checking API');
    }
  };

  return (
    <>
        <h2>Check API</h2>
        <Form onSubmit={handleCheckDB}>
            <Form.Control plaintext readOnly defaultValue="http://127.0.0.1:3001/api/questions" />
            <Button variant="primary" type="submit">Check API</Button>
        </Form>
        <p><br></br>{`Got response from API: ${dbResponse}`}</p>
    </>
  );
};

export default CheckComponents;
