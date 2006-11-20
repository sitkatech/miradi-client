/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import java.awt.Dimension;

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
		JTextField jTextField = (JTextField)getComponent();
		jTextField.setBorder(textArea.getBorder());
		jTextField.setFont(textArea.getFont());
		
		int preferredHeight = jTextField.getPreferredSize().height;
		int preferredWidth = textArea.getPreferredSize().width;
		Dimension preferredSize = new Dimension(preferredWidth, preferredHeight);
		jTextField.setPreferredSize(preferredSize);
	}
}
