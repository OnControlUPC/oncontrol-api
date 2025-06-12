package pe.edu.upc.oncontrol.treatment.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

@Embeddable
public class SchedulePattern {
    @Enumerated(EnumType.STRING)
    @Column(name = "recurrence_type", nullable = false)
    private RecurrenceType type;

    @Column(name = "interval_value", nullable = false)
    private int interval;

    @Column(name = "occurrences")
    private Integer totalOccurrences;

    @Column(name = "until_date")
    private LocalDate untilDate;

    protected SchedulePattern() {
        // JPA
    }

    public SchedulePattern(RecurrenceType type, int interval, Integer totalOccurrences, LocalDate untilDate) {
        if (type == null) throw new IllegalArgumentException("Occurrence type cannot be null.");
        if (interval <= 0) throw new IllegalArgumentException("Interval must be greater than zero.");

        this.type = type;
        this.interval = interval;

        if (totalOccurrences != null && totalOccurrences <= 0) {
            throw new IllegalArgumentException("Occurrences must be greater than zero.");
        }

        if (untilDate != null && untilDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Limit date cannot be in the past.");
        }

        this.totalOccurrences = totalOccurrences;
        this.untilDate = untilDate;
    }

    public boolean endsByOccurrences() {
        return totalOccurrences != null;
    }

    public boolean endsByDate() {
        return untilDate != null;
    }

    public RecurrenceType getType() {
        return type;
    }

    public int getInterval() {
        return interval;
    }

    public Integer getTotalOccurrences() {
        return totalOccurrences;
    }

    public LocalDate getUntilDate() {
        return untilDate;
    }

    @Override
    public String toString() {
        return type + " every " + interval + (endsByOccurrences() ? " times" : endsByDate() ? " until " + untilDate : "");
    }
}
