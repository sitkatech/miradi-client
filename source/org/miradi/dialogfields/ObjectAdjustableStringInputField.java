/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.miradi.ids.BaseId;
import org.miradi.project.Project;

public class ObjectAdjustableStringInputField extends ObjectStringInputField
{
	public ObjectAdjustableStringInputField(Project projectToUse, int objectType, BaseId objectId, String tag, int columnsToUse)
	{
		super(projectToUse, objectType, objectId, tag, columnsToUse);
		DocumentEventHandler handler = new DocumentEventHandler();
		((JTextComponent)getComponent()).getDocument().addUndoableEditListener(handler);
		columns = columnsToUse;
	}


	class DocumentEventHandler implements  UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent e)
		{
			Document document = (Document)e.getSource();
			if (document.getLength()<=columns)
				return;
			e.getEdit().undo();
			// FIXME: Avoid beeping when loading legacy projects
			// Also similar code in ObjectStringInputField, 
			// ObjectAdjustableStringInputField, and 
			// UiTextFieldWithLengthLimit
			//Toolkit.getDefaultToolkit().beep();
		}
	}
	
	int columns;
}
