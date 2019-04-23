package simple_ga;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;



/**
 * 
 * @author ali Kamali
 * 
 * Entry Point
 *
 */
public class Driver {

	public static void main(String[] args) {
		try (Scanner input = new Scanner(System.in)) {
			System.out.println("Enter The Length of Number : ");
			int Numb_size = input.nextInt();
			System.out.println("Enter The Objective Function : ");
			String formula = input.next();
			List<NumberSchema> vars = sepertingObjFunction(formula, Numb_size);

			System.out.println(vars);
			GeneticAlgorithm gAlgorithm = new GeneticAlgorithm();
			GeneticAlgorithm.setFinalTarget(vars);
			Population population = new Population(GeneticAlgorithm.POPULATION_SIZE);
			population.initializationPoplation(Numb_size);
			System.out.println(
					"Choromosome #0 Fittest Choromosome Fitness : " + population.getChoromosomes()[0].getFitness());
			printPopulation(population, "Target Choromosome : " + Arrays.toString(GeneticAlgorithm.FINAL_TARGET));

			int generationNumber = 0;
			while (population.getChoromosomes()[0].getFitness() < GeneticAlgorithm.FINAL_TARGET.length) {
				generationNumber++;
				System.out.println("\n----------------------------------------------------------");
				population = gAlgorithm.rEvolve(population);
				population.sortChoromosomeByFitness();
				System.out.println("Generation # " + generationNumber + " | Fittest Choromosome Fitness : "
						+ population.getChoromosomes()[0].getFitness());
				printPopulation(population, "Target Choromosome : " + Arrays.toString(GeneticAlgorithm.FINAL_TARGET));
			}
		}
	}

	public static void printPopulation(Population population, String heading) {
		System.out.println(heading);
		System.out.println("--------------------------------------------------");
		int count = 0;
		for (int i = 0; i < population.getChoromosomes().length; i++) {
			System.out.printf("Choromosome #%d : %s  |Age %d |Fitness :%d%n", (count++),
					Arrays.toString(population.getChoromosomes()[i].getGenes()),
					population.getChoromosomes()[i].getAge(), population.getChoromosomes()[i].getFitness());
		}
	}

	public static List<NumberSchema> sepertingObjFunction(String input, int numb_size) {
		List<NumberSchema> args = new LinkedList<NumberSchema>();

		if (input.toLowerCase().contains("f(x)")) {
			input = input.split("=")[1];
			int count = CountX(input) + 1;
			NumberSchema[] vars = new NumberSchema[count];
			for (int i = 0; i < count; i++) {
				if (input.toLowerCase().contains("x" + String.valueOf(i))) {
					String var = "x" + String.valueOf(i);
					int index = input.indexOf(var);
					if (index == 0) {
						vars[Integer.valueOf(String.valueOf(i))] = new NumberSchema(Signs.PLUS, "1");
					} else {
						String number = "";
						int k = index - 1;
						for (; k >= 0; k--) {
							if (Character.isDigit(input.charAt(k))) {
								number = input.charAt(k) + number;
							} else {
								break;
							}
						}
						if (k >= 0) {
							if (input.charAt(k) == '-') {
								vars[Integer.valueOf(String.valueOf(i))] = number.length() == 0
										? new NumberSchema(Signs.MINUS, "1")
										: new NumberSchema(Signs.MINUS, number);
							} else if (input.charAt(k) == '+') {
								vars[Integer.valueOf(String.valueOf(i))] = number.length() == 0
										? new NumberSchema(Signs.PLUS, "1")
										: new NumberSchema(Signs.PLUS, number);
							}
						} else if (k < 0) {
							vars[Integer.valueOf(String.valueOf(i))] = number.length() == 0
									? new NumberSchema(Signs.PLUS, "1")
									: new NumberSchema(Signs.PLUS, number);
						}

					}
				} else {
					vars[Integer.valueOf(String.valueOf(i))] = new NumberSchema(Signs.PLUS, "0");
				}
			}
			Collections.addAll(args, vars);

			int remain = numb_size - args.size();
			for (int i = 0; i < remain; i++) {
				args.add(new NumberSchema(Signs.PLUS, "0"));
			}

		}

		Collections.reverse(args);

		return args;
	}

	public static Integer CountX(String input) {
		List<Character> args = new LinkedList<>();
		for (int i = 0; i < input.length(); i++) {
			if (input.charAt(i) == 'x')
				args.add(input.charAt(i + 1));
		}

		args.sort(new Comparator<Character>() {

			@Override
			public int compare(Character o1, Character o2) {
				int numb1 = Integer.valueOf(o1.toString());
				int numb2 = Integer.valueOf(o2.toString());
				return numb1 > numb2 ? 1 : numb1 < numb2 ? -1 : 0;
			}
		});

		return Integer.decode(args.get(args.size() - 1).toString());
	}

}
