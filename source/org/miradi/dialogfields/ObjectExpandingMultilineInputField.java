/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.martus.swing.UiTextArea;
import org.miradi.ids.BaseId;
import org.miradi.project.Project;

public class ObjectExpandingMultilineInputField extends ObjectMultilineInputField
{
	public ObjectExpandingMultilineInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 1, columnsToUse);
	}

	@Override
	UiTextArea getTextComponent()
	{
		return (UiTextArea)getComponent();
	}

}
