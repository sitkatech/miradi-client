/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTextField;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.SingleClickAutoSelectCellEditor;
import org.conservationmeasures.eam.utils.StringMapData;
import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;
import org.martus.swing.UiScrollPane;

public class ObjectStringMapTableField extends ObjectDataInputField
{
	public ObjectStringMapTableField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse.getTag());
		question = questionToUse;
		String[][] data = new String[][]{{}};
		String[] names = getColumnNames(questionToUse);
		table = new MyTable(new DefaultTableModel(data,names));
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
			names[i] = items[i+1].getLabel();
		}
		return names;
	}

	public JComponent getComponent()
	{
		UiScrollPane pane = new UiScrollPane(table);
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		pane.setPreferredSize(new Dimension(pane.getPreferredSize().width, table.getPreferredSize().height));
		return pane;
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
				data.add(code, value);
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
			avoidSaveDuringInternalSet = true;
			table.editingCanceled(null);
			table.clearSelection();
			StringMapData data = new StringMapData(dataString);
			ChoiceItem[] items = question.getChoices();
			for(int i=0; i < items.length - 1; ++i)
			{
				String code = items[i+1].getCode();
				String value = data.get(code);
				table.getModel().setValueAt(value, 0, i);
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
		finally
		{
			avoidSaveDuringInternalSet = false;
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
	
	
	class MyTable extends TableWithColumnWidthSaver
	{
		public MyTable(DefaultTableModel model)
		{
			super(model);
			setSingleCellEditor();
		}
		
		public String getUniqueTableIdentifier()
		{
			return UNIQUE_IDENTIFIER;
		}

		public Component prepareEditor(TableCellEditor editor, int row, int column)
		{
			Component c = super.prepareEditor(editor, row, column);
			if (c instanceof JTextComponent)
			{
				if (row==0)
				{
					((JTextField)c).selectAll();
				}
			}

			return c;
		}

		public boolean isCellEditable(int row, int column)
		{
			return (row==0);
		}
		
		private void setSingleCellEditor()
		{
			int colCount = question.getChoices().length-1;
			for (int i = 0; i < colCount; i++)
			{
				TableColumn column = getColumnModel().getColumn(i);
				column.setCellEditor(new SingleClickAutoSelectCellEditor(new PanelTextField()));
			}
		}
		
	}
	
	class TableChangeHandler implements TableModelListener
	{

		public void tableChanged(TableModelEvent event)
		{
			if (!avoidSaveDuringInternalSet) 
			{
				//TODO: shold not be needed if setIconRowObject sets avoidSaveDuringInternalSet
				if (event.getFirstRow()==0)
				{
					saveSelection();
				}
			}
		}

	}
	private boolean avoidSaveDuringInternalSet;
	private ChoiceQuestion question;
	protected JTable table;
	
	public static final String UNIQUE_IDENTIFIER = "ObjectStringMapTableField";
}
