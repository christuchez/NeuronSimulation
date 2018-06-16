import java.util.ArrayList;
import java.util.List;

/**
 * Creates an OlfactoryReceptor that handles olfactory (smell) stimuli
 */
public class OlfactoryReceptor {
	
	private Mucus mucus;
	private SodiumChannel sodiumChannel;
	private PlasmaMembrane plasmaMembrane;
	private List<AxonTerminal> axonTerminalList;
	
	private double SIGNAL = 0;

	/**
	 * Generates the dendrites the olfactory receptor connects to
	 * @param molecule
	 * @param numDendrites
	 */
	public OlfactoryReceptor(OdorantMolecule molecule, int numDendrites){
		mucus = new Mucus();
		mucus.setOdorantMolecule(molecule);
		plasmaMembrane = new PlasmaMembrane();
		sodiumChannel = plasmaMembrane.getSodiumChannel();
		
		axonTerminalList = new ArrayList<>();
		for(int i = 0; i < numDendrites; i++) {
			axonTerminalList.add(new AxonTerminal());
		}
	}
	
	/**
	 * Processes the signal
	 */
	public void process(){
		mucus.putMoleculeInCilia();
		if(mucus.isCAMPActivated()) {
			sodiumChannel.openGate();
		}
		plasmaMembrane.depolarize();
		sodiumChannel.closeGate();
		SIGNAL = plasmaMembrane.getSignal();
		for(AxonTerminal anAxonTerminal : axonTerminalList){
			anAxonTerminal.setSignal(SIGNAL);
		}
	}
	
	/**
	 * Gets the dendrite this olfactory receptor is connected to
	 * @param index
	 * @return
	 */
	public List<AxonTerminal> getAxonTerminalsAsList() {
		return axonTerminalList;
	}

	
	/**
	 * Data class for Cilia, which are fibrelike hairs found inside nose to collect
	 * odorant molecules
	 * @param <OdorantMolecule>
	 *
	 */
	private class Cilia {

		private OdorantMolecule molecule;
		private boolean adenylylCyclaseActivated = false;
		private boolean cAMPActivated = false;   //cyclic AMP activated with adenyl cyclase
		
		/**
		 * Cilia senses the odorant molecule
		 * @param molecule
		 */
		public void setOdorantMolecule(OdorantMolecule molecule){
			this.molecule = molecule;
			activateCAMP();
		}
		
		/**
		 * Activates the signal transduction process
		 */
		private void activateCAMP(){
			adenylylCyclaseActivated = true;
			cAMPActivated = true;
		}
		
		/**
		 * Checks if signal transduction is occuring
		 * @return
		 */
		public boolean isCAMPActivated(){
			return cAMPActivated;
		}
	}

	 /**
	 * Data wrapper for interaction between the cilia and the mucus
	 * @param <OdorantMolecule>
	 *
	 */
	private class Mucus {
		
		private Cilia cilia;
		private OdorantMolecule molecule; 
		
		/**
		 * Mucus contains cilia
		 */
		public Mucus(){
			cilia = new Cilia();
		}
		
		/**
		 * Sets the orderant molecule
		 * @param molecule
		 */
		public void setOdorantMolecule(OdorantMolecule molecule){
			this.molecule = molecule;
		}
		
		/**
		 * Mucus embraces the cilia and molecule together
		 */
		public void putMoleculeInCilia(){
			cilia.setOdorantMolecule(molecule);
		}
		
		/**
		 * Checks if the cAMP within the cilia is activated
		 * @return
		 */
		public boolean isCAMPActivated(){
			return cilia.isCAMPActivated();
		}
	}
	/**
	 * Handles interaction between intracellular environment and receptor
	 *
	 */
	private class PlasmaMembrane {
		
		private static final double SIGNAL = 1.2;
		private int sodiumIon = 1; //Na+
		private int threshold = 40;
		private int currentVoltage  = 0;
		private SodiumChannel sodiumChannel;
		
		public PlasmaMembrane() {
			sodiumChannel = new SodiumChannel();
		}
		/**
		 * Returns the sodium channel for this plasma membrane
		 * @return
		 */
		public SodiumChannel getSodiumChannel() {
			return sodiumChannel;
		}
		/**
		 * Depolarizes along the receptor to trigger a signal
		 */
		public void depolarize(){
			if(sodiumChannel.getGateStatus().equalsIgnoreCase("open")) {
				for(int i = 1; i <= threshold; i++) {
					currentVoltage = currentVoltage + sodiumIon;
				}
			}
		}
		/**
		 * Returns the predetermined signal for olfactory receptors
		 * @return
		 */
		public double getSignal(){
			return SIGNAL;            //a constant for olfactory
		}
	}
	/**
	 * Sodium channel handles flux of sodium and checks whether gate is open or closed to allow 
	 * more or less sodium in
	 *
	 */
	private class SodiumChannel {
		
		private String gateStatus = "closed";
		
		/**
		 * Simulates opening of gate to allow more sodium in
		 */
		public void openGate(){
			gateStatus = "open";
		}
		
		/**
		 * Simulates closing of gate to allow more sodium in
		 */
		public void closeGate(){
			gateStatus = "closed";
		}
		
		/**
		 * Returns the status of the gate
		 * @return
		 */
		public String getGateStatus() { 
			return gateStatus;
		}
	}
}