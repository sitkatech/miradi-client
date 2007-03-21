/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Toolkit;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiTextField;

public class ObjectAdjustableStringInputField extends ObjectTextInputField
{
	public ObjectAdjustableStringInputField(Project projectToUse, int objectType, BaseId objectId, String tag, int columnsToUse)
	{
		super(projectToUse, objectType, objectId, tag,  new UiTextField());
		setupFixedSizeTextField(0,columnsToUse);
		DocumentEventHandler handler = new DocumentEventHandler();
		((JTextComponent)getComponent()).getDocument().addUndoableEditListener(handler);
		getComponent().setMaximumSize(getComponent().getPreferredSize());
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
			Toolkit.getDefaultToolkit().beep();
		}
	}
	
	int columns;
}
