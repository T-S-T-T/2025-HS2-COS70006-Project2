import java.time.LocalDateTime;

/**
 * Purpose: Used to represent a car.
 * @author Tung Tran 103432596
 * @version JDK 21
 * Class COS70006 Tuesday 18:30
 */
public class Car {
    private String registrationNumber;
    private String owner;
    private boolean isStaff;
    private LocalDateTime parkedTime;

    /**
     * Constructs a new Car object with the given registration number, owner, and staff flag.
     *
     * @param registrationNumber the registration number of the car (format: one uppercase letter followed by four digits, e.g., "T1234")
     * @param owner              the name of the car's owner
     * @param isStaff            true if the car belongs to a staff member, false if it belongs to a visitor
     * @throws IllegalArgumentException if the registration number does not match the required format
     */
    public Car(String registrationNumber, String owner, boolean isStaff) {
        if (!registrationNumber.matches("^[A-Z]\\d{4}$")) {
            throw new IllegalArgumentException("Invalid registration number format (e.g. T1234).");
        }
        this.registrationNumber = registrationNumber;
        this.owner = owner;
        this.isStaff = isStaff;
    }

    /**
     * Returns the registration number of the car.
     *
     * @return the registration number
     */
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    /**
     * Returns the name of the car's owner.
     *
     * @return the owner's name
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Indicates whether the car belongs to a staff member.
     *
     * @return true if the car belongs to a staff member, false otherwise
     */
    public boolean isStaff() {
        return isStaff;
    }

    /**
     * Returns the time when the car was parked.
     *
     * @return the parked time, or null if the car has not been parked
     */
    public LocalDateTime getParkedTime() {
        return parkedTime;
    }

    /**
     * Sets the time when the car was parked.
     *
     * @param parkedTime the time the car was parked
     */
    public void setParkedTime(LocalDateTime parkedTime) {
        this.parkedTime = parkedTime;
    }

    /**
     * Returns a string representation of the car, including its registration number, owner, and whether it belongs to staff or a visitor.
     *
     * @return a string representation of the car
     */
    @Override
    public String toString() {
        return registrationNumber + " (" + owner + (isStaff ? ", Staff" : ", Visitor") + ")";
    }
}