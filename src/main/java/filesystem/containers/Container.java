package filesystem.containers;

import filesystem.api.FileSystemEntity;

import java.util.Collection;

public interface Container {
    void addEntity(FileSystemEntity entity);

    void removeEntity(String name);

    FileSystemEntity getEntity(String name);

    Collection<FileSystemEntity> getContents();

    boolean hasEntity(String name);
}