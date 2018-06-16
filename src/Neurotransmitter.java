
/**
 * Physical chemicals that are sent from one neuron to another
 *
 */
public class Neurotransmitter {
	public int type; 
	
	/**
	 * Sets the type of neurotransmitter
	 * @param type
	 */
	public Neurotransmitter(int type) {
		this.type = type;
	}
	
	/**
	 * Returns the neurotransmitter type
	 * @return
	 */
	public int getTransmitterType() {
		return type;
	}
	
}
