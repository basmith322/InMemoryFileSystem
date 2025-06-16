package filesystem;

import filesystem.api.FileSystem;
import filesystem.api.FileSystemEntity;
import filesystem.entities.TextFile;

import java.util.Scanner;

public class FilesystemCLI {

    private static final FileSystem fs = new FileSystem();

    public static void main(String[] args) {
        System.out.println("=== In-Memory File System CLI ===");
        System.out.println("Type 'help' for commands, 'exit' to quit");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("fs> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            }

            String[] commandArgs = input.split("\\s+");
            processCommand(commandArgs);
        }

        scanner.close();
    }

    private static void processCommand(String[] args) {
        try {
            String command = args[0].toLowerCase();

            switch (command) {
                case "help":
                    showHelp();
                    break;
                case "create":
                    handleCreate(args);
                    break;
                case "delete":
                    handleDelete(args);
                    break;
                case "move":
                    handleMove(args);
                    break;
                case "write":
                    handleWrite(args);
                    break;
                case "read":
                    handleRead(args);
                    break;
                case "find":
                    handleFind(args);
                    break;
                default:
                    System.err.println("Unknown command: " + command + ". Type 'help' for available commands.");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void showHelp() {
        System.out.println("Available commands:");
        System.out.println("  create <type> <name> <parent_path>  - Create entity (Drive, Folder, TextFile, ZipFile)");
        System.out.println("  delete <path>                       - Delete entity");
        System.out.println("  move <source_path> <dest_path>      - Move entity");
        System.out.println("  write <path> <content>              - Write content to file");
        System.out.println("  read <path>                         - Read and display file content");
        System.out.println("  find <path>                         - Find and show entity info");
        System.out.println("  help                                - Show this help message");
        System.out.println("  exit                                - Exit the CLI");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  create Drive C null");
        System.out.println("  create Folder Documents C");
        System.out.println("  create TextFile note.txt C\\Documents");
        System.out.println("  write C\\Documents\\note.txt Hello World!");
        System.out.println("  read C\\Documents\\note.txt");
        System.out.println("  find C\\Documents\\note.txt");
    }

    private static void handleCreate(String[] args) {
        if (args.length != 4) {
            System.err.println("Usage: create <type> <name> <parent_path>");
            System.err.println("Types: Drive, Folder, TextFile, ZipFile");
            System.err.println("Use 'null' for parent_path when creating drives");
            return;
        }

        String type = args[1];
        String name = args[2];
        String parentPath = args[3].equals("null") ? null : args[3];

        fs.create(type, name, parentPath);
        System.out.println("✓ Created " + type + " '" + name + "' at " + (parentPath == null ? "root" : parentPath));
    }

    private static void handleDelete(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: delete <path>");
            return;
        }

        String path = args[1];
        fs.delete(path);
        System.out.println("✓ Deleted: " + path);
    }

    private static void handleMove(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: move <source_path> <dest_path>");
            return;
        }

        String sourcePath = args[1];
        String destPath = args[2];
        fs.move(sourcePath, destPath);
        System.out.println("✓ Moved " + sourcePath + " to " + destPath);
    }

    private static void handleWrite(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: write <path> <content>");
            return;
        }

        String path = args[1];
        // Join all remaining args as content (in case content has spaces)
        StringBuilder content = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            if (i > 2) content.append(" ");
            content.append(args[i]);
        }

        fs.writeToFile(path, content.toString());
        System.out.println("✓ Wrote content to: " + path);
    }

    private static void handleRead(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: read <path>");
            return;
        }

        String path = args[1];
        FileSystemEntity entity = fs.findEntity(path);

        if (entity == null) {
            System.out.println("✗ File not found: " + path);
            return;
        }

        if (!(entity instanceof TextFile textFile)) {
            System.out.println("✗ Cannot read: " + path + " is not a text file");
            return;
        }

        String content = textFile.getContent();

        System.out.println("--- Content of " + path + " ---");
        if (content.isEmpty()) {
            System.out.println("(empty file)");
        } else {
            System.out.println(content);
        }
        System.out.println("--- End of file ---");
    }

    private static void handleFind(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: find <path>");
            return;
        }

        String path = args[1];
        FileSystemEntity entity = fs.findEntity(path);

        if (entity == null) {
            System.out.println("✗ Not found: " + path);
        } else {
            System.out.println("✓ Found: " + entity.getClass().getSimpleName() + " '" + entity.getName() + "' at " + entity.getPath());
        }
    }
}