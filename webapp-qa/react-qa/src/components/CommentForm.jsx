import { useState } from 'react';
import { Form, Button, Alert } from 'react-bootstrap';
import API from '../API';
import { Link } from 'react-router-dom';


function CommentForm(props) {
    const [message, setMessage] = useState('');
    const [text, setText] = useState('');       

    const handleComment = async (event) => {
        event.preventDefault();
        try{   
            const comment = await API.addComment(text);
            setMessage(comment.text);
            
        }catch{
            setMessage("error!");
        }
    }

    return (
        <>
            <h3>Insert a comment or <Button className={"btn btn-light"}onClick={() => {window.location.href = '/'}}>Return to home</Button></h3> 
            <Form onSubmit={handleComment}>
                <Form.Group className='mb-3'>
                    <Form.Label>Text</Form.Label>
                    <Form.Control type="text" minLength={2} required={true} value={text} onChange={(event) => setText(event.target.value)}></Form.Control>
                </Form.Group>
                <><Button variant="primary" type="submit">Add</Button></>
            </Form>
            <p>Log:</p> <div dangerouslySetInnerHTML={{ __html: message }} />
        </>
    )
}

export default CommentForm;