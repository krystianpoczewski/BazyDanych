import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const roomList = document.getElementById('roomList');
    const newRoomFormContainer = document.createElement('div');
    const editFormContainer = document.createElement('div');

    newRoomFormContainer.id = 'newRoomFormContainer';
    editFormContainer.id = 'editFormContainer';
    editFormContainer.style.display = 'none';

    document.body.insertBefore(newRoomFormContainer, roomList);
    document.body.appendChild(editFormContainer);

    // Fetch and render all rooms
    const fetchAllRooms = async () => {
        try {
            const rooms = await fetchData('/user/room');
            renderRooms(rooms);
        } catch (error) {
            console.error('Failed to fetch rooms:', error);
            alert('Failed to load rooms. Please try again.');
        }
    };

    // Render all rooms
    const renderRooms = (rooms) => {
        roomList.innerHTML = rooms.map(room => `
            <div class="room" data-id="${room.id}">
                <h3>${room.type.name}</h3>
                <p>Id: ${room.id}</p>
                <p>Capacity: ${room.capacity}</p>
                <p>Price per night: $${room.pricePerNight.toFixed(2)}</p>
                <p>Amenities:</p>
                <ul>
                    ${room.amenities.map(amenity => `<li>${amenity.name}</li>`).join('')}
                </ul>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `).join('');
        attachEventListeners();
    };

    // Add a new room
    const addRoom = async (newRoom) => {
        try {
            await fetchData('/admin/room', 'POST', {
                type: { id: parseInt(newRoom.type) },
                capacity: parseInt(newRoom.capacity),
                pricePerNight: parseFloat(newRoom.pricePerNight),
                amenities: newRoom.amenities.map(id => ({ id }))
            });
            alert('New room added successfully!');
            fetchAllRooms();
        } catch (error) {
            console.error('Failed to add room:', error);
            alert('Failed to add room. Please try again.');
        }
    };

    // Update an existing room
    const updateRoom = async (roomId, updatedRoom) => {
        try {
            await fetchData(`/admin/room/${roomId}`, 'PUT', {
                type: { id: parseInt(updatedRoom.type) },
                capacity: parseInt(updatedRoom.capacity),
                pricePerNight: parseFloat(updatedRoom.pricePerNight),
                amenities: updatedRoom.amenities.map(id => ({ id }))
            });
            alert('Room updated successfully!');
            fetchAllRooms();
        } catch (error) {
            console.error('Failed to update room:', error);
            alert('Failed to update room. Please try again.');
        }
    };

    // Open the edit form
    const openEditForm = async (roomId) => {
        try {
            const room = await fetchData(`/user/room/${roomId}`);
            const roomTypes = await fetchData('/user/room-type');
            const roomAmenities = await fetchData('/user/room-amenities');

            const roomTypeOptions = roomTypes.map(type => `
                <option value="${type.id}" ${room.type.id === type.id ? 'selected' : ''}>${type.name}</option>
            `).join('');

            const roomAmenitiesCheckboxes = roomAmenities.map(amenity => `
                <label>
                    <input type="checkbox" value="${amenity.id}" ${room.amenities.some(a => a.id === amenity.id) ? 'checked' : ''}>
                    ${amenity.name}
                </label>
            `).join('<br>');

            editFormContainer.innerHTML = `
                <div class="edit-form">
                    <h2>Edit Room</h2>
                    <form id="editRoomForm">
                        <label for="editRoomType">Room Type:</label>
                        <select id="editRoomType" name="type" required>
                            ${roomTypeOptions}
                        </select>
                        <label for="editCapacity">Capacity:</label>
                        <input type="number" id="editCapacity" name="capacity" min="1" max="8" value="${room.capacity}" required>
                        <label for="editPrice">Price Per Night:</label>
                        <input type="number" id="editPrice" name="price" step="0.01" min="0" value="${room.pricePerNight}" required>
                        <label for="editAmenities">Room Amenities:</label>
                        <div id="editAmenities">${roomAmenitiesCheckboxes}</div>
                        <button type="submit">Save Changes</button>
                        <button type="button" id="cancelEdit">Cancel</button>
                    </form>
                </div>
            `;

            editFormContainer.style.display = 'block';

            document.getElementById('editRoomForm').addEventListener('submit', async (e) => {
                e.preventDefault();
                const updatedRoom = {
                    type: document.getElementById('editRoomType').value,
                    capacity: document.getElementById('editCapacity').value,
                    pricePerNight: parseFloat(document.getElementById('editPrice').value),
                    amenities: Array.from(document.querySelectorAll('#editAmenities input:checked')).map(input => input.value),
                };

                updateRoom(roomId, updatedRoom);
                editFormContainer.style.display = 'none';
            });

            document.getElementById('cancelEdit').addEventListener('click', () => {
                editFormContainer.style.display = 'none';
            });
        } catch (error) {
            console.error('Failed to fetch data for the edit form:', error);
            alert('Failed to load room data. Please try again.');
        }
    };

    // Render the add room form
    const renderAddRoomForm = async () => {
        try {
            const roomTypes = await fetchData('/user/room-type');
            const roomAmenities = await fetchData('/user/room-amenities');

            const roomTypeOptions = roomTypes.map(type => `<option value="${type.id}">${type.name}</option>`).join('');
            const roomAmenitiesCheckboxes = roomAmenities.map(amenity => `
                <label>
                    <input type="checkbox" value="${amenity.id}">
                    ${amenity.name}
                </label>
            `).join('<br>');

            newRoomFormContainer.innerHTML = `
                <div class="add-form">
                    <h2>Add New Room</h2>
                    <form id="addRoomForm">
                        <!-- Room Type -->
                        <div class="form-group">
                            <label for="addRoomType">Room Type:</label>
                            <select id="addRoomType" name="type" required>
                                ${roomTypeOptions}
                            </select>
                        </div>
                        <!-- Capacity -->
                        <div class="form-group">
                            <label for="addCapacity">Capacity:</label>
                            <input type="number" id="addCapacity" name="capacity" min="1" max="8" required>
                        </div>
                        <!-- Price per Night -->
                        <div class="form-group">
                            <label for="addPrice">Price Per Night:</label>
                            <input type="number" id="addPrice" name="price" step="0.01" min="0" required>
                        </div>
                        <!-- Amenities -->
                        <div class="form-group">
                            <label for="addAmenities">Amenities:</label>
                            <div id="addAmenities">
                                ${roomAmenitiesCheckboxes}
                            </div>
                        </div>
                        <button type="submit">Add Room</button>
                    </form>
                </div>

            `;

            document.getElementById('addRoomForm').addEventListener('submit', async (e) => {
                e.preventDefault();
                const newRoom = {
                    type: document.getElementById('addRoomType').value,
                    capacity: document.getElementById('addCapacity').value,
                    pricePerNight: parseFloat(document.getElementById('addPrice').value),
                    amenities: Array.from(document.querySelectorAll('#addAmenities input:checked')).map(input => input.value),
                };

                addRoom(newRoom);
            });
        } catch (error) {
            console.error('Failed to render add room form:', error);
            alert('Failed to load data for add room form. Please try again.');
        }
    };
    // Delete a room
    const deleteRoom = async (id) => {
        try {
            await fetchData(`/admin/room/${id}`, 'DELETE');
            alert('Room deleted successfully!');
            fetchAllRooms();
        } catch (error) {
            console.error('Failed to delete room:', error);
            alert('Failed to delete room. Please try again.');
        }
    };

    const attachEventListeners = () => {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        const editButtons = document.querySelectorAll('.edit-btn');

        deleteButtons.forEach(button => {
            button.addEventListener('click', () => {
                const roomId = button.closest('.room').dataset.id;
                if (confirm('Are you sure you want to delete this room?')) {
                    deleteRoom(roomId);
                }
            });
        });

        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const roomId = button.closest('.room').dataset.id;
                openEditForm(roomId);
            });
        });
    };

    await renderAddRoomForm();
    fetchAllRooms();
});
