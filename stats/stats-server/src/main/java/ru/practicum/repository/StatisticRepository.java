package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dto.StatisticGetDto;
import ru.practicum.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;

@Repository("dbStatisticRepository")
public interface StatisticRepository  extends JpaRepository<Statistic, Long> {

    //Просмотры всех uri (не уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(*) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.view_date BETWEEN ?1 AND ?2 " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatisticGetDto> getUrisViews(LocalDateTime start, LocalDateTime end);

    //Просмотры конкретного uri (не уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(*) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.uri=?1 " +
            "AND s.view_date BETWEEN ?2 AND ?3 " +
            "GROUP BY s.uri, s.app ", nativeQuery = true)
    StatisticGetDto getUriViews(String uri, LocalDateTime start, LocalDateTime end);

    //Просмотры всех uri (уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(DISTINCT s.ip) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.view_date BETWEEN ?1 AND ?2 " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatisticGetDto> getUrisViewsUnique(LocalDateTime start, LocalDateTime end);

    //Просмотры конкретного uri (уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(DISTINCT s.ip) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.uri=?1 " +
            "AND s.view_date BETWEEN ?2 AND ?3 " +
            "GROUP BY s.uri, s.app ", nativeQuery = true)
    StatisticGetDto getUriViewsUnique(String uri, LocalDateTime start, LocalDateTime end);


}
