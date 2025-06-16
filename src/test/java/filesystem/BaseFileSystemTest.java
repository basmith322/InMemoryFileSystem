package filesystem;

import filesystem.api.FileSystem;
import org.junit.jupiter.api.BeforeEach;

abstract class BaseFileSystemTest {
    protected FileSystem fs;

    @BeforeEach
    void setUp() {
        fs = new FileSystem();
    }

    // Common helper methods
    protected void createBasicStructure() {
        fs.create("Drive", "C", null);
        fs.create("Folder", "Documents", "C");
    }
}
