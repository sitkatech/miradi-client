/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogfields;


import java.awt.Color;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

import org.miradi.actions.Actions;
import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ObjectTextInputField extends ObjectDataInputField
{
	public ObjectTextInputField(Project projectToUse, int objectType, BaseId objectId, String tag, PanelTextArea componentToUse)
	{
		super(projectToUse, objectType, objectId, tag);
		field = componentToUse;
		addFocusListener();
		setEditable(true);
		field.getDocument().addDocumentListener(new DocumentEventHandler());
		new TextAreaRightClickMouseHandler(getActions(), field);
		field.addKeyListener(new KeyHandler(getActions()));
		
		setDefaultFieldBorder();
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
		setTextWithoutScrollingToMakeFieldVisible(newValue);
		clearNeedsSave();
	}
	
	private void setTextWithoutScrollingToMakeFieldVisible(String newValue)
	{
		DefaultCaret caret = (DefaultCaret)field.getCaret();
		caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		field.setText(newValue);
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
		super.focusGained(e);
		field.setSelectionStart(0);
		field.setSelectionEnd(field.getSize().width);
	}
	
	private Actions getActions()
	{
		return EAM.getMainWindow().getActions();
	}

	JTextComponent field;
}
