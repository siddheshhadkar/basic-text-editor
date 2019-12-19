import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.*;
public class GUI {
	static UndoManager[] undoMan = new UndoManager[10];
	static JFrame frame;
	//Panels according to region
	FlowLayout layout = new FlowLayout();
	JPanel south= new JPanel(layout);
	//Menu Bar
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu edit = new JMenu("Edit");
	JMenu extra = new JMenu("Extra");
	JMenu preference = new JMenu("Preference");
	//Menu Items
	//FILE
	JMenuItem newFile = new JMenuItem("New", KeyEvent.VK_N);
	JMenuItem open = new JMenuItem("Open", KeyEvent.VK_O);
	JMenuItem save = new JMenuItem("Save", KeyEvent.VK_S);
	JMenuItem saveAs = new JMenuItem("Save As", KeyEvent.VK_A);
	JMenuItem close = new JMenuItem("Close Tab", KeyEvent.VK_C);
	JMenuItem closeAll = new JMenuItem("Close all Tabs", KeyEvent.VK_L);
	JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_E);
	//EDIT
	JMenuItem cut = new JMenuItem("Cut");
	JMenuItem copy = new JMenuItem("Copy");
	JMenuItem paste = new JMenuItem("Paste");
	JMenuItem undoButton = new JMenuItem("undoButton");
	JMenuItem redoButton= new JMenuItem("redoButton");
	//EXTRA
	JCheckBoxMenuItem readOnly = new JCheckBoxMenuItem("Read Only");
	static String mode = "Writable";
	static String rev="Unknown";
	//preference
	JMenu appearance = new JMenu("Appearance");
	static JMenuItem fontDialog= new JMenuItem("Font");
	static JMenuItem incFont = new JMenuItem("Increase Font");
	static JMenuItem decFont= new JMenuItem("Decrease Font");
	//Icons Bar
	//JLabel numLine;
	static JTabbedPane tabPane = new JTabbedPane();
	static int taCount=0;
	static JTextArea[] ta = new JTextArea[10];
	static JScrollPane[] jsp = new JScrollPane[10];
	static JLabel statusbar = new JLabel(mode+"     |  "+rev+" file  |");
	
	public GUI() {
		frame = new JFrame("Text Editor");
		frame.setSize(1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		eventListenersAdd();
		accelsAdd();
		menuAdditions();
		for(int i=0;i<10;i++) {
			undoMan[i] = new UndoManager();
			ta[i] = new JTextArea();
			ta[i].setFont(new Font("Arial", Font.PLAIN, 16));
		}
		
		frame.setJMenuBar(menuBar);
		frame.setLocationRelativeTo(null);
		frame.add(tabPane, BorderLayout.CENTER);
		south.add(statusbar);
		layout.setAlignment(FlowLayout.LEFT);
		frame.add(south, BorderLayout.SOUTH);
		frame.setVisible(true);
	}
	public void eventListenersAdd() {
		newFile.addActionListener(new FileHandler());
		open.addActionListener(new FileHandler());
		save.addActionListener(new FileHandler());
		saveAs.addActionListener(new FileHandler());
		close.addActionListener(new FileHandler());
		closeAll.addActionListener(new FileHandler());
		exit.addActionListener(new FileHandler());
		undoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
				undoMan[tabPane.getSelectedIndex()].undo();
				}
				catch(CannotRedoException cre) {
					cre.printStackTrace();
				}
				undoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canUndo());
				redoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canRedo());
			}
		});
		redoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {
					undoMan[tabPane.getSelectedIndex()].redo();
					}
					catch(CannotRedoException cre) {
						cre.printStackTrace();
					}
				undoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canUndo());
				redoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canRedo());
			}
		});
		tabPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				if(tabPane.getSelectedIndex()>=0) {
					undoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canUndo());
					redoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canRedo());
					ta[tabPane.getSelectedIndex()].getDocument().addUndoableEditListener(new UndoableEditListener() {
						public void undoableEditHappened(UndoableEditEvent ue) {
							undoMan[tabPane.getSelectedIndex()].addEdit(ue.getEdit());
							undoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canUndo());
							redoButton.setEnabled(undoMan[tabPane.getSelectedIndex()].canRedo());
						}
					});
					String str = FileHandler.fileNames[tabPane.getSelectedIndex()];
					if(str!=null) {
						int i=str.length()-1;
						rev="";
						while(str.charAt(i)!='.') {
							rev += str.charAt(i);
							i--;
						}
						Handler.setStat(mode, rev);
					}
					else
						Handler.setStat(mode, "Unknown");
				}
			}
		});
		readOnly.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if(ie.getStateChange()==ItemEvent.SELECTED) {
					mode = "Read Only";
					ta[tabPane.getSelectedIndex()].setEditable(false);
					Handler.setStat(mode, rev);
				}
				else {
					mode = "Writable";
					ta[tabPane.getSelectedIndex()].setEditable(true);
					Handler.setStat(mode, rev);
				}
			}
		});
		fontDialog.addActionListener(new Handler());
		incFont.addActionListener(new Handler());
		decFont.addActionListener(new Handler());
	}
	public void accelsAdd() {
		newFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		saveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));
		close.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK));
		closeAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_DOWN_MASK));
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
		undoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
		redoButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
		incFont.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));
		decFont.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK+InputEvent.SHIFT_DOWN_MASK));
	}
	public void menuAdditions() {
		file.add(newFile);
		file.add(open);
		file.add(save);
		file.add(saveAs);
		file.addSeparator();
		file.add(close);
		file.add(closeAll);
		file.addSeparator();
		file.add(exit);
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		edit.addSeparator();
		edit.add(undoButton);
		edit.add(redoButton);
		extra.add(readOnly);
		appearance.add(fontDialog);
		appearance.add(incFont);
		appearance.add(decFont);
		preference.add(appearance);
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(extra);
		menuBar.add(preference);
	}
}
