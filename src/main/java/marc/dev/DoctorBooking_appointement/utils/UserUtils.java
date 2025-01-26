package marc.dev.DoctorBooking_appointement.utils;


import marc.dev.DoctorBooking_appointement.dto.Doctor;
import marc.dev.DoctorBooking_appointement.dto.User;
import marc.dev.DoctorBooking_appointement.entity.*;
import org.springframework.beans.BeanUtils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static java.time.LocalDateTime.now;
import static marc.dev.DoctorBooking_appointement.constant.Constants.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;

public class UserUtils {

    public static PatientEntity createPatientEntity(String firstName, String lastName, String email, RoleEntity role) {
        return PatientEntity.builder()
                .userId(UUID.randomUUID().toString())
                .patientId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .loginAttempts(0)
                .phone(EMPTY)
                .medical_history(EMPTY)
                .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
                .role(role)
                .build();
    }

    public static DoctorEntity createDoctorEntity(String firstName, String lastName, String email, String imageUrl, RoleEntity role, SpecialisationEntity specialisation) {
        String randomUniversity = UNIVERSITIES_IN_TURKEY[ThreadLocalRandom.current().nextInt(UNIVERSITIES_IN_TURKEY.length)];

        return DoctorEntity.builder()
                .userId(UUID.randomUUID().toString())
                .doctorId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .loginAttempts(0)
                .experience(experience)
                .phone(EMPTY)
                .aboutMe(ABOUT_ME)
                .education(randomUniversity)
                .rating(rating)
                .totalRating(totalRating)
                .totalPatients(totalPatients)
                .specialisation(specialisation)
                .imageUrl(imageUrl)
                .role(role)
                .build();
    }

    public static User fromPatientEntity(PatientEntity patientEntity, RoleEntity role, CredentialEntity credentialEntity) {
        User patient = new User();
        BeanUtils.copyProperties(patientEntity, patient);
        patient.setLastLogin(patientEntity.getLastLogin().toString());
        patient.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        patient.setCreatedAt(patientEntity.getCreatedAt().toString());
        patient.setUpdatedAt(patientEntity.getUpdatedAt().toString());
        patient.setRole(role.getName());
        patient.setAuthorities(role.getAuthorities().getValue());
        return patient;
    }

    public static User fromDoctorEntity(DoctorEntity doctorEntity, RoleEntity role, CredentialEntity credentialEntity) {
        User doctor = new User();
        BeanUtils.copyProperties(doctorEntity, doctor);
        doctor.setLastLogin(doctorEntity.getLastLogin().toString());
        doctor.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        doctor.setCreatedAt(doctorEntity.getCreatedAt().toString());
        doctor.setUpdatedAt(doctorEntity.getUpdatedAt().toString());
        doctor.setRole(role.getName());
        doctor.setAuthorities(role.getAuthorities().getValue());


        return doctor;
    }
    public static Doctor fromDoctorEntity(DoctorEntity doctorEntity, RoleEntity role) {
        var doctor = new Doctor();
        BeanUtils.copyProperties(doctorEntity, doctor);
        doctor.setLastLogin(doctorEntity.getLastLogin().toString());
        doctor.setCreatedAt(doctorEntity.getCreatedAt().toString());
        doctor.setUpdatedAt(doctorEntity.getUpdatedAt().toString());
        doctor.setRole(role.getName());
        doctor.setAuthorities(role.getAuthorities().getValue());


        return doctor;
    }
    public static boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(NINETY_DAYS).isAfter(now());
    }






}

