const API_URL = 'https://localhost:8080/api'; // Adjust for your environment

// Helper to get cookie value
function getCookie(name) {
    const cookieString = document.cookie.split('; ').find((row) => row.startsWith(`${name}=`));
    return cookieString ? cookieString.split('=')[1] : null;
}

async function fetchData(endpoint, method = 'GET', body = null) {
    const defaultHeaders = {
        'Content-Type': 'application/json',
    };

    const token = getCookie('jwt'); // Retrieve the JWT from cookies
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

        // Check if the response contains JSON
        const contentType = response.headers.get('Content-Type');
        const isJson = contentType && contentType.includes('application/json');

        // Parse the response body based on its content type
        const responseBody = isJson ? await response.json() : await response.text();

        if (!response.ok) {
            // Attach response body (JSON or text) to the error message
            const errorMessage = `HTTP error! status: ${response.status} - ${isJson ? responseBody.message : responseBody}`;
            throw new Error(errorMessage);
        }

        // Return the parsed JSON or text body for successful responses
        return responseBody;
    } catch (error) {
        console.error('API request failed:', error);
        throw error; // Re-throw error for higher-level handling
    }
}


export { fetchData };
