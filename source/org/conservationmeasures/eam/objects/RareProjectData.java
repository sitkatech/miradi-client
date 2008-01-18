/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.IntegerData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class RareProjectData extends BaseObject
{
	public RareProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public RareProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	public RareProjectData(int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(new BaseId(idAsInt), jsonObject);
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
		return ObjectType.RARE_PROJECT_DATA;
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
		
		speciesCommonName = new StringData();
		speciesScientificName = new StringData();
		speciesDetail = new StringData();
		campaignSlogan = new StringData();
		campaignTheoryOfChange = new StringData();
		summaryOfKeyMessages = new StringData();
		biodiversityHotspots = new StringData();
		relatedProjects = new StringData();
		projectCode = new IntegerData();
		cohort = new StringData();
		numberOfCommunitiesInCampaignArea = new IntegerData();
		audience = new StringData();
		
		addField(TAG_FLAGSHIP_SPECIES_COMMON_NAME, speciesCommonName);
		addField(TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME, speciesScientificName);
		addField(TAG_FLAGSHIP_SPECIES_DETAIL, speciesDetail);
		addField(TAG_CAMPAIGN_SLOGAN, campaignSlogan);
		addField(TAG_CAMPAIGN_THEORY_OF_CHANGE, campaignTheoryOfChange);
		addField(TAG_SUMMARY_OF_KEY_MESSAGES, summaryOfKeyMessages);
		addField(TAG_BIODIVERSITY_HOTSPOTS, biodiversityHotspots);
		addField(TAG_RELATED_PROJECTS, relatedProjects);
		addField(TAG_PROJECT_CODE, projectCode);
		addField(TAG_COHORT, cohort);
		addField(TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA, numberOfCommunitiesInCampaignArea);
		addField(TAG_AUDIENCE, audience);

	}

	public static final String OBJECT_NAME = "RareProjectData";
	
	public static final String TAG_FLAGSHIP_SPECIES_COMMON_NAME = "FlagshipSpeciesCommonName";
	public static final String TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME = "FlagshipSpeciesScientificName";
	public static final String TAG_FLAGSHIP_SPECIES_DETAIL = "FlagshipSpeciesDetail";
	public static final String TAG_CAMPAIGN_SLOGAN = "CampaignSlogan";
	public static final String TAG_CAMPAIGN_THEORY_OF_CHANGE = "CampaignTheoryOfChange";
	public static final String TAG_SUMMARY_OF_KEY_MESSAGES = "SummaryOfKeyMessages";
	public static final String TAG_BIODIVERSITY_HOTSPOTS = "BiodiversityHotspots";
	public static final String TAG_RELATED_PROJECTS = "RelatedProjects";
	public static final String TAG_PROJECT_CODE = "ProjectCode";
	public static final String TAG_COHORT = "Cohort";
	public static final String TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA = "NumberOfCommunitiesInCampaignArea";
	public static final String TAG_AUDIENCE = "Audience";
	
	private StringData speciesCommonName;
	private StringData speciesScientificName;
	private StringData speciesDetail;
	private StringData campaignSlogan;
	private StringData campaignTheoryOfChange;
	private StringData summaryOfKeyMessages;
	private StringData biodiversityHotspots;
	private StringData relatedProjects;
	private IntegerData projectCode;
	private StringData cohort;
	private IntegerData numberOfCommunitiesInCampaignArea;
	private StringData audience;
}
