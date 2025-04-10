# Restaurant Catalog

> **Note:** This document is finalized and may be watermarked for academic submission purposes.

Restaurant Catalog is a Java-based desktop application that allows users to browse, search, and manage restaurants. It supports full user authentication, favorites management, CSV-based data persistence, and administrative CRUD operations. The project follows modern software engineering practices including modular design, structured documentation, and multi-level testing (unit, integration, system).

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)
- [Testing](#testing)
- [Deployment](#deployment)
- [Documentation](#documentation)
- [Contributions](#contributions)
- [License](#license)
- [Acknowledgements](#acknowledgements)

---

## 🧠 Overview

Restaurant Catalog was developed by a team of software engineering students as a final project for their course. The app features:

- User login, registration, and session management
- Favorite restaurants list (with duplicate prevention)
- Search and filtering of restaurants
- Admin privileges for editing/adding/removing restaurants
- Ratings and image handling
- Local CSV-based data storage

The system architecture allows for modular testing and clean separation of concerns, enhancing maintainability.

---

## 🚀 Features

### ✅ User Management
- Register and log in
- Role-based access control (admin vs. normal user)
- Add/remove favorite restaurants (no duplicates)
- Secure CSV parsing for user data

### 🍽️ Restaurant Catalog
- Search/filter restaurants by name or other criteria
- Add, edit, delete restaurants (admin only)
- Rating enforcement between 0.5 and 5.0
- Load/save restaurant data via CSV
- Upload and display restaurant images

### 🔐 Authentication
- Password validation (length, uppercase, digits)
- Login and logout functionality
- Session state control

### 💾 Persistence
- Reads/writes from/to CSV files
- Temporary files used during tests to ensure isolation

---

## 💻 Installation

### Prerequisites

- **Java 8 or higher**
- **Git** to clone the repository
- **Maven** (or your preferred Java IDE like IntelliJ IDEA or Eclipse)

### Clone & Build

```bash
git clone https://github.com/grgicd001/RestaurantCatalog.git
cd RestaurantCatalog
mvn clean install
