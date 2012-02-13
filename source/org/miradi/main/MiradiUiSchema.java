/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.main;

import java.util.Arrays;
import java.util.HashSet;

import org.miradi.objects.BaseObject;

public class MiradiUiSchema
{
	public static boolean isSingleLineTextCell(BaseObject object, String tag)
	{
		if(!object.getField(tag).isUserText())
			return false;
		
		return !isMultiLineTextCell(object, tag);
	}
	
	public static boolean isMultiLineTextCell(BaseObject object, String tag)
	{
		if(!object.getField(tag).isUserText())
			return false;
		
		String thisField = Integer.toString(object.getType()) + "." + tag;
		boolean isMultiLine = getMultiLineFields().contains(thisField);
		return isMultiLine;
	}

	public static HashSet<String> getMultiLineFields()
	{
		return new HashSet<String>(Arrays.asList(new String[] {
			"3.Details", 
			"3.Comments",
			"6.SimpleThreatRatingComment",
			"6.Comment",
			"7.Comments",
			"8.MeasurementDetail",
			"8.FutureStatusDetail",
			"8.Detail",
			"8.Comments",
			"8.ViabilityRatingsComment",
			"8.FutureStatusComment",
			"8.ThresholdDetails",
			"9.FullText",
			"9.Comments",
			"10.FullText",
			"10.Comments",
			"11.ProjectStatus",
			"11.NextSteps",
			"11.ProjectScope",
			"11.ProjectVision",
			"11.ProjectDescription",
			"11.ProjectAreaNote",
			"11.ScopeComments",
			"11.LocationDetail",
			"11.LocationComments",
			"11.FinancialComments",
			"11.PlanningComments",
			"11.HumanPopulationNotes",
			"11.ProtectedAreaCategoryNotes",
			"11.TNC.LessonsLearned",
			"15.Comments",
			"16.Comments",
			"17.Description",
			"17.Details",
			"19.Detail",
			"20.Comments",
			"20.Text",
			"21.Comments",
			"21.Text",
			"22.Comments",
			"22.Text",
			"23.Comments",
			"23.Text",
			"24.Detail",
			"25.Comments",
			"25.Text",
			"26.Text",
			"32.Comments",
			"32.Detail",
			"33.Detail",
			"33.Comments",
			"36.Detail",
			"37.Details",
			"38.FlagshipSpeciesDetail",
			"38.CampaignSlogan",
			"38.CampaignTheoryOfChange",
			"38.SummaryOfKeyMessages",
			"38.MainActivitiesNotes",
			"40.ProjectResourcesScorecard",
			"40.ProjectLevelComments",
			"40.ProjectCitations",
			"40.CapStandardsScorecard",
			"42.Comments",
			"43.LegalStatus",
			"43.LegislativeContext",
			"43.PhysicalDescription",
			"43.BiologicalDescription",
			"43.SocioEconomicInformation",
			"43.HistoricalDescription",
			"43.CulturalDescription",
			"43.AccessInformation",
			"43.VisitationInformation",
			"43.CurrentLandUses",
			"43.ManagementResources",
			"45.PercentCompleteNotes",
			"46.Comments",
			"47.Comments",
			"49.ThreatRatingCommentsData",
			"50.Text",
			"50.Comments",
			"52.Text",
			"52.Comments",
			"55.Label",
			"55.Summary",
			"56.Comments",
			"57.Comments",
		}));

	}

}
