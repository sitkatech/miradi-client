package org.conservationmeasures.eam.dialogs;

import java.awt.Container;

import javax.swing.JDialog;

import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;

public class ProjectScopePropertiesDialog extends JDialog
{
	public ProjectScopePropertiesDialog(MainWindow parent, Project project, EAMGraphCell scope)
	{
		super(parent, EAM.text("Title|Project Scope Properties"));
		mainWindow = parent;

		Container contents = getContentPane();
		contents.add(new ProjectScopePanel(mainWindow));
		pack();
		setResizable(true);
	}	

	MainWindow mainWindow;
}
