package pe.edu.upc.oncontrol.appointment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.appointment.domain.model.aggregates.Appointment;
import pe.edu.upc.oncontrol.appointment.domain.model.commands.CreateAppointmentCommand;
import pe.edu.upc.oncontrol.appointment.interfaces.rest.resources.AppointmentCalendarItem;
import pe.edu.upc.oncontrol.appointment.interfaces.rest.resources.AppointmentDetail;
import pe.edu.upc.oncontrol.appointment.interfaces.rest.resources.CreateAppointmentResource;

import java.util.UUID;

@Component
public class AppointmentAssembler {

    public CreateAppointmentCommand toCreateCommand(CreateAppointmentResource resource, UUID doctorProfileUuid) {
        return new CreateAppointmentCommand(
                doctorProfileUuid,
                resource.patientProfileUuid(),
                resource.scheduledAt(),
                resource.locationName(),
                resource.locationMapsUrl(),
                resource.meetingUrl()
        );
    }

    public AppointmentDetail toDetail(Appointment appointment) {
        return new AppointmentDetail(
                appointment.getId(),
                appointment.getScheduledAt().getValue(),
                appointment.getStatus().name(),
                appointment.getLocation() != null ? appointment.getLocation().getName() : null,
                appointment.getLocation() != null ? appointment.getLocation().getMapsUrl() : null,
                appointment.getMeetingUrl() != null ? appointment.getMeetingUrl().getValue() : null
        );
    }

    public AppointmentCalendarItem toCalendarItem(Appointment appointment, String title) {
        return new AppointmentCalendarItem(
                appointment.getId(),
                title,
                appointment.getScheduledAt().getValue(),
                appointment.getStatus().name(),
                "APPOINTMENT"
        );
    }

}
