import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class BackPropagationTraining extends Thread {
	static String paramsFileName;
	static int numHiddenLayerOneNeurons;
	static int numHiddenLayerTwoNeurons;
	static int numInputNeurons;
	static int numOutputNeurons;
	static float learningRate;
	static float momentum;
	static String trainFile;
	static String testFile;
	static int totalChecked = 0;
	static int totalCorrect = 0;
	static float trainingInputData[][];
	static float trainingOutputData[][];
	static float testingInputData[][];
	static float testingOutputData[][];
	static boolean usePreviousWeights = false;
	static String weightsFile;
	public static int currentIteration = 0;	
	public static int maxIterations;
	
	public static String toShow ="";
	
	public static float[][] weightsToLayer1;
	public static float[][] weightsToLayer2;
	public static float[][] weightsToOutput;

	public BackPropagationTraining(String params) {
		paramsFileName = params;
		try {
			System.out.println("Trying to read parameters...");
			readParameters();
			System.out.println("Done reading parameters...");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("Error with reading parameters file.");
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		int numberOfTrainingData = 0;
		int numberOfTestingData = 0;
		try {
			numberOfTrainingData = getNumberOfTrainingData();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			numberOfTestingData = getNumberOfTestingData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		trainingInputData = new float[numberOfTrainingData][numInputNeurons];
		trainingOutputData = new float[numberOfTrainingData][numOutputNeurons];
		testingInputData = new float[numberOfTestingData][numInputNeurons];
		testingOutputData = new float[numberOfTestingData][numOutputNeurons];

		try {
			System.out.println("Trying to fill data...");
			fillData(trainingInputData, trainingOutputData, trainFile);
			fillData(testingInputData, testingOutputData, testFile);
			System.out.println("Done filling data...");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Initialising weights...");
		float weightsLayerOne[][] = new float[numInputNeurons + 1][numHiddenLayerOneNeurons];
		float weightsLayerTwo[][] = null;
		float weightsOutLayer[][] = null;
		float lastweightsLayerOne[][] = new float[numInputNeurons + 1][numHiddenLayerOneNeurons];
		float lastweightsLayerTwo[][] = null;
		float lastweightsOutLayer[][] = null;

		if (numHiddenLayerTwoNeurons == 0) {
			weightsOutLayer = new float[numHiddenLayerOneNeurons + 1][numOutputNeurons];
			lastweightsOutLayer = new float[numHiddenLayerOneNeurons + 1][numOutputNeurons];
			if (usePreviousWeights) {
				try {
					fillWeightsFromFile(weightsLayerOne, weightsOutLayer, lastweightsLayerOne, lastweightsOutLayer);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				fillWeights(weightsLayerOne, weightsOutLayer, lastweightsLayerOne, lastweightsOutLayer);

		} else {
			weightsLayerTwo = new float[numHiddenLayerOneNeurons + 1][numHiddenLayerTwoNeurons];
			weightsOutLayer = new float[numHiddenLayerTwoNeurons + 1][numOutputNeurons];
			lastweightsLayerTwo = new float[numHiddenLayerOneNeurons + 1][numHiddenLayerTwoNeurons];
			lastweightsOutLayer = new float[numHiddenLayerTwoNeurons + 1][numOutputNeurons];
			if (usePreviousWeights) {
				try {
					fillWeightsFromFile(weightsLayerOne, weightsLayerTwo, weightsOutLayer, lastweightsLayerOne,
							lastweightsLayerTwo, lastweightsOutLayer);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				fillWeights(weightsLayerOne, weightsLayerTwo, weightsOutLayer, lastweightsLayerOne, lastweightsLayerTwo,
						lastweightsOutLayer);
		}
		System.out.println("Initialised weights...");
		try {
			if (numHiddenLayerTwoNeurons == 0)
				BackPropagationOneLayer(weightsLayerOne, weightsOutLayer, lastweightsLayerOne, lastweightsOutLayer);
			else
				BackPropagationTwoLayer(weightsLayerOne, weightsLayerTwo, weightsOutLayer, lastweightsLayerOne,
						lastweightsLayerTwo, lastweightsOutLayer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void readParameters() throws FileNotFoundException {
		Scanner in = new Scanner(new File(paramsFileName));
		String s = in.nextLine();
		System.out.println(s);
		numHiddenLayerOneNeurons = Integer.parseInt(s.split(" ")[1]);
		s = in.nextLine();
		System.out.println(s);
		numHiddenLayerTwoNeurons = Integer.parseInt(s.split(" ")[1]);
		s = in.nextLine();
		System.out.println(s);
		numInputNeurons = Integer.parseInt(s.split(" ")[1]);
		s = in.nextLine();
		System.out.println(s);
		numOutputNeurons = Integer.parseInt(s.split(" ")[1]);
		s = in.nextLine();
		System.out.println(s);
		learningRate = Float.parseFloat(s.split(" ")[1]);
		s = in.nextLine();
		System.out.println(s);
		momentum = Float.parseFloat(s.split(" ")[1]);
		s = in.nextLine();
		System.out.println(s);
		maxIterations = Integer.parseInt(s.split(" ")[1]);
		s = in.nextLine();
		System.out.println(s);
		trainFile = s.split(" ")[1];
		s = in.nextLine();
		System.out.println(s);
		testFile = s.split(" ")[1];
		s = in.nextLine();
		System.out.println(s);
		usePreviousWeights = Integer.parseInt(s.split(" ")[1]) == 1;
		s = in.nextLine();
		System.out.println(s);
		weightsFile = s.split(" ")[1];
	}

	public static int getNumberOfTrainingData() throws FileNotFoundException {
		Scanner in = new Scanner(new File(trainFile));
		int count = 0;
		while (in.hasNextLine()) {
			in.nextLine();
			count++;
		}
		return count;
	}

	public static int getNumberOfTestingData() throws FileNotFoundException {
		Scanner in = new Scanner(new File(testFile));
		int count = 0;
		while (in.hasNextLine()) {
			in.nextLine();
			count++;
		}
		return count;
	}

	public static void fillData(float[][] inputData, float[][] outputData, String fileName)
			throws FileNotFoundException {
		Scanner in = new Scanner(new File(fileName));
		String line = "";
		for (int i = 0; i < inputData.length; i++) {
			line = in.nextLine();
			String[] values = line.split(" ");
			int count = 0;
			for (int j = 0; j < inputData[i].length; j++) {
				inputData[i][j] = Float.parseFloat(values[count]);
				count++;
			}
			for (int j = 0; j < outputData[i].length; j++) {
				outputData[i][j] = Float.parseFloat(values[count]);
				count++;
			}
		}
	}

	/*
	 * public static float[] fixOutput(String ch) { float abc[] = new
	 * float[numOutputNeurons]; int index = (int) ch.charAt(0) - 65; abc[index] = 1;
	 * return abc; }
	 * 
	 * public static void fixInput(float[][] inputData) { float minMax[] =
	 * findMinMax(inputData); for (int i = 0; i < inputData.length; i++) { for (int
	 * j = 0; j < inputData[i].length; j++) { inputData[i][j] =
	 * normalize(inputData[i][j], minMax); } } }
	 * 
	 * public static float[] findMinMax(float[][] inputData) { float minmax[] = new
	 * float[2]; minmax[0]=inputData[0][0]; minmax[1]=inputData[0][0]; for (int i =
	 * 0; i < inputData.length; i++) { for (int j = 0; j < inputData[i].length; j++)
	 * { if(inputData[i][j] > minmax[1]){ minmax[1] = inputData[i][j]; }else
	 * if(inputData[i][j] < minmax[0]){ minmax[0] = inputData[i][j]; } } } return
	 * minmax; }
	 * 
	 * public static float normalize(float value, float[] minMax) { return ((value -
	 * minMax[0]) / (minMax[1] - minMax[0])) * 2 - 1; }
	 */

	public static void fillWeights(float[][] weightsLayerOne, float[][] weightsOutLayer, float[][] lastweightsLayerOne,
			float[][] lastweightsOutLayer) {
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				weightsLayerOne[i][j] = (float) (Math.random() * 2 - 1);
				lastweightsLayerOne[i][j] = weightsLayerOne[i][j];
				System.out.println(weightsLayerOne[i][j]);
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = (float) (Math.random() * 2 - 1);
				lastweightsOutLayer[i][j] = weightsOutLayer[i][j];
			}
		}
	}

	public static void fillWeights(float[][] weightsLayerOne, float[][] weightsLayerTwo, float[][] weightsOutLayer,
			float[][] lastweightsLayerOne, float[][] lastweightsLayerTwo, float[][] lastweightsOutLayer) {
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				weightsLayerOne[i][j] = (float) (Math.random() * 2 - 1);
				lastweightsLayerOne[i][j] = weightsLayerOne[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {
				weightsLayerTwo[i][j] = (float) (Math.random() * 2 - 1);
				lastweightsLayerTwo[i][j] = weightsLayerTwo[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerTwoNeurons + 1); i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = (float) (Math.random() * 2 - 1);
				lastweightsOutLayer[i][j] = weightsOutLayer[i][j];
			}
		}

	}

	public static void fillWeightsFromFile(float[][] weightsLayerOne, float[][] weightsOutLayer,
			float[][] lastweightsLayerOne, float[][] lastweightsOutLayer) throws FileNotFoundException {
		Scanner in = new Scanner(new File(weightsFile));
		String line[];
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				weightsLayerOne[i][j] = Float.parseFloat(line[j]);
				lastweightsLayerOne[i][j] = weightsLayerOne[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = Float.parseFloat(line[j]);
				lastweightsOutLayer[i][j] = weightsOutLayer[i][j];
			}
		}
	}

	public static void fillWeightsFromFile(float[][] weightsLayerOne, float[][] weightsLayerTwo,
			float[][] weightsOutLayer, float[][] lastweightsLayerOne, float[][] lastweightsLayerTwo,
			float[][] lastweightsOutLayer) throws FileNotFoundException {
		Scanner in = new Scanner(new File(weightsFile));
		String line[];
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				line = in.nextLine().split(" ");
				weightsLayerOne[i][j] = Float.parseFloat(line[j]);
				lastweightsLayerOne[i][j] = weightsLayerOne[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {
				weightsLayerTwo[i][j] = Float.parseFloat(line[j]);
				lastweightsLayerTwo[i][j] = weightsLayerTwo[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerTwoNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = Float.parseFloat(line[j]);
				lastweightsOutLayer[i][j] = weightsOutLayer[i][j];
			}
		}

	}

	public static void BackPropagationOneLayer(float[][] weightsLayerOne, float[][] weightsOutLayer,
			float[][] lastweightsLayerOne, float[][] lastweightsOutLayer) throws IOException {
		float layerOneY[] = new float[numHiddenLayerOneNeurons];
		float output[] = new float[numOutputNeurons];

		float layerOneDelta[] = new float[numHiddenLayerOneNeurons];
		float outputDelta[] = new float[numOutputNeurons];

		BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"));
		writer.write("Epochs\tTrainError\tTestError\n");
//		BufferedWriter writerSuccess = new BufferedWriter(new FileWriter("successrate.txt"));
//		writerSuccess.write("Epochs\tTrainRate\tTestRate\n");

		for (currentIteration = 0; currentIteration < maxIterations; currentIteration++) {
			float trainingError = 0;
			float testingError = 0;
			totalChecked = 0;
			totalCorrect = 0;

			for (int t = 0; t < trainingInputData.length; t++) {

				calculateSigmoid(numHiddenLayerOneNeurons, weightsLayerOne, trainingInputData[t], layerOneY);
				calculateSigmoid(numOutputNeurons, weightsOutLayer, layerOneY, output);
				for (int i = 0; i < output.length; i++) {
					trainingError += Math.abs(Math.pow((trainingOutputData[t][i] - output[i]), 2) / 2);
				}
//				checkResult(trainingOutputData[t], output);

				fixWeights(layerOneY, output, layerOneDelta, outputDelta, t, weightsLayerOne, weightsOutLayer,
						lastweightsLayerOne, lastweightsOutLayer);
			}
//			float trainingRate = (float) (1.0 * totalCorrect / totalChecked);
//			totalChecked = 0;
//			totalCorrect = 0;
			for (int t = 0; t < testingInputData.length; t++) {

				calculateSigmoid(numHiddenLayerOneNeurons, weightsLayerOne, testingInputData[t], layerOneY);
				calculateSigmoid(numOutputNeurons, weightsOutLayer, layerOneY, output);

				for (int i = 0; i < output.length; i++) {
					testingError += Math.abs(Math.pow((testingOutputData[t][i] - output[i]), 2) / 2);
				}
//				checkResult(testingOutputData[t], output);

			}
			float testingRate = (float) (1.0 * totalCorrect / totalChecked);
//			writerSuccess.write((c + 1) + "\t\t" + String.format("%.5g", trainingRate) + "\t\t\t"
//					+ String.format("%.5g", testingRate) + "\n");

			writer.write((currentIteration + 1) + "\t\t" + String.format("%.5g", (trainingError / 4)) + "\t\t"
					+ String.format("%.5g", (testingError / 4)) + "\n");
		}
		writer.close();
//		writerSuccess.close();
		writer = new BufferedWriter(new FileWriter(weightsFile));
		for (int i = 0; i <= numInputNeurons; i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				if (j > 0)
					writer.write(" ");
				writer.write(weightsLayerOne[i][j] + "");
			}
			writer.write("\n");
		}
		for (int i = 0; i <= numHiddenLayerOneNeurons; i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				if (j > 0)
					writer.write(" ");
				writer.write(weightsOutLayer[i][j] + "");
			}
			if (i != numHiddenLayerOneNeurons)
				writer.write("\n");
		}

		writer.close();

	}

	public static void BackPropagationTwoLayer(float[][] weightsLayerOne, float[][] weightsLayerTwo,
			float[][] weightsOutLayer, float[][] lastweightsLayerOne, float[][] lastweightsLayerTwo,
			float[][] lastweightsOutLayer) throws IOException {

		float layerOneY[] = new float[numHiddenLayerOneNeurons];
		float layerTwoY[] = new float[numHiddenLayerTwoNeurons];
		float output[] = new float[numOutputNeurons];
		float layerOneDelta[] = new float[numHiddenLayerOneNeurons];
		float layerTwoDelta[] = new float[numHiddenLayerTwoNeurons];
		float outputDelta[] = new float[numOutputNeurons];

		BufferedWriter writer = new BufferedWriter(new FileWriter("results.txt"));
		writer.write("Epochs\tTrainError\tTestError\n");
		// BufferedWriter writerSuccess = new BufferedWriter(new
		// FileWriter("successrate.txt"));
		// writerSuccess.write("Epochs\tTrainRate\tTestRate\n");

		for (currentIteration = 0; currentIteration < maxIterations; currentIteration++) {
			float trainingError = 0;
			float testingError = 0;
//			 totalChecked = 0;
//			 totalCorrect = 0;
			for (int t = 0; t < trainingInputData.length; t++) {

				calculateSigmoid(numHiddenLayerOneNeurons, weightsLayerOne, trainingInputData[t], layerOneY);
				calculateSigmoid(numHiddenLayerTwoNeurons, weightsLayerTwo, layerOneY, layerTwoY);
				calculateSigmoid(numOutputNeurons, weightsOutLayer, layerTwoY, output);

				for (int i = 0; i < output.length; i++) {
					trainingError += (Math.pow((trainingOutputData[t][i] - output[i]), 2) / 2);
				}
				trainingError /= output.length;
				// checkResult(trainingOutputData[t], output);
				fixWeights(layerOneY, layerTwoY, output, layerOneDelta, layerTwoDelta, outputDelta, t, weightsLayerOne,
						weightsLayerTwo, weightsOutLayer, lastweightsLayerOne, lastweightsLayerTwo,
						lastweightsOutLayer);
			}
			weightsToLayer1 = weightsLayerOne.clone();
			weightsToLayer2 = weightsLayerTwo.clone();
			weightsToOutput = weightsOutLayer.clone();
//			float trainingRate = (float) (1.0 * totalCorrect / totalChecked);
//			totalChecked = 0;
//			totalCorrect = 0;
			for (int t = 0; t < testingInputData.length; t++) {

				calculateSigmoid(numHiddenLayerOneNeurons, weightsLayerOne, testingInputData[t], layerOneY);
				calculateSigmoid(numHiddenLayerTwoNeurons, weightsLayerTwo, layerOneY, layerTwoY);
				calculateSigmoid(numOutputNeurons, weightsOutLayer, layerTwoY, output);
				// checkResult(testingOutputData[t], output);
				for (int i = 0; i < output.length; i++) {
					testingError += Math.abs(Math.pow((testingOutputData[t][i] - output[i]), 2) / 2);
				}
				testingError /= output.length;

			}
//			 float testingRate = (float) (1.0 * totalCorrect / totalChecked);

			writer.write((currentIteration + 1) + "\t\t" + String.format("%.5g", (trainingError / 4)) + "\t\t"
					+ String.format("%.5g", (testingError / 4)) + "\n");
			toShow = "Gen "+(currentIteration + 1) + " of "+ maxIterations+" | TrainingError: "+ String.format("%.5g", (trainingError / 4)) + " | TestingError: "
					+ String.format("%.5g", (testingError / 4));
//			 writerSuccess.write((c + 1) + "\t\t" + String.format("%.5g", trainingRate) +
//			"\t\t\t"
//			 + String.format("%.5g", testingRate) + "\n");
		}
		writer.close();
//		 writerSuccess.close();
		writer = new BufferedWriter(new FileWriter(weightsFile));
		for (int i = 0; i <= numInputNeurons; i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				if (j > 0)
					writer.write(" ");
				writer.write(weightsLayerOne[i][j] + "");
			}
			writer.write("\n");
		}
		for (int i = 0; i <= numHiddenLayerOneNeurons; i++) {
			for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {
				if (j > 0)
					writer.write(" ");
				writer.write(weightsLayerTwo[i][j] + "");
			}
			writer.write("\n");
		}
		for (int i = 0; i <= numHiddenLayerTwoNeurons; i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				if (j > 0)
					writer.write(" ");
				writer.write(weightsOutLayer[i][j] + "");
			}
			if (i != numHiddenLayerTwoNeurons)
				writer.write("\n");
		}
		writer.close();

	}

	public static void calculateSigmoid(int neuronsToCalc, float weights[][], float incomingData[], float layerY[]) {
		for (int i = 0; i < neuronsToCalc; i++) {
			float y = 0;
			// Calculate the y sum of each neuron on Layer One
			for (int j = 0; j < incomingData.length; j++) {
				y += weights[j][i] * incomingData[j];
			}
			// add bias
			y += weights[incomingData.length][i];
			// calculate sigmoid
			layerY[i] = (float) (1.0 / (1 + Math.pow(Math.E, -y)));
		}
	}

	public static void fixWeights(float layerOneY[], float output[], float layerOneDelta[], float outputDelta[], int t,
			float[][] weightsLayerOne, float[][] weightsOutLayer, float[][] lastweightsLayerOne,
			float[][] lastweightsOutLayer) {
		// calculate d of Output Layer
		for (int i = 0; i < outputDelta.length; i++)
			outputDelta[i] = output[i] * (1 - output[i]) * (output[i] - trainingOutputData[t][i]);
		// Calculate d of the Hidden layer 1
		for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
			float sum = 0;
			for (int i = 0; i < numOutputNeurons; i++) {
				sum += weightsOutLayer[j][i] * outputDelta[i];
			}
			layerOneDelta[j] = layerOneY[j] * (1 - layerOneY[j]) * sum;
		}

		float tempLayerOne[][] = new float[numInputNeurons + 1][numHiddenLayerOneNeurons];
		float tempOutput[][] = new float[numHiddenLayerOneNeurons + 1][numOutputNeurons];

		// Change the weights going to Layer 1
		for (int i = 0; i < numHiddenLayerOneNeurons; i++) {
			for (int j = 0; j < numInputNeurons; j++) {
				tempLayerOne[j][i] = weightsLayerOne[j][i];
				weightsLayerOne[j][i] += -learningRate * layerOneDelta[i] * trainingInputData[t][j]
						+ momentum * (weightsLayerOne[j][i] - lastweightsLayerOne[j][i]);
			}
			tempLayerOne[numInputNeurons][i] = weightsLayerOne[numInputNeurons][i];
			weightsLayerOne[numInputNeurons][i] += -learningRate * layerOneDelta[i]
					+ momentum * (weightsLayerOne[numInputNeurons][i] - lastweightsLayerOne[numInputNeurons][i]);

		}

		// Change the weights going to Output Layer
		for (int i = 0; i < numOutputNeurons; i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {

				tempOutput[j][i] = weightsOutLayer[j][i];
				weightsOutLayer[j][i] += -learningRate * outputDelta[i] * layerOneY[j]
						+ momentum * (weightsOutLayer[j][i] - lastweightsOutLayer[j][i]);

			}
			tempOutput[numHiddenLayerOneNeurons][i] = weightsOutLayer[numHiddenLayerOneNeurons][i];
			weightsOutLayer[numHiddenLayerOneNeurons][i] += -learningRate * outputDelta[i] + momentum
					* (weightsOutLayer[numHiddenLayerOneNeurons][i] - lastweightsOutLayer[numHiddenLayerOneNeurons][i]);
		}

		// Update past weights
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				lastweightsLayerOne[i][j] = tempLayerOne[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				lastweightsOutLayer[i][j] = tempOutput[i][j];
			}
		}
	}

	public static void fixWeights(float[] layerOneY, float[] layerTwoY, float[] output, float[] layerOneDelta,
			float[] layerTwoDelta, float[] outputDelta, int t, float[][] weightsLayerOne, float[][] weightsLayerTwo,
			float[][] weightsOutLayer, float[][] lastweightsLayerOne, float[][] lastweightsLayerTwo,
			float[][] lastweightsOutLayer) {
		// calculate d of Output Layer
		for (int i = 0; i < outputDelta.length; i++)
			outputDelta[i] = output[i] * (1 - output[i]) * (output[i] - trainingOutputData[t][i]);
		// Calculate d of the Hidden layer 2
		for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {
			float sum = 0;
			for (int i = 0; i < numOutputNeurons; i++) {
				sum += weightsOutLayer[j][i] * outputDelta[i];
			}
			layerTwoDelta[j] = layerTwoY[j] * (1 - layerTwoY[j]) * sum;
		}
		// Calculate d of the Hidden layer 1
		for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
			float sum = 0;
			for (int i = 0; i < numHiddenLayerTwoNeurons; i++) {
				sum += weightsLayerTwo[j][i] * layerTwoDelta[i];
			}
			layerOneDelta[j] = layerOneY[j] * (1 - layerOneY[j]) * sum;
		}

		float tempLayerOne[][] = new float[numInputNeurons + 1][numHiddenLayerOneNeurons];
		float tempLayerTwo[][] = new float[numHiddenLayerOneNeurons + 1][numHiddenLayerTwoNeurons];
		float tempOutput[][] = new float[numHiddenLayerTwoNeurons + 1][numOutputNeurons];

		// Change the weights going to Layer 1
		for (int i = 0; i < numHiddenLayerOneNeurons; i++) {
			for (int j = 0; j < numInputNeurons; j++) {
				tempLayerOne[j][i] = weightsLayerOne[j][i];
				weightsLayerOne[j][i] += -learningRate * layerOneDelta[i] * trainingInputData[t][j]
						+ momentum * (weightsLayerOne[j][i] - lastweightsLayerOne[j][i]);
			}
			tempLayerOne[numInputNeurons][i] = weightsLayerOne[numInputNeurons][i];
			weightsLayerOne[numInputNeurons][i] += -learningRate * layerOneDelta[i]
					+ momentum * (weightsLayerOne[numInputNeurons][i] - lastweightsLayerOne[numInputNeurons][i]);
		}

		// Change the weights going to Layer 2
		for (int i = 0; i < numHiddenLayerTwoNeurons; i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {

				tempLayerTwo[j][i] = weightsLayerTwo[j][i];
				weightsLayerTwo[j][i] += -learningRate * layerTwoDelta[i] * layerOneY[j]
						+ momentum * (weightsLayerTwo[j][i] - lastweightsLayerTwo[j][i]);
			}
			tempLayerTwo[numHiddenLayerOneNeurons][i] = weightsLayerTwo[numHiddenLayerOneNeurons][i];
			weightsLayerTwo[numHiddenLayerOneNeurons][i] += -learningRate * layerTwoDelta[i] + momentum
					* (weightsLayerTwo[numHiddenLayerOneNeurons][i] - lastweightsLayerTwo[numHiddenLayerOneNeurons][i]);
		}

		// Change the weights going to Output Layer
		for (int i = 0; i < numOutputNeurons; i++) {
			for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {

				tempOutput[j][i] = weightsOutLayer[j][i];
				weightsOutLayer[j][i] += -learningRate * outputDelta[i] * layerTwoY[j]
						+ momentum * (weightsOutLayer[j][i] - lastweightsOutLayer[j][i]);
			}
			tempOutput[numHiddenLayerTwoNeurons][i] = weightsOutLayer[numHiddenLayerTwoNeurons][i];
			weightsOutLayer[numHiddenLayerTwoNeurons][i] += -learningRate * outputDelta[i] + momentum
					* (weightsOutLayer[numHiddenLayerTwoNeurons][i] - lastweightsOutLayer[numHiddenLayerTwoNeurons][i]);
		}

		// Update past weights
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				lastweightsLayerOne[i][j] = tempLayerOne[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {
				lastweightsLayerTwo[i][j] = tempLayerTwo[i][j];
			}
		}
		for (int i = 0; i < (numHiddenLayerTwoNeurons + 1); i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				lastweightsOutLayer[i][j] = tempOutput[i][j];
			}
		}
	}

	/*
	 * public static void checkResult(float[] correct, float[] output) { // TODO
	 * Auto-generated method stub int max = 0; for (int i = 1; i < output.length;
	 * i++) { if (output[i] > output[max]) max = i; } totalChecked++; if
	 * (correct[max] == 1) totalCorrect++;
	 * 
	 * }
	 */
}
