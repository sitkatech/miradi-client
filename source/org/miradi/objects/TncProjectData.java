/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.TncOrganizationalPrioritiesQuestion;
import org.miradi.questions.TncProjectPlaceTypeQuestion;
import org.miradi.questions.TncProjectSharingQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class TncProjectData extends BaseObject
{
	public TncProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public TncProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	public CodeList getOrganizationalPriorityCodes()
	{
		return organizationalPriorities.getCodeList();
	}
	
	public CodeList getProjectPlaceTypeCodes()
	{
		return projectPlaceTypes.getCodeList();
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.TNC_PROJECT_DATA;
	}
	
	public boolean canShareOutsideOfTnc()
	{
		return projectSharingCode.get().equals(TncProjectSharingQuestion.SHARE_WITH_ANYONE);
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}

	public static TncProjectData find(ObjectManager objectManager, ORef tncProjectDataRef)
	{
		return (TncProjectData) objectManager.findObject(tncProjectDataRef);
	}
	
	public static TncProjectData find(Project project, ORef tncProjectDataRef)
	{
		return find(project.getObjectManager(), tncProjectDataRef);
	}
		
	@Override
	void clear()
	{
		super.clear();
		
		projectSharingCode = new StringData(TAG_PROJECT_SHARING_CODE);
		projectPlaceTypes = new CodeListData(TAG_PROJECT_PLACE_TYPES, getProject().getQuestion(TncProjectPlaceTypeQuestion.class));
		organizationalPriorities = new CodeListData(TAG_ORGANIZATIONAL_PRIORITIES, getProject().getQuestion(TncOrganizationalPrioritiesQuestion.class));
		parentChild = new StringData(TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		
		addField(projectSharingCode);
		addField(projectPlaceTypes);
		addField(organizationalPriorities);
		addField(parentChild);
	}
	
	public static final String OBJECT_NAME = "TncProjectData";

	public final static String TAG_PROJECT_SHARING_CODE = "ProjectSharingCode";
	public final static String TAG_PROJECT_PLACE_TYPES = "ProjectPlaceTypes";
	public final static String TAG_ORGANIZATIONAL_PRIORITIES = "OrganizationalPriorities";
	public final static String TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT = "ConProParentChildProjectText";
	
	public StringData projectSharingCode;
	public CodeListData projectPlaceTypes;
	public CodeListData organizationalPriorities;
	public StringData parentChild;
}
