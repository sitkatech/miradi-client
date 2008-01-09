/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JPanel;

import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.wizard.noproject.FileSystemTreeNode;
import org.conservationmeasures.eam.wizard.noproject.NoProjectWizardStep;

public class TreeBasedProjectList extends JPanel
{
	public TreeBasedProjectList(MainWindow mainWindow, NoProjectWizardStep handlerToUse) throws Exception
	{
		setLayout(new BorderLayout());
		File home = EAM.getHomeDirectory();
		rootNode = new FileSystemTreeNode(home);
		ProjectListTreeTableModel model = new ProjectListTreeTableModel(rootNode);
		ProjectListTreeTable table = new ProjectListTreeTable(model, handlerToUse);
		OneRowPanel buttonBar = new OneRowPanel();
		
		add(table, BorderLayout.CENTER);
		add(buttonBar, BorderLayout.AFTER_LAST_LINE);
	}

	public void refresh()
	{
		try
		{
			rootNode.rebuild();
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
		repaint();
	}

	private FileSystemTreeNode rootNode;
}
