/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SingleClickAutoSelectCellEditor extends DefaultCellEditor 
{
	public SingleClickAutoSelectCellEditor(final JTextField textField) 
	{
		super(textField);
		setClickCountToStart(1);
		delegate = new EditorDeletegate();
	}
	
	final class EditorDeletegate extends EditorDelegate
	{
		public void setValue(Object value) 
		{
			if (value == null)
				return;
			
			((JTextField)editorComponent).setText(value.toString());
		}

		public Object getCellEditorValue() 
		{
			return ((JTextField)editorComponent).getText();
		}

		public boolean isCellEditable(EventObject anEvent) 
		{
			boolean isEditable = super.isCellEditable(anEvent);
			
			if (anEvent == null)
				return isEditable;
			
			if(isEditable)
				SwingUtilities.invokeLater(new LaterRunner());
			
			return isEditable;
		}
	}

	final class LaterRunner implements Runnable
	{
		public void run() 
		{
			((JTextField)editorComponent).selectAll();
		}
	}
}