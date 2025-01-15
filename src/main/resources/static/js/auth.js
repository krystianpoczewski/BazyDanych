import { fetchData } from './api.js';


document.getElementById('loginForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });

        if (!response.ok) {
            throw new Error('Invalid credentials');
        }

        const responseData = await response.json();
        const token = responseData.token;
        if (!token) {
            throw new Error('Token not received');
        }
        localStorage.setItem('token', token);
        alert('Login successful!');
        window.location.href = '/home';
    } catch (error) {
        console.error('Login failed:', error);
        alert('Login failed. Please check your credentials.');
    }
});




document.getElementById('registerForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const firstName = document.getElementById('firstName').value;
    const lastName = document.getElementById('lastName').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        await fetchData('/auth/register', 'POST', { firstName, lastName, email, password });
        alert('Registration successful!');
        window.location.href = 'login.html';
    } catch (error) {
        console.error('Registration failed:', error);
        alert('Registration failed. Please try again.');
    }
});

