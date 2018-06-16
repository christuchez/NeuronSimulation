import java.util.ArrayList;
import java.util.List;

/**
 * Data class to send vesicles across the Synapse
 *
 */
public class AxonTerminal {
	
	private double signal;
	private List<Vesicle> vesiclesList;
	private static final int NUM_VESICLES = 3;
	
	/**
 	 * Instantiates default 3 number of vesicles
 	 * @param signal
 	 */
	public AxonTerminal() {
		signal = 0;
		vesiclesList = new ArrayList<>();
		for(int i = 0; i < NUM_VESICLES; i++) {
			vesiclesList.add(new Vesicle());
		}

	}
	
	/**
	 * Returns all vesicles it contains
	 * @return
	 */
	public List<Vesicle> getVesicles() {
		return vesiclesList;
	}
	
	/**
	 * Sets the signal if vesicles are to be sent accross Synapse
	 * @param signal
	 */
	public void setSignal(double signal) {
		this.signal = signal;
	}
}
