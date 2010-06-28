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
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.CategoryOne;
import org.miradi.objects.CategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

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
		
		ORefList allAssignments = new ORefList();
		allAssignments.addAll(getProject().getAssignmentPool().getRefList());
		allAssignments.addAll(getProject().getExpenseAssignmentPool().getRefList());	
		children.add(new RollupReportsNode(getProject(), getVisibleRows(), getProject().getMetadata(), getLevelTypeCodes(), levelType, allAssignments));
	}

	private CodeList getLevelTypeCodes()
	{
		CodeList types = new CodeList();
		types.addIntCode(ProjectResource.getObjectType());
		types.addIntCode(FundingSource.getObjectType());
		types.addIntCode(AccountingCode.getObjectType());
		types.addIntCode(CategoryOne.getObjectType());
		types.addIntCode(CategoryTwo.getObjectType());
		return types;
	}
}
