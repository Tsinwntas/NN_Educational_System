import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class BackPropagationExecution extends Thread {
	static String paramsFileName;
	static int numHiddenLayerOneNeurons;
	static int numHiddenLayerTwoNeurons;
	static int numInputNeurons;
	static int numOutputNeurons;
	static String inputFile;
	static float inputData[][];
	static float outputData[][];
	static boolean usePreviousWeights = false;
	static String weightsFile;

	public static int currentLine = 0;
	public static int numberOfInputData = 0;

	public BackPropagationExecution(String params) {
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
		try {
			numberOfInputData = getNumberOfInputData();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		inputData = new float[numberOfInputData][numInputNeurons];
		outputData = new float[numberOfInputData][numOutputNeurons];
		try {
			System.out.println("Trying to fill data...");
			fillData();
			System.out.println("Done filling data...");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Initialising weights...");
		float weightsLayerOne[][] = new float[numInputNeurons + 1][numHiddenLayerOneNeurons];
		float weightsLayerTwo[][] = null;
		float weightsOutLayer[][] = null;

		if (numHiddenLayerTwoNeurons == 0) {
			weightsOutLayer = new float[numHiddenLayerOneNeurons + 1][numOutputNeurons];
			if (usePreviousWeights) {
				try {
					fillWeightsFromFile(weightsLayerOne, weightsOutLayer);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				fillWeights(weightsLayerOne, weightsOutLayer);
			System.out.println("Initialised weights...");
			calculateOutputs(weightsLayerOne, weightsOutLayer);
		} else {
			weightsLayerTwo = new float[numHiddenLayerOneNeurons + 1][numHiddenLayerTwoNeurons];
			weightsOutLayer = new float[numHiddenLayerTwoNeurons + 1][numOutputNeurons];
			if (usePreviousWeights) {
				try {
					fillWeightsFromFile(weightsLayerOne, weightsLayerTwo, weightsOutLayer);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				fillWeights(weightsLayerOne, weightsLayerTwo, weightsOutLayer);
			System.out.println("Initialised weights...");
			calculateOutputs(weightsLayerOne, weightsLayerTwo, weightsOutLayer);
		}
		for(int i=0; i < numberOfInputData; i++) {
			printArray(inputData[i]);
			printArray(outputData[i]);
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
		inputFile = s.split(" ")[1];
		s = in.nextLine();
		System.out.println(s);
		usePreviousWeights = Integer.parseInt(s.split(" ")[1]) == 1;
		s = in.nextLine();
		System.out.println(s);
		weightsFile = s.split(" ")[1];
	}

	public static int getNumberOfInputData() throws FileNotFoundException {
		Scanner in = new Scanner(new File(inputFile));
		int count = 0;
		while (in.hasNextLine()) {
			in.nextLine();
			count++;
		}
		return count;
	}

	public static void fillData() throws FileNotFoundException {
		Scanner in = new Scanner(new File(inputFile));
		String line = "";
		for (int i = 0; i < inputData.length; i++) {
			line = in.nextLine();
			String[] values = line.split(" ");
			int count = 0;
			for (int j = 0; j < inputData[i].length; j++) {
				inputData[i][j] = Float.parseFloat(values[count]);
				count++;
			}
		}
	}

	public static void fillWeights(float[][] weightsLayerOne, float[][] weightsOutLayer) {
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				weightsLayerOne[i][j] = (float) (Math.random() * 2 - 1);
				System.out.println(weightsLayerOne[i][j]);
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = (float) (Math.random() * 2 - 1);
			}
		}
	}

	public static void fillWeights(float[][] weightsLayerOne, float[][] weightsLayerTwo, float[][] weightsOutLayer) {
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				weightsLayerOne[i][j] = (float) (Math.random() * 2 - 1);
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {
				weightsLayerTwo[i][j] = (float) (Math.random() * 2 - 1);
			}
		}
		for (int i = 0; i < (numHiddenLayerTwoNeurons + 1); i++) {
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = (float) (Math.random() * 2 - 1);
			}
		}

	}

	public static void fillWeightsFromFile(float[][] weightsLayerOne, float[][] weightsOutLayer)
			throws FileNotFoundException {
		Scanner in = new Scanner(new File(weightsFile));
		String line[];
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				weightsLayerOne[i][j] = Float.parseFloat(line[j]);
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = Float.parseFloat(line[j]);
			}
		}
	}

	public static void fillWeightsFromFile(float[][] weightsLayerOne, float[][] weightsLayerTwo,
			float[][] weightsOutLayer) throws FileNotFoundException {
		Scanner in = new Scanner(new File(weightsFile));
		String line[];
		for (int i = 0; i < (numInputNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numHiddenLayerOneNeurons; j++) {
				weightsLayerOne[i][j] = Float.parseFloat(line[j]);
			}
		}
		for (int i = 0; i < (numHiddenLayerOneNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numHiddenLayerTwoNeurons; j++) {
				weightsLayerTwo[i][j] = Float.parseFloat(line[j]);
			}
		}
		for (int i = 0; i < (numHiddenLayerTwoNeurons + 1); i++) {
			line = in.nextLine().split(" ");
			for (int j = 0; j < numOutputNeurons; j++) {
				weightsOutLayer[i][j] = Float.parseFloat(line[j]);
			}
		}

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

	public static void calculateOutputs(float[][] weightsLayerOne, float[][] weightsOutLayer) {
		float layerOneY[] = new float[numHiddenLayerOneNeurons];
		float output[] = new float[numOutputNeurons];
		float layerOneDelta[] = new float[numHiddenLayerOneNeurons];
		float outputDelta[] = new float[numOutputNeurons];
		for (int i = 0; i < inputData.length; i++) {
			calculateSigmoid(numHiddenLayerOneNeurons, weightsLayerOne, inputData[i], layerOneY);
			calculateSigmoid(numOutputNeurons, weightsOutLayer, layerOneY, output);
			outputData[i]= output.clone();
			currentLine++;
		}
	}

	public static void calculateOutputs(float[][] weightsLayerOne, float[][] weightsLayerTwo,
			float[][] weightsOutLayer) {
		for (int i = 0; i < inputData.length; i++) {
			float layerOneY[] = new float[numHiddenLayerOneNeurons];
			float layerTwoY[] = new float[numHiddenLayerTwoNeurons];
			float output[] = new float[numOutputNeurons];
			float layerOneDelta[] = new float[numHiddenLayerOneNeurons];
			float layerTwoDelta[] = new float[numHiddenLayerTwoNeurons];
			float outputDelta[] = new float[numOutputNeurons];
			calculateSigmoid(numHiddenLayerOneNeurons, weightsLayerOne, inputData[i], layerOneY);
			calculateSigmoid(numHiddenLayerTwoNeurons, weightsLayerTwo, layerOneY, layerTwoY);
			calculateSigmoid(numOutputNeurons, weightsOutLayer, layerTwoY, output);
			outputData[i]= output.clone();
			currentLine++;
		}
	}

	public static void printArray(float a[]) {
		for (int i = 0; i < a.length; i++) {
			if (i > 0)
				System.out.print(" ");
			System.out.print(a[i]);
		}
		System.out.println();
	}
}
