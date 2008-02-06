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

public abstract class ObjectMultilineInputField extends ObjectTextInputField
{
	protected ObjectMultilineInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int initialVisibleRows, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, createTextComponent(initialVisibleRows, columnsToUse));
		UiTextArea textComponent = getTextComponent();
		textComponent.setWrapStyleWord(true);
		textComponent.setLineWrap(true);
	}

	private static PanelTextArea createTextComponent(int initialVisibleRows, int columnsToUse)
	{
		return new PanelTextArea(initialVisibleRows, columnsToUse);
	}

	abstract UiTextArea getTextComponent();
}
