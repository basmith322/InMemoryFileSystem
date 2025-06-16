package filesystem;

import filesystem.entities.Drive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileSystemDeleteOperationsTest extends BaseFileSystemTest {

    @Test
    void deleteDrive_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.delete("C");
        assertNull(fs.findDrive("C"));
    }

    @Test
    void deleteFolder_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");
        fs.delete("C\\Documents");

        Drive drive = fs.findDrive("C");
        assertNull(drive.getEntity("Documents"));
    }

    @Test
    void deleteFolder_withContents_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");
        fs.create("TextFile", "note.txt", "C\\Documents");
        fs.delete("C\\Documents");

        assertNull(fs.findEntity("C\\Documents\\note.txt"));
    }

    @Test
    void delete_nonexistentPath_shouldThrowException() {
        fs.create("Drive", "C", null);
        assertThrows(IllegalArgumentException.class,
                () -> fs.delete("C\\NonExistent"));
    }
}
