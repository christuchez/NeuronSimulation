import java.util.ArrayList;
import java.util.List;

/**
 * Data class for communication between the Axon Terminal and Dendrite in the Synapse
 *
 */
public class Vesicle {

	private Neurotransmitter dopamine;
	private Neurotransmitter antidopamine;

	private static final int DOPAMINE     = 1;
	private static final int ANTIDOPAMINE = 2;
	
	private static List<Neurotransmitter> vesicle; 
	
	/**
	 * Instantiates the vesicle with two default neurotransmitters Dopamine and Antidopamine
	 */
	public Vesicle() {
		dopamine = new Neurotransmitter(DOPAMINE);
		antidopamine = new Neurotransmitter(ANTIDOPAMINE);
		vesicle = new ArrayList<>();
		vesicle.add(dopamine);
		vesicle.add(antidopamine);
	}
	
	/**
	 * Returns the vesicle
	 * @return
	 */
	public List<Neurotransmitter> getVesicle() {
		return vesicle;
	}
}
