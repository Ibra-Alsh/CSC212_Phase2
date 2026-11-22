public abstract class Ride implements IRide {
    private final int rideId;
    private final String pickupLocation;
    private final String dropoffLocation;
    private final ITimeSlot timeSlot;
    private final IDriver driver;

    protected Ride(int rideId, String pickupLocation, String dropoffLocation, ITimeSlot timeSlot, IDriver driver) {
        this.rideId = rideId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.timeSlot = timeSlot;
        this.driver = driver;
    }

    @Override 
    public int getRideId() { 
        return rideId; 
    }

    @Override 
    public String getPickupLocation() { 
        return pickupLocation; 
    }
    
    @Override 
    public String getDropoffLocation() { 
        return dropoffLocation; 
    }

    @Override 
    public ITimeSlot getTimeSlot() { 
        return timeSlot; 
    }

    @Override 
    public IDriver getDriver() { 
        return driver; 
    }

    @Override
    public int compareTo(IRide other) {
        return Integer.compare(rideId, other.getRideId());
    }

    @Override
    public String toString() {
        return "Ride: " + rideId + ", pickupLocation: " + pickupLocation + ", dropoffLocation: " + dropoffLocation + ", pickupTime: " + timeSlot.getStart().format() + ", dropoffTime: " + timeSlot.getEnd().format() + ", driver: " + driver.getId();
    }
}
