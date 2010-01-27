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

import java.awt.event.FocusEvent;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.martus.swing.UiTextArea;
import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ObjectStringInputField extends ObjectTextInputField
{
	public ObjectStringInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, new PanelTextArea(0, columnsToUse));		
	}
	
	public ObjectStringInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, int columnsToUse, Document document)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, new PanelTextArea(0, columnsToUse), document);
	}
	
	private ObjectStringInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, PanelTextArea componentToUse)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, componentToUse, componentToUse.getDocument());
	}

	private ObjectStringInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, PanelTextArea componentToUse, Document document)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, componentToUse, document);
		
		DocumentEventHandler handler = new DocumentEventHandler();
		((JTextComponent)getComponent()).getDocument().addUndoableEditListener(handler);
		((UiTextArea)getComponent()).setWrapStyleWord(true);
		((UiTextArea)getComponent()).setLineWrap(true);
	}

	class DocumentEventHandler implements  UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent e)
		{
			Document document = (Document)e.getSource();
			try
			{
				if (document.getLength()==0)
					return;
			
				String text = document.getText(0, document.getLength());
				int index = text.indexOf('\n');
				if (index>=0)
				{
					e.getEdit().undo();
				}
			}
			catch(BadLocationException e1)
			{
				EAM.logException(e1);
			}
		}
	}

	@Override
	public void setText(String newValue)
	{
		newValue.replaceAll("\n", " ");
		super.setText(newValue);
	}

	@Override
	public void focusGained(FocusEvent e)
	{
		super.focusGained(e);
		field.setSelectionStart(0);
		field.setSelectionEnd(field.getSize().width);
	}
	
}

