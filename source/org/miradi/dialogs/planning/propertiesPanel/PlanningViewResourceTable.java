/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;

import javax.swing.JLabel;

import org.miradi.ids.BaseId;
import org.miradi.main.AppPreferences;
import org.miradi.objectpools.ResourcePool;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;

public class PlanningViewResourceTable extends PlanningViewAbstractTableWithPreferredScrollableViewportSize
{
	public PlanningViewResourceTable(PlanningViewResourceTableModel modelToUse)
	{
		super(modelToUse);
		model = modelToUse;
		
		setBackground(getColumnBackGroundColor(0));
		rebuildColumnEditorsAndRenderers();
	}
	
	public Color getColumnBackGroundColor(int column)
	{
		return AppPreferences.RESOURCE_TABLE_BACKGROUND;
	}

	public void rebuildColumnEditorsAndRenderers()
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
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}
	
	public ProjectResource[] getAllProjectResources()
	{
		return  getResourcePool().getAllProjectResources();
	}
	
	public ResourcePool getResourcePool()
	{
		return getObjectManager().getResourcePool();
	}

	private PlanningViewResourceTableModel model;
    public static final String UNIQUE_IDENTIFIER = "PlanningViewResourceTable";
}
