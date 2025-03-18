import java.io.*;
import java.util.*;

public class CatalogManagement {

    private static final String FILE_NAME = "restaurants.csv";
    private static final String USERS_FILE = "users.csv";
    private static List<Restaurant> catalog = new ArrayList<>();
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        loadCatalog();
        loadUsers();
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
            System.out.println("6. Find Restaurant's Location");
            System.out.println("7. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> viewRestaurants();
                case 2 -> {
                    if (currentUser.isAdmin()) addRestaurant(scanner);
                    else System.out.println("Access denied. Only admins can add restaurants.");
                }
                case 3 -> {
                    if (currentUser.isAdmin()) editRestaurant(scanner);
                    else System.out.println("Access denied. Only admins can edit restaurants.");
                }
                case 4 -> {
                    if (currentUser.isAdmin()) deleteRestaurant(scanner);
                    else System.out.println("Access denied. Only admins can delete restaurants.");
                }
                case 5 -> searchAndFilter(scanner);
                case 6 -> findRestaurantLocation(scanner);
                case 7 -> {
                    saveCatalog();
                    saveUsers();
                    running = false;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static boolean authenticateUser(Scanner scanner) {
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Continue as Guest");
        System.out.print("Choose an option: ");
        int option = scanner.nextInt();
        scanner.nextLine();

        switch (option) {
            case 1 -> {
                System.out.print("Enter username: ");
                String username = scanner.nextLine();
                System.out.print("Enter password: ");
                String password = scanner.nextLine();
                currentUser = users.stream()
                        .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                        .findFirst()
                        .orElse(null);
                if (currentUser == null) {
                    System.out.println("Invalid username or password.");
                    return false;
                }
            }
            case 2 -> {
                System.out.print("Enter a username: ");
                String username = scanner.nextLine();
                if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
                    System.out.println("Username already exists.");
                    return false;
                }
                System.out.print("Enter a password: ");
                String password = scanner.nextLine();
                System.out.print("Are you an admin? (yes/no): ");
                boolean isAdmin = scanner.nextLine().trim().equalsIgnoreCase("yes");
                currentUser = new User(username, password, isAdmin);
                users.add(currentUser);
                System.out.println("Registration successful.");
            }
            case 3 -> {
                currentUser = new User("guest", "", false);
                System.out.println("Continuing as guest.");
            }
            default -> {
                System.out.println("Invalid option.");
                return false;
            }
        }
        return true;
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

    private static void loadUsers() {
        File file = new File(USERS_FILE);
        if (!file.exists()) {
            System.out.println("Users file not found. Creating a new one.");
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating users file: " + e.getMessage());
            }
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    users.add(new User(parts[0].trim(), parts[1].trim(), Boolean.parseBoolean(parts[2].trim())));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading users file: " + e.getMessage());
        }
    }

    private static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (User user : users) {
                bw.write(user.toCSV());
                bw.newLine();
            }
            System.out.println("Users saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
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

    private static void searchAndFilter(Scanner scanner) {
        System.out.print("Enter a keyword to search (name/location/cuisine): ");
        String keyword = scanner.nextLine().trim().toLowerCase();
        catalog.stream().filter(r -> r.name.toLowerCase().contains(keyword) || r.location.toLowerCase().contains(keyword) || r.cuisine.toLowerCase().contains(keyword))
                .forEach(System.out::println);
    }

    private static void findRestaurantLocation(Scanner scanner) {
        System.out.print("Enter your city: ");
        String city = scanner.nextLine().trim().toLowerCase();

        List<Restaurant> results = new ArrayList<>();
        for (Restaurant restaurant : catalog) {
            if (restaurant.location.toLowerCase().contains(city)) {
                results.add(restaurant);
            }
        }

        if (results.isEmpty()) {
            System.out.println("No restaurants found in your area.");
        } else {
            System.out.println("Closest restaurant(s):");
            for (Restaurant r : results) {
                System.out.println(r.name + " - " + r.location);
            }
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

    static class User {
        private String username, password;
        private boolean isAdmin;

        public User(String username, String password, boolean isAdmin) {
            this.username = username;
            this.password = password;
            this.isAdmin = isAdmin;
        }

        public String getUsername() { return username; }
        public String getPassword() { return password; }
        public boolean isAdmin() { return isAdmin; }

        public String toCSV() { return username + "," + password + "," + isAdmin; }
    }
}