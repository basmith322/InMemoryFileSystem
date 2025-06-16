package filesystem;

import filesystem.api.FileSystemEntity;
import filesystem.entities.Drive;
import filesystem.entities.Folder;
import filesystem.entities.TextFile;
import filesystem.entities.ZipFile;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class FileSystemIntegrationTest extends BaseFileSystemTest {

    @Nested
    class PathTests {
        @Test
        void verifyPath_forNestedEntities() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");
            fs.create("Folder", "Work", "C\\Documents");
            fs.create("TextFile", "note.txt", "C\\Documents\\Work");

            FileSystemEntity file = fs.findEntity("C\\Documents\\Work\\note.txt");
            assertEquals("C\\Documents\\Work\\note.txt", file.getPath());
        }

        @Test
        void verifyPath_afterMove() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");
            fs.create("TextFile", "note.txt", "C\\Documents");
            fs.create("Folder", "Backup", "C");

            fs.move("C\\Documents\\note.txt", "C\\Backup");

            FileSystemEntity file = fs.findEntity("C\\Backup\\note.txt");
            assertEquals("C\\Backup\\note.txt", file.getPath());
        }
    }

    @Nested
    class ValidationTests {
        @Test
        void create_withNonAlphanumericName_shouldThrowException() {
            fs.create("Drive", "C", null);

            assertThrows(IllegalArgumentException.class,
                    () -> fs.create("Folder", "My Documents!", "C"));
        }

        @Test
        void create_withInvalidType_shouldThrowException() {
            fs.create("Drive", "C", null);

            assertThrows(IllegalArgumentException.class,
                    () -> fs.create("InvalidType", "test", "C"));
        }
    }

    @Nested
    class ContainerInterfaceTests {
        @Test
        void container_addEntity_shouldSetParent() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");

            Drive drive = fs.findDrive("C");
            Folder folder = (Folder) drive.getEntity("Documents");

            assertEquals(drive, folder.getParent());
        }

        @Test
        void container_removeEntity_shouldClearParent() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");

            Drive drive = fs.findDrive("C");
            Folder folder = (Folder) drive.getEntity("Documents");

            drive.removeEntity("Documents");
            assertNull(folder.getParent());
        }

        @Test
        void container_getContents_shouldReturnAllEntities() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");
            fs.create("TextFile", "note.txt", "C");
            fs.create("ZipFile", "archive.zip", "C");

            Drive drive = fs.findDrive("C");
            Collection<FileSystemEntity> contents = drive.getContents();

            assertEquals(3, contents.size());
        }

        @Test
        void container_hasEntity_shouldReturnCorrectBoolean() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");

            Drive drive = fs.findDrive("C");

            assertTrue(drive.hasEntity("Documents"));
            assertFalse(drive.hasEntity("NonExistent"));
        }
    }

    @Nested
    class EntityPropertiesTests {
        @Test
        void entity_getName_shouldReturnCorrectName() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");

            FileSystemEntity folder = fs.findEntity("C\\Documents");
            assertEquals("Documents", folder.getName());
        }

        @Test
        void entity_getType_shouldReturnCorrectType() {
            fs.create("Drive", "C", null);
            fs.create("ZipFile", "archive.zip", "C");

            FileSystemEntity zipFile = fs.findEntity("C\\archive.zip");
            assertEquals("ZipFile", zipFile.getType());
        }

        @Test
        void entity_getParent_shouldReturnCorrectParent() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");

            Drive drive = fs.findDrive("C");
            FileSystemEntity folder = fs.findEntity("C\\Documents");

            assertEquals(drive, folder.getParent());
        }

        @Test
        void drive_getParent_shouldReturnNull() {
            fs.create("Drive", "C", null);

            Drive drive = fs.findDrive("C");
            assertNull(drive.getParent());
        }
    }

    @Nested
    class TextFileContentTests {
        @Test
        void textFile_initialContent_shouldBeEmpty() {
            fs.create("Drive", "C", null);
            fs.create("TextFile", "note.txt", "C");

            TextFile file = (TextFile) fs.findEntity("C\\note.txt");
            assertEquals("", file.getContent());
        }

        @Test
        void textFile_setContent_shouldUpdateContent() {
            fs.create("Drive", "C", null);
            fs.create("TextFile", "note.txt", "C");

            TextFile file = (TextFile) fs.findEntity("C\\note.txt");
            file.setContent("Direct content update");

            assertEquals("Direct content update", file.getContent());
        }

        @Test
        void writeToFile_multipleWrites_shouldOverwriteContent() {
            fs.create("Drive", "C", null);
            fs.create("TextFile", "note.txt", "C");

            fs.writeToFile("C\\note.txt", "First content");
            fs.writeToFile("C\\note.txt", "Second content");

            TextFile file = (TextFile) fs.findEntity("C\\note.txt");
            assertEquals("Second content", file.getContent());
        }
    }

    @Nested
    class ComplexScenarioTests {
        @Test
        void createNestedStructure_shouldMaintainCorrectPaths() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Users", "C");
            fs.create("Folder", "John", "C\\Users");
            fs.create("Folder", "Documents", "C\\Users\\John");
            fs.create("TextFile", "resume.txt", "C\\Users\\John\\Documents");

            FileSystemEntity file = fs.findEntity("C\\Users\\John\\Documents\\resume.txt");
            assertEquals("C\\Users\\John\\Documents\\resume.txt", file.getPath());
        }

        @Test
        void moveFolder_withNestedContents_shouldUpdateAllPaths() {
            fs.create("Drive", "C", null);
            fs.create("Drive", "D", null);
            fs.create("Folder", "Project", "C");
            fs.create("Folder", "src", "C\\Project");
            fs.create("TextFile", "main.java", "C\\Project\\src");

            fs.move("C\\Project", "D");

            FileSystemEntity file = fs.findEntity("D\\Project\\src\\main.java");
            assertEquals("D\\Project\\src\\main.java", file.getPath());
        }

        @Test
        void zipFile_canContainOtherEntities() {
            fs.create("Drive", "C", null);
            fs.create("ZipFile", "backup.zip", "C");
            fs.create("TextFile", "data.txt", "C\\backup.zip");
            fs.create("Folder", "images", "C\\backup.zip");

            ZipFile zipFile = (ZipFile) fs.findEntity("C\\backup.zip");
            assertEquals(2, zipFile.getContents().size());
            assertTrue(zipFile.hasEntity("data.txt"));
            assertTrue(zipFile.hasEntity("images"));
        }

        @Test
        void delete_driveWithComplexStructure_shouldRemoveEverything() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Users", "C");
            fs.create("Folder", "John", "C\\Users");
            fs.create("TextFile", "file1.txt", "C\\Users\\John");
            fs.create("ZipFile", "archive.zip", "C\\Users");
            fs.create("TextFile", "file2.txt", "C\\Users\\archive.zip");

            fs.delete("C");

            assertNull(fs.findDrive("C"));
            assertNull(fs.findEntity("C\\Users\\John\\file1.txt"));
            assertNull(fs.findEntity("C\\Users\\archive.zip\\file2.txt"));
        }
    }

    @Nested
    class EdgeCaseTests {
        @Test
        void findEntity_withNullPath_shouldReturnNull() {
            FileSystemEntity result = fs.findEntity(null);
            assertNull(result);
        }

        @Test
        void findEntity_withEmptyPath_shouldReturnNull() {
            FileSystemEntity result = fs.findEntity("");
            assertNull(result);
        }

        @Test
        void findEntity_nonExistentDrive_shouldReturnNull() {
            FileSystemEntity result = fs.findEntity("X\\Documents");
            assertNull(result);
        }

        @Test
        void move_entityToSameLocation_shouldThrowException() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "Documents", "C");
            fs.create("TextFile", "note.txt", "C\\Documents");

            assertThrows(IllegalArgumentException.class,
                    () -> fs.move("C\\Documents\\note.txt", "C\\Documents"));
        }

        @Test
        void delete_nonExistentDrive_shouldThrowException() {
            assertThrows(IllegalArgumentException.class,
                    () -> fs.delete("X"));
        }
    }

    @Nested
    class NameValidationTests {
        @Test
        void create_withExtension_shouldSucceed() {
            fs.create("Drive", "C", null);
            fs.create("TextFile", "document.txt", "C");

            assertNotNull(fs.findEntity("C\\document.txt"));
        }

        @Test
        void create_withNumericName_shouldSucceed() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "123", "C");

            assertNotNull(fs.findEntity("C\\123"));
        }

        @Test
        void create_withMixedAlphanumeric_shouldSucceed() {
            fs.create("Drive", "C", null);
            fs.create("Folder", "test123", "C");

            assertNotNull(fs.findEntity("C\\test123"));
        }

        @Test
        void create_withSpaces_shouldThrowException() {
            fs.create("Drive", "C", null);

            assertThrows(IllegalArgumentException.class,
                    () -> fs.create("Folder", "My Folder", "C"));
        }

        @Test
        void create_withSpecialCharacters_shouldThrowException() {
            fs.create("Drive", "C", null);

            assertThrows(IllegalArgumentException.class,
                    () -> fs.create("Folder", "folder@home", "C"));
        }

        @Test
        void create_withMultipleDots_shouldThrowException() {
            fs.create("Drive", "C", null);

            assertThrows(IllegalArgumentException.class,
                    () -> fs.create("TextFile", "file.backup.txt", "C"));
        }
    }
}
