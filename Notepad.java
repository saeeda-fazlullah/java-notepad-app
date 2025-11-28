/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

/**
 *
 * @author Saeeda
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.ImageFilter;
import java.awt.font.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import static javax.swing.JFileChooser.SAVE_DIALOG;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout.Constraints;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.UndoManager;

public class Notepad extends JFrame implements ActionListener
{
	public static void main(String[] args)
	{
		Notepad app = new Notepad();
		app.addWindowListener(new WindowAdapter() 
		{
			   public void windowClosing(WindowEvent evt) 
			   {
			     app.exit();
			   }
		});
		app.setVisible(true);
	}
	
	private String fileName = null;
	private String fileDirectory = null;
	private boolean isEdited = false;
	private Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JMenuBar menuMain;
	private JMenu menuFile, menuEdit, menuFormat, menuHelp;
	private JMenuItem itemNew, itemOpen, itemSave, itemSaveAs, itemExit;
	private JMenuItem itemFind, itemReplace, itemTimeDate;
	private JMenuItem itemFont, itemBackground;
	private JMenuItem itemHelp, itemAbout;
	private JPopupMenu popupMenu;
	private JMenuItem itemCopy, itemCut, itemPaste,itemUndo,itemRedo,itemSelectAll;
	protected UndoManager undoManager;
	
	Notepad() //CUNSTRUCTOR 
	{
		setSize(1000, 600);//size of frame
		setTitle("Notepad");//the title on frame
		setLayout(new BorderLayout()); 
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
                  //CREATE COMPONENTS(Menu-MenuItem)
                  //TextArea is the font style and size on compenets difine here 
		textArea = new JTextArea();
		textArea.setFont(new Font ("Arial", Font.PLAIN, 12));
		textArea.setForeground(Color.BLACK);
		textArea.setCaretColor(Color.BLACK);
                 scrollPane = new JScrollPane(textArea);
                 undoManager = new UndoManager();
                
		menuMain = new JMenuBar();

                 menuFile = new JMenu("File");
                 menuFile.setMnemonic('F');
		
		itemNew = new JMenuItem("New");
		itemNew.setMnemonic('N');
		
		itemOpen = new JMenuItem("Open...");
		itemOpen.setMnemonic('O');
		
		itemSave = new JMenuItem("Save");
		itemSave.setMnemonic('S');
		
		itemSaveAs = new JMenuItem("Save As...");
		itemSaveAs.setMnemonic('A');
		
		itemExit = new JMenuItem("Exit");
		itemExit.setMnemonic('E');
		
		menuEdit = new JMenu("Edit");
		
		itemFind = new JMenuItem("Find...");
		itemFind.setMnemonic('F');
		//add menu item undo
                 itemUndo = new JMenuItem("Undo");
                 itemUndo.setMnemonic('U');
                 //add menu item redo
                 itemRedo = new JMenuItem("Redo");
                 itemRedo.setMnemonic('R');
                
		itemReplace = new JMenuItem("Find and replace...");
		itemReplace.setMnemonic('R');
		
		itemTimeDate = new JMenuItem("Time/Date");
		itemTimeDate.setMnemonic('T');
		
		menuFormat = new JMenu("Format");
		
		itemFont = new JMenuItem("Font...");
		itemFont.setMnemonic('F');
		
		itemBackground = new JMenuItem("Background...");
		itemBackground.setMnemonic('B');

		menuHelp = new JMenu("Help");

		itemHelp = new JMenuItem("Help");
		itemHelp.setMnemonic('H');
		
		popupMenu = new JPopupMenu();
		
		itemCopy = new JMenuItem("Copy");
		itemCut = new JMenuItem("Cut");
		itemPaste = new JMenuItem("Paste");
                 itemSelectAll=new JMenuItem("selectAll"); 
		
		
                 //EVENTS AND SHORTCUTS
		textArea.getDocument().addDocumentListener(new DocumentListener()
				{
			@Override
			public void changedUpdate(DocumentEvent e) 
				{isEdited = true;}
			@Override
			public void removeUpdate(DocumentEvent e) 
				{isEdited = true;}
			@Override
			public void insertUpdate(DocumentEvent e)
				{isEdited = true;}
				});
		
		itemAbout = new JMenuItem("About the program...");
		itemAbout.setMnemonic('A');
		
		itemNew.addActionListener(this);
		itemNew.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
		
		itemOpen.addActionListener(this);
		itemOpen.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
		
		itemSave.addActionListener(this);
		itemSave.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
		
		itemSaveAs.addActionListener(this);
		itemSaveAs.setAccelerator(KeyStroke.getKeyStroke("ctrl alt N"));
		
		itemExit.addActionListener(this);
		itemExit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));
		
		itemFind.addActionListener(this);
		itemFind.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
		
		itemReplace.addActionListener(this);
		itemReplace.setAccelerator(KeyStroke.getKeyStroke("ctrl R"));
		
		itemTimeDate.addActionListener(this);
		itemTimeDate.setAccelerator(KeyStroke.getKeyStroke("F5"));
                
		itemUndo.addActionListener(this);
                 itemUndo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
                 
                 itemRedo.addActionListener(this);
                 itemRedo.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
                 
		itemFont.addActionListener(this);
		
		itemBackground.addActionListener(this);
		
		itemHelp.addActionListener(this);
		itemHelp.setAccelerator(KeyStroke.getKeyStroke("F1"));
		
		itemAbout.addActionListener(this);
		
		itemCopy.addActionListener(this);
		itemCut.addActionListener(this);
		itemPaste.addActionListener(this);
                
                 itemSelectAll.addActionListener(this);  
		itemSelectAll.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
                 //ADD COMPONENTS
		add(scrollPane, BorderLayout.CENTER);
		add(menuMain, BorderLayout.PAGE_START);
		
		menuMain.add(menuFile);
		menuFile.add(itemOpen);
		menuFile.add(itemNew);
		menuFile.add(itemSave);
		menuFile.add(itemSaveAs);
		menuFile.addSeparator();
		menuFile.add(itemExit);
		
		menuMain.add(menuEdit);
                 menuEdit.add(itemFind);
                 menuEdit.add(itemReplace);
                 menuEdit.add(itemRedo);
                 menuEdit.add(itemUndo);
                 menuEdit.addSeparator();
                 menuEdit.add(itemTimeDate);
                 menuEdit.add(itemSelectAll);
		
		menuMain.add(menuFormat);
		menuFormat.add(itemFont);
		menuFormat.add(itemBackground);
		
		menuMain.add(menuHelp);
		menuHelp.add(itemHelp);
		menuHelp.add(itemAbout);
		
		popupMenu.add(itemCopy);
		popupMenu.add(itemCut);
		popupMenu.add(itemPaste);
		textArea.setComponentPopupMenu(popupMenu);
                
                 textArea.getDocument().addUndoableEditListener(
                    new UndoableEditListener() {
                 public void undoableEditHappened(UndoableEditEvent e) {
                    undoManager.addEdit(e.getEdit());
                    updateButtons();
                }
            });

	}
	
         //CALL EVENTS
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if (source == itemNew)
			createNewFile();
		else if (source == itemOpen)
			openFile();
		else if (source == itemSave)
			saveFile();
		else if (source == itemSaveAs)
			saveFileAs();
		else if (source == itemExit)
			exit();
		else if (source == itemFind)
			find();
		else if (source == itemReplace)
			replace();
		else if (source == itemTimeDate)
			addTimestamp();
		else if (source == itemFont)
			changeFont();
		else if (source == itemBackground)
			changeBackground();
		else if (source == itemHelp)
			help();
		else if (source == itemAbout)
			aboutProgram();
		else if (source == itemCopy)
			copy();
		else if (source == itemCut)
			cut();
		else if (source == itemPaste)
			paste();
                 else if (source == itemUndo)
                          undo();
                 else if (source == itemRedo)
                          redo();
                 else if(source==itemSelectAll)
                          selectAll();
	}
	
             //FUNCTIONS CALLED BY EVENTS
	private void createNewFile()
	{
            if (textArea.getText().equals(""))
		clearFile();
            else
            {
		int answer = JOptionPane.showConfirmDialog(this, "Do you want to save this file?", "Save file", JOptionPane.YES_NO_CANCEL_OPTION);
		switch (answer)
		{
			case JOptionPane.YES_OPTION:
		{
			if (fileName == null) saveFileAs();
			else saveFile();
		}
			case JOptionPane.NO_OPTION:
				clearFile();
		}
            }
            isEdited = false;
	}
	
	private void openFile()
	{
		JFileChooser fileChooser = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt");
		fileChooser.addChoosableFileFilter(filter);
		fileChooser.setAcceptAllFileFilterUsed(false);
		if(isEdited == true)
		{
			int answer = JOptionPane.showConfirmDialog(this, "Do you want to save your file?", "Confirm", JOptionPane.YES_NO_CANCEL_OPTION);
			if (answer == JOptionPane.YES_OPTION) saveFile();
			else if (answer == JOptionPane.CANCEL_OPTION) return;
		}
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			File choosenFile = fileChooser.getSelectedFile();
			Scanner stream = null;
			try 
			{
				textArea.setText("");
				stream = new Scanner(choosenFile);
				while (stream.hasNext())
					textArea.append(stream.nextLine() + "\n");
				fileDirectory = choosenFile.getAbsolutePath();
				fileName = choosenFile.getName();
				setTitle(fileName);
			} 
			catch (FileNotFoundException e1) 
			{
				JOptionPane.showMessageDialog(null, "Opening file failed!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				if (stream != null)
					stream.close();
			}
			isEdited = false;
		}
	}
	
	private void saveFile()
	{
		if (fileName != null && isEdited == true)
		{
			File currentFile = new File(fileDirectory);
			PrintWriter stream = null;
			Scanner scanner = null;
			try 
			{
				scanner = new Scanner(textArea.getText());
				stream = new PrintWriter(currentFile);
				while(scanner.hasNext())
					stream.println(scanner.nextLine());
			} 
			catch (FileNotFoundException e) 
			{
				JOptionPane.showMessageDialog(null, "An error has occured!\nFile has not been saved.", "Error", JOptionPane.ERROR_MESSAGE);
			}	
			finally
			{
				if (scanner != null)
					scanner.close();
				if (stream != null)
					stream.close();
			}
			isEdited = false;
		}
		else saveFileAs();
	}
	
	private void saveFileAs()
	{
		JFileChooser fileChooser = new JFileChooser()
		{
                    @Override
                    public void approveSelection()
                    {
			File choosenFile = getSelectedFile();
			if (choosenFile.exists() && getDialogType() == SAVE_DIALOG)
			{
				int result = JOptionPane.showConfirmDialog(null, "This file already exists." + "\n" + "Are you sure to overwrite it?", "Confirm", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
					super.approveSelection();
			}
			else if (!choosenFile.exists() && getDialogType() == SAVE_DIALOG)
			{
				super.approveSelection();
			}
                     }
		};
	FileFilter filter = new FileNameExtensionFilter("Text Files (.txt)", "txt");
	fileChooser.addChoosableFileFilter(filter);
	fileChooser.setAcceptAllFileFilterUsed(false);
		
	if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
        {
            File choosenFile = fileChooser.getSelectedFile();
            String choosenFilePath = choosenFile.getAbsolutePath().toString();
            if (!choosenFilePath.endsWith(".txt"))
		choosenFile = new File(choosenFilePath + ".txt");
			
		fileName = choosenFile.getName();
		fileDirectory = choosenFile.getAbsolutePath();
		setTitle(fileName);
			
		PrintWriter stream = null;
		Scanner scanner = null;
	try 
	{
		scanner = new Scanner(textArea.getText());
		stream = new PrintWriter(choosenFile);
		while(scanner.hasNext()) 
			stream.println(scanner.nextLine());
	} 
	catch (FileNotFoundException e) 
	{
		JOptionPane.showMessageDialog(null, "An error has occured!\nFile has not been saved.", "Error", JOptionPane.ERROR_MESSAGE);
	}
	finally
	{
            if (stream != null) 
		stream.close();
            if (scanner != null) 
		scanner.close();
	}
	isEdited = false;
	}
	}
	
	private void exit()
	{
		if ((fileName != null || !textArea.getText().contentEquals("")) && isEdited == true)
		{
			int ans = JOptionPane.showConfirmDialog(this, "Save this file?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
			switch (ans)
			{
				case JOptionPane.YES_OPTION:
				{
					if (fileName == null) saveFileAs();
					else saveFile();
					setDefaultCloseOperation(EXIT_ON_CLOSE);
					dispose();
					
				}
				case JOptionPane.NO_OPTION:
				{
					setDefaultCloseOperation(EXIT_ON_CLOSE);
					dispose();
				}
			}
		}
		else 
		{
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			dispose();
		}
	}
	
	private void find()
	{
		JDialog findWindow;
		JLabel findLabel;
		JTextField textToFindField;
		JButton findButton, cancelButton;
		JCheckBox startAtBeginningBox;
		
		//create new dialog window
		findWindow = new JDialog();
		findWindow.setLocationRelativeTo(Notepad.this);
		findWindow.setTitle("Finding");
		findWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		//findWindow.setResizable(false);

		findWindow.setLayout(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(3,5,3,5);
		
                 //CREATE COMPONENTS
		findLabel = new JLabel("Find: ");
		findLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		constraints.gridx = 0;
		constraints.gridy = 0;
		findWindow.add(findLabel, constraints);
		
		textToFindField = new JTextField();
		constraints.gridwidth = 2;
		constraints.gridx = 1;
		constraints.gridy = 0;
		findWindow.add(textToFindField, constraints);
		
		findButton = new JButton("Find next");
		constraints.gridwidth = 1;
		constraints.gridx = 0;
		constraints.gridy = 2;
		//constraints.anchor = GridBagConstraints.NONE;
		findWindow.add(findButton, constraints);
		
		cancelButton = new JButton("Cancel");
		constraints.gridx = 2;
		constraints.gridy = 2;
		findWindow.add(cancelButton, constraints);
		
		startAtBeginningBox = new JCheckBox("Start at the beginning");
		constraints.fill = GridBagConstraints.CENTER;
		constraints.gridwidth = 2;
		constraints.gridx = 0;
		constraints.gridy = 1;
		findWindow.add(startAtBeginningBox, constraints);
		
		findWindow.pack();
		findWindow.setVisible(true);
		
                  //SET EVENTS
		findButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String text = textArea.getText();
				String toFind = textToFindField.getText();
				int start;
				if (startAtBeginningBox.isSelected())
				{
					start = 0;
					startAtBeginningBox.setSelected(true);
				}
					
				else
					start = textArea.getCaretPosition();
				int index = text.indexOf(toFind, start);
				if (index != -1)
					textArea.select(index, index + toFind.length());
				else
					JOptionPane.showMessageDialog(findWindow, "Can't find \"" + toFind + "\".", "Notepad",JOptionPane.ERROR_MESSAGE);
			}
		});
		
		cancelButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) {findWindow.dispose();}
		});
	}
	
	private void replace()
	{
		JDialog replaceWindow;
		JLabel findLabel, replaceForLabel;
		JTextField textToFindField, textToReplaceForField;
		JButton replaceNextButton, replaceAllButton, cancelButton;
		JCheckBox startAtBeginningBox;
		
		//create new dialog window
		replaceWindow = new JDialog();
		{
			replaceWindow.setLocationRelativeTo(Notepad.this);
			
			replaceWindow.setTitle("Replacing");
			replaceWindow.setLocationRelativeTo(Notepad.this);
			replaceWindow.setLayout(new GridBagLayout());
			replaceWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(3,5,3,5);
		
                 //CREATE COMPONENTS
		findLabel = new JLabel("Find: ");
		findLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		constraints.gridx = 0;
		constraints.gridy = 0;
		replaceWindow.add(findLabel, constraints);
		
		textToFindField = new JTextField();
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		replaceWindow.add(textToFindField, constraints);
		
		replaceForLabel = new JLabel("Replace for: ");
		replaceForLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		replaceWindow.add(replaceForLabel, constraints);
		
		textToReplaceForField = new JTextField();
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		replaceWindow.add(textToReplaceForField, constraints);
		
		replaceNextButton = new JButton("Replace next");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		replaceWindow.add(replaceNextButton, constraints);
		
		replaceAllButton = new JButton("Replace all");
		constraints.gridx = 1;
		constraints.gridy = 3;
		replaceWindow.add(replaceAllButton, constraints);
		
		cancelButton = new JButton("Cancel");
		constraints.gridx = 2;
		constraints.gridy = 3;
		replaceWindow.add(cancelButton, constraints);
		
		startAtBeginningBox = new JCheckBox("Start at the beginning");
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		replaceWindow.add(startAtBeginningBox, constraints);
		
		replaceWindow.pack();
		replaceWindow.setVisible(true);
		
                 //--SET EVENTS--
		replaceNextButton.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						String text = textArea.getText();
						String toFind = textToFindField.getText();
						String toReplaceFor = textToReplaceForField.getText();
						
						int start;
						if (startAtBeginningBox.isSelected())
						{
							start = 0;
							startAtBeginningBox.setSelected(false);
						}
						else
							start = textArea.getCaretPosition();
						int index = text.indexOf(toFind, start);
						if (index != -1)
						{
							textArea.replaceRange(toReplaceFor, index, index + toFind.length());
							textArea.setCaretPosition(index + toReplaceFor.length());
						}
						else
							JOptionPane.showMessageDialog(replaceWindow, "Can't find \"" + toFind + "\".", "Notepad", JOptionPane.ERROR_MESSAGE);
					}
				});
		
		replaceAllButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						String text = textArea.getText();
						String toFind = textToFindField.getText();
						String toReplaceFor = textToReplaceForField.getText();
						
						int start;
						if (startAtBeginningBox.isSelected())
							start = 0;
						else
							start = textArea.getCaretPosition();
						
						int replacements = 0;
						while (text.indexOf(toFind, start) != -1)
						{
							int index = text.indexOf(toFind, start);
							textArea.replaceRange(toReplaceFor, index, index + toFind.length());
							text = textArea.getText();
							start = index + toReplaceFor.length();
							replacements++;
						}
						if (replacements == 0)
							JOptionPane.showMessageDialog(replaceWindow, "Can't find \"" + toFind + "\".", "Notepad", JOptionPane.ERROR_MESSAGE);
						else if (replacements == 1)
							JOptionPane.showMessageDialog(replaceWindow, "Replaced " + 1 + " occurence.", "Notepad", JOptionPane.INFORMATION_MESSAGE);
						else
							JOptionPane.showMessageDialog(replaceWindow, "Replaced " + replacements + " occurences.", "Notepad", JOptionPane.INFORMATION_MESSAGE);
					}
				});
		
		cancelButton.addActionListener(new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) {replaceWindow.dispose();}
				});
	}
	
	private void addTimestamp() //will return the current time 
	{
		String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
		textArea.append(timestamp + " ");
	}
	
	private void changeFont()
	{
		FontChooser fontChooser = new FontChooser(textArea.getFont(), textArea.getForeground());
		fontChooser.setLocationRelativeTo(this);
		fontChooser.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosed(WindowEvent e)
			{
				/*System.out.println(e.getSource().getClass().getName());
				System.out.println("DOOOHAA");
				System.out.println(fontChooser.choosenOption);*/
				if (fontChooser.choosenOption == FontChooser.OK_OPTION)
				{
					textArea.setFont(fontChooser.getSelectedFont());
					textArea.setForeground(fontChooser.getSelectedColor());
				}
				fontChooser.dispose();
			}
		});
	}
	
	private void changeBackground()
	{
		textArea.setBackground(JColorChooser.showDialog(this, "Background", textArea.getBackground()));
	}
	
	private void help() //the URL that take u to our website 
	{
		URL url = null;
		try 
		{
			url = new URL("https://drive.google.com/file/d/1GIW8uQl2C6UF63BtY7akmpPN9TG7_nJa/view?usp=sharing");
		} 
		catch (MalformedURLException e1) 
		{
			e1.printStackTrace();
		}
                 //open WebPage
		{ 					
			URI uri = null;
			try 
			{
				uri = url.toURI();
			} 
			catch (URISyntaxException e) 
			{
				e.printStackTrace();
			}
			
			Desktop desktop = Desktop.getDesktop();
			try 
			{
				desktop.browse(uri);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void aboutProgram() //the help option 
	{
		JOptionPane.showMessageDialog(this, "Notepad\n" 
                 + "Version: 1.0\n"
                 + "Author: HUDA DOHA SAEEDA\n"
                 + "Source Code: Islamic unversity ",
                   "About Me", JOptionPane.INFORMATION_MESSAGE); 
	}
        private void selectAll()
        {
             itemSelectAll.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                textArea.selectAll();
                itemSelectAll.transferFocusBackward();
            }
        });
        }
	
	private void copy()
	{
		String selectedText = textArea.getSelectedText();
		Transferable transferable = new StringSelection(selectedText);
		clipboard.setContents(transferable, null);
	}
	
	private void cut()
	{
		String selectedText = textArea.getSelectedText();
		Transferable transferable = new StringSelection(selectedText);
		clipboard.setContents(transferable, null);
		textArea.replaceRange("", textArea.getSelectionStart(), textArea.getSelectionEnd());
	}
	
	private void paste()
	{
		Transferable transferable = clipboard.getContents(null);
		String textToPaste;
		try 
		{
			textToPaste = (String)transferable.getTransferData(DataFlavor.stringFlavor);
			textArea.insert(textToPaste, textArea.getCaretPosition());
		} 
		catch (UnsupportedFlavorException | IOException e) 
		{
			e.printStackTrace();
		}
	}

	private void clearFile()
	{
		textArea.setText(null);
		fileName = null;
		fileDirectory = null;
		setTitle("Notepad");
	}
        
        private void undo()
        {
            itemUndo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            try {
		 undoManager.undo();
            } catch (Exception ex) {
                
				}
            updateButtons();
            }
            
	});

        }
        
        private void redo()
        {
         
           itemRedo.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	    try {
		   undoManager.redo();
	    } catch (Exception ex) {
				}
            updateButtons();
			}
		});
            
        }
        
        public void updateButtons() {
            itemUndo.setText(undoManager.getUndoPresentationName());
            itemRedo.setText(undoManager.getRedoPresentationName());
            itemUndo.setEnabled(undoManager.canUndo());
            itemRedo.setEnabled(undoManager.canRedo());
        }

}