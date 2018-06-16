import java.util.Random;


public class OdorantMolecule {

	private Random randGenerator;
	
	public OdorantMolecule(){
		randGenerator = new Random();
	}
	
	public int getStrength(){
		int strength = randGenerator.nextInt(10);
		return strength;
	}
}