package DS_Phase2;

public class TimeSlot implements ITimeSlot {
  private IDateTime start;
  private IDateTime end;
  
  public TimeSlot(IDateTime s, IDateTime e)throws  IllegalArgumentException{
	  if(s.compareTo(e)==0||s.compareTo(e)>=1)
		  throw new IllegalArgumentException("Start time must be before end time.");
	  start=s;
	  end=e;
  }
  
  
  public IDateTime getStart() {
	  return start;
  }
  
  public IDateTime getEnd() {
	  return end;
  }
  
  public int compareTo(ITimeSlot other) {
	  
	  //This time slot is before the other.
	  if(this.end.compareTo(other.getStart())<=0)
		  return -1;
	  
	  //the two time slots overlap.
	  else if(this.end.compareTo(other.getStart())>0 && this.start.compareTo(other.getEnd())<0)
		  return 0;
  //this time slot is after the other.
  else 
	  return 1;
  }
}
