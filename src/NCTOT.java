/**
 * NCTOT - Neural Cluster Traversal Optimization Technique
 * Calculates number of neurons required to trigger within a mesh topology to reach any destination neuron
 *  
 *
 */
public class NCTOT {

	private static NeuronMesh mesh;
	private static int NUM_DENDRITES = 8;
	private static int NUM_NEURONS_PER_ROW = 30;
	public static int NUM_AXON_TERMINALS = 8;
	/**
	 * Set up the neuron mesh
	 */
	public NCTOT() {
		mesh = new NeuronMesh(NUM_NEURONS_PER_ROW, NUM_DENDRITES, NUM_AXON_TERMINALS);
		mesh.setUpNeuronMesh();
	}
	
	/**
	 * Calculates the number of neurons triggered within shortest path from a source neuron
	 * @param aMesh
	 * @param neuron1
	 * @param neuron2
	 * @return
	 */
	private int calculateNeuronsTriggered(NeuronMesh aMesh, Neuron neuron1, Neuron neuron2){
		
		int n1x = neuron1.getX();
		int n1y = neuron1.getY();
		int n2x = neuron2.getX();
		int n2y = neuron2.getY();
		
		double diagonalDistance = Math.sqrt(Math.pow((n1x - n2x),2) + Math.pow((n1y - n2y),2));
		
		int numNeurons = 0;
		boolean haveIFoundIt = false;
		int iValue = 0;
		int jValue = 0;
		for(int i = n1x; i < NUM_NEURONS_PER_ROW && !haveIFoundIt; ){
			for(int j = n1y; j < NUM_NEURONS_PER_ROW && !haveIFoundIt; ){
				if(i < n2x && j < n2y){
					i++;
					j++;
					numNeurons++;
				}
				else if(i == n2x && j != n2y){
					j++;
					numNeurons++;
				}
				else if(j == n2y && i != n2x){
					i++;
					numNeurons++;
				}
				else {
					haveIFoundIt = true;
					iValue = i;
					jValue = j;
				}
			}
		}
		
		int endingXPosition = iValue;
		int endingYPosition = jValue;
		
		double newEuclidean = Math.sqrt(Math.pow((n1x - endingXPosition),2) + Math.pow((n1y - endingYPosition),2));
		/*System.out.println("New Euclidean: " + newEuclidean);
		System.out.println("Old Euclidean: " + diagonalDistance);
		if(newEuclidean == diagonalDistance){
			System.out.println("Euclidean Distance minimizes the number of neurons triggered!!!");
		}*/
		
		return numNeurons;
	}
	
	/**
	 * Runs the NCTOT
	 * @param args
	 */
	public static void main(String[] args) {
		int iCoord = 0;
		int jCoord = 0;

		NCTOT nctot = new NCTOT();
		
		Neuron source = new Neuron(NUM_DENDRITES, NUM_AXON_TERMINALS);
		source.setMeshCoordinates(iCoord, jCoord);
		
		Neuron sink = new Neuron(NUM_DENDRITES, NUM_AXON_TERMINALS);
		int neuronsTriggered = 0;
		
		for(int i = 0; i < NUM_NEURONS_PER_ROW; i++){
			for(int j = 0; j < NUM_NEURONS_PER_ROW; j++){
				sink.setMeshCoordinates(i, j);
				neuronsTriggered = nctot.calculateNeuronsTriggered(mesh, source, sink);
				mesh.getNeuron(i, j).setDistanceFromSource(neuronsTriggered);
				System.out.printf("%d ", neuronsTriggered);
			}
			System.out.println();
		}
	}
	
	
}
