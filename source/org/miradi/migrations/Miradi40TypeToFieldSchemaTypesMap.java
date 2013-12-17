/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.migrations;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.miradi.utils.Utility;

public final class Miradi40TypeToFieldSchemaTypesMap extends HashMap<Integer, HashMap<String, String>>
{
	private Miradi40TypeToFieldSchemaTypesMap()
	{
		fillMap();
	}
	
	public static Vector<String> getFieldTags(int objectType)
	{
		Vector<String> fieldTagsForType = new Vector<String>();
		HashMap<String, String> tagToFieldType = new Miradi40TypeToFieldSchemaTypesMap().get(objectType);
		fieldTagsForType.addAll(tagToFieldType.keySet());
		Collections.sort(fieldTagsForType);

		return fieldTagsForType;
	}
	
	public static boolean isUserTextData(final int objectType, final String fieldTag)
	{
		return new Miradi40TypeToFieldSchemaTypesMap().isUserTextField(objectType, fieldTag);
	}
	
	public static boolean isCodeToUserStringMapData(int objectType, String fieldTag)
	{
		return new Miradi40TypeToFieldSchemaTypesMap().isCodeToUserStringMapField(objectType, fieldTag);
	}
	
	public static boolean isNumericData(int objectType, String fieldTag)
	{
		return new Miradi40TypeToFieldSchemaTypesMap().isNumerField(objectType, fieldTag);
	}
	
	public static boolean hasType(int objectType)
	{
		return new Miradi40TypeToFieldSchemaTypesMap().containsKey(objectType);
	}
	
	public static boolean hasField(int objectType, String tag)
	{
		return new Miradi40TypeToFieldSchemaTypesMap().hasFieldForType(objectType, tag);
	}
	
	public static boolean isIdField(int objectType, String tag)
	{
		return new Miradi40TypeToFieldSchemaTypesMap().isIdFieldType(objectType, tag);
	}
	
	public static boolean isRefField(int objectType, String tag)
	{
		return new Miradi40TypeToFieldSchemaTypesMap().isRefFieldType(objectType, tag);
	}

	private void fillMap()
	{
		HashMap<String, String> fieldsForType1 = new HashMap<String, String>();
		fieldsForType1.put("Label", EXPANDING_USER_TEXT_DATA);
		put(1, fieldsForType1);

		HashMap<String, String> fieldsForType2 = new HashMap<String, String>();
		fieldsForType2.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType2.put("Numeric", INTEGER_DATA);
		fieldsForType2.put("Color", INTEGER_DATA);
		put(2, fieldsForType2);

		HashMap<String, String> fieldsForType3 = new HashMap<String, String>();
		fieldsForType3.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType3.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType3.put("Details", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType3.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType3.put("SubtaskIds", "IdListData");
		fieldsForType3.put("LeaderResource", OREF_DATA);
		fieldsForType3.put("AssignmentIds", "IdListData");
		fieldsForType3.put("ExpenseRefs", "RefListData");
		fieldsForType3.put("ProgressReportRefs", "RefListData");
		put(3, fieldsForType3);

		HashMap<String, String> fieldsForType5 = new HashMap<String, String>();
		fieldsForType5.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType5.put("CurrentMode", "ChoiceData");
		fieldsForType5.put("DiagramHiddenTypes", "CodeListData");
		fieldsForType5.put("BudgetRollupReportTypes", "CodeListData");
		fieldsForType5.put("SingleLevelChoice", "ChoiceData");
		fieldsForType5.put("CustomPlanRef", OREF_DATA);
		fieldsForType5.put("ActionTreeConfigurationChoice", "ChoiceData");
		fieldsForType5.put("MonitoringTreeConfigurationChoice", "ChoiceData");
		fieldsForType5.put("CurrentWizardStep", "CodeData");
		fieldsForType5.put("CurrentTab", INTEGER_DATA);
		fieldsForType5.put("CurrentConceptualModelRef", OREF_DATA);
		fieldsForType5.put("CurrentResultsChainRef", OREF_DATA);
		fieldsForType5.put("ChainModeFactorRefs", "RefListData");
		put(5, fieldsForType5);

		HashMap<String, String> fieldsForType6 = new HashMap<String, String>();
		fieldsForType6.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType6.put("FromRef", OREF_DATA);
		fieldsForType6.put("ToRef", OREF_DATA);
		fieldsForType6.put("BidirectionalLink", "BooleanData");
		put(6, fieldsForType6);

		HashMap<String, String> fieldsForType7 = new HashMap<String, String>();
		fieldsForType7.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType7.put("ResourceType", "ChoiceData");
		fieldsForType7.put("Initials", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("Name", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("SurName", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("Position", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("PhoneNumber", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("Email", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("CostPerUnit", NUMBER_DATA);
		fieldsForType7.put("Organization", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("RoleCodes", "CodeListData");
		fieldsForType7.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType7.put("Location", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("PhoneNumberMobile", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("PhoneNumberHome", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("PhoneNumberOther", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("AlternativeEmail", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("IMAddress", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("IMService", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("DateUpdated", "DateData");
		fieldsForType7.put("IsCcnCoach", "BooleanData");
		fieldsForType7.put("Custom.Custom1", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType7.put("Custom.Custom2", SINGLE_LINE_USER_TEXT_DATA);
		put(7, fieldsForType7);

		HashMap<String, String> fieldsForType8 = new HashMap<String, String>();
		fieldsForType8.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType8.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType8.put("Priority", "ChoiceData");
		fieldsForType8.put("TaskIds", "IdListData");
		fieldsForType8.put("IndicatorThresholds", CODE_TO_USER_STRING_MAP_DATA);
		fieldsForType8.put("ThresholdDetails", CODE_TO_USER_STRING_MAP_DATA);
		fieldsForType8.put("RatingSource", "ChoiceData");
		fieldsForType8.put("MeasurementRefs", "RefListData");
		fieldsForType8.put("Detail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType8.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType8.put("ViabilityRatingsComment", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType8.put("FutureStatusRating", "ChoiceData");
		fieldsForType8.put("FutureStatusDate", "DateData");
		fieldsForType8.put("FutureStatusSummary", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType8.put("FutureStatusDetail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType8.put("FutureStatusComment", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType8.put("LeaderResource", OREF_DATA);
		fieldsForType8.put("AssignmentIds", "IdListData");
		fieldsForType8.put("ExpenseRefs", "RefListData");
		fieldsForType8.put("ProgressReportRefs", "RefListData");
		put(8, fieldsForType8);

		HashMap<String, String> fieldsForType9 = new HashMap<String, String>();
		fieldsForType9.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType9.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType9.put("FullText", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType9.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType9.put("RelevantIndicatorSet", "RelevancyOverrideSetData");
		fieldsForType9.put("RelevantStrategySet", "RelevancyOverrideSetData");
		fieldsForType9.put("ProgressPrecentRefs", "RefListData");
		put(9, fieldsForType9);

		HashMap<String, String> fieldsForType10 = new HashMap<String, String>();
		fieldsForType10.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType10.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType10.put("FullText", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType10.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType10.put("RelevantIndicatorSet", "RelevancyOverrideSetData");
		fieldsForType10.put("RelevantStrategySet", "RelevancyOverrideSetData");
		fieldsForType10.put("ProgressPrecentRefs", "RefListData");
		put(10, fieldsForType10);

		HashMap<String, String> fieldsForType11 = new HashMap<String, String>();
		fieldsForType11.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType11.put("ProjectName", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ProjectLanguage", "ChoiceData");
		fieldsForType11.put("ProjectDescription", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ProjectURL", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ProjectScope", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ShortProjectScope", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ScopeComments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ProjectVision", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("StartDate", "DateData");
		fieldsForType11.put("ExpectedEndDate", "DateData");
		fieldsForType11.put("DataEffectiveDate", "DateData");
		fieldsForType11.put("CurrencyDecimalPlaces", INTEGER_DATA);
		fieldsForType11.put("CurrencyType", "ChoiceData");
		fieldsForType11.put("CurrencySymbol", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("TotalBudgetForFunding", NUMBER_DATA);
		fieldsForType11.put("BudgetSecuredPercent", PERCENTAGE_DATA);
		fieldsForType11.put("FiscalYearStart", "ChoiceData");
		fieldsForType11.put("FullTimeEmployeeDaysPerYear", NUMBER_DATA);
		fieldsForType11.put("QuarterColumnsVisibility", "ChoiceData");
		fieldsForType11.put("PlanningTreeTargetNodePosition", "ChoiceData");
		fieldsForType11.put("ProjectLatitude", FLOAT_DATA);
		fieldsForType11.put("ProjectLongitude", FLOAT_DATA);
		fieldsForType11.put("ProjectArea", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ProjectAreaNote", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("Countries", "CodeListData");
		fieldsForType11.put("StateAndProvinces", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("Municipalities", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("LegislativeDistricts", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("LocationDetail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("LocationComments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("KeyFundingSources", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("FinancialComments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("WorkPlanStartDate", "DateData");
		fieldsForType11.put("WorkPlanEndDate", "DateData");
		fieldsForType11.put("WorkPlanTimeUnit", "ChoiceData");
		fieldsForType11.put("PlanningComments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("HumanPopulation", INTEGER_DATA);
		fieldsForType11.put("HumanPopulationNotes", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("SocialContext", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("SiteMapReference", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ProtectedAreaCategories", "CodeListData");
		fieldsForType11.put("ProtectedAreaCategoryNotes", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("ProjectStatus", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("NextSteps", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("CurrentWizardScreenName", "CodeData");
		fieldsForType11.put("TNC.LessonsLearned", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("TNC.WorkbookVersionNumber", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("TNC.WorkbookVersionDate", "DateData");
		fieldsForType11.put("TNC.DatabaseDownloadDate", "DateData");
		fieldsForType11.put("TNC.PlanningTeamComment", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType11.put("TNC.OperatingUnitList", "CodeListData");
		fieldsForType11.put("TNC.TerrestrialEcoRegion", "CodeListData");
		fieldsForType11.put("TNC.MarineEcoRegion", "CodeListData");
		fieldsForType11.put("TNC.FreshwaterEcoRegion", "CodeListData");
		fieldsForType11.put("OtherOrgProjectNumber", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("OtherOrgRelatedProjects", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType11.put("DiagramFontSize", "ChoiceData");
		fieldsForType11.put("DiagramFontFamily", "ChoiceData");
		fieldsForType11.put("ThreatRatingMode", "ChoiceData");
		fieldsForType11.put("XenodataRefs", "StringRefMapData");
		fieldsForType11.put("TargetMode", "ChoiceData");
		fieldsForType11.put("WorkPlanDiagramDataInclusion", "ChoiceData");
		put(11, fieldsForType11);

		HashMap<String, String> fieldsForType13 = new HashMap<String, String>();
		fieldsForType13.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType13.put("WrappedLinkId", BASE_ID_DATA);
		fieldsForType13.put("FromDiagramFactorId", BASE_ID_DATA);
		fieldsForType13.put("ToDiagramFactorId", BASE_ID_DATA);
		fieldsForType13.put("BendPoints", "PointListData");
		fieldsForType13.put("GroupedDiagramLinkRefs", "RefListData");
		fieldsForType13.put("Color", "ChoiceData");
		fieldsForType13.put("IsBidirectionalLink", "BooleanData");
		put(13, fieldsForType13);

		HashMap<String, String> fieldsForType14 = new HashMap<String, String>();
		fieldsForType14.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType14.put("Details", "DateUnitEffortListData");
		fieldsForType14.put("CategoryOneRef", OREF_DATA);
		fieldsForType14.put("CategoryTwoRef", OREF_DATA);
		fieldsForType14.put("ResourceId", BASE_ID_DATA);
		fieldsForType14.put("AccountingCode", BASE_ID_DATA);
		fieldsForType14.put("FundingSource", BASE_ID_DATA);
		put(14, fieldsForType14);

		HashMap<String, String> fieldsForType15 = new HashMap<String, String>();
		fieldsForType15.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType15.put("Code", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType15.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(15, fieldsForType15);

		HashMap<String, String> fieldsForType16 = new HashMap<String, String>();
		fieldsForType16.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType16.put("Code", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType16.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(16, fieldsForType16);

		HashMap<String, String> fieldsForType17 = new HashMap<String, String>();
		fieldsForType17.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType17.put("IndicatorIds", "IdListData");
		fieldsForType17.put("Description", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType17.put("Details", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType17.put("KeyEcologicalAttributeType", "ChoiceData");
		fieldsForType17.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		put(17, fieldsForType17);

		HashMap<String, String> fieldsForType18 = new HashMap<String, String>();
		fieldsForType18.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType18.put("Size", "DimensionData");
		fieldsForType18.put("Location", "DiagramPointData");
		fieldsForType18.put("WrappedFactorRef", OREF_DATA);
		fieldsForType18.put("FontSize", "ChoiceData");
		fieldsForType18.put("FontColor", "ChoiceData");
		fieldsForType18.put("FontStyle", "ChoiceData");
		fieldsForType18.put("GroupBoxChildrenRefs", "RefListData");
		fieldsForType18.put("BackgroundColor", "ChoiceData");
		fieldsForType18.put("TextBoxZOrderCode", "ChoiceData");
		put(18, fieldsForType18);

		HashMap<String, String> fieldsForType19 = new HashMap<String, String>();
		fieldsForType19.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType19.put("DiagramFactorIds", "IdListData");
		fieldsForType19.put("DiagramFactorLinkIds", "IdListData");
		fieldsForType19.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType19.put("Detail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType19.put("HiddenTypes", "CodeListData");
		fieldsForType19.put("SelectedTaggedObjectSetRefs", "RefListData");
		fieldsForType19.put("ZoomScale", NUMBER_DATA);
		put(19, fieldsForType19);

		HashMap<String, String> fieldsForType20 = new HashMap<String, String>();
		fieldsForType20.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType20.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType20.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType20.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType20.put("TaxonomyCode", "ChoiceData");
		fieldsForType20.put("IsDirectThreat", "BooleanData");
		fieldsForType20.put("IndicatorIds", "IdListData");
		fieldsForType20.put("ObjectiveIds", "IdListData");
		put(20, fieldsForType20);

		HashMap<String, String> fieldsForType21 = new HashMap<String, String>();
		fieldsForType21.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType21.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType21.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType21.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType21.put("Status", "ChoiceData");
		fieldsForType21.put("ActivityIds", "IdListData");
		fieldsForType21.put("TaxonomyCode", "ChoiceData");
		fieldsForType21.put("ImpactRating", "ChoiceData");
		fieldsForType21.put("FeasibilityRating", "ChoiceData");
		fieldsForType21.put("LegacyTncStrategyRanking", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType21.put("LeaderResource", OREF_DATA);
		fieldsForType21.put("AssignmentIds", "IdListData");
		fieldsForType21.put("ExpenseRefs", "RefListData");
		fieldsForType21.put("ProgressReportRefs", "RefListData");
		fieldsForType21.put("IndicatorIds", "IdListData");
		fieldsForType21.put("ObjectiveIds", "IdListData");
		put(21, fieldsForType21);

		HashMap<String, String> fieldsForType22 = new HashMap<String, String>();
		fieldsForType22.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType22.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType22.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType22.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType22.put("TargetStatus", "ChoiceData");
		fieldsForType22.put("ViabilityMode", "ChoiceData");
		fieldsForType22.put("CurrentStatusJustification", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType22.put("SubTargetRefs", "RefListData");
		fieldsForType22.put("GoalIds", "IdListData");
		fieldsForType22.put("KeyEcologicalAttributeIds", "IdListData");
		fieldsForType22.put("IndicatorIds", "IdListData");
		fieldsForType22.put("StressRefs", "RefListData");
		fieldsForType22.put("SpeciesLatinName", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType22.put("HabitatAssociation", "CodeListData");
		put(22, fieldsForType22);

		HashMap<String, String> fieldsForType23 = new HashMap<String, String>();
		fieldsForType23.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType23.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType23.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType23.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType23.put("IndicatorIds", "IdListData");
		fieldsForType23.put("ObjectiveIds", "IdListData");
		put(23, fieldsForType23);

		HashMap<String, String> fieldsForType24 = new HashMap<String, String>();
		fieldsForType24.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType24.put("DiagramFactorIds", "IdListData");
		fieldsForType24.put("DiagramFactorLinkIds", "IdListData");
		fieldsForType24.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType24.put("Detail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType24.put("HiddenTypes", "CodeListData");
		fieldsForType24.put("SelectedTaggedObjectSetRefs", "RefListData");
		fieldsForType24.put("ZoomScale", NUMBER_DATA);
		put(24, fieldsForType24);

		HashMap<String, String> fieldsForType25 = new HashMap<String, String>();
		fieldsForType25.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType25.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType25.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType25.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType25.put("RelatedDirectThreatRef", OREF_DATA);
		fieldsForType25.put("IndicatorIds", "IdListData");
		fieldsForType25.put("ObjectiveIds", "IdListData");
		put(25, fieldsForType25);

		HashMap<String, String> fieldsForType26 = new HashMap<String, String>();
		fieldsForType26.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType26.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType26.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType26.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		put(26, fieldsForType26);

		HashMap<String, String> fieldsForType29 = new HashMap<String, String>();
		fieldsForType29.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType29.put("TagRowConfiguration", "CodeListData");
		fieldsForType29.put("TagColConfiguration", "CodeListData");
		fieldsForType29.put("TagDiagramDataInclusion", "ChoiceData");
		fieldsForType29.put("StrategyObjectiveOrder", "ChoiceData");
		fieldsForType29.put("TargetNodePosition", "ChoiceData");
		put(29, fieldsForType29);

		HashMap<String, String> fieldsForType30 = new HashMap<String, String>();
		fieldsForType30.put("ManagingOffices", "CodeListData");
		fieldsForType30.put("Regions", "CodeListData");
		fieldsForType30.put("EcoRegions", "CodeListData");
		put(30, fieldsForType30);

		HashMap<String, String> fieldsForType31 = new HashMap<String, String>();
		fieldsForType31.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType31.put("ParentRef", OREF_DATA);
		fieldsForType31.put("ChildRef", OREF_DATA);
		fieldsForType31.put("CostPercentage", INTEGER_DATA);
		put(31, fieldsForType31);

		HashMap<String, String> fieldsForType32 = new HashMap<String, String>();
		fieldsForType32.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType32.put("Trend", "ChoiceData");
		fieldsForType32.put("Status", "ChoiceData");
		fieldsForType32.put("Date", "DateData");
		fieldsForType32.put("Summary", EXPANDING_USER_TEXT_DATA);
		fieldsForType32.put("Detail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType32.put("StatusConfidence", "ChoiceData");
		fieldsForType32.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(32, fieldsForType32);

		HashMap<String, String> fieldsForType33 = new HashMap<String, String>();
		fieldsForType33.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType33.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType33.put("Detail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType33.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType33.put("Scope", "ChoiceData");
		fieldsForType33.put("Severity", "ChoiceData");
		put(33, fieldsForType33);

		HashMap<String, String> fieldsForType34 = new HashMap<String, String>();
		fieldsForType34.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType34.put("Contribution", "ChoiceData");
		fieldsForType34.put("Irreversibility", "ChoiceData");
		fieldsForType34.put("StressRef", OREF_DATA);
		fieldsForType34.put("ThreatRef", OREF_DATA);
		fieldsForType34.put("IsActive", "BooleanData");
		put(34, fieldsForType34);

		HashMap<String, String> fieldsForType35 = new HashMap<String, String>();
		fieldsForType35.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType35.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType35.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType35.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		put(35, fieldsForType35);

		HashMap<String, String> fieldsForType36 = new HashMap<String, String>();
		fieldsForType36.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType36.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType36.put("Detail", MULTI_LINE_USER_TEXT_DATA);
		put(36, fieldsForType36);

		HashMap<String, String> fieldsForType37 = new HashMap<String, String>();
		fieldsForType37.put("ProgressStatus", "ChoiceData");
		fieldsForType37.put("ProgressDate", "DateData");
		fieldsForType37.put("Details", MULTI_LINE_USER_TEXT_DATA);
		put(37, fieldsForType37);

		HashMap<String, String> fieldsForType38 = new HashMap<String, String>();
		fieldsForType38.put("FlagshipSpeciesCommonName", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType38.put("FlagshipSpeciesScientificName", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType38.put("FlagshipSpeciesDetail", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType38.put("CampaignSlogan", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType38.put("CampaignTheoryOfChange", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType38.put("SummaryOfKeyMessages", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType38.put("BiodiversityHotspots", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType38.put("Cohort", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType38.put("NumberOfCommunitiesInCampaignArea", INTEGER_DATA);
		fieldsForType38.put("ThreatsAddressedNotes", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType38.put("MainActivitiesNotes", MULTI_LINE_USER_TEXT_DATA);
		put(38, fieldsForType38);

		HashMap<String, String> fieldsForType39 = new HashMap<String, String>();
		fieldsForType39.put("OrganizationalFocus", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType39.put("OrganizationalLevel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType39.put("SwotCompleted", "BooleanData");
		fieldsForType39.put("SwotUrl", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType39.put("StepCompleted", "BooleanData");
		fieldsForType39.put("StepUrl", SINGLE_LINE_USER_TEXT_DATA);
		put(39, fieldsForType39);

		HashMap<String, String> fieldsForType40 = new HashMap<String, String>();
		fieldsForType40.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType40.put("ProjectSharingCode", "ChoiceData");
		fieldsForType40.put("ProjectPlaceTypes", "CodeListData");
		fieldsForType40.put("OrganizationalPriorities", "CodeListData");
		fieldsForType40.put("ConProParentChildProjectText", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType40.put("ProjectResourcesScorecard", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType40.put("ProjectLevelComments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType40.put("ProjectCitations", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType40.put("CapStandardsScorecard", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType40.put("MakingTheCase", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType40.put("Risks", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType40.put("CapacityAndFunding", MULTI_LINE_USER_TEXT_DATA);
		put(40, fieldsForType40);

		HashMap<String, String> fieldsForType41 = new HashMap<String, String>();
		fieldsForType41.put("TrainingType", "ChoiceData");
		fieldsForType41.put("TrainingDates", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType41.put("Trainers", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType41.put("Coaches", SINGLE_LINE_USER_TEXT_DATA);
		put(41, fieldsForType41);

		HashMap<String, String> fieldsForType42 = new HashMap<String, String>();
		fieldsForType42.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType42.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType42.put("RolesDescription", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType42.put("ContactFirstName", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType42.put("ContactLastName", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType42.put("Email", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType42.put("PhoneNumber", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType42.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(42, fieldsForType42);

		HashMap<String, String> fieldsForType43 = new HashMap<String, String>();
		fieldsForType43.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType43.put("LegalStatus", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("LegislativeContext", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("PhysicalDescription", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("BiologicalDescription", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("SocioEconomicInformation", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("HistoricalDescription", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("CulturalDescription", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("AccessInformation", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("VisitationInformation", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("CurrentLandUses", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType43.put("ManagementResources", MULTI_LINE_USER_TEXT_DATA);
		put(43, fieldsForType43);

		HashMap<String, String> fieldsForType44 = new HashMap<String, String>();
		fieldsForType44.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType44.put("ProjectId", SINGLE_LINE_USER_TEXT_DATA);
		put(44, fieldsForType44);

		HashMap<String, String> fieldsForType45 = new HashMap<String, String>();
		fieldsForType45.put("PercentDate", "DateData");
		fieldsForType45.put("PercentComplete", PERCENTAGE_DATA);
		fieldsForType45.put("PercentCompleteNotes", MULTI_LINE_USER_TEXT_DATA);
		put(45, fieldsForType45);

		HashMap<String, String> fieldsForType46 = new HashMap<String, String>();
		fieldsForType46.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType46.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType46.put("IncludeSectionCodes", "CodeListData");
		fieldsForType46.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(46, fieldsForType46);

		HashMap<String, String> fieldsForType47 = new HashMap<String, String>();
		fieldsForType47.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType47.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType47.put("TaggedObjectRefs", "RefListData");
		fieldsForType47.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(47, fieldsForType47);

		HashMap<String, String> fieldsForType48 = new HashMap<String, String>();
		fieldsForType48.put("TableIdentifier", "CodeData");
		fieldsForType48.put("RowHeight", INTEGER_DATA);
		fieldsForType48.put("DateUnitListData", "DateUnitListData");
		fieldsForType48.put("TagTableSettingsMap", "CodeToCodeListMapData");
		fieldsForType48.put("WorkPlanVisibleNodesCode", "ChoiceData");
		fieldsForType48.put("TreeExpansionList", "RefListListData");
		fieldsForType48.put("ColumnSequenceCodes", "TagListData");
		fieldsForType48.put("ColumnWidths", "CodeToCodeMapData");
		fieldsForType48.put("ColumnSortTag", "CodeData");
		fieldsForType48.put("ColumnSortDirection", "ChoiceData");
		put(48, fieldsForType48);

		HashMap<String, String> fieldsForType49 = new HashMap<String, String>();
		fieldsForType49.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType49.put("SimpleThreatRatingCommentsMap", CODE_TO_USER_STRING_MAP_DATA);
		fieldsForType49.put("StressBasedThreatRatingCommentsMap", CODE_TO_USER_STRING_MAP_DATA);
		put(49, fieldsForType49);

		HashMap<String, String> fieldsForType50 = new HashMap<String, String>();
		fieldsForType50.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType50.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType50.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType50.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType50.put("ScopeBoxColorCode", "ChoiceData");
		put(50, fieldsForType50);

		HashMap<String, String> fieldsForType51 = new HashMap<String, String>();
		fieldsForType51.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType51.put("Details", "DateUnitEffortListData");
		fieldsForType51.put("CategoryOneRef", OREF_DATA);
		fieldsForType51.put("CategoryTwoRef", OREF_DATA);
		fieldsForType51.put("AccountingCodeRef", OREF_DATA);
		fieldsForType51.put("FundingSourceRef", OREF_DATA);
		put(51, fieldsForType51);

		HashMap<String, String> fieldsForType52 = new HashMap<String, String>();
		fieldsForType52.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType52.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType52.put("Text", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType52.put("ShortLabel", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType52.put("TargetStatus", "ChoiceData");
		fieldsForType52.put("ViabilityMode", "ChoiceData");
		fieldsForType52.put("CurrentStatusJustification", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType52.put("SubTargetRefs", "RefListData");
		fieldsForType52.put("GoalIds", "IdListData");
		fieldsForType52.put("KeyEcologicalAttributeIds", "IdListData");
		fieldsForType52.put("IndicatorIds", "IdListData");
		put(52, fieldsForType52);

		HashMap<String, String> fieldsForType53 = new HashMap<String, String>();
		fieldsForType53.put("Label", EXPANDING_USER_TEXT_DATA);
		put(53, fieldsForType53);

		HashMap<String, String> fieldsForType54 = new HashMap<String, String>();
		fieldsForType54.put("Label", EXPANDING_USER_TEXT_DATA);
		put(54, fieldsForType54);

		HashMap<String, String> fieldsForType55 = new HashMap<String, String>();
		fieldsForType55.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType55.put("PeopleCount", INTEGER_DATA);
		fieldsForType55.put("Summary", MULTI_LINE_USER_TEXT_DATA);
		put(55, fieldsForType55);

		HashMap<String, String> fieldsForType56 = new HashMap<String, String>();
		fieldsForType56.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType56.put("Code", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType56.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(56, fieldsForType56);

		HashMap<String, String> fieldsForType57 = new HashMap<String, String>();
		fieldsForType57.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType57.put("Code", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType57.put("Comments", MULTI_LINE_USER_TEXT_DATA);
		put(57, fieldsForType57);

		HashMap<String, String> fieldsForType58 = new HashMap<String, String>();
		fieldsForType58.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType58.put("ProgressChoiceMap", "CodeToChoiceMapData");
		fieldsForType58.put("CommentsMap", CODE_TO_USER_STRING_MAP_DATA);
		fieldsForType58.put("FlagsMap", "CodeToCodeListMapData");
		fieldsForType58.put("CurrentDashboardTab", "CodeData");
		put(58, fieldsForType58);

		HashMap<String, String> fieldsForType59 = new HashMap<String, String>();
		fieldsForType59.put("Label", EXPANDING_USER_TEXT_DATA);
		fieldsForType59.put("TemplateContents", MULTI_LINE_USER_TEXT_DATA);
		fieldsForType59.put("FileExtension", SINGLE_LINE_USER_TEXT_DATA);
		fieldsForType59.put("IncludeImages", "BooleanData");
		put(59, fieldsForType59);
	}
	
	private boolean isUserTextField(final int objectType, final String fieldTag)
	{
		Vector<String>  userTextFieldTypes = new Vector<String>();
		userTextFieldTypes.add(SINGLE_LINE_USER_TEXT_DATA);
		userTextFieldTypes.add(EXPANDING_USER_TEXT_DATA);
		userTextFieldTypes.add(MULTI_LINE_USER_TEXT_DATA);
		
		return containFieldType(objectType, fieldTag, userTextFieldTypes);
	}
	
	private boolean isCodeToUserStringMapField(int objectType, String fieldTag)
	{
		return containFieldType(objectType, fieldTag, CODE_TO_USER_STRING_MAP_DATA);
	}
	
	private boolean isNumerField(int objectType, String fieldTag)
	{
		Vector<String>  userTextFieldTypes = new Vector<String>();
		userTextFieldTypes.add(INTEGER_DATA);
		userTextFieldTypes.add(NUMBER_DATA);
		userTextFieldTypes.add(FLOAT_DATA);
		userTextFieldTypes.add(PERCENTAGE_DATA);
		
		return containFieldType(objectType, fieldTag, userTextFieldTypes);
	}
	
	private boolean isIdFieldType(int objectType, String fieldTag)
	{
		return containFieldType(objectType, fieldTag, BASE_ID_DATA);
	}
	
	private boolean isRefFieldType(int objectType, String fieldTag)
	{
		return containFieldType(objectType, fieldTag, OREF_DATA); 
	}
	
	private boolean containFieldType(int objectType, String fieldTag, String userTextFieldTypesToMatch)
	{
		Vector<String>  userTextFieldTypes = Utility.convertToVector(userTextFieldTypesToMatch);
		
		return containFieldType(objectType, fieldTag, userTextFieldTypes);
	}
	
	private boolean containFieldType(int objectType, String fieldTag, Vector<String> userTextFieldTypesToMatch)
	{
		if (!containsKey(objectType))
			return false;
		
		HashMap<String, String> tagToFieldType = get(objectType);
		if (!tagToFieldType.containsKey(fieldTag))
			return false;
		
		String fieldTypeForTag = tagToFieldType.get(fieldTag);
		return userTextFieldTypesToMatch.contains(fieldTypeForTag);
	}
	
	private boolean hasFieldForType(int objectType, String fieldTag)
	{
		HashMap<String, String> tagToFieldType = get(objectType);
		
		return tagToFieldType.containsKey(fieldTag);
	}

	private final String SINGLE_LINE_USER_TEXT_DATA = "SingleLineUserTextData";
	private final String EXPANDING_USER_TEXT_DATA = "ExpandingUserTextData";
	private final String MULTI_LINE_USER_TEXT_DATA = "MultiLineUserTextData";
	private final String CODE_TO_USER_STRING_MAP_DATA = "CodeToUserStringMapData";
	private final String INTEGER_DATA = "IntegerData";
	private final String NUMBER_DATA = "NumberData";
	private final String FLOAT_DATA = "FloatData";
	private final String PERCENTAGE_DATA = "PercentageData";
	private final String OREF_DATA = "ORefData";
	private final String BASE_ID_DATA = "BaseIdData";
}
