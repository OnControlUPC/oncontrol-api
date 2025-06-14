package pe.edu.upc.oncontrol.profile.interfaces.rest.resources.doctor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DoctorProfileCreateResource(
        @NotNull
        Long userId,
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @Email
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber,
        @NotBlank
        String documentType,
        @NotBlank
        String documentNumber,
        @NotBlank
        String specialty,
        @NotBlank
        @Size(min = 5, max = 50)
        String CPMCode,
        @NotBlank
        String photoUrl
) {
}
