/**
 * Purpose: Used to represent a parking slot.
 * @author Tung Tran <103432596>
 * @version JDK 21
 * Class COS70006 Tuesday 18:30
 */

public class ParkingSlot {
    private String slotId;
    private boolean isStaffSlot;
    private Car parkedCar;

    /**
     * Constructs a new ParkingSlot with the given slot ID and type.
     *
     * @param slotId      the unique ID of the slot (format: one uppercase letter followed by two digits, e.g., "F01")
     * @param isStaffSlot true if the slot is for staff, false if it is for visitors
     * @throws IllegalArgumentException if the slot ID does not match the required format
     */
    public ParkingSlot(String slotId, boolean isStaffSlot) {
        if (!slotId.matches("^[A-Z]\\d{2}$")) {
            throw new IllegalArgumentException("Invalid slot ID format (e.g. F01).");
        }
        this.slotId = slotId;
        this.isStaffSlot = isStaffSlot;
    }

    /**
     * Returns the unique ID of this slot.
     *
     * @return the slot ID
     */
    public String getSlotId() {
        return slotId;
    }

    /**
     * Indicates whether this slot is reserved for staff or visitor.
     *
     * @return true if this is a staff slot, false if it is a visitor slot
     */
    public boolean isStaffSlot() {
        return isStaffSlot;
    }

    /**
     * Checks whether this slot is currently occupied by a car.
     *
     * @return true if a car is parked in this slot, false otherwise
     */
    public boolean isOccupied() {
        return parkedCar != null;
    }

    /**
     * Returns the car currently parked in this slot.
     *
     * @return the parked Car, or null if the slot is empty
     */
    public Car getParkedCar() {
        return parkedCar;
    }

    /**
     * Attempts to park a car in this slot.
     * 
     * A car can only be parked if the slot is unoccupied and the car type
     * (staff or visitor) matches the slot type. When parked, the current time
     * is recorded in the Car object.
     *
     * @param car the Car to park
     * @return true if the car was successfully parked, false if not
     */
    public boolean parkCar(Car car) {
        if (isOccupied()) return false;
        if (car.isStaff() != isStaffSlot) return false; // staff/visitor mismatch
        this.parkedCar = car;
        car.setParkedTime(java.time.LocalDateTime.now()); // record parked time
        return true;
    }

    /**
     * Removes the car from this slot, if one is parked.
     *
     * @return true if a car was successfully removed, false if the slot was already empty
     */
    public boolean removeCar() {
        if (!isOccupied()) return false;
        this.parkedCar = null;
        return true;
    }

    /**
     * Returns a string representation of this parking slot, including its ID,
     * type (staff or visitor), and occupancy status.
     *
     * @return a string representation of the slot
     */
    @Override
    public String toString() {
        String type = isStaffSlot ? "Staff" : "Visitor";
        if (isOccupied()) {
            return slotId + " [" + type + "] - Occupied by " + parkedCar.toString();
        } else {
            return slotId + " [" + type + "] - Empty";
        }
    }
}