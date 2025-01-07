import { fetchData } from './api.js';

document.getElementById('loginForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    try {
        const data = await fetchData('/auth/login', 'POST', { email, password });
        localStorage.setItem('token', data.token);
        alert('Login successful!');
        window.location.href = 'index.html';
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