const API_URL = 'https://localhost:8080/api'; // Adjust for your environment

async function fetchData(endpoint, method = 'GET', body = null) {
    const defaultHeaders = {
        'Content-Type': 'application/json',
    };

    const token = localStorage.getItem('token');
    const headers = { ...defaultHeaders };
    if (token) {
        headers.Authorization = `Bearer ${token}`;
    }

    const options = {
        method,
        headers,
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    try {
        const response = await fetch(`${API_URL}${endpoint}`, options);
        if (!response.ok) {
            let errorMessage = `HTTP error! status: ${response.status}`;
            try {
                const errorData = await response.json();
                errorMessage += ` - ${errorData.message || 'Unknown error'}`;
            } catch (e) {
                // Ignore JSON parsing errors for non-JSON responses
            }
            throw new Error(errorMessage);
        }
        return response.json(); // Expecting a JSON response
    } catch (error) {
        console.error('API request failed:', error);
        throw error; // Re-throw error for higher-level handling
    }
}

export { fetchData };
