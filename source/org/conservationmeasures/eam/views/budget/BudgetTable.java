/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

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
		// this property is set due to a JTable bug#4724980 
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		//TODO remove the addition of two pixels.  the + 2 is becuase dropdowns cut off 'g's.
		// must figure out a way to do this right.  
		final int TWO_PIXELS = 2;
		setRowHeight(rowHeight + TWO_PIXELS);
		rebuild();
	}
	
	public void setTask(BaseId taskId)
	{
		//TODO budget code - remove commented code
		//Task selectedTask = (Task)project.findObject(ObjectType.TASK, taskId);
		rebuild();
	}
	
	private void rebuild()
	{
		//TODO budget code - verify the need for this if
		if (model.getColumnCount() <= 0)
			return;
		
		addResourceColumn();
		addFundingSourceColumn();
		addAccountingCodeColumn();
		
		setSingleCellEditor();
		
	}

	private void addResourceColumn()
	{
		ProjectResource[] projectResources = project.getAllProjectResources();
		int resourceColumn = model.getResourcesColumnIndex();
		createComboColumn(projectResources, resourceColumn);
	}

	private void addAccountingCodeColumn()
	{
		int accountingCodesColumn = model.getAccountingCodeColumnIndex();
		if (accountingCodesColumn < 0)
			return;

		AccountingCode[] accountingCodes = project.getObjectManager().getAccountingCodePool().getAllAccountingCodes();
		createComboColumn(accountingCodes, accountingCodesColumn);
	}

	private void addFundingSourceColumn()
	{
		int fundingSourceColumn = model.getFundingSourceColumnIndex();
		if (fundingSourceColumn < 0 )
			return;

		FundingSource[] fundingSources = project.getObjectManager().getFundingSourcePool().getAllFundingSources();
		createComboColumn(fundingSources, fundingSourceColumn);
	}

	private void setSingleCellEditor()
	{
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++)
		{
			TableColumn column = getColumnModel().getColumn(i);
			if (model.isCostTotalsColumn(i) || model.isUnitsTotalColumn(i) || model.isYearlyTotalColumn(i))
			    column.setHeaderRenderer(new CustomColumnHeaderRenederer());
			if (model.isUnitsColumn(i))
				column.setCellEditor(new SingleClickAutoSelectCellEditor(new JTextField()));
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
	
	public void cancelCellEditing()
	{
		TableCellEditor editor = getCellEditor();
		if (editor == null)
			return;

		editor.cancelCellEditing();
	}
	
	public Component prepareEditor(TableCellEditor editor, int row, int column)
	{
		Component c = super.prepareEditor(editor, row, column);
		if (c instanceof JTextComponent)
		{
			((JTextField)c).selectAll();
		}

		return c;
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

class CustomColumnHeaderRenederer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		component.setBackground(new Color(0xf0, 0xf0, 0xf0).darker());
		return component;
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
		
		if (model.isYearlyTotalColumn(column))
			setComponentColors(component, totalsColor);
		
		if (model.isCostTotalsColumn(column))
			setComponentColors(component, totalsColor);
		
		if (model.isUnitsTotalColumn(column))
			setComponentColors(component, totalsColor);
		
		if (model.isCellEditable(row, column))
			component.setForeground(Color.BLUE);
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
		boolean yearlyTotalColumn = model.isYearlyTotalColumn(column);
		if (yearlyTotalColumn)
			setBorder(BorderFactory.createMatteBorder(0, THICKNESS, 0, THICKNESS, darkBorderColor));
		if (model.isUnitsLabelColumn(column))
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
	}

	private void setDoubleRowedBorders(AbstractBudgetTableModel model, int row, int column, Color darkBorderColor, final int THICKNESS)
	{
		if (model.isCostColumn(column))
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
		if (model.getCostPerUnitLabelColumnIndex() == column)
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
	}

	private static final Color[] EVERY_OTHER_TWO_COLORS = new Color[] { Color.WHITE, Color.WHITE, new Color(0xf0, 0xf0, 0xf0), new Color(0xf0, 0xf0, 0xf0), };
	private static final Color[] BACKGROUND_COLORS = new Color[] {  Color.WHITE, new Color(0xf0, 0xf0, 0xf0), };
}
