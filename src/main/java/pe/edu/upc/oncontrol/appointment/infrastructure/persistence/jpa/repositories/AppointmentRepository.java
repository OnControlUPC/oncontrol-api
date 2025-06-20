package pe.edu.upc.oncontrol.appointment.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.valueobject.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientProfileUuidAndScheduledAtAfter(UUID patientUuid, LocalDateTime dateTime);
    List<Appointment> findByDoctorProfileUuidAndScheduledAtAfter(UUID doctorUuid, LocalDateTime dateTime);
    Optional<Appointment> findByIdAndDoctorProfileUuid(Long id, UUID doctorUuid);
    Optional<Appointment> findByIdAndPatientProfileUuid(Long id, UUID patientUuid);
    List<Appointment> findByStatus(AppointmentStatus status);
    boolean existsByDoctorProfileUuidAndPatientProfileUuid(UUID doctorUuid, UUID patientUuid);
    @Query("""
        SELECT a FROM Appointment a
        WHERE a.status = 'SCHEDULED'
          AND a.meetingUrl IS NULL
          AND a.locationName IS NULL
          AND a.scheduledAt BETWEEN :from AND :to
    """)
    List<Appointment> findVirtualAppointmentsWithoutMeetingUrl(@Param("from") LocalDateTime from,
                                                               @Param("to") LocalDateTime to);
    List<Appointment> findByStatusAndScheduledAtBefore(AppointmentStatus status, LocalDateTime now);

}
