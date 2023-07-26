package ru.practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;


@Repository(value = "dbEventRepository")
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value =
            "SELECT * " +
            "FROM events AS e " +
            "WHERE e.category_id = ?1 " +
            "LIMIT 1", nativeQuery = true)
    Event findFirstByCategory(Long catId);

}
