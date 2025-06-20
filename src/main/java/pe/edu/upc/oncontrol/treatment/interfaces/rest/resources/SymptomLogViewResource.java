package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public record SymptomLogViewResource(
        Long id,
        LocalDateTime loggedAt, //Hora en la que sintio el sintoma
        String symptomType,
        String notes,
        UUID treatmentId,
        Date createdAt
) {
}
