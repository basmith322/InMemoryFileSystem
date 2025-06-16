package filesystem.containers;

import filesystem.api.FileSystemEntity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractContainer extends FileSystemEntity implements Container {
    protected final Map<String, FileSystemEntity> contents = new HashMap<>();

    public AbstractContainer(String name, String type) {
        super(name, type);
    }

    @Override
    public void addEntity(FileSystemEntity entity) {
        if (contents.containsKey(entity.getName())) {
            throw new IllegalArgumentException("Entity with name " + entity.getName() + " already exists");
        }
        contents.put(entity.getName(), entity);
        entity.setParent(this);
    }

    @Override
    public void removeEntity(String name) {
        FileSystemEntity removed = contents.remove(name);
        if (removed != null) {
            removed.setParent(null);
        }
    }

    @Override
    public FileSystemEntity getEntity(String name) {
        return contents.get(name);
    }

    @Override
    public Collection<FileSystemEntity> getContents() {
        return contents.values();
    }

    @Override
    public boolean hasEntity(String name) {
        return contents.containsKey(name);
    }
}