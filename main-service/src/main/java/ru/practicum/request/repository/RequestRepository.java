package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.request.model.Request;

import java.util.List;

@Repository(value = "dbRequestRepository")
public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByRequesterId(Long userId);

    List<Request> findAllByEventId(Long eventId);

    List<Request> getByEventIdAndStatus(Long id, RequestStatus pending);

    List<Request> findAllByIdInAndStatus(List<Long> ids, RequestStatus status);

    @Query(value =
            "SELECT * " +
            "FROM requests AS r " +
            "WHERE r.user_id = ?1 " +
            "AND r.event_id = ?2 " +
            "LIMIT 1", nativeQuery = true)
    Request findByUserIdAndEventId(Long userId, Long eventId);

}