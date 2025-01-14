import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reviewList = document.getElementById('reviewList');
    const editFormContainer = document.createElement('div'); // Container for the edit form
    editFormContainer.id = 'editFormContainer';
    editFormContainer.style.display = 'none'; // Initially hidden
    document.body.appendChild(editFormContainer);

    // Fetch all reviews
    const fetchAllReviews = async () => {
        try {
            const reviews = await fetchData('/user/review'); // Assuming this endpoint returns all reviews
            renderReviews(reviews);
        } catch (error) {
            console.error('Failed to fetch reviews:', error);
            alert('Failed to load reviews. Please try again.');
        }
    };

    // Render reviews with edit and delete options
    const renderReviews = (reviews) => {
        reviewList.innerHTML = reviews.map(review => `
            <div class="review" data-id="${review.id}">
                <p><strong>Rating:</strong> <span class="review-rating">${review.rating}</span>/5</p>
                <p><strong>Review:</strong> <span class="review-content">${review.content}</span></p>
                <button class="edit-btn">Edit</button>
                <button class="delete-btn">Delete</button>
            </div>
        `).join('');
        attachEventListeners();
    };

    // Delete review
    const deleteReview = async (id) => {
        try {
            await fetchData(`/admin/review/${id}`, 'DELETE');
            alert('Review deleted successfully!');
            fetchAllReviews(); // Refresh the reviews
        } catch (error) {
            console.error('Failed to delete review:', error);
            alert('Failed to delete review. Please try again.');
        }
    };

    // Open the edit form
    const openEditForm = (id, currentRating, currentContent) => {
        editFormContainer.innerHTML = `
            <div class="edit-form">
                <h2>Edit Review</h2>
                <form id="editReviewForm">
                    <label for="editRating">Rating (1-5):</label>
                    <input type="number" id="editRating" name="rating" min="1" max="5" value="${currentRating}" required>
                    <br>
                    <label for="editContent">Review:</label>
                    <textarea id="editContent" name="content" required>${currentContent}</textarea>
                    <br>
                    <button type="submit">Save Changes</button>
                    <button type="button" id="cancelEdit">Cancel</button>
                </form>
            </div>
        `;
        editFormContainer.style.display = 'block';

        // Handle form submission
        document.getElementById('editReviewForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            const updatedRating = document.getElementById('editRating').value;
            const updatedContent = document.getElementById('editContent').value;

            try {
                await fetchData(`/admin/review/${id}`, 'PUT', { rating: updatedRating, content: updatedContent });
                alert('Review updated successfully!');
                editFormContainer.style.display = 'none'; // Hide the form
                fetchAllReviews(); // Refresh the reviews
            } catch (error) {
                console.error('Failed to update review:', error);
                alert('Failed to update review. Please try again.');
            }
        });

        // Handle cancel action
        document.getElementById('cancelEdit').addEventListener('click', () => {
            editFormContainer.style.display = 'none'; // Hide the form
        });
    };

    // Attach event listeners for edit and delete buttons
    const attachEventListeners = () => {
        const deleteButtons = document.querySelectorAll('.delete-btn');
        const editButtons = document.querySelectorAll('.edit-btn');

        deleteButtons.forEach(button => {
            button.addEventListener('click', () => {
                const reviewId = button.closest('.review').dataset.id;
                if (confirm('Are you sure you want to delete this review?')) {
                    deleteReview(reviewId);
                }
            });
        });

        editButtons.forEach(button => {
            button.addEventListener('click', () => {
                const reviewElement = button.closest('.review');
                const reviewId = reviewElement.dataset.id;
                const currentRating = reviewElement.querySelector('.review-rating').textContent;
                const currentContent = reviewElement.querySelector('.review-content').textContent;

                openEditForm(reviewId, currentRating, currentContent);
            });
        });
    };

    // Load all reviews when the page loads
    fetchAllReviews();
});
