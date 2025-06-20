package pe.edu.upc.oncontrol.appointment.domain.model.valueobject;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


public class ScheduledAt implements Serializable {

    private final LocalDateTime value;

    public ScheduledAt(LocalDateTime value) {
        if (value == null) {
            throw new IllegalArgumentException("Scheduled date cannot be null.");
        }
        if (value.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled date must be in the future.");
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduledAt that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public LocalDateTime getValue() {
        return value;
    }

    public boolean isBefore(LocalDateTime other) {
        return value.isBefore(other);
    }
}
