package io.barblin.files;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Files {

    private Files() {

    }

    public static Function<String, Function<Charset, Optional<List<String>>>> toLines = fileName -> charset -> {
        try (final InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)) {
            if (Objects.isNull(is)) {
                return Optional.empty();
            }

            final Reader r = new InputStreamReader(is, charset);
            final BufferedReader br = new BufferedReader(r);

            return Optional.of(br.lines().collect(Collectors.toList()));

        } catch (IOException ex) {
            return Optional.empty();
        }
    };
}
