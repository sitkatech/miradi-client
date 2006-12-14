/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class BudgetTable extends JTable implements ObjectPicker 
{
	public BudgetTable(Project projectToUse, BudgetTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
		project = projectToUse;
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
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
		System.out.println("Rebuild called");
		//TODO budget code - verify the need for this if
		if (model.getColumnCount() <= 0)
			return;
		
		ProjectResource[] projectResources = project.getAllProjectResources();
		int resourceColumn = model.getResourcesColumnIndex();
		createComboColumn(projectResources, resourceColumn);
		
		FundingSource[] fundingSources = project.getObjectManager().getFundingSourcePool().getAllFundingSources();
		int fundingSourceColumn = model.getFundingSourceColumnIndex();
		createComboColumn(fundingSources, fundingSourceColumn);
		
		AccountingCode[] accountingCodes = project.getObjectManager().getAccountingCodePool().getAllAccountingCodes();
		int accountingCodesColumn = model.getAccountingCodeColumnIndex();
		createComboColumn(accountingCodes, accountingCodesColumn);
	}

	private void createComboColumn(EAMBaseObject[] projectResources, int col)
	{
		JComboBox resourceCombo = new JComboBox(projectResources);
		
		TableColumn resourceCol = getColumnModel().getColumn(col);
		resourceCol.setCellEditor(new DefaultCellEditor(resourceCombo));
		resourceCol.setCellRenderer(new ComboBoxRenderer(projectResources));
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
		
		BudgetTableModel budgetModel = getBudgetModel();
		if (budgetModel.isOdd(selectedRow))
			return new EAMObject[0];
		
		if (budgetModel.isTotalsRow(selectedRow))
			return new EAMObject[0];
		
		selectedRow = budgetModel.getCorrectedRow(selectedRow);
		
		//FIXME budget code - remove commented code
		
		//ProjectResource resource = (ProjectResource)budgetModel.getValueAt(selectedRow, budgetModel.getResourcesColumnIndex());
		//BaseId selectedId = resource.getId();

		BaseId selectedId = budgetModel.getSelectedAssignment(selectedRow);
		EAMObject selectedObject = project.findObject(ObjectType.ASSIGNMENT, selectedId);
		
		if (selectedObject == null)
			return new EAMObject[0];
		
		return new EAMObject[] {selectedObject};
	}

	public BudgetTableModel getBudgetModel()
	{
		return (BudgetTableModel)getModel();
	}

	Project project;
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
            boolean isSelected, boolean hasFocus, int row, int col) 
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
        
        BudgetTableModel budgetModel = ((BudgetTable)table).getBudgetModel();
		if (! budgetModel.isCellEditable(row, col))
        	return new JLabel("");
        
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