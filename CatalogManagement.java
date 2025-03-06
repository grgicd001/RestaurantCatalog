import java.io.*;
import java.util.*;

public class CatalogManagement {

    private static final String FILE_NAME = "restaurants.csv";
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
            System.out.println("5. Search Restaurants by Name");
            System.out.println("6. Filter Restaurants by Location or Cuisine");
            System.out.println("7. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewRestaurants();
                case 2 -> addRestaurant(scanner);
                case 3 -> editRestaurant(scanner);
                case 4 -> deleteRestaurant(scanner);
                case 5 -> search(scanner);
                case 6 -> filter(scanner);
                case 7 -> {
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
                if (parts.length == 4) {
                    catalog.add(new Restaurant(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
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
        System.out.println("\nRestaurant List:");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.println((i + 1) + ". " + catalog.get(i));
        }
    }

    private static void addRestaurant(Scanner scanner) {
        System.out.print("Enter restaurant ID: ");
        String id = scanner.nextLine().trim();

        if (catalog.stream().anyMatch(r -> r.id.equals(id))) {
            System.out.println("A restaurant with this ID already exists.");
            return;
        }

        System.out.print("Enter restaurant name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter restaurant location: ");
        String location = scanner.nextLine().trim();
        System.out.print("Enter restaurant cuisine type: ");
        String cuisine = scanner.nextLine().trim();

        catalog.add(new Restaurant(id, name, location, cuisine));
        System.out.println("Restaurant added successfully.");
    }

    private static void editRestaurant(Scanner scanner) {
        viewRestaurants();
        System.out.print("Enter the number of the restaurant to edit: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index < 0 || index >= catalog.size()) {
            System.out.println("Invalid restaurant number.");
            return;
        }

        Restaurant restaurant = catalog.get(index);
        System.out.print("Enter new name (current: " + restaurant.name + "): ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter new location (current: " + restaurant.location + "): ");
        String location = scanner.nextLine().trim();
        System.out.print("Enter new cuisine (current: " + restaurant.cuisine + "): ");
        String cuisine = scanner.nextLine().trim();

        if (!name.isEmpty()) restaurant.name = name;
        if (!location.isEmpty()) restaurant.location = location;
        if (!cuisine.isEmpty()) restaurant.cuisine = cuisine;

        System.out.println("Restaurant updated successfully.");
    }

    private static void deleteRestaurant(Scanner scanner) {
        viewRestaurants();
        System.out.print("Enter the number of the restaurant to delete: ");
        int index = scanner.nextInt() - 1;
        scanner.nextLine();

        if (index < 0 || index >= catalog.size()) {
            System.out.println("Invalid restaurant number.");
            return;
        }
        catalog.remove(index);
        System.out.println("Restaurant deleted successfully.");
    }

    private static void search(Scanner scanner) {
        System.out.print("Enter a keyword to search by restaurant name: ");
        String keyword = scanner.nextLine().trim().toLowerCase();

        List<Restaurant> results = catalog.stream()
                .filter(r -> r.name.toLowerCase().contains(keyword))
                .toList();

        if (results.isEmpty()) {
            System.out.println("No restaurants found matching the search term.");
        } else {
            results.forEach(System.out::println);
        }
    }

    private static void filter(Scanner scanner) {
        System.out.println("Filter by:");
        System.out.println("1. Location");
        System.out.println("2. Cuisine");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        Set<String> options = new HashSet<>();
        if (choice == 1) {
            catalog.forEach(r -> options.add(r.location));
            System.out.println("Available Locations: " + options);
        } else if (choice == 2) {
            catalog.forEach(r -> options.add(r.cuisine));
            System.out.println("Available Cuisines: " + options);
        } else {
            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("Enter the tags to filter by (comma-separated): ");
        String[] selectedTags = scanner.nextLine().trim().toLowerCase().split(",");
        Set<String> selectedSet = new HashSet<>(Arrays.asList(selectedTags));

        System.out.print("Filter inclusively (any tag) or exclusively (all tags)? (I/E): ");
        String mode = scanner.nextLine().trim().toUpperCase();

        List<Restaurant> filtered;
        if (mode.equals("I")) {
            filtered = catalog.stream()
                    .filter(r -> (choice == 1 ? selectedSet.contains(r.location.toLowerCase()) : selectedSet.contains(r.cuisine.toLowerCase())))
                    .toList();
        } else if (mode.equals("E")) {
            filtered = catalog.stream()
                    .filter(r -> (choice == 1 ? selectedSet.containsAll(Set.of(r.location.toLowerCase())) : selectedSet.containsAll(Set.of(r.cuisine.toLowerCase()))))
                    .toList();
        } else {
            System.out.println("Invalid filter mode.");
            return;
        }

        if (filtered.isEmpty()) {
            System.out.println("No restaurants match the selected filters.");
        } else {
            filtered.forEach(System.out::println);
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
        String id, name, location, cuisine;
        public Restaurant(String id, String name, String location, String cuisine) {
            this.id = id; this.name = name; this.location = location; this.cuisine = cuisine;
        }
        public String toCSV() { return id + "," + name + "," + location + "," + cuisine; }
        @Override public String toString() { return "ID: " + id + ", Name: " + name + ", Location: " + location + ", Cuisine: " + cuisine; }
    }
}
