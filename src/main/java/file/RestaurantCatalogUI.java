package file;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RestaurantCatalogUI extends Application {

    public static class Restaurant {
        public String name;
        public String cuisine;
        public String imageUrl;
        public double rating;
        public String description;
        public List<String> tags;
        public List<String> additionalImages;
        public List<String> menuItems;

        public Restaurant(String name, String cuisine, String imageUrl, double rating, String description,
                List<String> tags, List<String> additionalImages, List<String> menuItems) {
            this.name = name;
            this.cuisine = cuisine;
            this.imageUrl = imageUrl;
            this.rating = rating;
            this.description = description;
            this.tags = tags;
            this.additionalImages = additionalImages;
            this.menuItems = menuItems;
        }

        public Restaurant(String name, String cuisine, String imageUrl, double rating) {
            this(name, cuisine, imageUrl, rating, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }

    public List<User> users = User.loadUsers("users.csv");
    public User currentUser;
    private boolean isAdmin;
    public GridPane gridPane;
    public List<Restaurant> allRestaurants;

    @Override
    public void start(Stage primaryStage) {
        Stage loginStage = new Stage();
        loginStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setStyle("-fx-background-color: #ffffff;");

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        userField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8;");

        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        passField.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 8;");

        Button loginButton = new Button("Login");
        loginButton.setStyle(
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10 20;");

        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #007BFF; -fx-border-color: #007BFF; -fx-border-radius: 5; -fx-padding: 10 20;");

        Button guestButton = new Button("View as Guest");
        guestButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #6C757D; -fx-border-color: #6C757D; -fx-border-radius: 5; -fx-padding: 10 20;");

        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(signUpButton, 1, 3);
        grid.add(guestButton, 1, 4);

        loginButton.setOnAction(e -> {
            String username = userField.getText();
            String password = passField.getText();

            boolean isValid = false;
            for (User user : users) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = user;
                    isAdmin = user.isAdmin();
                    isValid = true;
                    break;
                }
            }

            if (isValid) {
                loginStage.close();
                showMainUI(primaryStage);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText("Invalid username or password");
                alert.setContentText("Please try again.");
                alert.show();
            }
        });

        signUpButton.setOnAction(e -> {
            TextInputDialog usernameDialog = new TextInputDialog();
            usernameDialog.setTitle("Sign Up");
            usernameDialog.setHeaderText("Create a new account");
            usernameDialog.setContentText("Username:");

            Optional<String> usernameResult = usernameDialog.showAndWait();
            usernameResult.ifPresent(username -> {
                Dialog<String> passwordDialog = new Dialog<>();
                passwordDialog.setTitle("Sign Up");
                passwordDialog.setHeaderText("Create a new account");

                PasswordField passwordField = new PasswordField();
                passwordField.setPromptText("Password");

                PasswordField confirmPasswordField = new PasswordField();
                confirmPasswordField.setPromptText("Confirm Password");

                GridPane loginGrid = new GridPane();
                loginGrid.setHgap(10);
                loginGrid.setVgap(10);
                loginGrid.setPadding(new Insets(20));

                loginGrid.add(new Label("Password:"), 0, 0);
                loginGrid.add(passwordField, 1, 0);
                loginGrid.add(new Label("Confirm Password:"), 0, 1);
                loginGrid.add(confirmPasswordField, 1, 1);

                passwordDialog.getDialogPane().setContent(loginGrid);

                ButtonType signUpButtonType = new ButtonType("Sign Up", ButtonBar.ButtonData.OK_DONE);
                passwordDialog.getDialogPane().getButtonTypes().addAll(signUpButtonType, ButtonType.CANCEL);

                passwordDialog.setResultConverter(buttonType -> {
                    if (buttonType == signUpButtonType) {
                        String pass = passwordField.getText();
                        String confirmPass = confirmPasswordField.getText();

                        if (!pass.equals(confirmPass)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("Passwords do not match");
                            alert.setContentText("Please make sure both passwords are the same.");
                            alert.show();
                            return null;
                        }
                        return pass;
                    }
                    return null;
                });

                Optional<String> passwordResult = passwordDialog.showAndWait();
                passwordResult.ifPresent(pass -> {
                    User newUser = new User(username, pass, false);
                    users.add(newUser);
                    User.saveUsers(users, "users.csv");

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Sign Up Successful");
                    alert.setHeaderText("Account created");
                    alert.setContentText("You can now log in with your new account.");
                    alert.show();
                });
            });
        });

        guestButton.setOnAction(e -> {
            currentUser = null;
            isAdmin = false;
            loginStage.close();
            showMainUI(primaryStage);
        });

        Scene loginScene = new Scene(grid, 350, 250);
        loginStage.setScene(loginScene);
        loginStage.show();
    }

    public void showMainUI(Stage primaryStage) {
        File imagesDir = new File("/RestaurantCatalog/images/");
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
        }

        allRestaurants = getRestaurants("restaurants.csv");

        primaryStage.setTitle("Restaurant Catalog");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8F9FA;");

        HBox topNav = createTopNavigation();
        VBox leftSidebar = createCategorySidebar();
        ScrollPane centerContent = createRestaurantGrid();

        root.setTop(topNav);
        root.setLeft(leftSidebar);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1100, 780);
        primaryStage.getIcons().add(new Image("https://via.placeholder.com/32.png?text=RC"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public HBox createTopNavigation() {
        HBox topNav = new HBox(20);
        topNav.setPadding(new Insets(20));
        topNav.setAlignment(Pos.CENTER_LEFT);
        topNav.setStyle("-fx-background-color: #343A40; -fx-border-color: #212529; -fx-border-width: 0 0 1 0;");

        Label logo = new Label("Restaurant Catalog");
        logo.setFont(Font.font("Montserrat", FontWeight.BOLD, 26));
        logo.setTextFill(Color.WHITE);

        TextField searchField = new TextField();
        searchField.setPromptText("Find restaurants or cuisines...");
        searchField.setPrefWidth(350);
        searchField.setStyle("-fx-background-radius: 20; -fx-padding: 8;");

        Button searchButton = new Button("Search");
        searchButton.setStyle(
                "-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
        searchButton.setOnAction(e -> {
            String query = searchField.getText().toLowerCase();
            filterRestaurants(query);
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        VBox topNavContainer = new VBox(10);
        topNavContainer.setPadding(new Insets(10));

        HBox searchBarContainer = new HBox(20);
        searchBarContainer.setAlignment(Pos.CENTER_LEFT);
        searchBarContainer.getChildren().addAll(logo, searchField, searchButton, spacer);

        topNavContainer.getChildren().addAll(searchBarContainer, createFilterBar());
        topNav.getChildren().add(topNavContainer);

        if (isAdmin) {
            Button addButton = new Button("Add Restaurant");
            addButton.setStyle(
                    "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
            addButton.setOnAction(e -> openAddRestaurantDialog());
            topNav.getChildren().add(addButton);
        }

        return topNav;
    }

    public HBox createFilterBar() {
        HBox filterBar = new HBox(10);
        filterBar.setPadding(new Insets(10, 0, 10, 0));
        filterBar.setStyle("-fx-background-color: #F8F9FA;");

        List<String> cuisines = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            if (!cuisines.contains(restaurant.cuisine)) {
                cuisines.add(restaurant.cuisine);
            }
        }

        ToggleGroup filterModeGroup = new ToggleGroup();
        RadioButton andFilter = new RadioButton("Show restaurants with all selected tags");
        andFilter.setToggleGroup(filterModeGroup);
        andFilter.setSelected(true);
        RadioButton orFilter = new RadioButton("Show restaurants with any selected tag");
        orFilter.setToggleGroup(filterModeGroup);

        VBox filterModeBox = new VBox(5, andFilter, orFilter);
        filterModeBox.setPadding(new Insets(5));

        filterBar.getChildren().addAll(filterModeBox);

        List<Button> tagButtons = new ArrayList<>();
        for (String cuisine : cuisines) {
            Button tagButton = new Button(cuisine);
            tagButton.setStyle(
                    "-fx-background-color: white; -fx-text-fill: #495057; -fx-alignment: CENTER; -fx-padding: 10 20; -fx-border-radius: 5; -fx-border-color: #495057; -fx-border-width: 2px;");
            tagButton.setOnAction(e -> toggleTag(tagButton));
            tagButtons.add(tagButton);
            filterBar.getChildren().add(tagButton);
        }

        Button applyButton = new Button("Apply Filter");
        applyButton.setStyle(
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
        applyButton.setOnAction(e -> applyFilter(tagButtons, andFilter.isSelected()));
        filterBar.getChildren().add(applyButton);

        ScrollPane scrollPane = new ScrollPane(filterBar);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: #F8F9FA;");

        HBox container = new HBox(scrollPane);
        container.setStyle("-fx-background-color: #F8F9FA;");

        return container;
    }

    private void toggleTag(Button tagButton) {
        String currentStyle = tagButton.getStyle();
        if (currentStyle.contains("#28A745")) {
            tagButton.setStyle(
                    "-fx-background-color: white; -fx-text-fill: #495057; -fx-alignment: CENTER; -fx-padding: 10 20; -fx-border-radius: 5; -fx-border-color: #495057; -fx-border-width: 2px;");
        } else {
            tagButton.setStyle(
                    "-fx-background-color: #28A745; -fx-text-fill: white; -fx-alignment: CENTER; -fx-padding: 10 20; -fx-border-radius: 5;");
        }
    }

    public void applyFilter(List<Button> tagButtons, boolean andFilter) {
        List<String> selectedTags = new ArrayList<>();
        for (Button tagButton : tagButtons) {
            if (tagButton.getStyle().contains("#28A745")) {
                selectedTags.add(tagButton.getText());
            }
        }

        List<Restaurant> filteredRestaurants = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            boolean hasMatchingTags = selectedTags.stream().anyMatch(tag -> restaurant.cuisine.equals(tag));

            if (andFilter) {
                boolean hasAll = selectedTags.stream().allMatch(tag -> restaurant.cuisine.equals(tag));
                if (hasAll) {
                    filteredRestaurants.add(restaurant);
                }
            } else {
                if (hasMatchingTags) {
                    filteredRestaurants.add(restaurant);
                }
            }
        }

        displayRestaurants(filteredRestaurants);
    }

    public VBox createCategorySidebar() {
        VBox leftSidebar = new VBox(15);
        leftSidebar.setPadding(new Insets(30, 20, 20, 20));
        leftSidebar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e9ecef; -fx-border-width: 0 1 0 0;");
        leftSidebar.setPrefWidth(200);

        Label categoriesLabel = new Label("CATEGORIES");
        categoriesLabel.setFont(Font.font("Montserrat", FontWeight.BOLD, 16));
        categoriesLabel.setTextFill(Color.web("#495057"));
        categoriesLabel.setPadding(new Insets(0, 0, 10, 0));

        String buttonStyle = "-fx-background-color: transparent; -fx-text-fill: #495057; -fx-alignment: CENTER_LEFT; -fx-padding: 10 15; -fx-border-radius: 5; -fx-background-radius: 5;";
        String activeButtonStyle = "-fx-background-color: #E9ECEF; -fx-text-fill: #212529; -fx-alignment: CENTER_LEFT; -fx-padding: 10 15; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-weight: bold;";

        Button allButton = new Button("All Restaurants");
        allButton.setMaxWidth(Double.MAX_VALUE);
        allButton.setStyle(activeButtonStyle);

        Button favoritesButton = new Button("Favorites");
        favoritesButton.setMaxWidth(Double.MAX_VALUE);
        favoritesButton.setStyle(buttonStyle);

        List<Button> categoryButtons = new ArrayList<>();
        categoryButtons.add(allButton);
        categoryButtons.add(favoritesButton);

        Button fastFoodButton = new Button("Fast Food");
        Button italianButton = new Button("Italian");
        Button chineseButton = new Button("Chinese");
        Button japaneseButton = new Button("Japanese");
        Button mexicanButton = new Button("Mexican");
        Button indianButton = new Button("Indian");
        Button dessertButton = new Button("Dessert");
        Button healthyButton = new Button("Healthy");

        categoryButtons.add(fastFoodButton);
        categoryButtons.add(italianButton);
        categoryButtons.add(chineseButton);
        categoryButtons.add(japaneseButton);
        categoryButtons.add(mexicanButton);
        categoryButtons.add(indianButton);
        categoryButtons.add(dessertButton);
        categoryButtons.add(healthyButton);

        for (Button button : categoryButtons) {
            if (button != allButton && button != favoritesButton) {
                button.setMaxWidth(Double.MAX_VALUE);
                button.setStyle(buttonStyle);
            }

            button.setOnAction(e -> {
                for (Button b : categoryButtons) {
                    b.setStyle(buttonStyle);
                }
                button.setStyle(activeButtonStyle);

                if (button == allButton) {
                    displayAllRestaurants();
                } else if (button == favoritesButton) {
                    displayFavorites();
                } else {
                    filterRestaurantsByCategory(button.getText());
                }
            });
        }

        VBox buttonContainer = new VBox(10);
        buttonContainer.getChildren().addAll(categoryButtons);

        ScrollPane scrollPane = new ScrollPane(buttonContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setStyle("-fx-background-color: transparent;");

        leftSidebar.getChildren().add(categoriesLabel);
        leftSidebar.getChildren().add(scrollPane);

        return leftSidebar;
    }

    public void filterRestaurantsByCategory(String category) {
        List<Restaurant> filtered = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.cuisine.equals(category)) {
                filtered.add(restaurant);
            }
        }
        displayRestaurants(filtered);
    }

    public ScrollPane createRestaurantGrid() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F8F9FA; -fx-background-color: #F8F9FA;");
        scrollPane.setPadding(new Insets(10));

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(30);
        gridPane.setVgap(30);

        allRestaurants = getRestaurants("restaurants.csv");
        displayAllRestaurants();

        scrollPane.setContent(gridPane);
        return scrollPane;
    }

    public void displayAllRestaurants() {
        displayRestaurants(allRestaurants);
    }

    public void filterRestaurants(String query) {
        if (query.isEmpty()) {
            displayAllRestaurants();
            return;
        }

        List<Restaurant> filtered = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.name.toLowerCase().contains(query) || restaurant.cuisine.toLowerCase().contains(query)) {
                filtered.add(restaurant);
            }
        }
        displayRestaurants(filtered);
    }

    public void displayRestaurants(List<Restaurant> restaurants) {
        gridPane.getChildren().clear();
        int column = 0;
        int row = 0;
        for (Restaurant restaurant : restaurants) {
            VBox card = createRestaurantCard(restaurant);
            gridPane.add(card, column, row);
            column++;
            if (column >= 2) {
                column = 0;
                row++;
            }
        }

        if (restaurants.isEmpty()) {
            Label noResults = new Label("No restaurants found");
            noResults.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
            noResults.setTextFill(Color.web("#6C757D"));
            gridPane.add(noResults, 0, 0);
        }
    }

    public VBox createRestaurantCard(Restaurant restaurant) {
        VBox card = new VBox(0);
        card.setPadding(new Insets(0));
        card.setStyle(
                "-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: #e0e0e0; -fx-border-width: 1;");
        card.setPrefWidth(475);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(15);
        shadow.setOffsetX(0);
        shadow.setOffsetY(4);
        shadow.setColor(Color.rgb(0, 0, 0, 0.15));
        card.setEffect(shadow);

        ImageView imageView;
        try {
            if (restaurant.imageUrl.startsWith("http")) {
                Image image = new Image(restaurant.imageUrl, 475, 250, false, true);
                imageView = new ImageView(image);
            } else {
                File imageFile = new File(restaurant.imageUrl);
                if (imageFile.exists()) {
                    Image image = new Image(imageFile.toURI().toString(), 475, 250, false, true);
                    imageView = new ImageView(image);
                } else {
                    throw new FileNotFoundException("Image file not found: " + imageFile.getAbsolutePath());
                }
            }
            imageView.setFitWidth(475);
            imageView.setFitHeight(238);
            imageView.setStyle("-fx-background-radius: 12 12 0 0; -fx-border-radius: 12 12 0 0;");
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            imageView = new ImageView();
            imageView.setFitWidth(475);
            imageView.setFitHeight(238);
            imageView.setStyle(
                    "-fx-background-color: #E9ECEF; -fx-background-radius: 12 12 0 0; -fx-border-radius: 12 12 0 0;");
        }

        Rectangle clip = new Rectangle(475, 238);
        clip.setArcWidth(24);
        clip.setArcHeight(24);
        imageView.setClip(clip);

        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(15, 20, 10, 20));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(restaurant.name);
        nameLabel.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));
        nameLabel.setTextFill(Color.web("#212529"));

        HBox ratingBox = new HBox(5);
        ratingBox.setAlignment(Pos.CENTER_RIGHT);

        Label ratingLabel = new Label(String.format("%.1f", restaurant.rating));
        ratingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ratingLabel.setTextFill(Color.web("#FFC107"));

        Label starLabel = new Label("★");
        starLabel.setFont(Font.font("Arial", 18));
        starLabel.setTextFill(Color.web("#FFC107"));

        ratingBox.getChildren().addAll(starLabel, ratingLabel);

        headerBox.getChildren().addAll(nameLabel);

        HBox cuisineRatingBox = new HBox(10, new Label(restaurant.cuisine), ratingBox);
        cuisineRatingBox.setAlignment(Pos.CENTER_LEFT);

        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);

        Button orderButton = new Button("Order Now");
        orderButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 5;");
        buttonGrid.add(orderButton, 0, 0);

        Button detailsButton = new Button("View Details");
        detailsButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #6C757D; -fx-border-color: #6C757D; -fx-border-radius: 5;");
        detailsButton.setOnAction(e -> showRestaurantDetails(restaurant));
        buttonGrid.add(detailsButton, 1, 0);

        Button favoriteButton = new Button("Add to Favorites");
        if (currentUser != null && currentUser.getFavoriteRestaurants().contains(restaurant.name)) {
            favoriteButton.setText("Remove from Favorites");
        }
        favoriteButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #FF0000; -fx-border-color: #FF0000; -fx-border-radius: 5;");
        favoriteButton.setOnAction(e -> {
            if (currentUser != null) {
                if (currentUser.getFavoriteRestaurants().contains(restaurant.name)) {
                    currentUser.removeFavoriteRestaurant(restaurant.name);
                    favoriteButton.setText("Add to Favorites");
                } else {
                    currentUser.addFavoriteRestaurant(restaurant.name);
                    favoriteButton.setText("Remove from Favorites");
                }
                User.saveUsers(users, "users.csv");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Not Logged In");
                alert.setHeaderText("You must be logged in to add favorites.");
                alert.setContentText("Please log in or sign up to use this feature.");
                alert.show();
            }
        });
        buttonGrid.add(favoriteButton, 0, 1);

        if (isAdmin) {
            Button updateButton = new Button("Update Details");
            updateButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 5;");
            updateButton.setOnAction(e -> openUpdateRestaurantDialog(restaurant));
            buttonGrid.add(updateButton, 1, 1);

            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-background-radius: 5;");
            deleteButton.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Restaurant");
                alert.setHeaderText("Are you sure you want to delete " + restaurant.name + "?");
                alert.setContentText("This action cannot be undone.");
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        deleteRestaurant(restaurant);
                    }
                });
            });
            buttonGrid.add(deleteButton, 2, 1);
        }

        infoBox.getChildren().addAll(headerBox, cuisineRatingBox, buttonGrid);
        card.getChildren().addAll(imageView, infoBox);
        return card;
    }

    private void showRestaurantDetails(Restaurant restaurant) {
        Stage detailsStage = new Stage();
        detailsStage.setTitle(restaurant.name + " - Details");

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_LEFT);

        Label title = new Label(restaurant.name);
        title.setFont(Font.font("Montserrat", FontWeight.BOLD, 28));

        ImageView mainImage = new ImageView();
        mainImage.setFitWidth(500);
        mainImage.setPreserveRatio(true);
        try {
            File mainFile = new File(restaurant.imageUrl);
            if (mainFile.exists()) {
                Image image = new Image(mainFile.toURI().toString());
                mainImage.setImage(image);
            } else if (restaurant.imageUrl.startsWith("http")) {
                mainImage.setImage(new Image(restaurant.imageUrl));
            }
        } catch (Exception e) {
        }

        Label ratingLabel = new Label("Rating: " + String.format("%.1f", restaurant.rating) + " ★");
        ratingLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        ratingLabel.setTextFill(Color.web("#FFC107"));

        Label descTitle = new Label("Description:");
        descTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        TextArea descArea = new TextArea(restaurant.description);
        descArea.setWrapText(true);
        descArea.setEditable(false);
        descArea.setPrefRowCount(3);

        Label tagsLabel = new Label("Tags:");
        tagsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        FlowPane tagPane = new FlowPane();
        tagPane.setHgap(10);
        tagPane.setVgap(10);
        for (String tag : restaurant.tags) {
            Label t = new Label(tag);
            t.setStyle(
                    "-fx-background-color: #28A745; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 5;");
            tagPane.getChildren().add(t);
        }

        Label moreImagesLabel = new Label("Extra Images:");
        moreImagesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        FlowPane imagesFlow = new FlowPane();
        imagesFlow.setHgap(10);
        imagesFlow.setVgap(10);
        for (String imgPath : restaurant.additionalImages) {
            File f = new File(imgPath);
            if (f.exists()) {
                ImageView iv = new ImageView(new Image(f.toURI().toString()));
                iv.setFitWidth(200);
                iv.setPreserveRatio(true);
                imagesFlow.getChildren().add(iv);
            } else if (imgPath.startsWith("http")) {
                ImageView iv = new ImageView(new Image(imgPath));
                iv.setFitWidth(200);
                iv.setPreserveRatio(true);
                imagesFlow.getChildren().add(iv);
            }
        }

        Label menuLabel = new Label("Menu:");
        menuLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        if (restaurant.menuItems.isEmpty()) {
            Label emptyMenuLabel = new Label("No menu items available.");
            root.getChildren().addAll(menuLabel, emptyMenuLabel);
        } else {
            ListView<String> menuList = new ListView<>();
            menuList.getItems().addAll(restaurant.menuItems);
            menuList.setMaxHeight(120);
            root.getChildren().addAll(menuLabel, menuList);
        }

        Button backButton = new Button("Back to Restaurants");
        backButton.setStyle(
                "-fx-background-color: #6C757D; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 10 20;");
        backButton.setOnAction(e -> detailsStage.close());

        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER);
        bottomBar.setPadding(new Insets(20, 0, 0, 0));
        bottomBar.getChildren().add(backButton);

        root.getChildren().addAll(
                title,
                mainImage,
                ratingLabel,
                descTitle,
                descArea,
                tagsLabel,
                tagPane,
                moreImagesLabel,
                imagesFlow,
                bottomBar);

        scrollPane.setContent(root);

        Scene scene = new Scene(scrollPane, 600, 700);
        detailsStage.setScene(scene);
        detailsStage.show();
    }

    public void openAddRestaurantDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Restaurant");

        TextField nameField = new TextField();
        nameField.setPromptText("Restaurant Name");

        TextField cuisineField = new TextField();
        cuisineField.setPromptText("Cuisine Type");

        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating (1-5)");

        TextArea descField = new TextArea();
        descField.setPromptText("Description");
        descField.setPrefRowCount(2);

        TextField tagsField = new TextField();
        tagsField.setPromptText("Tags (comma-separated)");

        Button uploadExtraImgs = new Button("Upload Extra Images");
        Label extraImgsLabel = new Label("None selected");
        List<File> selectedExtraImages = new ArrayList<>();

        uploadExtraImgs.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Extra Images");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            List<File> chosen = fileChooser.showOpenMultipleDialog(dialog);
            if (chosen != null && !chosen.isEmpty()) {
                selectedExtraImages.clear();
                selectedExtraImages.addAll(chosen);
                extraImgsLabel.setText(chosen.size() + " images selected");
            }
        });

        Label menuInfo = new Label("Menu Items (one per line, or separate by semicolons)");
        TextArea menuField = new TextArea();
        menuField.setPromptText("e.g.\nMargherita Pizza - $9.99;\nPepperoni Pizza - $10.99;");
        menuField.setPrefRowCount(3);

        Button uploadCoverButton = new Button("Upload Cover Image");
        Label coverLabel = new Label("No image selected");
        File[] selectedCoverFile = new File[1];

        uploadCoverButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            selectedCoverFile[0] = fileChooser.showOpenDialog(dialog);
            if (selectedCoverFile[0] != null) {
                coverLabel.setText(selectedCoverFile[0].getAbsolutePath());
            }
        });

        Button submitButton = new Button("Add Restaurant");
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String cuisine = cuisineField.getText();
            double ratingValue;
            try {
                ratingValue = Double.parseDouble(ratingField.getText());
                if (ratingValue < 1 || ratingValue > 5) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid rating between 1 and 5.");
                alert.show();
                return;
            }

            String description = descField.getText();
            List<String> tags = new ArrayList<>();
            if (!tagsField.getText().trim().isEmpty()) {
                for (String t : tagsField.getText().split(",")) {
                    tags.add(t.trim());
                }
            }

            List<String> extraImgPaths = new ArrayList<>();
            for (File img : selectedExtraImages) {
                String id = System.currentTimeMillis() + "_" + img.getName();
                addImage(img, id, false);
                extraImgPaths.add("images/" + id);
            }

            String imageUrl = "";
            if (selectedCoverFile[0] != null) {
                String id = System.currentTimeMillis() + "_" + selectedCoverFile[0].getName();
                addImage(selectedCoverFile[0], id, false);
                imageUrl = "images/" + id;
            }

            List<String> menuItems = new ArrayList<>();
            String rawMenu = menuField.getText().replace("\n", ";");
            for (String item : rawMenu.split(";")) {
                if (!item.trim().isEmpty()) {
                    menuItems.add(item.trim());
                }
            }

            addRestaurant(name, cuisine, imageUrl, ratingValue, description, tags, extraImgPaths, menuItems);
            dialog.close();
        });

        VBox dialogVbox = new VBox(10,
                nameField,
                cuisineField,
                ratingField,
                descField,
                tagsField,
                uploadExtraImgs,
                extraImgsLabel,
                uploadCoverButton,
                coverLabel,
                menuInfo,
                menuField,
                submitButton);
        dialogVbox.setPadding(new Insets(20));
        dialog.setScene(new Scene(dialogVbox, 500, 600));
        dialog.show();
    }

    public void openUpdateRestaurantDialog(Restaurant restaurant) {
        Stage dialog = new Stage();
        dialog.setTitle("Update Restaurant Details");

        TextField nameField = new TextField(restaurant.name);
        TextField cuisineField = new TextField(restaurant.cuisine);
        TextField ratingField = new TextField(String.valueOf(restaurant.rating));

        TextArea descField = new TextArea(restaurant.description);
        descField.setPrefRowCount(2);

        TextField tagsField = new TextField(String.join(",", restaurant.tags));

        Button uploadExtraImgs = new Button("Upload Extra Images");
        Label extraImgsLabel = new Label("No images selected (will keep existing if none chosen)");
        List<File> selectedExtraImages = new ArrayList<>();

        uploadExtraImgs.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Extra Images");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            List<File> chosen = fileChooser.showOpenMultipleDialog(dialog);
            if (chosen != null && !chosen.isEmpty()) {
                selectedExtraImages.clear();
                selectedExtraImages.addAll(chosen);
                extraImgsLabel.setText(chosen.size() + " images selected");
            }
        });

        Label menuInfo = new Label("Menu Items (semicolon or newline separated)");
        TextArea menuField = new TextArea(String.join(";\n", restaurant.menuItems));
        menuField.setPrefRowCount(3);

        Button uploadCoverButton = new Button("Upload Cover Image");
        Label coverLabel = new Label(restaurant.imageUrl.isEmpty() ? "No cover selected" : restaurant.imageUrl);
        File[] selectedCoverFile = new File[1];

        uploadCoverButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Cover Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
            selectedCoverFile[0] = fileChooser.showOpenDialog(dialog);
            if (selectedCoverFile[0] != null) {
                coverLabel.setText(selectedCoverFile[0].getAbsolutePath());
            }
        });

        Button submitButton = new Button("Update Restaurant");
        submitButton.setOnAction(e -> {
            String newName = nameField.getText();
            String newCuisine = cuisineField.getText();
            double newRating;
            try {
                newRating = Double.parseDouble(ratingField.getText());
                if (newRating < 1 || newRating > 5) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid rating between 1 and 5.");
                alert.show();
                return;
            }

            String newDesc = descField.getText();
            List<String> newTags = new ArrayList<>();
            if (!tagsField.getText().trim().isEmpty()) {
                for (String t : tagsField.getText().split(",")) {
                    newTags.add(t.trim());
                }
            }

            List<String> newExtraImgs;
            if (!selectedExtraImages.isEmpty()) {
                newExtraImgs = new ArrayList<>();
                for (File img : selectedExtraImages) {
                    String id = System.currentTimeMillis() + "_" + img.getName();
                    addImage(img, id, false);
                    newExtraImgs.add("images/" + id);
                }
            } else {
                newExtraImgs = restaurant.additionalImages;
            }

            List<String> newMenu = new ArrayList<>();
            String rawMenu = menuField.getText().replace("\n", ";");
            for (String item : rawMenu.split(";")) {
                if (!item.trim().isEmpty()) {
                    newMenu.add(item.trim());
                }
            }

            String newCover = restaurant.imageUrl;
            if (selectedCoverFile[0] != null) {
                String id = System.currentTimeMillis() + "_" + selectedCoverFile[0].getName();
                addImage(selectedCoverFile[0], id, false);
                newCover = "images/" + id;
            }

            updateRestaurant(restaurant, newName, newCuisine, newCover, newRating, newDesc, newTags, newExtraImgs,
                    newMenu);
            dialog.close();
        });

        VBox dialogVbox = new VBox(10,
                nameField,
                cuisineField,
                ratingField,
                descField,
                tagsField,
                uploadExtraImgs,
                extraImgsLabel,
                uploadCoverButton,
                coverLabel,
                menuInfo,
                menuField,
                submitButton);
        dialogVbox.setPadding(new Insets(20));
        dialog.setScene(new Scene(dialogVbox, 500, 600));
        dialog.show();
    }

    public void addRestaurant(String name, String cuisine, String imageUrl, double rating, String description,
            List<String> tags, List<String> extraImgs, List<String> menuItems) {
        Restaurant r = new Restaurant(name, cuisine, imageUrl, rating, description, tags, extraImgs, menuItems);
        allRestaurants.add(r);
        displayAllRestaurants();
        saveRestaurants("restaurants.csv");
    }

    public void addRestaurant(String name, String cuisine, String imageUrl, double rating) {
        addRestaurant(name, cuisine, imageUrl, rating, "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public void updateRestaurant(Restaurant oldRes, String newName, String newCuisine, String newImageUrl,
            double newRating, String newDesc, List<String> newTags, List<String> newExtraImgs, List<String> newMenu) {
        oldRes.name = newName;
        oldRes.cuisine = newCuisine;
        oldRes.imageUrl = newImageUrl;
        oldRes.rating = newRating;
        oldRes.description = newDesc;
        oldRes.tags = newTags;
        oldRes.additionalImages = newExtraImgs;
        oldRes.menuItems = newMenu;

        displayAllRestaurants();
        saveRestaurants("restaurants.csv");
    }

    public void deleteRestaurant(Restaurant restaurantToDelete) {
        allRestaurants.remove(restaurantToDelete);
        displayAllRestaurants();
        saveRestaurants("restaurants.csv");
    }

    public List<Restaurant> getRestaurants(String filePath) {
        List<Restaurant> restaurants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data.length >= 8) {
                    String name = data[0].trim();
                    String cuisine = data[1].trim();
                    String imageUrl = data[2].trim();
                    double rating = Double.parseDouble(data[3].trim());
                    String description = data[4].trim();

                    List<String> tags = new ArrayList<>();
                    if (!data[5].trim().isEmpty()) {
                        for (String t : data[5].split(";")) {
                            tags.add(t.trim());
                        }
                    }

                    List<String> extraImgs = new ArrayList<>();
                    if (!data[6].trim().isEmpty()) {
                        for (String img : data[6].split(";")) {
                            extraImgs.add(img.trim());
                        }
                    }

                    List<String> menuItems = new ArrayList<>();
                    if (!data[7].trim().isEmpty()) {
                        for (String m : data[7].split(";")) {
                            menuItems.add(m.trim());
                        }
                    }

                    restaurants.add(
                            new Restaurant(name, cuisine, imageUrl, rating, description, tags, extraImgs, menuItems));
                } else if (data.length == 7) {
                    String name = data[0].trim();
                    String cuisine = data[1].trim();
                    String imageUrl = data[2].trim();
                    double rating = Double.parseDouble(data[3].trim());
                    String description = data[4].trim();

                    List<String> tags = new ArrayList<>();
                    if (!data[5].trim().isEmpty()) {
                        for (String t : data[5].split(";")) {
                            tags.add(t.trim());
                        }
                    }

                    List<String> extraImgs = new ArrayList<>();
                    if (!data[6].trim().isEmpty()) {
                        for (String img : data[6].split(";")) {
                            extraImgs.add(img.trim());
                        }
                    }

                    restaurants.add(new Restaurant(name, cuisine, imageUrl, rating, description, tags, extraImgs,
                            new ArrayList<>()));
                } else if (data.length == 4) {
                    String name = data[0].trim();
                    String cuisine = data[1].trim();
                    String imageUrl = data[2].trim();
                    double rating = Double.parseDouble(data[3].trim());
                    restaurants.add(new Restaurant(name, cuisine, imageUrl, rating));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return restaurants;
    }

    public void saveRestaurants(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Restaurant r : allRestaurants) {
                String tagsJoined = String.join(";", r.tags);
                String extraImgsJoined = String.join(";", r.additionalImages);
                String menuJoined = String.join(";", r.menuItems);

                String line = String.join("|",
                        r.name,
                        r.cuisine,
                        r.imageUrl,
                        String.valueOf(r.rating),
                        r.description,
                        tagsJoined,
                        extraImgsJoined,
                        menuJoined);
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addImage(File img, String id, boolean cover) {
        InputStream is = null;
        OutputStream os = null;
        File resDir = new File("images");
        if (!resDir.exists()) {
            resDir.mkdirs();
        }

        try {
            File destFile = new File(resDir, id);
            is = new FileInputStream(img);
            os = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println("Error copying image: " + e.getMessage());
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e2) {
            }
            try {
                if (os != null)
                    os.close();
            } catch (IOException e3) {
            }
        }
    }

    public void displayFavorites() {
        if (currentUser == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Not Logged In");
            alert.setHeaderText("You must be logged in to see favorites.");
            alert.setContentText("Please log in or sign up to use this feature.");
            alert.show();
            return;
        }
        List<Restaurant> favorites = new ArrayList<>();
        for (String name : currentUser.getFavoriteRestaurants()) {
            for (Restaurant restaurant : allRestaurants) {
                if (restaurant.name.equals(name)) {
                    favorites.add(restaurant);
                    break;
                }
            }
        }
        displayRestaurants(favorites);
    }

    static class User {
        private String username, password;
        private boolean isAdmin;
        private List<String> favoriteRestaurants;

        public User(String username, String password, boolean isAdmin) {
            this.username = username;
            this.password = password;
            this.isAdmin = isAdmin;
            this.favoriteRestaurants = new ArrayList<>();
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public boolean isAdmin() {
            return isAdmin;
        }

        public List<String> getFavoriteRestaurants() {
            return favoriteRestaurants;
        }

        public void addFavoriteRestaurant(String restaurantName) {
            if (!favoriteRestaurants.contains(restaurantName)) {
                favoriteRestaurants.add(restaurantName);
            }
        }

        public void removeFavoriteRestaurant(String restaurantName) {
            favoriteRestaurants.remove(restaurantName);
        }

        public String toCSV() {
            String favJoined = String.join("|", favoriteRestaurants);
            return username + "," + password + "," + isAdmin + "," + favJoined;
        }

        public static User fromCSV(String csvLine) {
            String[] data = csvLine.split(",");
            if (data.length >= 3) {
                String username = data[0].trim();
                String password = data[1].trim();
                boolean isAdmin = Boolean.parseBoolean(data[2].trim());
                User user = new User(username, password, isAdmin);

                if (data.length >= 4) {
                    String favorites = data[3].trim();
                    if (!favorites.isEmpty()) {
                        for (String fav : favorites.split("\\|")) {
                            user.favoriteRestaurants.add(fav.trim());
                        }
                    }
                }
                return user;
            }
            return null;
        }

        public static List<User> loadUsers(String filePath) {
            List<User> users = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    User user = User.fromCSV(line);
                    if (user != null) {
                        users.add(user);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return users;
        }

        public static void saveUsers(List<User> users, String filePath) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                for (User u : users) {
                    bw.write(u.toCSV());
                    bw.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
