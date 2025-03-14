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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantCatalogUI extends Application {

    class Restaurant {
        String name;
        String cuisine;
        String imageUrl;
        double rating;

        Restaurant(String name, String cuisine, String imageUrl, double rating) {
            this.name = name;
            this.cuisine = cuisine;
            this.imageUrl = imageUrl;
            this.rating = rating;
        }
    }

    private GridPane gridPane;
    private List<Restaurant> allRestaurants;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurant Catalog");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F8F9FA;");

        HBox topNav = createTopNavigation();
        VBox leftSidebar = createCategorySidebar();
        ScrollPane centerContent = createRestaurantGrid();

        root.setTop(topNav);
        root.setLeft(leftSidebar);
        root.setCenter(centerContent);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.getIcons().add(new Image("https://via.placeholder.com/32.png?text=RC"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createTopNavigation() {
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
        searchButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
        searchButton.setOnAction(e -> {
            String query = searchField.getText().toLowerCase();
            filterRestaurants(query);
        });

        Button addButton = new Button("Add Restaurant");
        addButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
        addButton.setOnAction(e -> {
            openAddRestaurantDialog();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topNav.getChildren().addAll(logo, searchField, searchButton, spacer, addButton);

        return topNav;
    }

    private void openAddRestaurantDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Restaurant");

        TextField nameField = new TextField();
        nameField.setPromptText("Restaurant Name");
        TextField cuisineField = new TextField();
        cuisineField.setPromptText("Cuisine Type");
        TextField imageUrlField = new TextField();
        imageUrlField.setPromptText("Image URL");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Rating (1-5)");

        Button submitButton = new Button("Add Restaurant");
        submitButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String cuisine = cuisineField.getText();
            String imageUrl = imageUrlField.getText();
            double rating;

            try {
                rating = Double.parseDouble(ratingField.getText());
                if (rating < 1 || rating > 5) {
                    throw new NumberFormatException("Invalid rating range");
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid rating between 1 and 5.");
                alert.show();
                return;
            }


            addRestaurant(name, cuisine, imageUrl, rating);
            dialog.close();
        });

        VBox dialogVbox = new VBox(10, nameField, cuisineField, imageUrlField, ratingField, submitButton);
        dialogVbox.setPadding(new Insets(20));
        dialogVbox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
        dialog.setScene(new Scene(dialogVbox, 400, 300));
        dialog.show();
    }

    private VBox createCategorySidebar() {
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

        List<Button> categoryButtons = new ArrayList<>();
        categoryButtons.add(allButton);

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
            if (button != allButton) {
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
                } else {
                    filterRestaurantsByCategory(button.getText());
                }
            });
        }

        leftSidebar.getChildren().add(categoriesLabel);
        leftSidebar.getChildren().addAll(categoryButtons);
        return leftSidebar;
    }

    private ScrollPane createRestaurantGrid() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #F8F9FA; -fx-background-color: #F8F9FA;");
        scrollPane.setPadding(new Insets(10));

        gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(25);
        gridPane.setVgap(25);

        allRestaurants = getRestaurants("restaurants.csv");
        displayAllRestaurants();

        scrollPane.setContent(gridPane);
        return scrollPane;
    }

    private void displayAllRestaurants() {
        displayRestaurants(allRestaurants);
    }

    private void filterRestaurantsByCategory(String category) {
        List<Restaurant> filtered = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.cuisine.equals(category)) {
                filtered.add(restaurant);
            }
        }
        displayRestaurants(filtered);
    }

    private void filterRestaurants(String query) {
        if (query.isEmpty()) {
            displayAllRestaurants();
            return;
        }

        List<Restaurant> filtered = new ArrayList<>();
        for (Restaurant restaurant : allRestaurants) {
            if (restaurant.name.toLowerCase().contains(query) ||
                    restaurant.cuisine.toLowerCase().contains(query)) {
                filtered.add(restaurant);
            }
        }
        displayRestaurants(filtered);
    }

    private void displayRestaurants(List<Restaurant> restaurants) {
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

    private VBox createRestaurantCard(Restaurant restaurant) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(0, 0, 15, 0));
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 12; -fx-border-radius: 12;");
        card.setPrefWidth(430);
        card.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.1)));

        ImageView imageView;
        try {
            Image image = new Image(restaurant.imageUrl, 430, 240, false, true);
            imageView = new ImageView(image);
            imageView.setFitWidth(430);
            imageView.setFitHeight(240);
            imageView.setStyle("-fx-background-radius: 12 12 0 0;");
        } catch (Exception e) {
            imageView = new ImageView();
            imageView.setFitWidth(430);
            imageView.setFitHeight(240);
            imageView.setStyle("-fx-background-color: #E9ECEF;");
        }

        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(15, 20, 10, 20));

        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label nameLabel = new Label(restaurant.name);
        nameLabel.setFont(Font.font("Montserrat", FontWeight.BOLD, 22));
        nameLabel.setTextFill(Color.web("#212529"));

        HBox ratingBox = new HBox(5);
        ratingBox.setAlignment(Pos.CENTER_RIGHT);
        ratingBox.setPadding(new Insets(0, 0, 0, 15));

        Label ratingLabel = new Label(String.format("%.1f", restaurant.rating));
        ratingLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        ratingLabel.setTextFill(Color.web("#FFC107"));

        Label starLabel = new Label("★");
        starLabel.setFont(Font.font("Arial", 18));
        starLabel.setTextFill(Color.web("#FFC107"));

        ratingBox.getChildren().addAll(starLabel, ratingLabel);
        headerBox.getChildren().addAll(nameLabel);
        headerBox.setSpacing(10);

        HBox cuisineRatingBox = new HBox();
        cuisineRatingBox.setAlignment(Pos.CENTER_LEFT);

        Label cuisineLabel = new Label(restaurant.cuisine);
        cuisineLabel.setFont(Font.font("Arial", 16));
        cuisineLabel.setTextFill(Color.web("#6C757D"));
        cuisineLabel.setPadding(new Insets(0, 0, 10, 0));

        cuisineRatingBox.getChildren().addAll(cuisineLabel, ratingBox);
        cuisineRatingBox.setSpacing(10);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button orderButton = new Button("Order Now");
        orderButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");

        Button detailsButton = new Button("View Details");
        detailsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #6C757D; -fx-border-color: #6C757D; -fx-border-radius: 5; -fx-padding: 8 16;");

        detailsButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.NONE);  // NONE means no default buttons.
            alert.setTitle("Restaurant Details");
            alert.setHeaderText(restaurant.name);
            alert.setContentText("Cuisine: " + restaurant.cuisine + "\nRating: " + restaurant.rating + "/5.0" +
                    "\n\nThis restaurant offers delicious " + restaurant.cuisine.toLowerCase() +
                    " cuisine with exceptional service and ambiance.");

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType updateButtonType = new ButtonType("Update Details");

            alert.getButtonTypes().setAll(okButtonType, updateButtonType);

            alert.showAndWait().ifPresent(response -> {
                if (response == updateButtonType) {
                    openUpdateRestaurantDialog(restaurant);  // Open the Update Details dialog
                }
            });
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; -fx-background-radius: 5; -fx-padding: 8 16;");
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

        buttonBox.getChildren().addAll(orderButton, detailsButton, deleteButton);

        infoBox.getChildren().addAll(headerBox, cuisineRatingBox, buttonBox);
        card.getChildren().addAll(imageView, infoBox);
        return card;
    }

    private List<Restaurant> getRestaurants(String filePath) {
        List<Restaurant> restaurants = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
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

    // DOESNT HANDLE SPECIAL CASES YET
    private void addRestaurant(String name, String cuisine, String imageUrl, double rating) {
        Restaurant newRestaurant = new Restaurant(name, cuisine, imageUrl, rating);
        allRestaurants.add(newRestaurant);
        displayAllRestaurants();
        saveRestaurants();
    }

    private void deleteRestaurant(Restaurant restaurantToDelete) {
        allRestaurants.remove(restaurantToDelete);
        displayAllRestaurants();
        saveRestaurants();
    }

    private void updateRestaurant(Restaurant oldRestaurant, String newName, String newCuisine, String newImageUrl, double newRating) {
        oldRestaurant.name = newName;
        oldRestaurant.cuisine = newCuisine;
        oldRestaurant.imageUrl = newImageUrl;
        oldRestaurant.rating = newRating;
        displayAllRestaurants();
        saveRestaurants();
    }

    private void openUpdateRestaurantDialog(Restaurant restaurant) {
        Stage dialog = new Stage();
        dialog.setTitle("Update Restaurant Details");

        TextField nameField = new TextField(restaurant.name);
        nameField.setPromptText("Restaurant Name");
        TextField cuisineField = new TextField(restaurant.cuisine);
        cuisineField.setPromptText("Cuisine Type");
        TextField imageUrlField = new TextField(restaurant.imageUrl);
        imageUrlField.setPromptText("Image URL");
        TextField ratingField = new TextField(String.valueOf(restaurant.rating));
        ratingField.setPromptText("Rating (1-5)");

        Button submitButton = new Button("Update Restaurant");
        submitButton.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-background-radius: 20; -fx-padding: 8 16;");
        submitButton.setOnAction(e -> {
            String name = nameField.getText();
            String cuisine = cuisineField.getText();
            String imageUrl = imageUrlField.getText();
            double rating;

            try {
                rating = Double.parseDouble(ratingField.getText());
                if (rating < 1 || rating > 5) {
                    throw new NumberFormatException("Invalid rating range");
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a valid rating between 1 and 5.");
                alert.show();
                return;
            }

            updateRestaurant(restaurant, name, cuisine, imageUrl, rating);
            dialog.close();
        });

        VBox dialogVbox = new VBox(10, nameField, cuisineField, imageUrlField, ratingField, submitButton);
        dialogVbox.setPadding(new Insets(20));
        dialogVbox.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 10;");
        dialog.setScene(new Scene(dialogVbox, 400, 300));
        dialog.show();
    }

    private void saveRestaurants() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("restaurants.csv"))) {
            for (Restaurant restaurant : allRestaurants) {
                bw.write(restaurant.name + ", " + restaurant.cuisine + ", " + restaurant.imageUrl + ", " + restaurant.rating);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
