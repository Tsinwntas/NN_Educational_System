import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.sun.corba.se.spi.orbutil.fsm.Input;

import javax.swing.JTextField;
import javax.swing.JTextArea;

public class NNExecuteDialog extends JDialog {
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextArea textArea;

	private String saveFile;
	private String fullPath;

	private BackPropagationExecution be;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NNExecuteDialog dialog = new NNExecuteDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NNExecuteDialog(String paramsFile) {
		this();
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		be = new BackPropagationExecution(paramsFile);
		be.start();
		showScore();
	}

	public NNExecuteDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		textArea = new JTextArea();
		textArea.setBounds(10, 11, 414, 207);
		textArea.setEditable(false);
		contentPanel.add(textArea);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				textField = new JTextField();
				buttonPane.add(textField);
				textField.setColumns(10);
			}
			{
				JButton browse = new JButton("...");
				browse.setActionCommand("OK");
				buttonPane.add(browse);
				getRootPane().setDefaultButton(browse);
				final JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
				browse.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						int returnVal = fc.showOpenDialog(contentPanel);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = fc.getSelectedFile();
							// This is where a real application would open the
							// file.
							saveFile = file.getName();
							fullPath = file.getAbsolutePath();
							textField.setText(fullPath);
							try {
								save();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				});
			}
			{
				JButton save = new JButton("SAVE");
				save.setActionCommand("Cancel");
				buttonPane.add(save);
			}
		}
	}

	private void save() throws FileNotFoundException {
	}

	public void showScore() {
		Thread countPercentageExecution = new Thread() {
			public void run() {
				while (BackPropagationExecution.currentLine < BackPropagationExecution.numberOfInputData) {
					textArea.setText(
							BackPropagationExecution.currentLine * 100.0 / BackPropagationExecution.numberOfInputData
									+ "%");
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String toPrint = "";
				for(int i = 0 ; i < be.inputData.length; i ++){
					for(int j = 0; j <be.inputData[i].length; j++){
						if(j>0)toPrint+=" ";
						toPrint+=be.inputData[i][j];
					}
					toPrint+="\n";
					for(int j = 0; j <be.outputData[i].length; j++){
						if(j>0)toPrint+=" ";
						toPrint+=be.outputData[i][j];
					}
					toPrint+="\n";
				}
				textArea.setText(toPrint);
			}
		};
		countPercentageExecution.start();
	}
}
