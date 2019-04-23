package simple_ga;

import java.util.Arrays;

public class Choromosome {
	private int fitness = 0;
	private int[] genes;
	private int age=0;

	public Choromosome(int length) {
		genes = new int[length];
	}

	public void randomInitialChoromosome() {
		for (int i = 0; i < genes.length; i++) {
			double random = Math.random();
			if (random >= 0.7)
				genes[i] = 1;
			else
				genes[i] = 0;

		}
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

	public int[] getGenes() {
		return genes;
	}

	@Override
	public String toString() {
		return Arrays.toString(genes);
	}

	public int getFitness() {
		fitness = calculateFitness();
		return fitness;
	}

	public int calculateFitness() {
		int currentFitness = 0;
		for (int i = 0; i < genes.length; i++) {
			if (genes[i] == GeneticAlgorithm.FINAL_TARGET[i])
				currentFitness++;
		}

		return currentFitness;
	}

}
