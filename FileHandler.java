import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import javax.swing.undo.*;
public class FileHandler implements ActionListener{
	Scanner input;
	Formatter x;
	int temp;
	JFileChooser j = new JFileChooser();
	static String filePaths[] = new String[10];
	static String fileNames[] = new String[10];
	static int flag[] = new int[10];
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand()=="New") {
			if(GUI.taCount<10) {
				temp = GUI.taCount;
				GUI.taCount++;
				GUI.ta[temp] = new JTextArea();
				GUI.jsp[temp] = new JScrollPane(GUI.ta[temp]);
				GUI.tabPane.add("New Tab", GUI.jsp[temp]);
				flag[temp] = 0;
			}
		}
		
		if(e.getActionCommand()=="Open") {
			if(GUI.taCount<10) {
				int opt = j.showOpenDialog(GUI.frame);
				if(opt == JFileChooser.CANCEL_OPTION) {
					
				}
				else {
					String fileName = j.getSelectedFile().getAbsolutePath();
					String fName = j.getSelectedFile().getName();
					File file = new File(fileName);
					if(file.exists()) {
						temp=GUI.taCount;
						GUI.taCount++;
						GUI.ta[temp] = new JTextArea();
						GUI.jsp[temp] = new JScrollPane(GUI.ta[temp]);
						try{
				            input = new Scanner(new File(fileName));
				        }
				        catch(Exception exc){
				        	JOptionPane.showMessageDialog(null, "There was an error reading the file", "Error!!", JOptionPane.ERROR_MESSAGE);
				        }
						while(input.hasNext())
				            GUI.ta[temp].append(input.nextLine()+"\n");
				        input.close();
						GUI.tabPane.addTab(fName, GUI.jsp[temp]);
						flag[temp] = 1;
						filePaths[temp] = fileName;
						fileNames[temp] = fName;
					}
				}
			}
		}
		
		else if(e.getActionCommand()=="Save") {
			temp = GUI.tabPane.getSelectedIndex();
			fileSave(temp);
		}
		
		else if(e.getActionCommand()=="Save As") {
			if(GUI.taCount!=0) {
				int opt = j.showSaveDialog(GUI.frame);
				if(opt == JFileChooser.CANCEL_OPTION) {
					
				}
				else {
					String fileName = j.getSelectedFile().getAbsolutePath();
					String fName = j.getSelectedFile().getName();
					temp = GUI.tabPane.getSelectedIndex();
					try {
						x = new Formatter(fileName);
					}
					catch(Exception exc) {
						JOptionPane.showMessageDialog(null, "There was an error saving the file", "Error!!", JOptionPane.ERROR_MESSAGE);
					}
					x.format(GUI.ta[temp].getText());
					x.close();
					if(flag[temp] == 0) {
						GUI.tabPane.setTitleAt(temp, fName);
						filePaths[temp] = fileName;
						fileNames[temp] = fName;
					}
					if(flag[temp]==1) {
						int option = JOptionPane.showConfirmDialog(GUI.frame, "DO you wish to load the newly saved File?", "Load new File?", JOptionPane.YES_NO_OPTION);
						if(option==JOptionPane.YES_OPTION) {
							GUI.ta[temp].setText("");
							GUI.tabPane.setTitleAt(temp, fName);
							try{
					            input = new Scanner(new File(fileName));
					        }
					        catch(Exception exc){
					        	JOptionPane.showMessageDialog(null, "There was an error reading the file", "Error!!", JOptionPane.ERROR_MESSAGE);
					        }
							while(input.hasNext())
					            GUI.ta[temp].append(input.nextLine()+"\n");
					        input.close();
							filePaths[temp] = fileName;
							fileNames[temp] = fName;
						}
					}
					flag[temp] = 1;
				}
			}
		}
		
		else if(e.getActionCommand()=="Close Tab") {
			if(GUI.taCount>0) {
				int option = JOptionPane.showConfirmDialog(GUI.frame, "Save File before closing?", "Save File?", JOptionPane.YES_NO_CANCEL_OPTION);
				if(option == JOptionPane.YES_OPTION) {
					temp = GUI.tabPane.getSelectedIndex();
					fileSave(temp);
					for(int j=temp;j<GUI.taCount-1;j++) {
						GUI.undoMan[j] = GUI.undoMan[j+1];
						GUI.ta[j] = GUI.ta[j+1];
						GUI.ta[j].setText(GUI.ta[j+1].getText());
						GUI.jsp[j] = GUI.jsp[j+1];
						flag[j] = flag[j+1];
						filePaths[j] = filePaths[j+1];
						fileNames[j] = fileNames[j];
					}
					GUI.tabPane.remove(temp);
					GUI.taCount=GUI.taCount-1;
				}
				else if(option == JOptionPane.NO_OPTION) {
					temp = GUI.tabPane.getSelectedIndex();
					for(int j=temp;j<GUI.taCount-1;j++) {
						GUI.undoMan[j] = GUI.undoMan[j+1];
						GUI.ta[j] = GUI.ta[j+1];
						GUI.ta[j].setText(GUI.ta[j+1].getText());
						GUI.jsp[j] = GUI.jsp[j+1];
						flag[j] = flag[j+1];
						filePaths[j] = filePaths[j+1];
						fileNames[j] = fileNames[j];
					}
					GUI.tabPane.remove(temp);
					GUI.taCount=GUI.taCount-1;
				}
			}
		}
		
		else if(e.getActionCommand()=="Close all Tabs") {
			closeAllTabs();
		}
		
		else if(e.getActionCommand()=="Exit") {
			int option = JOptionPane.showConfirmDialog(GUI.frame, "Are you sure you want to exit?","EXIT?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(option == JOptionPane.YES_OPTION) {
				closeAllTabs();
				if(GUI.taCount==0) {
					System.exit(0);
				}
			}
		}
	}
	
	public void fileSave(int temp) {
		if(GUI.taCount!=0) {
			if(flag[temp]==0) {
				int opt = j.showSaveDialog(GUI.frame);
				if(opt == JFileChooser.CANCEL_OPTION) {
					
				}
				else{
					String fileName = j.getSelectedFile().getAbsolutePath();
					String fName = j.getSelectedFile().getName();
					try {
						x = new Formatter(fileName);
					}
					catch(Exception exc) {
						JOptionPane.showMessageDialog(null, "There was an error saving the file", "Error!!", JOptionPane.ERROR_MESSAGE);
					}
					x.format(GUI.ta[temp].getText());
					x.close();
					flag[temp] = 1;
					filePaths[temp] = fileName;
					fileNames[temp] = fName;
					GUI.tabPane.setTitleAt(temp, fName);
				}
			}
			else {
				try {
					x = new Formatter(filePaths[temp]);
				}
				catch(Exception exc) {
					JOptionPane.showMessageDialog(null, "There was an error saving the file", "Error!!", JOptionPane.ERROR_MESSAGE);
				}
				x.format(GUI.ta[temp].getText());
				x.close();
			}
		}
	}
	public void closeAllTabs() {
		temp = GUI.taCount;
		if(temp>0) {
			int option = JOptionPane.showConfirmDialog(GUI.frame, "Save All Files before closing?\nIf NO, no file will be saved", "Save Files?", JOptionPane.YES_NO_CANCEL_OPTION);
			if(option == JOptionPane.YES_OPTION) {
				for(int i=temp-1;i>=0;i--) {
					fileSave(i);
					GUI.undoMan[i] = new UndoManager();
					GUI.undoMan[i].addEdit(null);
					GUI.ta[i] = new JTextArea();
					GUI.jsp[i] = new JScrollPane();
					flag[i] = 0;
					filePaths[i] = null;
					fileNames[i] = null;
					GUI.tabPane.remove(i);
					GUI.taCount=GUI.taCount-1;
				}
			}
			else if(option == JOptionPane.NO_OPTION) {
				for(int i=temp-1;i>=0;i--) {
					GUI.undoMan[i] = new UndoManager();
					GUI.undoMan[i].addEdit(null);
					GUI.ta[i] = new JTextArea();
					GUI.jsp[i] = new JScrollPane();
					flag[i] = 0;
					filePaths[i] = null;
					fileNames[i] = null;
					GUI.tabPane.remove(i);
					GUI.taCount=GUI.taCount-1;
				}
			}
		}
	}
}
