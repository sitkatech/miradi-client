/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.FloatData;
import org.conservationmeasures.eam.objectdata.IntegerData;
import org.conservationmeasures.eam.objectdata.NumberData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.ObjectPool;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.FontFamiliyQuestion;
import org.conservationmeasures.eam.questions.FontSizeQuestion;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ProjectMetadata extends BaseObject
{
	public ProjectMetadata(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public ProjectMetadata(BaseId idToUse)
	{
		super(idToUse);
		clear();
	}
	
	public ProjectMetadata(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	
	public ProjectMetadata(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
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
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	
	public String getData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_PROJECT_FILENAME))
			return objectManager.getFileName();
		
		return super.getData(fieldTag);
	}
	
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_RELATED_GOAL_REFS))
			return getAllGoalRefs().toString();
		
		if (fieldTag.equals(PSEUDO_TAG_PROJECT_TEAM_MEMBER_REFS))
			return getTeamMemberRefs().toString();
			
		return super.getPseudoData(fieldTag);
	}

	public ORefList getAllGoalRefs()
	{
		return objectManager.getGoalPool().getORefList();
	}
		
	private ORefList getTeamMemberRefs()
	{
		ORefList teamMembers = new ORefList();
		try
		{
			ProjectResource[] projectResources = getProject().getAllProjectResources();
			for (int i = 0; i < projectResources.length; ++i)
			{
				if (! projectResources[i].hasRole(ResourceRoleQuestion.TeamMemberRoleCode))
					continue;
				
				teamMembers.add(projectResources[i].getRef());
			}
			
			return teamMembers;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ORefList();
		}
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
	
	public String getShortProjectVision()
	{
		return shortProjectVision.get();
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
	
	public String getThreatRatingMode()
	{
		return threatRatingMode.get();
	}

	static int getMonthDelta(int oldMonth, int newMonth)
	{
		int monthDelta = newMonth - oldMonth;
		return monthDelta;
	}

	protected static int getSkewedMonthFromCode(String fiscalYearStartCode)
	{
		if(fiscalYearStartCode.length() == 0)
			fiscalYearStartCode = "1";
		int month = Integer.parseInt(fiscalYearStartCode);
		if(month > 6)
			month -= 12;
		return month;
	}

	// FIXME: This code needs to be moved into a doer, when that doer gets created
	private void updateExistingBudgetData(int monthDelta) throws Exception
	{
		ObjectPool pool = getProject().getPool(Assignment.getObjectType());
		ORefList assignmentRefs = pool.getRefList();
		for(int i = 0; i < assignmentRefs.size(); ++i)
		{
			Assignment assignment = Assignment.find(getProject(), assignmentRefs.get(i));
			Vector<Command> commands = assignment.getCommandsToShiftEffort(monthDelta);
			getProject().executeCommandsWithoutTransaction(commands);
		}
	}

	void clear()
	{
		super.clear();
		currentWizardScreenName = new StringData();
		projectName = new StringData();
		projectScope = new StringData();
		shortProjectScope = new StringData();
		projectVision = new StringData();
		shortProjectVision = new StringData();
		startDate = new DateData();
		expectedEndDate = new DateData();
		effectiveDate = new DateData();
		sizeInHectares = new NumberData();
		currencyDecimalPlaces = new IntegerData();
		latitude = new FloatData();
		longitude = new FloatData();
		totalBudgetForFunding = new NumberData();
		budgetSecuredPercent = new NumberData();
		currencyType = new ChoiceData();
		currencySymbol = new StringData();
		fiscalYearStart = new ChoiceData();
		
		addField(TAG_CURRENT_WIZARD_SCREEN_NAME, currentWizardScreenName);
		addField(TAG_PROJECT_NAME, projectName);
		addField(TAG_PROJECT_SCOPE, projectScope);
		addField(TAG_SHORT_PROJECT_SCOPE, shortProjectScope);
		addField(TAG_PROJECT_VISION, projectVision);
		addField(TAG_SHORT_PROJECT_VISION, shortProjectVision);
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
		
		tncLessonsLearned = new StringData();
		tncWorkbookVersionNumber = new StringData();
		tncWorkbookVersionDate = new DateData();
		tncDatabaseDownloadDate = new DateData();
		tncPlanningTeamComment = new StringData();
		tncEcoregion = new StringData();
		tncCountry = new StringData();
		tncOperatingUnits = new StringData();

		addField(TAG_TNC_LESSONS_LEARNED, tncLessonsLearned);
		addField(TAG_TNC_WORKBOOK_VERSION_NUMBER, tncWorkbookVersionNumber);
		addField(TAG_TNC_WORKBOOK_VERSION_DATE, tncWorkbookVersionDate);
		addField(TAG_TNC_DATABASE_DOWNLOAD_DATE, tncDatabaseDownloadDate);
		addField(TAG_TNC_PLANNING_TEAM_COMMENT, tncPlanningTeamComment);
		addField(TAG_TNC_ECOREGION, tncEcoregion);
		addField(TAG_TNC_COUNTRY, tncCountry);
		addField(TAG_TNC_OPERATING_UNITS, tncOperatingUnits);
		
		diagramFontSize = new IntegerData();
		diagramFontFamily = new StringData();
		threatRatingMode = new ChoiceData();
		diagramFontSizeValue = new PseudoQuestionData(new FontSizeQuestion(TAG_DIAGRAM_FONT_SIZE));
		diagramFontFamilyValue = new PseudoQuestionData(new FontFamiliyQuestion(TAG_DIAGRAM_FONT_FAMILY));
		projectTeamMembers = new PseudoORefListData(PSEUDO_TAG_PROJECT_TEAM_MEMBER_REFS);
		
		addField(TAG_DIAGRAM_FONT_SIZE, diagramFontSize);
		addField(TAG_DIAGRAM_FONT_FAMILY, diagramFontFamily);
		addField(TAG_THREAT_RATING_MODE, threatRatingMode);
		addField(PSEUDO_TAG_DIAGRAM_FONT_FAMILY, diagramFontFamilyValue);
		addField(PSEUDO_TAG_DIAGRAM_FONT_SIZE, diagramFontSizeValue);
		addField(PSEUDO_TAG_PROJECT_TEAM_MEMBER_REFS, projectTeamMembers);
	}

	public static final String TAG_CURRENT_WIZARD_SCREEN_NAME = "CurrentWizardScreenName";
	public static final String TAG_PROJECT_NAME = "ProjectName";
	public static final String TAG_PROJECT_SCOPE = "ProjectScope";
	public static final String TAG_SHORT_PROJECT_SCOPE = "ShortProjectScope";
	public static final String TAG_PROJECT_VISION = "ProjectVision";
	public static final String TAG_SHORT_PROJECT_VISION = "ShortProjectVision";
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
	
	public static final String PSEUDO_TAG_PROJECT_FILENAME = "PseudoTagProjectFilename";
	
	public static final String TAG_TNC_LESSONS_LEARNED = "TNC.LessonsLearned";
	public static final String TAG_TNC_WORKBOOK_VERSION_NUMBER = "TNC.WorkbookVersionNumber";
	public static final String TAG_TNC_WORKBOOK_VERSION_DATE = "TNC.WorkbookVersionDate";
	public static final String TAG_TNC_DATABASE_DOWNLOAD_DATE = "TNC.DatabaseDownloadDate";
	public static final String TAG_TNC_PLANNING_TEAM_COMMENT = "TNC.PlanningTeamComment";
	public static final String TAG_TNC_SIZE_IN_HECTARES = "TNC.SizeInHectares";
	public static final String TAG_TNC_ECOREGION = "TNC.Ecoregion";
	public static final String TAG_TNC_COUNTRY = "TNC.Country";
	public static final String TAG_TNC_OPERATING_UNITS = "TNC.OperatingUnits";

	
	public static final String TAG_DIAGRAM_FONT_FAMILY = "DiagramFontFamily";
	public static final String TAG_DIAGRAM_FONT_SIZE = "DiagramFontSize";
	public static final String TAG_THREAT_RATING_MODE = "ThreatRatingMode";
	
	public static final String PSEUDO_TAG_DIAGRAM_FONT_FAMILY = "DiagramFontFamilyValue";
	public static final String PSEUDO_TAG_DIAGRAM_FONT_SIZE = "DiagramFontSizeValue";
	public static final String PSEUDO_TAG_RELATED_GOAL_REFS = "PseudoTagRelatedGoalRefs";
	public static final String PSEUDO_TAG_PROJECT_TEAM_MEMBER_REFS = "PseudoTagProjectTeamMembersRefs";

	static final String OBJECT_NAME = "ProjectMetadata";

	private StringData currentWizardScreenName;

	private StringData projectName;
	private StringData projectScope;
	private StringData shortProjectScope;
	private StringData projectVision;
	private StringData shortProjectVision;
	private DateData startDate;
	private DateData expectedEndDate;
	private DateData effectiveDate;
	private NumberData sizeInHectares;
	private IntegerData currencyDecimalPlaces;
	private FloatData latitude;
	private FloatData longitude;
	private NumberData totalBudgetForFunding;
	private NumberData budgetSecuredPercent;
	private ChoiceData currencyType;
	private StringData currencySymbol;
	private ChoiceData fiscalYearStart;
	
	private StringData tncLessonsLearned;
	private StringData tncWorkbookVersionNumber;
	private DateData tncWorkbookVersionDate;
	private DateData tncDatabaseDownloadDate;
	private StringData tncPlanningTeamComment;
	private StringData tncEcoregion;
	private StringData tncCountry;
	private StringData tncOperatingUnits;

	
	private StringData diagramFontFamily;
	private IntegerData diagramFontSize;
	private ChoiceData threatRatingMode;
	
	private PseudoQuestionData diagramFontFamilyValue;
	private PseudoQuestionData diagramFontSizeValue;
	private PseudoORefListData projectTeamMembers;
}
