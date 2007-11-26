/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelComboBox;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

abstract public class EditableObjectTable extends TableWithColumnWidthSaver
{
	public EditableObjectTable(TableModel model)
	{
		super(model);
	}
	
	protected int getColumnWidth(int column)
	{
		return getColumnHeaderWidth(column);
	}	
		
	protected void createComboColumn(BaseObject[] content, int col, BaseObject invalidObject)
	{
		Arrays.sort(content, new SorterByToString());
		BaseObject[] comboContent = addEmptySpaceAtStart(content, invalidObject);
		PanelComboBox comboBox = new PanelComboBox(comboContent);
		TableColumn tableColumn = getColumnModel().getColumn(col);
		tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
		tableColumn.setCellRenderer(new ComboBoxRenderer(comboContent));
	}
	
	protected BaseObject[] addEmptySpaceAtStart(BaseObject[] content, BaseObject invalidObject)
	{
		final int EMPTY_SPACE = 0;
		BaseObject[]  comboContent = new BaseObject[content.length + 1];
		comboContent[EMPTY_SPACE] = invalidObject;

		try
		{
			invalidObject.setLabel(" ");
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	
		System.arraycopy(content, 0, comboContent, 1, content.length);	
		return comboContent;
	}
		
	public class SorterByToString implements Comparator<BaseObject>
	{
		public int compare(BaseObject o1, BaseObject o2)
		{
			return o1.toString().compareToIgnoreCase(o2.toString());
		}	
	}
	
	protected class ComboBoxRenderer extends JComboBox implements TableCellRenderer 
	{
	    public ComboBoxRenderer(Object[] items) 
	    {
	        super(items);
	    }

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) 
	    {
	        if (isSelected) 
	        	setColors(table.getSelectionBackground(), table.getSelectionForeground());
	        else 
	        	setColors(table.getBackground(), table.getForeground());

	        setSelectedItem(value);
	        return this;
	    }
	    
	    private void setColors(Color background, Color foreground)
	    {
	        setForeground(foreground);
	        setBackground(background);
	    }
	}
}

