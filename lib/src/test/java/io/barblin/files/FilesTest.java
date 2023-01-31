package io.barblin.files;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FilesTest {
    @Test
    void resourceFileToLinesShouldReturnEmptyOnNullPointerException() {
        assertFalse(Files.resourceFileToLines.apply("notFound.txt").apply(StandardCharsets.UTF_8).isPresent());
    }

    @Test
    void validFileNameShouldReturnStreamOfLines() {
        List<String> lines = Files.resourceFileToLines.apply("test.txt").apply(StandardCharsets.UTF_8).get();

        assertEquals("test", lines.get(0));
    }

    @Test
    void fileToLinesShouldReturnEmptyOnNullPointerException() {
        File file = Paths.get("./src/test/resources/notFound.txt").toFile();

        assertFalse(Files.toLines.apply(file).apply(StandardCharsets.UTF_8).isPresent());
    }

    @Test
    void validFileShouldReturnStreamOfLines() {
        File file = Paths.get("./src/test/resources/test.txt").toFile();
        List<String> lines = Files.toLines.apply(file).apply(StandardCharsets.UTF_8).get();

        assertEquals("test", lines.get(0));
    }

    @Test
    void writeFileShouldSucceed() {
        Files.WriteOptions options = new Files.WriteOptions("./build/write.txt", StandardCharsets.UTF_8, false, false);
        List<String> expected = Files.resourceFileToLines.apply("test.txt").apply(StandardCharsets.UTF_8).get();

        File file = Files.linesToFile.apply(expected.stream())
                .apply(options).get();

        Stream<String> actual = Files.toLines.apply(file).apply(StandardCharsets.UTF_8).get().stream();

        assertTrue(file.exists());
        assertStreamEquals(expected.stream(), actual);
    }

    static void assertStreamEquals(Stream<?> s1, Stream<?> s2) {
        Iterator<?> it1 = s1.iterator(), it2 = s2.iterator();
        while (it1.hasNext() && it2.hasNext())
            assertEquals(it1.next(), it2.next());
        assert !it1.hasNext() && !it2.hasNext();
    }
}
