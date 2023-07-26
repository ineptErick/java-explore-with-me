package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import ru.practicum.dto.StatisticGetProjection;
import ru.practicum.model.Statistic;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component("dbStatisticRepository")
public interface StatisticRepository  extends JpaRepository<Statistic, Long> {

    //Просмотры всех uri (не уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(*) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.view_date BETWEEN ?1 AND ?2 " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatisticGetProjection> getUrisViews(LocalDateTime start, LocalDateTime end);

    //Просмотры конкретных uri (не уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(*) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.uri IN ?1 " +
            "AND s.view_date BETWEEN ?2 AND ?3 " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatisticGetProjection> getUrisViewsFromSet(Set<String> uris, LocalDateTime start, LocalDateTime end);

    //Просмотры всех uri (уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(DISTINCT s.ip) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.view_date BETWEEN ?1 AND ?2 " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatisticGetProjection> getUrisViewsUnique(LocalDateTime start, LocalDateTime end);

    //Просмотры конкретных uri (уникальные)
    @Query(value = "SELECT s.uri, s.app, COUNT(DISTINCT s.ip) AS hits " +
            "FROM statistic AS s " +
            "WHERE s.uri IN ?1 " +
            "AND s.view_date BETWEEN ?2 AND ?3 " +
            "GROUP BY s.uri, s.app " +
            "ORDER BY hits DESC ", nativeQuery = true)
    List<StatisticGetProjection> getUrisViewsFromSetUnique(Set<String> uris, LocalDateTime start, LocalDateTime end);

}