import { fetchData } from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
    const reviewList = document.getElementById('reviewList');
    const reviewForm = document.getElementById('reviewForm');

    // Pobierz i wyświetl recenzje
    const fetchReviews = async () => {
        try {
            const reviews = await fetchData('/user/review');
            reviewList.innerHTML = reviews.map(review => `
                <div class="review">
                    <p><strong>Rating:</strong> ${review.rating}/5</p>
                    <p><strong>Review:</strong> ${review.content}</p>
                    <p><strong>Date:</strong> ${new Date(review.date).toLocaleDateString()}</p>
                </div>
            `).join('');
        } catch (error) {
            console.error('Failed to fetch reviews:', error);
        }
    };

    // Dodaj nową recenzję
    reviewForm?.addEventListener('submit', async (e) => {
        e.preventDefault();
        const rating = document.getElementById('rating').value;
        const content = document.getElementById('content').value;

        try {
            await fetchData('/user/review', 'POST', { rating, content });
            alert('Review submitted successfully!');
            fetchReviews(); // Odśwież listę recenzji
        } catch (error) {
            console.error('Failed to submit review:', error);
            alert('Failed to submit review. Please try again.');
        }
    });

    // Załaduj recenzje przy otwarciu strony
    fetchReviews();
});