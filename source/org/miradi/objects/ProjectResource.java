/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import java.util.Collections;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.ProjectResourceId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.BaseObjectByFullNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.DoubleUtilities;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectResource extends BaseObject
{
	public ProjectResource(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, new ProjectResourceSchema());
	}
	
	public ProjectResource(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ProjectResourceId(idAsInt), json, new ProjectResourceSchema());
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

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}
	
	public boolean hasRole(String code) throws Exception
	{
		CodeList codeRoles = getCodeList(ProjectResource.TAG_ROLE_CODES);
		if (codeRoles.contains(code))
			return true;
		
		return false;
	}

	public CodeList getRoleCodes()
	{
		return getCodeListData(TAG_ROLE_CODES);
	}
	
	@Override
	public String getLabel()
	{
		return getWho();
	}
	
	@Override
	public String toString()
	{
		return getFullName();
	}
	
	public String getWho()
	{
		String result = getInitials();
		if(result.length() > 0)
			return result;
		
		result = getFullName();
		if(result.length() > 0)
			return result;
		
		result = getData(TAG_POSITION);
		if(result.length() > 0)
			return result;
		
		result = super.getLabel();
		if (result.length() > 0)
			return result;
		
		return EAM.text("Label|(Undefined Resource)");
	}

	public String getInitials()
	{
		return getData(TAG_INITIALS);
	}

	@Override
	public String getFullName()
	{
		String result = "";
		if(getInitials().length() > 0)
			result += getInitials() + ": ";
		result += (getGivenName() + " " + getSurName()).trim();
		
		return result;
	}
	
	public String getGivenName()
	{
		return getData(TAG_GIVEN_NAME);
	}
	
	public String getSurName()
	{
		return getData(TAG_SUR_NAME);
	}
	
	public String getEmail()
	{
		return getData(TAG_EMAIL);
	}

	public boolean isPerson()
	{
		return getData(TAG_RESOURCE_TYPE).length() == 0;
	}
	
	public boolean isTeamLead()
	{
		return hasRoleCode(ResourceRoleQuestion.TEAM_LEADER_CODE);
	}
	
	public boolean isTeamContact()
	{
		return hasRoleCode(ResourceRoleQuestion.CONTACT_CODE);
	}

	private boolean hasRoleCode(String code)
	{
		return getRoleCodes().contains(code);
	}
	
	public double getCostPerUnit() throws Exception
	{
		String costAsString = getData(TAG_COST_PER_UNIT);
		if (costAsString.length() == 0)
			return 0;
		
		return DoubleUtilities.toDoubleFromDataFormat(costAsString);
	}
	
	public static CodeList getSortedProjectResourceCodes(Project project, ORefSet resourceRefs)
	{
		Vector<ProjectResource> sortedProjectResources = new Vector<ProjectResource>();
		for(ORef projectResourceRef : resourceRefs)
		{
			ProjectResource projectResource = ProjectResource.find(project, projectResourceRef);
			sortedProjectResources.add(projectResource);
		}
		
		Collections.sort(sortedProjectResources, new BaseObjectByFullNameSorter());
		
		CodeList projectResourceCodes = new CodeList();
		for(ProjectResource resource : sortedProjectResources)
		{
			if (resource == null)
				projectResourceCodes.add(ORef.INVALID.toString());
			else
				projectResourceCodes.add(resource.getRef().toString());
		}
		
		return projectResourceCodes;
	}
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
		
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
			
	public static ProjectResource find(ObjectManager objectManager, ORef projectResourceRef)
	{
		return (ProjectResource) objectManager.findObject(projectResourceRef);
	}
	
	public static ProjectResource find(Project project, ORef projectResourceRef)
	{
		return find(project.getObjectManager(), projectResourceRef);
	}
	
	public static final String TAG_RESOURCE_TYPE = "ResourceType";
	public static final String TAG_INITIALS = "Initials";
	public static final String TAG_GIVEN_NAME = "Name";
	public static final String TAG_SUR_NAME = "SurName";
	public static final String TAG_POSITION = "Position";
	public static final String TAG_PHONE_NUMBER = "PhoneNumber";
	public static final String TAG_EMAIL = "Email";
	public static final String TAG_COST_PER_UNIT = "CostPerUnit";
	public static final String TAG_ORGANIZATION = "Organization";
	public static final String TAG_ROLE_CODES = "RoleCodes";
	public static final String TAG_COMMENTS = "Comments";
	
	public static final String TAG_LOCATION = "Location";
	public static final String TAG_PHONE_NUMBER_MOBILE = "PhoneNumberMobile";
	public static final String TAG_PHONE_NUMBER_HOME = "PhoneNumberHome";
	public static final String TAG_PHONE_NUMBER_OTHER = "PhoneNumberOther";
	public static final String TAG_ALTERNATIVE_EMAIL = "AlternativeEmail";
	public static final String TAG_IM_ADDRESS = "IMAddress";
	public static final String TAG_IM_SERVICE = "IMService";
	public static final String TAG_DATE_UPDATED = "DateUpdated";
	public static final String TAG_IS_CCN_COACH = "IsCcnCoach";
	
	public static final String TAG_CUSTOM_FIELD_1 = "Custom.Custom1";
	public static final String TAG_CUSTOM_FIELD_2 = "Custom.Custom2";

	public static final String OBJECT_NAME = "ProjectResource";
}
