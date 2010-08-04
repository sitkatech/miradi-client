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

import org.miradi.actions.ActionCreateCategoryOne;
import org.miradi.actions.ActionDeleteCategoryOne;
import org.miradi.dialogs.planning.BudgetCategoryOneCoreRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.icons.BudgetCategoryOneIcon;
import org.miradi.main.EAM;
import org.miradi.project.Project;

public class BudgetCategoryOneManagementConfiguration extends AbstractManagementConfiguration
{
	public BudgetCategoryOneManagementConfiguration(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public Class[] getButtonActions()
	{
		return new Class[] {
				ActionCreateCategoryOne.class, 
				ActionDeleteCategoryOne.class,
		};
	}

	@Override
	public Icon getIcon()
	{
		return new BudgetCategoryOneIcon();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Category #1");
	}

	@Override
	public WorkPlanCategoryTreeRowColumnProvider getRowColumnProvider()	throws Exception
	{
		return new BudgetCategoryOneCoreRowColumnProvider();
	}

	@Override
	public String getUniqueTreeTableIdentifier()
	{
		return UNIQUE_TREE_TABLE_IDENTIFIER;
	}
	
	private static final String UNIQUE_TREE_TABLE_IDENTIFIER = "CategoryOneTreeTableModel";
}
