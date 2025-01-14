import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reservationAmenitiesList = document.getElementById('reservationAmenitiesList');
    const newAmenityForm = document.getElementById('newAmenityForm');
    const editFormContainer = document.createElement('div');
    editFormContainer.id = 'editFormContainer';
    editFormContainer.style.display = 'none';
    document.body.appendChild(editFormContainer);

    const fetchAllReservationAmenities = async () => {
        try {
            const reservationAmenities = await fetchData('/user/reservation-amenities');
            renderReservationAmenities(reservationAmenities);
        } catch (error) {
            console.error('Failed to fetch reservation amenities:', error);
            alert('Failed to load reservation amenities. Please try again.');
        }
    };

    const renderReservationAmenities = (reservationAmenities) => {
        reservationAmenitiesList.innerHTML = reservationAmenities.map(reservationAmenity => `
            <div class="reservation-amenity" data-id="${reservationAmenity.id}">
                <p><strong>Name:</strong> <span class="reservation-amenity-name">${reservationAmenity.name}</span></p>
                <p><strong>Price Per Night:</strong> <span class="reservation-amenity-pricePerNight">${reservationAmenity.pricePerNight}$</span></p>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `).join('');
        attachEventListeners();
    };

    const addReservationAmenity = async (newAmenity) => {
        try {
            await fetchData('/admin/reservation-amenities', 'POST', newAmenity);
            alert('New reservation amenity added successfully!');
            fetchAllReservationAmenities();
        } catch (error) {
            console.error('Failed to add reservation amenity:', error);
            alert('Failed to add reservation amenity. Please try again.');
        }
    };

    const deleteReservationAmenity = async (id) => {
        try {
            await fetchData(`/admin/reservation-amenities/${id}`, 'DELETE');
            alert('Reservation amenity deleted successfully!');
            fetchAllReservationAmenities();
        } catch (error) {
            console.error('Failed to delete reservation amenity:', error);
            alert('Failed to delete reservation amenity. Please try again.');
        }
    };

    const openEditForm = (id, currentName, currentPricePerNight) => {
        editFormContainer.innerHTML = `
            <div class="edit-form">
                <h2>Edit Reservation Amenity</h2>
                <form id="editReservationAmenitiesForm">
                    <label for="editName">Name:</label>
                    <textarea id="editName" name="name" required>${currentName}</textarea>
                    <br>
                    <label for="editPricePerNight">Price Per Night:</label>
                    <input type="number" id="editPricePerNight" name="pricePerNight" min="0" step="0.01" value="${parseFloat(currentPricePerNight).toFixed(2)}" required>
                    <br>
                    <button type="submit">Save Changes</button>
                    <button type="button" id="cancelEdit">Cancel</button>
                </form>
            </div>
        `;
        editFormContainer.style.display = 'block';

        document.getElementById('editReservationAmenitiesForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedName = document.getElementById('editName').value;
            const updatedPricePerNight = document.getElementById('editPricePerNight').value;

            try {
                await fetchData(`/admin/reservation-amenities/${id}`, 'PUT', { name: updatedName, pricePerNight: updatedPricePerNight });
                alert('Reservation amenity updated successfully!');
                editFormContainer.style.display = 'none';
                fetchAllReservationAmenities();
            } catch (error) {
                console.error('Failed to update reservation amenity:', error);
                alert('Failed to update reservation amenity. Please try again.');
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
                const reservationAmenityId = button.closest('.reservation-amenity').dataset.id;
                if (confirm('Are you sure you want to delete this reservation amenity?')) {
                    deleteReservationAmenity(reservationAmenityId);
                }
            });
        });

        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const reservationAmenityElement = button.closest('.reservation-amenity');
                const reservationAmenityId = reservationAmenityElement.dataset.id;
                const currentName = reservationAmenityElement.querySelector('.reservation-amenity-name').textContent;
                const currentPricePerNight = reservationAmenityElement.querySelector('.reservation-amenity-pricePerNight').textContent.replace('$', '');

                openEditForm(reservationAmenityId, currentName, currentPricePerNight);
            });
        });
    };

    newAmenityForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const newName = document.getElementById('newAmenityName').value;
        const newPricePerNight = document.getElementById('newAmenityPricePerNight').value;

        addReservationAmenity({ name: newName, pricePerNight: newPricePerNight });
    });

    fetchAllReservationAmenities();
});
