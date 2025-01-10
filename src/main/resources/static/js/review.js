import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reviewList = document.getElementById('reviewList');
    const reviewForm = document.getElementById('reviewForm');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    let currentPage = 0;  // Start on the first page
    const reviewsPerPage = 5;  // Show 5 reviews per page

    // Fetch reviews from the server with pagination
    const fetchReviews = async () => {
        try {
            const reviews = await fetchData(`/user/reviewPage?page=${currentPage}&limit=${reviewsPerPage}`);
            reviewList.innerHTML = reviews.map(review => `
                <div class="review">
                    <p><strong>Rating:</strong> ${review.rating}/5</p>
                    <p><strong>Review:</strong> ${review.content}</p>
                    <p><strong>Date:</strong> ${new Date(review.date).toLocaleDateString()}</p>
                </div>
            `).join('');

            // Enable or disable pagination buttons
            prevBtn.disabled = currentPage === 0;
            nextBtn.disabled = reviews.length < reviewsPerPage; // Disable next button if fewer than 5 reviews are returned
        } catch (error) {
            console.error('Failed to fetch reviews:', error);
        }
    };

    // Add new review
    reviewForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const rating = document.getElementById('rating').value;
        const content = document.getElementById('content').value;

        try {
            await fetchData('/user/review', 'POST', { rating, content });
            alert('Review submitted successfully!');
            fetchReviews(); // Refresh the review list
        } catch (error) {
            console.error('Failed to submit review:', error);
            alert('Failed to submit review. Please try again.');
        }
    });

    // Load the reviews when the page is first loaded
    fetchReviews();

    // Previous button functionality
    prevBtn?.addEventListener('click', () => {
        if (currentPage > 1) {
            currentPage--;
            fetchReviews();
        }
    });

    // Next button functionality
    nextBtn?.addEventListener('click', () => {
        currentPage++;
        fetchReviews();
    });
});
