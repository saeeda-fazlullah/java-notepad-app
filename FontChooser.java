/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

/**
 *
 * @author duha
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.border.Border;

public class FontChooser extends JDialog implements ActionListener
{
	private JComboBox<String> fontCombo;
	private JButton colorButton, OKButton, cancelButton;//Buttons can be configured, and to some degree controlled
	private JColorChooser colorChooser; //JColorChooser provides a pane of controls designed to allow a user to manipulate and select a colo
	private JCheckBox boldCheckBox, italicCheckBox;
	private JComboBox<Integer> sizeCombo; //which lets the user choose one of several choices
	private JTextArea exampleArea;//A JTextArea is a multi-line area that displays plain text
	private JLabel fontLabel, colorLabel, styleLabel, sizeLabel, exampleLabel; //A display area for a short text string or an image,
	
	public static final int OK_OPTION = 1; //the box dialog for ok 
	public static final int CANCEL_OPTION = 0; //the box dialog for cancel
	public int choosenOption = CANCEL_OPTION;
	
	private Font currentFont;
	private Color currentColor;
	
	FontChooser(Font currentFont, Color currentColor) //font chooser constructer
	{
		this.currentFont = currentFont;
		this.currentColor = currentColor;
		setTitle("Font");
		setResizable(false);
		setLayout(new GridBagLayout()); //determines the size and position of the components within a container
		setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Dispose of the frame object, but keep the application running.

	
		GridBagConstraints constraints = new GridBagConstraints(); // is a very flexible layout manager that allows you to position components relative to one another using constraints
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(3,5,3,5);
		
		fontLabel = new JLabel("Font:");
		fontLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		constraints.gridx = 0; //Controls the position of the component on the layout’s grid
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(fontLabel, constraints);
		fontLabel.setVisible(true);
		
		String[] fontNames;
		{ 	//gets list of fonts from system
			GraphicsEnvironment graphicsEnvironment;	//The GraphicsEnvironment class describes the collection of GraphicsDevice objects and Font objects available to a Java							
			graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			fontNames = graphicsEnvironment.getAvailableFontFamilyNames();
		}
		
                 //===CREATE COMPONENTS=== 
                 //الازرار الي محطوطة جوا الفريم بعد م تختار الاوبشن حق الفونت افتحيه تشوفي ازارا كنسل وشوزر و اوك هدي هنا مزبطه على الفريم
		fontCombo = new JComboBox<String>(fontNames);
		fontCombo.setEditable(false);
		String currentFontName = currentFont.getFamily();
		//System.out.println(currentFont.getFamily());
		if (currentFontName.equals("Dialog.plain")) currentFontName = "Arial";  //case when font name is incorrect
		fontCombo.setSelectedItem(currentFontName);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(fontCombo, constraints);
		
		colorLabel = new JLabel("Color:");
		colorLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		constraints.gridx = 2;
		constraints.gridy = 0;
		add(colorLabel, constraints);
		
		colorButton = new JButton("Color chooser");
		constraints.gridx = 2;
		constraints.gridy = 1;
		add(colorButton, constraints);
		
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		
		sizeLabel = new JLabel("Size:");
		constraints.gridx = 0;
		constraints.gridy = 2;
		add(sizeLabel, constraints);
		//Array for chosing the font size 
		Integer[] fontSizes = {8, 9, 10, 11, 12, 14, 16, 18, 20, 22, 24, 26, 28, 36, 48, 72};
		sizeCombo = new JComboBox<Integer>(fontSizes);
		sizeCombo.setSelectedItem((Integer)currentFont.getSize());
		sizeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
		sizeCombo.setEditable(false);
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.ipadx = 30;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(sizeCombo, constraints);
		
		constraints.ipadx = 0;
		
		styleLabel = new JLabel("Font style:"); //the font style نوع الخط 
		styleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
		constraints.gridx = 3;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(styleLabel, constraints);
		
		constraints.anchor = GridBagConstraints.LINE_START;
		
		boldCheckBox = new JCheckBox("BOLD");
		boldCheckBox.setFont(new Font("Arial", Font.BOLD, 14));
		constraints.gridx = 4;
		constraints.gridy = 2;
		add(boldCheckBox, constraints);
		
		italicCheckBox = new JCheckBox("ITALIC");
		italicCheckBox.setFont(new Font("Arial", Font.ITALIC, 14));
		constraints.gridx = 4;
		constraints.gridy = 3;
		add(italicCheckBox, constraints);
		
		exampleLabel = new JLabel("Example:");
		exampleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
		constraints.gridx = 0;
		constraints.gridy = 3;
		add(exampleLabel, constraints);
		
		exampleArea = new JTextArea("Aa Bb Cc");
		exampleArea.setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), currentFont.getSize()));
		exampleArea.setForeground(currentColor);
		exampleArea.setEditable(false);
		Border exampleBorder = BorderFactory.createLineBorder(Color.BLACK);													//creates black border around
		exampleArea.setBorder(BorderFactory.createCompoundBorder(exampleBorder,BorderFactory.createEmptyBorder(2,2,2,2)));	//text area
		constraints.gridx = 0;
		constraints.gridy = 4;
		constraints.gridwidth = 3;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		add(exampleArea, constraints);
		
		constraints.gridwidth = 1;
		constraints.fill = GridBagConstraints.NONE;
		
		OKButton = new JButton("OK");
		constraints.gridx = 3;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(OKButton, constraints);
		
		cancelButton = new JButton("Cancel");
		constraints.gridx = 4;
		constraints.gridy = 5;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(cancelButton, constraints);
		
		pack();
		setVisible(true);
		
                 //CURRENT FONT STYLE
		int currentFontStyle = currentFont.getStyle();
		switch (currentFontStyle)
		{
		case (Font.BOLD):
			boldCheckBox.setSelected(true);
			italicCheckBox.setSelected(false);
			break;
		case (Font.ITALIC):
			boldCheckBox.setSelected(false);
			italicCheckBox.setSelected(true);
			break;
		case (Font.ITALIC + Font.BOLD):
			boldCheckBox.setSelected(true);
			italicCheckBox.setSelected(true);
			break;
		default:
			boldCheckBox.setSelected(false);
			italicCheckBox.setSelected(false);
			break;
		}
		
                  //SET EVENTS
		fontCombo.addActionListener(this);
		sizeCombo.addActionListener(this);
		boldCheckBox.addActionListener(this);
		italicCheckBox.addActionListener(this);
		colorButton.addActionListener(this);
		OKButton.addActionListener(this);
		cancelButton.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if (source == fontCombo || source == sizeCombo || source == boldCheckBox || source == italicCheckBox)
		{	//set font in Example Field
			exampleArea.setFont(getSelectedFont());
			pack();
		}
		else if (source == colorButton)
		{
			Color selectedColor = JColorChooser.showDialog(this, "Color", currentColor);
			exampleArea.setForeground(selectedColor);
		}
		else if (source == OKButton)
		{
			choosenOption = OK_OPTION;
			dispose();
		}
		else if (source == cancelButton)
		{
			choosenOption = CANCEL_OPTION;
			dispose();
		}
	}
	
	public Font getSelectedFont()
	{
		String selectedName = fontCombo.getSelectedItem().toString();
		int selectedSize = (int)sizeCombo.getSelectedItem();
		int selectedStyle;
		if (boldCheckBox.isSelected() && italicCheckBox.isSelected()) selectedStyle = Font.BOLD + Font.ITALIC;
		else if (boldCheckBox.isSelected()) selectedStyle = Font.BOLD;
		else if (italicCheckBox.isSelected()) selectedStyle = Font.ITALIC;
		else selectedStyle = Font.PLAIN;
		Font selectedFont = new Font(selectedName, selectedStyle, selectedSize);
		return selectedFont;
	}
	
	public Color getSelectedColor() //get the color that user chose
	{
		Color selectedColor = exampleArea.getForeground();
		return selectedColor;
	}
}