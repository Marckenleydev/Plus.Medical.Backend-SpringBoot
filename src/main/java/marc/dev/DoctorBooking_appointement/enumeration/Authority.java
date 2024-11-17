package marc.dev.DoctorBooking_appointement.enumeration;

import static marc.dev.DoctorBooking_appointement.constant.Constants.*;

public enum Authority {
    PATIENT(PATIENT_AUTHORITIES),
    DOCTOR(DOCTOR_AUTHORITIES),
    STAFF(STAFF_AUTHORITIES),
    USER(USER_AUTHORITIES),
    ADMIN(ADMIN_AUTHORITIES),
    SUPER_ADMIN(SUPER_ADMIN_AUTHORITIES),
    MANAGER(MANAGER_AUTHORITIES);



    private final String value;

    Authority(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}