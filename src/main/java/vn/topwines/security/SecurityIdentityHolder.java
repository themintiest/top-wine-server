package vn.topwines.security;

import io.quarkus.security.identity.CurrentIdentityAssociation;
import io.quarkus.security.identity.SecurityIdentity;

import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;

@Provider
public class SecurityIdentityHolder implements ContainerRequestFilter {
    private static final ThreadLocal<SecurityIdentity> SECURITY_IDENTITY_HOLDER = new InheritableThreadLocal<>();

    @Inject
    public CurrentIdentityAssociation association;

    public static SecurityIdentity getIdentity() {
        return SECURITY_IDENTITY_HOLDER.get();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        SECURITY_IDENTITY_HOLDER.set(association.getIdentity());
    }
}
