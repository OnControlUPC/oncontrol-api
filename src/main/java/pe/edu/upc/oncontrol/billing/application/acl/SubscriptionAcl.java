package pe.edu.upc.oncontrol.billing.application.acl;

import org.springframework.stereotype.Component;
import pe.edu.upc.oncontrol.billing.application.internal.queryservices.SubscriptionQueryServiceImpl;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Plan;
import pe.edu.upc.oncontrol.billing.domain.model.aggregates.Subscription;
import pe.edu.upc.oncontrol.billing.infrastructure.persistence.jpa.repositories.PlanRepository;

import java.util.Optional;

@Component
public class SubscriptionAcl {

    private final SubscriptionQueryServiceImpl subscriptionQueryService;
    private final PlanRepository planRepository;

    public SubscriptionAcl(SubscriptionQueryServiceImpl subscriptionQueryService, PlanRepository planRepository) {
        this.subscriptionQueryService = subscriptionQueryService;
        this.planRepository = planRepository;
    }

    public Optional<Plan> getActivePlanByAdminId(Long adminId) {
        Optional<Subscription> subscription = subscriptionQueryService.getActiveByAdminId(adminId);
        return subscription.flatMap(sub -> planRepository.findById(sub.getId()));
    }
}