package com.dualnback.data.filesystem.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class FileUtilTest {

    private File tempDir;

    @Before
    public void setUp() throws IOException {
        // Create a temporary directory for testing
        tempDir = Files.createTempDirectory("fileutil-test").toFile();
    }

    @After
    public void tearDown() {
        // Clean up temporary files
        if (tempDir != null && tempDir.exists()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            tempDir.delete();
        }
    }

    @Test
    public void getDataFile_createsFileIfNotExists() {
        // Given: A directory without the data file
        File dataFile = new File(tempDir, FileUtil.FILE_NAME);
        assertThat(dataFile.exists()).isFalse();

        // When: Getting the data file
        File result = FileUtil.getDataFile(tempDir);

        // Then: File is created
        assertThat(result.exists()).isTrue();
        assertThat(result.getName()).isEqualTo(FileUtil.FILE_NAME);
    }

    @Test
    public void getDataFile_initializesWithEmptyDataOnCreation() throws IOException {
        // Given: A directory without the data file
        File dataFile = new File(tempDir, FileUtil.FILE_NAME);
        assertThat(dataFile.exists()).isFalse();

        // When: Getting the data file for the first time
        File result = FileUtil.getDataFile(tempDir);

        // Then: File contains empty JSON structure
        String content = new String(Files.readAllBytes(result.toPath()));
        assertThat(content).isEqualTo("{ \"data\" :[] }");
    }

    @Test
    public void getDataFile_doesNotOverwriteExistingFile() throws IOException {
        // Given: A data file with existing content
        File dataFile = new File(tempDir, FileUtil.FILE_NAME);
        String existingContent = "{\"data\":[{\"datapoint\":{\"date\":\"2024-01-01T12:00:00\",\"score\":100,\"version\":\"Dual 3-Back\"}}]}";
        Files.write(dataFile.toPath(), existingContent.getBytes());

        // When: Getting the data file again
        File result = FileUtil.getDataFile(tempDir);

        // Then: Existing content is preserved (BUG FIX VERIFICATION)
        String contentAfter = new String(Files.readAllBytes(result.toPath()));
        assertThat(contentAfter).isEqualTo(existingContent);
        assertThat(contentAfter).isNotEqualTo("{ \"data\" :[] }");
    }

    @Test
    public void getDataFile_withCustomFileName_createsFileWithGivenName() {
        // Given: A custom file name
        String customFileName = "custom_data.json";

        // When: Getting the data file with custom name
        File result = FileUtil.getDataFile(tempDir, customFileName);

        // Then: File is created with custom name
        assertThat(result.exists()).isTrue();
        assertThat(result.getName()).isEqualTo(customFileName);
    }

    @Test
    public void getDataFile_withCustomFileName_doesNotOverwriteExisting() throws IOException {
        // Given: A custom file with existing content
        String customFileName = "custom_data.json";
        File customFile = new File(tempDir, customFileName);
        String existingContent = "{\"data\":[{\"score\":50}]}";
        Files.write(customFile.toPath(), existingContent.getBytes());

        // When: Getting the file again
        File result = FileUtil.getDataFile(tempDir, customFileName);

        // Then: Content is preserved
        String contentAfter = new String(Files.readAllBytes(result.toPath()));
        assertThat(contentAfter).isEqualTo(existingContent);
    }

    @Test
    public void getDataFile_multipleCalls_returnsSameFile() {
        // When: Getting data file multiple times
        File file1 = FileUtil.getDataFile(tempDir);
        File file2 = FileUtil.getDataFile(tempDir);
        File file3 = FileUtil.getDataFile(tempDir);

        // Then: All return the same file path
        assertThat(file1.getAbsolutePath()).isEqualTo(file2.getAbsolutePath());
        assertThat(file2.getAbsolutePath()).isEqualTo(file3.getAbsolutePath());
    }

    @Test(expected = RuntimeException.class)
    public void getDataFile_throwsExceptionWhenFileCreationFails() {
        // Given: An invalid directory (null or non-existent parent)
        File invalidDir = new File("/this/path/definitely/does/not/exist/and/cannot/be/created");

        // When/Then: Attempting to create file throws RuntimeException
        FileUtil.getDataFile(invalidDir);
    }
}
