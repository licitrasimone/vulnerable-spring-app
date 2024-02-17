import { useEffect, useState } from 'react';
import { Row, Col, Form, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

export default function QuestionList(props) {
  const [parsedDocument, setParsedDocument] = useState('');
  const xmlInput = `<?xml version="1.0"?>
<details>
	<username>Alice</username>
</details>`;

  const [hashValue, setHashValue] = useState('');
  const [text, setText] = useState('');   
  const [result, setResult] = useState('')

  const getHashValue = async () => {
    try {
      const response = await fetch('http://localhost:3001/api/services/crypto/sha256', {credentials: 'include'});

      if (response.ok) {
        const data = await response.text();
        setHashValue(data);
      } else {
        console.error('Failed to getHashValue');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const submitXHR = async () => {
    try {
      const response = await fetch('http://localhost:3001/api/sessions/online', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/xml',
        },
        body: xmlInput.trim(),
      });

      if (response.ok) {
        const parsedXML = await response.text();
        setParsedDocument(parsedXML);
      } else {
        console.error('Failed to submit XHR');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  };

  const submitHashPassword = async (event) => {
    event.preventDefault();
    try {
      const response = await fetch('http://localhost:3001/api/services/crypto/sha256/verify', {
        method: 'POST',
        credentials: 'include',
        body: text,
      });

      if (response.ok) {
        const data = await response.text();
        setResult(data);
      } else {
        console.error('Failed to submit ');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }

  useEffect(()=> {
    submitXHR();
  }, []);

  useEffect(()=> {
    getHashValue();
  }, []);

  return (
    <>
      <Row>
        <Col>
          <h1>Welcome to HeapOverrun!</h1>
          <p className='lead'>We now have {props.questions.length} questions available.</p>
        </Col>
      </Row>
      <Row>
        <dl>
          {
            props.questions.map((q) => <QuestionRow question={q} key={q.id}/>)
          }
        </dl>
      </Row>
      <Row>
        <Col>
          <h3>Web Site Comments</h3>
          <p className='lead'>Here the comments.</p>
            {
              props.comments.map((c) =>    
              <div key={c.id}>
              <p><strong>Comment #{c.id}</strong>: <span dangerouslySetInnerHTML={{ __html: c.text }} /></p>
              </div>
                )
            }          
            
          </Col>
          
      </Row>
      <p>------------------------------</p>
      <Row>
        <Col>
          <h5>Today's challange!</h5>
          <p><strong>Which password belongs to this hash: </strong>{hashValue}</p>
          <Form onSubmit={submitHashPassword} className="d-flex" >
            <Form.Group className='me-2'>
                <Form.Control type="text" minLength={2} required={true} value={text} onChange={(event) => setText(event.target.value)}></Form.Control>
            </Form.Group>
            <Button variant="secondary" type="submit">Try</Button>
          </Form>
          <p>{result}</p>
        </Col> 
      </Row>
      <p>------------------------------</p>      
      <Row>
        <h5>Site Information</h5>
        <Col>
          <p><strong>Online user: </strong></p><div dangerouslySetInnerHTML={{ __html: parsedDocument }} />
        </Col> 
      </Row>
    </>
  );
}

function QuestionRow(props) {
  return (
    <>
      <dt>Question #{props.question.id}: <Link to={`/questions/${props.question.id}`}>{props.question.text}</Link></dt>
      <dd>Asked by {props.question.author} on {props.question.date.format('YYYY-MM-DD')}</dd>
    </>
  );
}