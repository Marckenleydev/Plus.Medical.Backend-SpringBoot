package marc.dev.DoctorBooking_appointement.validation;

import marc.dev.DoctorBooking_appointement.entity.DoctorEntity;
import marc.dev.DoctorBooking_appointement.entity.PatientEntity;
import marc.dev.DoctorBooking_appointement.entity.StaffEntity;
import marc.dev.DoctorBooking_appointement.exception.ApiException;

public class UserValidation {
    public static void verifyAccountStatus(PatientEntity patientEntity) {
        if(!patientEntity.isEnabled()){
            throw  new ApiException("User is disabled");
        }
        if(!patientEntity.isAccountNonExpired()){ throw  new ApiException("User is expired");}
        if(!patientEntity.isAccountNonLocked()){ throw  new ApiException("User is locked");}
    }
    public static void verifyAccountStatus(DoctorEntity doctorEntity) {
        if(!doctorEntity.isEnabled()){
            throw  new ApiException("User is disabled");
        }
        if(!doctorEntity.isAccountNonExpired()){ throw  new ApiException("User is expired");}
        if(!doctorEntity.isAccountNonLocked()){ throw  new ApiException("User is locked");}
    }
    public static void verifyAccountStatus(StaffEntity staffEntity) {
        if(!staffEntity.isEnabled()){
            throw  new ApiException("User is disabled");
        }
        if(!staffEntity.isAccountNonExpired()){ throw  new ApiException("User is expired");}
        if(!staffEntity.isAccountNonLocked()){ throw  new ApiException("User is locked");}
    }
}
