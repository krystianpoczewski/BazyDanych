import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reservationList = document.getElementById('reservationList');
    const editFormContainer = document.getElementById('editFormContainer');
    editFormContainer.style.display = 'none';

    const fetchAllReservations = async () => {
        try {
            const reservations = await fetchData('/admin/reservations/all');
            renderReservations(reservations);
        } catch (error) {
            console.error('Failed to fetch reservations:', error);
            alert('Failed to load reservations. Please try again.');
        }
    };

    const renderReservations = (reservations) => {
        reservationList.innerHTML = reservations.map(reservation => `
            <div class="reservation" data-id="${reservation.id}">
                <h3>Reservation ID: ${reservation.id}</h3>
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
                <p><strong>Total Price:</strong> $${reservation.calculatedPrice.toFixed(2)}</p>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `).join('');
        attachEventListeners();
    };

    const openEditForm = async (reservationId) => {
        try {
            const reservation = await fetchData(`/admin/reservations/${reservationId}`);
            const rooms = await fetchData('/user/room');
            const amenities = await fetchData('/user/reservation-amenities');

            const roomOptions = rooms.map(room => `
                <label>
                    <input type="checkbox" value="${room.id}" ${reservation.rooms.some(r => r.id === room.id) ? 'checked' : ''}>
                    Room ID: ${room.id} - ${room.type.name}
                </label><br>
            `).join('');
            const amenityOptions = amenities.map(amenity => `
                <label>
                    <input type="checkbox" value="${amenity.id}" ${reservation.amenities.some(a => a.id === amenity.id) ? 'checked' : ''}>
                    ${amenity.name} - $${amenity.pricePerNight}
                </label><br>
            `).join('');

            editFormContainer.innerHTML = `
                <div class="edit-form">
                    <h2>Edit Reservation</h2>
                    <form id="editReservationForm">
                        <div class="form-group">
                            <label for="editCheckIn">Check-in:</label>
                            <input type="date" id="editCheckIn" name="checkIn" value="${new Date(reservation.checkInDate).toISOString().split('T')[0]}" required>
                        </div>
            
                        <div class="form-group">
                            <label for="editCheckOut">Check-out:</label>
                            <input type="date" id="editCheckOut" name="checkOut" value="${new Date(reservation.checkOutDate).toISOString().split('T')[0]}" required>
                        </div>
            
                        <label for="editRooms">Rooms:</label>
                        <div id="editRooms">${roomOptions}</div>
            
                        <label for="editAmenities">Amenities:</label>
                        <div id="editAmenities">${amenityOptions}</div>
            
                        <button type="submit">Save Changes</button>
                        <button type="button" id="cancelEdit">Cancel</button>
                    </form>
                </div>
            `;

            editFormContainer.style.display = 'block';

            document.getElementById('editReservationForm').addEventListener('submit', async (e) => {
                e.preventDefault();

                const updatedReservation = {
                    checkInDate: document.getElementById('editCheckIn').value,
                    checkOutDate: document.getElementById('editCheckOut').value,
                    rooms: Array.from(document.querySelectorAll('#editRooms input:checked')).map(input => ({ id: input.value })),
                    amenities: Array.from(document.querySelectorAll('#editAmenities input:checked')).map(input => ({ id: input.value })),
                };

                updateReservation(reservationId, updatedReservation);
                editFormContainer.style.display = 'none';
            });

            document.getElementById('cancelEdit').addEventListener('click', () => {
                editFormContainer.style.display = 'none';
            });
        } catch (error) {
            console.error('Failed to fetch data for the edit form:', error);
            alert('Failed to load reservation data. Please try again.');
        }
    };

    const updateReservation = async (reservationId, updatedReservation) => {
        try {
            await fetchData(`/admin/reservations/${reservationId}`, 'PUT', updatedReservation);
            alert('Reservation updated successfully!');
            fetchAllReservations();
        } catch (error) {
            console.error('Failed to update reservation:', error);
            alert('Failed to update reservation. Please try again.');
        }
    };

    const deleteReservation = async (reservationId) => {
        try {
            await fetchData(`/admin/reservations/${reservationId}`, 'DELETE');
            alert('Reservation deleted successfully!');
            fetchAllReservations();
        } catch (error) {
            console.error('Failed to delete reservation:', error);
            alert('Failed to delete reservation. Please try again.');
        }
    };

    const attachEventListeners = () => {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        const editButtons = document.querySelectorAll('.edit-btn');

        deleteButtons.forEach(button => {
            button.addEventListener('click', () => {
                const reservationId = button.closest('.reservation').dataset.id;
                if (confirm('Are you sure you want to delete this reservation?')) {
                    deleteReservation(reservationId);
                }
            });
        });

        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const reservationId = button.closest('.reservation').dataset.id;
                openEditForm(reservationId);
            });
        });
    };

    fetchAllReservations();
});
