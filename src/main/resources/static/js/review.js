import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reviewList = document.getElementById('reviewList');
    const reviewForm = document.getElementById('reviewForm');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    let currentPage = 0;
    const reviewsPerPage = 5;

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

            prevBtn.disabled = currentPage === 0;
            nextBtn.disabled = reviews.length < reviewsPerPage;
        } catch (error) {
            console.error('Failed to fetch reviews:', error);
        }
    };

    reviewForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const rating = document.getElementById('rating').value;
        const content = document.getElementById('content').value;

        try {
            await fetchData('/user/review', 'POST', { rating, content });
            alert('Review submitted successfully!');
            fetchReviews();
        } catch (error) {
            console.error('Failed to submit review:', error);
            alert('Failed to submit review. Please try again.');
        }
    });

    fetchReviews();

    prevBtn?.addEventListener('click', () => {
        if (currentPage > 0) {
            currentPage--;
            fetchReviews();
        }
    });

    nextBtn?.addEventListener('click', () => {
        currentPage++;
        fetchReviews();
    });
});
