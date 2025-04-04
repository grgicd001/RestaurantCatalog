# Restaurant Catalog

A JavaFX desktop application for browsing, searching, and managing restaurant listings.


## Overview

Restaurant Catalog is a modern, user-friendly desktop application built with JavaFX that allows users to browse through restaurant listings, filter by cuisine type, search for specific restaurants, and add new restaurants to the catalog.

## Features

- **Clean and Modern UI**: Built with JavaFX for a responsive and attractive user interface
- **Restaurant Browsing**: View restaurant cards with images, names, cuisine types, and ratings
- **Category Filtering**: Filter restaurants by cuisine categories via the sidebar
- **Search Functionality**: Find restaurants by name or cuisine type
- **Add New Restaurants**: Easily add new restaurants to the catalog with a user-friendly form
- **Restaurant Details**: View additional information about each restaurant
- **Responsive Design**: UI elements adjust appropriately based on window size

## Getting Started

### Prerequisites

- Java JDK 11 or later
- JavaFX SDK 11 or later

### Running the Application

1. Clone or download this repository
2. Compile the application:
   ```
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml file/RestaurantCatalogUI.java
   ```
3. Run the application:
   ```
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml file.RestaurantCatalogUI
   ```

### Application Structure

- `RestaurantCatalogUI.java`: Main application class containing the UI implementation
- `Restaurant` (inner class): Model class for restaurant data

## Usage Guide

### Browsing Restaurants

The main view displays restaurant cards in a grid layout. Each card includes:
- Restaurant image
- Restaurant name
- Cuisine type
- Star rating
- "Order Now" and "View Details" buttons

### Filtering by Category

Use the sidebar on the left to filter restaurants by cuisine type:
- Click "All Restaurants" to show all listings
- Click on a specific cuisine category to filter the display

### Searching

The search bar at the top allows you to find restaurants by name or cuisine:
1. Enter text in the search field
2. Click the "Search" button or press Enter
3. Results will be filtered to match your search query

### Adding a New Restaurant

To add a new restaurant to the catalog:
1. Click the "Add Restaurant" button in the top navigation bar
2. Fill in the restaurant details in the form:
    - Restaurant name
    - Cuisine type (select from existing or enter a new one)
    - Image URL (a placeholder is provided by default)
    - Rating using the slider
3. Click "Add Restaurant" to save
4. A confirmation will appear when the restaurant is added

### Viewing Restaurant Details

Click the "View Details" button on any restaurant card to see additional information.

## Customization

### Default Cuisine Categories

The application comes pre-loaded with the following cuisine categories:
- Fast Food
- Italian
- Chinese
- Japanese
- Mexican
- Indian
- Dessert
- Healthy

New categories are automatically added when a restaurant with a new cuisine type is created.

### Default Restaurants

The application is pre-loaded with 8 sample restaurants across different cuisine categories.

## Implementation Details

- **JavaFX Components**: Uses BorderPane, GridPane, ScrollPane, VBox, HBox for layout
- **UI Styling**: CSS-style properties for modern appearance
- **Event Handling**: Lambda expressions for button actions and other events
- **Data Structure**: ArrayList for storing restaurant data
- **Dynamic UI**: UI elements are created and updated programmatically

## Future Enhancements

Potential features for future versions:
- Database integration for persistent storage
- User accounts and favorites
- Restaurant reviews and comments
- Map integration for location-based search
- Online ordering integration



## Credits

Developed by the restaurant catalog team