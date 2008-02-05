/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.organization;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolManagementPanel;
import org.conservationmeasures.eam.icons.OrganizationIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ExportableTableInterface;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

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
		
	private static String PANEL_DESCRIPTION = EAM.text("Title|Partners"); 
}
