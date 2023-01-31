package io.barblin.files;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Files {

    private Files() {

    }

    public record WriteOptions(String path, Charset charset, boolean append, boolean autoFlush) {
    }

    public static final Function<String, Function<Charset, Optional<List<String>>>> resourceFileToLines = fileName -> charset -> {
        try (final var is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
             final var reader = new InputStreamReader(is, charset);
             final var bufferedReader = new BufferedReader(reader)) {

            return Optional.of(bufferedReader.lines().collect(Collectors.toList()));

        } catch (NullPointerException | IOException ex) {
            return Optional.empty();
        }
    };

    public static final Function<File, Function<Charset, Optional<List<String>>>> toLines = file -> charset -> {
        try (final var is = new FileInputStream(file);
             final var reader = new InputStreamReader(is, charset);
             final var bufferedReader = new BufferedReader(reader)) {

            return Optional.of(bufferedReader.lines().collect(Collectors.toList()));

        } catch (IOException ex) {
            return Optional.empty();
        }
    };

    public static final Function<Stream<String>, Function<WriteOptions, Optional<File>>> linesToFile =
            stream -> options -> {
                File file = new File(options.path);

                try (FileOutputStream fos = new FileOutputStream(file, options.append);
                     OutputStreamWriter osw = new OutputStreamWriter(fos, options.charset);
                     BufferedWriter bw = new BufferedWriter(osw);
                     PrintWriter pw = new PrintWriter(bw, options.autoFlush)) {

                    file.createNewFile();

                    if (!file.exists()) {
                        return Optional.empty();
                    }

                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    stream.forEach(pw::println);

                    return Optional.of(file);

                } catch (IOException e) {
                    return Optional.empty();
                }
            };
}
