public class Driver extends Person implements IDriver {
    
    private String vehiclePlate;
    private VehicleType vehicleType;

    public Driver(int id, String name, String phoneNumber, String vehiclePlate, VehicleType vehicleType) {
        super(id, name, phoneNumber);
        setVehiclePlate(vehiclePlate);
        setVehicleType(vehicleType);
    }


    public void setVehiclePlate(String vehiclePlate) {
        if (vehiclePlate == null || !vehiclePlate.matches("[A-Z]{3}[0-9]{4}")) {
            throw new IllegalArgumentException("Vehicle plate must contain 3 uppercase letters followed by 4 digits.");
        }
        this.vehiclePlate = vehiclePlate;
    }

    public void setVehicleType(VehicleType vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null.");
        }
        this.vehicleType = vehicleType;
    }

    @Override
    public String getVehiclePlate() { 
        return vehiclePlate; 
    }

    @Override
    public VehicleType getVehicleType() { 
        return vehicleType; 
    }

    @Override
    public int compareTo(IDriver other) {
        if (this.getId() < other.getId()) {
            return -1;
        } else if (this.getId() > other.getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Driver: " + getId() + ", name: " + getName() + ", phoneNumber: " + getPhoneNumber() + ", vehiclePlate: " + vehiclePlate + ", vehicleType: " + vehicleType;
    }
}
