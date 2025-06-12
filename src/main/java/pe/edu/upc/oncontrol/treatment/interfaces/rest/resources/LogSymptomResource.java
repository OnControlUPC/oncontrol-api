package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.SymptomSeverity;

import java.time.LocalDateTime;
import java.util.UUID;

public record LogSymptomResource(
        UUID patientProfileUuid,
        String symptomType,
        SymptomSeverity severity,
        String notes,
        LocalDateTime loggedAt
) {
}
