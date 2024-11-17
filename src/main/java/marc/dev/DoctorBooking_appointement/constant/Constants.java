package marc.dev.DoctorBooking_appointement.constant;

import java.util.concurrent.ThreadLocalRandom;

public class Constants {

    public static final String[] PUBLIC_URLS = { "/users/patients/resetpassword/reset/**","/users/doctors/resetpassword/reset/**","/users/staffs/resetpassword/reset/**", "/users/verify/resetpassword/**","/users/patients/verify/resetpassword/**","/users/doctors/verify/resetpassword/**","/users/staffs/verify/resetpassword/**", "/users/resetpassword/**","/users/patients/resetpassword/**","/users/doctors/resetpassword/**","/users/staffs/resetpassword/**",  "/user/login/**", "/users/verify/account/**","/users/patients/verify/**","/users/doctors/verify/**","/users/staffs/verify/**","/users/doctors/register/**", "/users/patients/register/**","/users/staffs/register/**", "/users/new/password/**", "/users/verify/**","/users/doctors/verify/**", "/users/resetpassword/**", "/users/image/**", "/users/verify/password/**", "/users/doctors/**","/users/patients/**","/specialisation/**","/api/appointments/**" ,"/api/medicalservices/**","/api/messages/**", "/api/feedbacks/**"  };
    public static final int NINETY_DAYS = 90;

    public static final String[] UNIVERSITIES_IN_TURKEY = {
            "Boğaziçi University, Bebek, 34342 Beşiktaş/Istanbul, Turkey",
            "Middle East Technical University (METU), Üniversiteler, Dumlupınar Blv. No:1, 06800 Çankaya/Ankara, Turkey",
            "Istanbul University, Beyazıt, 34452 Fatih/Istanbul, Turkey",
            "Hacettepe University, Beytepe, 06800 Çankaya/Ankara, Turkey",
            "Koç University, Rumelifeneri Yolu, 34450 Sarıyer/Istanbul, Turkey",
            "Sabancı University, Orhanlı, Tuzla/Istanbul, Turkey",
            "Ankara University, Emniyet, Dögol Cd. No:1, 06560 Yenimahalle/Ankara, Turkey"
    };
    public static final int STRENGTH = 12;
    // Generate a random rating between 4 and 5
    public static final float rating = ThreadLocalRandom.current().nextFloat() * (5 - 4) + 4;

    // Generate a random totalRating between 140 and 300
    public static final int totalRating = ThreadLocalRandom.current().nextInt(140, 301);
    public static final int experience = ThreadLocalRandom.current().nextInt(4, 7);

    // Generate a random totalPatients between 230 and 400
    public static final long totalPatients = ThreadLocalRandom.current().nextLong(230, 401);

    public static final String BASE_PATH = "/**";
    public static final String FILE_NAME = "File-Name";
    public static final String LOGIN_PATH = "/user/login";
    public static final int MAX_ABOUT_ME_LENGTH = 255;
    public static final String ABOUT_ME = "As a dedicated and compassionate healthcare professional, I have spent over a decade in the medical field, specializing in internal medicine. My journey began with a passion for understanding the complexities of the human body and a desire to make a tangible difference in people's lives. Over the years, I have honed my skills in diagnosing and treating a wide range of illnesses, always prioritizing patient care and well-being.";
    public static final String[] PUBLIC_ROUTES = { "/users/patients/resetpassword/reset","/users/doctors/resetpassword/reset","/users/staffs/resetpassword/reset", "/users/patients/verify/resetpassword","/users/doctors/verify/resetpassword","/users/staffs/verify/resetpassword", "/users/resetpassword",  "/users/patients/resetpassword", "/users/doctors/resetpassword", "/users/staffs/resetpassword","/users/stream", "/users/id", "/user/login","","/users/patients/register","/users/doctors/register","/users/patients/register", "/users/patients/new/password", "/users/doctors/new/password", "/users/staffs/new/password", "/users/patients/verify","/users/doctors/verify", "/users/staffs/verify","/users/refresh/token", "/users/resetpassword", "/users/image", "/users/verify/account", "/users/verify/password","/users/doctors","/users/patients","/api/specialisation","/api/appointments","/api/medicalservices", "/api/messages","/api/feedbacks"  };
    public static final String AUTHORITIES = "authorities";
    public static final String MARC_DEV_LLC = "MARC_DEV_LLC";
    public static final String EMPTY_VALUE = "empty";
    public static final String ROLE = "role";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String PATIENT_AUTHORITIES = "appointment:read,appointment:create,appointment:update,appointment:delete,doctor:read";
    public static final String DOCTOR_AUTHORITIES = "appointment:read,appointment:update,appointment:delete,patient:read,specialisation:read";
    public static final String STAFF_AUTHORITIES = "user:create,user:read,user:update,appointment:create,appointment:read,appointment:update,appointment:delete,specialisation:read";
    public static final String USER_AUTHORITIES = "user:read,appointment:create,appointment:read,appointment:update,appointment:delete,doctor:read,specialisation:read";
    public static final String ADMIN_AUTHORITIES = "user:create,user:read,user:update,appointment:create,appointment:read,appointment:update,appointment:delete,specialisation:read,specialisation:create,specialisation:update,specialisation:delete";
    public static final String SUPER_ADMIN_AUTHORITIES = "user:create,user:read,user:update,user:delete,appointment:create,appointment:read,appointment:update,appointment:delete,specialisation:read,specialisation:create,specialisation:update";
    public static final String MANAGER_AUTHORITIES = "user:create,user:read,user:update,appointment:create,appointment:read,appointment:update,appointment:delete,specialisation:read,specialisation:create,specialisation:update,";
    // Query
    public static final  String SELECT_DOCTORS_QUERY =
            "SELECT DISTINCT doc.id, doc.user_id, doc.doctor_id, doc.first_name, doc.last_name, doc.email, doc.phone, doc.rating,doc.total_patients, doc.total_rating,doc.rating, doc.experience,doc.about_me, doc.image_url, s.name AS specialisation, r.name AS role_name, r.authorities AS role_authorities, doc.image_url, doc.reference_id, doc.account_non_expired, doc.account_non_locked, doc.last_login, doc.created_at, doc.enabled, doc.updated_at FROM doctors doc JOIN doctor_roles dr ON dr.doctor_id = doc.id JOIN roles r ON r.id = dr.role_id JOIN doctor_specialisations ds ON ds.doctor_id = doc.id JOIN specialisations s ON s.id = ds.specialisation_id LEFT JOIN appointment_doctors ad ON ad.doctor_id = doc.id LEFT JOIN appointments appt ON appt.id = ad.appointment_id ORDER BY doc.first_name ASC";

    //    public static final  String SELECT_DOCTORS_QUERY = "SELECT doc.id, doc.user_id, doc.doctor_id, doc.first_name, doc.last_name,doc.email, doc.phone,doc.image_url,s.name AS specialisation, r.name AS role_name,r.authorities AS role_authorities,doc.image_url,   doc.reference_id, doc.account_non_expired,doc.account_non_locked,doc.last_login, doc.created_at,doc.enabled, doc.updated_at FROM doctors doc JOIN doctor_roles dr ON dr.doctor_id = doc.id  JOIN roles r ON r.id = dr.role_id JOIN doctor_specialisations ds ON ds.doctor_id = doc.id JOIN specialisations s ON s.id = ds.specialisation_id LEFT JOIN appointment_doctors ad ON ad.doctor_id = doc.id LEFT JOIN appointments appt ON appt.id = ad.appointment_id  ORDER BY doc.first_name ASC ";
    public static final  String SELECT_DOCTOR_QUERY = "SELECT doc.id, doc.user_id, doc.doctor_id, doc.first_name, doc.last_name,doc.email, doc.phone, doc.rating,doc.total_patients, doc.total_rating,doc.rating, doc.experience,doc.about_me, doc.image_url,s.name AS specialisation, r.name AS role_name,r.authorities AS role_authorities,doc.image_url,   doc.reference_id, doc.account_non_expired,doc.account_non_locked,doc.last_login, doc.created_at,doc.enabled, doc.updated_at FROM doctors doc JOIN doctor_roles dr ON dr.doctor_id = doc.id  JOIN roles r ON r.id = dr.role_id JOIN doctor_specialisations ds ON ds.doctor_id = doc.id JOIN specialisations s ON s.id = ds.specialisation_id LEFT JOIN appointment_doctors ad ON ad.doctor_id = doc.id LEFT JOIN appointments appt ON appt.id = ad.appointment_id  WHERE doc.doctor_id = ?1";
    public static final String SELECT_COUNT_DOCTORS_QUERY = "SELECT COUNT(*) FROM doctors";

    public static final  String SELECT_DOCTORS_BY_NAME_QUERY ="SELECT DISTINCT doc.id, doc.user_id, doc.doctor_id, doc.first_name, doc.last_name, doc.email, doc.phone, doc.rating,doc.total_patients, doc.total_rating,doc.rating, doc.experience,doc.about_me, doc.image_url, s.name AS specialisation, r.name AS role_name, r.authorities AS role_authorities, doc.image_url, doc.reference_id, doc.account_non_expired, doc.account_non_locked, doc.last_login, doc.created_at, doc.enabled, doc.updated_at FROM doctors doc JOIN doctor_roles dr ON dr.doctor_id = doc.id JOIN roles r ON r.id = dr.role_id JOIN doctor_specialisations ds ON ds.doctor_id = doc.id JOIN specialisations s ON s.id = ds.specialisation_id LEFT JOIN appointment_doctors ad ON ad.doctor_id = doc.id LEFT JOIN appointments appt ON appt.id = ad.appointment_id WHERE doc.first_name LIKE CONCAT('%', :name, '%') OR doc.last_name LIKE CONCAT('%', :name, '%')";

    public static final String SELECT_COUNT_DOCTORS_BY_NAME_QUERY = "SELECT COUNT(*) FROM doctors  WHERE first_name LIKE CONCAT('%', :name, '%') OR last_name LIKE CONCAT('%', :name, '%')";

//    Patients query
    public static final  String SELECT_PATIENTS_QUERY = "SELECT DISTINCT pat.id, pat.user_id, pat.patient_id, pat.first_name, pat.last_name, pat.email, pat.phone, pat.image_url, r.name AS role_name,r.authorities AS role_authorities,pat.image_url,   pat.reference_id, pat.account_non_expired, pat.account_non_locked, pat.last_login, pat.created_at, pat.enabled, pat.updated_at FROM patients pat JOIN patient_roles pr ON pr.patient_id = pat.id  JOIN roles r ON r.id = pr.role_id LEFT JOIN appointment_patients ap ON ap.patient_id = pat.id  LEFT JOIN appointments appt ON appt.id = ap.appointment_id  ORDER BY pat.first_name ASC ";
    public static final String SELECT_COUNT_PATIENTS_QUERY = "SELECT COUNT(*) FROM patients";
    public static final  String SELECT_PATIENTS_BY_NAME_QUERY ="SELECT DISTINCT pat.id, pat.user_id, pat.patient_id, pat.first_name, pat.last_name, pat.email, pat.phone, pat.image_url, r.name AS role_name,r.authorities AS role_authorities,pat.image_url,   pat.reference_id, pat.account_non_expired, pat.account_non_locked, pat.last_login, pat.created_at, pat.enabled, pat.updated_at FROM patients pat JOIN patient_roles pr ON pr.patient_id = pat.id  JOIN roles r ON r.id = pr.role_id LEFT JOIN appointment_patients ap ON ap.patient_id = pat.id LEFT JOIN appointments appt ON appt.id = ap.appointment_id  WHERE pat.first_name LIKE CONCAT('%', :name, '%') OR pat.last_name LIKE CONCAT('%', :name, '%')";
    public static final String SELECT_COUNT_PATIENTS_BY_NAME_QUERY = "SELECT COUNT(*) FROM patients  WHERE first_name LIKE CONCAT('%', :name, '%') OR last_name LIKE CONCAT('%', :name, '%')";
}
