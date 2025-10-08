import java.time.LocalDateTime;

public class Car {
    private String registrationNumber; // e.g. "T1234"
    private String owner;
    private boolean isStaff;
    private LocalDateTime parkedTime; // advanced feature

    public Car(String registrationNumber, String owner, boolean isStaff) {
        if (!registrationNumber.matches("^[A-Z]\\d{4}$")) {
            throw new IllegalArgumentException("Invalid registration number format (e.g. T1234).");
        }
        this.registrationNumber = registrationNumber;
        this.owner = owner;
        this.isStaff = isStaff;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isStaff() {
        return isStaff;
    }

    public LocalDateTime getParkedTime() {
        return parkedTime;
    }

    public void setParkedTime(LocalDateTime parkedTime) {
        this.parkedTime = parkedTime;
    }

    @Override
    public String toString() {
        return registrationNumber + " (" + owner + (isStaff ? ", Staff" : ", Visitor") + ")";
    }
}