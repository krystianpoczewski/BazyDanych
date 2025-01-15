import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
    const changePasswordForm = document.getElementById('changePasswordForm');

    changePasswordForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const oldPassword = document.getElementById('oldPassword').value;
        const newPassword = document.getElementById('newPassword').value;

        if (!oldPassword || !newPassword) {
            alert("Please fill in both fields.");
            return;
        }

        try {
            await fetchData('/user/change-password', 'POST', {
                oldPassword: oldPassword,
                newPassword: newPassword,
            });

            alert('Password changed successfully!');
        } catch (error) {
            alert('An error occurred. Please try again.');
        }
    });
});
