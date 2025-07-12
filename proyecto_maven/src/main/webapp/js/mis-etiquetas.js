class Cabecera extends HTMLElement {
    constructor() {
        super()
        this.innerHTML = `<header><h1>Logo Tienda - Nombre Tienda Virtual</h1></header>`
    }
}
window.customElements.define('mi-cabecera', Cabecera);

class Pie extends HTMLElement {
    constructor() {
        super()
        this.loadHTML();
    }

    async loadHTML() {
        try {
            const response = await fetch("mis-tags/pie.html"); // Change the path as needed
            if (!response.ok) throw new Error("Failed to load menu.");
            this.innerHTML = await response.text();
        } catch (error) {
            console.error(error);
            this.innerHTML = "<p>Error loading menu.</p>";
        }
    }


}
window.customElements.define('mi-pie', Pie);

class Menu extends HTMLElement {
    constructor() {
        super()
        this.loadHTML();
    }

    async loadHTML() {
        try {
            const response = await fetch("mis-tags/menu.html"); // Change the path as needed
            if (!response.ok) throw new Error("Failed to load menu.");
            this.innerHTML = await response.text();
        } catch (error) {
            console.error(error);
            this.innerHTML = "<p>Error loading menu.</p>";
        }
    }
}
window.customElements.define('mi-menu', Menu);
