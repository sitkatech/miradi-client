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

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiComboBox;
import org.martus.swing.UiTable;

public class BudgetTable extends UiTable
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
		
		//FIXME budget code - remove comments, resources should be in model
		//model = getModel();
	    //model.addColumn(COLUMN_HEADER_TITLE, new String[]{""});

		//projectResources = project.getAllProjectResources();
		//String[] resourceNames = new String[projectResources.length];
		//resourceCombo = new UiComboBox();
		//for (int i = 0; i < projectResources.length; i++)
		//	resourceNames[i] = projectResources[i].getData(ProjectResource.TAG_NAME);
		
		 //int vColIndex = 0;
		 //TableColumn col = getColumnModel().getColumn(vColIndex);
		 //col.setCellEditor(new ComboBoxEditor(resourceNames));
		 //col.setCellRenderer(new ComboBoxRenderer(resourceNames));
	}
	
	Project project;
	UiComboBox resourceCombo;
	ProjectResource[] projectResources;
	BudgetTableModel model;
	
	static final String COLUMN_HEADER_TITLE = EAM.text("Resource Names");
}

class ComboBoxRenderer extends JComboBox implements TableCellRenderer 
{
    public ComboBoxRenderer(String[] items) 
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
    public ComboBoxEditor(String[] items) 
    {
        super(new JComboBox(items));
    }
}