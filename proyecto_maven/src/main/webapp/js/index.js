document.addEventListener("DOMContentLoaded", function () {
    const description = document.getElementById("company-description");

    function revealOnScroll() {
        const rect = description.getBoundingClientRect();
        if (rect.top < window.innerHeight * 0.75) {
            description.classList.add("visible");
        }
    }

    window.addEventListener("scroll", revealOnScroll);
});