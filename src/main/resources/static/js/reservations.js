import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reservationList = document.getElementById('reservationList');
    const reservationForm = document.getElementById('reservationForm');

    // Pobierz i wyświetl rezerwacje
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
                </div>
            `).join('');
        } catch (error) {
            console.error('Failed to fetch reservations:', error);
        }
    };

    // Dodaj nową rezerwację
    reservationForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const checkInDate = document.getElementById('checkInDate').value;
        const checkOutDate = document.getElementById('checkOutDate').value;
        const roomId = document.getElementById('roomId').value;

        try {
            await fetchData('/user/reservations', 'POST', { checkInDate, checkOutDate, roomId });
            alert('Reservation created successfully!');
            fetchReservations(); // Odśwież listę rezerwacji
        } catch (error) {
            console.error('Failed to create reservation:', error);
            alert('Failed to create reservation. Please try again.');
        }
    });

    // Załaduj rezerwacje przy otwarciu strony
    fetchReservations();
});