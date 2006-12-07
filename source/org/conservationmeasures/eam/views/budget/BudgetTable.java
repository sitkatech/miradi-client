/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiComboBox;

public class BudgetTable extends JTable
{
	public BudgetTable(Project projectToUse, BudgetTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
		project = projectToUse;
		
		rebuild();
	}
	
	public void setTask(BaseId taskId)
	{
		//TODO budget code - remove commented code
		//Task selectedTask = (Task)project.findObject(ObjectType.TASK, taskId);
		//System.out.println("selected task  = "+selectedTask.getLabel());
		//rebuild();
	}
	
	private void rebuild()
	{
		if (model.getColumnCount() <= 0)
			return;
		
		
		projectResources = project.getAllProjectResources();
		JComboBox combo = new JComboBox(projectResources);
		
		TableColumn col = getColumnModel().getColumn(RESOURCE_COLUMN);
		col.setCellEditor(new DefaultCellEditor(combo));
		col.setCellRenderer(new ComboBoxRenderer(projectResources));
	}
	
	public static final int RESOURCE_COLUMN = 0;
	Project project;
	UiComboBox resourceCombo;
	ProjectResource[] projectResources;
	BudgetTableModel model;
	
	static final String COLUMN_HEADER_TITLE = EAM.text("Resource Names");
}

class ComboBoxRenderer extends JComboBox implements TableCellRenderer 
{
    public ComboBoxRenderer(Object[] items) 
    {
        super(items);
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) 
    {

        if (isSelected) 
        {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } 
        else 
        {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }

        setSelectedItem(value);
        return this;
    }
}

class ComboBoxEditor extends DefaultCellEditor 
{
    public ComboBoxEditor(Object[] items) 
    {
        super(new JComboBox(items));
    }
}