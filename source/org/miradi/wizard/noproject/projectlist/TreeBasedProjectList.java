/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.wizard.noproject.projectlist;

import java.awt.BorderLayout;
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
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.wizard.noproject.NoProjectWizardStep;

public class TreeBasedProjectList extends JPanel
{
	public TreeBasedProjectList(MainWindow mainWindow, NoProjectWizardStep handlerToUse) throws Exception
	{
		setLayout(new BorderLayout());
		actions = new Vector<ProjectListAction>();

		model = ProjectListTreeTableModel.createProjectListTreeTableModel();
		ProjectListTreeTable table = new ProjectListTreeTable(mainWindow, model, handlerToUse);

		actions.add(new ProjectListOpenAction(table));
		actions.add(new ProjectListRenameAction(table));
		actions.add(new ProjectListDeleteAction(table));
		actions.add(new ProjectListSaveAsAction(table));
		actions.add(new ProjectListExportAction(table));
		actions.add(new ProjectListCreateDirectory(table));
		actions.add(new ProjectListMoveToDirectory(table));
		
		OneRowPanel buttonBar = new OneRowPanel();
		buttonBar.setBackground(AppPreferences.getWizardBackgroundColor());
		buttonBar.setGaps(3);
		for(Action action : actions)
		{
			buttonBar.add(new UiButton(action));		
		}
		
		String instructions = EAM.text("<div class='WizardText'>To <strong>continue work</strong> on an existing project, or <strong>browse an example</strong>, choose a project below:");
		OneRowPanel instructionsBar = new OneRowPanel();
		instructionsBar.add(new FlexibleWidthHtmlViewer(mainWindow, instructions));
		add(instructionsBar, BorderLayout.BEFORE_FIRST_LINE);
		add(new MiradiScrollPane(table), BorderLayout.CENTER);
		add(buttonBar, BorderLayout.AFTER_LAST_LINE);
		
		table.getSelectionModel().addListSelectionListener(new ActionUpdater());
	}

	public void refresh()
	{
		try
		{
			model.rebuildEntireTree(EAM.getHomeDirectory());
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
	private Vector<ProjectListAction> actions;
}
