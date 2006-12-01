/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.text.ParseException;

import javax.swing.JComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;

public class ObjectResourceCodeListField extends ObjectDataInputField
{
	public ObjectResourceCodeListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		project = projectToUse;
		listComponent = new ResourceCodeListEditorComponent(project);
		codeList = new CodeList();
	}
	
	public JComponent getComponent()
	{
		return listComponent;
	}

	public String getText()
	{
		return codeList.toString();
	}

	public void setText(String codeListToUse)
	{
		try
		{
			codeList =  new CodeList(codeListToUse);
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			codeList = new CodeList();
		}
		rebuildComponent();
	}

	private void rebuildComponent()
	{
		listComponent.setList(codeList);
	}

	private CodeList codeList;
	private Project project;
	private ResourceCodeListEditorComponent listComponent;
}
