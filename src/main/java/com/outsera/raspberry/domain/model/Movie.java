package com.outsera.raspberry.domain.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "release_year", nullable = false)
    private int releaseYear;

    @Column(nullable = false)
    private String title;

    @Column
    private String studios;

    @Column(nullable = false)
    private boolean winner;

    @ElementCollection
    @CollectionTable(name = "movie_producers", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "producer")
    private List<String> producers = new ArrayList<>();

    protected Movie() {
    }

    public Movie(int releaseYear, String title, String studios, boolean winner, List<String> producers) {
        this.releaseYear = releaseYear;
        this.title = title;
        this.studios = studios;
        this.winner = winner;
        this.producers = producers;
    }

    public Long getId() {
        return id;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getTitle() {
        return title;
    }

    public String getStudios() {
        return studios;
    }

    public boolean isWinner() {
        return winner;
    }

    public List<String> getProducers() {
        return producers;
    }
}
