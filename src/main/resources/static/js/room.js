import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const roomList = document.getElementById('roomList');
    const roomSearchForm = document.getElementById('roomSearchForm');
    const roomTypeSelect = document.getElementById('roomType');

    // Fetch room types and populate the dropdown
    try {
        const roomTypes = await fetchData('/user/room-type');
        roomTypeSelect.innerHTML += roomTypes.map(roomType => `
            <option value="${roomType.id}">${roomType.name}</option>
        `).join('');
    } catch (error) {
        console.error('Failed to fetch room types:', error);
    }

    const renderRooms = (rooms) => {
        if (rooms.length === 0) {
            roomList.innerHTML = '<p>No rooms found matching your criteria.</p>';
            return;
        }

        roomList.innerHTML = rooms.map(room => `
            <div class="room">
                <h3>${room.type.name}</h3>
                <p>Id: ${room.id}</p>
                <p>Capacity: ${room.capacity}</p>
                <p>Price per night: $${room.pricePerNight}</p>
                <p>Amenities:</p>
                <ul>
                    ${room.amenities.map(amenity => `<li>${amenity.name}</li>`).join('')}
                </ul>
            </div>
        `).join('');
    };

    const fetchAndRenderRooms = async (queryParams = '') => {
        try {
            const rooms = await fetchData(`/user/room/search${queryParams}`);
            renderRooms(rooms);
        } catch (error) {
            console.error('Failed to fetch rooms:', error);
            roomList.innerHTML = '<p>Error fetching rooms.</p>';
        }
    };

    roomSearchForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        // Capture form data and convert it to query parameters
        const formData = new FormData(roomSearchForm);
        const queryParams = Array.from(formData.entries())
            .filter(([key, value]) => value) // Filter out empty values
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');

        // Fetch and render rooms based on the filters
        await fetchAndRenderRooms(queryParams ? `?${queryParams}` : '');
    });

    // Initial fetch to display all rooms
    fetchAndRenderRooms();
});
