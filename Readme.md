# Restaurant Catalog

> **Note:** This document may be watermarked for submission verification purposes.

Restaurant Catalog is a fully featured application designed to help users browse, rate, and manage information about their favorite restaurants. The application supports user registration, login, favorites management, CSV data import/export, image handling, and robust testing practices. It has been built following industry-standard software development practices, ensuring a reliable user experience and easy maintainability.

---

## Table of Contents

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

## Overview

The Restaurant Catalog project allows users to:
- Browse a catalog of restaurants.
- Search and filter restaurants based on various criteria.
- Rate restaurants, ensuring that ratings are within acceptable boundaries.
- Manage favorite restaurants without duplicates.
- View and manage restaurant images.

The system uses CSV files for data persistence, which makes it easy to import and export both user and restaurant data. Comprehensive unit, integration, and system tests have been developed using JUnit to ensure proper functionality.

---

## Features

- **User Management**
  - Registration and login functionality.
  - Role-based access control for administering the catalog.
  - Favorites management: add/remove favorite restaurants with duplicate prevention.
  - Robust CSV parsing for user data.

- **Restaurant Operations**
  - Adding, editing, and deleting restaurant entries.
  - Rating validations to ensure ratings fall within the valid range.
  - Image handling: uploading and managing restaurant images.

- **Authentication & Authorization**
  - Secure user authentication and session management.
  - Password validation enforcing complexity requirements.

- **User Interface**
  - Search and filtering capabilities for efficient browsing.
  - Responsive design to facilitate a smooth user experience.

- **Testing**
  - A full suite of unit, integration, and system tests.
  - Automated test execution via JUnit with detailed results reported in our test plan.

---

## Installation

### Prerequisites

- **Java Development Kit (JDK):** Version 8 or higher.
- **Build Tool:** Maven or Gradle (depending on your project configuration).
- **Git:** For cloning the repository.

### Steps to Install

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/grgicd001/RestaurantCatalog.git
   cd RestaurantCatalog
   ```

2. **Build the Project:**

   If using Maven:
   ```bash
   mvn clean install
   ```

   If using Gradle:
   ```bash
   gradle build
   ```

3. **Run the Application:**

   If a packaged executable (JAR) is provided:
   ```bash
   java -jar target/RestaurantCatalog.jar
   ```

   Alternatively, open the project in your preferred IDE and run the main application class.

---

## Usage

Once the project is built and running, users can:

1. **Register / Log in:**
   - Create a new account or log in using existing credentials.
   - Authentication ensures only authorized users can modify data.

2. **Browse the Restaurant Catalog:**
   - Use the search and filtering options to view restaurants that meet your criteria.
  
3. **Manage Favorites and Restaurant Data:**
   - Add restaurants to your favorites list (duplicates are automatically prevented).
   - Admin users can add, edit, and delete restaurant entries.

4. **Image Handling:**
   - Upload images for restaurants. The system validates image files and ensures correct file handling.

Detailed instructions for each feature are provided within the in-application help or user guide section of the project documentation.

---

## Testing

The project includes comprehensive tests to ensure high quality and reliability:

- **Unit Tests:** Validate individual components such as CSV parsing and favorite management.
- **Integration Tests:** Test interactions between components like user registration with authentication.
- **System Tests:** Simulate end-to-end workflows, from browsing to final user registration and logout.

Run the tests using:
```bash
mvn test
```
or
```bash
gradle test
```
Detailed test execution results and methodologies can be found in the `Restaurant Catalog - Phase 3_ Test Plan.pdf` document within the repository.

---

## Deployment

The application is packaged to either run as a standalone executable or be deployed online if it is web-based. For more information:

- **Deployed Version:** [Insert live deployment URL here, if applicable]
- **Packaged Executable:** Follow the installation instructions above to run the JAR file locally.

For any deployment constraints or additional details, please refer to the documentation in the repository.

---

## Documentation

- **User Documentation:**  
  This README along with in-application help provides users with the necessary instructions for installation and usage.
  
- **Developer Documentation:**  
  Refer to `DEVELOPER.md` for detailed documentation on the project architecture, coding guidelines, and setup procedures.

- **Testing Documentation:**  
  For detailed test plans and execution results, see the provided `Restaurant Catalog - Phase 3_ Test Plan.pdf`.

- **Presentation and Project Retrospective:**  
  Check out the Lab 11 Presentation document for insights into our development process and retrospective analysis.

---

## Contributions

The project is a collaborative effort. Key contributions include:

- **Daniel Grgic:** Core functionality including authentication and rating validations.
- **Zawwar Rizvi:** Implemented image handling and restaurant data management.
- **Vithuran Kankatharan:** Deployment setup and infrastructure.
- **Arshia Pakdaman:** Enhanced CSV parsing and password validation.
- **Zisong Zhou:** Led testing frameworks and conducted end-to-end system tests.

For a complete breakdown of individual contributions, please refer to our contribution report generated from GitHub version control metrics.

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Acknowledgements

- Special thanks to our instructor and peers for guidance and feedback.
- Icon and presentation templates sourced from Slidesgo, Flaticon, and Freepik.

---

> **End of README**  
> _This document was generated to fulfill final project submission requirements and may include a watermark in its final PDF format._
