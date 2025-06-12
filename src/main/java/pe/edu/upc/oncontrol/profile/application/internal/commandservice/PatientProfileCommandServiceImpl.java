package pe.edu.upc.oncontrol.profile.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.PatientProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.DeactivateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.CreatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.DeactivatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.patient.UpdatePatientProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.services.patient.PatientProfileCommandService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.PatientProfileRepository;

@Service
public class PatientProfileCommandServiceImpl implements PatientProfileCommandService {

    private final PatientProfileRepository patientProfileRepository;

    public PatientProfileCommandServiceImpl(PatientProfileRepository patientProfileRepository) {
        this.patientProfileRepository = patientProfileRepository;
    }

    @Override
    public PatientProfile createProfile(CreatePatientProfileCommand command) {
        PatientProfile profile = new PatientProfile(command);
        return patientProfileRepository.save(profile);
    }

    @Override
    public PatientProfile updateProfile(UpdatePatientProfileCommand command) {
        PatientProfile profile = patientProfileRepository.findByUuid(command.patientUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));

        profile.updateProfile(command);
        return patientProfileRepository.save(profile);
    }

    @Override
    public void deactivateProfile(DeactivatePatientProfileCommand command) {
        PatientProfile profile = patientProfileRepository.findByUuid(command.patientUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));
        profile.deactivateProfile();
        patientProfileRepository.save(profile);
    }
}
