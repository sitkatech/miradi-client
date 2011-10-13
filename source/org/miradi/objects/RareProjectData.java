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
import org.miradi.objectdata.IntegerData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

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
		return ObjectType.RARE_PROJECT_DATA;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static RareProjectData find(ObjectManager objectManager, ORef rareProjectDataRef)
	{
		return (RareProjectData) objectManager.findObject(rareProjectDataRef);
	}
	
	public static RareProjectData find(Project project, ORef rareProjectDataRef)
	{
		return find(project.getObjectManager(), rareProjectDataRef);
	}
	
	@Override
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
		legacyThreatsAddressedNotes = new StringData(LEGACY_TAG_THREATS_ADDRESSED_NOTES);
		mainActivitiesNotes = new StringData(TAG_MAIN_ACTIVITIES_NOTES);
		
		addField(TAG_FLAGSHIP_SPECIES_COMMON_NAME, speciesCommonName);
		addField(TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME, speciesScientificName);
		addField(TAG_FLAGSHIP_SPECIES_DETAIL, speciesDetail);
		addField(TAG_CAMPAIGN_SLOGAN, campaignSlogan);
		addField(TAG_CAMPAIGN_THEORY_OF_CHANGE, campaignTheoryOfChange);
		addField(TAG_SUMMARY_OF_KEY_MESSAGES, summaryOfKeyMessages);
		addField(TAG_BIODIVERSITY_HOTSPOTS, biodiversityHotspots);
		addField(TAG_COHORT, cohort);
		addField(TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA, numberOfCommunitiesInCampaignArea);
		addField(legacyThreatsAddressedNotes);
		addField(mainActivitiesNotes);
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
	public static final String LEGACY_TAG_THREATS_ADDRESSED_NOTES = "ThreatsAddressedNotes";
	public static final String TAG_MAIN_ACTIVITIES_NOTES = "MainActivitiesNotes";
	
	private StringData speciesCommonName;
	private StringData speciesScientificName;
	private StringData speciesDetail;
	private StringData campaignSlogan;
	private StringData campaignTheoryOfChange;
	private StringData summaryOfKeyMessages;
	private StringData biodiversityHotspots;
	private StringData cohort;
	private IntegerData numberOfCommunitiesInCampaignArea;
	private StringData legacyThreatsAddressedNotes;
	private StringData mainActivitiesNotes;
}
