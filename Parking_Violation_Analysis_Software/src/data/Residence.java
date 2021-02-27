package data;

public class Residence {
	double marketValue;		 //dollars
	double totalLivableArea; //dollars
	String zipCode;			 //String because zip codes have leading zeros
	
	public Residence (double marketValue, double totalLivableArea, String zipCode) {
		this.marketValue = marketValue;
		this.totalLivableArea = totalLivableArea;
		this.zipCode = zipCode;
	}

	public double getMarketValue() {
		return marketValue;
	}

	public double getTotalLivableArea() {
		return totalLivableArea;
	}

	public String getZipCode() {
		return zipCode;
	}
}
