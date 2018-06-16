import java.util.ArrayList;
import java.util.List;


/**
 * The driver for the linear Olfactoryreceptor simulation. 
 * The pizza provides stimuli to the Olfactoryreceptor, which then propagates the signal across a linearly chained network of neurons. 
 * 
 */
public class PizzaSimulationDriverLinear {
	public static final int NUM_DENDRITES = 8;
	public static final int NUM_NEURONS = 1225;
	public static final int NUM_AXON_TERMINALS = 8;
	public static final int olfactoryreceptor_ID = 9999;
	public static final int NUM_SYNAPSES = NUM_NEURONS - 1;
	
	public static void main(String [] args) {
		
		Pizza somePizza = new Pizza();   //possible sources of stimuli
		List<Neuron> neuronList = new ArrayList<>();
		OdorantMolecule oneMolecule = somePizza.getMolecule();
		OlfactoryReceptor olfactoryReceptor = new OlfactoryReceptor(oneMolecule, NUM_DENDRITES); //put in odorant molecules
		
		olfactoryReceptor.process();
		
		//Make one neuron to connect to olfactoryreceptor
		Neuron neuronForOlfactoryReceptor = new Neuron(NUM_DENDRITES, NUM_AXON_TERMINALS);
		
		//Make one synapse for the olfactoryreceptor
		List<Synapse> synapseList = new ArrayList<>();
		Synapse synapseForOlfactoryReceptor = new Synapse(-1, -1);
		synapseList.add(synapseForOlfactoryReceptor);
		
		//Get axon terminals from the olfactoryreceptor and setting to synapse
		synapseForOlfactoryReceptor.setAxonTerminalList(olfactoryReceptor.getAxonTerminalsAsList());
		
		//Set dendrites into the synapse
		synapseForOlfactoryReceptor.setDendriteList(neuronForOlfactoryReceptor.getDendritesAsList());

		//Synapse should now process things and pass a signal into the dendrites
		synapseForOlfactoryReceptor.process();

		//Kill cell
		synapseForOlfactoryReceptor.setActive(false);
				
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
		for(int i = 0; i < NUM_SYNAPSES; i++){
			Synapse aSynapse = synapseList.get(i);
			Neuron preNeuron = neuronList.get(preSynapticNeuronIndex);
			Neuron postNeuron = neuronList.get(postSynapticNeuronIndex);
			aSynapse.setAxonTerminalList(preNeuron.getAxonTerminalsAsList());
			aSynapse.setDendriteList(postNeuron.getDendritesAsList());
			preSynapticNeuronIndex += 1;
			postSynapticNeuronIndex += 1;
		}
					
		//Run through the Hogdkin Huxley Simulation
		int indexForNeurons = 0;
		int indexForSynapses = 0;
		long oldTime = System.currentTimeMillis();
		while(indexForSynapses < NUM_SYNAPSES){	
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
		long displacement = System.currentTimeMillis() - oldTime;
		System.out.println("Total Simulation Time: "+ displacement/1000 + " s");
	}
}
