/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject.projectlist;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.layout.OneRowPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.wizard.noproject.FileSystemTreeNode;
import org.conservationmeasures.eam.wizard.noproject.NoProjectWizardStep;
import org.martus.swing.UiButton;

public class TreeBasedProjectList extends JPanel
{
	public TreeBasedProjectList(MainWindow mainWindow, NoProjectWizardStep handlerToUse) throws Exception
	{
		setLayout(new BorderLayout());
		actions = new Vector<ProjectListAction>();
		
		File home = EAM.getHomeDirectory();
		rootNode = new FileSystemTreeNode(home);
		model = new ProjectListTreeTableModel(rootNode);
		ProjectListTreeTable table = new ProjectListTreeTable(model, handlerToUse);

		actions.add(new ProjectListOpenAction(table));
		actions.add(new ProjectListRenameAction(table));
		actions.add(new ProjectListCopyToAction(table));
		actions.add(new ProjectListExportAction(table));
		actions.add(new ProjectListDeleteAction(table));
		
		OneRowPanel buttonBar = new OneRowPanel();
		for(Action action : actions)
		{
			buttonBar.add(new UiButton(action));		
		}
		
		add(new FastScrollPane(table), BorderLayout.CENTER);
		add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		
		table.getSelectionModel().addListSelectionListener(new ActionUpdater());
	}

	public void refresh()
	{
		try
		{
			model.rebuildEntireTree();
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
		validate();
		repaint();
	}
	
	class ActionUpdater implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			for(ProjectListAction action : actions)
			{
				action.updateEnabledState();
			}
		}
	}

	private ProjectListTreeTableModel model;
	private FileSystemTreeNode rootNode;
	private Vector<ProjectListAction> actions;
}
