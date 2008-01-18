/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTextArea;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextArea;

public class ObjectMultilineDisplayField extends ObjectTextInputField
{
	public ObjectMultilineDisplayField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 50);
	}
	
	public ObjectMultilineDisplayField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnCount)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, new PanelTextArea(1, columnCount));
		((UiTextArea)getComponent()).setWrapStyleWord(true);
		((UiTextArea)getComponent()).setLineWrap(true);
		setEditable(false);
		getComponent().setFocusable(false);
	}
}
