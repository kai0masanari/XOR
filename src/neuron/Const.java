package neuron;

public class Const {
	public final String types[] = {"input","hidden","output"};
	
	//training data
	public final double training_sample[][] = {{0,0},{0,1},{1,0},{1,1}};
	public final double training_data[] = {0,1,1,0};
	
	//please edit parameters 
	//leraning_rate and number of hidden neurons, threshold
	public final double learning_rate = 0.3;
	public static final int hidden_neurons = 10;
	public static final double threshold = 0.001;
	
	public Const(){
		
	}
}
