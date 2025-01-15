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

            const amenityOptions = amenities.map(amenity => `
                <label>
                    <input type="checkbox" value="${amenity.id}" ${reservation.amenities.some(a => a.id === amenity.id) ? 'checked' : ''}>
                    ${amenity.name} - $${amenity.pricePerNight}
                </label><br>
            `).join('');

            const roomOptions = rooms.map(room => `
                <option value="${room.id}" ${reservation.rooms.some(r => r.id === room.id) ? 'selected' : ''}>
                    Room ID: ${room.id} - ${room.type.name}
                </option>
            `).join('');

            const editFormHTML = `
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
                        <select id="editRooms" name="editRooms" multiple required>
                            ${roomOptions}
                        </select>
            
                        <label for="editAmenities">Amenities:</label>
                        <div id="editAmenities">${amenityOptions}</div>
            
                        <button type="submit">Save Changes</button>
                        <button type="button" id="cancelEdit">Cancel</button>
                    </form>
                </div>
            `;

            editFormContainer.innerHTML = editFormHTML;
            editFormContainer.style.display = 'block';

            const editCheckIn = document.getElementById('editCheckIn');
            const editCheckOut = document.getElementById('editCheckOut');

            const validateDates = () => {
                const checkInDate = new Date(editCheckIn.value);
                const checkOutDate = new Date(editCheckOut.value);
                const today = new Date();
                today.setHours(0, 0, 0, 0);

                if (checkInDate && checkOutDate) {
                    if (checkInDate <= today) {
                        alert('Check-in date must be after today.');
                        editCheckIn.setCustomValidity('Check-in date must be after today.');
                    } else if (checkOutDate <= today) {
                        alert('Check-out date must be after today.');
                        editCheckOut.setCustomValidity('Check-out date must be after today.');
                    } else {
                        const diffInTime = checkOutDate - checkInDate;
                        const diffInDays = diffInTime / (1000 * 3600 * 24);

                        if (diffInDays < 1) {
                            alert('Check-out date must be at least 1 day after check-in date.');
                            editCheckOut.setCustomValidity('Check-out date must be at least 1 day after check-in date.');
                        } else if (diffInDays > 30) {
                            alert('Check-out date cannot be more than 30 days after check-in date.');
                            editCheckOut.setCustomValidity('Check-out date cannot be more than 30 days after check-in date.');
                        } else {
                            editCheckIn.setCustomValidity('');
                            editCheckOut.setCustomValidity('');
                        }
                    }
                }
            };

            const validateRooms = () => {
                const rooms = document.querySelectorAll('#editRooms option:checked');
                if (rooms.length === 0) {
                    alert('You must select at least 1 room.');
                    return false;
                }
                if (rooms.length > 10) {
                    alert('You can select a maximum of 10 rooms.');
                    return false;
                }
                return true;
            };

            document.getElementById('editReservationForm').addEventListener('submit', async (e) => {
                e.preventDefault();

                validateDates();
                const isValidRoom = validateRooms();
                if (editCheckIn.validity.valid && editCheckOut.validity.valid && isValidRoom) {
                    const updatedReservation = {
                        checkInDate: editCheckIn.value,
                        checkOutDate: editCheckOut.value,
                        rooms: Array.from(document.querySelectorAll('#editRooms option:checked')).map(option => ({ id: option.value })),
                        amenities: Array.from(document.querySelectorAll('#editAmenities input:checked')).map(input => ({ id: input.value })),
                    };

                    updateReservation(reservationId, updatedReservation);
                    editFormContainer.style.display = 'none';
                } else {
                    alert('Please fix the errors before submitting.');
                }
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
