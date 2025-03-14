package file;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

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

        topNav.getChildren().addAll(logo, searchField, searchButton);
        return topNav;
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

        allRestaurants = getRestaurants();
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
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Restaurant Details");
            alert.setHeaderText(restaurant.name);
            alert.setContentText("Cuisine: " + restaurant.cuisine + "\nRating: " + restaurant.rating + "/5.0" +
                    "\n\nThis restaurant offers delicious " + restaurant.cuisine.toLowerCase() +
                    " cuisine with exceptional service and ambiance.");
            alert.showAndWait();
        });

        buttonBox.getChildren().addAll(orderButton, detailsButton);
        infoBox.getChildren().addAll(headerBox, cuisineRatingBox, buttonBox);
        card.getChildren().addAll(imageView, infoBox);
        return card;
    }

    private List<Restaurant> getRestaurants() {
        List<Restaurant> list = new ArrayList<>();
        list.add(new Restaurant("Pizza Palace", "Italian", "https://via.placeholder.com/430x240.png?text=Pizza+Palace", 4.5));
        list.add(new Restaurant("Burger Bonanza", "Fast Food", "https://via.placeholder.com/430x240.png?text=Burger+Bonanza", 4.2));
        list.add(new Restaurant("Sushi Central", "Japanese", "https://via.placeholder.com/430x240.png?text=Sushi+Central", 4.8));
        list.add(new Restaurant("Noodle Nook", "Chinese", "https://via.placeholder.com/430x240.png?text=Noodle+Nook", 4.3));
        list.add(new Restaurant("Taco Town", "Mexican", "https://via.placeholder.com/430x240.png?text=Taco+Town", 4.0));
        list.add(new Restaurant("Curry Corner", "Indian", "https://via.placeholder.com/430x240.png?text=Curry+Corner", 4.6));
        list.add(new Restaurant("Bakery Bliss", "Dessert", "https://via.placeholder.com/430x240.png?text=Bakery+Bliss", 4.7));
        list.add(new Restaurant("Salad Stop", "Healthy", "https://via.placeholder.com/430x240.png?text=Salad+Stop", 4.1));
        return list;
    }

    public static void main(String[] args) {
        launch(args);
    }
}