/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.wizard.noproject.projectlist;

import java.awt.BorderLayout;
import java.io.File;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.martus.swing.UiButton;
import org.miradi.layout.OneRowPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.wizard.noproject.FileSystemTreeNode;
import org.miradi.wizard.noproject.NoProjectWizardStep;

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
		actions.add(new ProjectListDeleteAction(table));
		actions.add(new ProjectListCopyToAction(table));
		actions.add(new ProjectListExportAction(table));
		
		OneRowPanel buttonBar = new OneRowPanel();
		buttonBar.setBackground(AppPreferences.getWizardBackgroundColor());
		buttonBar.setGaps(3);
		for(Action action : actions)
		{
			buttonBar.add(new UiButton(action));		
		}
		
		add(new MiradiScrollPane(table), BorderLayout.CENTER);
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
