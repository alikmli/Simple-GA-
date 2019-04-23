package simple_ga;

import java.security.SecureRandom;
import java.util.List;


public class GeneticAlgorithm {
	public final static int POPULATION_SIZE = 8;
	public static int[] FINAL_TARGET;
	public final static double MUTATION_RATE = 0.25;
	public final static int NUMB_OF_ELLITE_CHOROMOSOME = 3;
	private final static int TUORNOMENT_SELECTION_SIZE = 4;
	private final double uniformRate = 0.4;
	private final int AGELIMIT = 8;
	private final int BITFLIPRATE = 3;

	public Population tuornomenetSelectionPopulation(Population population) {
		Population tPopulation = new Population(TUORNOMENT_SELECTION_SIZE);
		for (int i = 0; i < TUORNOMENT_SELECTION_SIZE; i++) {
			tPopulation.getChoromosomes()[i] = population
					.getChoromosomes()[(int) (Math.random() * population.getChoromosomes().length)];
		}
		tPopulation.sortChoromosomeByFitness();
		return tPopulation;
	}

	public Population tournoment_Ellite_Combine_crossOver(Population population) {
		Population crossOverPopulation = new Population(population.getChoromosomes().length);
		for (int i = 0; i < NUMB_OF_ELLITE_CHOROMOSOME; i++) {
			crossOverPopulation.getChoromosomes()[i] = population.getChoromosomes()[i];
		}

		for (int i = NUMB_OF_ELLITE_CHOROMOSOME; i < population.getChoromosomes().length; i++) {
			Choromosome choromosome1 = tuornomenetSelectionPopulation(population).getChoromosomes()[0];
			Choromosome choromosome2 = tuornomenetSelectionPopulation(population).getChoromosomes()[0];

			crossOverPopulation.getChoromosomes()[i] = singleCrossOver(choromosome1, choromosome2);
//			crossOverPopulation.getChoromosomes()[i]=uniformCrossOver(choromosome1, choromosome2);
		}

		return crossOverPopulation;
	}

	public static void setFinalTarget(List<NumberSchema> list) {
		FINAL_TARGET = new int[list.size()];
		int index = 0;
		for (NumberSchema item : list) {
			if (item.getSign() == Signs.MINUS) {
				FINAL_TARGET[index++] = 0;
			} else {
				FINAL_TARGET[index++] = Integer.valueOf(item.getNumbs()) > 0 ? 1 : 0;
			}
		}
	}

	public static int[] getFINAL_TARGET() {
		return FINAL_TARGET;
	}

	public int rouletteSelection(Population population) {
		int totalSum = 0;
		for (int i = population.getChoromosomes().length - 1; i >= 0; i--) {
			totalSum += population.getChoromosomes()[i].getFitness();
		}

		for (int i = 0; i < population.getChoromosomes().length; i++) {
			int rand = new SecureRandom().nextInt(totalSum);
			int partialSum = 0;
			for (int k = population.getChoromosomes().length - 1; k >= 0; k--) {
				partialSum += population.getChoromosomes()[k].getFitness();
				if (partialSum >= rand) {
					return k;
				}
			}
		}

		return -1;
	}

	public Population rouletteSelection_crossOver(Population population) {
		Population crossOverPopulation = new Population(population.getChoromosomes().length);
		for (int i = 0; i < population.getChoromosomes().length; i++) {
			Choromosome choromosome1 = population.getChoromosomes()[rouletteSelection(population)];
			Choromosome choromosome2 = population.getChoromosomes()[rouletteSelection(population)];
			crossOverPopulation.getChoromosomes()[i] = singleCrossOver(choromosome1, choromosome2);
//			crossOverPopulation.getChoromosomes()[i] = uniformCrossOver(choromosome1, choromosome2);
		}
		return crossOverPopulation;
	}

	public Population rEvolve(Population population) {
		Population result = bitFlipMutation(rouletteSelection_crossOver(population));
		for (Choromosome item : result.getChoromosomes()) {
			item.setAge(item.getAge() + 1);
		}

		return result;
	}

	public Population tEvolve(Population population) {
		Population result = bitFlipMutation(tournoment_Ellite_Combine_crossOver(population));

		for (Choromosome item : result.getChoromosomes()) {
			item.setAge(item.getAge() + 1);
		}

		return result;
	}

	public int ageSurvivorSelection(Population population) {
		for (int i = 1; i < population.getChoromosomes().length; i++) {
			int age = population.getChoromosomes()[i].getAge();
			if (age > AGELIMIT) {
				return i;
			}
		}
		return -1;
	}

	public int fitnessSurvivorSelection(Population population) {
		population.sortChoromosomeByFitness();
		return population.getChoromosomes().length - 1;
	}

	public Population swapMutation(Population population) {

		int size = population.getChoromosomes().length;
		for (int i = 0; i < size; i++) {
			Choromosome choromosome = population.getChoromosomes()[i];
			int position1 = new SecureRandom().nextInt(choromosome.getGenes().length - 1);
			int position2 = new SecureRandom().nextInt(choromosome.getGenes().length - 1);

			int val1 = choromosome.getGenes()[position1];
			int val2 = choromosome.getGenes()[position2];

			choromosome.getGenes()[position1] = val2;
			choromosome.getGenes()[position2] = val1;

			population.getChoromosomes()[fitnessSurvivorSelection(population)] = choromosome;

		}

		return population;
	}

	public Population bitFlipMutation(Population population) {
		SecureRandom random = new SecureRandom();
		int count_genes_flip = random.nextInt(population.getChoromosomes()[0].getGenes().length);
		for (int i = 0; i < BITFLIPRATE; i++) {
			Choromosome choromosome = population.getChoromosomes()[random.nextInt(population.getChoromosomes().length)];

			for (int k = 0; k < count_genes_flip; k++) {
				int position = random.nextInt(population.getChoromosomes()[0].getGenes().length);
				int value = choromosome.getGenes()[position];
				choromosome.getGenes()[position] = value == 1 ? 0 : 1;
			}
			population.getChoromosomes()[fitnessSurvivorSelection(population)] = choromosome;

		}

		return population;
	}

	public Choromosome uniformCrossOver(Choromosome choromosome1, Choromosome choromosome2) {
		Choromosome crossChoromosome = new Choromosome(FINAL_TARGET.length);
		for (int i = 0; i < choromosome1.getGenes().length; i++) {
			if (Math.random() < uniformRate)
				crossChoromosome.getGenes()[i] = choromosome1.getGenes()[i];
			else
				crossChoromosome.getGenes()[i] = choromosome2.getGenes()[i];
		}
		crossChoromosome.setAge(crossChoromosome.getAge() + 1);
		return crossChoromosome;
	}

	public Choromosome singleCrossOver(Choromosome choromosome1, Choromosome choromosome2) {
		Choromosome crossChoromosome = new Choromosome(FINAL_TARGET.length);
		int rand = (new SecureRandom()).nextInt(crossChoromosome.getGenes().length);

		for (int i = 0; i < rand; i++) {
			crossChoromosome.getGenes()[i] = choromosome1.getGenes()[i];
		}

		for (int i = rand; i < crossChoromosome.getGenes().length; i++) {
			crossChoromosome.getGenes()[i] = choromosome2.getGenes()[i];
		}

		return crossChoromosome;
	}

	public Choromosome mutateChoromosome(Choromosome choromosome) {
		Choromosome mChoromosome = new Choromosome(FINAL_TARGET.length);
		for (int i = 0; i < choromosome.getGenes().length; i++) {
			if (Math.random() < MUTATION_RATE) {
				if (Math.random() < 0.5)
					mChoromosome.getGenes()[i] = 1;
				else
					mChoromosome.getGenes()[i] = 0;
			} else {
				mChoromosome.getGenes()[i] = choromosome.getGenes()[i];
			}
		}
		return mChoromosome;
	}
}
