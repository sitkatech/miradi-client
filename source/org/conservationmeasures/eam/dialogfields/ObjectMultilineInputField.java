/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextArea;

public class ObjectMultilineInputField extends ObjectTextInputField
{
	public ObjectMultilineInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, new UiTextArea(3, 50));
		((UiTextArea)getComponent()).setWrapStyleWord(true);
		((UiTextArea)getComponent()).setLineWrap(true);
	}
}
