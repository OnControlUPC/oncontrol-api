package pe.edu.upc.oncontrol.profile.application.acl;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.profile.domain.model.valueobjects.LinkStatus;
import pe.edu.upc.oncontrol.profile.infrastructure.persistence.jpa.repositories.DoctorPatientLinkRepository;

import java.util.UUID;

@Component
public class ProfileAccessAcl {

    private final DoctorPatientLinkRepository linkRepository;

    public ProfileAccessAcl(DoctorPatientLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public boolean isLinkActive(UUID doctorProfile, UUID patientProfile) {
        return linkRepository.findByDoctorProfileUuidAndPatientProfileUuid(doctorProfile, patientProfile)
                .map(link -> link.getStatus() == LinkStatus.ACTIVE)
                .orElse(false);
    }



}

