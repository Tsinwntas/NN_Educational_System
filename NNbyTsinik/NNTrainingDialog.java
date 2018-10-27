import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NNTrainingDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JButton button;
	private JPanel drawPanel;

	private BackPropagationTraining bp;

	/**
	 * Launch the application.
	 *//*
		 * public static void main(String[] args) { try { NNTrainingDialog dialog = new
		 * NNTrainingDialog();
		 * dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		 * dialog.setVisible(true); } catch (Exception e) { e.printStackTrace(); } }
		 */

	/**
	 * Create the dialog.
	 */
	public NNTrainingDialog(String paramsFile) {
		this();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		bp = new BackPropagationTraining(paramsFile);
		bp.start();
		fillPanel();
		showGen();
	}

	private void fillPanel() {
		Thread showTeachings = new Thread() {
			public void run() {
				Graphics g = drawPanel.getGraphics();
				int x = drawPanel.getWidth();
				int y = drawPanel.getHeight();
				int forward = 50;
				while (true) {
					// g.fillRect(3/7*x, y/7, 100, 100);
					if (BackPropagationTraining.weightsToLayer1 == null)
						;
					else {
						if (BackPropagationTraining.numHiddenLayerTwoNeurons > 0) {
							int circleSize = 660;
							int currentSize = Math.min(70, circleSize / (BackPropagationTraining.numInputNeurons + 1));
							for (int i = 0; i < BackPropagationTraining.weightsToLayer1.length; i++) {
								for (int j = 0; j < BackPropagationTraining.weightsToLayer1[i].length; j++) {
									g.setColor(getWeightsColor(BackPropagationTraining.weightsToLayer1[i][j]));
									g.drawLine(forward + currentSize / 2 + x / 9,
											(2 * i + 1) * (y / (2 * BackPropagationTraining.numInputNeurons + 2)),
											forward + x * 3 / 9, (2 * j + 1)
													* (y / (2 * BackPropagationTraining.numHiddenLayerOneNeurons + 2)));
									g.setColor(Color.WHITE);
									g.fillOval(forward + x / 9 - currentSize / 2,
											(2 * i + 1) * (y / (2 * BackPropagationTraining.numInputNeurons + 2))
													- currentSize / 2,
											currentSize, currentSize);
								}
							}
							currentSize = Math.min(70,
									circleSize / (BackPropagationTraining.numHiddenLayerOneNeurons + 1));
							for (int i = 0; i < BackPropagationTraining.weightsToLayer2.length; i++) {
								for (int j = 0; j < BackPropagationTraining.weightsToLayer2[i].length; j++) {
									g.setColor(getWeightsColor(BackPropagationTraining.weightsToLayer2[i][j]));
									g.drawLine(forward + currentSize / 2 + 3 * x / 9,
											(2 * i + 1)
													* (y / (2 * BackPropagationTraining.numHiddenLayerOneNeurons + 2)),
											forward + x * 5 / 9, (2 * j + 1)
													* (y / (2 * BackPropagationTraining.numHiddenLayerTwoNeurons + 2)));
									g.setColor(Color.BLACK);
									g.fillOval(forward + 3 * x / 9 - currentSize / 2,
											(2 * i + 1)
													* (y / (2 * BackPropagationTraining.numHiddenLayerOneNeurons + 2))
													- currentSize / 2,
											currentSize, currentSize);
								}
							}
							currentSize = Math.min(70,
									circleSize / (BackPropagationTraining.numHiddenLayerTwoNeurons + 1));
							for (int i = 0; i < BackPropagationTraining.weightsToOutput.length; i++) {
								for (int j = 0; j < BackPropagationTraining.weightsToOutput[i].length; j++) {
									g.setColor(getWeightsColor(BackPropagationTraining.weightsToOutput[i][j]));
									g.drawLine(forward + currentSize / 2 + 5 * x / 9,
											(2 * i + 1)
													* (y / (2 * BackPropagationTraining.numHiddenLayerTwoNeurons + 2)),
											forward + x * 7 / 9,
											(2 * j + 1) * (y / (2 * BackPropagationTraining.numOutputNeurons + 1)));
									g.setColor(Color.BLACK);
									g.fillOval(forward + 5 * x / 9 - currentSize / 2,
											(2 * i + 1)
													* (y / (2 * BackPropagationTraining.numHiddenLayerTwoNeurons + 2))
													- currentSize / 2,
											currentSize, currentSize);
								}
							}
							currentSize = Math.min(70, circleSize / (BackPropagationTraining.numOutputNeurons));
							for (int i = 0; i < BackPropagationTraining.numOutputNeurons; i++) {
								g.setColor(Color.WHITE);
								g.fillOval(forward + 7 * x / 9 - currentSize / 2,
										(2 * i + 1) * (y / (2 * BackPropagationTraining.numOutputNeurons + 1))
												- currentSize / 2,
										currentSize, currentSize);
							}
						} else {

						}
					}
					if(BackPropagationTraining.currentIteration == BackPropagationTraining.maxIterations)break;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			private Color getWeightsColor(float f) {
				float toSet = Math.abs(f);
				toSet = Math.min(toSet/4*255,255);
				if(f >= 0)
					return new Color(0,0,Math.round(toSet));
				else return new Color(Math.round(toSet),0,0);
			}
		};
		showTeachings.start();
	}

	private void showGen() {
		Thread genCount = new Thread() {
			public void run() {
				while (true) {
					if (BackPropagationTraining.currentIteration < BackPropagationTraining.maxIterations
							&& !button.getText().equals("STOP")) {
						button.setText("STOP");
					} else if (bp.currentIteration == bp.maxIterations && !button.getText().equals("DONE")) {
						button.setText("DONE");
					}
					textField.setText("\t" + bp.toShow);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		genCount.start();
	}

	public NNTrainingDialog() {
		setBounds(100, 100, 1094, 1038);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 1076, 924);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);

		drawPanel = new JPanel();
		drawPanel.setBounds(12, 13, 1050, 907);
		contentPanel.add(drawPanel);
		drawPanel.setLayout(null);
		
				textField = new JTextField();
				textField.setBounds(12, 929, 905, 49);
				getContentPane().add(textField);
				textField.setEditable(false);
				textField.setColumns(10);
				
						button = new JButton("DONE");
						button.setBounds(929, 929, 135, 49);
						getContentPane().add(button);
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								dispose();
							}
						});
	}
}
