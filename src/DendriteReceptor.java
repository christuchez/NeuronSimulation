/**
 * Handles interaction between the neurotransmitter and the signal-receiving neuron
 *
 */
public class DendriteReceptor {
	
	private int receptorType;
	private Neurotransmitter neuroTransmitter;
	
	
	/**
	 * Each receptor contains a specific receptor type determined at instantiation
	 * @param receptorType
	 */
	public DendriteReceptor(int receptorType) {
		 this.receptorType= receptorType;
		 neuroTransmitter = null;
	}

	/**
	 * Sets the neurotransmiter for this receptor if the neurotransmitter type matches the receptor type
	 * @param neurotransmitter
	 * @return
	 */
	public boolean setNeurotransmitter(Neurotransmitter neurotransmitter) {
		if(receptorType == neurotransmitter.getTransmitterType()) {
			this.neuroTransmitter = neurotransmitter;
			return true;
		}
		return false;
	}
	
	/**
	 * Empty the receptor of any neurotransmitters
	 */
	public void emptyReceptor() {
		neuroTransmitter = null;
	}
	
	/**
	 * Returns the type of the receptor
	 * @return
	 */
	public int getReceptorType() {
		return receptorType;
	}
	
	/**
	 * Checks if the receptor is empty
	 * @return
	 */
	public boolean isEmpty(){
		return neuroTransmitter == null;
	}
	
}
