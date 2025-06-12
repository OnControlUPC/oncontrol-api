package pe.edu.upc.oncontrol.profile.domain.services.patient;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.CreatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.DeactivatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.UpdatePatientProfileCommand;

public interface PatientProfileCommandService {
    PatientProfile createProfile(CreatePatientProfileCommand command);
    PatientProfile updateProfile(UpdatePatientProfileCommand command);
    void deactivateProfile(DeactivatePatientProfileCommand command);
}
