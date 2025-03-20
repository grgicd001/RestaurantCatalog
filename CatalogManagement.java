
import java.io.*;
import java.util.*;

public class CatalogManagement {

    private static final String FILE_NAME = "catalog.csv";
    private static List<Item> catalog = new ArrayList<>();

    public static void main(String[] args) {
        loadCatalog();
        Scanner scanner = new Scanner(System.in);

        if (!authenticateUser(scanner)) {
            System.out.println("Authentication failed. Exiting.");
            return;
        }

        boolean running = true;

        while (running) {
            System.out.println("\nCatalog Management System");
            System.out.println("1. View Items");
            System.out.println("2. Add Item");
            System.out.println("3. Edit Item");
            System.out.println("4. Save and Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewItems();
                case 2 -> addItem(scanner);
                case 3 -> editItem(scanner);
                case 4 -> {
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
                if (parts.length == 3) { 
                    catalog.add(new Item(parts[0].trim(), parts[1].trim(), parts[2].trim()));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading catalog file: " + e.getMessage());
        }
    }

    private static void viewItems() {
        if (catalog.isEmpty()) {
            System.out.println("No items in the catalog.");
            return;
        }

        System.out.println("\nCatalog Items:");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.println((i + 1) + ". " + catalog.get(i));
        }
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Enter item ID: ");
        String id = scanner.nextLine().trim();

        if (catalog.stream().anyMatch(item -> item.id.equals(id))) {
            System.out.println("An item with this ID already exists. Please use a unique ID.");
            return;
        }

        System.out.print("Enter item name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter item description: ");
        String description = scanner.nextLine().trim();

        if (id.trim().isEmpty() || name.trim().isEmpty() || description.trim().isEmpty()) {
            System.out.println("ERROR: All fields are required.");
            return;
        }

        catalog.add(new Item(id, name, description));
        System.out.println("Item added successfully.");
    }

    private static void editItem(Scanner scanner) {
        if (catalog.isEmpty()) {
            System.out.println("The catalog is empty. Nothing to edit.");
            return;
        }

        viewItems();

        System.out.print("Enter the number of the item to edit: ");
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
            System.out.println("Invalid item number.");
            return;
        }

        Item item = catalog.get(index);

        System.out.print("Enter new name (current: " + item.getName() + "): ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter new description (current: " + item.getDescription() + "): ");
        String description = scanner.nextLine().trim();

       
        if (name.isEmpty() && description.isEmpty()) {
            System.out.println("Error: At least one field (Name or Description) must be updated.");
            return;
        }

       
        if (!name.isEmpty()) {
            item.setName(name);
        }
        if (!description.isEmpty()) {
            item.setDescription(description);
        }

        System.out.println("Item updated successfully.");
    }

    private static void addImage(File img, String id, boolean cover) {
        InputStream is = null;
        OutputStream os = null;

        //create directory if it doesn't exist
        File resDir = new File("/RestaurantCatalog/images/"+id);
        if (!resDir.exists()) {
            resDir.mkdirs();
        }

        try {
            String fileName;
            if (cover) { //if the user wants the image to be the cover image, rename it to cover.
                String ext = img.getName().substring(img.getName().lastIndexOf("."));
                fileName = "cover."+ext;
            } else {
                fileName = img.getName();
            }

            File destFile = new File(resDir, fileName);

            is = new FileInputStream(img);
            os = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.err.println("Error copying image: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                System.err.println("Error closing input stream: " + e.getMessage());
            }

            try {
                if (os != null) os.close();
            } catch (IOException e) {
                System.err.println("Error closing output stream: " + e.getMessage());
            }
        }
    }

    private static void saveCatalog() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Item item : catalog) {
                bw.write(item.toCSV());
                bw.newLine();
            }
            System.out.println("Catalog saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving catalog: " + e.getMessage());
        }
    }

    static class Item {
        private String id;
        private String name;
        private String description;

        public Item(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String toCSV() {
            return id + "," + name + "," + description;
        }

        @Override
        public String toString() {
            return "ID: " + id + ", Name: " + name + ", Description: " + description;
        }
    }
}
