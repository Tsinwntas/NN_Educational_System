import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class NNFront {

	private JFrame frame;
	private JTextField textField;
	private JLabel[] params;

	private String parametersFile;
	private String fullPath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NNFront window = new NNFront();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NNFront() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 685);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel.setBounds(28, 57, 428, 500);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		params = new JLabel[22];
		int i = 0;

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(44, 25, 190, 31);
		panel.add(lblNewLabel);
		params[i++] = lblNewLabel;

		JLabel label = new JLabel("");
		label.setBounds(44, 67, 190, 31);
		panel.add(label);
		params[i++] = label;

		JLabel label_1 = new JLabel("");
		label_1.setBounds(44, 109, 190, 31);
		panel.add(label_1);
		params[i++] = label_1;

		JLabel label_2 = new JLabel("");
		label_2.setBounds(44, 151, 190, 31);
		panel.add(label_2);
		params[i++] = label_2;

		JLabel label_3 = new JLabel("");
		label_3.setBounds(44, 193, 190, 31);
		panel.add(label_3);
		params[i++] = label_3;

		JLabel label_4 = new JLabel("");
		label_4.setBounds(44, 235, 190, 31);
		panel.add(label_4);
		params[i++] = label_4;

		JLabel label_5 = new JLabel("");
		label_5.setBounds(44, 277, 190, 31);
		panel.add(label_5);
		params[i++] = label_5;

		JLabel label_6 = new JLabel("");
		label_6.setBounds(44, 319, 190, 31);
		panel.add(label_6);
		params[i++] = label_6;

		JLabel label_7 = new JLabel("");
		label_7.setBounds(44, 361, 190, 31);
		panel.add(label_7);
		params[i++] = label_7;

		JLabel label_8 = new JLabel("");
		label_8.setBounds(44, 403, 190, 31);
		panel.add(label_8);
		params[i++] = label_8;

		JLabel label_9 = new JLabel("");
		label_9.setBounds(44, 445, 190, 31);
		panel.add(label_9);
		params[i++] = label_9;

		JLabel label_10 = new JLabel("");
		label_10.setBounds(321, 25, 190, 31);
		panel.add(label_10);
		params[i++] = label_10;

		JLabel label_11 = new JLabel("");
		label_11.setBounds(321, 67, 190, 31);
		panel.add(label_11);
		params[i++] = label_11;

		JLabel label_12 = new JLabel("");
		label_12.setBounds(321, 109, 190, 31);
		panel.add(label_12);
		params[i++] = label_12;

		JLabel label_13 = new JLabel("");
		label_13.setBounds(321, 151, 190, 31);
		panel.add(label_13);
		params[i++] = label_13;

		JLabel label_14 = new JLabel("");
		label_14.setBounds(321, 193, 190, 31);
		panel.add(label_14);
		params[i++] = label_14;

		JLabel label_15 = new JLabel("");
		label_15.setBounds(321, 235, 190, 31);
		panel.add(label_15);
		params[i++] = label_15;

		JLabel label_16 = new JLabel("");
		label_16.setBounds(321, 277, 190, 31);
		panel.add(label_16);
		params[i++] = label_16;

		JLabel label_17 = new JLabel("");
		label_17.setBounds(321, 319, 190, 31);
		panel.add(label_17);
		params[i++] = label_17;

		JLabel label_18 = new JLabel("");
		label_18.setBounds(321, 361, 190, 31);
		panel.add(label_18);
		params[i++] = label_18;

		JLabel label_19 = new JLabel("");
		label_19.setBounds(321, 403, 190, 31);
		panel.add(label_19);
		params[i++] = label_19;

		JLabel label_20 = new JLabel("");
		label_20.setBounds(321, 445, 190, 31);
		panel.add(label_20);
		params[i++] = label_20;

		JButton btnNewButton = new JButton("TRAIN");
		btnNewButton.setBounds(69, 568, 123, 46);
		frame.getContentPane().add(btnNewButton);

		JButton button = new JButton("USE");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new NNExecuteDialog(fullPath);
			}
		});
		button.setBounds(281, 568, 123, 46);
		frame.getContentPane().add(button);

		textField = new JTextField();
		textField.setBounds(28, 26, 351, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		JButton button_1 = new JButton("...");
		button_1.setBounds(389, 25, 31, 23);
		frame.getContentPane().add(button_1);
		final JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		button_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int returnVal = fc.showOpenDialog(frame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					// This is where a real application would open the file.
					parametersFile = file.getName();
					fullPath = file.getAbsolutePath();
					textField.setText(fullPath);
					try {
						load();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		});

		JButton button_2 = new JButton("ASD");
		int offset = button_2.getInsets().left;
		button_2.setIcon(resizeIcon(new ImageIcon("C:\\Users\\ntsino01\\workspace\\NNbyTsinik\\refresh.png"),
				button_2.getWidth() - offset, button_2.getHeight() - offset));
		button_2.setBounds(425, 25, 31, 23);
		frame.getContentPane().add(button_2);
		button_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					load();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
			}
		});
	}

	private Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
		Image img = icon.getImage();
		Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
		return new ImageIcon(resizedImage);
	}

	private void load() throws FileNotFoundException {
		Scanner in = new Scanner(new File(fullPath));
		String line[];
		boolean clean = false;
		for (int i = 0; i < params.length / 2; i++) {
			if (!in.hasNextLine())
				clean = true;
			if (clean) {
				params[i].setText("");
				params[i + params.length / 2].setText("");
			} else {
				line = in.nextLine().split(" ");
				params[i].setText(line[0]);
				params[i + params.length / 2].setText(line[1]);
			}
		}
	}
}
