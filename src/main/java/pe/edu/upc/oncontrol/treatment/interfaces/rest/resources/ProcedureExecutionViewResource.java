package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ExecutionStatus;

import java.time.LocalDateTime;

public record ProcedureExecutionViewResource(
        Long id,
        LocalDateTime scheduleAt,
        ExecutionStatus status,
        LocalDateTime completedAt
) {
}
