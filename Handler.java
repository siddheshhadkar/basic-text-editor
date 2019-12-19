import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Handler implements ActionListener{
	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    final String[] family = ge.getAvailableFontFamilyNames();
    final String[] fs = {"12","14","16","18","20","22","24","26","28","30"};
    final int[] fsInt = {12,14,16,18,20,22,24,26,28,30};
    
    static int ffsi;
    static int fstylesi = Font.PLAIN;
    static int fssi;
    Font font = new Font(family[0],fstylesi, fsInt[0]);
	
	public static void setStat(String mode, String rev) {
		String ref = rev;
		if(rev.equals("txt"))
			ref = "Text";
		else if(rev.equals("avaj"))
			ref = "JAVA";
		else if(rev.equals("hs") || rev.equals("hsab") || rev.equals("hsz") || rev.equals("hsif"))
			ref = "Bourne Again Shell";
		else if(rev.equals("tab") || rev.equals("dmc"))
			ref = "Batch";
		else if(rev.equals("c") || rev.equals("h"))
			ref = "C";
		else if(rev.equals("sc") || rev.equals("xsc"))
			ref = "C#";
		else if(rev.equals("ssc"))
			ref = "Cascading Style Sheet";
		else if(rev.equals("lmth") || rev.equals("mth") || rev.equals("lmths") || rev.equals("lmthx"))
			ref = "Hyper Text Markup Language";
		else if(rev.equals("og"))
			ref = "GO";
		else if(rev.equals("nosj"))
			ref = "JSON";
		else if(rev.equals("psj"))
			ref = "Java Server Page";
		else if(rev.equals("sj"))
			ref = "JavaScript";
		else if(rev.equals("ekam"))
			ref = "Make";
		else if(rev.equals("php") || rev.equals("3php") || rev.equals("4php") || rev.equals("5php") || rev.equals("7php") || rev.equals("sphp") || rev.equals("tphp") || rev.equals("lmthp"))
			ref = "PHP";
		else if(rev.equals("yp"))
			ref = "Python";
		else if(rev.equals("r") || rev.equals("R") || rev.equals("s") || rev.equals("S"))
			ref = "R";
		else if(rev.equals("br"))
			ref = "Ruby";
		else if(rev.equals("lqs"))
			ref = "SQL";
		else if(rev.equals("alacs"))
			ref = "Scala";
		else if(rev.equals("lmx"))
			ref = "XML";
		GUI.statusbar.setText(mode+"     |  "+ref+" file  |");
	}
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==GUI.fontDialog) {
			JDialog dialog = new JDialog(GUI.frame,"Font",true);
			dialog.setSize(400,150);
		    JComboBox<String> font_size = new JComboBox<String>(fs);
		    JComboBox<String> font_family = new JComboBox<String>(family);
		    JButton button = new JButton("Apply");
		    font_family.addItemListener(new ItemListener(){
		    	public void itemStateChanged(ItemEvent e){
		    		if(e.getStateChange()==ItemEvent.SELECTED)
		    			ffsi = font_family.getSelectedIndex();
	                font = new Font(family[ffsi],fstylesi,fsInt[fssi]);
	            }
	        });
		    font_size.addItemListener(new ItemListener(){
		    	public void itemStateChanged(ItemEvent e){
			        if(e.getStateChange()==ItemEvent.SELECTED)
			        	fssi = font_size.getSelectedIndex();
			        font = new Font(family[ffsi],fstylesi,fsInt[fssi]);
		        }
		    });
		    button.addActionListener(new ActionListener() {
		    	public void actionPerformed(ActionEvent ae) {
		    		for(int i=0;i<10;i++) {
	                	GUI.ta[i].setFont(font);
	                }
		    	}
		    });
		    dialog.setLocationRelativeTo(null);
		    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		    dialog.setLayout(new FlowLayout());
		    dialog.setResizable(false);
		    dialog.add(font_family);
		    dialog.add(font_size);
		    dialog.add(button);
		    dialog.setVisible(true);
		}
		else if(ae.getSource()==GUI.incFont) {
			if(fssi<9) {
				fssi=fssi+1;
				for(int i=0;i<10;i++) {
					font = new Font(family[ffsi],fstylesi,fsInt[fssi]);
	            	GUI.ta[i].setFont(font);
	            }
			}
		}
		else if(ae.getSource()==GUI.decFont) {
			if(fssi>0) {
				fssi=fssi-1;
				for(int i=0;i<10;i++) {
					font = new Font(family[ffsi],fstylesi,fsInt[fssi]);
	            	GUI.ta[i].setFont(font);
	            }
			}
		}
	}
}
