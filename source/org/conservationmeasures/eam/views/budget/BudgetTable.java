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
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiComboBox;

public class BudgetTable extends JTable implements ObjectPicker 
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
	
	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}

	public EAMObject[] getSelectedObjects()
	{
		int selectedRow = getSelectedRow();
		if (selectedRow < 0)
			return new EAMObject[0];
		BaseId selectedId = getBudgetModel().getSelectedAssignment(selectedRow);
		EAMObject selectedObject = project.findObject(ObjectType.ASSIGNMENT, selectedId);
		
		if (selectedObject == null)
			return new EAMObject[0];
		
		return new EAMObject[] {selectedObject};
	}

	private BudgetTableModel getBudgetModel()
	{
		return (BudgetTableModel)getModel();
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