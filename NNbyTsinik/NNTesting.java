
public class NNTesting {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int option = 1;
		switch(option) {
		case 0:
		BackPropagationTraining bp = new BackPropagationTraining("parameters.txt");
		bp.start();break;
		case 1:
			BackPropagationExecution be = new BackPropagationExecution("parameters2.txt");
			be.start();
		}
		createPercentageCounter(option);
	}

	public static void createPercentageCounter(int option) {
		switch (option) {
		case 0:
			Thread countPercentageTraining = new Thread() {
				public void run() {
					while (BackPropagationTraining.currentIteration < BackPropagationTraining.maxIterations) {
						System.out.println(
								BackPropagationTraining.currentIteration * 100.0 / BackPropagationTraining.maxIterations + "%");
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			countPercentageTraining.start();
			break;
		case 1:
			Thread countPercentageExecution = new Thread() {
				public void run() {
					while (BackPropagationExecution.currentLine < BackPropagationExecution.numberOfInputData) {
						System.out.println(
								BackPropagationExecution.currentLine * 100.0 / BackPropagationExecution.numberOfInputData + "%");
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			countPercentageExecution.start();
			break;
		}
	}

}
