package filesystem;

public abstract class FileSystemEntity {
    private final String name;
    private final String type;
    protected FileSystemEntity parent;

    public FileSystemEntity(String name, String type) {
        if (!name.matches("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)?$")) {
            throw new IllegalArgumentException("Name must be alphanumeric with optional extension");
        }
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getPath() {
        if (parent == null) {
            return name;
        }
        return parent.getPath() + "\\" + name;
    }

    public FileSystemEntity getParent() {
        return parent;
    }

    public void setParent(FileSystemEntity parent) {
        this.parent = parent;
    }
}