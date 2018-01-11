/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.RareProjectDataSchema;

public class RareProjectData extends BaseObject
{
	public RareProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema(objectManager));
	}

	public static RareProjectDataSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static RareProjectDataSchema createSchema(ObjectManager objectManager)
	{
		return (RareProjectDataSchema) objectManager.getSchemas().get(ObjectType.RARE_PROJECT_DATA);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
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
}
