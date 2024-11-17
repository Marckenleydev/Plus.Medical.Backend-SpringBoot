package marc.dev.DoctorBooking_appointement.enumeration;

public enum Status {
    COMPLETED("completed"), CANCELLED("cancelled"),CONFIRMED("confirmed");

    private final String StatusName;

    Status(String statusName) {
        this.StatusName = statusName;
    }

    public String getStatusName() {
        return StatusName;
    }
}
