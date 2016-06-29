package neuron;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class XORtest {
	static Const _const = new Const();
	
	static Network net = new Network();
	static inputNeuron in_neuro[] = new inputNeuron[2];
	static hiddenNeuron hid_neuro[] = new hiddenNeuron[10000];
	static outputNeuron out_neuro[] = new outputNeuron[1];
	
	//training sample and data
	private static double input_value[];
	private static double training_value = 0;
	
	// end statement
	private static boolean complete = false;
	
	private static int[] networks = new int[3];
	
	private static double[] outputs = new double[4];
	
	//number of trials
	private static int trials = 0;
	
	static class Network{
		int input_units;
		int hidden_units;
		int output_units;
	}
	
	static class inputNeuron{
		double input;
		double output;
	}
	
	static class hiddenNeuron{
		double[] input = new double[10000];
		double output;
		double[] weight = new double[10000];
	}
	
	static class outputNeuron{
		double[] input = new double[10000];
		double output;
		double[] weight = new double[10000];
	}
	
	static void createNetwork(int input_u, int hidden_u, int output_u){
		net.input_units = input_u;
		net.hidden_units = hidden_u;
		net.output_units = output_u;
		
		int network[] = {input_u, hidden_u, output_u};
		networks[0] = input_u;
		networks[1] = hidden_u;
		networks[2] = output_u;
		
		
		for(int j = 0; j < network.length; j++){
			for(int i = 0; i < network[j]; i++){
				if(j - 1 >= 0){
					addNeuron(_const.types[j], i , networks[j-1]);
				}else{
					addNeuron(_const.types[j], i , 0);
				}
			}
		}
		
	}
	
	static void addNeuron(String type, int ord, int num){
		switch(type){
			case "input":
				inputNeuron n1 = new inputNeuron();
				n1.input = 0;
				n1.output = 0;
				in_neuro[ord] = n1;
				break;
			case "hidden":
				hiddenNeuron n2 = new hiddenNeuron();
				for(int i = 0; i < num; i++){
					n2.input[i] = 0;
				}
				n2.output = 0;
				for(int i = 0; i < num; i++){
					int weight1 = (int)(((Math.random()*2)-1.0)*100);
					n2.weight[i] = ((double)weight1/100);
				}
				hid_neuro[ord] = n2;
				break;
			case "output":
				outputNeuron n3 = new outputNeuron();
				for(int i = 0; i < num; i++){
					n3.input[i] = 0;
				}
				n3.output = 0;
				for(int i = 0; i < num; i++){
					int weight2 = (int)(((Math.random()*2)-1.0)*100);
					n3.weight[i] = ((double)weight2/100);
				}
				out_neuro[ord] = n3;
				break;
		}
	}
	
	//activation function
	static double sigmoid(double u){
		double sigmoid;
		sigmoid = 1 / (1 + Math.exp(-u));
		
		return sigmoid;
	}
	
	//square error
	static double Error(double y){
		double error = 0;
		error = (training_value - y) * (training_value - y);
		return error;
	}

	static void updateInNeuron(inputNeuron n, double input, double output){
		n.input = input;
		n.output = output;
	}
	static void updateHidNeuron(hiddenNeuron n, double[] input, double output, double[] weight){
		n.input = input;
		n.output = output;
		n.weight = weight;
	}
	static void updateOutNeuron(outputNeuron n, double[] input, double output, double[] weight){
		n.input = input;
		n.output = output;
		n.weight = weight;
	}
	
	public static void main(String args[]){
		createNetwork(2, _const.hidden_neurons, 1);
		int count = 0;
		double training_sample[][] = _const.training_sample;
		double training_data[] = _const.training_data;
		double threshold = _const.threshold;
		
		double start_time = System.currentTimeMillis();
		while(!complete){
			input_value = training_sample[count];
			training_value = training_data[count];
			
			//step1 : input_value(training data) to inputNeurons
			for(int i = 0; i < networks[0]; i++){
				updateInNeuron(in_neuro[i], input_value[i], input_value[i]);
			}
			
			//step2 : to hidden Neurons
			for(int i = 0; i < networks[1]; i++){
				double sum = 0.0;
				double[] input = new double[networks[0]];
				for(int j = 0; j < networks[0]; j++){
					sum += in_neuro[j].output*hid_neuro[i].weight[j];
					input[j] = in_neuro[j].output;
				}
				updateHidNeuron(hid_neuro[i], input, sigmoid(sum), hid_neuro[i].weight);
			}
			
			//step3 : to output Neurons
			for(int i = 0; i < networks[2]; i++){
				double sum = 0.0;
				double[] input = new double[networks[1]];
				for(int j = 0; j < networks[1]; j++){
					sum += hid_neuro[j].output*out_neuro[i].weight[j];
					input[j] = hid_neuro[j].output;
				}
				updateOutNeuron(out_neuro[i], input, sigmoid(sum), out_neuro[i].weight);
			}
			
			double y = out_neuro[0].output;
			
			if(Error(y) >= threshold){
				//output Neurons learning
				for(int i = 0; i < networks[1]; i++){
					out_neuro[0].weight[i] += (training_value - y) * y * (1-y) * hid_neuro[i].output;
				}
				
				//hidden Neurons learning
				for(int i = 0; i < networks[1] ;i++){
					for(int j = 0; j < networks[0]; j++){
						double hoge = (training_value - y) * y * (1-y) * hid_neuro[i].output * out_neuro[0].weight[i] * (1-hid_neuro[i].output);
						hid_neuro[i].weight[j] += hoge * in_neuro[j].output;
					}
				}
			}
			
			outputs[count] = out_neuro[0].output;
			
			if(count < 3){
				count++;
			}else{
				int correct = 0;
				count = 0;
				for(int i = 0; i < outputs.length; i++){
					if(Error(y) < threshold){
						correct++;
					}
				}
				if(correct == 4){
					double end_time = System.currentTimeMillis();
					double diff = end_time - start_time;
					double mills = (diff/1000);
					
					for(int i = 0; i < outputs.length; i++){
						//System.out.println(training_sample[i][0]+","+training_sample[i][1]+":"+outputs[i]);
						System.out.println(training_sample[i][0]+","+training_sample[i][1]+":"+String.format("%s",outputs[i]));
					}
					
					System.out.println("number of trials is "+trials+","+"duration is "+mills+"sec");
					complete = true;
					
				}
			}
			trials++;
		}
	}
}
