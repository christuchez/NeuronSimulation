import java.util.ArrayList;
import java.util.List;

/**
 * The driver for the linear photoreceptor simulation. 
 * The lightbulb provides stimuli to the photoreceptor, which then propagates the signal across a linearly chained network of neurons. 
 * 
 */
public class LightBulbSimulationDriverLinear {
	public static final int NUM_DENDRITES = 8;
	public static final int NUM_NEURONS = 1225;
	public static final int NUM_AXON_TERMINALS = 8;
	public static final int PHOTORECEPTOR_ID = 9999;
	public static final int NUM_SYNAPSES = NUM_NEURONS - 1;
	
	public static void main(String [] args){
		
		LightBulb theBulb = new LightBulb();   //light stimuli
		List<Neuron> neuronList = new ArrayList<>();

		int numInitialPhotons = theBulb.getPhotons();
		int numPhotonsActivation = 0;
		PhotoReceptor photoReceptor = new PhotoReceptor(numInitialPhotons, NUM_DENDRITES);
		
		//Accumulate photons until we reach activation threshold
		while(!photoReceptor.wasRhodopsinTriggered()) {
			numPhotonsActivation = numInitialPhotons + theBulb.getPhotons();
			photoReceptor.addMorePhotons(numPhotonsActivation);
		}
		photoReceptor.process();
		
		//Make one neuron to connect to photoreceptor
		Neuron neuronForPhotoReceptor = new Neuron(NUM_DENDRITES, NUM_AXON_TERMINALS);
		
		//Make one synapse for the photoreceptor
		List<Synapse> synapseList = new ArrayList<>();
		Synapse synapseForPhotoReceptor = new Synapse(-1, -1);
		synapseList.add(synapseForPhotoReceptor);
		
		//Get axon terminals from the photoreceptor and setting to synapse
		synapseForPhotoReceptor.setAxonTerminalList(photoReceptor.getAxonTerminalAsList());
		
		//Set dendrites into the synapse
		synapseForPhotoReceptor.setDendriteList(neuronForPhotoReceptor.getDendritesAsList());

		//Synapse should now process things and pass a signal into the dendrites
		synapseForPhotoReceptor.process();

		//Kill cell
		synapseForPhotoReceptor.setActive(false);
				
		//Total 64 neurons
		for(int i = 0; i < NUM_NEURONS; i++){
			neuronList.add(new Neuron(NUM_DENDRITES, NUM_AXON_TERMINALS));
		}
		
		//Total synapses
		for(int i = 0; i < NUM_SYNAPSES; i++){
			synapseList.add(new Synapse());
		}
		
		//Connect all the neurons
		int preSynapticNeuronIndex = 0; //index of first neuron
		int postSynapticNeuronIndex = 1; //index of second neuron
		for(int i = 0; i < NUM_SYNAPSES; i++) {
			Synapse aSynapse = synapseList.get(i);
			Neuron preNeuron = neuronList.get(preSynapticNeuronIndex);
			Neuron postNeuron = neuronList.get(postSynapticNeuronIndex);
			aSynapse.setAxonTerminalList(preNeuron.getAxonTerminalsAsList());
			aSynapse.setDendriteList(postNeuron.getDendritesAsList());
			preSynapticNeuronIndex += 1;
			postSynapticNeuronIndex += 1;
		}
					
		//Run through the Hogdkin Huxley Simulation
		int indexForNeurons = 0, indexForSynapses = 0;
		
		long startSimulation = System.currentTimeMillis();
		while(indexForSynapses < NUM_SYNAPSES) {	
			Neuron preSynapticNeuron = neuronList.get(indexForNeurons);
			Neuron postSynapticNeuron = neuronList.get(indexForNeurons + 1); 
			Synapse theSynapse = synapseList.get(indexForSynapses);
						
			preSynapticNeuron.processHodgkinHuxley();
			theSynapse.process();
			postSynapticNeuron.processHodgkinHuxley();

			indexForNeurons++;
			indexForSynapses++;
			
			System.out.println("Triggered Synapse: " + indexForSynapses);
		}
		long totalDisplacement = System.currentTimeMillis() - startSimulation;
		System.out.println("Total Simulation Time: "+ totalDisplacement/1000 + " s");
	}
}
