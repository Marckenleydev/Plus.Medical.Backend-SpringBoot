package marc.dev.DoctorBooking_appointement.exception;



public class ApiException extends RuntimeException {
    public ApiException(String message) { super(message); }
    public ApiException() { super("An error occurred"); }
}
