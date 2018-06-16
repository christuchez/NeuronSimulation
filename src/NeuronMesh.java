import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Creates a neural cluster with mesh topology
 *
 */
public class NeuronMesh {
	
	private int numNeuronsPerRow;
	private int numDendrites;
	private int numAxonTerminals;
	private HashMap<Neuron, List<Neuron>> neighborMap;
	private HashMap<Neuron, List<Synapse>> synapseMap;

	private List<Neuron> neighborList;
	private Neuron[][] neuronMesh;
	private List<Synapse> synapseList;
	
	public NeuronMesh(int numNeuronsPerRow, int numDendrites, int numAxonTerminals) {
		this.numNeuronsPerRow = numNeuronsPerRow;
		this.numDendrites     = numDendrites;
		this.numAxonTerminals = numAxonTerminals;
		neuronMesh = new Neuron[numNeuronsPerRow][numNeuronsPerRow];
		neighborMap = new HashMap<>();
		synapseMap = new HashMap<>();

	}
	
	/**
	 * Sets up the neural cluster with mesh topology
	 * @return
	 */
	public Neuron[][] setUpNeuronMesh() {
		for(int i = 0; i < neuronMesh.length; i++) {
			for(int j = 0; j < neuronMesh.length; j++) {
				if(i < neuronMesh.length && j < neuronMesh.length) {
					numDendrites = 4;
					numAxonTerminals = 8;
				}
				else if((i == neuronMesh.length && (j == 0 || j == neuronMesh.length)) || (i == 0 && j == neuronMesh.length)) {
					numDendrites = 4;
					numAxonTerminals = 4;
				}
				else if(i > 0 && i < neuronMesh.length && j > 0 && j < neuronMesh.length) {
					numDendrites = 8;
					numAxonTerminals = 8;
				}
				else {
					numDendrites = 8;
					numAxonTerminals = 4;
				}
				neuronMesh[i][j] = new Neuron(numDendrites, numAxonTerminals);
				neuronMesh[i][j].setMeshCoordinates(i, j);
			}
		}
		
		Synapse currentSynapse = null;
		for(int i = 0; i < neuronMesh.length; i++) {
			for(int j = 0; j < neuronMesh.length; j++) {
				neighborList = new ArrayList<>();
				synapseList = new ArrayList<>();

				if(j < neuronMesh.length - 1) {
					currentSynapse = new Synapse(i,j);
					currentSynapse.setAxonTerminalList(neuronMesh[i][j].getAxonTerminalsAsList());
					currentSynapse.setDendriteList(neuronMesh[i][j+1].getDendritesAsList());
					synapseList.add(currentSynapse);
					neighborList.add(neuronMesh[i][j+1]);
					System.out.printf("Created Synpase 1 at (%d, %d) -> (%d, %d)\n", currentSynapse.getSynapseID()[0], currentSynapse.getSynapseID()[1], i, j + 1);
					
				}
				if(i < neuronMesh.length - 1) {
					currentSynapse = new Synapse(i,j);
					currentSynapse.setAxonTerminalList(neuronMesh[i][j].getAxonTerminalsAsList());
					currentSynapse.setDendriteList(neuronMesh[i+1][j].getDendritesAsList());
					synapseList.add(currentSynapse);
					neighborList.add(neuronMesh[i+1][j]);
					System.out.printf("Created Synpase 2 at (%d, %d) -> (%d, %d)\n", currentSynapse.getSynapseID()[0], currentSynapse.getSynapseID()[1], i +1, j);
				}
				neighborMap.put(neuronMesh[i][j], neighborList);
				synapseMap.put(neuronMesh[i][j], synapseList);
			}
		}
		return neuronMesh;
	}
	
	/**
	 * Returns a neuron at an i, j coordinate within the mesh
	 * @param i
	 * @param j
	 * @return
	 */
	public Neuron getNeuron(int i, int j) {
		return neuronMesh[i][j];
	}
	
	/**
	 * Gets the list of synapses associated with that neuron
	 * @param neuron
	 * @return
	 */
	public List<Synapse> getSynapses(Neuron neuron) {
		return synapseMap.get(neuron);
	}
	
	/**
	 * Gets the list of neighbors associated with that neuron.
	 * @param neuron
	 * @return
	 */
	public List<Neuron> getNeighbors(Neuron neuron) {
		return neighborMap.get(neuron);
	}
}
