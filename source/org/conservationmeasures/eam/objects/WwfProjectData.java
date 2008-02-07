/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.CodeListData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.CountriesQuestion;
import org.conservationmeasures.eam.questions.WwfEcoRegionsQuestion;
import org.conservationmeasures.eam.questions.WwfManagingOfficesQuestion;
import org.conservationmeasures.eam.questions.WwfRegionsQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class WwfProjectData extends BaseObject
{
	public WwfProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public WwfProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
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
		return ObjectType.WWF_PROJECT_DATA;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	void clear()
	{
		super.clear();
		
		managingOffices = new CodeListData(TAG_MANAGING_OFFICES, getQuestion(WwfManagingOfficesQuestion.class));
		regions = new CodeListData(TAG_REGIONS, getQuestion(WwfRegionsQuestion.class));
		countries = new CodeListData(TAG_COUNTRIES, getQuestion(CountriesQuestion.class));
		ecoRegions = new CodeListData(TAG_ECOREGIONS, getQuestion(WwfEcoRegionsQuestion.class));
		
		addField(TAG_MANAGING_OFFICES, managingOffices);
		addField(TAG_REGIONS, regions);
		addField(TAG_COUNTRIES, countries);
		addField(TAG_ECOREGIONS, ecoRegions);
	}

	public static final String TAG_MANAGING_OFFICES = "ManagingOffices";
	public static final String TAG_REGIONS = "Regions";
	public static final String TAG_COUNTRIES = "Countries";
	public static final String TAG_ECOREGIONS = "EcoRegions";
	
	public static final String OBJECT_NAME = "WwfProjectData";
	
	
	
	private CodeListData managingOffices;
	private CodeListData regions;
	private CodeListData countries;
	private CodeListData ecoRegions;
}
