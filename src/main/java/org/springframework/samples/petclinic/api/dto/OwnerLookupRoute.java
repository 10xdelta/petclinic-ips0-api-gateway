package org.springframework.samples.petclinic.api.dto;

/** QA live: the gateway route that forwards to the customers-service owner lookup. */
public final class OwnerLookupRoute {
    public static final String ROUTE = "/api/customer/owners/lookup";

    private OwnerLookupRoute() {}
}
