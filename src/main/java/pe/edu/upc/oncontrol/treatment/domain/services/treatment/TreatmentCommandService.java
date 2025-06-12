package pe.edu.upc.oncontrol.treatment.domain.services.treatment;

import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.CreateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.MarkProcedureComplianceCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.procedure.UpdateProcedureCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.symptom.CreateSymptomCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.CreateTreatmentCommand;
import pe.edu.upc.oncontrol.treatment.domain.model.commands.treatment.UpdateTreatmentCommand;

import java.util.UUID;

public interface TreatmentCommandService {
    UUID createTreatment(CreateTreatmentCommand command);
    void addProcedure(CreateProcedureCommand command);
    void updateTreatment(UpdateTreatmentCommand command);
    void updateProcedure(UpdateProcedureCommand command);
    void markProcedureCompliance(MarkProcedureComplianceCommand command);
    void logSymptom(CreateSymptomCommand command);
}
