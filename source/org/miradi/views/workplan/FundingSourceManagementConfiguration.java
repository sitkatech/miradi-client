/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.views.workplan;

import javax.swing.Icon;

import org.miradi.actions.ActionCreateFundingSource;
import org.miradi.actions.ActionDeleteFundingSource;
import org.miradi.dialogs.planning.FundingSourceRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.icons.FundingSourceIcon;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class FundingSourceManagementConfiguration extends AbstractManagementConfiguration
{
	public FundingSourceManagementConfiguration(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public Class[] getButtonActions()
	{
		return new Class[] {
				ActionCreateFundingSource.class, 
				ActionDeleteFundingSource.class,
		};
	}

	@Override
	public Icon getIcon()
	{
		return new FundingSourceIcon();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Funding Sources");
	}

	@Override
	public WorkPlanCategoryTreeRowColumnProvider getRowColumnProvider() throws Exception
	{
		return new FundingSourceRowColumnProvider();
	}

	@Override
	public String getUniqueTreeTableIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "FundingSourceTreeTableModel";
}
