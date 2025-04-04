# Restaurant Catalog UI - Developer Documentation

## Overview
This JavaFX application provides a restaurant catalog system with user authentication, restaurant management, and browsing capabilities. The application features:
- User login/signup system
- Restaurant browsing with filtering
- Admin functions (add/edit/delete restaurants)
- Favorite restaurants tracking
- Detailed restaurant views

## Main Classes

### RestaurantCatalogUI
The main application class extending `javafx.application.Application`.

#### Nested Classes
- **Restaurant**: Represents a restaurant with all its attributes
- **User**: Handles user data and authentication

## Key Methods

### Core Application Methods

#### `start(Stage primaryStage)`
- **Purpose**: Entry point for the JavaFX application
- **Functionality**:
    - Creates and shows the login screen
    - Handles user authentication
    - Transitions to main UI after successful login
- **Components**:
    - Username/password fields
    - Login, Sign Up, and Guest buttons

#### `showMainUI(Stage primaryStage)`
- **Purpose**: Initializes and displays the main application interface
- **Functionality**:
    - Sets up directory structure for images
    - Loads restaurant data
    - Creates the main UI layout with navigation, sidebar, and content area

### UI Creation Methods

#### `createTopNavigation()`
- **Purpose**: Creates the top navigation bar
- **Components**:
    - Application logo
    - Search functionality
    - User/admin controls
    - Filter controls
- **Features**:
    - Search restaurants by name/cuisine
    - Admin add restaurant button
    - Logout functionality

#### `createCategorySidebar()`
- **Purpose**: Creates the left sidebar with category filters
- **Components**:
    - All restaurants view
    - Favorites section
    - Cuisine type filters
- **Behavior**:
    - Updates main content when categories are selected

#### `createRestaurantGrid()`
- **Purpose**: Creates the scrollable main content area
- **Functionality**:
    - Displays restaurants in a responsive grid
    - Handles empty state when no restaurants match filters

#### `createRestaurantCard(Restaurant restaurant)`
- **Purpose**: Creates an individual restaurant card
- **Components**:
    - Restaurant image
    - Name and cuisine type
    - Rating display
    - Action buttons (View Details, Add to Favorites)
    - Admin controls (Update, Delete) for admin users
- **Behavior**:
    - Interactive buttons for details and favorites
    - Visual styling with shadows and rounded corners

### Restaurant Management Methods

#### `openAddRestaurantDialog()`
- **Purpose**: Shows dialog for adding new restaurants (admin only)
- **Fields**:
    - Name, cuisine type, rating
    - Description, tags
    - Cover image upload
    - Additional images upload
    - Menu items input
- **Validation**:
    - Ensures valid rating (1-5)
    - Handles image uploads

#### `openUpdateRestaurantDialog(Restaurant restaurant)`
- **Purpose**: Shows dialog for editing existing restaurants
- **Behavior**:
    - Pre-fills with current restaurant data
    - Allows partial updates (preserves unchanged fields)
    - Same validation as add dialog

#### `showRestaurantDetails(Restaurant restaurant)`
- **Purpose**: Displays detailed view of a restaurant
- **Components**:
    - Large cover image
    - Full description
    - Tags display
    - Additional images gallery
    - Menu items list
    - Back button

### Data Management Methods

#### `getRestaurants(String filePath)`
- **Purpose**: Loads restaurant data from CSV file
- **Format**: Pipe-delimited with sections for:
    - Basic info (name, cuisine, image, rating)
    - Description
    - Tags (semicolon-delimited)
    - Additional images (semicolon-delimited)
    - Menu items (semicolon-delimited)

#### `saveRestaurants(String fileName)`
- **Purpose**: Saves current restaurant list to CSV
- **Behavior**:
    - Overwrites existing file
    - Maintains same pipe-delimited format

#### `addImage(File img, String id, boolean cover)`
- **Purpose**: Handles image uploads for restaurants
- **Functionality**:
    - Creates `images` directory if needed
    - Copies uploaded file with unique ID
    - Stores in application's image directory

### Filtering/Search Methods

#### `filterRestaurants(String query)`
- **Purpose**: Filters restaurants by search query
- **Matching**: Checks against:
    - Restaurant name
    - Cuisine type
- **Behavior**: Updates grid display

#### `applyFilter()`
- **Purpose**: Applies complex filters from UI
- **Filter Types**:
    - By cuisine
    - By tags
    - Combination of both
- **Logic Options**:
    - Match any tag
    - Match all tags
- **Behavior**: Updates grid display

#### `displayRestaurants(List<Restaurant> restaurants)`
- **Purpose**: Main display method for restaurant lists
- **Behavior**:
    - Clears current grid
    - Creates cards for each restaurant
    - Handles empty state
    - Responsive 2-column layout

### User Management Methods

#### `displayFavorites()`
- **Purpose**: Shows current user's favorite restaurants
- **Requirements**: User must be logged in
- **Behavior**:
    - Filters restaurant list to favorites
    - Shows warning if not logged in

## Data Structures

### Restaurant Class
- **Attributes**:
    - `name`: String - Restaurant name
    - `cuisine`: String - Primary cuisine type
    - `imageUrl`: String - Path/URL to cover image
    - `rating`: double - Average rating (1-5)
    - `description`: String - Detailed description
    - `tags`: List<String> - Categorization tags
    - `additionalImages`: List<String> - More restaurant images
    - `menuItems`: List<String> - Offered menu items

### User Class
- **Attributes**:
    - `username`: String
    - `password`: String
    - `isAdmin`: boolean - Admin privileges flag
    - `favoriteRestaurants`: List<String> - Names of favorited restaurants
- **Methods**:
    - CSV serialization/deserialization
    - Favorite management

## File Structure
- `restaurants.csv`: Main data store for restaurants
- `users.csv`: User account data
- `images/`: Directory for uploaded restaurant images

## Usage Patterns

### Admin Flow
1. Login as admin
2. Use "Add Restaurant" button
3. Fill form and submit
4. Edit/delete via card buttons

### User Flow
1. Login or continue as guest
2. Browse restaurants
3. Filter by category/search
4. View details
5. Add favorites (if logged in)

### Guest Flow
1. Select "View as Guest"
2. Browse restaurants (no favorites)
3. All other features available