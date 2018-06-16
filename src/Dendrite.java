import java.util.ArrayList;
import java.util.List;

/**
 * Data class for incoming signals
 *
 */
public class Dendrite {
	
	private double signal = 0;
	private DendriteReceptor receptor1;
	private DendriteReceptor receptor2;
	
	public static final int DOPAMINE = 1;
	public static final int ANTIDOPAMINE = 2;

	/**
	 * Instantiates the incoming signal and receptors on the dendrite
	 * @param signal
	 */
	public Dendrite() {
		receptor1 = new DendriteReceptor(DOPAMINE);
		receptor2 = new DendriteReceptor(ANTIDOPAMINE);
	}
	
	/**
	 * Checks if a signal is present
	 * @return signal
	 */
	public boolean isSignalPresent() {
		return (signal > 0);
	}
	
	/**
	 * Propgates the signal forward
	 * @param signal
	 */
	public void setSignal(double signal){
		this.signal = signal;
	}
	
	/**
	 * Return the list of receptors
	 * @return
	 */
	public List<DendriteReceptor> getReceptors() {
		List<DendriteReceptor> list = new ArrayList<>();
		list.add(receptor1);
		list.add(receptor2);
		return list;
	}
}
