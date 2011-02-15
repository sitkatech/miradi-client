/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import java.util.HashMap;

import org.miradi.dialogs.planning.AbstractBudgetCategoryRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.NewAbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeAlwaysExpandedBaseObjectNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeBaseObjectNode;
import org.miradi.dialogs.planning.treenodes.UnspecifiedBaseObject;
import org.miradi.dialogs.planning.treenodes.UnspecifiedBaseObjectNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanCategoryTypesQuestion;
import org.miradi.utils.CodeList;

public class PlanningCategoryTreeRebuilder extends AbstractTreeRebuilder
{
	public PlanningCategoryTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(projectToUse, rowColumnProviderToUse);
	}
	
	@Override
	public void rebuildTree(NewAbstractPlanningTreeNode rootNode) throws Exception
	{
		rootNode.clearChildren();
		ORefList allAsignmentRefs = new ORefList();
		allAsignmentRefs.addAll(getProject().getAssignmentPool().getRefList());
		allAsignmentRefs.addAll(getProject().getExpenseAssignmentPool().getRefList());

		PlanningTreeBaseObjectNode projectNode = new PlanningTreeAlwaysExpandedBaseObjectNode(getProject(), rootNode, getProject().getMetadata());
		rootNode.addChild(projectNode);
		
		int initialLevel = 0;
		rebuild(projectNode, allAsignmentRefs, initialLevel);
	}
	
	private void rebuild(NewAbstractPlanningTreeNode parentNode, ORefList assignmentRefsThatApplyToThisNode, int currentLevel) throws Exception
	{
		final int ONE_LEVEL = 1;
		int childLevel = currentLevel + ONE_LEVEL;
		
		if (!getLevelObjectTypes().hasData())
			return;
		
		if (currentLevel >= getLevelObjectTypes().size())
			return;
		
		String typeOfNodeBeingCreatedAsString = getLevelObjectTypes().get(currentLevel);
		if (typeOfNodeBeingCreatedAsString.equals(WorkPlanCategoryTypesQuestion.UNSPECIFIED_CODE))
			return;
		
		int typeOfNodeBeingCreated = Integer.parseInt(typeOfNodeBeingCreatedAsString);
		HashMap<ORef, ORefList> categoryRefToAssignmentRefsMap = createCategoryRefToAssignmentRefsMap(assignmentRefsThatApplyToThisNode, typeOfNodeBeingCreated);
		ORefList childRefs = getProject().getPool(typeOfNodeBeingCreated).getRefList();
		addUnspecifiedRowInPlace(childRefs);
		for (int index = 0; index < childRefs.size(); ++index)
		{	
			ORef candidateChildRef = childRefs.get(index);
			BaseObject possibleChildObject = createOrFindChildObject(candidateChildRef, typeOfNodeBeingCreated);
			ORefList assignmentRefsThatMatchPossibleChild = getAssignmentsReferringToRow(categoryRefToAssignmentRefsMap, possibleChildObject);
			ORefList assignmentRefsThatMatchPossibleChildHierarchy = assignmentRefsThatMatchPossibleChild.getOverlappingRefs(assignmentRefsThatApplyToThisNode);
			if (shouldIncludeChildNode(assignmentRefsThatMatchPossibleChildHierarchy))
			{
				NewAbstractPlanningTreeNode childNode = createChildNode(parentNode, candidateChildRef, typeOfNodeBeingCreated);
				
				parentNode.addChild(childNode);
				rebuild(childNode, assignmentRefsThatMatchPossibleChildHierarchy, childLevel);
			}
		}
		
		possiblySortChildren(parentNode, parentNode.getRawChildrenByReference());
	}

	protected NewAbstractPlanningTreeNode createChildNode(NewAbstractPlanningTreeNode parentNode, ORef possibleChildRef, int typeOfNodeToCreate) throws Exception
	{
		if (possibleChildRef.isValid())
			return new PlanningTreeBaseObjectNode(getProject(), parentNode, possibleChildRef);

		String objectNameForType = getProject().getObjectManager().getInternalObjectTypeName(typeOfNodeToCreate);
		return  new UnspecifiedBaseObjectNode(getProject(), parentNode, typeOfNodeToCreate, objectNameForType);
	}
	
	private boolean shouldIncludeChildNode(ORefList overlappingAssignmentRefs)
	{
		if (overlappingAssignmentRefs.hasRefs())
			return true;
		
		return getCastedRowColumnProvider().shouldIncludeEmptyRows();
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

	private HashMap<ORef, ORefList> createCategoryRefToAssignmentRefsMap(ORefList assignmentRefsThatMatchThisNodeHierarchy, int objectTypeForLevel)
	{
		HashMap<ORef, ORefList> categoryRefToAssignmentRefsMap = new HashMap<ORef, ORefList>();
		for (int index = 0; index < assignmentRefsThatMatchThisNodeHierarchy.size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(getProject(), assignmentRefsThatMatchThisNodeHierarchy.get(index));
			ORefList refList = new ORefList(assignment);
			ORef categoryRef = assignment.getCategoryRef(objectTypeForLevel);
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
		
		String objectNameForType = getProject().getObjectManager().getInternalObjectTypeName(levelObjectType);
		return new UnspecifiedBaseObject(getProject().getObjectManager(), levelObjectType, objectNameForType);
	}

	private CodeList getLevelObjectTypes() throws Exception
	{
		return getCastedRowColumnProvider().getLevelTypeCodes();
	}

	private AbstractBudgetCategoryRowColumnProvider getCastedRowColumnProvider()
	{
		return ((AbstractBudgetCategoryRowColumnProvider) getRowColumnProvider());
	}
	
	@Override
	protected ORefList getChildRefs(ORef parentRef, DiagramObject diagram) throws Exception
	{
		return new ORefList();
	}
}
