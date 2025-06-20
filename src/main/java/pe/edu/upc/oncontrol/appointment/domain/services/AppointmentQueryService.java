package pe.edu.upc.oncontrol.appointment.domain.services;

import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.queries.AppointmentCalendarItem;

import java.util.List;
import java.util.UUID;

public interface AppointmentQueryService {
    List<AppointmentCalendarItem> getAppointmentsForPatient(UUID patientProfileUuid);
    List<AppointmentCalendarItem> getAppointmentsForDoctor(UUID doctorProfileUuid);
    Appointment findByIdAndDoctor(Long appointmentId, UUID doctorProfileUuid);
    Appointment findByIdAndPatient(Long appointmentId, UUID patientProfileUuid);

}
