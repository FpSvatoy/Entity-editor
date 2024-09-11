package listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import gui.ListGUI;
import launch.Launcher;
import repository.Project;

public class OpenXMLFileButtonListener extends JFileChooser implements ActionListener
{
	public String directory = "res/";
	public String file = "objecttypes.xml";

	ListGUI listGUI;
	
	public OpenXMLFileButtonListener(ListGUI listGUI) {
		super(Project.getInstance().getXMLPath()
			+ Project.getInstance().getXMLFileName());// Create file chooser at the current project file directory
		this.listGUI = listGUI;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		this.setDialogTitle("Choose file");
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Xml File", "xml");
		this.setFileFilter(filter);
        int result = this.showOpenDialog(OpenXMLFileButtonListener.this);
        if (result == JFileChooser.APPROVE_OPTION )
        {
        	directory = this.getCurrentDirectory().toString()+File.separator;
        	file = this.getName(getSelectedFile()).toString();
        	Project.getInstance().load(directory, file);
            listGUI.updateList();
            
        	//Debug 
        	System.out.println(directory + file);
        }
	}
	
	
}
