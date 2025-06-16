package filesystem;

import filesystem.api.FileSystemEntity;
import filesystem.containers.Container;
import filesystem.entities.Drive;
import filesystem.entities.TextFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemCreateOperationsTest extends BaseFileSystemTest {

    @Test
    void createDrive_shouldSucceed() {
        createBasicStructure();

        Drive drive = fs.findDrive("C");
        assertNotNull(drive);
        assertEquals("C", drive.getName());
    }

    @Test
    void createDrive_withExistingName_shouldThrowException() {
        fs.create("Drive", "C", null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> fs.create("Drive", "C", null)
        );
        assertEquals("Drive with that name already exists.", exception.getMessage());
    }

    @Test
    void createFolder_inDrive_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");

        Drive drive = fs.findDrive("C");
        assertNotNull(drive.getEntity("Documents"));
        assertEquals("Folder", drive.getEntity("Documents").getType());
        assertEquals("C\\Documents", drive.getEntity("Documents").getPath());
    }

    @Test
    void createTextFile_inFolder_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");
        fs.create("TextFile", "note.txt", "C\\Documents");

        FileSystemEntity file = fs.findEntity("C\\Documents\\note.txt");
        assertNotNull(file);
        assertEquals("TextFile", file.getType());
        assertEquals("note.txt", file.getName());
        assertInstanceOf(TextFile.class, file);
    }

    @Test
    void createZipFile_inFolder_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");
        fs.create("ZipFile", "archive.zip", "C\\Documents");

        FileSystemEntity zipFile = fs.findEntity("C\\Documents\\archive.zip");
        assertNotNull(zipFile);
        assertEquals("ZipFile", zipFile.getType());
        assertInstanceOf(Container.class, zipFile);
    }

    @Test
    void createEntity_withInvalidParentPath_shouldThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> fs.create("Folder", "Documents", "X"));
    }

    @Test
    void createEntity_withDuplicateName_shouldThrowException() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");

        assertThrows(IllegalArgumentException.class,
                () -> fs.create("Folder", "Documents", "C"));
    }

    @Test
    void createEntity_inTextFile_shouldThrowException() {
        fs.create("Drive", "C", null);
        fs.create("TextFile", "note.txt", "C");

        assertThrows(IllegalArgumentException.class,
                () -> fs.create("Folder", "folder", "C\\note.txt"));
    }

    @Test
    void addEntity_directCall_withDuplicateName_shouldThrowException() {
        fs.create("Drive", "C", null);
        Drive drive = fs.findDrive("C");

        TextFile file1 = new TextFile("duplicate.txt");
        TextFile file2 = new TextFile("duplicate.txt");

        // First, add should succeed
        drive.addEntity(file1);

        // Second add with same name should throw exception
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> drive.addEntity(file2)
        );

        assertEquals("Entity with name duplicate.txt already exists", exception.getMessage());
    }


}
