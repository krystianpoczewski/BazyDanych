// Function to get the cookie value by name
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}




// Function to check if the user has the ADMIN role
function checkAdminRole() {
    const token = getCookie('jwt');
    if (token) {
        try {
            const decodedToken = jwt_decode(token);
            const role = decodedToken.role || "";

            if (role.includes("ADMIN")) {
                document.getElementById('admin-panel-link').style.display = 'inline'; // Show Admin Panel link
            }
        } catch (error) {
            console.error('Error decoding token:', error);
        }
    }
}

window.onload = () => {
    checkAdminRole();
};
