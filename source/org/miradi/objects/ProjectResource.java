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

import java.util.Collections;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.ProjectResourceId;
import org.miradi.main.EAM;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.DateData;
import org.miradi.objectdata.NumberData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.BaseObjectByFullNameSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.BudgetCostUnitQuestion;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.ResourceTypeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectResource extends BaseObject
{
	public ProjectResource(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
	
	public ProjectResource(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ProjectResourceId(idAsInt), json);
	}

	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.PROJECT_RESOURCE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
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
		return roleCodes.getCodeList();
	}
	
	public String getLabel()
	{
		return getWho();
	}
	
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
		
		result = position.get();
		if(result.length() > 0)
			return result;
		
		result = label.get();
		if (result.length() > 0)
			return result;
		
		return EAM.text("Label|(Undefined Resource)");
	}

	public String getInitials()
	{
		return initials.get();
	}

	public String getFullName()
	{
		String result = "";
		if(getInitials().length() > 0)
			result += getInitials() + ": ";
		result += (givenName.get() + " " + surName.get()).trim();
		
		return result;
	}
	
	public String getGivenName()
	{
		return givenName.get();
	}

	public boolean isPerson()
	{
		return getData(TAG_RESOURCE_TYPE).length() == 0;
	}
	
	public boolean isTeamLead()
	{
		return getRoleCodes().contains(ResourceRoleQuestion.TeamLeaderCode);
	}
	
	public double getCostPerUnit()
	{
		if (costPerUnit.toString().length() == 0)
			return 0;
		return Double.parseDouble(costPerUnit.toString());
	}
	
	private String getCostUnitCode()
	{
		return costUnit.get();
	}
	
	public String getCostUnitValue()
	{
		ChoiceQuestion question = getProject().getQuestion(BudgetCostUnitQuestion.class);
		return question.findChoiceByCode(getCostUnitCode()).getLabel();
	}
	
	public static String getResourcesAsString(Project project, ORefSet resourceRefs)
	{
		Vector<ProjectResource> sortedProjectResources = new Vector();
		for(ORef projectResourceRef : resourceRefs)
		{
			if(projectResourceRef.isInvalid())
				continue;
			ProjectResource projectResource = ProjectResource.find(project, projectResourceRef);
			sortedProjectResources.add(projectResource);
		}
		Collections.sort(sortedProjectResources, new BaseObjectByFullNameSorter());
		
		boolean isFirstIteration = true; 
		String appendedResources = "";
		for(ProjectResource resource : sortedProjectResources)
		{
			if (resource == null)
				continue;
			
			if (!isFirstIteration)
				appendedResources += ", ";
					
			appendedResources += resource.getWho();
			isFirstIteration = false;
		}
		
		return appendedResources;
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
	
	public void clear()
	{
		super.clear();
		
		resourceType = new ChoiceData(TAG_RESOURCE_TYPE, getQuestion(ResourceTypeQuestion.class));
		initials = new StringData(TAG_INITIALS);
		givenName = new StringData(TAG_GIVEN_NAME);
		surName = new StringData(TAG_SUR_NAME);
		position = new StringData(TAG_POSITION);
		phoneNumber = new StringData(TAG_PHONE_NUMBER);
		email = new StringData(TAG_EMAIL);
		costPerUnit = new NumberData(TAG_COST_PER_UNIT);
		costUnit = new ChoiceData(TAG_COST_UNIT, getQuestion(BudgetCostUnitQuestion.class));
		organization = new StringData(TAG_ORGANIZATION);
		roleCodes = new CodeListData(TAG_ROLE_CODES, getQuestion(ResourceRoleQuestion.class));
		comments = new StringData(TAG_COMMENTS);
		location = new StringData(TAG_LOCATION);
		phoneNumberMobile = new StringData(TAG_PHONE_NUMBER_MOBILE);
		phoneNumberHome = new StringData(TAG_PHONE_NUMBER_HOME);
		phoneNumberOther = new StringData(TAG_PHONE_NUMBER_OTHER);
		alternativeEmail = new StringData(TAG_ALTERNATIVE_EMAIL);
		iMAddress = new StringData(TAG_IM_ADDRESS);
		iMService = new StringData(TAG_IM_SERVICE);
		dateUpdated = new DateData(TAG_DATE_UPDATED);

		addField(TAG_RESOURCE_TYPE, resourceType);
		addField(TAG_INITIALS, initials);
		addField(TAG_GIVEN_NAME, givenName);
		addField(TAG_SUR_NAME, surName);
		addField(TAG_ORGANIZATION, organization);
		addField(TAG_POSITION, position);
		addField(TAG_PHONE_NUMBER, phoneNumber);
		addField(TAG_EMAIL, email);
		addField(TAG_COST_UNIT, costUnit);
		addField(TAG_COST_PER_UNIT, costPerUnit);
		addField(TAG_ROLE_CODES, roleCodes);
		addField(TAG_COMMENTS, comments);
		addField(TAG_LOCATION, location);
		addField(TAG_PHONE_NUMBER_MOBILE, phoneNumberMobile);
		addField(TAG_PHONE_NUMBER_HOME, phoneNumberHome);
		addField(TAG_PHONE_NUMBER_OTHER, phoneNumberOther);
		addField(TAG_ALTERNATIVE_EMAIL, alternativeEmail);
		addField(TAG_IM_ADDRESS, iMAddress);
		addField(TAG_IM_SERVICE, iMService);
		addField(TAG_DATE_UPDATED, dateUpdated);
		
		customUserField1 = new StringData(TAG_CUSTOM_FIELD_1);
		customUserField2 = new StringData(TAG_CUSTOM_FIELD_2);
		addField(TAG_CUSTOM_FIELD_1, customUserField1);
		addField(TAG_CUSTOM_FIELD_2, customUserField2);
		
	}
	
	public static final String TAG_RESOURCE_TYPE = "ResourceType";
	public static final String TAG_INITIALS = "Initials";
	public static final String TAG_GIVEN_NAME = "Name";
	public static final String TAG_SUR_NAME = "SurName";
	public static final String TAG_POSITION = "Position";
	public static final String TAG_PHONE_NUMBER = "PhoneNumber";
	public static final String TAG_EMAIL = "Email";
	public static final String TAG_COST_PER_UNIT = "CostPerUnit";
	public static final String TAG_COST_UNIT = "CostUnit";
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
	
	public static final String TAG_CUSTOM_FIELD_1 = "Custom.Custom1";
	public static final String TAG_CUSTOM_FIELD_2 = "Custom.Custom2";

	static final String OBJECT_NAME = "ProjectResource";

	private ChoiceData resourceType;
	private StringData initials;
	private StringData givenName;
	private StringData surName;
	private StringData position;
	private StringData phoneNumber;
	private StringData email;
	private NumberData costPerUnit;
	private ChoiceData costUnit;
	private StringData organization;
	private CodeListData roleCodes;
	private StringData comments;
	private StringData location;
	private StringData phoneNumberMobile;
	private StringData phoneNumberHome;
	private StringData phoneNumberOther;
	private StringData alternativeEmail;
	private StringData iMAddress;
	private StringData iMService;
	private DateData dateUpdated;
	
	private StringData customUserField1;
	private StringData customUserField2;
}
