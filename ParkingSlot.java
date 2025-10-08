/**
 * Represents a parking slot in the car park system.
 * <p>
 * Each parking slot has a unique identifier (slot ID), a type indicating
 * whether it is reserved for staff or visitors, and may contain a parked car.
 * The class enforces rules such as allowing only one car per slot and ensuring
 * that staff cars can only be parked in staff slots, and visitor cars in visitor slots.
 * </p>
 */
public class ParkingSlot {

    /**
     * The unique identifier of the parking slot.
     * Must follow the format: one uppercase letter followed by two digits (e.g., "F01").
     */
    private String slotId;

    /**
     * Indicates whether this slot is reserved for staff (true) or visitors (false).
     */
    private boolean isStaffSlot;

    /**
     * The car currently parked in this slot, or {@code null} if the slot is empty.
     */
    private Car parkedCar;

    /**
     * Constructs a new {@code ParkingSlot} with the given slot ID and type.
     *
     * @param slotId      the unique identifier of the slot (format: one uppercase letter followed by two digits, e.g., "F01")
     * @param isStaffSlot {@code true} if the slot is for staff, {@code false} if it is for visitors
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
     * Returns the unique identifier of this slot.
     *
     * @return the slot ID
     */
    public String getSlotId() {
        return slotId;
    }

    /**
     * Indicates whether this slot is reserved for staff.
     *
     * @return {@code true} if this is a staff slot, {@code false} if it is a visitor slot
     */
    public boolean isStaffSlot() {
        return isStaffSlot;
    }

    /**
     * Checks whether this slot is currently occupied by a car.
     *
     * @return {@code true} if a car is parked in this slot, {@code false} otherwise
     */
    public boolean isOccupied() {
        return parkedCar != null;
    }

    /**
     * Returns the car currently parked in this slot.
     *
     * @return the parked {@link Car}, or {@code null} if the slot is empty
     */
    public Car getParkedCar() {
        return parkedCar;
    }

    /**
     * Attempts to park a car in this slot.
     * <p>
     * A car can only be parked if the slot is unoccupied and the car type
     * (staff or visitor) matches the slot type. When parked, the current time
     * is recorded in the car object.
     * </p>
     *
     * @param car the {@link Car} to park
     * @return {@code true} if the car was successfully parked, {@code false} otherwise
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
     * @return {@code true} if a car was successfully removed, {@code false} if the slot was already empty
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