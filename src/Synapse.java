import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Region between axon terminals and dendrites managing the sending of neurotransmitters (signals) between neurons
 *
 */
public class Synapse {
	
	private List<AxonTerminal> axonTerminalList;
	private List<Dendrite> dendriteList;
	List<Neurotransmitter> neurotransmitterList = new ArrayList<>();
	private double signal = 0;
	private boolean active = false;
	private int i;
	private int j;
	

	/**
	 * Base Constructor
	 */
	public Synapse() { }
	
	/**
	 * Constructor for meshes
	 * @param x
	 * @param j
	 */
	public Synapse(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	/**
	 * Sends the signal from each axon terminal to an available dendrite
	 */
	public void process() {
		for(AxonTerminal anAxonTerminal : axonTerminalList){
			List<Vesicle> vesicles = anAxonTerminal.getVesicles();  //takes neurotransmitters out of axonterminals and adds to big list
			for(Vesicle vesicle : vesicles) {                       //of neurotransmitters
				List<Neurotransmitter> localList = vesicle.getVesicle();
				for(Neurotransmitter aTransmitter : localList){
					//System.out.println(aTransmitter.getTransmitterType());
					neurotransmitterList.add(aTransmitter);
				}
			}
		}
		
		for(Dendrite aDendrite : dendriteList){
			signal = calculateSignal();
			aDendrite.setSignal(signal);
		}
	}
	
	/**
	 * Get the ID of the synapse for the mesh
	 * @return
	 */
	public int[] getSynapseID() {
		return new int[]{i, j};
	}
	
	/**
	 * Post synpatic strength is between 0.4 to 20 meV
	 * @return
	 */
	private double calculateSignal() {
		Random random = new Random();
		double randomValue = 0.4 + (20 - 0.4) * random.nextDouble();
	    return randomValue;
	}

	/**
	 * Gets the axonterminals associated with this synapse
	 * @return
	 */
	public List<AxonTerminal> getAxonTerminalList() {
		return axonTerminalList;
	}

	/**
	 * Set the axon terminals from the sending neuron for this synapse
	 * @param axonTerminalList
	 */
	public void setAxonTerminalList(List<AxonTerminal> axonTerminalList) {
		this.axonTerminalList = axonTerminalList;
	}

	/**
	 * Returns the list of dendrites in a synapse
	 * @return
	 */
	public List<Dendrite> getDendriteList() {
		return dendriteList;
	}

	/**
	 * Sets the dendrites of the signal receiving neuron to the synapse
	 * @param dendriteList
	 */
	public void setDendriteList(List<Dendrite> dendriteList) {
		this.dendriteList = dendriteList;
	}
	
	/**
	 * Sets the synapse active
	 * @param active
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
	/**
	 * Checks if the synpase is active
	 * @return
	 */
	public boolean isActive() {
		return active;
	}
}
