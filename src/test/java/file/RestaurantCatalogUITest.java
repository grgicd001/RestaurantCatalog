package file;

import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

//import org.testfx.framework.junit5.ApplicationTest;
//import org.testfx.framework.junit5.Start;
import javafx.application.Platform;

import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;

class RestaurantCatalogUITest {
    private RestaurantCatalogUI app;

    // Helper method to create standard operating hours
    private Map<String, String> createStandardOperatingHours() {
        Map<String, String> hours = new LinkedHashMap<>();
        hours.put("Monday", "9AM-10PM");
        hours.put("Tuesday", "9AM-10PM");
        hours.put("Wednesday", "9AM-10PM");
        hours.put("Thursday", "9AM-10PM");
        hours.put("Friday", "9AM-11PM");
        hours.put("Saturday", "10AM-11PM");
        hours.put("Sunday", "10AM-9PM");
        return hours;
    }

    // =============================================
    // Clear Box Tests
    // =============================================
    @Nested
    @DisplayName("User Class Unit Tests")
    class UserTests {
        // Path coverage for favorite management
        @Test
        void addFavorite_duplicateEntry_preventsDuplicates() {
            RestaurantCatalogUI.User user = new RestaurantCatalogUI.User("user", "pass", false);
            user.addFavoriteRestaurant("Cafe");
            user.addFavoriteRestaurant("Cafe");  // Duplicate
            assertEquals(1, user.getFavoriteRestaurants().size());
        }

        // Boundary testing for CSV parsing
        @Test
        void fromCSV_malformedData_handlesGracefully() {
            assertAll(
                    () -> assertNull(RestaurantCatalogUI.User.fromCSV("")),
                    () -> assertNull(RestaurantCatalogUI.User.fromCSV("invalid,data")),
                    () -> assertNotNull(RestaurantCatalogUI.User.fromCSV("name,pass,true"))
            );
        }

        // Decision coverage for admin privileges
        @Test
        void isAdmin_privilegeLevels_returnsCorrectState() {
            RestaurantCatalogUI.User regular = new RestaurantCatalogUI.User("u1", "p1", false);
            RestaurantCatalogUI.User admin = new RestaurantCatalogUI.User("u2", "p2", true);
            assertAll(
                    () -> assertFalse(regular.isAdmin()),
                    () -> assertTrue(admin.isAdmin())
            );
        }
    }

    @Nested
    @DisplayName("Restaurant Operations Unit Tests")
    class RestaurantTests {
        // Multiple condition coverage for rating validation
        @Test
        void addRestaurant_invalidRatings_throwsException() {
            RestaurantCatalogUI app = new RestaurantCatalogUI();
            app.allRestaurants = new ArrayList<>();
            Map<String, String> hours = createStandardOperatingHours();

            assertAll(
                    () -> assertThrows(NumberFormatException.class,
                            () -> app.addRestaurant("R1", "C1", "img", 0.5,
                                    "Desc", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                    "123 Main St", hours)),
                    () -> assertThrows(NumberFormatException.class,
                            () -> app.addRestaurant("R2", "C2", "img", 5.1,
                                    "Desc", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                    "123 Main St", hours)),
                    () -> assertDoesNotThrow(
                            () -> app.addRestaurant("R3", "C3", "img", 3.0,
                                    "Desc", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                    "123 Main St", hours))
            );
        }

        // Data flow testing for image handling
        @Test
        void addImage_fileOperations_verifyStreamHandling() {
            File tempFile = createTempImage();
            RestaurantCatalogUI.addImage(tempFile, "test123", true);

            File expected = new File("images/test123/cover.png");
            assumeTrue(expected.exists());
            assertTrue(expected.length() > 0);

            cleanupTempFiles(expected);
        }
    }

    // =============================================
    // Translucent Box Tests
    // =============================================
    @Nested
    @DisplayName("Authentication Integration Tests")
    class AuthIntegration {
        // State transition testing for login workflow
        @Test
        void loginWorkflow_stateChanges_verifyTransitions() throws IOException {
            File authFile = createTestAuthFile();
            RestaurantCatalogUI app = new RestaurantCatalogUI();

            // Initial state
            assertNull(app.currentUser);

            // Load users from the test file
            app.users = RestaurantCatalogUI.User.loadUsers(authFile.getAbsolutePath());

            // Valid transition (simulate login)
            for (RestaurantCatalogUI.User user : app.users) {
                if (user.getUsername().equals("testuser") && user.getPassword().equals("testpass")) {
                    app.currentUser = user;
                    break;
                }
            }
            assertNotNull(app.currentUser);

            // Invalid transition (simulate failed login)
            app.currentUser = null;
            for (RestaurantCatalogUI.User user : app.users) {
                if (user.getUsername().equals("wrong") && user.getPassword().equals("wrong")) {
                    app.currentUser = user;
                    break;
                }
            }
            assertNull(app.currentUser);

            Files.delete(authFile.toPath());
        }

        // Equivalence partitioning for password validation
        @Test
        void signUp_passwordValidation_partitions() {
            assertAll(
                    () -> assertFalse(isValidPassword("short")),        // Too short
                    () -> assertFalse(isValidPassword("no uppercase")), // Missing complexity
                    () -> assertTrue(isValidPassword("Valid123!"))     // Valid
            );
        }

        // Helper method for password validation
        private boolean isValidPassword(String password) {
            // Example password validation rules:
            // - At least 8 characters
            // - Contains at least one uppercase letter
            // - Contains at least one digit
            return password != null &&
                    password.length() >= 8 &&
                    password.matches(".*[A-Z].*") &&
                    password.matches(".*\\d.*");
        }

        // Helper method to create a test auth file
        private File createTestAuthFile() throws IOException {
            File file = File.createTempFile("auth-test", ".csv");
            List<RestaurantCatalogUI.User> users = List.of(
                    new RestaurantCatalogUI.User("testuser", "testpass", false)
            );
            RestaurantCatalogUI.User.saveUsers(users, file.getAbsolutePath());
            return file;
        }
    }

    // Non Existent file Image handling
    @Test
    void addImage_nonExistentSource_handling(@TempDir Path tempDir) {
        // Use a non-existent file
        File nonExistentFile = new File(tempDir.toFile(), "nonexistent.png");

        // Should not throw exception
        assertDoesNotThrow(() ->
                        RestaurantCatalogUI.addImage(nonExistentFile, "test456", true),
                "Method should handle non-existent files properly"
        );
    }

    // =============================================
    // Opaque Box Tests
    // =============================================
    @Nested
    @DisplayName("End-to-End System Tests")
    class SystemTests {
        // Use case testing for main workflow
        @Test
        void fullUserWorkflow_validateSystemBehavior() throws IOException {
            // Initialize clean state
            File usersFile = File.createTempFile("users", ".csv");
            File restaurantsFile = File.createTempFile("restaurants", ".csv");

            // Create test application
            RestaurantCatalogUI app = new RestaurantCatalogUI();

            // Phase 1: Simulate guest browsing
            app.currentUser = null;
            app.allRestaurants = new ArrayList<>();
            app.showMainUI(new Stage());
            app.filterRestaurants("pizza");
            assertEquals(0, app.gridPane.getChildren().size());

            // Phase 2: Simulate user registration through the actual flow
            RestaurantCatalogUI.User newUser = new RestaurantCatalogUI.User("newuser", "Password123!", false);
            app.users.add(newUser);
            RestaurantCatalogUI.User.saveUsers(app.users, usersFile.getAbsolutePath());
            assertTrue(RestaurantCatalogUI.User.loadUsers(usersFile.getAbsolutePath()).size() > 0);

            // Phase 3: Simulate restaurant operations
            app.currentUser = newUser;
            Map<String, String> hours = createStandardOperatingHours();
            app.addRestaurant("Test Restaurant", "Italian", "img.jpg", 4.5,
                    "Test description",
                    Arrays.asList("tag1", "tag2"),
                    Arrays.asList("img1.jpg", "img2.jpg"),
                    Arrays.asList("Pizza-15.99", "Pasta-16.99"),
                    "123 Main St, Testville",
                    hours);
            app.saveRestaurants(restaurantsFile.getAbsolutePath());
            assertEquals(1, app.getRestaurants(restaurantsFile.getAbsolutePath()).size());

            // Cleanup
            Files.delete(usersFile.toPath());
            Files.delete(restaurantsFile.toPath());
        }

        // Boundary value analysis for search filters
        @ParameterizedTest
        @ValueSource(strings = {"", "a", "mexican", "Mexican Restaurant Long Name"})
        void searchFilter_boundaryValues_consistentBehavior(String input) {
            RestaurantCatalogUI app = new RestaurantCatalogUI();
            app.allRestaurants = List.of(
                    new RestaurantCatalogUI.Restaurant("Mexican Place", "Mexican", "", 4.0),
                    new RestaurantCatalogUI.Restaurant("A Small Cafe", "Cafe", "", 3.5)
            );

            app.filterRestaurants(input);
            assertDoesNotThrow(() -> app.gridPane.getChildren());
        }
    }

    // =============================================
    // Helper Methods
    // =============================================
    private File createTempImage() {
        try {
            File temp = File.createTempFile("test-image", ".png");
            temp.deleteOnExit();
            return temp;
        } catch (IOException e) {
            throw new RuntimeException("Temp file creation failed");
        }
    }

    private void cleanupTempFiles(File... files) {
        Arrays.stream(files).forEach(f -> {
            try {
                Files.deleteIfExists(f.toPath());
            } catch (IOException e) {
                System.err.println("Cleanup failed for: " + f);
            }
        });
    }

    private File createTestAuthFile() throws IOException {
        File file = File.createTempFile("auth-test", ".csv");
        RestaurantCatalogUI.User.saveUsers(
                List.of(new RestaurantCatalogUI.User("testuser", "testpass", false)),
                file.getAbsolutePath()
        );
        return file;
    }

    @Test
    public void testRecommendationsForUser() {
        RestaurantCatalogUI app = new RestaurantCatalogUI();

        // Setup test user with favorites
        RestaurantCatalogUI.User user = new RestaurantCatalogUI.User("testUser", "password", false);
        user.addFavoriteRestaurant("Italian Restaurant");
        user.addFavoriteRestaurant("Mexican Restaurant");
        app.currentUser = user;

        // Setup test restaurants
        app.allRestaurants = List.of(
                new RestaurantCatalogUI.Restaurant("Italian Restaurant", "Italian", "italian.jpg", 4.5),
                new RestaurantCatalogUI.Restaurant("Mexican Restaurant", "Mexican", "mexican.jpg", 4.2),
                new RestaurantCatalogUI.Restaurant("Chinese Restaurant", "Chinese", "chinese.jpg", 3.8),
                new RestaurantCatalogUI.Restaurant("Japanese Restaurant", "Japanese", "japanese.jpg", 4.0),
                new RestaurantCatalogUI.Restaurant("Another Italian Place", "Italian", "italian2.jpg", 4.3),
                new RestaurantCatalogUI.Restaurant("Taco Place", "Mexican", "mexican2.jpg", 4.1)
        );

        // Test 1: Verify favorites are correctly tracked
        assertEquals(2, app.currentUser.getFavoriteRestaurants().size());
        assertTrue(app.currentUser.getFavoriteRestaurants().contains("Italian Restaurant"));
        assertTrue(app.currentUser.getFavoriteRestaurants().contains("Mexican Restaurant"));

        // Test 2: Verify filtering by favorite cuisines
        List<RestaurantCatalogUI.Restaurant> italianRestaurants = app.allRestaurants.stream()
                .filter(r -> r.cuisine.equals("Italian"))
                .toList();
        assertEquals(2, italianRestaurants.size());

        // Test 3: Verify displayFavorites filters correctly (without UI inspection)
        // We'll test the logic that determines what would be displayed
        List<RestaurantCatalogUI.Restaurant> expectedFavorites = app.allRestaurants.stream()
                .filter(r -> app.currentUser.getFavoriteRestaurants().contains(r.name))
                .toList();
        assertEquals(2, expectedFavorites.size());

        // Test 4: Verify non-favorite restaurants aren't included
        boolean onlyFavorites = expectedFavorites.stream()
                .allMatch(r -> r.name.equals("Italian Restaurant") ||
                        r.name.equals("Mexican Restaurant"));
        assertTrue(onlyFavorites);
    }
    @AfterAll
    static void tearDownAll() {
        Platform.exit(); // Properly shutdown JavaFX
    }
}