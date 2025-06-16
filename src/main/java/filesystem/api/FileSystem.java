package filesystem;

import java.util.HashMap;
import java.util.Map;

public class FileSystem {
    private final Map<String, Drive> drives = new HashMap<>();

    public void create(String type, String name, String parentPath) {
        if (type.equals("Drive")) {
            if (drives.containsKey(name)) {
                throw new IllegalArgumentException("Drive with that name already exists.");
            }
            drives.put(name, new Drive(name));
            return;
        }

        Container parent = getContainerAtPath(parentPath);
        if (parent == null) {
            throw new IllegalArgumentException("Parent path not found: " + parentPath);
        }

        if (parent.hasEntity(name)) {
            throw new IllegalArgumentException("Entity with that name already exists in " + parentPath);
        }

        FileSystemEntity newEntity = switch (type) {
            case "Folder" -> new Folder(name);
            case "TextFile" -> new TextFile(name);
            case "ZipFile" -> new ZipFile(name);
            default -> throw new IllegalArgumentException("Unknown entity type: " + type);
        };

        parent.addEntity(newEntity);
    }

    public void delete(String path) {
        if (!path.contains("\\")) {
            // Deleting a drive
            if (!drives.containsKey(path)) {
                throw new IllegalArgumentException("Drive not found: " + path);
            }
            drives.remove(path);
            return;
        }

        String parentPath = getParentPath(path);
        String name = getNameFromPath(path);
        Container parent = getContainerAtPath(parentPath);

        if (parent == null || !parent.hasEntity(name)) {
            throw new IllegalArgumentException("Path not found: " + path);
        }

        parent.removeEntity(name);
    }

    public void move(String sourcePath, String destPath) {
        // Cannot move drives
        if (!sourcePath.contains("\\")) {
            throw new IllegalArgumentException("Cannot move a drive");
        }

        FileSystemEntity source = findEntity(sourcePath);
        if (source == null) {
            throw new IllegalArgumentException("Source path not found: " + sourcePath);
        }

        Container destParent = getContainerAtPath(destPath);
        if (destParent == null) {
            throw new IllegalArgumentException("Destination path not found: " + destPath);
        }

        String sourceName = getNameFromPath(sourcePath);
        if (destParent.hasEntity(sourceName)) {
            throw new IllegalArgumentException("Entity with same name already exists at destination");
        }

        // Remove from old parent
        String sourceParentPath = getParentPath(sourcePath);
        Container sourceParent = getContainerAtPath(sourceParentPath);
        sourceParent.removeEntity(sourceName);

        // Add to new parent
        destParent.addEntity(source);
    }

    public void writeToFile(String path, String content) {
        FileSystemEntity entity = findEntity(path);
        if (entity == null) {
            throw new IllegalArgumentException("File not found: " + path);
        }

        if (!(entity instanceof TextFile)) {
            throw new IllegalArgumentException("Entity is not a text file: " + path);
        }

        ((TextFile) entity).setContent(content);
    }

    // Helper methods
    public Drive findDrive(String name) {
        return drives.get(name);
    }

    public FileSystemEntity findEntity(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        if (!path.contains("\\")) return drives.get(path);

        String[] parts = path.split("\\\\");
        Drive drive = drives.get(parts[0]);
        if (drive == null) return null;

        FileSystemEntity current = drive;
        for (int i = 1; i < parts.length; i++) {
            if (!(current instanceof Container)) {
                return null;
            }
            current = ((Container) current).getEntity(parts[i]);
            if (current == null) return null;
        }

        return current;
    }

    private Container getContainerAtPath(String path) {
        if (path == null) return null;

        FileSystemEntity entity = findEntity(path);
        if (entity instanceof Container) {
            return (Container) entity;
        }
        return null;
    }

    private String getParentPath(String path) {
        int lastBackslash = path.lastIndexOf('\\');
        return lastBackslash == -1 ? null : path.substring(0, lastBackslash);
    }

    private String getNameFromPath(String path) {
        int lastBackslash = path.lastIndexOf('\\');
        return lastBackslash == -1 ? path : path.substring(lastBackslash + 1);
    }
}