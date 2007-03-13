/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.StringMapData;
import org.martus.swing.UiTable;

public class ObjectStringMapTableField extends ObjectDataInputField
{
	public ObjectStringMapTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse.getTag());
		question = questionToUse;
		String[][] data = new String[][]{{}};
		String[] names = getColumnNames(questionToUse);
		table = new UiTable(new DefaultTableModel(data,names));
		addFocusListener();
		table.setRowHeight(20);
	    table.getModel().addTableModelListener(new TableChangeHandler());
	}

	private String[] getColumnNames(ChoiceQuestion questionToUse)
	{
		ChoiceItem[] items = questionToUse.getChoices();
		String[] names = new String[items.length-1];
		for (int i=0; i<items.length-1; ++i)
		{
			names[i] = items[i].getLabel();
		}
		return names;
	}

	public JComponent getComponent()
	{
		return table;
	}

	public String getText()
	{
		try
		{
			StringMapData data = new StringMapData();
			ChoiceItem[] items = question.getChoices();
			for(int i=0; i < items.length - 1; ++i)
			{
				String code = items[i+1].getCode();
				String value = (String) table.getModel().getValueAt(0, i);
				data.add(code, (value==null)?"":value);
			}
			return data.get();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public void setText(String dataString)
	{
		try
		{
			StringMapData data = new StringMapData(dataString);
			ChoiceItem[] items = question.getChoices();
			for(int i=0; i < items.length - 1; ++i)
			{
				String code = items[i+1].getCode();
				table.getModel().setValueAt(data.get(code), 0, i);
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	public void updateEditableState()
	{
		boolean editable = allowEdits() && isValidObject();
		table.setEnabled(editable);
		Color fg = EAM.EDITABLE_FOREGROUND_COLOR;
		Color bg = EAM.EDITABLE_BACKGROUND_COLOR;
		if(!editable)
		{
			fg = EAM.READONLY_FOREGROUND_COLOR;
			bg = EAM.READONLY_BACKGROUND_COLOR;
		}
		table.setForeground(fg);
		table.setBackground(bg);
	}

	public void saveSelection()
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	class TableChangeHandler implements TableModelListener
	{
		//FIXME: should be a better way to stop loop....maybe do save only in fouse change?
		boolean skip = true;
		public void tableChanged(TableModelEvent arg0)
		{
			if (skip) 
			{
				skip = false;
				saveSelection();
				skip = true;
			}
		}
	}
	ChoiceQuestion question;
	JTable table;
}
