import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * The driver for the mesh photoreceptor simulation. 
 * The lightbulb provides stimuli to the photoreceptor, 
 * which then propagates the signal across a P x P mesh neural topology (p = number of neurons per dimension).
 * 
 */
public class LightBulbSimulationDriverMesh {
	
	private static int NUM_DENDRITES = 8;
	private static int NUM_NEURONS_PER_ROW = 25;

	public static final int PHOTORECEPTOR_ID = 9999;
	public static double NUM_NEURONS_TOTAL = Math.pow(NUM_NEURONS_PER_ROW,2);
	public static int NUM_AXON_TERMINALS = 8;
	private static Neuron[][] neuronMesh;
	
	public static void main(String [] args){
		
		LightBulb theBulb = new LightBulb();   //possible sources of stimuli
		List<Neuron> queue = new ArrayList<>();
		NeuronMesh mesh = new NeuronMesh(NUM_NEURONS_PER_ROW, NUM_DENDRITES, NUM_AXON_TERMINALS);
		
		//Sets up the neural cluster with a mesh topology
		neuronMesh = mesh.setUpNeuronMesh();
		
		int numInitialPhotons = theBulb.getPhotons();
		int numPhotonsActivation = 0;
		PhotoReceptor photoReceptor = new PhotoReceptor(numInitialPhotons, NUM_DENDRITES);
		
		while(!photoReceptor.wasRhodopsinTriggered()) {
			numPhotonsActivation = numInitialPhotons + theBulb.getPhotons();
			photoReceptor.addMorePhotons(numPhotonsActivation);
		}
		photoReceptor.process();

		//Make one synapse for the photoreceptor
		Synapse synapseForPhotoReceptor = new Synapse(-1, -1);
		
		//Get axon terminals from the photoreceptor and setting to synapse
		synapseForPhotoReceptor.setAxonTerminalList(photoReceptor.getAxonTerminalAsList());
		
		//Set dendrites into the synapse
		synapseForPhotoReceptor.setDendriteList(neuronMesh[0][0].getDendritesAsList());

		//Synapse should now process things and pass a signal into the dendrites
		synapseForPhotoReceptor.process();

		//Kill cell
		synapseForPhotoReceptor.setActive(false);
		
		List<Synapse> synapseList = new ArrayList<>();
		List<Neuron> neighborsList = new ArrayList<>();
		Neuron neighbor = new Neuron();
		Neuron currentNeuron = mesh.getNeuron(0, 0);
		Synapse synapse = new Synapse();
		
		queue.add(currentNeuron);
		currentNeuron.processHodgkinHuxley();
		synapseList = mesh.getSynapses(currentNeuron);

		long oldTime = System.currentTimeMillis();
		while(!queue.isEmpty())	{
			currentNeuron = queue.remove(0);
			System.out.println("Current Neuron: " + currentNeuron.getX() + ", " +currentNeuron.getY());

			synapseList = mesh.getSynapses(currentNeuron);
			neighborsList = mesh.getNeighbors(currentNeuron);
			
			//System.out.println("Current Neuron Is Set True? : " + currentNeuron.isVisited());
			if(!currentNeuron.isVisited()) {
				//System.out.println("YES and I reached here");

				assert(neighborsList.size() == synapseList.size());
				for(int k = 0; k < neighborsList.size(); k++) {
					
					neighbor = neighborsList.get(k);
					synapse = synapseList.get(k); 
					
					if(!neighbor.isVisited()) {
						//System.out.println("NEIGHBOR IS NOT VISITED AND PROCESSING NOW!");

						synapse.process();
						currentNeuron.processHodgkinHuxley();

						queue.add(neighbor);
					}
				}
				currentNeuron.setVisited();
			}
		}
		long displacement = System.currentTimeMillis() - oldTime;
		System.out.println("Total Simulation Time: "+ displacement/1000 + " s");
	}	
}
