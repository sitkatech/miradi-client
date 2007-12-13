/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTextArea;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextArea;

public class ObjectMultilineInputField extends ObjectTextInputField
{
	public ObjectMultilineInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 3, columnsToUse);
	}
	
	public ObjectMultilineInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int initialVisibleRows, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, new PanelTextArea(initialVisibleRows, columnsToUse));
		((UiTextArea)getComponent()).setWrapStyleWord(true);
		((UiTextArea)getComponent()).setLineWrap(true);
	}
}
