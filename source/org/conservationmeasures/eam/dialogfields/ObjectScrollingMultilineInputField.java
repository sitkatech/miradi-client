/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MiradiScrollPane;
import org.martus.swing.UiTextArea;

public class ObjectScrollingMultilineInputField extends ObjectMultilineInputField
{
	public ObjectScrollingMultilineInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 4, columnsToUse);
	}

	public JComponent getComponent()
	{
		if(scrollPane == null)
			scrollPane = new MiradiScrollPane(getTextComponent());
		return scrollPane;
	}

	@Override
	UiTextArea getTextComponent()
	{
		return (UiTextArea)super.getComponent();
	}
	
	@Override
	void addFocusListener()
	{
		super.addFocusListener();
		getTextComponent().addFocusListener(this);
	}

	private MiradiScrollPane scrollPane;

}
