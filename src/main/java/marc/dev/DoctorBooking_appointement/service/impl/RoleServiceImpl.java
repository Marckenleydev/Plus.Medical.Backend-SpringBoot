package marc.dev.DoctorBooking_appointement.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import marc.dev.DoctorBooking_appointement.domain.RequestContext;
import marc.dev.DoctorBooking_appointement.entity.RoleEntity;
import marc.dev.DoctorBooking_appointement.enumeration.Authority;
import marc.dev.DoctorBooking_appointement.repository.RoleRepository;
import marc.dev.DoctorBooking_appointement.service.RoleService;
import org.springframework.stereotype.Service;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public void initializeRoles() {
        RequestContext.setUserId(0L);
        createRoleIfNotFound(Authority.USER, "USER");
        createRoleIfNotFound(Authority.PATIENT, "PATIENT");
        createRoleIfNotFound(Authority.DOCTOR, "DOCTOR");
        createRoleIfNotFound(Authority.STAFF, "STAFF");
        createRoleIfNotFound(Authority.MANAGER, "MANAGER");
        createRoleIfNotFound(Authority.ADMIN, "ADMIN");
        createRoleIfNotFound(Authority.SUPER_ADMIN, "SUPER_ADMIN");



    }

    @Override
    public void createRoleIfNotFound(Authority authority, String roleName) {
        if (roleRepository.findByNameIgnoreCase(roleName).isEmpty()) {
            var role = new RoleEntity();
            role.setName(roleName);
            role.setAuthorities(authority);
            roleRepository.save(role);
        }
    }
}
