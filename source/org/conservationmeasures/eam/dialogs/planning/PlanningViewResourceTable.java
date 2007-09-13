/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.FundingSource;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.TableWithHelperMethods;

public class PlanningViewResourceTable extends TableWithHelperMethods
{
	public PlanningViewResourceTable(PlanningViewResourceTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;

		addColumnEditorsAndRenderers();
		//TODO planning table - find better solution
		setRowHeight(getRowHeight() + 10);
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		model.setObjectRefs(hierarchyToSelectedRef);
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
		JComboBox comboBox = new JComboBox(content);
		TableColumn tableColumn = getColumnModel().getColumn(col);
		tableColumn.setCellEditor(new DefaultCellEditor(comboBox));
		tableColumn.setCellRenderer(new ComboBoxRenderer(content));
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

