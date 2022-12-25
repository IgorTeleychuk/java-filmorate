package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.dao.daointerface.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDao implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(MpaDao.class);

    public MpaDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> findAll() {
        String sqlQuery = "select * from MOTION_PICTURE_ASSOCIATIONS";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpa(rs, rowNum));
    }

    @Override
    public Optional<Mpa> getMPAByID(int id) {
        String sqlQuery = "select * from MOTION_PICTURE_ASSOCIATIONS where MPA_ID = ?";
        List<Mpa> MpaRows = jdbcTemplate.query(sqlQuery, (rs, rowNum) ->
                makeMpa(rs, rowNum), id);
        if (MpaRows.size() > 0) {
            Mpa MPA = MpaRows.get(0);
            log.info("The MPA rating by id was found: {} {}", MPA.getName(), id);
            return Optional.of(MPA);
        } else {
            return Optional.empty();
        }
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("MPA_ID"), rs.getString("MPA_NAME"));
    }
}
