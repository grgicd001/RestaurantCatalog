import java.io.*;
import java.util.*;

public class RestaurantCatalogManagement {

    private static final String FILE_NAME = "restaurant_catalog.csv";
    private static List<Restaurant> catalog = new ArrayList<>();

    public static void main(String[] args) {
        loadCatalog();
        Scanner scanner = new Scanner(System.in);

        if (!authenticateUser(scanner)) {
            System.out.println("Authentication failed. Exiting.");
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\nRestaurant Catalog Management System");
            System.out.println("1. View Restaurants");
            System.out.println("2. Add Restaurant");
            System.out.println("3. Edit Restaurant");
            System.out.println("4. Delete Restaurant");
            System.out.println("5. Search and Filter Restaurants");
            System.out.println("6. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewRestaurants();
                case 2 -> addRestaurant(scanner);
                case 3 -> editRestaurant(scanner);
                case 4 -> deleteRestaurant(scanner);
                case 5 -> searchAndFilterRestaurants(scanner);
                case 6 -> {
                    saveCatalog();
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static boolean authenticateUser(Scanner scanner) {
        final String USERNAME = "admin";
        final String PASSWORD = "password123";

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        return USERNAME.equals(username) && PASSWORD.equals(password);
    }

    private static void loadCatalog() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Catalog file not found. Creating a new one.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating catalog file: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    String cuisine = parts[2].trim();
                    List<String> dietaryOptions = new ArrayList<>();
                    for (int i = 3; i < parts.length; i++) {
                        dietaryOptions.add(parts[i].trim());
                    }
                    catalog.add(new Restaurant(id, name, cuisine, dietaryOptions));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading catalog file: " + e.getMessage());
        }
    }

    private static void viewRestaurants() {
        if (catalog.isEmpty()) {
            System.out.println("No restaurants in the catalog.");
            return;
        }

        System.out.println("\nRestaurant Catalog:");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.println((i + 1) + ". " + catalog.get(i));
        }
    }

    private static void addRestaurant(Scanner scanner) {
        System.out.print("Enter restaurant ID: ");
        String id = scanner.nextLine().trim();

        if (catalog.stream().anyMatch(restaurant -> restaurant.id.equals(id))) {
            System.out.println("A restaurant with this ID already exists. Please use a unique ID.");
            return;
        }

        System.out.print("Enter restaurant name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter cuisine type: ");
        String cuisine = scanner.nextLine().trim();

        System.out.print("Enter dietary options (comma-separated, e.g., Vegetarian,Vegan,Gluten-Free): ");
        String dietaryOptionsInput = scanner.nextLine().trim();
        List<String> dietaryOptions = Arrays.asList(dietaryOptionsInput.split(","));

        if (id.isEmpty() || name.isEmpty() || cuisine.isEmpty()) {
            System.out.println("ERROR: ID, Name, and Cuisine are required.");
            return;
        }

        catalog.add(new Restaurant(id, name, cuisine, dietaryOptions));
        System.out.println("Restaurant added successfully.");
    }

    private static void editRestaurant(Scanner scanner) {
        if (catalog.isEmpty()) {
            System.out.println("The catalog is empty. Nothing to edit.");
            return;
        }

        viewRestaurants();

        System.out.print("Enter the number of the restaurant to edit: ");
        int index;
        try {
            index = scanner.nextInt() - 1;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        if (index < 0 || index >= catalog.size()) {
            System.out.println("Invalid restaurant number.");
            return;
        }

        Restaurant restaurant = catalog.get(index);

        System.out.print("Enter new name (current: " + restaurant.getName() + "): ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter new cuisine type (current: " + restaurant.getCuisine() + "): ");
        String cuisine = scanner.nextLine().trim();

        System.out.print("Enter new dietary options (comma-separated, current: " + String.join(",", restaurant.getDietaryOptions()) + "): ");
        String dietaryOptionsInput = scanner.nextLine().trim();
        List<String> dietaryOptions = Arrays.asList(dietaryOptionsInput.split(","));

        if (name.isEmpty() && cuisine.isEmpty() && dietaryOptions.isEmpty()) {
            System.out.println("Error: At least one field (Name, Cuisine, or Dietary Options) must be updated.");
            return;
        }

        if (!name.isEmpty()) {
            restaurant.setName(name);
        }
        if (!cuisine.isEmpty()) {
            restaurant.setCuisine(cuisine);
        }
        if (!dietaryOptions.isEmpty()) {
            restaurant.setDietaryOptions(dietaryOptions);
        }

        System.out.println("Restaurant updated successfully.");
    }

    private static void deleteRestaurant(Scanner scanner) {
        if (catalog.isEmpty()) {
            System.out.println("The catalog is empty. Nothing to delete.");
            return;
        }

        viewRestaurants();

        System.out.print("Enter the number of the restaurant to delete: ");
        int index;
        try {
            index = scanner.nextInt() - 1;
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            scanner.nextLine();
            return;
        }
        scanner.nextLine();

        if (index < 0 || index >= catalog.size()) {
            System.out.println("Invalid restaurant number.");
            return;
        }

        Restaurant restaurant = catalog.remove(index);
        System.out.println("Restaurant '" + restaurant.getName() + "' deleted successfully.");
    }

    private static void searchAndFilterRestaurants(Scanner scanner) {
        if (catalog.isEmpty()) {
            System.out.println("The catalog is empty. Nothing to search.");
            return;
        }

        System.out.println("\nSearch and Filter Restaurants");
        System.out.println("1. Filter by Cuisine");
        System.out.println("2. Filter by Dietary Options");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> {
                System.out.print("Enter cuisine type to filter by: ");
                String cuisine = scanner.nextLine().trim();
                List<Restaurant> filteredByCuisine = catalog.stream()
                        .filter(restaurant -> restaurant.getCuisine().equalsIgnoreCase(cuisine))
                        .toList();
                if (filteredByCuisine.isEmpty()) {
                    System.out.println("No restaurants found with the specified cuisine.");
                } else {
                    System.out.println("\nFiltered Restaurants:");
                    filteredByCuisine.forEach(System.out::println);
                }
            }
            case 2 -> {
                System.out.print("Enter dietary option to filter by: ");
                String dietaryOption = scanner.nextLine().trim();
                List<Restaurant> filteredByDietaryOption = catalog.stream()
                        .filter(restaurant -> restaurant.getDietaryOptions().contains(dietaryOption))
                        .toList();
                if (filteredByDietaryOption.isEmpty()) {
                    System.out.println("No restaurants found with the specified dietary option.");
                } else {
                    System.out.println("\nFiltered Restaurants:");
                    filteredByDietaryOption.forEach(System.out::println);
                }
            }
            default -> System.out.println("Invalid option. Please try again.");
        }
    }

    private static void saveCatalog() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Restaurant restaurant : catalog) {
                bw.write(restaurant.toCSV());
                bw.newLine();
            }
            System.out.println("Catalog saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving catalog: " + e.getMessage());
        }
    }

    static class Restaurant {
        private String id;
        private String name;
        private String cuisine;
        private List<String> dietaryOptions;

        public Restaurant(String id, String name, String cuisine, List<String> dietaryOptions) {
            this.id = id;
            this.name = name;
            this.cuisine = cuisine;
            this.dietaryOptions = dietaryOptions;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCuisine() {
            return cuisine;
        }

        public void setCuisine(String cuisine) {
            this.cuisine = cuisine;
        }

        public List<String> getDietaryOptions() {
            return dietaryOptions;
        }

        public void setDietaryOptions(List<String> dietaryOptions) {
            this.dietaryOptions = dietaryOptions;
        }

        public String toCSV() {
            return id + "," + name + "," + cuisine + "," + String.join(",", dietaryOptions);
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Cuisine: " + cuisine + ", Dietary Options: " + String.join(", ", dietaryOptions);
        }
    }
}