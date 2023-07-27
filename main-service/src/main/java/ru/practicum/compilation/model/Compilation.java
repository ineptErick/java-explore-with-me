package ru.practicum.compilation.model;

import io.micrometer.core.instrument.Statistic;
import lombok.*;
import org.hibernate.Hibernate;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compilations")
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id", nullable = false)
    private Long id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title", nullable = false, length = 512)
    private String title;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "events_compilations",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id"))
    List<Event> events = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Compilation compilation = (Compilation) o;
        return id != null && Objects.equals(id, compilation.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}