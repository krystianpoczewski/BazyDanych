const API_URL = 'https://localhost:8080/api';

function getCookie(name) {
    const cookieString = document.cookie.split('; ').find((row) => row.startsWith(`${name}=`));
    return cookieString ? cookieString.split('=')[1] : null;
}

async function fetchData(endpoint, method = 'GET', body = null) {
    const defaultHeaders = {
        'Content-Type': 'application/json',
    };

    const token = getCookie('jwt');
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
    const url = endpoint.startsWith('/auth/') ? "https://localhost:8080"+endpoint : `${API_URL}${endpoint}`;
    try {
        const response = await fetch(url, options);

        const contentType = response.headers.get('Content-Type');
        const isJson = contentType && contentType.includes('application/json');

        const responseBody = isJson ? await response.json() : await response.text();

        if (!response.ok) {
            const errorMessage = `HTTP error! status: ${response.status} - ${isJson ? responseBody.message : responseBody}`;
            throw new Error(errorMessage);
        }

        return responseBody;
    } catch (error) {
        console.error('API request failed:', error);
        throw error;
    }
}


export { fetchData };
