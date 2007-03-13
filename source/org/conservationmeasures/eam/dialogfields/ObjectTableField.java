/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

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

public class ObjectTableField extends ObjectDataInputField
{
	public ObjectTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
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
		for (int i=1; i<items.length-1; ++i)
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
			for(int i = 0; i < items.length - 1; ++i)
			{
				String code = items[i].getCode();
				String value = (String) table.getModel().getValueAt(0, i);
				data.add(code, (value==null)?"":value);
			}
			//System.out.println("HERE getText:" + data.get());
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
			//System.out.println("HERE setText:" + dataString);
			StringMapData data = new StringMapData(dataString);
			ChoiceItem[] items = question.getChoices();
			for(int i = 1; i < items.length - 1; ++i)
			{
				String code = items[i].getCode();
				table.getModel().setValueAt(data.get(code), 0, i - 1);
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	public void updateEditableState()
	{

	}

	public void saveSelection()
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	class TableChangeHandler implements TableModelListener
	{
		public void tableChanged(TableModelEvent event)
		{
			saveSelection();
		}
	}
	ChoiceQuestion question;
	JTable table;
}
