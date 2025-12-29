package com.dualnback.data.filesystem.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class FileIOTest {

    private File tempDir;
    private File testFile;

    @Before
    public void setUp() throws IOException {
        // Create a temporary directory and file for testing
        tempDir = Files.createTempDirectory("fileio-test").toFile();
        testFile = new File(tempDir, "test-data.json");
        testFile.createNewFile();
    }

    @After
    public void tearDown() {
        // Clean up temporary files
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
        if (tempDir != null && tempDir.exists()) {
            tempDir.delete();
        }
    }

    @Test
    public void canCreateInstance() {
        // When/Then: Can create FileIO with valid file
        FileIO fileIO = new FileIO(testFile);
        assertThat(fileIO).isNotNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throwsExceptionWhenFileIsNull() {
        // When/Then: Constructor throws exception for null file
        new FileIO(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_throwsExceptionWhenFileDoesNotExist() {
        // Given: A non-existent file
        File nonExistentFile = new File(tempDir, "does-not-exist.json");

        // When/Then: Constructor throws exception
        new FileIO(nonExistentFile);
    }

    @Test
    public void read_returnsEmptyStringForEmptyFile() throws FileIOException {
        // Given: An empty file
        FileIO fileIO = new FileIO(testFile);

        // When: Reading the file
        String result = fileIO.read();

        // Then: Returns empty string
        assertThat(result).isEmpty();
    }

    @Test
    public void read_returnsFileContent() throws FileIOException, IOException {
        // Given: A file with content
        String expectedContent = "{\"data\":[{\"score\":100}]}";
        Files.write(testFile.toPath(), expectedContent.getBytes());
        FileIO fileIO = new FileIO(testFile);

        // When: Reading the file
        String result = fileIO.read();

        // Then: Returns the content
        assertThat(result).isEqualTo(expectedContent);
    }

    @Test
    public void read_handlesMultilineContent() throws FileIOException, IOException {
        // Given: A file with multi-line JSON
        String expectedContent = "{\n  \"data\": [\n    {\n      \"score\": 100\n    }\n  ]\n}";
        Files.write(testFile.toPath(), expectedContent.getBytes());
        FileIO fileIO = new FileIO(testFile);

        // When: Reading the file
        String result = fileIO.read();

        // Then: Returns the full content including newlines
        assertThat(result).isEqualTo(expectedContent);
    }

    @Test
    public void write_writesDataToFile() throws FileIOException, IOException {
        // Given: FileIO instance and data to write
        FileIO fileIO = new FileIO(testFile);
        String dataToWrite = "{\"data\":[{\"score\":95}]}";

        // When: Writing data
        fileIO.write(dataToWrite);

        // Then: File contains the written data
        String fileContent = new String(Files.readAllBytes(testFile.toPath()));
        assertThat(fileContent).isEqualTo(dataToWrite);
    }

    @Test
    public void write_overwritesExistingContent() throws FileIOException, IOException {
        // Given: A file with existing content
        String oldContent = "{\"data\":[{\"score\":50}]}";
        Files.write(testFile.toPath(), oldContent.getBytes());
        FileIO fileIO = new FileIO(testFile);

        String newContent = "{\"data\":[{\"score\":100}]}";

        // When: Writing new data
        fileIO.write(newContent);

        // Then: File contains only the new data
        String fileContent = new String(Files.readAllBytes(testFile.toPath()));
        assertThat(fileContent).isEqualTo(newContent);
        assertThat(fileContent).isNotEqualTo(oldContent);
    }

    @Test
    public void write_handlesEmptyString() throws FileIOException, IOException {
        // Given: FileIO instance and existing content
        Files.write(testFile.toPath(), "old content".getBytes());
        FileIO fileIO = new FileIO(testFile);

        // When: Writing empty string
        fileIO.write("");

        // Then: File is empty
        String fileContent = new String(Files.readAllBytes(testFile.toPath()));
        assertThat(fileContent).isEmpty();
    }

    @Test
    public void write_handlesLargeContent() throws FileIOException, IOException {
        // Given: FileIO instance and large data
        FileIO fileIO = new FileIO(testFile);
        StringBuilder largeData = new StringBuilder("{\"data\":[");
        for (int i = 0; i < 1000; i++) {
            if (i > 0) largeData.append(",");
            largeData.append("{\"score\":").append(i).append("}");
        }
        largeData.append("]}");
        String largeContent = largeData.toString();

        // When: Writing large data
        fileIO.write(largeContent);

        // Then: File contains all the data
        String fileContent = new String(Files.readAllBytes(testFile.toPath()));
        assertThat(fileContent).isEqualTo(largeContent);
        assertThat(fileContent.length()).isGreaterThan(10000);
    }

    @Test
    public void readWriteCycle_preservesData() throws FileIOException {
        // Given: FileIO instance and test data
        FileIO fileIO = new FileIO(testFile);
        String originalData = "{\"data\":[{\"date\":\"2024-01-01\",\"score\":88,\"version\":\"Dual 2-Back\"}]}";

        // When: Writing then reading
        fileIO.write(originalData);
        String readData = fileIO.read();

        // Then: Data is preserved exactly
        assertThat(readData).isEqualTo(originalData);
    }

    @Test
    public void multipleWrites_eachOverwritesPrevious() throws FileIOException, IOException {
        // Given: FileIO instance
        FileIO fileIO = new FileIO(testFile);

        // When: Writing multiple times
        fileIO.write("first");
        fileIO.write("second");
        fileIO.write("third");

        // Then: Only the last write is preserved
        String fileContent = new String(Files.readAllBytes(testFile.toPath()));
        assertThat(fileContent).isEqualTo("third");
    }

    @Test
    public void write_flushedImmediately() throws FileIOException, IOException {
        // Given: FileIO instance
        FileIO fileIO = new FileIO(testFile);
        String data = "{\"data\":[]}";

        // When: Writing data
        fileIO.write(data);

        // Then: Data is immediately available (flush() was called)
        // We can read it with a new FileIO instance
        FileIO newFileIO = new FileIO(testFile);
        String readData = newFileIO.read();
        assertThat(readData).isEqualTo(data);
    }
}
