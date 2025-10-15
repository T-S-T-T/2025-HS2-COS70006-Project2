import java.util.*;

/**
 * Purpose: Used to represent a whole car park.
 * @author Tung Tran <103432596>
 * @version JDK 21
 * Class COS70006 Tuesday 18:30
 */
public class CarPark {

    /**
     * A map of parking slots in the car park, keyed by their unique slot ID.
     */
    private Map<String, ParkingSlot> slots = new HashMap<>();

    /**
     * Adds a new parking slot to the car park.
     *
     * @param slot the ParkingSlot to add
     * @return true if the slot was successfully added, false if a slot with the same ID already exists
     */
    public boolean addSlot(ParkingSlot slot) {
        if (slots.containsKey(slot.getSlotId())) return false;
        slots.put(slot.getSlotId(), slot);
        return true;
    }

    /**
     * Deletes a parking slot from the car park if it exists and is unoccupied.
     *
     * @param slotId the ID of the slot to delete
     * @return true if the slot was successfully deleted, false} if the slot does not exist or is currently occupied
     */
    public boolean deleteSlot(String slotId) {
        ParkingSlot slot = slots.get(slotId);
        if (slot == null || slot.isOccupied()) return false;
        slots.remove(slotId);
        return true;
    }

    /**
     * Deletes all unoccupied parking slots from the car park.
     * Occupied slots remain untouched.
     */
    public void deleteAllUnoccupiedSlots() {
        slots.values().removeIf(slot -> !slot.isOccupied());
    }

    /**
     * Finds a parking slot by its ID.
     *
     * @param slotId the ID of the slot to find
     * @return the ParkingSlot with the given ID, or null if no such slot exists
     */
    public ParkingSlot findSlot(String slotId) {
        return slots.get(slotId);
    }

    /**
     * Finds the parking slot where a car with the given registration number is parked.
     *
     * @param registrationNumber the registration number of the car
     * @return the ParkingSlot containing the car, or null if the car is not found in any slot
     */
    public ParkingSlot findCar(String registrationNumber) {
        for (ParkingSlot slot : slots.values()) {
            if (slot.isOccupied() && slot.getParkedCar().getRegistrationNumber().equals(registrationNumber)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * Removes a car from the car park by its registration number.
     *
     * @param registrationNumber the registration number of the car to remove
     * @return true if the car was successfully removed, false if the car was not found
     */
    public boolean removeCar(String registrationNumber) {
        ParkingSlot slot = findCar(registrationNumber);
        if (slot == null) return false;
        return slot.removeCar();
    }

    /**
     * Returns a collection of all parking slots in the car park.
     *
     * @return a Collection of ParkingSlot objects
     */
    public Collection<ParkingSlot> listSlots() {
        return slots.values();
    }
}