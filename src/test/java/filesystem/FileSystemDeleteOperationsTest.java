package filesystem;

import filesystem.entities.Drive;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileSystemDeleteOperationsTest extends BaseFileSystemTest {

    @Test
    void deleteDrive_shouldSucceed() {
        createBasicStructure();
        fs.delete("C");
        assertNull(fs.findDrive("C"));
    }

    @Test
    void deleteFolder_shouldSucceed() {
        createBasicStructure();
        
        fs.delete("C\\Documents");

        Drive drive = fs.findDrive("C");
        assertNull(drive.getEntity("Documents"));
    }

    @Test
    void deleteFolder_withContents_shouldSucceed() {
        createBasicStructure();
        
        fs.create("TextFile", "note.txt", "C\\Documents");
        fs.delete("C\\Documents");

        assertNull(fs.findEntity("C\\Documents\\note.txt"));
    }

    @Test
    void delete_nonexistentPath_shouldThrowException() {
        createBasicStructure();
        assertThrows(IllegalArgumentException.class,
                () -> fs.delete("C\\NonExistent"));
    }
}
