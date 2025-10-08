import java.util.*;

public class CarPark {
    private Map<String, ParkingSlot> slots = new HashMap<>();

    public boolean addSlot(ParkingSlot slot) {
        if (slots.containsKey(slot.getSlotId())) return false;
        slots.put(slot.getSlotId(), slot);
        return true;
    }

    public boolean deleteSlot(String slotId) {
        ParkingSlot slot = slots.get(slotId);
        if (slot == null || slot.isOccupied()) return false;
        slots.remove(slotId);
        return true;
    }

    public void deleteAllUnoccupiedSlots() {
        slots.values().removeIf(slot -> !slot.isOccupied());
    }

    public ParkingSlot findSlot(String slotId) {
        return slots.get(slotId);
    }

    public ParkingSlot findCar(String registrationNumber) {
        for (ParkingSlot slot : slots.values()) {
            if (slot.isOccupied() && slot.getParkedCar().getRegistrationNumber().equals(registrationNumber)) {
                return slot;
            }
        }
        return null;
    }

    public boolean removeCar(String registrationNumber) {
        ParkingSlot slot = findCar(registrationNumber);
        if (slot == null) return false;
        return slot.removeCar();
    }

    public Collection<ParkingSlot> listSlots() {
        return slots.values();
    }
}