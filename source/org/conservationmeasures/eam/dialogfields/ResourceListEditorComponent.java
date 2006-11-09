/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionResourceListModify;
import org.conservationmeasures.eam.actions.ActionResourceListRemove;
import org.conservationmeasures.eam.actions.ActionViewPossibleResources;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

public class ResourceListEditorComponent extends JPanel
{
	public ResourceListEditorComponent(Project projectToUse, Actions actions)
	{
		super(new BorderLayout());
		project = projectToUse;

		resourceListModel = new ResourceListModel(project);
		resourceListTable = new ResourceListTable(resourceListModel);
		resourceListTable.resizeTable(4);
		rebuild();
		
		add(new UiScrollPane(resourceListTable), BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.AFTER_LINE_ENDS);
	}
	
	public void setList(IdList idList)
	{
		resourceListModel.setList(idList);
		rebuild();
	}
	
	public void rebuild()
	{
		resourceListModel.fireTableDataChanged();
	}
	
	Box createButtonBar(Actions actions)
	{
		Box box = Box.createVerticalBox();
		box.add(new UiButton(actions.get(ActionViewPossibleResources.class)));
		box.add(new ObjectsActionButton(actions.getObjectsAction(ActionResourceListRemove.class), resourceListTable));
		box.add(new ObjectsActionButton(actions.getObjectsAction(ActionResourceListModify.class), resourceListTable));
		return box;
	}
	
	Project project;
	ResourceListModel resourceListModel;
	ResourceListTable resourceListTable;
}