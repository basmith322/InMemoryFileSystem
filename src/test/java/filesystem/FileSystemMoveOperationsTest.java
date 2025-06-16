package filesystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemMoveOperationsTest extends BaseFileSystemTest {

    @Test
    void moveFolder_betweenDrives_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("Drive", "D", null);
        fs.create("Folder", "Documents", "C");

        fs.move("C\\Documents", "D");

        assertNull(fs.findEntity("C\\Documents"));
        assertNotNull(fs.findEntity("D\\Documents"));
    }

    @Test
    void moveFile_toFolder_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");
        fs.create("Folder", "Backup", "C");
        fs.create("TextFile", "note.txt", "C\\Documents");

        fs.move("C\\Documents\\note.txt", "C\\Backup");

        assertNull(fs.findEntity("C\\Documents\\note.txt"));
        assertNotNull(fs.findEntity("C\\Backup\\note.txt"));
    }

    @Test
    void moveDrive_shouldThrowException() {
        fs.create("Drive", "C", null);
        fs.create("Drive", "D", null);

        assertThrows(IllegalArgumentException.class,
                () -> fs.move("C", "D"));
    }

    @Test
    void move_toNonexistentDestination_shouldThrowException() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");

        assertThrows(IllegalArgumentException.class,
                () -> fs.move("C\\Documents", "D"));
    }

    @Test
    void move_nonexistentSource_shouldThrowException() {
        fs.create("Drive", "C", null);

        assertThrows(IllegalArgumentException.class,
                () -> fs.move("C\\Documents", "C"));
    }
}
