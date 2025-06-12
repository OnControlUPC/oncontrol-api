package pe.edu.upc.oncontrol.treatment.domain.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.edu.upc.oncontrol.shared.domain.model.entities.AuditableModel;
import pe.edu.upc.oncontrol.treatment.domain.model.aggregates.Treatment;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ComplianceStatus;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.ProcedureDescription;
import pe.edu.upc.oncontrol.treatment.domain.model.valueobjects.SchedulePattern;

import java.time.LocalDateTime;

@Entity
@Table(name = "treatment_procedure")
public class Procedure extends AuditableModel {
    @Getter
    @Embedded
    private ProcedureDescription description;
    @Getter
    @Embedded
    SchedulePattern schedulePattern;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "compliance_status", nullable = false)
    private ComplianceStatus complianceStatus;
    @Getter
    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;
    @Getter
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "treatment_id", nullable = false)
    private Treatment treatment;

    public Procedure(ProcedureDescription description,
                     SchedulePattern schedulePattern,
                     LocalDateTime scheduledAt,
                     Treatment treatment) {
        this.description = description;
        this.schedulePattern = schedulePattern;
        this.scheduledAt = scheduledAt;
        this.treatment = treatment;
        this.complianceStatus = ComplianceStatus.PENDING;
    }

    public Procedure() {
    }

    public void markCompleted(LocalDateTime at) {
        this.completedAt = at;
        this.complianceStatus = ComplianceStatus.COMPLETED;
    }

    public void markMissed() {
        if (complianceStatus == ComplianceStatus.PENDING && LocalDateTime.now().isAfter(scheduledAt.plusDays(1))) {
            this.complianceStatus = ComplianceStatus.MISSED;
        }
    }

    public void regularize(LocalDateTime at) {
        if (complianceStatus == ComplianceStatus.MISSED && at.isBefore(scheduledAt.plusDays(1))) {
            this.completedAt = at;
            this.complianceStatus = ComplianceStatus.REGULARIZED;
        }
    }

}
