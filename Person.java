package DS_Phase2;


public abstract class Person implements IPerson {
	
	
	protected final int id;
	protected String name;
	protected String phoneNumber;
	protected IBST<ITimeSlot, IRide> schedule;

	
	public Person(int id,String name,String phoneNumber) {
		this.id=id;
		this.name=name;
		setPhoneNumber(phoneNumber);
		schedule=new BST<ITimeSlot,IRide>();
	}
	
	

	public String getName() {
		return name;
	}

	

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if(phoneNumber==null||phoneNumber.length()!=10||!phoneNumber.matches("[0-9]+")) {
		throw new IllegalArgumentException("Phonne number must be exactly 10 digits (numric characters only)");
		}
		this.phoneNumber=phoneNumber;
	}

	public int getId() {
		return id;
	}
	
	public IBST<ITimeSlot, IRide> getSchedule(){
		return schedule;
	}

	@Override
	public String toString() {
		return "Person: " + id + ", name: " + name + ", phoneNumber: " + phoneNumber ;
	}
	
	
	
	




}
