import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reservationList = document.getElementById('userReservationList');
    const reservationForm = document.getElementById('reservationForm');
    const roomSelect = document.getElementById('roomIds');
    const amenitiesSelect = document.getElementById('amenities');

    const fetchReservations = async () => {
        try {
            const reservations = await fetchData('/user/reservations');
            reservationList.innerHTML = reservations.map(reservation => `
                <div class="reservation">
                    <p><strong>Check-in:</strong> ${new Date(reservation.checkInDate).toLocaleDateString()}</p>
                    <p><strong>Check-out:</strong> ${new Date(reservation.checkOutDate).toLocaleDateString()}</p>
                    <p><strong>Rooms ID:</strong></p>
                    <ul>
                        ${reservation.rooms.map(room => `<li>${room.id}</li>`).join('')}
                    </ul>
                    <p><strong>Amenities:</strong></p>
                    <ul>
                        ${reservation.amenities.map(amenity => `<li>${amenity.name} - $${amenity.pricePerNight}</li>`).join('')}
                    </ul>
                    <p><strong>Total price:</strong>${reservation.calculatedPrice}$</p>
                </div>
            `).join('');
        } catch (error) {
            console.error('Failed to fetch reservations:', error);
        }
    };

    const fetchRooms = async () => {
        try {
            const rooms = await fetchData('/user/room');
            roomSelect.innerHTML = rooms.map(room => `
                <option value="${room.id}">${room.id}</option>
            `).join('');
        } catch (error) {
            console.error('Failed to fetch rooms:', error);
        }
    };

    const fetchAmenities = async () => {
        try {
            const amenities = await fetchData('/user/reservation-amenities');
            amenitiesSelect.innerHTML = amenities.map(amenity => `
                <option value="${amenity.id}">${amenity.name} - $${amenity.pricePerNight}</option>
            `).join('');
        } catch (error) {
            console.error('Failed to fetch amenities:', error);
        }
    };

    reservationForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const checkInDate = document.getElementById('checkInDate').value;
        const checkOutDate = document.getElementById('checkOutDate').value;
        const rooms = Array.from(roomSelect.selectedOptions).map(option => ({ "id": parseInt(option.value, 10) }));
        const amenities = Array.from(amenitiesSelect.selectedOptions).map(option => ({ "id": parseInt(option.value, 10) }));

        try {
            await fetchData('/user/reservations', 'POST', { checkInDate, checkOutDate, rooms, amenities });
            alert('Reservation created successfully!');
            fetchReservations();
        } catch (error) {
            console.error('Failed to create reservation:', error);
            alert('Failed to create reservation. Please try again.');
        }
    });

    await fetchRooms();
    await fetchAmenities();
    fetchReservations();
});
