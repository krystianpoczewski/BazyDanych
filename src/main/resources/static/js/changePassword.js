import { fetchData } from './api.js';  // Import the fetchData function

document.addEventListener('DOMContentLoaded', () => {
    const changePasswordForm = document.getElementById('changePasswordForm');
    const statusMessage = document.getElementById('statusMessage');

    changePasswordForm.addEventListener('submit', async (e) => {
        e.preventDefault(); // Prevent form from submitting normally

        const oldPassword = document.getElementById('oldPassword').value;
        const newPassword = document.getElementById('newPassword').value;

        // Ensure both fields are filled in
        if (!oldPassword || !newPassword) {
            statusMessage.textContent = "Please fill in both fields.";
            statusMessage.style.color = 'red';
            return;
        }

        // Send request to change password using fetchData
        try {
            await fetchData('/user/change-password', 'POST', {
                oldPassword: oldPassword,
                newPassword: newPassword,
            });

            statusMessage.textContent = 'Password changed successfully!';
            statusMessage.style.color = 'green';
        } catch (error) {
            // If there is an error, show the error message
            console.error('Error changing password:', error);
            statusMessage.textContent = 'An error occurred. Please try again.';
            statusMessage.style.color = 'red';
        }
    });
});
