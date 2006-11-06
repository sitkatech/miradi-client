/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import javax.swing.JTextField;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextArea;
import org.martus.swing.UiTextField;


public class ObjectStringInputField extends ObjectTextInputField
{
	public ObjectStringInputField(Project projectToUse, int objectType, BaseId objectId, String tag)
	{
		super(projectToUse, objectType, objectId, tag, new UiTextField());
		UiTextArea textArea = new UiTextArea(1,50);
		JTextField jTextArea = (JTextField)getComponent();
		jTextArea.setBorder(textArea.getBorder());
		jTextArea.setFont(textArea.getFont());
		jTextArea.setPreferredSize(textArea.getPreferredSize());
	}
}
