/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class PlanningViewResourceTable extends PlanningViewAbstractTable implements ObjectPicker
{
	public PlanningViewResourceTable(PlanningViewResourceTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
		
		selectionListeners = new Vector();
		addColumnEditorsAndRenderers();
	}
		
	private void addColumnEditorsAndRenderers()
	{
		for (int i = 0; i < model.getColumnCount(); ++i)
		{
			createResourceCombo(i);
			createFundingSourceColumn(i);
			createAccountingCodeColumn(i);
		}
	}
	
	private void createAccountingCodeColumn(int column)
	{
		if (! model.isAccountingCodeColumn(column))
			return;
		
		AccountingCode[] accountingCodes = getObjectManager().getAccountingCodePool().getAllAccountingCodes();
		AccountingCode invalidAccountingCode = new AccountingCode(getObjectManager(), BaseId.INVALID);
		createComboColumn(accountingCodes, column, invalidAccountingCode);
	}
	
	private void createResourceCombo(int column)
	{
		if (! model.isResourceColumn(column))
			return;
		
		ProjectResource[] resources = getAllProjectResources();
		ProjectResource invalidResource = new ProjectResource(getObjectManager(), BaseId.INVALID);
		createComboColumn(resources, column, invalidResource);
	}
	
	private void createFundingSourceColumn(int column)
	{
		if (! model.isFundingSourceColumn(column))
			return;

		FundingSource[] fundingSources = getObjectManager().getFundingSourcePool().getAllFundingSources();
		FundingSource invalidFundintSource = new FundingSource(getObjectManager(), BaseId.INVALID);
		createComboColumn(fundingSources, column, invalidFundintSource);
	}
	
	private void createComboColumn(BaseObject[] content, int col, BaseObject invalidObject)
	{
		BaseObject[] comboContent = addEmptySpaceAtStart(content, invalidObject);
		JComboBox comboBox = new JComboBox(comboContent);
		TableColumn tableColumn = getColumnModel().getColumn(col);
		tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
		tableColumn.setCellRenderer(new ComboBoxRenderer(comboContent));
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
	
	public ProjectResource[] getAllProjectResources()
	{
		return  getResourcePool().getAllProjectResources();
	}
	
	public ResourcePool getResourcePool()
	{
		return getObjectManager().getResourcePool();
	}

	private Project getProject()
	{
		return model.getProject();
	}
	
	private ObjectManager getObjectManager()
	{
		return getProject().getObjectManager();
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
		
		Assignment selectedAssignment = model.getAssignment(selectedRow);
		if (selectedAssignment == null)
			return new BaseObject[0];
	
		return new BaseObject[] {selectedAssignment};
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
	
	private Vector selectionListeners;
	private PlanningViewResourceTableModel model;
}

class ComboBoxRenderer extends JComboBox implements TableCellRenderer 
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

