package pe.edu.upc.oncontrol.treatment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureComplianceCommand;
import pe.edu.upc.oncontrol.treatment.interfaces.rest.resources.MarkProcedureComplianceResource;

@Component
public class MarkProcedureComplianceAssembler {
    public MarkProcedureComplianceCommand toCommand(Long procedureId, MarkProcedureComplianceResource resource) {
        return new MarkProcedureComplianceCommand(
                procedureId,
                resource.patientProfileUuid(),
                resource.completionDate()
        );
    }
}
