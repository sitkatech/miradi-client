/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.martus.util.MultiCalendar;
import org.miradi.ids.BaseId;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.CodeListData;
import org.miradi.objectdata.DateData;
import org.miradi.objectdata.FloatData;
import org.miradi.objectdata.IntegerData;
import org.miradi.objectdata.NumberData;
import org.miradi.objectdata.PercentageData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringRefMapData;
import org.miradi.project.ObjectManager;
import org.miradi.questions.BudgetTimePeriodQuestion;
import org.miradi.questions.CountriesQuestion;
import org.miradi.questions.CurrencyTypeQuestion;
import org.miradi.questions.FiscalYearStartQuestion;
import org.miradi.questions.FontFamiliyQuestion;
import org.miradi.questions.FontSizeQuestion;
import org.miradi.questions.ProtectedAreaCategoryQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectMetadata extends BaseObject
{
	public ProjectMetadata(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public ProjectMetadata(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
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
		return ObjectType.PROJECT_METADATA;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_PROJECT_FILENAME))
			return objectManager.getFileName();
		
		return super.getPseudoData(fieldTag);
	}

	public ORefList getAllDiagramObjectRefs()
	{
		return objectManager.getAllDiagramObjectRefs(); 
	}

	public String getCurrentWizardScreenName()
	{
		return currentWizardScreenName.get();
	}
	
	public String getProjectName()
	{
		return projectName.get();
	}
	
	public String getProjectScope()
	{
		return projectScope.get();
	}
	
	public String getShortProjectScope()
	{
		return shortProjectScope.get();
	}
	
	public String getProjectVision()
	{
		return projectVision.get();
	}
	
	public MultiCalendar getWorkPlanStartDate()
	{
		return workPlanStartDate.getDate();
	}
	
	public String getWorkPlanStartDateAsString()
	{
		return workPlanStartDate.get();
	}
	
	public String getWorkPlanEndDate()
	{
		return workPlanEndDate.get();
	}
	
	public MultiCalendar getProjectStartDate()
	{
		return startDate.getDate();
	}
	
	public String getStartDate()
	{
		return startDate.get();
	}
	
	public String getExpectedEndDate()
	{
		return expectedEndDate.get();
	}
	
	public String getEffectiveDate()
	{
		return effectiveDate.get();
	}
	
	public String getSizeInHectares()
	{
		return sizeInHectares.get();
	}
	
	public double getSizeInHectaresAsDouble()
	{
		String size = getSizeInHectares();
		if (size.length() == 0)
			return 0;
		
		return Double.parseDouble(size);
	}
	
	public String getProjectNumber()
	{
		return otherOrgProjectNumber.get();
	}
	
	public float getLongitudeAsFloat()
	{
		return longitude.asFloat();
	}
	
	public float getLatitudeAsFloat()
	{
		return latitude.asFloat();
	}
	
	public String getLongitude()
	{
		return longitude.get();
	}
	
	public String getLatitude()
	{
		return latitude.get();
	}
	
	public int getCurrencyDecimalPlaces()
	{
		return currencyDecimalPlaces.asInt();
	}

	public int getDiagramFontSize()
	{
		String sizeAsString = diagramFontSize.get();
		if(sizeAsString.length() == 0)
			return 0;
		return Integer.parseInt(sizeAsString);
	}
	
	public String getDiagramFontFamily()
	{
		return diagramFontFamily.get();
	}
	
	public CodeList getTncTerrestrialEcoRegion()
	{
		return tncTerrestrialEcoRegion.getCodeList();
	}
	
	public CodeList getTncFreshwaterEcoRegion()
	{
		return tncFreshwaterEcoRegion.getCodeList();
	}

	public CodeList getTncMarineEcoRegion()
	{
		return tncMarineEcoRegion.getCodeList();
	}
	
	public boolean isStressBasedThreatRatingMode()
	{
		if (getThreatRatingMode().equals(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE))
			return true;
		
		return false;
	}
	
	
	public String getThreatRatingMode()
	{
		return threatRatingMode.get();
	}

	public int getFiscalYearFirstMonth()
	{
		return getFiscalYearFirstMonth(getData(TAG_FISCAL_YEAR_START));
	}
	
	public static int getFiscalYearFirstMonth(String fiscalYearStartCode)
	{
		if(fiscalYearStartCode.length() == 0)
			fiscalYearStartCode = "1";
		int month = Integer.parseInt(fiscalYearStartCode);
		return month;
	}

	public boolean isBudgetTimePeriodQuarterly()
	{
		return getData(TAG_WORKPLAN_TIME_UNIT).equals(BudgetTimePeriodQuestion.BUDGET_BY_QUARTER_CODE);
	}

	public boolean isBudgetTimePeriodYearly()
	{
		return getData(TAG_WORKPLAN_TIME_UNIT).equals(BudgetTimePeriodQuestion.BUDGET_BY_YEAR_CODE);
	}
	
	public static boolean is(BaseObject object)
	{
		if(object == null)
			return false;
		return is(object.getRef());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	@Override
	public String toString()
	{
		return getProjectName();
	}
	
	void clear()
	{
		super.clear();
		currentWizardScreenName = new StringData(TAG_CURRENT_WIZARD_SCREEN_NAME);
		projectName = new StringData(TAG_PROJECT_NAME);
		projectScope = new StringData(TAG_PROJECT_SCOPE);
		shortProjectScope = new StringData(TAG_SHORT_PROJECT_SCOPE);
		projectVision = new StringData(TAG_PROJECT_VISION);
		startDate = new DateData(TAG_START_DATE);
		expectedEndDate = new DateData(TAG_EXPECTED_END_DATE);
		effectiveDate = new DateData(TAG_DATA_EFFECTIVE_DATE);
		sizeInHectares = new NumberData(TAG_TNC_SIZE_IN_HECTARES);
		currencyDecimalPlaces = new IntegerData(TAG_CURRENCY_DECIMAL_PLACES);
		latitude = new FloatData(TAG_PROJECT_LATITUDE);
		longitude = new FloatData(TAG_PROJECT_LONGITUDE);
		totalBudgetForFunding = new NumberData(TAG_TOTAL_BUDGET_FOR_FUNDING);
		budgetSecuredPercent = new PercentageData(TAG_BUDGET_SECURED_PERCENT);
		currencyType = new ChoiceData(TAG_CURRENCY_TYPE, getQuestion(CurrencyTypeQuestion.class));
		currencySymbol = new StringData(TAG_CURRENCY_SYMBOL);
		fiscalYearStart = new ChoiceData(TAG_FISCAL_YEAR_START, getQuestion(FiscalYearStartQuestion.class));
		projectDescription = new StringData(TAG_PROJECT_DESCRIPTION);
		projectURL = new StringData(TAG_PROJECT_URL);
		projectArea = new StringData(TAG_PROJECT_AREA);
		projectAreaNotes = new StringData(TAG_PROJECT_AREA_NOTES);
		scopeComments = new StringData(TAG_SCOPE_COMMENTS);
		countries = new CodeListData(TAG_COUNTRIES, getQuestion(CountriesQuestion.class));
		statesAndProvinces = new StringData(TAG_STATE_AND_PROVINCES);
		municipalities = new StringData(TAG_MUNICIPALITIES);
		legislativeDistricts = new StringData(TAG_LEGISLATIVE_DISTRICTS);
		locationDetail = new StringData(TAG_LOCATION_DETAIL);
		locationComments = new StringData(TAG_LOCATION_COMMENTS);
		keyFundingSources = new StringData(TAG_KEY_FUNDING_SOURCES);
		financialComments = new StringData(TAG_FINANCIAL_COMMENTS);
		workPlanStartDate = new DateData(TAG_WORKPLAN_START_DATE);
		workPlanEndDate = new DateData(TAG_WORKPLAN_END_DATE);
		workPlanTimeUnit = new ChoiceData(TAG_WORKPLAN_TIME_UNIT, getQuestion(BudgetTimePeriodQuestion.class));
		planningComments = new StringData(TAG_PLANNING_COMMENTS);
		redListSpecies = new StringData(TAG_RED_LIST_SPECIES);
		otherNotableSpecies = new StringData(TAG_OTHER_NOTABLE_SPECIES);
		humanPopulation = new IntegerData(TAG_HUMAN_POPULATION);
		humanPopulationNotes = new StringData(TAG_HUMAN_POPULATION_NOTES);
		socialContext = new StringData(TAG_SOCIAL_CONTEXT);
		siteMapReference = new StringData(TAG_SITE_MAP_REFERENCE);
		protectedAreaCategories = new CodeListData(TAG_PROTECTED_AREA_CATEGORIES, getQuestion(ProtectedAreaCategoryQuestion.class));
		protectedAreaCategoryNotes = new StringData(TAG_PROTECTED_AREA_CATEGORY_NOTES);
		projectStatus = new StringData(TAG_PROJECT_STATUS);
		nextSteps = new StringData(TAG_NEXT_STEPS);

		addPresentationDataField(TAG_CURRENT_WIZARD_SCREEN_NAME, currentWizardScreenName);
		addField(TAG_PROJECT_NAME, projectName);
		addField(TAG_PROJECT_SCOPE, projectScope);
		addField(TAG_SHORT_PROJECT_SCOPE, shortProjectScope);
		addField(TAG_PROJECT_VISION, projectVision);
		addField(TAG_START_DATE, startDate);
		addField(TAG_EXPECTED_END_DATE, expectedEndDate);
		addField(TAG_DATA_EFFECTIVE_DATE, effectiveDate);
		addField(TAG_TNC_SIZE_IN_HECTARES, sizeInHectares);
		addField(TAG_CURRENCY_DECIMAL_PLACES, currencyDecimalPlaces);
		addField(TAG_PROJECT_LATITUDE, latitude);
		addField(TAG_PROJECT_LONGITUDE, longitude);
		addField(TAG_TOTAL_BUDGET_FOR_FUNDING, totalBudgetForFunding);
		addField(TAG_BUDGET_SECURED_PERCENT, budgetSecuredPercent);
		addField(TAG_CURRENCY_TYPE, currencyType);
		addField(TAG_CURRENCY_SYMBOL, currencySymbol);
		addField(TAG_FISCAL_YEAR_START, fiscalYearStart);
		addField(TAG_PROJECT_DESCRIPTION, projectDescription);
		addField(TAG_PROJECT_URL, projectURL);
		addField(TAG_PROJECT_AREA, projectArea);
		addField(TAG_PROJECT_AREA_NOTES, projectAreaNotes);
		addField(TAG_SCOPE_COMMENTS, scopeComments);
		addField(TAG_COUNTRIES, countries);
		addField(TAG_STATE_AND_PROVINCES, statesAndProvinces);
		addField(TAG_MUNICIPALITIES, municipalities);
		addField(TAG_LEGISLATIVE_DISTRICTS, legislativeDistricts);
		addField(TAG_LOCATION_DETAIL, locationDetail);
		addField(TAG_LOCATION_COMMENTS, locationComments);
		addField(TAG_KEY_FUNDING_SOURCES, keyFundingSources);
		addField(TAG_FINANCIAL_COMMENTS, financialComments);
		addField(TAG_WORKPLAN_START_DATE, workPlanStartDate);
		addField(TAG_WORKPLAN_END_DATE, workPlanEndDate);
		addField(TAG_WORKPLAN_TIME_UNIT, workPlanTimeUnit);
		addField(TAG_PLANNING_COMMENTS, planningComments);
		addField(TAG_RED_LIST_SPECIES, redListSpecies);
		addField(TAG_OTHER_NOTABLE_SPECIES, otherNotableSpecies);
		addField(TAG_HUMAN_POPULATION, humanPopulation);
		addField(TAG_HUMAN_POPULATION_NOTES, humanPopulationNotes);
		addField(TAG_SOCIAL_CONTEXT, socialContext);
		addField(TAG_SITE_MAP_REFERENCE, siteMapReference);
		addField(TAG_PROTECTED_AREA_CATEGORIES, protectedAreaCategories);
		addField(TAG_PROTECTED_AREA_CATEGORY_NOTES, protectedAreaCategoryNotes);
		addField(TAG_PROJECT_STATUS, projectStatus);
		addField(TAG_NEXT_STEPS, nextSteps);

		tncLessonsLearned = new StringData(TAG_TNC_LESSONS_LEARNED);
		tncWorkbookVersionNumber = new StringData(TAG_TNC_WORKBOOK_VERSION_NUMBER);
		tncWorkbookVersionDate = new DateData(TAG_TNC_WORKBOOK_VERSION_DATE);
		tncDatabaseDownloadDate = new DateData(TAG_TNC_DATABASE_DOWNLOAD_DATE);
		tncPlanningTeamComment = new StringData(TAG_TNC_PLANNING_TEAM_COMMENT);
		tncOperatingUnits = new CodeListData(TAG_TNC_OPERATING_UNITS, getQuestion(TncOperatingUnitsQuestion.class));
		tncTerrestrialEcoRegion = new CodeListData(TAG_TNC_TERRESTRIAL_ECO_REGION, getQuestion(TncTerrestrialEcoRegionQuestion.class));
		tncMarineEcoRegion = new CodeListData(TAG_TNC_MARINE_ECO_REGION, getQuestion(TncMarineEcoRegionQuestion.class));
		tncFreshwaterEcoRegion = new CodeListData(TAG_TNC_FRESHWATER_ECO_REGION, getQuestion(TncFreshwaterEcoRegionQuestion.class));

		addField(TAG_TNC_LESSONS_LEARNED, tncLessonsLearned);
		addField(TAG_TNC_WORKBOOK_VERSION_NUMBER, tncWorkbookVersionNumber);
		addField(TAG_TNC_WORKBOOK_VERSION_DATE, tncWorkbookVersionDate);
		addField(TAG_TNC_DATABASE_DOWNLOAD_DATE, tncDatabaseDownloadDate);
		addField(TAG_TNC_PLANNING_TEAM_COMMENT, tncPlanningTeamComment);
		addField(TAG_TNC_OPERATING_UNITS, tncOperatingUnits);
		addField(TAG_TNC_TERRESTRIAL_ECO_REGION, tncTerrestrialEcoRegion);
		addField(TAG_TNC_MARINE_ECO_REGION, tncMarineEcoRegion);
		addField(TAG_TNC_FRESHWATER_ECO_REGION, tncFreshwaterEcoRegion);
		
		otherOrgProjectNumber = new StringData(TAG_OTHER_ORG_PROJECT_NUMBER);
		otherOrgRelatedProjects = new StringData(TAG_OTHER_ORG_RELATED_PROJECTS);
		addField(TAG_OTHER_ORG_PROJECT_NUMBER, otherOrgProjectNumber);
		addField(TAG_OTHER_ORG_RELATED_PROJECTS, otherOrgRelatedProjects);
		
		diagramFontSize = new ChoiceData(TAG_DIAGRAM_FONT_SIZE, getQuestion(FontSizeQuestion.class));
		diagramFontFamily = new ChoiceData(TAG_DIAGRAM_FONT_FAMILY, getQuestion(FontFamiliyQuestion.class));
		threatRatingMode = new ChoiceData(TAG_THREAT_RATING_MODE, getQuestion(ThreatRatingModeChoiceQuestion.class));
		xenodataRefs = new StringRefMapData(TAG_XENODATA_STRING_REF_MAP);
		
		addField(TAG_DIAGRAM_FONT_SIZE, diagramFontSize);
		addField(TAG_DIAGRAM_FONT_FAMILY, diagramFontFamily);
		addField(TAG_THREAT_RATING_MODE, threatRatingMode);
		addField(TAG_XENODATA_STRING_REF_MAP, xenodataRefs);
		
		projectFileName = new PseudoStringData(PSEUDO_TAG_PROJECT_FILENAME);
		addField(PSEUDO_TAG_PROJECT_FILENAME, projectFileName);
	}

	public static final String TAG_CURRENT_WIZARD_SCREEN_NAME = "CurrentWizardScreenName";
	public static final String TAG_PROJECT_NAME = "ProjectName";
	public static final String TAG_PROJECT_SCOPE = "ProjectScope";
	public static final String TAG_SHORT_PROJECT_SCOPE = "ShortProjectScope";
	public static final String TAG_PROJECT_VISION = "ProjectVision";
	public static final String TAG_START_DATE = "StartDate";
	public static final String TAG_EXPECTED_END_DATE = "ExpectedEndDate";
	public static final String TAG_DATA_EFFECTIVE_DATE = "DataEffectiveDate";
	public static final String TAG_CURRENCY_DECIMAL_PLACES = "CurrencyDecimalPlaces";
	public static final String TAG_PROJECT_LATITUDE = "ProjectLatitude";
	public static final String TAG_PROJECT_LONGITUDE = "ProjectLongitude";
	public static final String TAG_TOTAL_BUDGET_FOR_FUNDING = "TotalBudgetForFunding";
	public static final String TAG_BUDGET_SECURED_PERCENT = "BudgetSecuredPercent";
	public static final String TAG_CURRENCY_TYPE = "CurrencyType";
	public static final String TAG_CURRENCY_SYMBOL = "CurrencySymbol";
	public static final String TAG_FISCAL_YEAR_START = "FiscalYearStart";
	public static final String TAG_PROJECT_DESCRIPTION = "ProjectDescription";
	public static final String TAG_PROJECT_URL = "ProjectURL";
	public static final String TAG_PROJECT_AREA = "ProjectArea";
	public static final String TAG_PROJECT_AREA_NOTES = "ProjectAreaNote";
	public static final String TAG_SCOPE_COMMENTS = "ScopeComments";
	public static final String TAG_COUNTRIES = "Countries";
	public static final String TAG_STATE_AND_PROVINCES = "StateAndProvinces";
	public static final String TAG_MUNICIPALITIES = "Municipalities";
	public static final String TAG_LEGISLATIVE_DISTRICTS = "LegislativeDistricts";
	public static final String TAG_LOCATION_DETAIL = "LocationDetail";
	public static final String TAG_LOCATION_COMMENTS = "LocationComments";
	public static final String TAG_KEY_FUNDING_SOURCES = "KeyFundingSources";
	public static final String TAG_FINANCIAL_COMMENTS = "FinancialComments";
	public static final String TAG_WORKPLAN_START_DATE = "WorkPlanStartDate";
	public static final String TAG_WORKPLAN_END_DATE = "WorkPlanEndDate";
	public static final String TAG_WORKPLAN_TIME_UNIT = "WorkPlanTimeUnit";
	public static final String TAG_PLANNING_COMMENTS = "PlanningComments";
	public static final String TAG_RED_LIST_SPECIES = "RedListSpecies";
	public static final String TAG_OTHER_NOTABLE_SPECIES = "OtherNotableSpecies";
	public static final String TAG_HUMAN_POPULATION = "HumanPopulation";
	public static final String TAG_HUMAN_POPULATION_NOTES = "HumanPopulationNotes";
	public static final String TAG_SOCIAL_CONTEXT = "SocialContext";
	public static final String TAG_SITE_MAP_REFERENCE = "SiteMapReference";
	public static final String TAG_PROTECTED_AREA_CATEGORIES = "ProtectedAreaCategories";
	public static final String TAG_PROTECTED_AREA_CATEGORY_NOTES = "ProtectedAreaCategoryNotes";
	public static final String TAG_PROJECT_STATUS = "ProjectStatus";
	public static final String TAG_NEXT_STEPS = "NextSteps";
	
	public static final String PSEUDO_TAG_PROJECT_FILENAME = "PseudoTagProjectFilename";
	
	public static final String TAG_TNC_LESSONS_LEARNED = "TNC.LessonsLearned";
	public static final String TAG_TNC_WORKBOOK_VERSION_NUMBER = "TNC.WorkbookVersionNumber";
	public static final String TAG_TNC_WORKBOOK_VERSION_DATE = "TNC.WorkbookVersionDate";
	public static final String TAG_TNC_DATABASE_DOWNLOAD_DATE = "TNC.DatabaseDownloadDate";
	public static final String TAG_TNC_PLANNING_TEAM_COMMENT = "TNC.PlanningTeamComment";
	public static final String TAG_TNC_SIZE_IN_HECTARES = "TNC.SizeInHectares";
	public static final String TAG_TNC_OPERATING_UNITS = "TNC.OperatingUnitList";
	public static final String TAG_TNC_TERRESTRIAL_ECO_REGION = "TNC.TerrestrialEcoRegion";
	public static final String TAG_TNC_MARINE_ECO_REGION = "TNC.MarineEcoRegion";
	public static final String TAG_TNC_FRESHWATER_ECO_REGION = "TNC.FreshwaterEcoRegion";

	public static final String TAG_OTHER_ORG_PROJECT_NUMBER = "OtherOrgProjectNumber";
	public static final String TAG_OTHER_ORG_RELATED_PROJECTS = "OtherOrgRelatedProjects";	
	
	public static final String TAG_DIAGRAM_FONT_FAMILY = "DiagramFontFamily";
	public static final String TAG_DIAGRAM_FONT_SIZE = "DiagramFontSize";
	public static final String TAG_THREAT_RATING_MODE = "ThreatRatingMode";
	
	public static final String TAG_XENODATA_STRING_REF_MAP = "XenodataRefs";
	
	public static final String OBJECT_NAME = "ProjectMetadata";

	private StringData currentWizardScreenName;

	private StringData projectName;
	private StringData projectScope;
	private StringData shortProjectScope;
	private StringData projectVision;
	private DateData startDate;
	private DateData expectedEndDate;
	private DateData effectiveDate;
	private NumberData sizeInHectares;
	private IntegerData currencyDecimalPlaces;
	private FloatData latitude;
	private FloatData longitude;
	private NumberData totalBudgetForFunding;
	private PercentageData budgetSecuredPercent;
	private ChoiceData currencyType;
	private StringData currencySymbol;
	private ChoiceData fiscalYearStart;
	private StringData projectDescription;
	private StringData projectURL;
	private StringData projectArea;
	private StringData projectAreaNotes;
	private StringData scopeComments;
	private CodeListData countries;
	private StringData statesAndProvinces;
	private StringData municipalities;
	private StringData legislativeDistricts;
	private StringData locationDetail;
	private StringData locationComments;
	private StringData keyFundingSources;
	private StringData financialComments;
	private DateData workPlanStartDate;
	private DateData workPlanEndDate;
	private ChoiceData workPlanTimeUnit;
	private StringData planningComments;
	private StringData redListSpecies;
	private StringData otherNotableSpecies;
	private IntegerData humanPopulation;
	private StringData humanPopulationNotes;
	private StringData socialContext;
	private StringData siteMapReference;
	private CodeListData protectedAreaCategories;
	private StringData protectedAreaCategoryNotes;
	private StringData projectStatus;
	private StringData nextSteps;

	private StringData tncLessonsLearned;
	private StringData tncWorkbookVersionNumber;
	private DateData tncWorkbookVersionDate;
	private DateData tncDatabaseDownloadDate;
	private StringData tncPlanningTeamComment;
	private CodeListData tncOperatingUnits;
	private CodeListData tncTerrestrialEcoRegion;
	private CodeListData tncMarineEcoRegion;
	private CodeListData tncFreshwaterEcoRegion;
	
	private StringData otherOrgProjectNumber;
	private StringData otherOrgRelatedProjects;
	
	private ChoiceData diagramFontFamily;
	private ChoiceData diagramFontSize;
	private ChoiceData threatRatingMode;
	
	private StringRefMapData xenodataRefs;
	private PseudoStringData projectFileName;
}
