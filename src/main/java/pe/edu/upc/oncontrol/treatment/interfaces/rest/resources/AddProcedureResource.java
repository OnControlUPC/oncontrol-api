package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.RecurrenceType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record AddProcedureResource(
        UUID doctorProfileUuid,
        String description,
        RecurrenceType recurrenceType,
        int interval,
        Integer totalOccurrences,
        LocalDate untilDate,
        LocalDateTime firstExecutionTime
) {
}
