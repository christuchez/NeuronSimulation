import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.xeiam.xchart.BitmapEncoder;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;
/**
 * Creates Hodgkin Huxley Simulation based on 1952 HH paper to determine how ion 
 * conductances and voltage changes affect:
 * 			1) Action potential
 * 			2) Conductances of different ions such as Sodium, 
 * 			   Potassium and Leakage Currents comprised of Chlorine Ions
 * 
 * Creates graphs measuring Voltage Vs Time and Conductances of Na Vs Time 
 * and Conductances of K vs Time
 * 
 *
 */
public class Hodgkin_Huxley_Simulation {

	/* Simulation time and time steps */
	private static int simulationTime     = 100;
	private static double timeStep        = 0.01;
	
	private static int INITIAL_TIME_STEPS = 500;
	private static int MID_TIME_STEPS     = 2000;
	private static int FINAL_TIME_STEPS   = 0;
	private static double currentLevels   = 50;
	
	/* Constants based on Hodgkin Huxley Model from HH '52 Paper*/
	private static double gbar_K  = 36;
	private static double E_K     = -12;

	private static double gbar_Na = 120;
	private static double E_Na    = 115;

	private static double g_L     = 0.3;
	private static double E_L     = 10.6;
	
	private static double C       = 1;
	
	private static List<Double> time;
	private static List<Double> I;
	private static List<Double> V; 
	
	private static List<Double> n;
	private static List<Double> m;
	private static List<Double> h;	
	
	private static List<Double> alpha_n;
	private static List<Double> beta_n;
	
	private static List<Double> alpha_m;
	private static List<Double> beta_m;
	
	private static List<Double> alpha_h;	
	private static List<Double> beta_h;

	private double[] timeData;
	private double[] voltageData;
	private double[] conductance_KData;
	private double[] conductance_NaData;
    
    /**
     * Initializes the base configurations for the model
     */
	public Hodgkin_Huxley_Simulation() {
		
		//Create time array
		time = new ArrayList<Double>();
		I = new ArrayList<Double>();
		n = new ArrayList<Double>();
		m = new ArrayList<Double>();
		h = new ArrayList<Double>();
		
		V = new ArrayList<Double>();
		alpha_n = new ArrayList<Double>();
		beta_n = new ArrayList<Double>();
		alpha_m = new ArrayList<Double>();
		beta_m = new ArrayList<Double>();
		alpha_h = new ArrayList<Double>();
		beta_h = new ArrayList<Double>();
		
		for(double i = 0; i < simulationTime; i += timeStep) {
			time.add(i);
		}
		
		V.add(0, 0.0);
		
		double local_v = V.get(0);
		
		alpha_n.add(0, 0.01*((10-local_v)/(Math.exp((10-local_v)/10) - 1)));
		beta_n.add(0, 0.125*Math.exp(-local_v/80));
		alpha_m.add(0, 0.1*((25-local_v)/(Math.exp((25-local_v)/10) - 1)));
		beta_m.add(0,4*Math.exp(-local_v/18));
		alpha_h.add(0, 0.07*Math.exp(-local_v/20));
		beta_h.add(0, 1/(Math.exp((30-local_v)/10) + 1));

		FINAL_TIME_STEPS = time.size();
		
		for(int i = 0; i < FINAL_TIME_STEPS; i++) {
			if(i >= INITIAL_TIME_STEPS + 1 && i <= MID_TIME_STEPS) {
				I.add(0.0);
			}
			else {
				I.add(currentLevels);
				
				/* Uncomment to show the dampening effect which is caused by change in current
				 * if(currentLevels - i >= 3) { currentLevels -= i; }
				 * I.add(currentLevels);
				 */
			}
		}

		n.add(0, alpha_n.get(0)/(alpha_n.get(0) + beta_n.get(0)));
		m.add(0, alpha_m.get(0)/(alpha_m.get(0) + beta_m.get(0)));
		h.add(0, alpha_h.get(0)/(alpha_h.get(0) + beta_h.get(0)));		
	}
	
	/**
	 * Runs simulation for 100 ms and calcualtes required voltage/conductances
	 */
	public void runSimulation() {
		double I_Na;
		double I_K;
		double I_L;
		double I_ion;
		double local_alpha_n;
		double local_beta_n;
		double local_alpha_m;
		double local_beta_m;
		double local_alpha_h;
		double local_beta_h;
		double local_n;
		double local_m;
		double local_h; 
		double local_v;
		
		for(int i = 0; i < time.size()-1; i++) {
			local_v       = V.get(i);

			alpha_n.add(i, 0.01*((10-local_v)/(Math.exp((10-local_v)/10) - 1)));
			beta_n.add(i, 0.125*Math.exp(-local_v/80));
			alpha_m.add(i, 0.1*((25-local_v)/(Math.exp((25-local_v)/10) - 1)));
			beta_m.add(i,4*Math.exp(-local_v/18));
			alpha_h.add(i, 0.07*Math.exp(-local_v/20));
			beta_h.add(i, 1/(Math.exp((30-local_v)/10) + 1));
			
			local_alpha_n = alpha_n.get(i);
			local_alpha_h = alpha_h.get(i);
			local_alpha_m = alpha_m.get(i);
			local_beta_n  = beta_n.get(i);
			local_beta_m  = beta_m.get(i);
			local_beta_h  = beta_h.get(i);
			local_n       = n.get(i);
			local_m       = m.get(i);
			local_h       = h.get(i);
			
			I_Na = (Math.pow(local_m, 3) * gbar_Na * local_h * (local_v-E_Na));
			I_K  = (Math.pow(local_n, 4) * gbar_K  * (local_v - E_K));
			I_L = g_L *(local_v - E_L);
			I_ion = I.get(i) - I_K - I_Na - I_L;
			
			V.add(i+1, local_v + timeStep*I_ion/C);
			n.add(i+1, local_n + timeStep*(local_alpha_n*(1-local_n) - (local_beta_n * local_n)));
			m.add(i+1, local_m + timeStep*(local_alpha_m*(1-local_m) - (local_beta_m * local_m)));
			h.add(i+1, local_h + timeStep*(local_alpha_h*(1-local_h) - (local_beta_h * local_h)));			
		}
		
		
		/*
		 * Performs V = V - 70. Sets to resting potential
		 */
		/*for(int i = 0; i < V.size(); i++) {
			V.set(i, V.get(i) - 70);
	    	System.out.println("voltageData: " + V.get(i));
		}*/
		
		timeData = convertToArray(time);
	    voltageData = convertToArray(V);
	    conductance_KData = convertToArray(n);
	    conductance_NaData = convertToArray(m);

	    for(int i = 0; i < conductance_KData.length; i++) {
	    	conductance_KData[i] = Math.pow(conductance_KData[i], 4)*gbar_K;
	    }

	    for(int i = 0; i < conductance_NaData.length; i++) {
	    	conductance_NaData[i] = Math.pow(conductance_NaData[i], 3)*gbar_Na*h.get(i);
	    }
	}
	
	public double[] convertToArray(List<Double> list) {
		double[] array = new double[list.size()];
		for(int i = 0; i < list.size(); i++) {
			array[i] = list.get(i);
		}
		return array;
	}
	
	/**
	 * Plots the Time Vs Voltage, Time Vs K Conductance, and Time Vs Na Conductance graphs using XChart library. 
	 * @throws Exception
	 */
	public void plot() throws Exception {	    
		
	    /* Creates Time Vs Voltage Conductance Graph */
		System.out.println("STATE: [Creating Chart 1]");
	    Chart chartTimeVsVoltage = new Chart(1000, 1000);
	    chartTimeVsVoltage.setChartTitle("Voltage over Time in Simulated Neuron");
	    chartTimeVsVoltage.setXAxisTitle("Time (ms)");
	    chartTimeVsVoltage.setYAxisTitle("Voltage (mEv)");
	   
	    Series timeVsVoltageSeries = chartTimeVsVoltage.addSeries("Time Vs Voltage", timeData, voltageData);
	    timeVsVoltageSeries.setMarker(SeriesMarker.CIRCLE);
	    timeVsVoltageSeries.setLineColor(Color.BLUE);
	    
	    System.out.println("STATE: [Saving to Image]");
	    BitmapEncoder.saveJPGWithQuality(chartTimeVsVoltage, "C:\\Users\\ShreyyasV\\Desktop\\Time_Vs_Voltage2.jpg", 0.95f);
	    System.out.println("STATE: [Complete!]");
	    
	    /* Creates Time Vs K Conductance Graph */
	    System.out.println("STATE: [Creating Chart 2]");
	    Chart chartTimeVsKConductance = new Chart(1000, 1000);
	    chartTimeVsKConductance.setChartTitle("Conductance of Potassium Ions in Simulated Neuron");
	    chartTimeVsKConductance.setXAxisTitle("Time (ms)");
	    chartTimeVsKConductance.setYAxisTitle("Conductance (S)");
	   
	    Series timeVsConductanceKSeries = chartTimeVsKConductance.addSeries("Time Vs K Conductance", timeData, conductance_KData);
	    timeVsConductanceKSeries.setMarker(SeriesMarker.SQUARE);
	    timeVsConductanceKSeries.setLineColor(Color.GREEN);
	    
	    System.out.println("STATE: [Saving to Image]");
	    BitmapEncoder.saveJPGWithQuality(chartTimeVsKConductance, "C:\\Users\\Tuchez\\Desktop\\Time_Vs_KConductance1.jpg", 0.95f);
	    System.out.println("STATE: [Complete!]");
	    
	    /* Creates Time Vs Na Conductance Graph */
	    System.out.println("STATE: [Creating Chart 3]");
	    Chart chartTimeVsNaConductance = new Chart(1000, 1000);
	    chartTimeVsNaConductance.setChartTitle("Conductance of Sodium Ions in Simulated Neuron");
	    chartTimeVsNaConductance.setXAxisTitle("Time (ms)");
	    chartTimeVsNaConductance.setYAxisTitle("Conductance (S)");
	   
	    Series timeVsConductanceNaSeries = chartTimeVsNaConductance.addSeries("Time Vs Na Conductance", timeData, conductance_NaData);
	    timeVsConductanceNaSeries.setMarker(SeriesMarker.TRIANGLE_UP);
	    timeVsConductanceNaSeries.setLineColor(Color.RED);
	    
	    System.out.println("STATE: [Saving to Image]");
	    BitmapEncoder.saveJPGWithQuality(chartTimeVsNaConductance, "C:\\Users\\Tuchez\\Desktop\\Time_Vs_NaConductance1.jpg", 0.95f);
	    System.out.println("STATE: [Complete!]");
	    
	    
	    /* Creates Time Vs Na Conductance and Time vs K Conductance Graph */
	    System.out.println("STATE: [Creating Chart 4]");
	    Chart chartTimeVsKNaConductance = new Chart(1000, 1000);
	    chartTimeVsKNaConductance.setChartTitle("Conductance of Sodium Ions in Simulated Neuron");
	    chartTimeVsKNaConductance.setXAxisTitle("Time (ms)");
	    chartTimeVsKNaConductance.setYAxisTitle("Conductance (S)");
	   
	    chartTimeVsKNaConductance.addSeries("Time Vs K Conductance", timeData, conductance_KData);
	    timeVsConductanceKSeries.setMarker(SeriesMarker.SQUARE);
	    timeVsConductanceKSeries.setLineColor(Color.GREEN);
	   
	    chartTimeVsKNaConductance.addSeries("Time Vs Na Conductance", timeData, conductance_NaData);
	    timeVsConductanceNaSeries.setMarker(SeriesMarker.TRIANGLE_UP);
	    timeVsConductanceNaSeries.setLineColor(Color.RED);
	    
	    System.out.println("STATE: [Saving to Image]");
	    BitmapEncoder.saveJPGWithQuality(chartTimeVsKNaConductance, "C:\\Users\\Tuchez\\Desktop\\Time_Vs_KNaConductance1.jpg", 0.95f);
	    System.out.println("STATE: [Complete!]");
	}
	
	/**
	 * Performs main simulation.
	 * @param args
	 */
	public static void main(String[] args) {
		Hodgkin_Huxley_Simulation hxs = new Hodgkin_Huxley_Simulation();
		hxs.runSimulation();
		try {
			hxs.plot();
		} catch (Exception e) {
			System.out.println("Check either File not Found Exception or IO Exception!");
			e.printStackTrace();
		}
	}
}
