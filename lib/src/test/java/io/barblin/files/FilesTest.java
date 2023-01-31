package io.barblin.files;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FilesTest {
    @Test
    void fileNotFoundExceptionShouldReturnEmpty() {
        assertFalse(Files.toLines.apply("notFound.txt").apply(StandardCharsets.UTF_8).isPresent());
    }

    @Test
    void validFileNameShouldReturnStreamOfLines() {
        List<String> lines = Files.toLines.apply("test.txt").apply(StandardCharsets.UTF_8).get();

        assertEquals("test", lines.get(0));
    }

}
