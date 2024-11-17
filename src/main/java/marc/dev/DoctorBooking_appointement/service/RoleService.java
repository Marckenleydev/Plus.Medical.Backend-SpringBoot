package marc.dev.DoctorBooking_appointement.service;

import marc.dev.DoctorBooking_appointement.enumeration.Authority;



public interface RoleService {
    public void initializeRoles();
    public void createRoleIfNotFound(Authority authority, String roleName);
}
