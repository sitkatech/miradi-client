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

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.CategoryOne;
import org.miradi.objects.CategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class RollupReportsNode extends AbstractPlanningTreeNode
{
	public RollupReportsNode(Project project, CodeList visibleRows,	BaseObject nodeObjectToUse, CodeList levelObjectTypesToUse, int levelToUse, ORefList assignmentsToUse) throws Exception
	{
		super(project, visibleRows);
		
		nodeObject = nodeObjectToUse;
		levelObjectTypes = levelObjectTypesToUse;
		currentLevel = levelToUse;
		assignments = assignmentsToUse;
		
		rebuild();
	}

	@Override
	public BaseObject getObject()
	{
		return nodeObject;
	}
	
	@Override
	public void rebuild() throws Exception
	{
		final int ONE_LEVEL = 1;
		int childLevel = currentLevel + ONE_LEVEL;

		children = new Vector();
		String levelObjectTypeAsString = levelObjectTypes.get(currentLevel);
		int levelObjectType = Integer.parseInt(levelObjectTypeAsString);
		ORefList refs = getProject().getPool(levelObjectType).getRefList();
		for (int index = 0; index < refs.size(); ++index)
		{	
			BaseObject childBaseObject = BaseObject.find(getProject(), refs.get(index));
			ORefList referringAssignmentRefs = isReferredToByAtleastOneAssignment(childBaseObject, levelObjectType);
			if (referringAssignmentRefs.hasRefs())
				children.add(new RollupReportsNode(getProject(), getVisibleRows(), childBaseObject, levelObjectTypes, childLevel, referringAssignmentRefs));
		}		
	}
	
	private ORefList isReferredToByAtleastOneAssignment(BaseObject childBaseObject, int levelObjectType)
	{
		ORefList assignmentsReferringToNode = new ORefList();
		for (int assignmentIndex = 0; assignmentIndex < assignments.size(); ++assignmentIndex)
		{
			Assignment assignment = Assignment.findAssignment(getProject(), assignments.get(assignmentIndex));
			ORef refForLevelType = getRefForLevelType(levelObjectType, assignment);
			if (childBaseObject.getRef().equals(refForLevelType))
				assignmentsReferringToNode.add(assignment);
		}
		
		return assignmentsReferringToNode;
	}
	
	private ORef getRefForLevelType(int levelObjectType, Assignment assignment)
	{
		if (ProjectResource.is(levelObjectType))
			return assignment.getResourceRef();
		
		if (FundingSource.is(levelObjectType))
			return assignment.getFundingSourceRef();
		
		if (AccountingCode.is(levelObjectType))
			return assignment.getAccountingCodeRef();
		
		if (CategoryOne.is(levelObjectType))
			return assignment.getCategoryOneRef();
		
		if (CategoryTwo.is(levelObjectType))
			return assignment.getCategoryTwoRef();
		
		throw new RuntimeException("unkwnown budget type: " + levelObjectType);
	}

	private BaseObject nodeObject;
	private CodeList levelObjectTypes;
	private int currentLevel;
	private ORefList assignments;
}
