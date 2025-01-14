import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const roomTypeList = document.getElementById('roomTypeList');
    const newRoomTypeForm = document.getElementById('newRoomTypeForm');
    const editFormContainer = document.createElement('div');
    editFormContainer.id = 'editFormContainer';
    editFormContainer.style.display = 'none';
    document.body.appendChild(editFormContainer);

    const fetchAllRoomTypes = async () => {
        try {
            const roomTypes = await fetchData('/user/room-type');
            renderRoomTypes(roomTypes);
        } catch (error) {
            console.error('Failed to fetch room types:', error);
            alert('Failed to load room types. Please try again.');
        }
    };

    const renderRoomTypes = (roomTypes) => {
        roomTypeList.innerHTML = roomTypes.map(roomType => `
            <div class="room-type" data-id="${roomType.id}">
                <p><strong>Name:</strong> <span class="room-type-name">${roomType.name}</span></p>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `).join('');
        attachEventListeners();
    };

    const addRoomType = async (newRoomType) => {
        try {
            await fetchData('/admin/room-type', 'POST', newRoomType);
            alert('New room type added successfully!');
            fetchAllRoomTypes();
        } catch (error) {
            console.error('Failed to add room type:', error);
            alert('Failed to add room type. Please try again.');
        }
    };

    const deleteRoomType = async (id) => {
        try {
            await fetchData(`/admin/room-type/${id}`, 'DELETE');
            alert('Room type deleted successfully!');
            fetchAllRoomTypes();
        } catch (error) {
            console.error('Failed to delete room type:', error);
            alert('Failed to delete room type. Please try again.');
        }
    };

    const openEditForm = (id, currentName) => {
        editFormContainer.innerHTML = `
            <div class="edit-form">
                <h2>Edit Room Type</h2>
                <form id="editRoomTypeForm">
                    <label for="editName">Name:</label>
                    <textarea id="editName" name="name" required>${currentName}</textarea>
                    <button type="submit">Save Changes</button>
                    <button type="button" id="cancelEdit">Cancel</button>
                </form>
            </div>
        `;
        editFormContainer.style.display = 'block';

        document.getElementById('editRoomTypeForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedName = document.getElementById('editName').value;

            try {
                await fetchData(`/admin/room-type/${id}`, 'PUT', { name: updatedName});
                alert('Room type updated successfully!');
                editFormContainer.style.display = 'none';
                fetchAllRoomTypes();
            } catch (error) {
                console.error('Failed to update room type:', error);
                alert('Failed to update room type. Please try again.');
            }
        });

        document.getElementById('cancelEdit').addEventListener('click', () => {
            editFormContainer.style.display = 'none';
        });
    };

    const attachEventListeners = () => {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        const editButtons = document.querySelectorAll('.edit-btn');

        deleteButtons.forEach(button => {
            button.addEventListener('click', () => {
                const roomTypeId = button.closest('.room-type').dataset.id;
                if (confirm('Are you sure you want to delete this room type?')) {
                    deleteRoomType(roomTypeId);
                }
            });
        });

        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const roomTypeElement = button.closest('.room-type');
                const roomTypeId = roomTypeElement.dataset.id;
                const currentName = roomTypeElement.querySelector('.room-type-name').textContent;
                openEditForm(roomTypeId, currentName);
            });
        });
    };

    newRoomTypeForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const newName = document.getElementById('newRoomTypeName').value;

        addRoomType({ name: newName});
    });

    fetchAllRoomTypes();
});
