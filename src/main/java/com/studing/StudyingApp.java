package com.studing;

import com.studing.users.entity.User;
import com.studing.users.service.UserService;
import com.studing.users.service.UserRoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class StudyingApp implements CommandLineRunner {
    private final UserService userService;
    private final UserRoleService userRoleService;

    public StudyingApp(UserService userService, UserRoleService userRoleService) {
        this.userService = userService;
        this.userRoleService = userRoleService;
    }

    @Override
    public void run(String... args) {
        System.out.println("=== USER MANAGEMENT SYSTEM ===");
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    showAllUsers();
                    break;
                case "2":
                    createNewUser(scanner);
                    break;
                case "3":
                    updateUser(scanner);
                    break;
                case "4":
                    deleteUser(scanner);
                    break;
                case "5":
                    showUserDetails(scanner);
                    break;
                case "6":
                    addRoleToUser(scanner);
                    break;
                case "7":
                    checkUserRole(scanner);
                    break;
                case "8":
                    showUsersWithRole(scanner);
                    break;
                case "9":
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private void printMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Show all users");
        System.out.println("2. Create new user");
        System.out.println("3. Update user");
        System.out.println("4. Delete user");
        System.out.println("5. Show user details");
        System.out.println("6. Add role to user");
        System.out.println("7. Check user role");
        System.out.println("8. Show users with role");
        System.out.println("9. Exit");
        System.out.print("Select option (1-9): ");
    }

    private void showAllUsers() {
        System.out.println("\n=== ALL USERS ===");
        List<User> users = userService.findAll();

        if (users.isEmpty()) {
            System.out.println("No users found in database.");
            return;
        }

        System.out.println("Total users: " + users.size());
        System.out.println("+----+------------+----------------------+-----+--------+---------------------+---------------------+");
        System.out.println("| ID | Username   | Name                 | Age | Active | Created             | Last Updated        |");
        System.out.println("+----+------------+----------------------+-----+--------+---------------------+---------------------+");

        for (User user : users) {
            String fullName = user.getName() + " " + user.getSurname();
            String active = user.getActive() ? "Yes" : "No";
            String created = user.getInsertedAt() != null ? user.getInsertedAt().toString() : "N/A";
            String updated = user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : "N/A";

            System.out.printf("| %-2d | %-10s | %-20s | %-3d | %-6s | %-19s | %-19s |\n",
                    user.getId(), user.getUsername(), fullName, user.getAge(),
                    active, created, updated);
        }
        System.out.println("+----+------------+----------------------+-----+--------+---------------------+---------------------+");
    }

    private void createNewUser(Scanner scanner) {
        System.out.println("\n=== CREATE NEW USER ===");

        try {
            System.out.print("Username: ");
            String username = scanner.nextLine();

            // Check if username already exists
            if (userService.existsByUsername(username)) {
                System.out.println("Error: Username '" + username + "' already exists!");
                return;
            }

            System.out.print("First Name: ");
            String name = scanner.nextLine();

            System.out.print("Last Name: ");
            String surname = scanner.nextLine();

            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine());

            System.out.print("Password: ");
            String password = scanner.nextLine();

            // Create new user
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setName(name);
            newUser.setSurname(surname);
            newUser.setAge(age);
            newUser.setPassword(password);
            newUser.setActive(true); // По умолчанию активен

            // Save user
            User savedUser = userService.save(newUser);
            System.out.println("\nSUCCESS: User created!");
            System.out.println("User ID: " + savedUser.getId());
            System.out.println("Username: " + savedUser.getUsername());
            System.out.println("Active: " + (savedUser.getActive() ? "Yes" : "No"));

        } catch (NumberFormatException e) {
            System.out.println("Error: Age must be a number!");
        } catch (Exception e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }

    private void updateUser(Scanner scanner) {
        System.out.println("\n=== UPDATE USER ===");

        System.out.print("Enter username to update: ");
        String username = scanner.nextLine();

        userService.findByUsername(username).ifPresentOrElse(
                user -> {
                    System.out.println("\nCurrent user data:");
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Name: " + user.getName());
                    System.out.println("Surname: " + user.getSurname());
                    System.out.println("Age: " + user.getAge());
                    System.out.println("Active: " + (user.getActive() ? "Yes" : "No"));
                    System.out.println("Created: " + user.getInsertedAt());
                    System.out.println("Last Updated: " + user.getUpdatedAt());
                    System.out.println();

                    System.out.println("Enter new data (press Enter to keep current value):");

                    System.out.print("New First Name [" + user.getName() + "]: ");
                    String newName = scanner.nextLine();
                    if (!newName.isEmpty()) {
                        user.setName(newName);
                    }

                    System.out.print("New Last Name [" + user.getSurname() + "]: ");
                    String newSurname = scanner.nextLine();
                    if (!newSurname.isEmpty()) {
                        user.setSurname(newSurname);
                    }

                    System.out.print("New Age [" + user.getAge() + "]: ");
                    String ageInput = scanner.nextLine();
                    if (!ageInput.isEmpty()) {
                        try {
                            user.setAge(Integer.parseInt(ageInput));
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Age must be a number! Keeping old value.");
                        }
                    }

                    System.out.print("New Password [*****]: ");
                    String newPassword = scanner.nextLine();
                    if (!newPassword.isEmpty()) {
                        user.setPassword(newPassword);
                    }

                    System.out.print("Active? (yes/no) [" + (user.getActive() ? "yes" : "no") + "]: ");
                    String activeInput = scanner.nextLine();
                    if (!activeInput.isEmpty()) {
                        user.setActive(activeInput.equalsIgnoreCase("yes"));
                    }

                    // Update user
                    User updatedUser = userService.update(user);
                    System.out.println("\nSUCCESS: User updated!");
                    System.out.println("Updated at: " + updatedUser.getUpdatedAt());

                },
                () -> System.out.println("Error: User '" + username + "' not found!")
        );
    }

    private void deleteUser(Scanner scanner) {
        System.out.println("\n=== DELETE USER ===");

        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();

        userService.findByUsername(username).ifPresentOrElse(
                user -> {
                    System.out.println("\nUser to delete:");
                    System.out.println("ID: " + user.getId());
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Name: " + user.getName() + " " + user.getSurname());
                    System.out.println("Active: " + (user.getActive() ? "Yes" : "No"));
                    System.out.println("Created: " + user.getInsertedAt());
                    System.out.println();

                    System.out.print("Are you sure? (yes/no): ");
                    String confirmation = scanner.nextLine();

                    if (confirmation.equalsIgnoreCase("yes")) {
                        // Мягкое удаление (деактивация)
                        user.setActive(false);
                        userService.update(user);
                        System.out.println("SUCCESS: User deactivated (soft delete)!");
                    } else {
                        System.out.println("Deletion cancelled.");
                    }
                },
                () -> System.out.println("Error: User '" + username + "' not found!")
        );
    }

    private void showUserDetails(Scanner scanner) {
        System.out.println("\n=== USER DETAILS ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        userService.findByUsername(username).ifPresentOrElse(
                user -> {
                    System.out.println("\n=== USER INFORMATION ===");
                    System.out.println("ID: " + user.getId());
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Full Name: " + user.getName() + " " + user.getSurname());
                    System.out.println("Age: " + user.getAge());
                    System.out.println("Active: " + (user.getActive() ? "Yes" : "No"));
                    System.out.println("Created: " + user.getInsertedAt());
                    System.out.println("Last Updated: " + user.getUpdatedAt());
                    System.out.println();

                    // Show user roles
                    try {
                        List<com.studing.users.entity.Role> roles = userRoleService.getUserRoles(username);
                        if (!roles.isEmpty()) {
                            System.out.println("=== ROLES ===");
                            for (com.studing.users.entity.Role role : roles) {
                                System.out.println("- " + role.getName() +
                                        (role.getDescription() != null ?
                                                " (" + role.getDescription() + ")" : ""));
                            }
                        } else {
                            System.out.println("No roles assigned to this user.");
                        }
                    } catch (Exception e) {
                        System.out.println("No roles assigned to this user.");
                    }

                },
                () -> System.out.println("Error: User '" + username + "' not found!")
        );
    }

    private void addRoleToUser(Scanner scanner) {
        System.out.println("\n=== ADD ROLE TO USER ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter role name: ");
        String roleName = scanner.nextLine();

        System.out.println("\nProcessing...");

        try {
            userRoleService.addRoleToUser(username, roleName);
            System.out.println("SUCCESS: Role '" + roleName + "' added to user '" + username + "'");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());

            // Show suggestions
            if (e.getMessage().contains("User not found")) {
                System.out.println("\nAvailable users:");
                userService.findAll().forEach(user ->
                        System.out.println("- " + user.getUsername()));
            }
        }
    }

    private void checkUserRole(Scanner scanner) {
        System.out.println("\n=== CHECK USER ROLE ===");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter role name: ");
        String roleName = scanner.nextLine();

        boolean hasRole = userRoleService.hasUserRole(username, roleName);

        System.out.println("\n=== CHECK RESULT ===");
        System.out.println("User: " + username);
        System.out.println("Role: " + roleName);
        System.out.println("Result: " + (hasRole ? "✅ HAS this role" : "❌ DOES NOT HAVE this role"));
    }

    private void showUsersWithRole(Scanner scanner) {
        System.out.println("\n=== USERS WITH ROLE ===");

        System.out.print("Enter role name: ");
        String roleName = scanner.nextLine();

        System.out.println("\nSearching for users with role: " + roleName);

        try {
            List<User> users = userRoleService.getUsersWithRole(roleName);

            if (users.isEmpty()) {
                System.out.println("No users found with role: " + roleName);
            } else {
                System.out.println("Found " + users.size() + " user(s):");
                System.out.println("+------------+----------------------+--------+---------------------+");
                System.out.println("| Username   | Name                 | Active | Created             |");
                System.out.println("+------------+----------------------+--------+---------------------+");

                for (User user : users) {
                    String fullName = user.getName() + " " + user.getSurname();
                    String active = user.getActive() ? "Yes" : "No";
                    String created = user.getInsertedAt() != null ?
                            user.getInsertedAt().toString().substring(0, 16) : "N/A";

                    System.out.printf("| %-10s | %-20s | %-6s | %-19s |\n",
                            user.getUsername(), fullName, active, created);
                }
                System.out.println("+------------+----------------------+--------+---------------------+");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
}