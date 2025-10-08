import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * Main GUI application for the Parking Spot System.
 * Responsibilities:
 * - Render slots as interactive buttons
 * - Provide operations via controls
 * - Validate input and show messages
 * - Delegate business logic to CarPark, ParkingSlot, Car
 */
public class Application extends JFrame {

    private final CarPark carPark = new CarPark();
    private final JPanel slotGrid = new JPanel(new GridLayout(0, 8, 8, 8)); // dynamic rows, 8 columns
    private final JPanel controlPanel = new JPanel();
    private final JLabel statusBar = new JLabel("Welcome to Parking Spot System");

    private final Color staffColor = new Color(30, 60, 150);
    private final Color visitorColor = new Color(25, 120, 60);
    private final Color occupiedBorderColor = new Color(240, 200, 40);

    /**
     * Custom button representing a parking slot in the grid.
     */
    private static class SlotButton extends JButton {
        private final String slotId;

        SlotButton(String slotId) {
            super(slotId);
            this.slotId = slotId;
            setFocusPainted(false);
            setForeground(Color.WHITE);
            setFont(getFont().deriveFont(Font.BOLD, 12f));
        }

        public String getSlotId() {
            return slotId;
        }
    }

    /**
     * Create and show the application window.
     */
    public Application() {
        super("Parking Spot System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initControls();
        add(new JScrollPane(slotGrid), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initialize the controls panel (left side).
     */
    private void initControls() {
        controlPanel.setLayout(new GridBagLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);

        // Section: Initialize slots
        JLabel initLabel = new JLabel("Initialize Car Park");
        initLabel.setFont(initLabel.getFont().deriveFont(Font.BOLD));
        controlPanel.add(initLabel, gbc);

        JButton generateBtn = new JButton("Generate Slots");
        generateBtn.addActionListener(e -> onGenerateSlots());
        controlPanel.add(generateBtn, gbc);

        controlPanel.add(new JSeparator(), gbc);

        // Section: Operations
        JLabel opsLabel = new JLabel("Operations");
        opsLabel.setFont(opsLabel.getFont().deriveFont(Font.BOLD));
        controlPanel.add(opsLabel, gbc);

        JButton addSlotBtn = new JButton("Add Slot");
        addSlotBtn.addActionListener(e -> onAddSlot());
        controlPanel.add(addSlotBtn, gbc);

        JButton delSlotBtn = new JButton("Delete Slot");
        delSlotBtn.addActionListener(e -> onDeleteSlot());
        controlPanel.add(delSlotBtn, gbc);

        JButton listAllBtn = new JButton("List All Slots");
        listAllBtn.addActionListener(e -> onListAllSlots());
        controlPanel.add(listAllBtn, gbc);

        JButton delUnoccBtn = new JButton("Delete Unoccupied Slots");
        delUnoccBtn.addActionListener(e -> onDeleteUnoccupiedSlots());
        controlPanel.add(delUnoccBtn, gbc);

        JButton parkCarBtn = new JButton("Park Car");
        parkCarBtn.addActionListener(e -> onParkCar());
        controlPanel.add(parkCarBtn, gbc);

        JButton findCarBtn = new JButton("Find Car");
        findCarBtn.addActionListener(e -> onFindCar());
        controlPanel.add(findCarBtn, gbc);

        JButton removeCarBtn = new JButton("Remove Car");
        removeCarBtn.addActionListener(e -> onRemoveCar());
        controlPanel.add(removeCarBtn, gbc);

        JButton exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> onExit());
        controlPanel.add(exitBtn, gbc);
    }

    /**
     * Render all current slots into the grid.
     */
    private void renderSlots() {
        slotGrid.removeAll();

        // Sort by ID for consistent layout
        Collection<ParkingSlot> slots = carPark.listSlots();
        java.util.List<ParkingSlot> sorted = new ArrayList<>(slots);
        sorted.sort(Comparator.comparing(ParkingSlot::getSlotId));

        for (ParkingSlot slot : sorted) {
            SlotButton btn = new SlotButton(slot.getSlotId());
            styleSlotButton(btn, slot);
            attachSlotButtonHandlers(btn, slot);
            slotGrid.add(btn);
        }

        slotGrid.revalidate();
        slotGrid.repaint();
    }

    /**
     * Style a slot button according to type and occupancy.
     */
    private void styleSlotButton(SlotButton btn, ParkingSlot slot) {
        boolean staff = slot.isStaffSlot();
        btn.setBackground(staff ? staffColor : visitorColor);

        if (slot.isOccupied()) {
            btn.setBorder(new LineBorder(occupiedBorderColor, 3));
            Car car = slot.getParkedCar();
            String tt = buildSlotTooltip(slot, car);
            btn.setToolTipText(tt);
        } else {
            btn.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            btn.setToolTipText(staff ? "Staff slot (empty)" : "Visitor slot (empty)");
        }
    }

    /**
     * Attach click handlers to a slot button for advanced interaction.
     */
    private void attachSlotButtonHandlers(SlotButton btn, ParkingSlot slot) {
        btn.addActionListener(e -> {
            if (slot.isOccupied()) {
                int choice = JOptionPane.showConfirmDialog(this,
                    "Remove car from slot " + slot.getSlotId() + "?",
                    "Remove Car", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    slot.removeCar();
                    setStatus("Car removed from " + slot.getSlotId(), true);
                    renderSlots();
                }
            } else {
                onParkCarToSlot(slot.getSlotId());
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (slot.isOccupied()) {
                        warn("Cannot delete occupied slot " + slot.getSlotId());
                    } else {
                        int choice = JOptionPane.showConfirmDialog(Application.this,
                            "Delete slot " + slot.getSlotId() + "?",
                            "Delete Slot", JOptionPane.YES_NO_OPTION);
                        if (choice == JOptionPane.YES_OPTION) {
                            if (carPark.deleteSlot(slot.getSlotId())) {
                                setStatus("Deleted slot " + slot.getSlotId(), true);
                                renderSlots();
                            } else {
                                warn("Failed to delete slot " + slot.getSlotId());
                            }
                        }
                    }
                }
            }
        });
    }

    // =========================
    // Operation handlers
    // =========================

    private void onGenerateSlots() {
        JTextField staffField = new JTextField();
        JTextField visitorField = new JTextField();
        Object[] msg = {
            "Number of staff slots:", staffField,
            "Number of visitor slots:", visitorField
        };
        int res = JOptionPane.showConfirmDialog(this, msg, "Generate Slots",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        int staffCount, visitorCount;
        try {
            staffCount = Integer.parseInt(staffField.getText().trim());
            visitorCount = Integer.parseInt(visitorField.getText().trim());
            if (staffCount < 0 || visitorCount < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            warn("Please enter non-negative integers for slot counts.");
            return;
        }

        // Generate IDs: Staff F01.., Visitor T01..
        for (int i = 1; i <= staffCount; i++) {
            String id = String.format("F%02d", i);
            addSlotInternal(id, true);
        }
        for (int i = 1; i <= visitorCount; i++) {
            String id = String.format("T%02d", i);
            addSlotInternal(id, false);
        }
        renderSlots();
        setStatus("Generated " + staffCount + " staff and " + visitorCount + " visitor slots.", true);
    }

    private void onAddSlot() {
        JTextField idField = new JTextField();
        String[] types = {"Staff", "Visitor"};
        JComboBox<String> typeBox = new JComboBox<>(types);
        Object[] msg = {"Slot ID (e.g., F01):", idField, "Type:", typeBox};
        int res = JOptionPane.showConfirmDialog(this, msg, "Add Slot",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        String id = idField.getText().trim();
        if (!id.matches("^[A-Z]\\d{2}$")) {
            warn("Invalid Slot ID. Use an uppercase letter followed by 2 digits (e.g., F01).");
            return;
        }
        boolean isStaff = typeBox.getSelectedItem().equals("Staff");
        if (!addSlotInternal(id, isStaff)) {
            warn("Slot " + id + " already exists.");
            return;
        }
        renderSlots();
        setStatus("Added slot " + id, true);
    }

    private void onDeleteSlot() {
        String id = JOptionPane.showInputDialog(this, "Enter Slot ID to delete (e.g., F01):");
        if (id == null) return;
        id = id.trim();
        if (!id.matches("^[A-Z]\\d{2}$")) {
            warn("Invalid Slot ID format.");
            return;
        }
        ParkingSlot slot = carPark.findSlot(id);
        if (slot == null) {
            warn("Slot " + id + " not found.");
            return;
        }
        if (slot.isOccupied()) {
            warn("Cannot delete occupied slot " + id + ".");
            return;
        }
        if (carPark.deleteSlot(id)) {
            renderSlots();
            setStatus("Deleted slot " + id, true);
        } else {
            warn("Failed to delete slot " + id + ".");
        }
    }

    private void onListAllSlots() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-6s %-8s %-10s %-10s %-20s %-10s%n",
                "ID", "Type", "Occupied", "Reg", "Owner", "Duration/Fee"));
        for (ParkingSlot slot : carPark.listSlots()) {
            String type = slot.isStaffSlot() ? "Staff" : "Visitor";
            if (slot.isOccupied()) {
                Car car = slot.getParkedCar();
                String dur = formatDuration(car.getParkedTime(), LocalDateTime.now());
                String fee = "$" + computeFee(car.getParkedTime(), LocalDateTime.now());
                sb.append(String.format("%-6s %-8s %-10s %-10s %-20s %-10s%n",
                        slot.getSlotId(), type, "Yes",
                        car.getRegistrationNumber(), car.getOwner(), dur + " / " + fee));
            } else {
                sb.append(String.format("%-6s %-8s %-10s %-10s %-20s %-10s%n",
                        slot.getSlotId(), type, "No", "-", "-", "-"));
            }
        }
        JTextArea area = new JTextArea(sb.toString(), 20, 60);
        area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "All Slots", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onDeleteUnoccupiedSlots() {
        carPark.deleteAllUnoccupiedSlots();
        renderSlots();
        setStatus("Deleted all unoccupied slots.", true);
    }

    private void onParkCar() {
        JTextField slotField = new JTextField();
        JTextField regField = new JTextField();
        JTextField ownerField = new JTextField();
        JCheckBox staffChk = new JCheckBox("Staff car");
        Object[] msg = {
            "Slot ID (e.g., F01):", slotField,
            "Car registration (e.g., T1234):", regField,
            "Owner:", ownerField,
            staffChk
        };
        int res = JOptionPane.showConfirmDialog(this, msg, "Park Car",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        parkCarInternal(slotField.getText().trim(), regField.getText().trim(),
                ownerField.getText().trim(), staffChk.isSelected());
    }

    private void onParkCarToSlot(String slotId) {
        JTextField regField = new JTextField();
        JTextField ownerField = new JTextField();
        JCheckBox staffChk = new JCheckBox("Staff car");
        Object[] msg = {
            "Slot: " + slotId,
            "Car registration (e.g., T1234):", regField,
            "Owner:", ownerField,
            staffChk
        };
        int res = JOptionPane.showConfirmDialog(this, msg, "Park Car",
                JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        parkCarInternal(slotId, regField.getText().trim(),
                ownerField.getText().trim(), staffChk.isSelected());
    }

    private void onFindCar() {
        String reg = JOptionPane.showInputDialog(this, "Enter car registration (e.g., T1234):");
        if (reg == null) return;
        reg = reg.trim();
        if (!reg.matches("^[A-Z]\\d{4}$")) {
            warn("Invalid registration format.");
            return;
        }
        ParkingSlot slot = carPark.findCar(reg);
        if (slot == null) {
            warn("Car " + reg + " not found.");
            return;
        }
        Car car = slot.getParkedCar();
        String msg = "Car: " + reg + "\nOwner: " + car.getOwner() + "\nSlot: " + slot.getSlotId();
        if (car.getParkedTime() != null) {
            String dur = formatDuration(car.getParkedTime(), LocalDateTime.now());
            int fee = computeFee(car.getParkedTime(), LocalDateTime.now());
            msg += "\nParked: " + car.getParkedTime() + "\nDuration: " + dur + "\nFee: $" + fee;
        }
        JOptionPane.showMessageDialog(this, msg, "Find Car", JOptionPane.INFORMATION_MESSAGE);
        setStatus("Found car " + reg + " in slot " + slot.getSlotId(), true);
    }

    private void onRemoveCar() {
        String reg = JOptionPane.showInputDialog(this, "Enter car registration (e.g., T1234):");
        if (reg == null) return;
        reg = reg.trim();
        if (!reg.matches("^[A-Z]\\d{4}$")) {
            warn("Invalid registration format.");
            return;
        }
        boolean ok = carPark.removeCar(reg);
        if (ok) {
            renderSlots();
            setStatus("Removed car " + reg, true);
        } else {
            warn("Car " + reg + " not found.");
        }
    }

    private void onExit() {
        JOptionPane.showMessageDialog(this, "Program end!");
        dispose();
    }

    // =========================
    // Helpers
    // =========================

    private boolean addSlotInternal(String id, boolean isStaff) {
        if (!id.matches("^[A-Z]\\d{2}$")) return false;
        ParkingSlot slot = new ParkingSlot(id, isStaff);
        return carPark.addSlot(slot);
    }

    private void parkCarInternal(String slotId, String reg, String owner, boolean isStaffCar) {
        if (!slotId.matches("^[A-Z]\\d{2}$")) {
            warn("Invalid Slot ID format.");
            return;
        }
        if (!reg.matches("^[A-Z]\\d{4}$")) {
            warn("Invalid registration number format (e.g., T1234).");
            return;
        }
        ParkingSlot slot = carPark.findSlot(slotId);
        if (slot == null) {
            warn("Slot " + slotId + " not found.");
            return;
        }
        // ensure car not already parked
        if (carPark.findCar(reg) != null) {
            warn("Car " + reg + " is already parked in another slot.");
            return;
        }
        Car car = new Car(reg, owner, isStaffCar);
        if (!slot.parkCar(car)) {
            if (slot.isOccupied()) warn("Slot " + slotId + " is occupied.");
            else warn("Car type must match slot type.");
            return;
        }
        setStatus("Car parked in " + slotId + " at " + car.getParkedTime(), true);
        renderSlots();
        JOptionPane.showMessageDialog(this,
                "Car parked at: " + car.getParkedTime(),
                "Park Car", JOptionPane.INFORMATION_MESSAGE);
    }

    private String buildSlotTooltip(ParkingSlot slot, Car car) {
        StringBuilder sb = new StringBuilder();
        sb.append(slot.getSlotId()).append(" - ").append(slot.isStaffSlot() ? "Staff" : "Visitor");
        sb.append("\nOccupied by: ").append(car.getRegistrationNumber())
          .append(" (").append(car.getOwner()).append(")");
        if (car.getParkedTime() != null) {
            String dur = formatDuration(car.getParkedTime(), LocalDateTime.now());
            int fee = computeFee(car.getParkedTime(), LocalDateTime.now());
            sb.append("\nParked: ").append(car.getParkedTime())
              .append("\nDuration: ").append(dur)
              .append("\nFee: $").append(fee);
        }
        return "<html>" + sb.toString().replace("\n", "<br/>") + "</html>";
    }

    private String formatDuration(LocalDateTime start, LocalDateTime end) {
        Duration d = Duration.between(start, end);
        long seconds = d.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        return hours + " hours " + minutes + " minutes " + secs + " seconds";
    }

    private int computeFee(LocalDateTime start, LocalDateTime end) {
        Duration d = Duration.between(start, end);
        long seconds = Math.max(d.getSeconds(), 1);
        double hours = seconds / 3600.0;
        int billableHours = (int) Math.ceil(hours);
        return billableHours * 6;
    }

    private void setStatus(String msg, boolean success) {
        statusBar.setText(msg);
        statusBar.setForeground(success ? new Color(0, 130, 0) : new Color(170, 0, 0));
    }

    private void warn(String msg) {
        setStatus(msg, false);
        JOptionPane.showMessageDialog(this, msg, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    // Entry point for GUI-based Project 2; ensure Car, ParkingSlot, CarPark are present.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}