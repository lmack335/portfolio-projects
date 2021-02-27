package processor;

public class MarketValueAverager implements Averager {
	
	private double totalMarketValue;
	private int totalResidences;
	
	public MarketValueAverager() {
		totalMarketValue = 0;
		totalResidences = 0;
	}
	
	@Override
	public double average() {
		// TODO Auto-generated method stub
		if (totalResidences <= 0) {
			return 0;
		}
		else {
			return totalMarketValue / totalResidences;
		}
	}
	
	@Override
	public void update(double newValue) {
		totalMarketValue += newValue;
		totalResidences++;
	}
	

}
