# In-Memory File System

A Java implementation of an in-memory hierarchical file system with CRUD operations.

## Quick Start

### Prerequisites

- Java 17+
- Maven 3.6+

### Build & Test

```bash
mvn clean compile
mvn test
```

## What This Demonstrates

This project showcases:

- **Object-Oriented Design** (inheritance, polymorphism, interfaces)
- **Design Patterns** (Composite, Template Method, Factory)
- **Clean Architecture** (layered structure with `api` / `entities` / `containers` packages)
- **Comprehensive Testing** (unit tests with nearly 100% coverage)
- **Error Handling** (validation and exception management)

## Core API Usage

```java
FileSystem fs = new FileSystem();

// Create drives and folders
fs.create("Drive", "C", null);
fs.create("Folder", "Documents", "C");
fs.create("TextFile", "note.txt", "C\Documents");
fs.create("ZipFile", "backup.zip", "C");

// Write to files
fs.writeToFile("C\Documents\note.txt", "Hello World!");

// Move files/folders
fs.move("C\Documents\note.txt", "C\backup.zip");

// Delete items
fs.delete("C\Documents");

// Find items
FileSystemEntity file = fs.findEntity("C\backup.zip\note.txt");
```

## Testing

**Run all tests:**

```bash
mvn test
```

**Test structure:**

- **Entity creation** — ```FileSystemCreateOperationsTest```
- **Deletion operations** — ```FileSystemDeleteOperationsTest```
- **Move operations** — `FileSystemMoveOperationsTest`
- **File content operations** — `FileSystemWriteOperationsTest`
- **Complex scenarios and edge cases** — `FileSystemIntegrationTest`

## Architecture

```
filesystem/
├── api/           # FileSystem (main API) + FileSystemEntity (base class)
├── entities/      # Drive, Folder, TextFile, ZipFile  
├── containers/    # Container interface + AbstractContainer
└── tests/         # Comprehensive test suite
```

## Key Features

- **Entity Types**: Drives, Folders, Text Files, Zip Files (archives)
- **Operations**: Create, Delete, Move, Write content
- **Path Support**: Windows-style paths (`C\Documents\file.txt`)
- **Validation**: Name validation, duplicate prevention
- **Hierarchy**: Parent-child relationships with automatic path generation

## Constraints

- **Names**: Alphanumeric only (`file123.txt` ✅, `my file.txt` ❌)
- **Containers**: Only Drives, Folders, and ZipFiles can contain other entities
- **Paths**: Use backslash format (`C\Documents\file.txt`)
- **Drives**: Cannot be moved, are root-level only
