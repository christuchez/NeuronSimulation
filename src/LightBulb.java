import java.util.Random;

/**
 * Create a light bulb with a random number of photons
 *
 */
public class LightBulb {
	
	private Random rand;
	private int ceiling = 50;
	
	/**
	 * Instantiates a new Light Bulb
	 */
	public LightBulb(){
		rand = new Random();
	}
	
	/**
	 * Returns a random amount of photons from a ceiling
	 * @return
	 */
	public int getPhotons(){
		return rand.nextInt(ceiling);
	}
	
	
}
