package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.daointerface.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class MpaDao implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "select * from MOTION_PICTURE_ASSOCIATIONS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs, rowNum));
    }

    @Override
    public Optional<Mpa> getMpaByID(int id) {
        String sqlQuery = "select * from MOTION_PICTURE_ASSOCIATIONS where MPA_ID = ?";
        List<Mpa> MpaRows = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                makeMpa(rs, rowNum), id);
        if (MpaRows.size() > 0) {
            Mpa mpa = MpaRows.get(0);
            log.info("The MPA rating by id was found: {} {}", mpa.getName(), id);
            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

    private static Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));
    }
}
