package neuron;

public class Genetic {
	int g_size = 32;
	Gene genes[] = new Gene[10];
	Gene g_exc[] = new Gene[2];
	
	void initGene(){
		for(int i=0; i<genes.length;i++){
			Gene g;
			g = new Gene();
			g.generation = 0;
			g.score = 0;
			for(int j=0; j<g_size; j++){
				g.genetic[j] = (int)(Math.random()*2);
			}
			genes[i] = g;
		}
		for(int i=0; i<g_exc.length;i++){
			Gene g;
			g = new Gene();
			g.generation = 0;
			g.score = 0;
			for(int j=0; j<g_size; j++){
				g.genetic[j] = (int)(Math.random()*2);
			}
			g_exc[i] = g;
		}
	}
	
	
	void crossing(){
		
	}
	
	void mutation(){
	}
	
	class Gene{
		int generation;
		int genetic[] = new int[g_size];
		int score;
	}
	
	
	public Genetic(){
	}
}
