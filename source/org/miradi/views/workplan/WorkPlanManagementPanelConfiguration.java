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

import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

abstract public class WorkPlanManagementPanelConfiguration
{
	public WorkPlanManagementPanelConfiguration(Project projectToUse)
	{
		project = projectToUse;
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	public CodeList getLevelTypeCodes() throws Exception
	{
		return getRowColumnProvider().getLevelTypeCodes();
	}
	
	abstract public WorkPlanCategoryTreeRowColumnProvider getRowColumnProvider() throws Exception;

	abstract public String getPanelDescription();

	abstract public Class[] getButtonActions();
	
	abstract public String getUniqueTreeTableIdentifier();
	
	abstract public Icon getIcon();
	
	private Project project;
}
