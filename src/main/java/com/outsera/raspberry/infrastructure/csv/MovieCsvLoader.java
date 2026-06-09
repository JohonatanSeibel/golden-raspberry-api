package com.outsera.raspberry.infrastructure.csv;

import com.outsera.raspberry.domain.model.Movie;
import com.outsera.raspberry.infrastructure.persistence.JpaMovieRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class MovieCsvLoader implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieCsvLoader.class);

    private final ResourceLoader resourceLoader;
    private final MovieCsvParser movieCsvParser;
    private final JpaMovieRepository movieRepository;
    private final String csvPath;

    public MovieCsvLoader(ResourceLoader resourceLoader,
                          MovieCsvParser movieCsvParser,
                          JpaMovieRepository movieRepository,
                          @Value("${app.csv.path}") String csvPath) {
        this.resourceLoader = resourceLoader;
        this.movieCsvParser = movieCsvParser;
        this.movieRepository = movieRepository;
        this.csvPath = csvPath;
    }

    @Override
    public void run(ApplicationArguments args) {
        Resource resource = resourceLoader.getResource(csvPath);
        List<Movie> movies = readMovies(resource);
        movieRepository.saveAll(movies);
        LOGGER.info("Carga concluída: {} filmes importados de {}", movies.size(), csvPath);
    }

    private List<Movie> readMovies(Resource resource) {
        List<Movie> movies = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null) {
                return movies;
            }
            String line;
            while ((line = reader.readLine()) != null) {
                String currentLine = line;
                movieCsvParser.parse(currentLine).ifPresentOrElse(
                        movies::add,
                        () -> LOGGER.warn("Linha ignorada por formato inválido: {}", currentLine));
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Falha ao ler o arquivo CSV: " + csvPath, exception);
        }
        return movies;
    }
}
