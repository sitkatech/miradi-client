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
import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.RollupReportsObjectTypeQuestion;
import org.miradi.utils.CodeList;

public class RollupReportsNode extends AbstractPlanningTreeNode
{
	public RollupReportsNode(Project project, CodeList visibleRows,	BaseObject nodeObjectToUse, CodeList levelObjectTypesToUse, int levelToUse, ORefList assignmentRefsThatMatchToUse) throws Exception
	{
		super(project, visibleRows);
		
		nodeObject = nodeObjectToUse;
		levelObjectTypes = levelObjectTypesToUse;
		currentLevel = levelToUse;
		assignmentRefsThatMatch = assignmentRefsThatMatchToUse;
		siblingAssignmentsThatMatch = new ORefList();
		
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
		
		String levelObjectTypeAsString = getLevelObjectTypes().get(getCurrentLevel());
		if (levelObjectTypeAsString.equals(RollupReportsObjectTypeQuestion.UNSPECIFIED_CODE))
			return;
		
		int levelObjectType = Integer.parseInt(levelObjectTypeAsString);
		ORefList refs = getProject().getPool(levelObjectType).getRefList();
		refs.add(ORef.INVALID);
		for (int index = 0; index < refs.size(); ++index)
		{	
			ORef ref = refs.get(index);
			BaseObject childBaseObject = createOrFindChildObject(ref, levelObjectType);
			ORefList referringAssignmentRefs = getReferringAssignmentRefs(childBaseObject, levelObjectType);
			ORefList overlapptingAssignmentRefs = referringAssignmentRefs.getOverlappingRefs(getAssignmentRefsThatMatch());
			siblingAssignmentsThatMatch.addAll(overlapptingAssignmentRefs);
			if (overlapptingAssignmentRefs.hasRefs())
				children.add(new RollupReportsNode(getProject(), getVisibleRows(), childBaseObject, getLevelObjectTypes(), childLevel, overlapptingAssignmentRefs));
		}
		
		Collections.sort(children, createNodeSorter());
	}

	private ORefList getReferringAssignmentRefs(BaseObject childBaseObject, int levelType)
	{
		if (childBaseObject.getRef().isValid())
			return childBaseObject.findObjectsThatReferToUs();
		
		ORefList allAsignmentRefs = new ORefList();
		allAsignmentRefs.addAll(getProject().getAssignmentPool().getRefList());
		allAsignmentRefs.addAll(getProject().getExpenseAssignmentPool().getRefList());
		ORefList referringToInvalidAssingments = new ORefList();
		for (int index = 0; index < allAsignmentRefs.size(); ++index)
		{
			Assignment assignment = Assignment.findAssignment(getProject(), allAsignmentRefs.get(index));
			ORefList refList = assignment.getReferencedObjects(levelType);
			if (refList.hasRefs() && !siblingAssignmentsThatMatch.contains(assignment.getRef()))
				referringToInvalidAssingments.add(assignment);
		}
		
		return referringToInvalidAssingments;
	}

	private BaseObject createOrFindChildObject(ORef ref, int levelObjectType)
	{
		if (ref.isValid())
			return BaseObject.find(getProject(), ref);
		
		return new UnspecifiedBaseObject(getProject().getObjectManager(), levelObjectType, getObjectTypeName());
	}

	private ORefList getAssignmentRefsThatMatch()
	{
		return assignmentRefsThatMatch;
	}

	private CodeList getLevelObjectTypes()
	{
		return levelObjectTypes;
	}

	private int getCurrentLevel()
	{
		return currentLevel;
	}
		
	private BaseObject nodeObject;
	private CodeList levelObjectTypes;
	private int currentLevel;
	private ORefList assignmentRefsThatMatch;
	private ORefList siblingAssignmentsThatMatch;
}
