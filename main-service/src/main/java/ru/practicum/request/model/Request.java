package ru.practicum.request.model;

import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.event.model.Event;
import ru.practicum.request.enums.RequestStatus;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", nullable = false)
    private Long id;

    @Column(name = "created")
    private LocalDateTime created;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 15)
    private RequestStatus status = RequestStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User requester;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Request request = (Request) o;
        return id != null && Objects.equals(id, request.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}