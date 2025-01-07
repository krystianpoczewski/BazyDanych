const API_URL = 'http://localhost:8080/api'; // Zmień na adres swojego backendu

async function fetchData(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
        },
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    // Dodaj token JWT do nagłówków, jeśli jest dostępny
    const token = localStorage.getItem('token');
    if (token) {
        options.headers.Authorization = `Bearer ${token}`;
    }

    const response = await fetch(`${API_URL}${endpoint}`, options);
    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }
    return response.json();
}

export { fetchData };