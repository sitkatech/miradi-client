/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
