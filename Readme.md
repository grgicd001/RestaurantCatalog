# Restaurant Catalog

A fully functional JavaFX desktop application for Browse, searching, and managing restaurant listings. Developed as part of a software engineering project, this application demonstrates modern GUI development, file I/O, modular design, and robust testing.

---

## 🚀 Overview

Restaurant Catalog is a desktop application built with JavaFX that enables users to:

- Browse a catalog of restaurants with images, details, and ratings
- Filter restaurants by cuisine
- Search by name or cuisine
- Add new restaurant listings with validation
- Manage admin-level actions (e.g., editing and deleting entries)
- Simulate user login and registration workflows
- Run tests to verify functionality using JUnit

---

## 🎯 Features

- **Modern JavaFX UI**: Responsive layout and intuitive navigation
- **Restaurant Browse**: Grid layout with images, star ratings, and filters
- **Search & Filter**: Quickly narrow down restaurants using search terms or cuisine types
- **Add/Edit Restaurants**: Add new restaurants or edit existing ones with image and rating validation
- **Image Upload**: Add a local `.png` file as a restaurant image
- **User Authentication**: Basic login and registration with password validation
- **Favorites**: Save and persist a list of favorite restaurants
- **Data Persistence**: Saves and loads from CSV files (users, restaurants, favorites)

---

## 🧰 Getting Started

### ✅ Prerequisites

- Java JDK 11 or higher
- JavaFX SDK 11 or higher
- IDE such as IntelliJ IDEA or Eclipse (or compile via terminal)

### ▶️ Running the Application

1.  **Clone the repository**:
    ```bash
    git clone [https://github.com/grgicd001/RestaurantCatalog.git](https://github.com/grgicd001/RestaurantCatalog.git)
    cd RestaurantCatalog
    ```
2.  **Compile the project**:
    ```bash
    javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml file/RestaurantCatalogUI.java
    ```
3.  **Run the project**:
    ```bash
    java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml file.RestaurantCatalogUI
    ```
    💡 Replace `/path/to/javafx-sdk/lib` with the actual path to your JavaFX installation.

### 📂 Project Structure

/file
├── RestaurantCatalogUI.java     # Main JavaFX UI
├── User.java                  # User model and logic
├── Restaurant.java              # Restaurant data model
├── FavoriteManager.java       # Logic for saving/loading favorites
├── AuthManager.java             # Login & registration logic
├── CSVUtils.java              # Utility for reading/writing CSV files
└── tests/                     # Contains all JUnit test files


### 🧪 Testing

This project includes a comprehensive test suite using JUnit 5. We cover:

-   Unit Tests (logic, validation, parsing)
-   Integration Tests (e.g., login + CSV)
-   System Tests (e.g., full user journey)

**🧼 Run Tests (IDE or CLI)**

1.  Open the `tests/` folder in your IDE and run individual test classes.
2.  Or, run using command-line:
    ```bash
    # Compile tests (adjust classpath if needed)
    javac -cp .:/path/to/junit-platform-console-standalone.jar tests/*.java

    # Run tests using JUnit Platform Console Standalone JAR
    java -jar /path/to/junit-platform-console-standalone.jar --class-path . --scan-class-path
    ```
    *Replace `/path/to/junit-platform-console-standalone.jar` with the actual path to the JUnit JAR.*

Test results are logged automatically to the console, and any failed tests will display stack traces for debugging.

---

## 📘 User Guide

### 🌐 Browse & Filtering

-   Restaurants are displayed in a grid with names, cuisines, images, and ratings.
-   Filter by clicking a category in the sidebar.
-   Use the search bar to find a restaurant by name or cuisine.

### ➕ Adding Restaurants

-   Click `Add Restaurant`.
-   Fill in name, cuisine, image path (`.png`), and rating.
-   Click `Save`.
-   The restaurant is added and persisted to the CSV file.

### 👤 Authentication

-   Use the `Register`/`Login` menu to create or access user accounts.
-   Passwords require at least one uppercase letter and one number.

### 💖 Favorites

-   Registered users can add restaurants to their favorites list.
-   Favorite data is saved between sessions in a separate CSV file.

### 🔐 Admin Features

Admin users have additional privileges:

-   Edit or delete any restaurant listing.
-   View extra admin tools (if implemented).
-   Admin status is set in the `users.csv` data or granted during user creation/management.

---

## 🌍 Deployment

While this is a desktop application, a packaged `.jar` file could be created for easy execution (instructions may vary based on build tools). Please see the Releases section on GitHub (if available) or compile and run as described in the "Getting Started" section.

---

## 👨‍💻 Contributors

This project was developed by the Restaurant Catalog Team as part of a group software engineering course:

-   Daniel Grgic
-   Vithuran Kankatharan
-   Zawwar Rizvi
-   Arshia Pakdaman
-   Zisong Zhou

---

## 📈 Contribution & Testing Reports

-   All code contributions are tracked via GitHub commits, issues, and pull requests.
-   Testing results are documented in the Phase 3 Test Plan and final test execution report.
-   Contribution breakdown and demo are included in our final course submission.

---

## 🏁 Future Enhancements

-   Real database integration (e.g., PostgreSQL or SQLite).
-   Web version of the application (e.g., using React + Spring Boot or Flask).
-   User reviews and comments feature.
-   OAuth-based login (e.g., Google, Facebook).
-   Mobile version or Progressive Web App (PWA).

---

## 📄 License

MIT License. See the `LICENSE` file for details.

---

## 🙌 Acknowledgments

-   UI inspired by common restaurant listing applications.
-   JavaFX documentation and community tutorials.
-   Special thanks to our course instructor and teaching assistants for guidance.
