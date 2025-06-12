package pe.edu.upc.oncontrol.profile.domain.services.doctor;

import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.CreateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.DeactivateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;

public interface DoctorProfileCommandService {
    DoctorProfile createProfile(CreateDoctorProfileCommand command);
    DoctorProfile updateProfile(UpdateDoctorProfileCommand command);
    void deactivateProfile(DeactivateDoctorProfileCommand command);
}
