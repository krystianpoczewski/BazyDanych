import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', () => {
    const userSearchInput = document.getElementById('userSearch');
    const userListContainer = document.getElementById('userList');
    const editFormContainer = document.getElementById('editFormContainer');

    const fetchAllUsers = async () => {
        try {
            const users = await fetchData('/admin/users');
            renderUserList(users);
        } catch (error) {
            console.error('Error fetching users:', error);
            alert('Failed to load users. Please try again.');
        }
    };

    const searchUserByEmail = async (email) => {
        try {
            const user = await fetchData(`/admin/users/${email}`);
            renderUserList([user]);
        } catch (error) {
            console.error('Error searching user:', error);
            alert('User not found or error occurred.');
        }
    };

    const renderUserList = (users) => {
        userListContainer.innerHTML = users.map(user => `
            <div class="user-card" data-id="${user.id}">
                <p><strong>ID:</strong> ${user.id}</p>
                <p><strong>First Name:</strong> ${user.firstName}</p>
                <p><strong>Last Name:</strong> ${user.lastName}</p>
                <p><strong>Email:</strong> ${user.email}</p>
                <p><strong>Role:</strong> ${user.role}</p>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `).join('');

        attachEventListeners();
    };

    const attachEventListeners = () => {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        const editButtons = document.querySelectorAll('.edit-btn');

        deleteButtons.forEach(button => {
            button.addEventListener('click', async () => {
                const userId = button.closest('.user-card').dataset.id;
                if (confirm('Are you sure you want to delete this user?')) {
                    await deleteUser(userId);
                }
            });
        });

        editButtons.forEach(button => {
            button.addEventListener('click', async () => {
                const userId = button.closest('.user-card').dataset.id;
                openEditForm(userId);
            });
        });
    };

    const deleteUser = async (userId) => {
        try {
            await fetchData(`/admin/users/id/${userId}`, 'DELETE');
            alert('User deleted successfully!');
            fetchAllUsers();
        } catch (error) {
            console.error('Error deleting user:', error);
            alert('Failed to delete user. Please try again.');
        }
    };

    const openEditForm = async (userId) => {
        try {
            const user = await fetchData(`/admin/users/id/${userId}`);
            editFormContainer.innerHTML = `
                <h2>Edit User</h2>
                <form id="editUserForm">
                    <label for="firstName">First Name:</label>
                    <input type="text" id="firstName" name="firstName" value="${user.firstName}" required><br>

                    <label for="lastName">Last Name:</label>
                    <input type="text" id="lastName" name="lastName" value="${user.lastName}" required><br>

                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="${user.email}" required><br>

                    <label for="role">Role:</label>
                    <select id="role" name="role" required>
                        <option value="USER" ${user.role === 'USER' ? 'selected' : ''}>User</option>
                        <option value="ADMIN" ${user.role === 'ADMIN' ? 'selected' : ''}>Admin</option>
                    </select><br>
                    
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password"><br>
                    
                    <button type="submit">Save Changes</button>
                    <button type="button" id="cancelEdit">Cancel</button>
                </form>
            `;

            editFormContainer.style.display = 'block';

            document.getElementById('editUserForm').addEventListener('submit', async (e) => {
                e.preventDefault();

                const updatedUser = {
                    firstName: document.getElementById('firstName').value,
                    lastName: document.getElementById('lastName').value,
                    email: document.getElementById('email').value,
                    role: document.getElementById('role').value,
                    hashedPassword: document.getElementById('password').value,
                };

                await updateUser(userId, updatedUser);
                editFormContainer.style.display = 'none';
            });

            document.getElementById('cancelEdit').addEventListener('click', () => {
                editFormContainer.style.display = 'none';
            });
        } catch (error) {
            console.error('Error fetching user data for editing:', error);
            alert('Failed to load user data for editing.');
        }
    };

    const updateUser = async (userId, updatedUser) => {
        try {
            if (!updatedUser.password) {
                delete updatedUser.password;
            }

            await fetchData(`/admin/users/${userId}`, 'PUT', updatedUser);
            alert('User updated successfully!');
            fetchAllUsers();
        } catch (error) {
            console.error('Error updating user:', error);
            alert('Failed to update user. Please try again.');
        }
    };


    userSearchInput.addEventListener('input', async (e) => {
        const email = e.target.value;
        if (email) {
            searchUserByEmail(email);
        } else {
            fetchAllUsers();
        }
    });

    fetchAllUsers();
});
