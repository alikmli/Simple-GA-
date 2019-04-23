package simple_ga;

import java.util.Arrays;
import java.util.Comparator;

public class Population {
	private Choromosome[] choromosomes;

	public Population(int length) {
		choromosomes = new Choromosome[length];
	}

	public void sortChoromosomeByFitness() {
		Arrays.sort(choromosomes, new Comparator<Choromosome>() {

			@Override
			public int compare(Choromosome o1, Choromosome o2) {
				return o1.getFitness() > o2.getFitness() ? -1 : o1.getFitness() == o2.getFitness() ? 0 : 1;
			}
		});
	}
	
	public void sortChoromosomeByAge() {
		Arrays.sort(choromosomes, new Comparator<Choromosome>() {

			@Override
			public int compare(Choromosome o1, Choromosome o2) {
				return o1.getAge() > o2.getAge() ? -1 : o1.getAge() == o2.getAge() ? 0 : 1;
			}
		});
	}

	public void initializationPoplation(int n) {
		for (int i = 0; i < choromosomes.length; i++) {
			choromosomes[i] = new Choromosome(n);
			choromosomes[i].randomInitialChoromosome();
		}
		sortChoromosomeByFitness();
	}

	public Choromosome[] getChoromosomes() {
		return choromosomes;
	}

}
