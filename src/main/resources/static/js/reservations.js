import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reservationList = document.getElementById('userReservationList');
    const reservationForm = document.getElementById('reservationForm');
    const roomSelect = document.getElementById('roomIds');
    const amenitiesSelect = document.getElementById('amenities');
    const checkInDateInput = document.getElementById('checkInDate');
    const checkOutDateInput = document.getElementById('checkOutDate');

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
                    <p><strong>Total price:</strong> $${reservation.calculatedPrice}</p>
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

    const validateDates = () => {
        const checkInDate = new Date(checkInDateInput.value);
        const checkOutDate = new Date(checkOutDateInput.value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        if (checkInDate && checkOutDate) {
            if (checkInDate <= today) {
                alert('Check-in date must be after today.');
                checkInDateInput.setCustomValidity('Check-in date must be after today.');
            } else if (checkOutDate <= today) {
                alert('Check-out date must be after today.');
                checkOutDateInput.setCustomValidity('Check-out date must be after today.');
            } else {
                const diffInTime = checkOutDate - checkInDate;
                const diffInDays = diffInTime / (1000 * 3600 * 24);

                if (diffInDays < 1) {
                    alert('Check-out date must be at least 1 day after check-in date.');
                    checkOutDateInput.setCustomValidity('Check-out date must be at least 1 day after check-in date.');
                } else if (diffInDays > 30) {
                    alert('Check-out date cannot be more than 30 days after check-in date.');
                    checkOutDateInput.setCustomValidity('Check-out date cannot be more than 30 days after check-in date.');
                } else {
                    checkOutDateInput.setCustomValidity('');
                }
            }
        }
    };


    const validateRooms = () => {
        const selectedRooms = roomSelect.selectedOptions.length;

        if (selectedRooms > 10) {
            alert('You can select a maximum of 10 rooms.');
            roomSelect.setCustomValidity('You can select a maximum of 10 rooms.');
        } else {
            roomSelect.setCustomValidity('');
        }
    };

    reservationForm?.addEventListener('submit', async (e) => {
        e.preventDefault();

        validateDates();
        validateRooms();

        if (checkInDateInput.validity.valid && checkOutDateInput.validity.valid && roomSelect.validity.valid) {
            const checkInDate = checkInDateInput.value;
            const checkOutDate = checkOutDateInput.value;
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
        }
    });

    await fetchRooms();
    await fetchAmenities();
    fetchReservations();
});
