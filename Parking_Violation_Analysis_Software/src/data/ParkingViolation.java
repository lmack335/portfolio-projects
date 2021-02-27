package data;

public class ParkingViolation {
	double fine;		//dollars
	String plateState;	//two letter state abbreviation, i.e., PA = Pennsylvania
	String zipCode;		//String because zip codes have leading zeros
	int id;				//unique id number
	
	public ParkingViolation(double fine, String plateState, String zipCode) {
		this.fine = fine;
		this.plateState = plateState;
		this.zipCode = zipCode;
	}

	public double getFine() {
		return fine;
	}

	public String getPlateState() {
		return plateState;
	}

	public String getZipCode() {
		return zipCode;
	}
	
}
