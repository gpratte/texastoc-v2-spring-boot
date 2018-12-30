package com.texastoc.repository;

import com.texastoc.model.season.SeasonPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SeasonPlayerRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public SeasonPlayerRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SeasonPlayer> getBySeasonId(int id) {
        throw new RuntimeException("not implemented");
    }

    public List<SeasonPlayer> getByQuarterlySeasonId(int id) {
        throw new RuntimeException("not implemented");
    }

    public void deleteByIds(int seasonId, int qSeasonId) {
        throw new RuntimeException("not implemented");
    }

    public void save(SeasonPlayer seasonPlayer) {
        throw new RuntimeException("not implemented");
    }

}
