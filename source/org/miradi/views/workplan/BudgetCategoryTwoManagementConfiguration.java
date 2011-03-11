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

import org.miradi.actions.ActionCreateCategoryTwo;
import org.miradi.actions.ActionDeleteCategoryTwo;
import org.miradi.dialogs.planning.BudgetCategoryTwoCoreRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.icons.BudgetCategoryTwoIcon;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class BudgetCategoryTwoManagementConfiguration extends WorkPlanManagementPanelConfiguration
{
	public BudgetCategoryTwoManagementConfiguration(Project projectToUse)
	{
		super(projectToUse);
	}
	
	@Override
	public WorkPlanCategoryTreeRowColumnProvider getRowColumnProvider() throws Exception
	{
		return new BudgetCategoryTwoCoreRowColumnProvider(getProject());
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Category #2");
	}	
	
	@Override
	public Icon getIcon()
	{
		return new BudgetCategoryTwoIcon();
	}
	
	@Override
	public Class[] getButtonActions()
	{
		return new Class[] {
				ActionCreateCategoryTwo.class, 
				ActionDeleteCategoryTwo.class,
		};
	}
	
	@Override
	public String getUniqueTreeTableIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "CategoryTwoTreeTableModel";
}
