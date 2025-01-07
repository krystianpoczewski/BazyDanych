import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const roomList = document.getElementById('roomList');
    if (roomList) {
        try {
            const rooms = await fetchData('/user/room');
            roomList.innerHTML = rooms.map(room => `
                <div class="room">
                    <h3>${room.type.name}</h3>
                    <p>Capacity: ${room.capacity}</p>
                    <p>Price per night: $${room.pricePerNight}</p>
                </div>
            `).join('');
        } catch (error) {
            console.error('Failed to fetch rooms:', error);
        }
    }
});