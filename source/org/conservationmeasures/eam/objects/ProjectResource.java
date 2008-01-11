/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ProjectResourceId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.CodeListData;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.NumberData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ProjectResource extends BaseObject
{
	public ProjectResource(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}
	
	public ProjectResource(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public ProjectResource(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ProjectResourceId(idAsInt), json);
	}

	public ProjectResource(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new ProjectResourceId(idAsInt), json);
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
	
	
	public static boolean canReferToThisType(int type)
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

	public String toString()
	{
		return getWho();
}
	
	public String getWho()
	{
		String result = initials.get();
		if(result.length() > 0)
			return result;
		
		result = getGivenName();
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
	
	String getGivenName()
	{
		return givenName.get();
	}

	public double getCostPerUnit()
	{
		if (costPerUnit.toString().length() == 0)
			return 0;
		return Double.parseDouble(costPerUnit.toString());
	}
	
	public String getCostUnit()
	{
		return costUnit.get();
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
		
		initials = new StringData();
		givenName = new StringData();
		position = new StringData();
		phoneNumber = new StringData();
		email = new StringData();
		costPerUnit = new NumberData();
		costUnit = new ChoiceData();
		organization = new StringData();
		roleCodes = new CodeListData();
		comments = new StringData();
		location = new StringData();
		phoneNumberMobile = new StringData();
		phoneNumberHome = new StringData();
		phoneNumberOther = new StringData();
		alternativeEmail = new StringData();
		iMAddress = new StringData();
		iMService = new StringData();
		dateUpdated = new DateData();

		addField(TAG_INITIALS, initials);
		addField(TAG_GIVEN_NAME, givenName);
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
		
		customUserField1 = new StringData();
		customUserField2 = new StringData();
		addField(TAG_CUSTOM_FIELD_1, customUserField1);
		addField(TAG_CUSTOM_FIELD_2, customUserField2);
		
	}
	
	public static final String TAG_INITIALS = "Initials";
	public static final String TAG_GIVEN_NAME = "Name";
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

	private StringData initials;
	private StringData givenName;
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
