/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextArea;

public class ObjectTextInputField extends ObjectDataInputField
{
	public ObjectTextInputField(Project projectToUse, int objectType, BaseId objectId, String tag, JTextComponent componentToUse)
	{
		super(projectToUse, objectType, objectId, tag);
		field = componentToUse;
		addFocusListener();
		setEditable(true);
		field.getDocument().addDocumentListener(new DocumentEventHandler());
	}

	public JComponent getComponent()
	{
		return field;
	}

	public String getText()
	{
		return field.getText();
	}

	public void setText(String newValue)
	{
		field.setText(newValue);
		clearNeedsSave();
	}
	
	public void updateEditableState()
	{
		boolean editable = allowEdits() && isValidObject();
		field.setEditable(editable);
		Color fg = EAM.EDITABLE_FOREGROUND_COLOR;
		Color bg = EAM.EDITABLE_BACKGROUND_COLOR;
		if(!editable)
		{
			fg = EAM.READONLY_FOREGROUND_COLOR;
			bg = EAM.READONLY_BACKGROUND_COLOR;
		}
		field.setForeground(fg);
		field.setBackground(bg);
	}

	public void focusGained(FocusEvent e)
	{
		field.setSelectionStart(0);
		field.setSelectionEnd(field.getSize().width);
	}
	
	class DocumentEventHandler implements DocumentListener
	{
		public void changedUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}

		public void insertUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}

		public void removeUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}
	}
	
	public void setupFixedSizeTextField(int row, int column)
	{
		JTextComponent textComponent = (JTextComponent)getComponent();
		UiTextArea textArea = new UiTextArea(row,column);
		textComponent.setBorder(textArea.getBorder());
		textComponent.setFont(textArea.getFont());
		int preferredHeight = textComponent.getPreferredSize().height;
		int preferredWidth = textArea.getPreferredSize().width;
		Dimension preferredSize = new Dimension(preferredWidth, preferredHeight);
		textComponent.setPreferredSize(preferredSize);
	}
	
	JTextComponent field;
}
