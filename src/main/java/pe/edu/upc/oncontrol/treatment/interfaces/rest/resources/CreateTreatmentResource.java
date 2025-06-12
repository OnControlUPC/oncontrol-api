package pe.edu.upc.oncontrol.treatment.interfaces.rest.resources;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTreatmentResource(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        UUID doctorProfileUuid,
        UUID patientProfileUuid
) {
}
