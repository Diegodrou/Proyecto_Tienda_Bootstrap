# 🎵 Vinyl Store Web Application

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge\&logo=openjdk\&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge\&logo=bootstrap\&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge\&logo=mariadb\&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge\&logo=apachemaven\&logoColor=white)
![Tomcat](https://img.shields.io/badge/Tomcat-F8DC75?style=for-the-badge\&logo=apachetomcat\&logoColor=black)

A full-stack web application designed to simulate an **online vinyl record store**.
It includes both a **customer-facing website** and an **administration panel** for managing content and users.

---

## 🧩 Overview

This project was developed as part of a web development course focused on building a complete e-commerce system from scratch.

### 🎧 Main Website

* Browse vinyl records by genre or artist
* Add items to a shopping cart
* View company information and contact details
* User login, registration, and order history

### 🛠️ Admin Panel

* Manage users (activation, modification, deletion)
* Add, edit, and remove vinyl records from the catalog
* View and manage customer orders
* Link to that project's GitHub:

---

## ⚙️ Tech Stack

| Layer                     | Technologies                               |
| ------------------------- | ------------------------------------------ |
| **Frontend**              | Bootstrap 5, HTML5, CSS3, JavaScript       |
| **Backend (Main Site)**   | Java (Data Modeling), Maven, Apache Tomcat |
| **Backend (Admin Panel)** | PHP                                        |
| **Database**              | MariaDB                                    |
| **IDE / Tools**           | Visual Studio Code                         |

---

## 🚀 Installation & Setup (Main site)

### 1. Prerequisites

Before running the project, make sure you have installed:

* ☕ **JDK 17+**
* 🐱 **Apache Tomcat 10.1.x**
* ⚙️ **Maven 3.9+**
* 🐬 **MariaDB Server**
* 💻 **Visual Studio Code** (recommended extensions: *Community Server Connectors*, *PHP Server*, *MySQL/MariaDB*)

### 2. Database Setup

1. Create a MariaDB database (e.g., `vinylstore_db`).
2. Import the SQL schema if provided.
3. Update the connection credentials in the Java "AccesoDB.java"file.

### 3. Running the Applications

* **Main Website (Java + Tomcat)**
  ```bash
  mvn clean package
  # Deploy to Tomcat
  http://localhost:8080/proyecto_maven (find the the url in the pom.xml file)
  ```
---

## 🎨 Features

* ✅ Fully responsive design with **Bootstrap 5**
* ✅ Carousel, product galleries, and dynamic navigation
* ✅ Contact form
* ✅ Product catalog with “Add to Cart” feature
* ✅ Secure login and registration system

---

## 🖼️ Project Screenshots

### 🏠 Home Page

![Home Page gif](/demofiles/home.gif)

### 💿 Product Catalog

![Product Catalog Screenshot](/demofiles/catalog.gif)

### 🛒 Shopping Cart

![Shopping Cart Screenshot](/demofiles/Car.gif)

---
⚠️ Media Assets Notice

Please note: For copyright reasons, the following media assets are not included in this repository:

    Album cover images used in the product catalog

    Homepage background video featured on the main site

These proprietary assets have been removed to respect intellectual property rights. You will need to provide your own images and videos for these elements when running the application locally.

---

## 🧠 Learnings

This project demonstrates how to integrate **frontend design**, **Java-based backend modeling**, and **database connectivity** into a functional e-commerce system.
It highlights the interaction between a **Java web server (Tomcat)**, a **PHP admin interface**, and a **MariaDB relational database**.

---

## 📚 References

* [Bootstrap Documentation](https://getbootstrap.com/docs/5.3/getting-started/download/)
* [MariaDB Foundation](https://mariadb.org/)
* [Apache Tomcat](https://tomcat.apache.org/)
* [PHP Official Site](https://www.php.net/)
* [Visual Studio Code](https://code.visualstudio.com/)
