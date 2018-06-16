import java.util.ArrayList;
import java.util.List;

/**
 * The driver for the mesh olfactory receptor simulation. 
 * The pizza provides stimuli to the olfactory receptor, 
 * which then propagates the signal across a P x P mesh neural topology (p = number of neurons per dimension).
 * 
 */
public class PizzaSimulationDriverMesh {
	
	private static int NUM_DENDRITES = 8;
	private static int NUM_NEURONS_PER_ROW = 25;
	public static final int PHOTORECEPTOR_ID = 9999;
	public static double NUM_NEURONS_TOTAL = Math.pow(NUM_NEURONS_PER_ROW,2);
	public static int NUM_AXON_TERMINALS = 8;
	private static Neuron[][] neuronMesh;
	
	public static void main(String [] args) {
		
		Pizza somePizza = new Pizza();   //pizza stimuli
		OdorantMolecule oneMolecule = somePizza.getMolecule();
		OlfactoryReceptor olfactoryReceptor = new OlfactoryReceptor(oneMolecule, NUM_DENDRITES); //put in odorant molecules
		
		NeuronMesh mesh = new NeuronMesh(NUM_NEURONS_PER_ROW, NUM_DENDRITES, NUM_AXON_TERMINALS);
		List<Neuron> queue = new ArrayList<>();

		//Sets up the neural cluster with a mesh topology
		neuronMesh = mesh.setUpNeuronMesh();

		//Sets up the neural cluster with a mesh topology
		neuronMesh = mesh.setUpNeuronMesh();

		//Process the smell
		olfactoryReceptor.process();

		//Make one synapse for the olfactoryreceptor
		Synapse synapseForOlfactoryReceptor = new Synapse(-1, -1);
		
		//Get axon terminals from the olfactoryreceptor and setting to synapse
		synapseForOlfactoryReceptor.setAxonTerminalList(olfactoryReceptor.getAxonTerminalsAsList());
		
		//Set dendrites into the synapse
		synapseForOlfactoryReceptor.setDendriteList(neuronMesh[0][0].getDendritesAsList());

		//Synapse should now process things and pass a signal into the dendrites
		synapseForOlfactoryReceptor.process();

		//Kill cell
		synapseForOlfactoryReceptor.setActive(false);
		
		//Set up to perform BFS traversal through the mesh
		List<Synapse> synapseList = new ArrayList<>();
		List<Neuron> neighborsList = new ArrayList<>();
		Neuron neighbor = new Neuron();
		Neuron currentNeuron = mesh.getNeuron(0, 0);
		Synapse synapse = new Synapse();
		
		queue.add(currentNeuron);
		currentNeuron.processHodgkinHuxley();
		synapseList = mesh.getSynapses(currentNeuron);

		long oldTime = System.currentTimeMillis();
		
		//Run BFS traversal through the mesh 
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
