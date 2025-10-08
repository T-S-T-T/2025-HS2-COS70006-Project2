import java.time.LocalDateTime;

/**
 * Represents a car that can be parked in the car park system.
 * <p>
 * Each car has a registration number, an owner, and a flag indicating
 * whether the owner is a staff member or a visitor. When parked, the
 * time of parking is recorded for advanced features such as calculating
 * parking duration and fees.
 * </p>
 */
public class Car {

    /**
     * The unique registration number of the car.
     * Must follow the format: one uppercase letter followed by four digits (e.g., "T1234").
     */
    private String registrationNumber;

    /**
     * The name of the car's owner.
     */
    private String owner;

    /**
     * Indicates whether the car belongs to a staff member (true) or a visitor (false).
     */
    private boolean isStaff;

    /**
     * The time when the car was parked.
     * This is recorded when the car is parked in a slot and used to calculate duration and fees.
     */
    private LocalDateTime parkedTime;

    /**
     * Constructs a new {@code Car} object with the given registration number, owner, and staff flag.
     *
     * @param registrationNumber the registration number of the car (format: one uppercase letter followed by four digits, e.g., "T1234")
     * @param owner              the name of the car's owner
     * @param isStaff            {@code true} if the car belongs to a staff member, {@code false} if it belongs to a visitor
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
     * @return {@code true} if the car belongs to a staff member, {@code false} otherwise
     */
    public boolean isStaff() {
        return isStaff;
    }

    /**
     * Returns the time when the car was parked.
     *
     * @return the parked time, or {@code null} if the car has not been parked
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
     * Returns a string representation of the car, including its registration number,
     * owner, and whether it belongs to staff or a visitor.
     *
     * @return a string representation of the car
     */
    @Override
    public String toString() {
        return registrationNumber + " (" + owner + (isStaff ? ", Staff" : ", Visitor") + ")";
    }
}