import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const roomAmenityList = document.getElementById('roomAmenityList');
    const newRoomAmenityForm = document.getElementById('newRoomAmenityForm');
    const editFormContainer = document.createElement('div');
    editFormContainer.id = 'editFormContainer';
    editFormContainer.style.display = 'none';
    document.body.appendChild(editFormContainer);

    const fetchAllRoomAmenities = async () => {
        try {
            const roomAmenities = await fetchData('/user/room-amenities');
            renderRoomAmenities(roomAmenities);
        } catch (error) {
            console.error('Failed to fetch room amenities:', error);
            alert('Failed to load room amenities. Please try again.');
        }
    };

    const renderRoomAmenities = (roomAmenities) => {
        roomAmenityList.innerHTML = roomAmenities.map(roomAmenity => `
            <div class="room-amenity" data-id="${roomAmenity.id}">
                <p><strong>Name:</strong> <span class="room-amenity-name">${roomAmenity.name}</span></p>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `).join('');
        attachEventListeners();
    };

    const addRoomAmenities = async (newRoomAmenity) => {
        try {
            await fetchData('/admin/room-amenities', 'POST', newRoomAmenity);
            alert('New room amenity added successfully!');
            fetchAllRoomAmenities();
        } catch (error) {
            console.error('Failed to add room amenity:', error);
            alert('Failed to add room amenity. Please try again.');
        }
    };

    const deleteRoomAmenity = async (id) => {
        try {
            await fetchData(`/admin/room-amenities/${id}`, 'DELETE');
            alert('Room amenity deleted successfully!');
            fetchAllRoomAmenities();
        } catch (error) {
            console.error('Failed to delete room amenity:', error);
            alert('Failed to delete room amenity. Please try again.');
        }
    };

    const openEditForm = (id, currentName) => {
        editFormContainer.innerHTML = `
            <div class="edit-form">
                <h2>Edit Room Amenity</h2>
                <form id="editRoomAmenityForm">
                    <label for="editName">Name:</label>
                    <textarea id="editName" name="name" required>${currentName}</textarea>
                    <button type="submit">Save Changes</button>
                    <button type="button" id="cancelEdit">Cancel</button>
                </form>
            </div>
        `;
        editFormContainer.style.display = 'block';

        document.getElementById('editRoomAmenityForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedName = document.getElementById('editName').value;

            try {
                await fetchData(`/admin/room-amenities/${id}`, 'PUT', { name: updatedName});
                alert('Room amenity updated successfully!');
                editFormContainer.style.display = 'none';
                fetchAllRoomAmenities();
            } catch (error) {
                console.error('Failed to update room amenity:', error);
                alert('Failed to update room amenity. Please try again.');
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
                const roomAmenityId = button.closest('.room-amenity').dataset.id;
                if (confirm('Are you sure you want to delete this room amenity?')) {
                    deleteRoomAmenity(roomAmenityId);
                }
            });
        });

        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const roomAmenityElement = button.closest('.room-amenity');
                const roomAmenityId = roomAmenityElement.dataset.id;
                const currentName = roomAmenityElement.querySelector('.room-amenity-name').textContent;
                openEditForm(roomAmenityId, currentName);
            });
        });
    };

    newRoomAmenityForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const newName = document.getElementById('newRoomAmenityName').value;

        addRoomAmenities({ name: newName});
    });

    fetchAllRoomAmenities();
});
