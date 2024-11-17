package marc.dev.DoctorBooking_appointement.utils;


public class EmailUtils {

//    public static String getEmailMessage(String name, String host, String key) {
//        return "Hello " + name + ",\n\nYour new account has been created. Please click on the link below to verify your account.\n\n" +
//                getVerificationUrl(host, key) + "\n\nThe Support Team";
//    }
//
//    public static String getResetPasswordMessage(String name, String host, String key) {
//        return "Hello " + name + ",\n\nPlease use this link bellow to reset your password.\n\n" +
//                getResetPasswordUrl(host, key) + "\n\nThe Support Team";
//    }
//
//    public static String getVerificationUrl(String host, String key) {
//        return host + "/users/verify/account?key=" + key;
//    }
//
//    public static String getResetPasswordUrl(String host, String key) {
//        return host + "/users/verify/password?key=" + key;
//    }

    public static String getPatientEmailMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click on the link below to verify your account.\n\n" +
                getPatientVerificationUrl(host, key) + "\n\nThe Support Team";
    }

    public static String getPatientResetPasswordMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nPlease use this link bellow to reset your password.\n\n" +
                getPatientResetPasswordUrl(host, key) + "\n\nThe Support Team";
    }

    public static String getDoctorEmailMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click on the link below to verify your account.\n\n" +
                getDoctorVerificationUrl(host, key) + "\n\nThe Support Team";
    }

    public static String getDoctorResetPasswordMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nPlease use this link bellow to reset your password.\n\n" +
                getDoctorResetPasswordUrl(host, key) + "\n\nThe Support Team";
    }

    public static String getStaffEmailMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click on the link below to verify your account.\n\n" +
                getStaffVerificationUrl(host, key) + "\n\nThe Support Team";
    }

    public static String getStaffResetPasswordMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nPlease use this link bellow to reset your password.\n\n" +
                getStaffResetPasswordUrl(host, key) + "\n\nThe Support Team";
    }

    //    PatientVerificationUrl
    public static String getPatientVerificationUrl(String host, String key) {
        return host + "/users/patients/verify/account?key=" + key;
    }

    public static String getPatientResetPasswordUrl(String host, String key) {
        return host + "/users/patients/verify/password?key=" + key;
    }

    //    DoctorVerificationUrl
    public static String getDoctorVerificationUrl(String host, String key) {
        return host + "/users/doctors/verify/account?key=" + key;
    }

    public static String getDoctorResetPasswordUrl(String host, String key) {
        return host + "/users/doctors/patients/verify/password?key=" + key;
    }

//    StaffVerificationUrl

    public static String getStaffVerificationUrl(String host, String key) {
        return host + "/users/staffs/verify/account?key=" + key;
    }

    public static String getStaffResetPasswordUrl(String host, String key) {
        return host + "/users/staffs/verify/password?key=" + key;
    }
}
