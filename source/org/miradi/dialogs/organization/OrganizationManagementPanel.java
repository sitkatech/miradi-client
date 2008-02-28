/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.organization;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpSummaryWizardRolesAndResponsibilities;
import org.miradi.dialogs.base.ObjectPoolManagementPanel;
import org.miradi.icons.OrganizationIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class OrganizationManagementPanel extends ObjectPoolManagementPanel
{
	public OrganizationManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, Actions actionsToUse) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, new OrganizationPoolTablePanel(projectToUse, actionsToUse),
				new OrganizationPropertiesPanel(projectToUse, BaseId.INVALID));

	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new OrganizationIcon();
	}
	
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
	{
		return createTable();
	}

	private OrganizationPoolTable createTable()
	{
		return new OrganizationPoolTable(new OrganizationPoolTableModel(getProject()));
	}
	
	@Override
	public boolean isPrintable()
	{
		return true;
	}
	
	@Override
	public JComponent getPrintableComponent() throws Exception
	{
		return createTable();
	}
		
	@Override
	public Class getJumpActionClass()
	{
		return ActionJumpSummaryWizardRolesAndResponsibilities.class;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Other Orgs"); 
}
