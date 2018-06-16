import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles light stimuli from the environment
 *
 */
public class PhotoReceptor {
	
	private OuterSegment outerSegment;
	private InnerSegment innerSegment;
	private SynapticBody synapticBody;
	
	public PhotoReceptor(int photons, int numAxonTerminals){
		outerSegment = new OuterSegment(photons);
		innerSegment = new InnerSegment();
		synapticBody = new SynapticBody(numAxonTerminals);
	}
	/**
	 * Photoreceptor adds more 
	 * @param morePhotons
	 */
	public void addMorePhotons(int morePhotons){
		outerSegment.addPhotons(morePhotons);
	}
	/**
	 * Checks if the rhodopsin was triggered
	 * @return
	 */
	public boolean wasRhodopsinTriggered() {
		return outerSegment.wasLightDetected();
	}
	
	/**
	 * Processes the signal and sets the signals within the synaptic body
	 */
	public void process() {
		if(outerSegment.wasLightDetected() == true){
			innerSegment.triggerCis11();
			innerSegment.setCis11Back();
			int transducinMade = innerSegment.getTransducin();
			synapticBody.setTransducin(transducinMade);
			synapticBody.setSignals();
		}
	}

	public List<AxonTerminal> getAxonTerminalAsList(){
		return synapticBody.getAxonTerminalAsList();
	}
	
	public AxonTerminal getAxonTerminal(int index) {
		return synapticBody.getAxonTerminal(index);
	}
	
	/**
	 * The OuterSegment is the outer section of the photoreceptor that interacts with the physical environment.
	 * When lights hits the membrane like shelves within the outer segment containing rhodopsin, the rhodopsin determines if the amount of photons
	 * received is enough to trigger a signal.
	 * 
	 */
	private class OuterSegment {
		private Rhodopsin rhodopsin;  //outer segment contains the rhodopsin
		
		/**
		 * OuterSegment contains rhodopsin
		 * @param photons
		 */
		public OuterSegment(int photons){
			rhodopsin = new Rhodopsin(photons);
		}
		
		/**
		 * Adds photons to the rhodopsin molecules
		 * @param somePhotons
		 */
		public void addPhotons(int somePhotons){
			rhodopsin.addPhotonsToRhodopsin(somePhotons);
		}
		
		/**
		 * Checks if a strong enough signal was received
		 * @return
		 */
		public boolean wasLightDetected(){
			return rhodopsin.isRhodopsinTriggered();
		}
	}

	private class InnerSegment {
	
		private String cis11RetinalState = "cis";
		private Transducin transducin;
		
		/**
		 * Creates an instance of transducin - the molecule that is transmitted during signal propogation
		 */
		public InnerSegment(){
			transducin = new Transducin();
		}
		
		/**
		 * Switches the state of 11-cis-retinal protein to 11-trans-retinal 
		 */
		public void triggerCis11(){  //changes the state of the 11 cis retinal if the rhodopsin has registered photons
			cis11RetinalState = "trans";
		}
		
		/**
		 * Sets the state of the 11-trans-retinal to 11-cis-retinal 
		 */
		public void setCis11Back(){
			cis11RetinalState = "cis";
		}
	
		/**
		 * Return the amount of transducin made within the inner segment of the neuron
		 * @return
		 */
		public int getTransducin(){
			int totalTransducinMade = transducin.getTransducin();
			return totalTransducinMade;
		}
	}

	private class SynapticBody {
		
		List<AxonTerminal> axonTerminalList;
		private double SIGNAL = 0; //idk if this is the default
		private int transducin = 0;
		private int numAxonTerminals;
		private boolean signalSet = false;
		
		public SynapticBody(int numAxonTerminals){
			this.numAxonTerminals = numAxonTerminals;
			axonTerminalList = new ArrayList<>();
			for(int i = 0; i < numAxonTerminals; i++) {
				axonTerminalList.add(new AxonTerminal());
			}
		}
		
		public void setTransducin(int transducin){
			this.transducin = transducin;
		}
		
		public void setSignals(){
			SIGNAL = transducin*.5;  //COME BACK HERE IF SIGNAL IS TOO WEAK
			
			for(int i = 0; i < numAxonTerminals; i++) {
				AxonTerminal anAxon = axonTerminalList.get(i);
				anAxon.setSignal(SIGNAL);
			}
			signalSet = true;
		}
		
		public AxonTerminal getAxonTerminal(int index) {
			return axonTerminalList.get(index);
		}
		
		public List<AxonTerminal> getAxonTerminalAsList(){
			return axonTerminalList;
		}
		
		public boolean wasSignalSet(){
			return signalSet == true;
		}

	}

	private class Rhodopsin {
	
		private int photons;
		public Rhodopsin(int photons){
			this.photons = photons;
		}
		
		public boolean isRhodopsinTriggered(){
			boolean toReturn = false;
			if(photons > 140){
				toReturn = true;
			}
			return toReturn;
		}
		
		public void addPhotonsToRhodopsin(int morePhotons){
			photons = photons + morePhotons;
		}
	}

	private class Transducin {
		
		private int transducin = 50;
	
		public int getTransducin(){
			Random random = new Random();
			return (transducin + random.nextInt(100));  //at least 50 
		}
	}
}
