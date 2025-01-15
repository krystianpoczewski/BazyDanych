function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

function checkAdminRole() {
    const token = getCookie('jwt');
    if (token) {
        try {
            const decodedToken = jwt_decode(token);
            const role = decodedToken.role || "";

            if (role.includes("ADMIN")) {
                document.getElementById('admin-panel-link').style.display = 'inline';
            }
        } catch (error) {
            console.error('Error decoding token:', error);
        }
    }
}

window.onload = () => {
    checkAdminRole();
};
