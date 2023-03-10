package com.training.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Entity(name = "History")
@Table(name = "history")
public class History {

    @Id
    @GeneratedValue
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    @Column(updatable = false)
    private UUID id;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id",
            nullable = false)
    private Ticket ticket;

    @CreationTimestamp
    @Column(name = "creation_date",
            updatable = false,
            nullable = false)
    private LocalDateTime date;

    @Setter
    private String action;

    @ToString.Exclude
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",
            nullable = false)
    private User user;

    @Setter
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        History history = (History) o;
        return Objects.equals(date, history.date)
                && Objects.equals(action, history.action)
                && Objects.equals(description, history.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, action, description);
    }
}
