package processor;

public class LivableAreaAverager implements Averager {
	
	private double totalLivableArea;
	private int totalResidences;
	
	public LivableAreaAverager() {
		this.totalLivableArea = 0;
		this.totalResidences = 0;
	}
	
	@Override
	public double average() {
		// TODO Auto-generated method stub
		if (totalResidences <= 0) {
			return 0;
		}
		else {
			return totalLivableArea / totalResidences;
		}
	}
	
	@Override
	public void update(double newValue) {
		totalLivableArea += newValue;
		totalResidences++;
	}
	

}
