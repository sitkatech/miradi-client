/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
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
		
		speciesCommonName = new StringData(TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		speciesScientificName = new StringData(TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		speciesDetail = new StringData(TAG_FLAGSHIP_SPECIES_DETAIL);
		campaignSlogan = new StringData(TAG_CAMPAIGN_SLOGAN);
		campaignTheoryOfChange = new StringData(TAG_CAMPAIGN_THEORY_OF_CHANGE);
		summaryOfKeyMessages = new StringData(TAG_SUMMARY_OF_KEY_MESSAGES);
		biodiversityHotspots = new StringData(TAG_BIODIVERSITY_HOTSPOTS);
		cohort = new StringData(TAG_COHORT);
		numberOfCommunitiesInCampaignArea = new IntegerData(TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA);
		audience = new StringData(TAG_AUDIENCE);
		threatsAddressedNotes = new StringData(TAG_THREATS_ADDRESSED_NOTES);
		mainActivitiesNotes = new StringData(TAG_MAIN_ACTIVITIES_NOTES);
		threatReductionObjectiveNotes = new StringData(TAG_THREAT_REDUCTION_OBJECTIVE_NOTES);
		monitoringObjectiveNotes = new StringData(TAG_MONITORING_OBJECTIVE_NOTES);
		
		addField(TAG_FLAGSHIP_SPECIES_COMMON_NAME, speciesCommonName);
		addField(TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME, speciesScientificName);
		addField(TAG_FLAGSHIP_SPECIES_DETAIL, speciesDetail);
		addField(TAG_CAMPAIGN_SLOGAN, campaignSlogan);
		addField(TAG_CAMPAIGN_THEORY_OF_CHANGE, campaignTheoryOfChange);
		addField(TAG_SUMMARY_OF_KEY_MESSAGES, summaryOfKeyMessages);
		addField(TAG_BIODIVERSITY_HOTSPOTS, biodiversityHotspots);
		addField(TAG_COHORT, cohort);
		addField(TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA, numberOfCommunitiesInCampaignArea);
		addField(TAG_AUDIENCE, audience);
		addField(threatsAddressedNotes);
		addField(mainActivitiesNotes);
		addField(threatReductionObjectiveNotes);
		addField(monitoringObjectiveNotes);

	}

	public static final String OBJECT_NAME = "RareProjectData";
	
	public static final String TAG_FLAGSHIP_SPECIES_COMMON_NAME = "FlagshipSpeciesCommonName";
	public static final String TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME = "FlagshipSpeciesScientificName";
	public static final String TAG_FLAGSHIP_SPECIES_DETAIL = "FlagshipSpeciesDetail";
	public static final String TAG_CAMPAIGN_SLOGAN = "CampaignSlogan";
	public static final String TAG_CAMPAIGN_THEORY_OF_CHANGE = "CampaignTheoryOfChange";
	public static final String TAG_SUMMARY_OF_KEY_MESSAGES = "SummaryOfKeyMessages";
	public static final String TAG_BIODIVERSITY_HOTSPOTS = "BiodiversityHotspots";
	public static final String TAG_COHORT = "Cohort";
	public static final String TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA = "NumberOfCommunitiesInCampaignArea";
	public static final String TAG_AUDIENCE = "Audience";
	public static final String TAG_THREATS_ADDRESSED_NOTES = "ThreatsAddressedNotes";
	public static final String TAG_MAIN_ACTIVITIES_NOTES = "MainActivitiesNotes";
	public static final String TAG_THREAT_REDUCTION_OBJECTIVE_NOTES = "ThreatReductionObjectiveNotes";
	public static final String TAG_MONITORING_OBJECTIVE_NOTES = "MonitoringObjectiveNotes";
	
	private StringData speciesCommonName;
	private StringData speciesScientificName;
	private StringData speciesDetail;
	private StringData campaignSlogan;
	private StringData campaignTheoryOfChange;
	private StringData summaryOfKeyMessages;
	private StringData biodiversityHotspots;
	private StringData cohort;
	private IntegerData numberOfCommunitiesInCampaignArea;
	private StringData audience;
	private StringData threatsAddressedNotes;
	private StringData mainActivitiesNotes;
	private StringData threatReductionObjectiveNotes;
	private StringData monitoringObjectiveNotes;
}
