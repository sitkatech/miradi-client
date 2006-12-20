/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
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
	public BudgetTable(Project projectToUse, AbstractBudgetTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
		project = projectToUse;
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setDefaultRenderer(Object.class, new AlternatingThickBorderedTotalsColoredRenderer());
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
		
		setSingleCellEditor();
		
	}

	private void setSingleCellEditor()
	{
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++)
		{
			if (model.isUnitsColumn(i))
			{
				TableColumn singleClickCostCol = getColumnModel().getColumn(i);
				DefaultCellEditor singleClickEditor = new SingleClickAutoSelectCellEditor(new JTextField());
				singleClickCostCol.setCellEditor(singleClickEditor);
			}
		}
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
		
		AbstractBudgetTableModel budgetModel = getBudgetModel();
		if (budgetModel.isTotalsRow(selectedRow))
			return new EAMObject[0];
		
		selectedRow = budgetModel.getCorrectedRow(selectedRow);
		
		BaseId selectedId = budgetModel.getAssignmentForRow(selectedRow);
		EAMObject selectedObject = project.findObject(ObjectType.ASSIGNMENT, selectedId);
		
		if (selectedObject == null)
			return new EAMObject[0];
		
		return new EAMObject[] {selectedObject};
	}

	public AbstractBudgetTableModel  getBudgetModel()
	{
		return (AbstractBudgetTableModel)getModel();
	}

	Project project;
	AbstractBudgetTableModel model;
	
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
        	setColors(table.getSelectionBackground(), table.getSelectionForeground());
        else 
        	setColors(table.getBackground(), table.getForeground());
        
        AbstractBudgetTableModel budgetModel = ((BudgetTable)table).getBudgetModel();
		if (! budgetModel.isCellEditable(row, col))
        	return new JLabel("");
        
        setSelectedItem(value);
        return this;
    }
    
    private void setColors(Color background, Color foreground)
    {
        setForeground(foreground);
        setBackground(background);
    }
}

class SingleClickAutoSelectCellEditor extends DefaultCellEditor 
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

class AlternatingThickBorderedTotalsColoredRenderer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		AbstractBudgetTableModel model = (AbstractBudgetTableModel)table.getModel();
		if (!isSelected)
			setColors(table, model, component, row, column);
	
		setBorders(model, row, column);
		return component;
	}

	private void setColors(JTable table, AbstractBudgetTableModel model, Component component, int row, int column)
	{
		if (model.doubleRowed())
			setComponentColors(component, EVERY_OTHER_TWO_COLORS[row % 4]);
		
		if (! model.doubleRowed())
			setComponentColors(component, BACKGROUND_COLORS[row % 2]);
		
		Color totalsColor = new Color(0xf0, 0xf0, 0xf0).darker();
		if (model.isTotalsRow(row))
			setComponentColors(component, totalsColor);
		
		if (model.isYearlyTotalColumn(column))
			setComponentColors(component, totalsColor);
		
		if (model.isCostTotalsColumn(column))
			setComponentColors(component, totalsColor);
		
		if (model.isUnitsTotalColumn(column))
			setComponentColors(component, totalsColor);
	}
	
	void setComponentColors(Component component, Color background)
	{
		component.setBackground(background);
		component.setForeground(Color.BLACK);
	}
	
	private void setBorders(AbstractBudgetTableModel model, int row, int column)
	{
		Color darkBorderColor = Color.DARK_GRAY;
		final int THICKNESS = 2;
		if (model.doubleRowed())
			setDoubleRowedBorders(model, row, column, darkBorderColor, THICKNESS);
		else
			setSingleRowedBorders(model, row, column, darkBorderColor, THICKNESS);
	}
	
	private void setSingleRowedBorders(AbstractBudgetTableModel model, int row, int column, Color darkBorderColor, final int THICKNESS)
	{
		boolean unitsTotalRow = model.isUnitsTotalRow(row);
		if (unitsTotalRow)
			setBorder(BorderFactory.createMatteBorder(THICKNESS, 0, 0, 0, darkBorderColor));
		
		boolean yearlyTotalColumn = model.isYearlyTotalColumn(column);
		if (yearlyTotalColumn)
			setBorder(BorderFactory.createMatteBorder(0, THICKNESS, 0, THICKNESS, darkBorderColor));
		
		if (unitsTotalRow && yearlyTotalColumn)
			setBorder(BorderFactory.createMatteBorder(THICKNESS, THICKNESS, 0, THICKNESS, darkBorderColor));
	}

	private void setDoubleRowedBorders(AbstractBudgetTableModel model, int row, int column, Color darkBorderColor, final int THICKNESS)
	{
	
		if (model.getRowTotalsLabelColumnIndex() == column)
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
		
		if (model.isCostColumn(column))
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
		
		if (model.isUnitsTotalRow(row))
			setBorder(BorderFactory.createMatteBorder(THICKNESS, 0, 0, 0, darkBorderColor));
		
		if (model.isCostColumn(column) && model.isUnitsTotalRow(row))
			setBorder(BorderFactory.createMatteBorder(THICKNESS, 0, 0, THICKNESS, darkBorderColor));
		
		if (model.getRowTotalsLabelColumnIndex() == column && model.isUnitsTotalRow(row))
			setBorder(BorderFactory.createMatteBorder(THICKNESS, 0, 0, THICKNESS, darkBorderColor));
	}

	private static final Color[] EVERY_OTHER_TWO_COLORS = new Color[] { new Color(0xf0, 0xf0, 0xf0), new Color(0xf0, 0xf0, 0xf0), Color.WHITE, Color.WHITE, };
	private static final Color[] BACKGROUND_COLORS = new Color[] { new Color(0xf0, 0xf0, 0xf0), Color.WHITE, };
}
