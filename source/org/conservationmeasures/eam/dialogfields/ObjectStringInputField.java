/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextField;


public class ObjectStringInputField extends ObjectTextInputField
{
	public ObjectStringInputField(Project projectToUse, int objectType, BaseId objectId, String tag)
	{
		super(projectToUse, objectType, objectId, tag, new UiTextField());
		setupFixedSizeTextField(1,10);
	}
}
