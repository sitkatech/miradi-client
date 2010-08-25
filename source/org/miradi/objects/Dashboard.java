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

package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class Dashboard extends BaseObject
{
	public Dashboard(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		
		clear();
	}
		
	public Dashboard(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.DASHBOARD;
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TEAM_MEMBER_COUNT))
			return getObjectPoolCount(ProjectResource.getObjectType());
		
		if (fieldTag.equals(PSEUDO_PROJECT_SCOPE_WORD_COUNT))
			return getProjectScopeWordCount();
		
		if (fieldTag.equals(PSEUDO_TARGET_COUNT))
			return getObjectPoolCount(Target.getObjectType());
		
		if (fieldTag.equals(PSEUDO_TARGET_WITH_ASSIGNED_STANDARD_CLASSIFICATION_COUNT))
			return getTargetWithStandardClassificationCount();
		
		if (fieldTag.equals(PSEUDO_HUMAN_WELFARE_TARGET_COUNT))
			return getObjectPoolCount(HumanWelfareTarget.getObjectType());
		
		return super.getPseudoData(fieldTag);
	}
	
	private String getTargetWithStandardClassificationCount()
	{
		int targetWithStandardClassificationCount = 0;
		ORefSet targetRefs = getProject().getTargetPool().getRefSet();
		for (ORef targetRef : targetRefs)
		{
			Target target = Target.find(getProject(), targetRef);
			if (target.getData(Target.TAG_CURRENT_STATUS_JUSTIFICATION).length() > 0)
				++targetWithStandardClassificationCount;
		}
		
		return Integer.toString(targetWithStandardClassificationCount);
	}

	private String getObjectPoolCount(int objectType)
	{
		int resourceCount = getProject().getPool(objectType).size();
		return Integer.toString(resourceCount);
	}

	private String getProjectScopeWordCount()
	{
		int scopeCount = getProject().getMetadata().getProjectScope().length();
		return Integer.toString(scopeCount);
	}

	public static boolean is(BaseObject object)
	{
		return is(object.getRef());
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static Dashboard find(ObjectManager objectManager, ORef ref)
	{
		return (Dashboard) objectManager.findObject(ref);
	}
	
	public static Dashboard find(Project project, ORef ref)
	{
		return find(project.getObjectManager(), ref);
	}
	
	@Override
	void clear()
	{
		super.clear();
		
		teamMemberCount = new PseudoStringData(PSEUDO_TEAM_MEMBER_COUNT);
		projectScopeWordCount = new PseudoStringData(PSEUDO_PROJECT_SCOPE_WORD_COUNT);
		targetCount = new PseudoStringData(PSEUDO_TARGET_COUNT);
		targetWithAssignedStandardClassificationCount = new PseudoStringData(PSEUDO_TARGET_WITH_ASSIGNED_STANDARD_CLASSIFICATION_COUNT);
		humanWelfareTargetCount = new PseudoStringData(PSEUDO_HUMAN_WELFARE_TARGET_COUNT);
		
		addPresentationDataField(PSEUDO_TEAM_MEMBER_COUNT, teamMemberCount);
		addPresentationDataField(PSEUDO_PROJECT_SCOPE_WORD_COUNT, projectScopeWordCount);
		addPresentationDataField(PSEUDO_TARGET_COUNT, targetCount);
		addPresentationDataField(PSEUDO_TARGET_WITH_ASSIGNED_STANDARD_CLASSIFICATION_COUNT, targetWithAssignedStandardClassificationCount);
		addPresentationDataField(PSEUDO_HUMAN_WELFARE_TARGET_COUNT, humanWelfareTargetCount);
	}
	
	public static final String OBJECT_NAME = "Dashboard";
	
	public static final String PSEUDO_TEAM_MEMBER_COUNT = "TeamMemberCount";
	public static final String PSEUDO_PROJECT_SCOPE_WORD_COUNT = "ProjectScopeWordCount";
	public static final String PSEUDO_TARGET_COUNT = "TargetCount";
	public static final String PSEUDO_TARGET_WITH_ASSIGNED_STANDARD_CLASSIFICATION_COUNT = "TargetWithStandardClassificationCount";
	public static final String PSEUDO_HUMAN_WELFARE_TARGET_COUNT = "HumanWelfareTargetCount";
	
	private PseudoStringData teamMemberCount;
	private PseudoStringData projectScopeWordCount;
	private PseudoStringData targetCount;
	private PseudoStringData targetWithAssignedStandardClassificationCount;
	private PseudoStringData humanWelfareTargetCount;
}
