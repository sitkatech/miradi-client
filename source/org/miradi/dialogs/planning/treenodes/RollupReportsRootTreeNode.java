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

package org.miradi.dialogs.planning.treenodes;

import java.util.Vector;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.views.workplan.WorkPlanView;

public class RollupReportsRootTreeNode extends AbstractPlanningTreeNode
{
	public RollupReportsRootTreeNode(Project projectToUse, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		
		rebuild();
	}
	
	@Override
	public boolean isAlwaysExpanded()
	{
		return true;
	}

	@Override
	public BaseObject getObject()
	{
		return getProject().getMetadata();
	}
	
	@Override
	public void rebuild() throws Exception
	{
		children = new Vector();
		int levelType = 0;
		CodeList levelObjectTypes = getLevelTypeCodes();
		
		ORefList allAsignmentRefs = new ORefList();
		allAsignmentRefs.addAll(getProject().getAssignmentPool().getRefList());
		allAsignmentRefs.addAll(getProject().getExpenseAssignmentPool().getRefList());
		children.add(new RollupReportsNode(getProject(), getVisibleRows(), getObject(), levelObjectTypes, levelType, allAsignmentRefs));
	}

	private CodeList getLevelTypeCodes() throws Exception
	{
		return getProject().getViewData(WorkPlanView.getViewName()).getBudgetRollupReportLevelTypes();
	}
}
