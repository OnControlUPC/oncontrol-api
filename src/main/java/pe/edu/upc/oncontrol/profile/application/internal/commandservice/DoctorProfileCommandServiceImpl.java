package pe.edu.upc.oncontrol.profile.application.internal.commandservice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.oncontrol.profile.domain.model.aggregates.DoctorProfile;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.CreateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.DeactivateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.model.commands.doctor.UpdateDoctorProfileCommand;
import pe.edu.upc.oncontrol.profile.domain.services.doctor.DoctorProfileCommandService;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorProfileRepository;

@Service
public class DoctorProfileCommandServiceImpl implements DoctorProfileCommandService {
    private final DoctorProfileRepository doctorProfileRepository;

    public DoctorProfileCommandServiceImpl(DoctorProfileRepository doctorProfileRepository) {
        this.doctorProfileRepository = doctorProfileRepository;
    }

    @Override
    public DoctorProfile createProfile(CreateDoctorProfileCommand command) {
        DoctorProfile profile = new DoctorProfile(command);
        return doctorProfileRepository.save(profile);
    }

    @Override
    public DoctorProfile updateProfile(UpdateDoctorProfileCommand command) {
        DoctorProfile profile = doctorProfileRepository.findByUuid(command.doctorUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));

        profile.updateProfile(command);
        return doctorProfileRepository.save(profile);
    }

    @Override
    public void deactivateProfile(DeactivateDoctorProfileCommand command) {
        DoctorProfile profile = doctorProfileRepository.findByUuid(command.doctorUuid())
                .orElseThrow(() -> new EntityNotFoundException("Doctor profile not found"));

        profile.deactivate();
        doctorProfileRepository.save(profile);
    }
}
