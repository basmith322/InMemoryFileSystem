package filesystem;

import filesystem.entities.TextFile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileSystemWriteOperationsTest extends BaseFileSystemTest {

    @Test
    void writeToFile_shouldSucceed() {
        fs.create("Drive", "C", null);
        fs.create("TextFile", "note.txt", "C");

        fs.writeToFile("C\\note.txt", "Hello, World!");

        TextFile file = (TextFile) fs.findEntity("C\\note.txt");
        assertEquals("Hello, World!", file.getContent());
    }

    @Test
    void writeToFile_nonexistentFile_shouldThrowException() {
        fs.create("Drive", "C", null);

        assertThrows(IllegalArgumentException.class,
                () -> fs.writeToFile("C\\note.txt", "Hello"));
    }

    @Test
    void writeToFile_toNonTextFile_shouldThrowException() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");

        assertThrows(IllegalArgumentException.class,
                () -> fs.writeToFile("C\\Documents", "Hello"));
    }
}
