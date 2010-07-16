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

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.miradi.dialogs.planning.CategoryTreeRowColumnProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.RollupReportsObjectTypeQuestion;
import org.miradi.utils.CodeList;

public class CategoryTreeNode extends AbstractPlanningTreeNode
{
	public CategoryTreeNode(Project project, CategoryTreeRowColumnProvider rowColumnProviderToUse, BaseObject nodeObjectToUse, int levelToUse, ORefList assignmentRefsThatMatchParentRowToUse) throws Exception
	{
		super(project, rowColumnProviderToUse.getRowListToShow());
		
		rowColumnProvider = rowColumnProviderToUse;
		nodeObject = nodeObjectToUse;
		currentLevel = levelToUse;
		assignmentRefsThatMatchParentRow = assignmentRefsThatMatchParentRowToUse;
		
		rebuild();
	}

	@Override
	public BaseObject getObject()
	{
		return nodeObject;
	}
	
	@Override
	public String toRawString()
	{
		if (ProjectMetadata.is(getObjectReference()))
			return BaseObjectTreeRootNodeWithUnspecifiedNode.getProjectTotalsName();
		
		return super.toRawString();
	}
	
	@Override
	public void rebuild() throws Exception
	{
		final int ONE_LEVEL = 1;
		int childLevel = getCurrentLevel() + ONE_LEVEL;

		children = new Vector();
		if (!getLevelObjectTypes().hasData())
			return;
		
		if (getLevelObjectTypes().size() <= getCurrentLevel())
			return;
		
		String levelObjectTypeAsString = getLevelObjectTypes().get(getCurrentLevel());
		if (levelObjectTypeAsString.equals(RollupReportsObjectTypeQuestion.UNSPECIFIED_CODE))
			return;
		
		int levelObjectType = Integer.parseInt(levelObjectTypeAsString);
		HashMap<ORef, ORefList> categoryRefToAssignmentRefsMap = createCategoryRefToAssignmentRefsMap(levelObjectType);
		ORefList childRefs = getProject().getPool(levelObjectType).getRefList();
		addUnspecifiedRowInPlace(childRefs);
		for (int index = 0; index < childRefs.size(); ++index)
		{	
			BaseObject possibleChildObject = createOrFindChildObject(childRefs.get(index), levelObjectType);
			ORefList assignmentRefsReferringToRow = getAssignmentsReferringToRow(categoryRefToAssignmentRefsMap, possibleChildObject);
			ORefList overlappingAssignmentRefs = assignmentRefsReferringToRow.getOverlappingRefs(getAssignmentRefsThatMatchParentRow());
			if (shouldIncludeChildNode(overlappingAssignmentRefs))
				children.add(new CategoryTreeNode(getProject(), rowColumnProvider, possibleChildObject, childLevel, overlappingAssignmentRefs));
		}
		
		Collections.sort(children, createNodeSorter());
	}

	private boolean shouldIncludeChildNode(ORefList overlappingAssignmentRefs)
	{
		if (overlappingAssignmentRefs.hasRefs())
			return true;
		
		return rowColumnProvider.shouldIncludeEmptyRows();
	}

	private ORefList getAssignmentsReferringToRow(HashMap<ORef, ORefList> categoryRefToAssignmentRefsMap, BaseObject possibleChildObject)
	{
		ORefList referringAssignments = categoryRefToAssignmentRefsMap.get(possibleChildObject.getRef());
		if (referringAssignments == null)
			return new ORefList();
		
		return referringAssignments;
	}

	private void addUnspecifiedRowInPlace(ORefList childRefs)
	{
		childRefs.add(ORef.INVALID);
	}

	private HashMap<ORef, ORefList> createCategoryRefToAssignmentRefsMap(int levelObjectType)
	{
		HashMap<ORef, ORefList> categoryRefToAssignmentRefsMap = new HashMap<ORef, ORefList>();
		for (int index = 0; index < getAssignmentRefsThatMatchParentRow().size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(getProject(), getAssignmentRefsThatMatchParentRow().get(index));
			ORefList refList = new ORefList(assignment);
			ORef categoryRef = assignment.getCategoryRef(levelObjectType);
			if (!categoryRefToAssignmentRefsMap.containsKey(categoryRef))
				categoryRefToAssignmentRefsMap.put(categoryRef, new ORefList());
				
			categoryRefToAssignmentRefsMap.get(categoryRef).addAll(refList);
		}
		
		return categoryRefToAssignmentRefsMap;
	}

	private BaseObject createOrFindChildObject(ORef ref, int levelObjectType)
	{
		if (ref.isValid())
			return BaseObject.find(getProject(), ref);
		
		return new UnspecifiedBaseObject(getProject().getObjectManager(), levelObjectType, getObjectTypeName());
	}

	private ORefList getAssignmentRefsThatMatchParentRow()
	{
		return assignmentRefsThatMatchParentRow;
	}

	private CodeList getLevelObjectTypes() throws Exception
	{
		return rowColumnProvider.getLevelTypeCodes();
	}

	private int getCurrentLevel()
	{
		return currentLevel;
	}
		
	private BaseObject nodeObject;
	private int currentLevel;
	private ORefList assignmentRefsThatMatchParentRow;
	private CategoryTreeRowColumnProvider rowColumnProvider;
}
