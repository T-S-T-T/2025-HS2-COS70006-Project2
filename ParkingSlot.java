public class ParkingSlot {
    private String slotId; // e.g. "F01"
    private boolean isStaffSlot;
    private Car parkedCar;

    public ParkingSlot(String slotId, boolean isStaffSlot) {
        if (!slotId.matches("^[A-Z]\\d{2}$")) {
            throw new IllegalArgumentException("Invalid slot ID format (e.g. F01).");
        }
        this.slotId = slotId;
        this.isStaffSlot = isStaffSlot;
    }

    public String getSlotId() {
        return slotId;
    }

    public boolean isStaffSlot() {
        return isStaffSlot;
    }

    public boolean isOccupied() {
        return parkedCar != null;
    }

    public Car getParkedCar() {
        return parkedCar;
    }

    public boolean parkCar(Car car) {
        if (isOccupied()) return false;
        if (car.isStaff() != isStaffSlot) return false; // staff/visitor mismatch
        this.parkedCar = car;
        car.setParkedTime(java.time.LocalDateTime.now()); // advanced feature
        return true;
    }

    public boolean removeCar() {
        if (!isOccupied()) return false;
        this.parkedCar = null;
        return true;
    }

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