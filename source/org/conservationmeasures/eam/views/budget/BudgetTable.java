/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelComboBox;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTextField;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SingleClickAutoSelectCellEditor;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class BudgetTable extends TableWithHelperMethods implements ObjectPicker 
{	
	public BudgetTable(Project projectToUse, AssignmentTableModelSplittableShell modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
		project = projectToUse;
		selectionListeners = new Vector();

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setDefaultRenderer(Object.class, new AlternatingThickBorderedTotalsColoredRenderer());

		//TODO remove the addition of two pixels.  the + 2 is becuase dropdowns cut off 'g's.
		// must figure out a way to do this right.  
		final int TWO_PIXELS = 2;
		setRowHeight(rowHeight + TWO_PIXELS);
		rebuild();
	}
	
	public void setTask(BaseId taskId)
	{
		rebuild();
	}
	
	private void rebuild()
	{
		addColumnEditorsAndRenderers();
		setSingleCellEditor();
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
				column.setCellEditor(new SingleClickAutoSelectCellEditor(new PanelTextField()));
		}
	}

	private void addColumnEditorsAndRenderers()
	{
		for (int colIndex = 0 ; colIndex < model.getColumnCount(); colIndex++)
		{
			setColumnEditorAndRenderer(colIndex);
		}
	}
	
	private void setColumnEditorAndRenderer(int column)
	{
		if (model.isResourceColumn(column))
		{
			ProjectResource[] resources = project.getAllProjectResources();
			ProjectResource invalidResource = new ProjectResource(project.getObjectManager(), BaseId.INVALID);
			createComboColumn(resources, column, invalidResource);
		}
		else if (model.isFundingSourceColumn(column))
		{
			FundingSource[] fundingSources = project.getObjectManager().getFundingSourcePool().getAllFundingSources();
			FundingSource invalidFundintSource = new FundingSource(project.getObjectManager(), BaseId.INVALID);
			createComboColumn(fundingSources, column, invalidFundintSource);
		}
		else if (model.isAccountingCodeColumn(column))
		{
			AccountingCode[] accountingCodes = project.getObjectManager().getAccountingCodePool().getAllAccountingCodes();
			AccountingCode invalidAccountingCode = new AccountingCode(project.getObjectManager(), BaseId.INVALID);
			createComboColumn(accountingCodes, column, invalidAccountingCode);
		}
	}
	
	private void createComboColumn(BaseObject[] content, int col, BaseObject invalidObject)
	{
		BaseObject[] comboContent = addEmptySpaceAtStart(content, invalidObject);
		JComboBox resourceCombo = new PanelComboBox(comboContent);
		
		TableColumn resourceCol = getColumnModel().getColumn(col);
		resourceCol.setCellEditor(new DefaultCellEditor(resourceCombo));
		resourceCol.setCellRenderer(new ComboBoxRenderer(comboContent));
	}

	private BaseObject[] addEmptySpaceAtStart(BaseObject[] content, BaseObject invalidObject)
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
	
	public TreeTableNode[] getSelectedTreeNodes()
	{
		return null;
	}

	public BaseObject[] getSelectedObjects()
	{
		int selectedRow = getSelectedRow();
		if (selectedRow < 0)
			return new BaseObject[0];
		
		AssignmentTableModelSplittableShell budgetModel = getBudgetModel();
		selectedRow = budgetModel.getCorrectedRow(selectedRow);
		
		BaseId selectedId = budgetModel.getAssignmentForRow(selectedRow);
		BaseObject selectedObject = project.findObject(ObjectType.ASSIGNMENT, selectedId);
		if (selectedObject == null)
			return new BaseObject[0];
	
		return new BaseObject[] {selectedObject};
	}

	public void ensureObjectVisible(ORef ref)
	{
		// TODO Auto-generated method stub
		// we should scroll the table as needed to make this 
		// probably-newly-created object visible
	}

	public void addSelectionChangeListener(SelectionChangeListener listener)
	{
		selectionListeners.add(listener);
	}

	public void removeSelectionChangeListener(SelectionChangeListener listener)
	{
		selectionListeners.remove(listener);
	}

	public void valueChanged(ListSelectionEvent e)
	{
		super.valueChanged(e);
		if(selectionListeners == null)	
			return;
		
		for(int i = 0; i < selectionListeners.size(); ++i)
		{
			SelectionChangeListener listener = (SelectionChangeListener)selectionListeners.get(i);
			listener.selectionHasChanged();
		}
	}

	public AssignmentTableModelSplittableShell  getBudgetModel()
	{
		return (AssignmentTableModelSplittableShell)getModel();
	}
	
	public void stopCellEditing()
	{
		TableCellEditor editor = getCellEditor();
		if (editor == null)
			return;

		editor.stopCellEditing();
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
	AssignmentTableModelSplittableShell model;
	Vector selectionListeners;
	
	static final String COLUMN_HEADER_TITLE = EAM.text("Resource Names");
}

class ComboBoxRenderer extends PanelComboBox implements TableCellRenderer 
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
        
        AssignmentTableModelSplittableShell budgetModel = ((BudgetTable)table).getBudgetModel();
		if (! budgetModel.isCellEditable(row, col))
        	return new PanelTitleLabel("");
        
        setSelectedItem(value);
        return this;
    }
    
    private void setColors(Color background, Color foreground)
    {
        setForeground(foreground);
        setBackground(background);
    }
}


class CustomColumnHeaderRenederer extends DefaultTableCellRenderer
{
	public CustomColumnHeaderRenederer()
	{
	  preferredSize = getPreferredSize();
	}
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		component.setBackground(new Color(0xf0, 0xf0, 0xf0).darker());
		int sameWidth = component.getPreferredSize().width;
		int doubleHieght = preferredSize.height * 2;
		component.setPreferredSize(new Dimension(sameWidth, doubleHieght));
		
		return component;
	}
	
	Dimension preferredSize;
}

class AlternatingThickBorderedTotalsColoredRenderer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		AssignmentTableModelSplittableShell model = ((BudgetTable)table).getBudgetModel();
		if (!isSelected)
			setColors(table, model, component, row, column);
		
		if (isSelected && hasFocus)
			setSelectedAndInFocusCellColors(table, row, column, component);
		
		setBorders(model, row, column);
		return component;
	}

	private void setSelectedAndInFocusCellColors(JTable table, int row, int column, Component component)
	{
		if (table.isCellEditable(row, column))
			setComponentColors(component, Color.white, Color.blue);
		else
			setComponentColors(component, Color.lightGray, Color.black);
	}

	private void setColors(JTable table, AssignmentTableModelSplittableShell model, Component component, int row, int column)
	{
		if (model.doubleRowed())
			setComponentColors(component, EVERY_OTHER_TWO_COLORS[row % 4], Color.BLACK);
		
		if (! model.doubleRowed())
			setComponentColors(component, BACKGROUND_COLORS[row % 2], Color.BLACK);
		
		Color totalsColor = new Color(0xf0, 0xf0, 0xf0).darker();
		
		if (model.isYearlyTotalColumn(column))
			setComponentColors(component, totalsColor, Color.BLACK);
		
		if (model.isCostTotalsColumn(column))
			setComponentColors(component, totalsColor, Color.BLACK);
		
		if (model.isUnitsTotalColumn(column))
			setComponentColors(component, totalsColor, Color.BLACK);
		
		if (model.isCellEditable(row, column))
			component.setForeground(Color.BLUE);
	}
	
	void setComponentColors(Component component, Color background, Color foreground)
	{
		component.setBackground(background);
		component.setForeground(foreground);
	}

	private void setBorders(AssignmentTableModelSplittableShell model, int row, int column)
	{
		Color darkBorderColor = Color.DARK_GRAY;
		final int THICKNESS = 2;
		if (model.doubleRowed())
			setDoubleRowedBorders(model, row, column, darkBorderColor, THICKNESS);
		else
			setSingleRowedBorders(model, row, column, darkBorderColor, THICKNESS);
	}
	
	private void setSingleRowedBorders(AssignmentTableModelSplittableShell model, int row, int column, Color darkBorderColor, final int THICKNESS)
	{
		boolean yearlyTotalColumn = model.isYearlyTotalColumn(column);
		if (yearlyTotalColumn)
			setBorder(BorderFactory.createMatteBorder(0, THICKNESS, 0, THICKNESS, darkBorderColor));
		if (model.isUnitsLabelColumn(column))
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
	}

	private void setDoubleRowedBorders(AssignmentTableModelSplittableShell model, int row, int column, Color darkBorderColor, final int THICKNESS)
	{
		if (model.isCostColumn(column))
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
		if (model.isCostPerUnitLabelColumn(column))
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, THICKNESS, darkBorderColor));
	}

	private static final Color[] EVERY_OTHER_TWO_COLORS = new Color[] { Color.WHITE, Color.WHITE, new Color(0xf0, 0xf0, 0xf0), new Color(0xf0, 0xf0, 0xf0), };
	private static final Color[] BACKGROUND_COLORS = new Color[] {  Color.WHITE, new Color(0xf0, 0xf0, 0xf0), };
}
