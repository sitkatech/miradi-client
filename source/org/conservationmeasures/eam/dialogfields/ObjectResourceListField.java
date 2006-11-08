/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.text.ParseException;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class ObjectResourceListField extends ObjectDataInputField
{
	public ObjectResourceListField(Actions actionsToUse, Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		project = projectToUse;
		actions = actionsToUse;
		idList = new IdList();
		listComponent = new ResourceListEditorComponent(project, actions, idList);
	}
	
	public JComponent getComponent()
	{
		return listComponent;
	}

	public String getText()
	{
		return idListString;
	}

	public void setText(String idListToUse)
	{
		idListString = idListToUse;
		rebuildComponent();
	}

	private void rebuildComponent()
	{
		try
		{
			idList = new IdList(idListString);
			listComponent.setList(idList);
		}
		catch(ParseException e)
		{
			EAM.logException(e);
		}
	}

	private IdList idList;
	private Actions actions;
	private Project project;
	private String idListString = "";
	private ResourceListEditorComponent listComponent;
}
