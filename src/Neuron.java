import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Handles the interaction between the dendrites and the axon termainals at the end of the axon.
 * Contains logic to run the HH signal propogation across the neuron
 * 
 */
public class Neuron {
	
	private boolean visited;
	private List<Dendrite> dendriteList;
	private List<AxonTerminal> axonTerminalList;
	private int i;
	private int j;
	private double axonLength;
	private int numDistanceFromSource;
	
	/**
	 * Instantiates the dendrites and axon terminals present on the neuron
	 * @param numDendrites
	 * @param numAxonTerminals
	 */
	public Neuron(int numDendrites, int numAxonTerminals) {
		axonLength 		 = (new Random()).nextDouble();
		visited 		 = false;
		dendriteList	 = new ArrayList<>();
		axonTerminalList = new ArrayList<>();

		numDistanceFromSource = 0;

		for(int i = 0; i < numDendrites; i++) {
			dendriteList.add(new Dendrite());
		}
		
		for(int i = 0; i < numAxonTerminals; i++) {
			axonTerminalList.add(new AxonTerminal());
		}
		
	}
	
	public Neuron() { };
	
	/**
	 * Simulates the HH signal propogation throughout the neuron and axon. Accounts for resistance.
	 */
	public void processHodgkinHuxley() {
		Hodgkin_Huxley_Simulation hxs = new Hodgkin_Huxley_Simulation();
		hxs.runSimulation();
	}
	/**
	 * Returns the axon length
	 * @return
	 */
	public double getAxonLength() {
		return axonLength;
		
	}

	/**
	 * Returns the x coordinate
	 * @return
	 */
	public int getX(){
		return i;
	}
	
	/**
	 * Returns the y coordinate
	 * @return
	 */
	public int getY(){
		return j;
	}
	
	/**
	 * Returns the distance from the source
	 * @return
	 */
	public int getDistanceFromSource(){
		return numDistanceFromSource;
	}
	
	/**
	 * Sets the distance from the source
	 * @return
	 */
	public void setDistanceFromSource(int numDistanceFromSource){
		this.numDistanceFromSource = numDistanceFromSource;
	}
	
	/**
	 * Returns the dendrite at a specific index
	 * @param index
	 * @return
	 */
	public Dendrite getDendrite(int index) {
		return dendriteList.get(index);
	}
	/**
	 * Returns the axon terminal at a specific index
	 * @param index
	 * @return
	 */
	public AxonTerminal getAxonTerminal(int index) {
		return axonTerminalList.get(index);
	}
	
	/**
	 * Returns the list of all dendrites present on the neuron
	 * @return
	 */
	public List<Dendrite> getDendritesAsList(){
		return dendriteList;
	}
	
	/**
	 * Returns the list of all axon terminals present on the neuron
	 * @return
	 */
	public List<AxonTerminal> getAxonTerminalsAsList(){
		return axonTerminalList;
	}
	
	/**
	 * Checks if the neuron is visited
	 * @return
	 */
	public boolean isVisited() {
		return visited;
	}
	
	/**
	 * Sets neuron to visited
	 * @return
	 */
	public void setVisited() {
		visited = true;
	}
	
	public void setMeshCoordinates(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public int[] getMeshCoordinates() {
		return new int[]{i,j};
	}
}
