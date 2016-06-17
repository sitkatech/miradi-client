/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.project;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.PersistentDiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.DateUnitListData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.*;
import org.miradi.objects.*;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.questions.*;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateRange;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.OptionalDouble;
import org.miradi.utils.PointList;
import org.miradi.utils.ThreatStressRatingHelper;
import org.miradi.utils.Translation;
import org.miradi.utils.XmlUtilities2;
import org.miradi.views.diagram.LinkCreator;


public class ProjectForTesting extends ProjectWithHelpers
{
	public static ProjectForTesting createProjectWithoutDefaultObjects(String testName) throws Exception
	{
		ProjectForTesting projectForTesting = new ProjectForTesting(testName);
		
		return projectForTesting;
	}
	
	public static ProjectForTesting createProjectWithDefaultObjects(String testName) throws Exception
	{
		ProjectForTesting projectForTesting = createProjectWithoutDefaultObjects(testName);
		projectForTesting.createMissingDefaultObjects();
		projectForTesting.applyDefaultBehavior();
		projectForTesting.loadDiagramModelForTesting();
		return projectForTesting;
	}

	private ProjectForTesting(String testName) throws Exception
	{
		Translation.initialize();
		
		finishOpeningAfterLoad(testName);
	}
	
	public DiagramObject getMainDiagramObject()
	{
		return getTestingDiagramModel().getDiagramObject();
	}
	
	private void fillProjectMetadata() throws Exception
	{
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_NAME, "Some Project Name");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_LANGUAGE, "en");
		setProjectStartDate(MultiCalendar.createFromGregorianYearMonthDay(2008, 1, 1));
		setProjectEndDate(MultiCalendar.createFromGregorianYearMonthDay(2009, 12, 31));
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, "2006-09-27");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_AREA, "10000");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS, "10");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_LONGITUDE, "30");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_LATITUDE, "40");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_SCOPE, "Some project scope");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, "Some Short project scope");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_DESCRIPTION, "Some project description");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_PROJECT_VISION, encodeXml("Some project \"vision\"" + HtmlUtilities.BR_TAG + HtmlUtilities.BR_TAG + "With multiple lines!"));
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS, encodeXml("TNC planning team comment, mentioning that x > 2 && x < 4"));
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED, "TNC lessons learned");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS, "Other Related Projects");
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_COUNTRIES, createSampleCountriesCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, createSampleTncOperatingUnitsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, createSampleFreshwaterEcoregionsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, createSampleMarineEcoregionsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, createSampleTerrestrialEcoregionsCodeList().toString());
		fillObjectUsingCommand(getMetadata().getRef(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, createSampleXenodata());
	}

	public ORef getTncProjectDataRef()
	{
		return getSingletonObjectRef(TncProjectDataSchema.getObjectType());
	}
	
	private String encodeXml(String data)
	{
		return XmlUtilities2.getXmlEncoded(data);
	}

	private void fillWwfProjectData() throws Exception
	{
		ORef wwfProjectDataRef = getSingletonObjectRef(WwfProjectDataSchema.getObjectType());
		fillObjectUsingCommand(wwfProjectDataRef, WwfProjectData.TAG_MANAGING_OFFICES, new WwfManagingOfficesQuestion().getAllCodes().toString());
		fillObjectUsingCommand(wwfProjectDataRef, WwfProjectData.TAG_REGIONS, new WwfRegionsQuestion().getAllCodes().toString());
		fillObjectUsingCommand(wwfProjectDataRef, WwfProjectData.TAG_ECOREGIONS, new WwfEcoRegionsQuestion().getAllCodes().toString());
	}
	
	private void fillWcsProjectData() throws Exception
	{
		ORef wcsProjectDataRef = getSingletonObjectRef(WcsProjectDataSchema.getObjectType());
		fillObjectUsingCommand(wcsProjectDataRef, WcsProjectData.TAG_ORGANIZATIONAL_FOCUS, "Sample Organizational Focus");
		fillObjectUsingCommand(wcsProjectDataRef, WcsProjectData.TAG_ORGANIZATIONAL_LEVEL, "Sample Organizational Level");
		fillObjectUsingCommand(wcsProjectDataRef, WcsProjectData.TAG_SWOT_COMPLETED, BooleanData.BOOLEAN_TRUE);
		fillObjectUsingCommand(wcsProjectDataRef, WcsProjectData.TAG_SWOT_URL, "Sample Swot Url");
		fillObjectUsingCommand(wcsProjectDataRef, WcsProjectData.TAG_STEP_COMPLETED, BooleanData.BOOLEAN_TRUE);
		fillObjectUsingCommand(wcsProjectDataRef, WcsProjectData.TAG_STEP_URL, "Sample Steps url");
	}
	
	private void fillRareProjectData() throws Exception
	{
		ORef rareProjectDataRef = getSingletonObjectRef(RareProjectDataSchema.getObjectType());
		
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_COHORT);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.LEGACY_TAG_THREATS_ADDRESSED_NOTES);
		fillObjectUsingCommand(rareProjectDataRef, RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA, "3");
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_BIODIVERSITY_HOTSPOTS);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_CAMPAIGN_SLOGAN);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		fillObjectWithSampleStringData(rareProjectDataRef, RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
	}
	
	private void fillFosProjectData() throws Exception
	{
		ORef fosProjectDataRef = getSingletonObjectRef(FosProjectDataSchema.getObjectType());
		
		fillObjectUsingCommand(fosProjectDataRef, FosProjectData.TAG_TRAINING_TYPE, FosTrainingTypeQuestion.ONLINE_CODE);
		fillObjectWithSampleStringData(fosProjectDataRef, FosProjectData.TAG_TRAINING_DATES);
		fillObjectWithSampleStringData(fosProjectDataRef, FosProjectData.TAG_TRAINERS);
		fillObjectWithSampleStringData(fosProjectDataRef, FosProjectData.TAG_COACHES);	
	}
	
	public void fillTncProjectData() throws Exception
	{
		ORef tncProjectDataRef = getTncProjectDataRef();

		fillObjectUsingCommand(getTncProjectDataRef(), TncProjectData.TAG_PROJECT_SHARING_CODE, ProjectSharingQuestion.SHARE_WITH_ANYONE);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_MAKING_THE_CASE);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_RISKS);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_CAPACITY_AND_FUNDING);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_PROJECT_LEVEL_COMMENTS);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_PROJECT_CITATIONS);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_CAP_STANDARDS_SCORECARD);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_MAKING_THE_CASE);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_RISKS);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_CAPACITY_AND_FUNDING);
		fillObjectWithSampleStringData(tncProjectDataRef, TncProjectData.TAG_FUNDRAISING_PLAN);
		fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_PROJECT_FOCUS, ProjectFocusQuestion.POLICY_AND_PRACTICE_CODE);
		fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_PROJECT_SCALE, ProjectScaleQuestion.REGIONAL_CODE);
	}
	
	public void setFiscalYearStartMonth(int startMonth) throws Exception
	{
		setObjectData(getMetadata(), ProjectMetadata.TAG_FISCAL_YEAR_START, Integer.toString(startMonth));
	}

	public void setSingleYearProjectDate(int singleYear) throws Exception
	{
		setProjectDates(singleYear, singleYear);	
	}

	public void setProjectDates(int startYear, int endYear) throws Exception
	{
		MultiCalendar projectStartDate = ProjectForTesting.createStartYear(startYear);
		MultiCalendar projectEndDate = ProjectForTesting.createEndYear(endYear);
		setProjectStartDate(projectStartDate);
		setProjectEndDate(projectEndDate);
	}
	
	public void setProjectStartDate(int startYear) throws Exception
	{
		setProjectStartDate(MultiCalendar.createFromGregorianYearMonthDay(startYear, 1, 1));
	}
	
	public void setProjectEndDate(int endYear) throws Exception
	{
		setProjectEndDate(MultiCalendar.createFromGregorianYearMonthDay(endYear, 1, 1));
	}
	
	public void setProjectStartDate(MultiCalendar startDate) throws Exception
	{
		setProjectDate(startDate, ProjectMetadata.TAG_START_DATE);
	}

	public void setProjectEndDate(MultiCalendar endDate) throws Exception
	{
		setProjectDate(endDate, ProjectMetadata.TAG_EXPECTED_END_DATE);
	}
	
	public void setProjectDate(MultiCalendar projectDate, String tag) throws Exception
	{
		String dateAsString = "";
		if (projectDate != null)
			dateAsString = projectDate.toString();  
			
		fillObjectUsingCommand(getMetadata().getRef(), tag, dateAsString);
	}
	
	public MiradiShareProjectData createMiradiShareProjectData() throws Exception
	{
		ORef miradiShareProjectDataRef = createObject(MiradiShareProjectDataSchema.getObjectType());
		return MiradiShareProjectData.find(this, miradiShareProjectDataRef);
	}
	
	public MiradiShareTaxonomy createMiradiShareTaxonomy() throws Exception
	{
		ORef miradiShareTaxonomyRef = createObject(MiradiShareTaxonomySchema.getObjectType());
		return MiradiShareTaxonomy.find(this, miradiShareTaxonomyRef);
	}
	
	public MiradiShareProjectData createAndPopulateMiradiShareProjectData() throws Exception
	{
		MiradiShareProjectData miradiShareProjectData = MiradiShareProjectData.find(this, getSingletonObjectRef(MiradiShareProjectDataSchema.getObjectType()));
		populateMiradiShareProjectData(miradiShareProjectData);
		
		return miradiShareProjectData;
	}
	
	public MiradiShareTaxonomy createAndPopulateMiradiShareTaxonomy() throws Exception
	{
		MiradiShareTaxonomy taxonomy = createMiradiShareTaxonomy();
		taxonomy.setData(MiradiShareTaxonomySchema.TAG_TAXONOMY_CODE, "randomTaxonomyCode1");
		taxonomy.setData(MiradiShareTaxonomySchema.TAG_TAXONOMY_VERSION, "VersionXxx");
		taxonomy.setData(MiradiShareTaxonomySchema.TAG_TAXONOMY_ELEMENTS, createSampleTaxonomyElementListAsJsonString());
		taxonomy.setData(MiradiShareTaxonomySchema.TAG_TAXONOMY_TOP_LEVEL_ELEMENT_CODES, CodeList.createWithSingleCode("RandomCodeXxx").toString());
		
		return taxonomy;
	}
	
	public ObjectTreeTableConfiguration createAndPopulateObjectTreeTableConfiguration() throws Exception
	{
		ObjectTreeTableConfiguration objectTreeTableConfiguration = createObjectTreeTableConfiguration();
		populateObjectTreeTableConfiguration(objectTreeTableConfiguration);
		
		return objectTreeTableConfiguration;
	}
	
	public TableSettings createAndPopulateTableSettings() throws Exception
	{
		TableSettings tableSettings = createTableSettings();
		populateTableSettings(tableSettings);

		return tableSettings;
	}

	public String createSampleTaxonomyElementListAsJsonString()
	{
		TaxonomyElementList taxonomyElementList = new TaxonomyElementList();

		TaxonomyElement taxonomyElement1 = new TaxonomyElement();
		final String URI_ONLY_CHARS = "http+ssh://sample.com@who~ever?http://sample.com?a=1&b=2#some_where";
		taxonomyElement1.setCode(URI_ONLY_CHARS);
		taxonomyElement1.setChildCodes(new CodeList(new String[]{"1",}));
		taxonomyElement1.setLabel("Sample & Label");
		taxonomyElement1.setDescription("Sample >< Description");
		taxonomyElement1.setUserCode("Sample >< User code");
		taxonomyElementList.add(taxonomyElement1);
		
		TaxonomyElement taxonomyElement2 = new TaxonomyElement();
		taxonomyElement2.setCode("randomCodeX");
		taxonomyElement2.setChildCodes(new CodeList(new String[]{"4",}));
		taxonomyElement2.setLabel("Random & Label");
		taxonomyElement2.setDescription("Random ><Description");
		taxonomyElement2.setUserCode("Sample >&'< User code");
		taxonomyElementList.add(taxonomyElement2);

        CodeList sampleCodeList = createSampleFreshwaterEcoregionsCodeList();
        for (String code : sampleCodeList)
        {
            TaxonomyElement taxonomyElement = new TaxonomyElement();
            taxonomyElement.setCode(code);
            taxonomyElement.setChildCodes(new CodeList());
            taxonomyElement.setLabel("Random & Label");
            taxonomyElement.setDescription("Random ><Description");
            taxonomyElement.setUserCode("Sample >&'< User code");
            taxonomyElementList.add(taxonomyElement);
        }

		return taxonomyElementList.toJsonString();
	}

    public String createSampleTaxonomyClassificationsList()
    {
        TaxonomyClassificationMap taxonomyClassificationList = new TaxonomyClassificationMap();
        taxonomyClassificationList.putCodeList("randomTaxonomyCode1", createSampleFreshwaterEcoregionsCodeList());

        return taxonomyClassificationList.toJsonString();
    }

    public String createSampleAccountingClassificationsList()
    {
		TaxonomyClassificationMap accountingClassificationMap = new TaxonomyClassificationMap();
        accountingClassificationMap.putCodeList("randomTaxonomyCode1", createSampleFreshwaterEcoregionsCodeList());

        return accountingClassificationMap.toJsonString();
    }

    public void populateTaxonomyAssociationsForBaseObjectTypes() throws Exception
	{
		Vector<Integer> objectTypesWithTaxonomyAssociationPool = getTypesWithTaxonomyAssociationPools();
		for(Integer taxonomyAssociationParentType : objectTypesWithTaxonomyAssociationPool)
		{
			Vector<String> poolNamesForType = TaxonomyHelper.getTaxonomyAssociationPoolNamesForType(taxonomyAssociationParentType);
			for(String taxonomyAssociationPoolName : poolNamesForType)
			{
				createAndPopulateTaxonomyAssociation(taxonomyAssociationParentType, taxonomyAssociationPoolName);
			}
		}
	}

	private void createAndPopulateTaxonomyAssociation(Integer taxonomyAssociationParentType, final String taxonomyAssociationPoolName) throws Exception
	{
		ORef taxonomyAssociationRef = createObject(TaxonomyAssociationSchema.getObjectType());
		TaxonomyAssociation taxonomyAssociation = TaxonomyAssociation.find(this, taxonomyAssociationRef);
		fillObjectUsingCommand(taxonomyAssociation, TaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_POOL_NAME, taxonomyAssociationPoolName);
		fillObjectUsingCommand(taxonomyAssociation, AbstractTaxonomyAssociationSchema.TAG_BASE_OBJECT_TYPE, taxonomyAssociationParentType);
		fillObjectUsingCommand(taxonomyAssociation, AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE, "RandomAssociationCodeFor?" + taxonomyAssociationPoolName);
		fillObjectUsingCommand(taxonomyAssociation, TaxonomyAssociationSchema.TAG_MULTI_SELECT, TaxonomyMultiSelectModeQuestion.MULTI_SELECT_CODE);
		fillObjectUsingCommand(taxonomyAssociation, AbstractTaxonomyAssociationSchema.TAG_SELECTION_TYPE, TaxonomyClassificationSelectionModeQuestion.ANY_NODE_CODE);
		fillObjectUsingCommand(taxonomyAssociation, AbstractTaxonomyAssociationSchema.TAG_DESCRIPTION, "Some random description");
		fillObjectUsingCommand(taxonomyAssociation, AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_CODE, "randomTaxonomyCode1");
		fillObjectUsingCommand(taxonomyAssociation, BaseObject.TAG_LABEL, "RandomLabel");
	}
	
	public Vector<Integer> getTypesWithTaxonomyAssociationPools()
	{
		Vector<Integer> typesWithTaxonomyAssociationPools = new Vector<Integer>();
		typesWithTaxonomyAssociationPools.add(MiradiShareProjectDataSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(TargetSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(HumanWelfareTargetSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(CauseSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(StrategySchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(ResultsChainDiagramSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(ThreatReductionResultSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(GoalSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(KeyEcologicalAttributeSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(IndicatorSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(ObjectiveSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(StressSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(TaskSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(ProjectResourceSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(ResourceAssignmentSchema.getObjectType());
		typesWithTaxonomyAssociationPools.add(ExpenseAssignmentSchema.getObjectType());

		return typesWithTaxonomyAssociationPools;
	}

	public void populateAccountingClassificationAssociationsForBaseObjectTypes() throws Exception
	{
		Vector<Integer> objectTypesWithAccountingClassificationAssociationPool = getTypesWithAccountingClassificationAssociationPools();
		for(Integer accountingClassificationAssociationParentType : objectTypesWithAccountingClassificationAssociationPool)
		{
			Vector<String> poolNamesForType = TaxonomyHelper.getAccountingClassificationAssociationPoolNamesForType(accountingClassificationAssociationParentType);
			for(String accountingClassificationAssociationPoolName : poolNamesForType)
			{
				createAndPopulateAccountingClassificationAssociation(accountingClassificationAssociationParentType, accountingClassificationAssociationPoolName);
			}
		}
	}

	private void createAndPopulateAccountingClassificationAssociation(Integer accountingClassificationAssociationParentType, final String accountingClassificationAssociationPoolName) throws Exception
	{
		ORef accountingClassificationAssociationRef = createObject(AccountingClassificationAssociationSchema.getObjectType());
		AccountingClassificationAssociation accountingClassificationAssociation = AccountingClassificationAssociation.find(this, accountingClassificationAssociationRef);
		fillObjectUsingCommand(accountingClassificationAssociation, AccountingClassificationAssociationSchema.TAG_ACCOUNTING_CLASSIFICATION_ASSOCIATION_POOL_NAME, accountingClassificationAssociationPoolName);
		fillObjectUsingCommand(accountingClassificationAssociation, AbstractTaxonomyAssociationSchema.TAG_BASE_OBJECT_TYPE, accountingClassificationAssociationParentType);
		fillObjectUsingCommand(accountingClassificationAssociation, AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_ASSOCIATION_CODE, "RandomAssociationCodeFor?" + accountingClassificationAssociationPoolName);
		fillObjectUsingCommand(accountingClassificationAssociation, AbstractTaxonomyAssociationSchema.TAG_SELECTION_TYPE, TaxonomyClassificationSelectionModeQuestion.ANY_NODE_CODE);
		fillObjectUsingCommand(accountingClassificationAssociation, AbstractTaxonomyAssociationSchema.TAG_DESCRIPTION, "Some random description");
		fillObjectUsingCommand(accountingClassificationAssociation, AbstractTaxonomyAssociationSchema.TAG_TAXONOMY_CODE, "randomTaxonomyCode1");
		fillObjectUsingCommand(accountingClassificationAssociation, AccountingClassificationAssociationSchema.TAG_SEQUENCE_NO, 1);
		fillObjectUsingCommand(accountingClassificationAssociation, BaseObject.TAG_LABEL, "RandomLabel");
	}

	public Vector<Integer> getTypesWithAccountingClassificationAssociationPools()
	{
		Vector<Integer> typesWithAccountingClassificationAssociationPools = new Vector<Integer>();
		typesWithAccountingClassificationAssociationPools.add(ResourceAssignmentSchema.getObjectType());
		typesWithAccountingClassificationAssociationPools.add(ExpenseAssignmentSchema.getObjectType());

		return typesWithAccountingClassificationAssociationPools;
	}

    public ORef createResultsChainDiagram() throws Exception
	{
		ORef resultsChainRef = createObject(ResultsChainDiagramSchema.getObjectType());
		ResultsChainDiagram resultsChain = ResultsChainDiagram.find(this, resultsChainRef);
		PersistentDiagramModel resultsChainDiagramModel = new PersistentDiagramModel(this);
		resultsChainDiagramModel.fillFrom(resultsChain);
		
		return resultsChainRef;
	}
	
	public String createXenodata(String externalAppCode, String xenoDataProjectId) throws Exception
	{
		ORef xenodataRef = createAndPopulateXenodata(xenoDataProjectId).getRef();
		StringRefMap refMap = new StringRefMap();
		refMap.add(externalAppCode, xenodataRef);
		
		return refMap.toJsonString();
	}

	public String createSampleXenodata() throws Exception
	{
		return createXenodata("randomCode", "randomProjectId");
	}

	public ProjectResource createAndPopulateProjectResource() throws Exception
	{
		ProjectResource projectResource = createProjectResource();
		populateProjectResource(projectResource);
		
		return projectResource;
	}
	
	public ScopeBox createAndPopulateHumanWelfareScopeBox() throws Exception
	{
		ScopeBox scopeBox = createScopeBox();
		populateScopeBox(scopeBox, ScopeBoxTypeQuestion.HUMAN_WELFARE_TARGET_CODE);
		return scopeBox;
	}
	
	public ScopeBox createAndPopulateBiodiversityScopeBox() throws Exception
	{
		ScopeBox scopeBox = createScopeBox();
		populateScopeBox(scopeBox, ScopeBoxTypeQuestion.BIODIVERSITY_TARGET_CODE);
		return scopeBox;
	}
	
	public Target createAndPopulateTarget() throws Exception
	{
		Target target = createTarget();
		populateTarget(target);
		return target;
	}
	
	public HumanWelfareTarget createAndPopulateHumanWelfareTarget() throws Exception
	{
		HumanWelfareTarget humanWelfareTarget = createHumanWelfareTarget();
		populateAbstractTarget(humanWelfareTarget);
		
		return humanWelfareTarget;
	}
	
	public BiophysicalFactor createAndPopulateBiophysicalFactor() throws Exception
	{
		BiophysicalFactor biophysicalFactor = createBiophysicalFactor();
		populateBiophysicalFactor(biophysicalFactor);

		return biophysicalFactor;
	}

	public BiophysicalResult createAndPopulateBiophysicalResult() throws Exception
	{
        BiophysicalResult biophysicalResult = createBiophysicalResult();
		populateBiophysicalResult(biophysicalResult);

		return biophysicalResult;
	}

	public Stress createAndPopulateStress() throws Exception
	{
		Stress stress = createStress();
		populateStress(stress);
		return stress;
	}
	
	public Cause createAndPopulateThreat() throws Exception
	{
		Cause cause = createCause();
		populateCause(cause);
		enableAsThreat(cause);
		return cause;
	}
	
	public FactorLink createAndPopulateDirectThreatLink() throws Exception
	{
		DiagramLink diagramLink = createAndPopulateDirectThreatDiagramLink();
		ORef targetRef = diagramLink.getToWrappedRef();
		Target target = Target.find(getObjectManager(), targetRef);
		populateDirectThreatLink(diagramLink, target.getRef());

		return diagramLink.getWrappedFactorLink();
	}
	
	public DiagramLink createAndPopulateDirectThreatDiagramLink() throws Exception
	{
		DiagramFactor targetDiagramFactor = createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		Target target = (Target) targetDiagramFactor.getWrappedFactor();
		populateTarget(target);
		
		DiagramFactor threatDiagramFactor = createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		final Cause threat = (Cause) threatDiagramFactor.getWrappedFactor();
		enableAsThreat(threat);
		populateCause(threat);
		
		LinkCreator creator = new LinkCreator(this);
		DiagramLink created = creator.createFactorLinkAndAddToDiagramUsingCommands(getTestingDiagramObject(), threatDiagramFactor, targetDiagramFactor);

		return created;
	}
	
	private DiagramLink createAndPopulateDirectThreatLink(AbstractTarget target) throws Exception
	{
		DiagramFactor diagramFactor = createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		enableAsThreat(diagramFactor.getWrappedORef());
		Cause threat = Cause.find(this, diagramFactor.getWrappedORef());
		populateCause(threat);
		
		FactorCommandHelper factorHelper = new FactorCommandHelper(this, getTestingDiagramModel());
		CommandCreateObject createDiagramFactor = factorHelper.createDiagramFactor(getTestingDiagramObject(), target.getRef());
		DiagramFactor targetDiagramFactor = DiagramFactor.find(this, createDiagramFactor.getObjectRef());
		
		
		ORef diagramLinkRef = createDiagramLinkAndAddToDiagram(diagramFactor, targetDiagramFactor);
		DiagramLink diagramLink = DiagramLink.find(this, diagramLinkRef);		
		populateDirectThreatLink(diagramLink, target.getRef());

		return diagramLink;
	}
	
	public ThreatStressRating createAndPopulateThreatStressRating() throws Exception
	{
		ThreatStressRating threatStressRating = createThreatStressRating();
		populateThreatStressRating(threatStressRating);
		
		return threatStressRating;
	}
	
	public ThreatStressRating createAndPopulateThreatStressRating(ORef stressRef, ORef threatRef) throws Exception
	{
		ThreatStressRating threatStressRating = createThreatStressRating(stressRef, threatRef);
		populateThreatStressRating(threatStressRating);
		
		return threatStressRating;
	}
	
	public SubTarget createAndPopulateSubTarget() throws Exception
	{
		SubTarget subTarget = createSubTarget();
		populateSubTarget(subTarget);
		
		return subTarget;
	}
	
	public KeyEcologicalAttribute createAndPopulateKea() throws Exception
	{
		KeyEcologicalAttribute kea = createKea();
		populateKea(kea);
		
		return kea;
	}
	
	public Indicator createAndPopulateIndicator(BaseObject owner) throws Exception
	{
		Indicator indicator = createIndicator(owner);
		populateIndicator(indicator);
		
		return indicator;
	}
	
	public FutureStatus createAndPopulateFutureStatus(Indicator owner) throws Exception
	{
		FutureStatus indicator = createFutureStatus(owner);
		populateBaseObjectWithSampleData(indicator);
		
		return indicator;
	}
	
	public Task createAndPopulateTask(BaseObject owner, String customTaskLabel) throws Exception
	{
		Task task  = createTask(owner);
		populateTask(task, customTaskLabel);
		
		return task;
	}
	
	public Method createAndPopulateMethod(BaseObject owner, String customMethodLabel) throws Exception
	{
		Method method  = createMethod(owner);
		populateMethod(method, customMethodLabel);
		
		return method;
	}
	
	public Measurement createAndPopulateMeasurement() throws Exception
	{
		Measurement measurement = createMeasurement();
		populateMeasurement(measurement);
		
		return measurement;
	}
	
	public Objective createAndPopulateObjective(Factor factor) throws Exception
	{
		Objective objective = createObjective(factor);
		populateObjective(objective);
		return objective;
	}
	
	public Strategy createAndPopulateStrategy() throws Exception
	{
		Strategy strategy = createStrategy();
		populateStrategy(strategy);
		
		return strategy;
	}
	
	public Indicator createIndicatorContainingWhiteSpacePaddedCode() throws Exception
	{
		Cause threat = createAndPopulateThreat();
		ORefList indicatorRefs = threat.getDirectOrIndirectIndicatorRefs();
		ORef indicatorRef = indicatorRefs.getRefForType(IndicatorSchema.getObjectType());
		Indicator indicator = Indicator.find(this, indicatorRef);
		
		final String STRING_TO_TRIM = "<br/>\t  \t";
		fillObjectUsingCommand(indicator, Indicator.TAG_RATING_SOURCE, STRING_TO_TRIM + RatingSourceQuestion.ONSITE_RESEARCH_CODE + STRING_TO_TRIM);
		
		return indicator;
	}

	public Strategy createAndPopulateDraftStrategy() throws Exception
	{
		Strategy strategy = createAndPopulateStrategy();
		turnOnDraft(strategy);
		
		return strategy;
	}

	public void turnOnDraft(Strategy strategy) throws Exception
	{
		fillObjectUsingCommand(strategy, Strategy.TAG_STATUS, StrategyStatusQuestion.STATUS_DRAFT_CODE);
	}
	
	public ProgressReport createAndPopulateProgressReport() throws Exception
	{
		ProgressReport progressReport = createProgressReport();
		populateProgressReport(progressReport);
		
		return progressReport;
	}
	
	public ProgressPercent createAndPopulateProgressPercent() throws Exception
	{
		ProgressPercent progressPercent = createProgressPercent();
		populateProgressPercent(progressPercent);
		
		return progressPercent;
	}
	
	public Xenodata createAndPopulateXenodata(String xenoDataProjectId) throws Exception
	{
		Xenodata xenodata = createXenodata();
		populateXenodata(xenodata, xenoDataProjectId);
		
		return xenodata;
	}
	
	public DiagramFactor createAndPopulateDiagramFactorGroupBox(DiagramFactor groupBoxChild) throws Exception
	{
		DiagramFactor diagramFactorGroupBox = createDiagramFactorAndAddToDiagram(GroupBoxSchema.getObjectType());
		populateDiagramFactorGroupBox(diagramFactorGroupBox, groupBoxChild);
		
		return diagramFactorGroupBox;	 
	}
	
	public Task createAndPopulateActivity() throws Exception
	{
		Task activity = createActivity();
		populateTask(activity, "Some Activity");
		
		return activity;
	}
	
	public Organization createAndPopulateOrganization() throws Exception
	{
		Organization organization = createOrganization();
		populateOrganization(organization);
		
		return organization;
	}
	
	public BudgetCategoryOne createAndPopulateCategoryOne() throws Exception
	{
		BudgetCategoryOne categoryOne = createCategoryOne();
		populateCategoryOne(categoryOne);
		
		return categoryOne;
	}
	
	public BudgetCategoryTwo createAndPopulateCategoryTwo() throws Exception
	{
		BudgetCategoryTwo categoryTwo = createCategoryTwo();
		populateCategoryTwo(categoryTwo);
		
		return categoryTwo;
	}
	
	public DiagramFactor createAndPopulateDiagramFactor() throws Exception
	{
		DiagramFactor diagramFactor = createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		populateDiagramFactor(diagramFactor);
		
		return diagramFactor;
	}
	
	public DiagramLink createAndPopulateDiagramLink() throws Exception
	{
		ORef diagramLinkRef = createDiagramLink();
		DiagramLink diagramLink = DiagramLink.find(this, diagramLinkRef);
		populateDiagramLink(diagramLink);
		
		return diagramLink;
	}
	
	public DiagramLink createAndPopulateGroupBoxDiagramLink() throws Exception
	{	
		DiagramFactor cause = createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		DiagramFactor causeGroupBox = createDiagramFactorAndAddToDiagram(GroupBoxSchema.getObjectType());
		fillObjectUsingCommand(causeGroupBox, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, new ORefList(cause));
		
		DiagramFactor target = createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		DiagramFactor targetGroupBox = createDiagramFactorAndAddToDiagram(GroupBoxSchema.getObjectType());
		fillObjectUsingCommand(targetGroupBox, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, new ORefList(target));
		
		DiagramLink causeTargetLink = createDiagramLinkAndAddToDiagramModel(cause, target);
		DiagramLink groupToGroupDiagramLink = createDiagramLinkAndAddToDiagramModel(causeGroupBox, targetGroupBox);
		
		fillObjectUsingCommand(groupToGroupDiagramLink, DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, new ORefList(causeTargetLink));
		
		return groupToGroupDiagramLink;
	}
	
	public ExpenseAssignment createAndPopulateExpenseAssignment() throws Exception
	{
		ExpenseAssignment expenseAssignment = createExpenseAssignment();
		populateExpenseAssignment(expenseAssignment);
		
		return expenseAssignment;
	}
	
	public ResourceAssignment createAndPopulateResourceAssignment() throws Exception
	{
		ResourceAssignment resourceAssignment = createResourceAssignment();
		populateResourceAssignment(resourceAssignment);
		
		return resourceAssignment;
	}
	
	public ProjectResource createProjectResource() throws Exception
	{
		ORef projectResourceRef = createObject(ProjectResourceSchema.getObjectType());
		return ProjectResource.find(this, projectResourceRef);
	}
	
	public ScopeBox createScopeBox() throws Exception
	{
		ORef scopeBoxRef = createObject(ScopeBoxSchema.getObjectType());
		
		return ScopeBox.find(this, scopeBoxRef);
	}
	
	public Target createTarget() throws Exception
	{
		ORef targetRef = createObject(TargetSchema.getObjectType(), new FactorId(takeNextId(TargetSchema.getObjectType())));
		return Target.find(this, targetRef);
	}
	
	public Target createKeaModeTarget() throws Exception
	{
		Target target = createTarget();
		turnOnTncMode(target);
		
		return target;
	}
	
	public HumanWelfareTarget createKeaModeHumanWelfareTarget() throws Exception
	{
		HumanWelfareTarget target = createHumanWelfareTarget();
		turnOnTncMode(target);
		
		return target;
	}
	
	public HumanWelfareTarget createHumanWelfareTarget() throws Exception
	{
		ORef ref = createObject(HumanWelfareTargetSchema.getObjectType(), new FactorId(takeNextId(HumanWelfareTargetSchema.getObjectType())));
		return HumanWelfareTarget.find(this, ref);
	}
	
	public BiophysicalFactor createBiophysicalFactor() throws Exception
	{
		ORef ref = createObject(BiophysicalFactorSchema.getObjectType(), new FactorId(takeNextId(BiophysicalFactorSchema.getObjectType())));
		return BiophysicalFactor.find(this, ref);
	}

	public BiophysicalResult createBiophysicalResult() throws Exception
	{
		ORef ref = createObject(BiophysicalResultSchema.getObjectType(), new FactorId(takeNextId(BiophysicalResultSchema.getObjectType())));
		return BiophysicalResult.find(this, ref);
	}

	public Stress createStress() throws Exception
	{
		ORef stressRef = createObject(StressSchema.getObjectType());
		return Stress.find(this, stressRef);
	}
	
	public Cause createCause() throws Exception
	{
		ORef threatRef = createObject(CauseSchema.getObjectType());
		return Cause.find(this, threatRef);
	}
	
	public MiradiShareProjectData populateMiradiShareProjectData(MiradiShareProjectData miradiShareProjectData) throws Exception
	{
		populateBaseObject(miradiShareProjectData);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROGRAM_ID);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROJECT_ID);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROJECT_URL);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROGRAM_ID);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROGRAM_NAME);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROGRAM_URL);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROJECT_TEMPLATE_ID);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROJECT_TEMPLATE_NAME);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROJECT_VERSION);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_NAME);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION_ID);
		fillObjectWithSampleStringData(miradiShareProjectData, MiradiShareProjectData.TAG_PROGRAM_TAXONOMY_SET_VERSION);
		
		return miradiShareProjectData;
	}
	
	public void populateBaseObject(BaseObject baseObject) throws Exception
	{
		fillObjectUsingCommand(baseObject, BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER, createSampleTaxonomyClassificationsList());
	}
	
	public ORef populateSimpleThreatRatingValues() throws Exception
	{
		DiagramLink diagramLink = createThreatTargetDiagramLink();
		ORef threatRef = diagramLink.getFromDiagramFactor().getWrappedORef();
		ORef targetRef = diagramLink.getToDiagramFactor().getWrappedORef();
		
		SimpleThreatRatingFramework framework = getSimpleThreatRatingFramework();
		framework.setScope(threatRef, targetRef, SimpleThreatRatingFramework.HIGH_RATING_VALUE);
		framework.setSeverity(threatRef, targetRef, SimpleThreatRatingFramework.MEDIUM_RATING_VALUE);
		framework.setIrreversibility(threatRef, targetRef, SimpleThreatRatingFramework.MEDIUM_RATING_VALUE);
		
		return targetRef;
	}
	
	public DiagramLink createThreatTargetDiagramLinkWithRating() throws Exception
	{
		DiagramLink diagramLink = createThreatTargetDiagramLink();
		switchToStressBaseMode();
		DiagramFactor diagramFactorTarget = diagramLink.getToDiagramFactor();
		Target target = (Target) diagramFactorTarget.getWrappedFactor();
		Stress stress = createAndPopulateStress();
		fillObjectUsingCommand(target, Target.TAG_STRESS_REFS, new ORefList(stress));
		
		DiagramFactor diagramFactorCause = diagramLink.getFromDiagramFactor();
		createDiagramFactorLinkAndAddToDiagram(diagramFactorCause, diagramFactorTarget);
				
		return diagramLink;
	}
	
	public DiagramLink createThreatTargetDiagramLink() throws Exception
	{
		DiagramFactor diagramFactorTarget = createDiagramFactorAndAddToDiagram(TargetSchema.getObjectType());
		DiagramFactor diagramFactorCause = createDiagramFactorAndAddToDiagram(CauseSchema.getObjectType());
		Cause threat = (Cause) diagramFactorCause.getWrappedFactor();
		enableAsThreat(threat);
		ORef diagramLink = createDiagramFactorLinkAndAddToDiagram(diagramFactorCause, diagramFactorTarget);
		
		return DiagramLink.find(this, diagramLink);
	}
	
	public ThreatStressRating createThreatStressRating() throws Exception
	{
		Stress stress = createAndPopulateStress();
		Cause threat = createAndPopulateThreat();
		return createThreatStressRating(stress.getRef(), threat.getRef());
	}

	public ThreatStressRating createThreatStressRating(ORef stressRef, ORef threatRef) throws Exception
	{
		ORef threatStressRatingRef = createObject(ThreatStressRatingSchema.getObjectType());
		setObjectData(threatStressRatingRef, ThreatStressRating.TAG_STRESS_REF, stressRef.toString());
		setObjectData(threatStressRatingRef, ThreatStressRating.TAG_THREAT_REF, threatRef.toString());
		setObjectData(threatStressRatingRef, ThreatStressRating.TAG_IS_ACTIVE, BooleanData.BOOLEAN_TRUE);

		return ThreatStressRating.find(this, threatStressRatingRef);
	}
	
	public SubTarget createSubTarget() throws Exception
	{
		ORef subTargetRef = createObject(SubTargetSchema.getObjectType());
		return SubTarget.find(this, subTargetRef);
	}
	
	public KeyEcologicalAttribute createKea(AbstractTarget target) throws Exception
	{
		KeyEcologicalAttribute kea = createKea();
		IdList keaIds = new IdList(KeyEcologicalAttributeSchema.getObjectType());
		keaIds.addRef(kea.getRef());
		fillObjectUsingCommand(target, AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaIds.toString());
		
		return kea;
	}
	
	public KeyEcologicalAttribute createKea() throws Exception
	{
		ORef keaRef = createObject(KeyEcologicalAttributeSchema.getObjectType());
		return KeyEcologicalAttribute.find(this, keaRef);
	}
	
	public Indicator createIndicator(BaseObject owner) throws Exception
	{
		ORef indicatorRef = createObject(IndicatorSchema.getObjectType());
		return addIndicatorToOwner(owner, indicatorRef);
	}

	public Indicator addIndicatorToOwner(BaseObject owner, ORef indicatorRef) throws Exception
	{
		IdList indicatorIds = new IdList(IndicatorSchema.getObjectType());
		indicatorIds.addRef(indicatorRef);	
		fillObjectUsingCommand(owner, Factor.TAG_INDICATOR_IDS, indicatorIds.toString());
		
		return Indicator.find(this, indicatorRef);
	}
	
	public Indicator createIndicatorWithCauseParent() throws Exception
	{
		Cause cause = createCause();
		return createIndicator(cause);
	}
	
	public FutureStatus createFutureStatus(Indicator owner) throws Exception
	{
		ORef futureStatusRef = createObject(FutureStatusSchema.getObjectType());
		FutureStatus futureStatus = FutureStatus.find(this, futureStatusRef);
		ORefList futureStatusRefs = new ORefList(owner.getFutureStatusRefs());
		futureStatusRefs.add(futureStatusRef);
		fillObjectUsingCommand(owner, Indicator.TAG_FUTURE_STATUS_REFS, futureStatusRefs);
		
		return futureStatus;
	}
	
	public BaseObject createBaseObject(int objectType) throws Exception
	{
		ORef baseObjectRef = createObject(objectType);
		return BaseObject.find(this, baseObjectRef);
	}
	
	public Task createTask(BaseObject owner) throws Exception
	{
		ORef taskRef = createObject(TaskSchema.getObjectType());
		Task task = Task.find(this, taskRef);
		String tag = Task.getTaskIdsTag(owner);
		appendTaskToParentIdList(owner, task, tag);
		return Task.find(this, taskRef);
	}
	
	public Method createMethod(BaseObject owner) throws Exception
	{
		ORef MethodRef = createObject(MethodSchema.getObjectType());
		Method method = Method.find(this, MethodRef);
		appendMethodToParentIdList(owner, method, Indicator.TAG_METHOD_IDS);
		return Method.find(this, MethodRef);
	}
	
	public Measurement createMeasurement() throws Exception
	{
		ORef measurementRef = createObject(MeasurementSchema.getObjectType());
		return Measurement.find(this, measurementRef);
	}
	
	public Objective createObjective(Factor owner) throws Exception
	{
		ORef objectiveRef = createObject(ObjectiveSchema.getObjectType());
		CommandSetObjectData append = CommandSetObjectData.createAppendIdCommand(owner, Factor.TAG_OBJECTIVE_IDS, objectiveRef.getObjectId());
		executeCommand(append);
		Objective objective = Objective.find(this, objectiveRef);
		return objective;
	}
	
	public Goal createGoal(AbstractTarget targetOwner) throws Exception
	{
		ORef goalRef = createObject(GoalSchema.getObjectType());
		CommandSetObjectData append = CommandSetObjectData.createAppendIdCommand(targetOwner, AbstractTarget.TAG_GOAL_IDS, goalRef.getObjectId());
		executeCommand(append);
		
		return Goal.find(this, goalRef);
	}
	
	public TextBox createTextBox() throws Exception
	{
		ORef textBoxRef = createObject(TextBoxSchema.getObjectType());
		
		return TextBox.find(this, textBoxRef);
	}
	
	public GroupBox createGroupBox() throws Exception
	{
		ORef groupBoxRef = createObject(GroupBoxSchema.getObjectType());
		
		return GroupBox.find(this, groupBoxRef);
	}
	
	public Audience createAudience() throws Exception
	{
		ORef audienceRef = createObject(AudienceSchema.getObjectType());
		
		return Audience.find(this, audienceRef);
	}
	
	public OtherNotableSpecies createOtherNotableSpecies() throws Exception
	{
		ORef otherNotableSpeciesRef = createObject(OtherNotableSpeciesSchema.getObjectType());
		return OtherNotableSpecies.find(this, otherNotableSpeciesRef);
	}
	
	public IucnRedlistSpecies createIucnRedlistSpecies() throws Exception
	{
		ORef iucnRedlistSpeciesRef = createObject(IucnRedlistSpeciesSchema.getObjectType());
		return IucnRedlistSpecies.find(this, iucnRedlistSpeciesRef);
	}
	
	public Strategy createStrategy() throws Exception
	{
		ORef strategyRef = createObject(StrategySchema.getObjectType(), new FactorId(takeNextId(StrategySchema.getObjectType())));
		return Strategy.find(this, strategyRef);
	}
	
	public ProgressReport createProgressReport() throws Exception
	{
		ORef progressReportRef = createObject(ProgressReportSchema.getObjectType());
		return ProgressReport.find(this, progressReportRef);
	}
	
	public ProgressPercent createProgressPercent() throws Exception
	{
		ORef progressPercentRef = createObject(ProgressPercentSchema.getObjectType());
		return ProgressPercent.find(this, progressPercentRef);
	}
	
	private Xenodata createXenodata() throws Exception
	{
		ORef xenodataRef = createObject(XenodataSchema.getObjectType());
		return Xenodata.find(this, xenodataRef);
	}
	
	public TaggedObjectSet createTaggedObjectSet() throws Exception
	{
		ORef taggedObjectSetRef = createObject(TaggedObjectSetSchema.getObjectType());
		return TaggedObjectSet.find(this, taggedObjectSetRef);
	}
	
	public TaggedObjectSet createLabeledTaggedObjectSet(String labelToUse) throws Exception
	{
		TaggedObjectSet taggedObjectSet = createTaggedObjectSet();
		setObjectData(taggedObjectSet.getRef(), TaggedObjectSet.TAG_LABEL, labelToUse);
		
		return taggedObjectSet;
	}
	
	public ObjectTreeTableConfiguration createObjectTreeTableConfiguration() throws Exception
	{
		ORef objectTreeTableConfigurationRef = createObject(ObjectTreeTableConfigurationSchema.getObjectType());
		return ObjectTreeTableConfiguration.find(this, objectTreeTableConfigurationRef);
	}
	
	public TableSettings createTableSettings() throws Exception
	{
		ORef tableSettingsRef = createObject(TableSettingsSchema.getObjectType());
		return TableSettings.find(this, tableSettingsRef);
	}

	public void tagDiagramFactor(ORef refToTag) throws Exception
	{
		TaggedObjectSet taggedObjectSet = createTaggedObjectSet();
		setObjectData(taggedObjectSet, TaggedObjectSet.TAG_LABEL, "SomeTag");
		ORefList taggedFactorRefs = new ORefList(refToTag);
		setObjectData(taggedObjectSet, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedFactorRefs.toString());
		
		ORefList taggedObjectSetRefs = new ORefList(taggedObjectSet.getRef());
		setObjectData(getTestingDiagramObject(), DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, taggedObjectSetRefs.toString());
	}
	
	public void addAccountingCode(ResourceAssignment resourceAssignment) throws Exception
	{
		ORef accountingCodeRef = createAccountingCode().getRef();
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, accountingCodeRef.getObjectId().toString());
	}
	
	public ResourceAssignment createResourceAssignment() throws Exception
	{
		ORef assignmentRef = createObject(ResourceAssignmentSchema.getObjectType());
		return ResourceAssignment.find(this, assignmentRef);
	}
	
	public ExpenseAssignment createExpenseAssignment() throws Exception
	{
		ORef expenseAssignmentRef = createObject(ExpenseAssignmentSchema.getObjectType());
		return ExpenseAssignment.find(this, expenseAssignmentRef);
	}
	
	public FundingSource createFundingSource() throws Exception
	{
		ORef fundingSourceRef = createObject(FundingSourceSchema.getObjectType());
		return FundingSource.find(this, fundingSourceRef);
	}
	
	public AccountingCode createAccountingCode() throws Exception
	{
		ORef accountingCodeRef = createObject(AccountingCodeSchema.getObjectType());
		return AccountingCode.find(this, accountingCodeRef);
	}
	
	public BudgetCategoryOne createCategoryOne() throws Exception
	{
		ORef categoryOneRef = createObject(BudgetCategoryOneSchema.getObjectType());
		return BudgetCategoryOne.find(this, categoryOneRef);
	}
	
	public BudgetCategoryTwo createCategoryTwo() throws Exception
	{
		ORef categoryTwoRef = createObject(BudgetCategoryTwoSchema.getObjectType());
		return BudgetCategoryTwo.find(this, categoryTwoRef);
	}

	public ThreatReductionResult createThreatReductionResult() throws Exception
	{
		ORef threatReductionResultRef = createObject(ThreatReductionResultSchema.getObjectType());
		return ThreatReductionResult.find(this, threatReductionResultRef);
	}
	
	public Organization createOrganization() throws Exception
	{
		ORef organizationRef = createObject(OrganizationSchema.getObjectType());
		return Organization.find(this, organizationRef);
	}
	
	public void populateScopeBox(ScopeBox scopeBox, String scopeBoxTypeCode) throws Exception
	{
		fillObjectWithSampleStringData(scopeBox, ScopeBox.TAG_LABEL);
		fillObjectWithSampleStringData(scopeBox, ScopeBox.TAG_SHORT_LABEL);
		fillObjectWithSampleStringData(scopeBox, ScopeBox.TAG_TEXT);
		fillObjectWithSampleStringData(scopeBox, ScopeBox.TAG_COMMENTS);
		fillObjectUsingCommand(scopeBox, ScopeBox.TAG_SCOPE_BOX_TYPE_CODE, scopeBoxTypeCode);
	}
	
	public void populateTarget(Target target) throws Exception
	{
		populateAbstractTarget(target);
				
		CodeList habitatCodes = new CodeList();
		habitatCodes.add(HabitatAssociationQuestion.FOREST_CODE);
		habitatCodes.add(HabitatAssociationQuestion.SAVANNA_CODE);
		fillObjectUsingCommand(target, Target.TAG_HABITAT_ASSOCIATION, habitatCodes.toString());
		
		createAndPopulateDirectThreatLink(target);
		
		ORefList stressRefs = new ORefList(createAndPopulateStress().getRef());
		fillObjectUsingCommand(target, Target.TAG_STRESS_REFS, stressRefs.toString());		
	}
	
	public void populateAbstractTarget(AbstractTarget target) throws Exception
	{
		fillObjectUsingCommand(target, AbstractTarget.TAG_LABEL, "Reefs " + target.getId().toString());
		fillObjectUsingCommand(target, AbstractTarget.TAG_TEXT, "Some Description Text");
		fillObjectUsingCommand(target, AbstractTarget.TAG_COMMENTS, "Some comment Text");
		fillObjectUsingCommand(target, AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION, "Some status justification");
		fillObjectUsingCommand(target, AbstractTarget.TAG_TARGET_STATUS, StatusQuestion.VERY_GOOD);
		turnOnTncMode(target);
				
		SubTarget subTarget = createAndPopulateSubTarget();
		ORefList subTargetRefs = new ORefList(subTarget.getRef());
		fillObjectUsingCommand(target, AbstractTarget.TAG_SUB_TARGET_REFS, subTargetRefs.toString());
		
		KeyEcologicalAttribute kea = createAndPopulateKea();
		IdList keaIds = new IdList(KeyEcologicalAttributeSchema.getObjectType());
		keaIds.addRef(kea.getRef());
		fillObjectUsingCommand(target, AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaIds.toString());
	}

	public void populateBiophysicalFactor(BiophysicalFactor biophysicalFactor) throws Exception
	{
		fillObjectUsingCommand(biophysicalFactor, BiophysicalFactor.TAG_LABEL, "Biophysical Factor " + biophysicalFactor.getId().toString());
		fillObjectUsingCommand(biophysicalFactor, BiophysicalFactor.TAG_TEXT, "Some Description Text");
		fillObjectUsingCommand(biophysicalFactor, BiophysicalFactor.TAG_COMMENTS, "Some comment Text");

        IdList objectiveIds = new IdList(ObjectiveSchema.getObjectType());
        final Objective objective = createAndPopulateObjective(biophysicalFactor);
        objectiveIds.addRef(objective.getRef());
        fillObjectUsingCommand(biophysicalFactor, Factor.TAG_OBJECTIVE_IDS, objectiveIds.toString());

        IdList indicatorIds = new IdList(IndicatorSchema.getObjectType());
        final Indicator indicator = createAndPopulateIndicator(biophysicalFactor);
        indicatorIds.addRef(indicator.getRef());
        fillObjectUsingCommand(biophysicalFactor, BiophysicalFactor.TAG_INDICATOR_IDS, indicatorIds.toString());
	}

	public void populateBiophysicalResult(BiophysicalResult biophysicalResult) throws Exception
	{
		fillObjectUsingCommand(biophysicalResult, BiophysicalResult.TAG_LABEL, "Biophysical Result " + biophysicalResult.getId().toString());
		fillObjectUsingCommand(biophysicalResult, BiophysicalResult.TAG_TEXT, "Some Description Text");
		fillObjectUsingCommand(biophysicalResult, BiophysicalResult.TAG_COMMENTS, "Some comment Text");

        IdList objectiveIds = new IdList(ObjectiveSchema.getObjectType());
        final Objective objective = createAndPopulateObjective(biophysicalResult);
        objectiveIds.addRef(objective.getRef());
        fillObjectUsingCommand(biophysicalResult, Factor.TAG_OBJECTIVE_IDS, objectiveIds.toString());

        IdList indicatorIds = new IdList(IndicatorSchema.getObjectType());
        final Indicator indicator = createAndPopulateIndicator(biophysicalResult);
        indicatorIds.addRef(indicator.getRef());
        fillObjectUsingCommand(biophysicalResult, BiophysicalResult.TAG_INDICATOR_IDS, indicatorIds.toString());
	}

	public void turnOnTncMode(AbstractTarget target) throws Exception
	{
		fillObjectUsingCommand(target, AbstractTarget.TAG_VIABILITY_MODE, ViabilityModeQuestion.TNC_STYLE_CODE);
	}
	
	public void turnOffVisibleQuarterColumns() throws Exception
	{
		fillObjectUsingCommand(getMetadata(), ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY, QuarterColumnsVisibilityQuestion.HIDE_QUARTER_COLUMNS_CODE);
	}

	public void turnOffVisibleDayColumns() throws Exception
	{
		fillObjectUsingCommand(getMetadata(), ProjectMetadata.TAG_DAY_COLUMNS_VISIBILITY, DayColumnsVisibilityQuestion.HIDE_DAY_COLUMNS_CODE);
	}

	public void populateCause(Cause cause) throws Exception
	{
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_LABEL, "SomeCauseLabel");
		
		ChoiceQuestion question = StaticQuestionManager.getQuestion(ThreatClassificationQuestion.class);
		final int FIRST_CODE = 0;
		fillObjectUsingCommand(cause.getRef(), Cause.TAG_TAXONOMY_CODE, question.getCode(FIRST_CODE));
		
		IdList indicatorIds = new IdList(IndicatorSchema.getObjectType());
		indicatorIds.addRef(createAndPopulateIndicator(cause).getRef());	
		fillObjectUsingCommand(cause, Cause.TAG_INDICATOR_IDS, indicatorIds.toString());
	}
	
	public void populateStress(Stress stress) throws Exception
	{
		fillObjectUsingCommand(stress, Stress.TAG_LABEL, "SomeStressLabel");
		fillObjectUsingCommand(stress, Stress.TAG_DETAIL, "Some Stress Details");
		fillObjectUsingCommand(stress, Stress.TAG_COMMENTS, "Some Stress Comments");
		fillObjectUsingCommand(stress, Stress.TAG_SEVERITY, StatusQuestion.GOOD);
		fillObjectUsingCommand(stress, Stress.TAG_SCOPE, StatusQuestion.GOOD);
	}
	
	public void populateDirectThreatLink(DiagramLink directThreatDiagramLink, ORef targetRef) throws Exception
	{
		ORef threatRef = getUpstreamThreatRef(directThreatDiagramLink);
		ThreatStressRatingHelper helper = new ThreatStressRatingHelper(this);
		ORefList threatStressRatingRefs = helper.getRelatedThreatStressRatingRefs(threatRef, targetRef);
		for (int index = 0; index < threatStressRatingRefs.size(); ++index)
		{
			ThreatStressRating threatStressRating = ThreatStressRating.find(this, threatStressRatingRefs.get(index));
			populateThreatStressRating(threatStressRating);
		}
	}
	
	public void populateThreatStressRating(ThreatStressRating threatStressRating) throws Exception
	{
		fillObjectUsingCommand(threatStressRating, ThreatStressRating.TAG_IS_ACTIVE, BooleanData.BOOLEAN_TRUE);
		fillObjectUsingCommand(threatStressRating, ThreatStressRating.TAG_IRREVERSIBILITY, StressIrreversibilityQuestion.HIGH_RATING_CODE);
		fillObjectUsingCommand(threatStressRating, ThreatStressRating.TAG_CONTRIBUTION, StressContributionQuestion.HIGH_RATING_CODE);
	}
	
	private void populateProjectResource(ProjectResource projectResource) throws Exception
	{
		ChoiceQuestion roleQuestion = StaticQuestionManager.getQuestion(ResourceRoleQuestion.class);
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_ROLE_CODES, roleQuestion.getAllCodes().toString());
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_LABEL, PROJECT_RESOURCE_LABEL_TEXT);
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_GIVEN_NAME, "María");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_SUR_NAME, "Doe");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_LOCATION, "1 SomeStreet ave. Tampa FL 33600");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_PHONE_NUMBER, "555-555-5555");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_ORGANIZATION, "TurtleWise Corp");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_RESOURCE_TYPE, ResourceTypeQuestion.GROUP_CODE);
		fillCostPerUnitField(projectResource, "10");
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_IS_CCN_COACH, BooleanData.BOOLEAN_TRUE);
		fillObjectUsingCommand(projectResource, BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER, createSampleTaxonomyClassificationsList());
	}
	
	public void populateSubTarget(SubTarget subTarget) throws Exception
	{
		fillObjectUsingCommand(subTarget, SubTarget.TAG_LABEL, "Some SubTarget Label");
		fillObjectUsingCommand(subTarget, SubTarget.TAG_SHORT_LABEL, "ShortL");
		fillObjectUsingCommand(subTarget, SubTarget.TAG_DETAIL, "Some SubTarget detail text");
	}
	
	public void populateKea(KeyEcologicalAttribute kea) throws Exception
	{
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_LABEL, "Some Kea Label");
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_SHORT_LABEL, "kea");
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, KeyEcologicalAttributeTypeQuestion.CONDITION);
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_DETAILS, "Some kea details text");
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_DESCRIPTION, "Some kea description text");
		
		IdList indicatorIds = new IdList(IndicatorSchema.getObjectType());
		indicatorIds.add(createAndPopulateIndicator(kea).getId());
		indicatorIds.add(createAndPopulateIndicator(kea).getId());
		Indicator indicatorWithoutThreshold = createAndPopulateIndicator(kea);
		fillObjectUsingCommand(indicatorWithoutThreshold, Indicator.TAG_THRESHOLDS_MAP, "");
		indicatorIds.add(indicatorWithoutThreshold.getId());
		
		fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorIds);
	}
	
	public void addThresholdWithXmlEscapedData(Indicator indicator) throws Exception
	{
		addPoorThresholdValue(indicator, "&gt;50%");
	}

	public void addPoorThresholdValue(Indicator indicator, final String thresholdValue) throws Exception
	{
		CodeToUserStringMap threshold = new CodeToUserStringMap();
		threshold.putUserString(StatusQuestion.POOR, thresholdValue);
		fillObjectUsingCommand(indicator, Indicator.TAG_THRESHOLDS_MAP, threshold.toJsonString());
		fillObjectUsingCommand(indicator, Indicator.TAG_THRESHOLD_DETAILS_MAP, threshold.toJsonString());
	}
	
	public void populateIndicator(Indicator indicator) throws Exception
	{
		fillObjectUsingCommand(indicator, Indicator.TAG_LABEL, "Some Indicator Label");
		fillObjectUsingCommand(indicator, Indicator.TAG_UNIT, "km/hr");
		fillObjectUsingCommand(indicator, Indicator.TAG_PRIORITY, PriorityRatingQuestion.HIGH_CODE);
		fillObjectUsingCommand(indicator, Indicator.TAG_DETAIL, "Some Indicator detail");
		fillObjectUsingCommand(indicator, Indicator.TAG_VIABILITY_RATINGS_COMMENTS, "Some Indicator viability ratings comment");
		fillObjectUsingCommand(indicator, Indicator.TAG_RATING_SOURCE, RatingSourceQuestion.ONSITE_RESEARCH_CODE);
		
		createAndPopulateMethod(indicator, "Some Method Name");

		Measurement measurement = createAndPopulateMeasurement();
		ORefList measurementRefs = new ORefList(measurement.getRef());
		fillObjectUsingCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, measurementRefs.toString());
		
		addProgressReport(indicator);
		
		CodeToUserStringMap threshold = new CodeToUserStringMap();
		threshold.putUserString(StatusQuestion.POOR, "poor text" + getSingleLineSampleData());
		threshold.putUserString(StatusQuestion.FAIR, "fair text" + getSingleLineSampleData());
		threshold.putUserString(StatusQuestion.GOOD, "good text" + getSingleLineSampleData());
		threshold.putUserString(StatusQuestion.VERY_GOOD, "very good text" + getSingleLineSampleData());
		fillObjectUsingCommand(indicator, Indicator.TAG_THRESHOLDS_MAP, threshold.toJsonString());
		
		CodeToUserStringMap thresholdDetails = new CodeToUserStringMap();
		thresholdDetails.putUserString(StatusQuestion.POOR, "poor details" + getSingleLineSampleData());
		thresholdDetails.putUserString(StatusQuestion.FAIR, "fair details" + getSingleLineSampleData());
		thresholdDetails.putUserString(StatusQuestion.GOOD, "good details" + getSingleLineSampleData());
		thresholdDetails.putUserString(StatusQuestion.VERY_GOOD, "very good details");
		fillObjectUsingCommand(indicator, Indicator.TAG_THRESHOLD_DETAILS_MAP, thresholdDetails.toJsonString());
		
		fillObjectUsingCommand(indicator, Indicator.TAG_COMMENTS, "Some indicator Comment");
		
		createAndPopulateFutureStatus(indicator);
	}
	
	public void populateTask(Task task, String customLabel) throws Exception
	{
		populateTask(task, customLabel, false);
	}
	
	public void populateTask(Task task, String customLabel, boolean isMonitoringActivity) throws Exception
	{
		fillObjectUsingCommand(task, Task.TAG_LABEL, customLabel);
		fillObjectUsingCommand(task, Task.TAG_DETAILS, "Some Task details");
		fillObjectUsingCommand(task, Task.TAG_IS_MONITORING_ACTIVITY, isMonitoringActivity ? BooleanData.BOOLEAN_TRUE : BooleanData.BOOLEAN_FALSE);
		addResourceAssignment(task);

		addExpenseWithValue(task);
	}

	public void populateMethod(Method method, String customLabel) throws Exception
	{
		fillObjectUsingCommand(method, Method.TAG_LABEL, customLabel);
		fillObjectUsingCommand(method, Method.TAG_DETAILS, "Some Method details");
	}

	public void populateMeasurement(Measurement measurement) throws Exception
	{
		fillObjectUsingCommand(measurement, Measurement.TAG_STATUS, StatusQuestion.GOOD);
		fillObjectUsingCommand(measurement, Measurement.TAG_DATE, "2007-03-19");
		fillObjectUsingCommand(measurement, Measurement.TAG_STATUS_CONFIDENCE, StatusConfidenceQuestion.ROUGH_GUESS_CODE);
		fillObjectUsingCommand(measurement, Measurement.TAG_TREND, TrendQuestion.STRONG_DECREASE_CODE);
		fillObjectUsingCommand(measurement, Measurement.TAG_COMMENTS, "Some Measurement comment");
		fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_PRECISION, "3");
		fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_PRECISION_TYPE, PrecisionTypeQuestion.SD_CODE);
		fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_SIZE, "10");
	}
	
	public void populateTextBox(TextBox textBox) throws Exception
	{
		fillObjectWithSampleStringData(textBox, TextBox.TAG_LABEL);
	}
	
	public void populateGroupBox(GroupBox groupBox) throws Exception
	{
		fillObjectWithSampleStringData(groupBox, GroupBox.TAG_LABEL);
	}
	
	public void populateAudience(Audience audience) throws Exception
	{
		fillObjectWithSampleStringData(audience, Audience.TAG_LABEL);
		fillObjectWithSampleStringData(audience, Audience.TAG_SUMMARY);
		fillObjectUsingCommand(audience, Audience.TAG_PEOPLE_COUNT, "12");
	}
	
	public void populateOtherNotableSpecies(OtherNotableSpecies otherNotableSpecies) throws Exception
	{
		fillObjectWithSampleStringData(otherNotableSpecies, OtherNotableSpecies.TAG_LABEL);
	}
	
	public void populateIucnRedlistSpecies(IucnRedlistSpecies iucnRedlistSpecies) throws Exception
	{
		fillObjectWithSampleStringData(iucnRedlistSpecies, IucnRedlistSpecies.TAG_LABEL);
	}
	
	public void populateObjective(Objective objective) throws Exception
	{
		fillObjectUsingCommand(objective, Objective.TAG_SHORT_LABEL, "123");
		fillObjectUsingCommand(objective, Objective.TAG_LABEL, "Some Objective label");
		fillObjectUsingCommand(objective, Objective.TAG_FULL_TEXT, "Some objective full text data");
		fillObjectUsingCommand(objective, Objective.TAG_COMMENTS, "Some Objective comments");
		
		Cause threat = createAndPopulateThreat();
		ORefList relevantIndicatorRefs = threat.getObjectiveRefs();
		addRelevantBaseObjects(objective, relevantIndicatorRefs, Objective.TAG_RELEVANT_INDICATOR_SET);
		
		ProgressPercent populatedProgressPercent = createAndPopulateProgressPercent();
		ProgressPercent emptyProgressPercent = createProgressPercent();
		ORefList progressPercentRefs = new ORefList();
		progressPercentRefs.add(populatedProgressPercent.getRef());
		progressPercentRefs.add(emptyProgressPercent.getRef());
		fillObjectUsingCommand(objective, Objective.TAG_PROGRESS_PERCENT_REFS, progressPercentRefs.toString());
	}
	
	public Goal createAndPopulateGoal(AbstractTarget targetOwner) throws Exception
	{
		Goal goal = createGoal(targetOwner);
		
		fillRelevantIndicators(goal);
		
		return goal;
	}
	
	public void createAndPopulateTextBox() throws Exception
	{
		TextBox textBox = createTextBox();	
		populateTextBox(textBox);
	}
	
	public void createAndPopulateGroupBox() throws Exception
	{
		GroupBox groupBox = createGroupBox();
		populateGroupBox(groupBox);
	}
	
	public void createAndPopulateAudience() throws Exception
	{
		Audience audience = createAudience();
		populateAudience(audience);
	}
	
	public void createAndPopulateOtherNotableSpecies() throws Exception
	{
		OtherNotableSpecies otherNotableSpecies = createOtherNotableSpecies();
		populateOtherNotableSpecies(otherNotableSpecies);
	}
	
	public void createAndPopulateIucnRedlistSpecies() throws Exception
	{
		IucnRedlistSpecies iucnRedlistSpecies = createIucnRedlistSpecies();
		populateIucnRedlistSpecies(iucnRedlistSpecies);
	}
	
	private void fillRelevantIndicators(Goal goal) throws Exception
	{
		Cause cause = createCause();
		Indicator indicator = createIndicator(cause);
		addSingleItemRelevantBaseObject(goal, indicator, Goal.TAG_RELEVANT_INDICATOR_SET);
	}

	public void addSingleItemRelevantBaseObject(Desire desire, BaseObject baseObject, final String relevancyTag) throws Exception
	{
		addRelevantBaseObjects(desire, new ORefList(baseObject), relevancyTag);
	}

	public void addRelevantBaseObjects(Desire desire, ORefList relevantBaseObjectRefs, final String relevancyTag) throws Exception
	{
		RelevancyOverrideSet relevantBaseObjects = new RelevancyOverrideSet();
		for (int index = 0; index < relevantBaseObjectRefs.size(); ++index)
		{
			relevantBaseObjects.add(new RelevancyOverride(relevantBaseObjectRefs.get(index), true));
		}
			
		fillObjectUsingCommand(desire, relevancyTag, relevantBaseObjects.toString());
	}
	
	public void populateStrategy(Strategy strategy) throws Exception
	{
		fillObjectUsingCommand(strategy, Strategy.TAG_LABEL, "Some Strategy label");
		fillObjectUsingCommand(strategy, Strategy.TAG_COMMENTS, "Some Strategy comments");
		
		final int FIRST_CODE = 0;
		ChoiceQuestion question = StaticQuestionManager.getQuestion(StrategyTaxonomyQuestion.class);
		fillObjectUsingCommand(strategy, Strategy.TAG_TAXONOMY_CODE, question.getCode(FIRST_CODE));
		fillObjectUsingCommand(strategy, Strategy.TAG_IMPACT_RATING, StrategyImpactQuestion.HIGH_CODE);
		fillObjectUsingCommand(strategy, Strategy.TAG_FEASIBILITY_RATING, StrategyFeasibilityQuestion.LOW_CODE);
		
		IdList activityIds = new IdList(TaskSchema.getObjectType());
		activityIds.addRef(createAndPopulateTask(strategy, "Some activity Label").getRef());
		fillObjectUsingCommand(strategy, Strategy.TAG_ACTIVITY_IDS, activityIds.toString());
		
		IdList objectiveIds = new IdList(ObjectiveSchema.getObjectType());
		objectiveIds.addRef(createAndPopulateObjective(strategy).getRef());
		objectiveIds.addRef(createAndPopulateObjective(strategy).getRef());
		fillObjectUsingCommand(strategy, Strategy.TAG_OBJECTIVE_IDS, objectiveIds.toString());
		
		fillObjectUsingCommand(strategy, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING, "good, tnc legacy strategy rating");
		fillObjectUsingCommand(strategy, Strategy.TAG_ASSIGNED_LEADER_RESOURCE, createProjectResource().getRef().toString());
		
		addExpenseWithValue(strategy);
		addProgressReport(strategy);
	}

	public Timeframe addTimeframe(BaseObject baseObject) throws Exception 
	{
		Timeframe timeframe = createAndPopulateTimeframe();
		IdList timeframeIds = new IdList(timeframe);
		fillObjectUsingCommand(baseObject, BaseObject.TAG_TIMEFRAME_IDS, timeframeIds);
		
		return timeframe;
	}

	public Timeframe createAndPopulateTimeframe() throws Exception
	{
		Timeframe timeframe = createTimeframe();
		populateTimeframe(timeframe);

		return timeframe;
	}

	public Timeframe createTimeframe() throws Exception
	{
		ORef timeframeRef = createObject(TimeframeSchema.getObjectType());
		return Timeframe.find(this, timeframeRef);
	}

	public void populateTimeframe(Timeframe timeframe) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(createDateUnitEffort(2008, 2008, 0.0));
		fillObjectUsingCommand(timeframe, Timeframe.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
	}
	
	public void createAndPopulateThreatReductionResult() throws Exception
	{
		ThreatReductionResult threatReductionResult = createThreatReductionResult();
		fillObjectWithSampleStringData(threatReductionResult.getRef(), ThreatReductionResult.TAG_LABEL);
		fillObjectWithSampleStringData(threatReductionResult.getRef(), ThreatReductionResult.TAG_SHORT_LABEL);
		fillObjectWithSampleStringData(threatReductionResult.getRef(), ThreatReductionResult.TAG_COMMENTS);
		fillObjectWithSampleStringData(threatReductionResult.getRef(), ThreatReductionResult.TAG_TEXT);
		addObjective(threatReductionResult);
		
		Cause threat = createCause();
		fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, threat.getRef().toString());
	}
	
	public void populateStressBasedThreatRatingCommentsData() throws Exception
	{
		populateStressBasedThreatRatingCommentsData("Some Comment for Threat and Target" + getSampleUserText());
	}

	public void populateStressBasedThreatRatingCommentsData(String comment)	throws Exception
	{
		populateThreatTargetCommentsData(ThreatRatingCommentsData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP,	comment);
	}
	
	public void populateSimpleThreatRatingCommentsData() throws Exception
	{
		populateSimpleThreatRatingCommentsData("Some Comment for Threat and Target" + getSampleUserText());
	}

	public void populateSimpleThreatRatingCommentsData(String comment)	throws Exception
	{
		populateThreatTargetCommentsData(ThreatRatingCommentsData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP,	comment);
	}

	private void populateThreatTargetCommentsData(String tagStressBasedThreatRatingCommentsMap, String comment) throws Exception
	{
		ThreatRatingCommentsData threatRatingCommentsData = getSingletonThreatRatingCommentsData();
		DiagramLink diagramLink = createThreatTargetDiagramLink();
		FactorLink factorLink = diagramLink.getWrappedFactorLink();
		String commentsKey = ThreatRatingCommentsData.createKey(factorLink.getFromFactorRef(), factorLink.getToFactorRef());
		CodeToUserStringMap map = new CodeToUserStringMap();
		map.putUserString(commentsKey, comment);
		
		fillObjectUsingCommand(threatRatingCommentsData, tagStressBasedThreatRatingCommentsMap, map.toJsonString());
	}
	
	public void populateObjectTreeTableConfiguration(ObjectTreeTableConfiguration objectTreeTableConfiguration) throws Exception
	{
		CodeList rowCodes = new CustomPlanningAllRowsQuestion().getAllCodes();
		fillObjectUsingCommand(objectTreeTableConfiguration, ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, rowCodes);
		
		CodeList columnCodes = new CustomPlanningColumnsQuestion().getAllCodes();
		fillObjectUsingCommand(objectTreeTableConfiguration, ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION, columnCodes);
		
		fillObjectUsingCommand(objectTreeTableConfiguration, ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION, DiagramObjectDataInclusionQuestion.INCLUDE_RESULTS_CHAIN_DATA_CODE);
		fillObjectUsingCommand(objectTreeTableConfiguration, ObjectTreeTableConfiguration.TAG_STRATEGY_OBJECTIVE_ORDER, StrategyObjectiveTreeOrderQuestion.STRATEGY_CONTAINS_OBJECTIVE_CODE);
		fillObjectUsingCommand(objectTreeTableConfiguration, ObjectTreeTableConfiguration.TAG_TARGET_NODE_POSITION, PlanningTreeTargetPositionQuestion.TARGET_NODES_TOP_OF_PLANNING_TREE_CODE);
	}

	public void populateTableSettings(TableSettings tableSettings) throws Exception
	{
		fillObjectWithSampleStringData(tableSettings, TableSettings.TAG_TABLE_IDENTIFIER);
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_ROW_HEIGHT, 100);

		DateUnitListData dateUnitListData = new DateUnitListData("");
		dateUnitListData.add("2009");
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_DATE_UNIT_LIST_DATA, dateUnitListData.toString());

		CodeToCodeListMap tableSettingsMap = new CodeToCodeListMap();
		CodeList colSettings = new CodeList();
		colSettings.add(WorkPlanColumnConfigurationQuestion.META_TIMEFRAME_TOTAL);
		colSettings.add(WorkPlanColumnConfigurationQuestion.META_ASSIGNED_WHO_TOTAL);
		colSettings.add(WorkPlanColumnConfigurationQuestion.META_ASSIGNED_WHEN_TOTAL);
		colSettings.add(WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE);
		colSettings.add(WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE);
		colSettings.add(WorkPlanColumnConfigurationQuestion.META_BUDGET_DETAIL_COLUMN_CODE);
		tableSettingsMap.putCodeList(TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY, colSettings);

		CodeList rowSettings = new CodeList();
		rowSettings.add(WorkPlanRowConfigurationQuestion.RESOURCE_ASSIGNMENT);
		rowSettings.add(WorkPlanRowConfigurationQuestion.EXPENSE_ASSIGNMENT);
		tableSettingsMap.putCodeList(TableSettings.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY, rowSettings);

		fillObjectUsingCommand(tableSettings, TableSettings.TAG_TABLE_SETTINGS_MAP, tableSettingsMap.toJsonString());

		fillObjectUsingCommand(tableSettings, TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE, WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE);
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_TREE_EXPANSION_LIST, "");

		fillObjectUsingCommand(tableSettings, TableSettings.TAG_COLUMN_SEQUENCE_CODES, "");
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_COLUMN_WIDTHS, "");
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_COLUMN_SORT_TAG, "");
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_COLUMN_SORT_DIRECTION, SortDirectionQuestion.DEFAULT_SORT_ORDER_CODE);
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_WORK_PLAN_DIAGRAM_FILTER, "");
		fillObjectUsingCommand(tableSettings, TableSettings.TAG_WORK_PLAN_PROJECT_RESOURCE_CONFIGURATION, "");
	}

	public ProgressReport addProgressReport(BaseObject baseObject) throws Exception
	{
		ProgressReport progressReport = createAndPopulateProgressReport();
		ORefList progressReportRefs = new ORefList(progressReport.getRef());
		fillObjectUsingCommand(baseObject, BaseObject.TAG_PROGRESS_REPORT_REFS, progressReportRefs.toString());
		
		return progressReport;
	}
	
	public void addProgressPercent(Desire desire) throws Exception
	{
		ProgressPercent progressPercent = createAndPopulateProgressPercent();
		ORefList progressReportRefs = new ORefList(progressPercent.getRef());
		fillObjectUsingCommand(desire, Desire.TAG_PROGRESS_PERCENT_REFS, progressReportRefs.toString());
	}
	
	public Objective addObjective(Factor factor) throws Exception
	{
		IdList objectiveIds = new IdList(ObjectiveSchema.getObjectType());
		final Objective objective = createAndPopulateObjective(factor);
		objectiveIds.addRef(objective.getRef());
		fillObjectUsingCommand(factor, Factor.TAG_OBJECTIVE_IDS, objectiveIds.toString());
		
		return objective;
	}
	
	public void populateProgressReport(ProgressReport progressReport) throws Exception
	{
		fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_DATE, "2008-01-23");
		fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_STATUS, ProgressReportLongStatusQuestion.PLANNED_CODE);
	}
	
	public void populateProgressPercent(ProgressPercent progressPercent) throws Exception
	{
		fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_DATE, "2009-01-23");
		fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE, "21");
		fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES, "some percent complete notes");
	}
	
	public void populateXenodata(Xenodata xenodata, String xenoDataProjectId) throws Exception
	{
		setObjectData(xenodata.getRef(), Xenodata.TAG_PROJECT_ID, xenoDataProjectId);
	}
	
	public void populateDiagramFactorGroupBox(DiagramFactor groupBoxDiagramFactor, DiagramFactor groupBoxChild) throws Exception
	{
		ORefList groupBoxChildren = new ORefList(groupBoxChild);
		fillObjectUsingCommand(groupBoxDiagramFactor, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, groupBoxChildren.toString());
	}
	
	public void populateThreatRatingCommentsData(ThreatRatingCommentsData threatRatingCommentsData, ORef threatRef, ORef targetRef) throws Exception
	{
		String threatTargetKey = ThreatRatingCommentsData.createKey(threatRef, targetRef);
				
		CodeToUserStringMap simpleThreatRatingCommentsMap = threatRatingCommentsData.getSimpleThreatRatingCommentsMap();
		simpleThreatRatingCommentsMap.putUserString(threatTargetKey, SIMPLE_THREAT_RATING_COMMENT);
		fillObjectUsingCommand(threatRatingCommentsData, ThreatRatingCommentsData.TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP, simpleThreatRatingCommentsMap.toJsonString());
	
		CodeToUserStringMap stressBasedThreatRatingCommentsMap = threatRatingCommentsData.getStressBasedThreatRatingCommentsMap();
		stressBasedThreatRatingCommentsMap.putUserString(threatTargetKey, STRESS_BASED_THREAT_RATING_COMMENT);
		fillObjectUsingCommand(threatRatingCommentsData, ThreatRatingCommentsData.TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP, stressBasedThreatRatingCommentsMap.toJsonString());
	}
	
	public void populateOrganization(Organization organization) throws Exception
	{
		fillObjectUsingCommand(organization, Organization.TAG_LABEL, "Some organization name");
		fillObjectUsingCommand(organization, Organization.TAG_SHORT_LABEL, "Some organization id");
		fillObjectUsingCommand(organization, Organization.TAG_ROLES_DESCRIPTION, "Some organization roles");
		fillObjectUsingCommand(organization, Organization.TAG_CONTACT_FIRST_NAME, "Some organization contact first name");
		fillObjectUsingCommand(organization, Organization.TAG_CONTACT_LAST_NAME, "Some organization contact last name");
		fillObjectUsingCommand(organization, Organization.TAG_EMAIL, "Some organization email");
		fillObjectUsingCommand(organization, Organization.TAG_PHONE_NUMBER, "Some organization phone number");
		fillObjectUsingCommand(organization, Organization.TAG_COMMENTS, "Some organization comments");
	}
	
	public void populateCategoryOne(BudgetCategoryOne categoryOne) throws Exception
	{
		fillObjectWithSampleStringData(categoryOne, AbstractBudgetCategoryObject.TAG_CODE);
		fillObjectWithSampleStringData(categoryOne, AbstractBudgetCategoryObject.TAG_COMMENTS);
	}
	
	public void populateCategoryTwo(BudgetCategoryTwo categoryTwo) throws Exception
	{
		fillObjectWithSampleStringData(categoryTwo, AbstractBudgetCategoryObject.TAG_CODE);
		fillObjectWithSampleStringData(categoryTwo, AbstractBudgetCategoryObject.TAG_COMMENTS);
	}
	
	public void populateDiagramFactor(DiagramFactor diagramFactor) throws Exception
	{
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(new Point(10, 10)));
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_SIZE, EnhancedJsonObject.convertFromDimension(new Dimension(30, 120)));
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_BACKGROUND_COLOR, DiagramFactorBackgroundQuestion.ORANGE_COLOR_CODE);
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FOREGROUND_COLOR, DiagramFactorFontColorQuestion.BROWN_HEX);
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_SIZE, "2.5");
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FONT_STYLE, DiagramFactorFontStyleQuestion.BOLD_CODE);
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_HEADER_HEIGHT, DiagramFactor.DEFAULT_HEADER_HEIGHT);
		fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_TEXT_BOX_Z_ORDER_CODE, TextBoxZOrderQuestion.FRONT_CODE);
	}
	
	private void populateDiagramLink(DiagramLink diagramLink) throws Exception
	{
		fillObjectUsingCommand(diagramLink, DiagramLink.TAG_COLOR, DiagramLinkColorQuestion.DARK_BLUE_CODE);
		fillObjectUsingCommand(diagramLink, DiagramLink.TAG_IS_BIDIRECTIONAL_LINK, DiagramLink.BIDIRECTIONAL_LINK);
		
		PointList bendPoints = new PointList();
		bendPoints.add(new Point(100, 100));
		fillObjectUsingCommand(diagramLink, DiagramLink.TAG_BEND_POINTS, bendPoints.toString());
	}
	
	public void populateExpenseAssignment(ExpenseAssignment expenseAssignment) throws Exception
	{
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_LABEL, "Some Expense");
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_FUNDING_SOURCE_REF, createFundingSource().getRef());
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, createAccountingCode().getRef());
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_ONE_REF, createCategoryOne().getRef());
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_CATEGORY_TWO_REF, createCategoryTwo().getRef());
		
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(createDateUnitEffort(2008, 2008, 10.0));
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
		fillObjectUsingCommand(expenseAssignment, BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER, createSampleTaxonomyClassificationsList());
		fillObjectUsingCommand(expenseAssignment, ExpenseAssignmentSchema.TAG_ACCOUNTING_CLASSIFICATION_CONTAINER, createSampleAccountingClassificationsList());
	}
	
	public void populateResourceAssignment(ResourceAssignment resourceAssignment) throws Exception
	{
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_LABEL, "Some Resource Assignment");
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, createAndPopulateProjectResource().getId());
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_FUNDING_SOURCE_ID, createFundingSource().getId());
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_ACCOUNTING_CODE_ID, createAccountingCode().getId());
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_CATEGORY_ONE_REF, createCategoryOne().getRef());
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_CATEGORY_TWO_REF, createCategoryTwo().getRef());
		
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(createDateUnitEffort(2007, 2008, 11.0));
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
		fillObjectUsingCommand(resourceAssignment, BaseObject.TAG_TAXONOMY_CLASSIFICATION_CONTAINER, createSampleTaxonomyClassificationsList());
		fillObjectUsingCommand(resourceAssignment, ResourceAssignmentSchema.TAG_ACCOUNTING_CLASSIFICATION_CONTAINER, createSampleAccountingClassificationsList());
	}
	
	public void populateEverything() throws Exception
	{
		fillProjectMetadata();
		fillWwfProjectData();
		fillWcsProjectData();
		fillRareProjectData();
		fillFosProjectData();
		fillTncProjectData();
		createAndPopulateHumanWelfareScopeBox();
		createAndPopulateBiodiversityScopeBox();
		createAndPopulateDirectThreatLink();
		createAndPopulateMeasurement();
		createAndPopulateProjectResource();
		createAndPopulateStress();
		createAndPopulateSubTarget();
		createAndPopulateTarget();
        createAndPopulateBiophysicalFactor();
        createAndPopulateBiophysicalResult();
		createAndPopulateThreat();
		createThreatReductionResult();
		createAndPopulateDraftStrategy();
		Strategy strategy = createAndPopulateStrategy();
		createAndPopulateStrategyThreatTargetAssociation();
		createAndPopulateObjective(strategy);
		Task activity = createAndPopulateActivity();
		createAndPopulateTask(activity, "Some Task Label");
		createAndPopulateOrganization();
		createAndPopulateExpenseAssignment();
		createAndPopulateResourceAssignment();
		createAndPopulateTextBox();
		createAndPopulateGroupBox();
		createAndPopulateAudience();
		createAndPopulateOtherNotableSpecies();
		createAndPopulateIucnRedlistSpecies();
		createAndPopulateCategoryOne();
		createAndPopulateCategoryTwo();
		populateDashboard();
		populateBaseObjectWithSampleData(createBaseObject(XslTemplateSchema.getObjectType()));
		populateBaseObjectWithSampleData(createBaseObject(ReportTemplateSchema.getObjectType()));
		createAndPopulateObjectTreeTableConfiguration();
	}
	
	public void populateBaseObjectWithSampleData(BaseObject baseObject) throws Exception
	{
		Vector<String> fieldTags = baseObject.getStoredFieldTags();
		for(String tag : fieldTags)
		{
			final ObjectData field = baseObject.getField(tag);
			String sampleData = getSampleData(field);
			setObjectData(baseObject, tag, sampleData);
		}
	}

	private String getSampleData(ObjectData field) throws Exception
	{
		if (field.isIntegerData())
		{
			return "45";
		}
		if (field.isNumberData())
		{
			return "34";
		}
		if (field.isFloatData())
		{
			return "1.2";
		}
		if (field.isBooleanData())
		{
			return BooleanData.BOOLEAN_TRUE;
		}
		if (field.isUserTextWithHtmlFormatting())
		{
			return getSampleUserText();
		}
		if (field.isSingleLineUserText())
		{
			return getSingleLineSampleData();
		}
		if (field.isDateData())
		{
			return "2006-02-01";
		}
		if (field.isCodeListData())
		{
			final CodeList oneCodeList = new CodeList(new String[]{getSampleCode(field)});
			return oneCodeList.toString();
		}
		if (field.isChoiceItemData())
		{
			return getSampleCode(field);
		}
		if (field.isCodeData())
		{
			return "randomCode";
		}
		if (field.isStringRefMapData())
		{
			return createSampleXenodata();
		}

		throw new Exception("no sample data for: " + field.getClass().getSimpleName());
	}

	public String getSampleUserText(BaseObject baseObject, String tag) throws Exception
	{
		ObjectData objectData = baseObject.getField(tag);
		return getSampleData(objectData);
	}
	
	public String getSampleUserText()
	{
		String userText = getSingleLineSampleData();
		userText = getSingleLineFormattedHtmlTextSample(userText);
		userText = getMultilineSampleData(userText);
		
		return userText;
	}

	private String getMultilineSampleData(String userText)
	{
		userText += "Value"+ HtmlUtilities.BR_TAG + HtmlUtilities.BR_TAG + "With multiple lines!";
		
		return userText;
	}

	public String getSingleLineSampleData()
	{
		return "Sample text with encoded values: &amp; &lt; &gt;";
	}

	private String getSingleLineFormattedHtmlTextSample(String userText)
	{
		userText += createSampleFormattedData(userText);
		userText += createWithAllowedHtmlTags(userText);
		
		return userText;
	}

	private String createWithAllowedHtmlTags(String userText)
	{
		userText += "<ul><li>" + userText +  "</li></ul>";
		userText += "<ol><li>" + userText +  "</li></ol>";
		
		return userText;
	}
	
	public String createNestedBulletsList()
	{
		String userText = "";
		userText += 
			"<ul>" +
				"<li>" +
					"<ol><li><b>level2</b></li></ol>" +
				"</li>" +
			"</ul>";
		
		userText += 
			"<ol>" +
				"<li>" +
					"<ul><li>level2</li></ul>" +
				"</li>" +
			"</ol>";

		return userText;
	}

	public String getSampleCode(ObjectData field)
	{
		ChoiceQuestion question = field.getChoiceQuestion();
		if (question.isProjectBasedDynamicQuestion())
			return "dyanmicSampleCode";
			
		CodeList allCodes = question.getAllCodes();
		final String lastElement = allCodes.lastElement();
		return lastElement;
	}

	private void populateDashboard() throws Exception
	{
		populateDashboard("Some random user comment" + getSingleLineSampleData());
	}

	public void populateDashboard(final String sampleComments) throws Exception
	{
		ORef dashboardRef = getSingletonObjectRef(DashboardSchema.getObjectType());
		CodeToChoiceMap progressChoiceMap = new CodeToChoiceMap();
		progressChoiceMap.putChoiceCode(OpenStandardsConceptualizeQuestion.SELECT_INITIAL_TEAM_MEMBERS_CODE, OpenStandardsProgressStatusQuestion.IN_PROGRESS_CODE);
		fillObjectUsingCommand(dashboardRef, Dashboard.TAG_PROGRESS_CHOICE_MAP, progressChoiceMap.toJsonString());
		
		CodeToUserStringMap commentsMap = new CodeToUserStringMap();
		commentsMap.putUserString(OpenStandardsConceptualizeQuestion.SELECT_INITIAL_TEAM_MEMBERS_CODE, sampleComments);
		fillObjectUsingCommand(dashboardRef, Dashboard.TAG_COMMENTS_MAP, commentsMap.toJsonString());

		CodeToCodeListMap flagsMap = new CodeToCodeListMap();
		CodeList flags = new CodeList();
		flags.add(DashboardFlagsQuestion.NEEDS_ATTENTION_CODE);
		flagsMap.putCodeList(OpenStandardsConceptualizeQuestion.SELECT_INITIAL_TEAM_MEMBERS_CODE, flags);
		fillObjectUsingCommand(dashboardRef, Dashboard.TAG_FLAGS_MAP, flagsMap.toJsonString());
	}

	public void validateObjectOwners(int type)
	{
		ORefList refs = getPool(type).getRefList();
		for(int i = 0; i < refs.size(); ++i)
		{
			BaseObject object = BaseObject.find(this, refs.get(i));
			if(object.getOwnerRef().isInvalid())
				throw new RuntimeException("Object without owner! " + object.getRef());
		}
	}

	public void switchToStressBaseMode() throws Exception
	{
		setObjectData(getMetadata().getRef(), ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
	}
	
	public void createAndPopulateStrategyThreatTargetAssociation() throws Exception
	{
		DiagramLink diagramLink = createAndPopulateDirectThreatDiagramLink();
		ORef threatRef = getUpstreamThreatRef(diagramLink);
		Strategy strategy = createAndPopulateStrategy();
		createFactorLink(threatRef, strategy.getRef());
	}
	
	private CodeList createSampleTerrestrialEcoregionsCodeList()
	{
		CodeList terrestrialEcoregions = new CodeList();
		terrestrialEcoregions.add("10000");
		terrestrialEcoregions.add("10649");
		terrestrialEcoregions.add("10671");
		
		return terrestrialEcoregions;
	}

	private CodeList createSampleMarineEcoregionsCodeList()
	{
		CodeList marineEcoregions = new CodeList();
		marineEcoregions.add("20030");
		marineEcoregions.add("25031");
		marineEcoregions.add("20192");
		
		return marineEcoregions;
	}
	
	private CodeList createSampleFreshwaterEcoregionsCodeList()
	{
		CodeList freshwaterEcoregions = new CodeList();
		freshwaterEcoregions.add("30736");
		freshwaterEcoregions.add("30424");
		freshwaterEcoregions.add("30103");
		
		return freshwaterEcoregions;
	}
	
	private CodeList createSampleTncOperatingUnitsCodeList()
	{
		CodeList tncOperatingUnitCodes = new CodeList();
		tncOperatingUnitCodes.add("IA_US");
		tncOperatingUnitCodes.add("SC_US");
		tncOperatingUnitCodes.add("FL_US");
		
		return tncOperatingUnitCodes;
	}
	
	private CodeList createSampleCountriesCodeList()
	{
		CodeList countriesCodeList = new CodeList();
		countriesCodeList.add("USA");
		countriesCodeList.add("BGD");
		countriesCodeList.add("AGO");
		
		return countriesCodeList;
	}
	
	public void fillObjectWithSampleStringData(BaseObject baseObject, String fieldTag) throws Exception
	{
		fillObjectWithSampleStringData(baseObject.getRef(), fieldTag);
	}
	
	public void fillObjectWithSampleStringData(ORef objectRef, String fieldTag) throws Exception
	{
		CommandSetObjectData setData = new CommandSetObjectData(objectRef, fieldTag, "Sample " + fieldTag + " data");
		executeCommand(setData);
	}

	public void fillCostPerUnitField(ProjectResource projectResource, String costPerUnit) throws Exception
	{
		fillObjectUsingCommand(projectResource, ProjectResource.TAG_COST_PER_UNIT, costPerUnit);
	}
	
	public void fillObjectUsingCommand(ORef objectRef, String fieldTag, String data) throws Exception
	{
		CommandSetObjectData setData = new CommandSetObjectData(objectRef, fieldTag, data);
		executeCommand(setData);
	}

	public void fillObjectUsingCommand(BaseObject object, String fieldTag, int data) throws Exception
	{
		fillObjectUsingCommand(object.getRef(), fieldTag, Integer.toString(data));
	}

	public void fillObjectUsingCommand(BaseObject object, String fieldTag, String data) throws Exception
	{
		fillObjectUsingCommand(object.getRef(), fieldTag, data);
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, ORef data) throws Exception
	{
		fillObjectUsingCommand(object, fieldTag, data.toString());
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, BaseId data) throws Exception
	{
		fillObjectUsingCommand(object, fieldTag, data.toString());
	}
	
	public void fillObjectUsingCommand(ORef ref, String fieldTag, ORefList data) throws Exception
	{
		fillObjectUsingCommand(ref, fieldTag, data.toString());
	}
	
	public void fillObjectUsingCommand(BaseObject baseObject, String fieldTag, CodeList data) throws Exception
	{
		fillObjectUsingCommand(baseObject, fieldTag, data.toJsonString());
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, ORefList data) throws Exception
	{
		fillObjectUsingCommand(object, fieldTag, data.toString());
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, IdList data) throws Exception
	{
		fillObjectUsingCommand(object, fieldTag, data.toString());
	}
	
	public void fillObjectUsingCommand(BaseObject object, String fieldTag, ORefList refListToBeConvertedToIdList, int type) throws Exception
	{
		fillObjectUsingCommand(object, Task.TAG_SUBTASK_IDS, refListToBeConvertedToIdList.convertToIdList(type));
	}
	
	public FactorId createFactorAndReturnId(int objectType) throws Exception
	{
		BaseId factorId = createObjectAndReturnId(objectType);
		return new FactorId(factorId.asInt());
	}
	
	public BaseId addItemToViewDataList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.VIEW_DATA, id),  type,  tag);
	}
	
	public BaseId addItemToProjectMetaDataList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.PROJECT_METADATA, id),  type,  tag);
	}
	
	public BaseId addItemToKeyEcologicalAttributeList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, id),  type,  tag);
	}
	
	public BaseId addItemToGoalList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, ObjectType.GOAL,  tag);
	}
	
	public BaseId addItemToObjectiveList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, ObjectType.OBJECTIVE,  tag);
	}
	
	public BaseId addItemToIndicatorList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, IndicatorSchema.getObjectType(), tag);
	}
	
	public BaseId addSubtaskToActivity(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, TaskSchema.getObjectType(), tag);
	}
	
	public ORef addActivityToStrategy(ORef strategyRef, String strategyTagForActivities) throws Exception
	{
		return new ORef(TaskSchema.getObjectType(), addItemToList(strategyRef, TaskSchema.getObjectType(), strategyTagForActivities));
	}
	
	public BaseId addActivityToStrategyList(ORef ref, String tag) throws Exception
	{
		return addItemToList(ref, TaskSchema.getObjectType(), tag);
	}
	
	public BaseId addItemToIndicatorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.INDICATOR, id),  type,  tag);
	}
	
	public BaseId addItemToTaskList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(new ORef(ObjectType.TASK, id),  type,  tag);
	}
	
	public BaseId addItemToFactorList(ORef factorRef, int type, String tag) throws Exception
	{
		return addItemToList(factorRef,  type,  tag);
	}
	
	public BaseId addItemToList(ORef parentRef, int typeToCreate, String tag) throws Exception
	{
		BaseObject foundObject = findObject(parentRef);
		IdList currentIdList = new IdList(typeToCreate, foundObject.getData(tag));
		
		BaseId baseId = createObjectAndReturnId(typeToCreate);
		currentIdList.add(baseId);
		setObjectData(parentRef, tag, currentIdList.toString());

		return baseId;
	}
	
	public FactorId createTaskAndReturnId() throws Exception
	{
		return (FactorId)createObjectAndReturnId(ObjectType.TASK, BaseId.INVALID);
	}

	public DiagramFactor createAndAddFactorToDiagram(int nodeType) throws Exception
	{
		return createAndAddFactorToDiagram(nodeType, takeNextId(nodeType));
	}
	
	public DiagramFactor createAndAddFactorToDiagram(DiagramObject diagramObject, int objectType) throws Exception
	{
		return createAndAddFactorToDiagram(diagramObject, objectType, takeNextId(objectType));
	}
		
	private DiagramFactor createAndAddFactorToDiagram(int nodeType, int id) throws Exception
	{
		DiagramObject diagramObject = getTestingDiagramModel().getDiagramObject();
		return createAndAddFactorToDiagram(diagramObject, nodeType, id);
	}

	private DiagramFactor createAndAddFactorToDiagram(DiagramObject diagramObject, int objectType, int id) throws Exception
	{
		CommandCreateObject createFactor = new CommandCreateObject(objectType);
		createFactor.setCreatedId(new BaseId(id));
		executeCommand(createFactor);
		
		return createAndAddFactorToDiagram(diagramObject, createFactor.getObjectRef());
	}
	
	public DiagramFactor createAndAddFactorToDiagram(DiagramObject diagramObject, ORef factorRef) throws Exception
	{
		FactorCommandHelper factorHelper = new FactorCommandHelper(this, getTestingDiagramModel());
		CommandCreateObject createDiagramFactor = factorHelper.createDiagramFactor(diagramObject, factorRef);
		
		return DiagramFactor.find(this, createDiagramFactor.getObjectRef());
	}
	
	public DiagramFactor createDiagramFactorWithWrappedRefLabelAndAddToDiagram(int objectType) throws Exception
	{
		DiagramFactor diagramFactor = createDiagramFactorAndAddToDiagram(objectType);
		ORef wrappedRef = diagramFactor.getWrappedORef();
		setObjectData(wrappedRef, BaseObject.TAG_LABEL, wrappedRef.toString());
		
		return diagramFactor;
	}
	
	public DiagramFactor createDiagramFactorAndAddToDiagram(int objectType) throws Exception
	{
		return createAndAddFactorToDiagram(objectType, takeNextId(objectType));
	}
	
	public ORef createDiagramFactorLinkAndAddToDiagram(DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORef diagramLinkRef = createDiagramLink(from, to);
		CommandSetObjectData cmd = CommandSetObjectData.createAppendIdCommand(getMainDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkRef.getObjectId());
		executeCommand(cmd);
		return diagramLinkRef;
	}

	private int takeNextId(int objectType)
	{
		if(nextStrategyId == 0)
		{
			int startAt = getNormalIdAssigner().getHighestAssignedId() + 1;
			nextStrategyId = startAt;
			nextCauseId = startAt;
			nextTargetId = startAt;
			nextOtherId = startAt;
		}
		
		int next = -1;
		
		if(objectType == StrategySchema.getObjectType())
			next = nextStrategyId++;
		else if(objectType == CauseSchema.getObjectType())
			next = nextCauseId++;
		else if(objectType == TargetSchema.getObjectType())
			next = nextTargetId++;
		else
			next = nextOtherId++;
		
		getNormalIdAssigner().idTaken(new BaseId(next));
		return next;
	}
	
	public FactorId createNodeAndAddToDiagram(int objectType) throws Exception
	{
		return createNodeAndAddToDiagram(objectType, takeNextId(objectType));
	}

	public FactorId createNodeAndAddToDiagram(int objectType, int factorId) throws Exception
	{
		return createAndAddFactorToDiagram(objectType, factorId).getWrappedId();
	}
	
	public FactorCell createFactorCell(int objectType) throws Exception
	{
		return createFactorCell(objectType, takeNextId(objectType));
	}
	
	public FactorCell createFactorCell(int objectType, int factorId) throws Exception
	{
		DiagramFactor diagramFactor = createAndAddFactorToDiagram(objectType, factorId);
		return getTestingDiagramModel().getFactorCellByWrappedRef(diagramFactor.getWrappedORef());
	}
	
	public LinkCell createLinkCell() throws Exception
	{
		BaseId diagramLinkId = createDiagramFactorLink();
		DiagramLink diagramLink = (DiagramLink) findObject(new ORef(ObjectType.DIAGRAM_LINK, diagramLinkId));
		addDiagramLinkToModel(diagramLink);
		
		return getTestingDiagramModel().getLinkCell(diagramLink);	
	}
	
	public void addDiagramLinkToModel(DiagramLink diagramLink) throws Exception 
	{
		 addDiagramLinkToModel(diagramLink.getDiagramLinkId());
	}
	
	public void addDiagramLinkToModel(BaseId diagramLinkId) throws Exception
	{
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(getTestingDiagramObject(), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkId);
		executeCommand(addLink);
	}
	
	public BaseId createDiagramFactorLink() throws Exception
	{
		return createDiagramLink().getObjectId();
	}

	public ORef createDiagramLink() throws Exception
	{
		DiagramFactor from = createAndAddFactorToDiagram(ObjectType.CAUSE, takeNextId(CauseSchema.getObjectType()));
		DiagramFactor to = createAndAddFactorToDiagram(ObjectType.CAUSE, takeNextId(CauseSchema.getObjectType()));
		return createDiagramLink(from, to);
	}

	public ORef createDiagramLinkAndAddToDiagram(DiagramFactor from, DiagramFactor to, String isBidirectionalTag) throws Exception
	{
		ORef diagramLinkRef = createDiagramLinkAndAddToDiagram(from, to);
		DiagramLink diagramLink = DiagramLink.find(this, diagramLinkRef);
		setBidirectionality(diagramLink, isBidirectionalTag);
		
		return diagramLinkRef;
	}
	
	public ORef createDiagramLinkAndAddToDiagram(DiagramFactor from, DiagramFactor to) throws Exception
	{	
		return createDiagramLinkAndAddToDiagramModel(from, to).getRef();
	}
	
	//TODO - find a better name,  or inline above method
	public DiagramLink createDiagramLinkAndAddToDiagramModel(DiagramFactor from, DiagramFactor to) throws Exception
	{
		ORef diagramLinkRef = createDiagramLink(from, to);
		DiagramLink diagramLink = DiagramLink.find(this, diagramLinkRef);
		addDiagramLinkToModel(diagramLink);
	
		return diagramLink;
	}
	
	public BaseId createDiagramFactorLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		return createDiagramLink(from, to).getObjectId();
	}
	
	public ORef createDiagramLink(DiagramFactor from, DiagramFactor to) throws Exception
	{
		final ORef diagramLinkRef = createObject(ObjectType.DIAGRAM_LINK);

		BaseId baseId = BaseId.INVALID;
		if(!shouldCreateGroupBoxLink(from, to))
			baseId = createFactorLink(from.getWrappedORef(), to.getWrappedORef()).getObjectId();
		
		setObjectData(diagramLinkRef, DiagramLink.TAG_WRAPPED_ID, baseId.toString());
		setObjectData(diagramLinkRef, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID, from.getId().toString());
		setObjectData(diagramLinkRef, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID, to.getId().toString());
		
		return diagramLinkRef;
	}

	public ORef createFactorLink(ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		final ORef factorLinkRef = createObject(ObjectType.FACTOR_LINK);
		setObjectData(factorLinkRef, FactorLink.TAG_FROM_REF, fromFactorRef.toString());
		setObjectData(factorLinkRef, FactorLink.TAG_TO_REF, toFactorRef.toString());

		return factorLinkRef;
	}
	
	public ORef createDiagramLinkWithCommand(DiagramFactor from, DiagramFactor to) throws Exception
	{
		BaseId baseId = BaseId.INVALID;
		if(!shouldCreateGroupBoxLink(from, to))
			baseId = createFactorLinkWithCommand(from.getWrappedORef(), to.getWrappedORef()).getObjectId();
		
		CommandCreateObject createDiagramLink = new CommandCreateObject(DiagramLinkSchema.getObjectType());
		executeCommand(createDiagramLink);

		final ORef diagramLinkRef = createDiagramLink.getObjectRef();
		fillObjectUsingCommand(diagramLinkRef, DiagramLink.TAG_WRAPPED_ID, baseId.toString());
		fillObjectUsingCommand(diagramLinkRef, DiagramLink.TAG_FROM_DIAGRAM_FACTOR_ID, from.getId().toString());
		fillObjectUsingCommand(diagramLinkRef, DiagramLink.TAG_TO_DIAGRAM_FACTOR_ID, to.getId().toString());
		
		return diagramLinkRef;
	}
	
	public ORef createFactorLinkWithCommand(ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		CommandCreateObject createFactorLink = new CommandCreateObject(FactorLinkSchema.getObjectType());
		executeCommand(createFactorLink);
		
		final ORef objectRef = createFactorLink.getObjectRef();
		setObjectData(objectRef, FactorLink.TAG_FROM_REF, fromFactorRef.toString());
		setObjectData(objectRef, FactorLink.TAG_TO_REF, toFactorRef.toString());
		
		return objectRef;
	}
	
	private boolean shouldCreateGroupBoxLink(DiagramFactor from, DiagramFactor to)
	{
		return from.isGroupBoxFactor() || to.isGroupBoxFactor();
	}
	
	public LinkCell createLinkCellWithBendPoints(PointList bendPoints) throws Exception
	{
		LinkCell linkCell = createLinkCell();
	
		CommandSetObjectData createBendPointsCommand =	CommandSetObjectData.createNewPointList(linkCell.getDiagramLink(), DiagramLink.TAG_BEND_POINTS, bendPoints);
		executeCommand(createBendPointsCommand);
		
		return linkCell;
	}
	
	public ORef createThreat() throws Exception
	{
		ORef factorLinkRef = createThreatTargetLink();
		FactorLink factorLink = FactorLink.find(this, factorLinkRef);
		
		return factorLink.getFromFactorRef();
	}
	
	public ORef createThreatTargetLink() throws Exception
	{
		DiagramFactor threat = createAndAddFactorToDiagram(ObjectType.CAUSE, takeNextId(CauseSchema.getObjectType()));
		enableAsThreat(threat.getWrappedORef());
		DiagramFactor target = createAndAddFactorToDiagram(ObjectType.TARGET, takeNextId(TargetSchema.getObjectType()));

		
		final ORef factorLinkRef = createObject(ObjectType.FACTOR_LINK);
		setObjectData(factorLinkRef, FactorLink.TAG_FROM_REF, threat.getWrappedORef().toString());
		setObjectData(factorLinkRef, FactorLink.TAG_TO_REF, target.getWrappedORef().toString());
		
		return factorLinkRef;
	}
	
	public void setBidirectionality(DiagramLink diagramLink, String isBidirectional)	throws CommandFailedException
	{
		CommandVector setBidirectionality = diagramLink.createCommandsToEnableBidirectionalFlag();
		executeCommands(setBidirectionality);
	}

	public void disableAsThreat(Cause threat) throws CommandFailedException
	{
		disableAsThreat(threat.getRef());
	}
	
	public void disableAsThreat(ORef threatRef) throws CommandFailedException
	{
		changeThreatStatus(threatRef, BooleanData.BOOLEAN_FALSE);
	}
	
	public void enableAsThreat(Cause threat) throws Exception
	{
		enableAsThreat(threat.getRef());
	}
	
	public void enableAsThreat(ORef threatRef) throws Exception
	{
		changeThreatStatus(threatRef, BooleanData.BOOLEAN_TRUE);
	}

	private void changeThreatStatus(ORef threatRef, String isThreat) throws CommandFailedException
	{
		CommandSetObjectData setThreat = new CommandSetObjectData(threatRef, Cause.TAG_IS_DIRECT_THREAT, isThreat);
		executeCommand(setThreat);
	}
	
	public static ORef getDownstreamTargetRef(DiagramLink diagramLink) throws Exception
	{
		if (diagramLink.getToDiagramFactor().getWrappedORef().getObjectType() == TargetSchema.getObjectType())
			return diagramLink.getToWrappedRef();
		
		if (diagramLink.getFromDiagramFactor().getWrappedORef().getObjectType() == TargetSchema.getObjectType() && diagramLink.isBidirectional())
			return diagramLink.getFromWrappedRef();
		
		throw new Exception();
	}
	
	public static ORef getUpstreamThreatRef(DiagramLink diagramLink) throws Exception
	{
		if (Cause.is(diagramLink.getFromWrappedRef()))
			return diagramLink.getFromWrappedRef();
		
		if (Cause.is(diagramLink.getToWrappedRef()) && diagramLink.isBidirectional())
			return diagramLink.getToWrappedRef();
		
		throw new Exception();
	}
	
	public MultiCalendar parseIsoDate(String date)
	{
		return MultiCalendar.createFromIsoDateString(date);
	}
	
	public ExpenseAssignment addExpenseWithValue(BaseObject baseObject) throws Exception
	{
		return addExpenseAssignment(baseObject, new DateUnit(), 12.0);
	}

	public ExpenseAssignment addExpenseAssignment(BaseObject baseObject,	DateUnit dateUnitToUse, double unitQuantityToUse) throws Exception
	{
		DateUnitEffort dateUnitEffort = new DateUnitEffort(dateUnitToUse, unitQuantityToUse);
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(dateUnitEffort);
		return addExpenseAssignment(baseObject, dateUnitEffortList);
	}
	
	public ExpenseAssignment addExpenseAssignment(BaseObject baseObject, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		ORef expenseRef = createObject(ExpenseAssignmentSchema.getObjectType());
		ExpenseAssignment assignment = ExpenseAssignment.find(this, expenseRef);
		setObjectData(assignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
		
		ORefList currentAssignmentRefList = baseObject.getSafeRefListData(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS);
		currentAssignmentRefList.add(assignment.getRef());
		setObjectData(baseObject,BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS, currentAssignmentRefList.toString());
		
		return assignment;
	}
	
	public ResourceAssignment addResourceAssignment(BaseObject baseObject, double units, int startYear, int endYear) throws Exception
	{
		ProjectResource projectResource = createAndPopulateProjectResource();
		return addResourceAssignment(baseObject, projectResource, units, startYear, endYear);
	}

	public ResourceAssignment addResourceAssignment(BaseObject parentObject, ProjectResource projectResource, double units, int startYear,	int endYear) throws Exception
	{
		MultiCalendar startDate = createStartYear(startYear);
		MultiCalendar endDate = createEndYear(endYear);
		DateUnitEffort dateUnitEffort = createDateUnitEffort(startDate, endDate);
		dateUnitEffort.setUnitQuantity(units);
		
		return addResourceAssignment(parentObject, projectResource, dateUnitEffort);
	}
	
	public ResourceAssignment addResourceAssignment(BaseObject object) throws Exception
	{
		return addResourceAssignment(object, createAndPopulateProjectResource(), 10.0, new DateUnit());
	}

	private ResourceAssignment addResourceAssignment(BaseObject parentObject, ProjectResource projectResource, double units, DateUnit dateUnit) throws Exception
	{
		ResourceAssignment resourceAssignment = addResourceAssignment(parentObject, units, dateUnit);
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		return resourceAssignment;
	}
	
	public ResourceAssignment addResourceAssignment(BaseObject parentObject, double units,  DateUnit dateUnit) throws Exception
	{
		DateUnitEffort dateUnitEffort = new DateUnitEffort(dateUnit, units);
		ResourceAssignment assignment = createResourceAssignment();
		ProjectResource projectResource = createAndPopulateProjectResource();
		fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		return addResourceAssignment(parentObject, assignment, dateUnitEffort);
	}

	public ResourceAssignment addResourceAssignment(BaseObject parentObject, ProjectResource projectResource, DateUnitEffort dateUnitEffort) throws Exception
	{
		ResourceAssignment assignment = createResourceAssignment();
		fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		return addResourceAssignment(parentObject, assignment, dateUnitEffort);
	}

	private ResourceAssignment addResourceAssignment(BaseObject parentObject, ResourceAssignment assignment, DateUnitEffort dateUnitEffort) throws Exception
	{
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(dateUnitEffort);
		return addResourceAssignment(parentObject, assignment, dateUnitEffortList);
	}
	
	public ResourceAssignment addResourceAssignment(BaseObject parentObject, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		ResourceAssignment resourceAssignment = createResourceAssignment();
		ProjectResource projectResource = createAndPopulateProjectResource();
		fillObjectUsingCommand(resourceAssignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		
		return addResourceAssignment(parentObject, resourceAssignment, dateUnitEffortList);
	}

	public ResourceAssignment addResourceAssignment(BaseObject parentObject, ResourceAssignment assignment, DateUnitEffortList dateUnitEffortList) throws Exception
	{
		setObjectData(assignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
		IdList currentAssignmentIdList = parentObject.getSafeIdListData(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS);
		currentAssignmentIdList.add(assignment.getId());
		setObjectData(parentObject, BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, currentAssignmentIdList.toString());
		
		return assignment;
	}
	
	public void fillWorkUnitDay() throws Exception
	{
		DateUnitEffort dateUnitEffort = new DateUnitEffort(new DateUnit("2012-01-01"), 10.0);
		ResourceAssignment assignment = createResourceAssignment();
		ProjectResource projectResource = createAndPopulateProjectResource();
		fillObjectUsingCommand(assignment, ResourceAssignment.TAG_RESOURCE_ID, projectResource.getId().toString());
		DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();
		dateUnitEffortList.add(dateUnitEffort);
		setObjectData(assignment, ResourceAssignment.TAG_DATEUNIT_DETAILS, dateUnitEffortList.toString());
	}

	public static MultiCalendar createStartYear(int startYear)
	{
		return MultiCalendar.createFromGregorianYearMonthDay(startYear, 1, 1);
	}
	
	public static MultiCalendar createEndYear(int endYear)
	{
		return MultiCalendar.createFromGregorianYearMonthDay(endYear, 12, 31);
	}
	
	public DateUnitEffort createDateUnitEffort(int startYear, int endYear) throws Exception
	{
		return createDateUnitEffort(startYear, endYear, 0.0);
	}
	
	public DateUnitEffort createDateUnitEffort(int startYear, int endYear, double effort) throws Exception
	{
		DateUnit dateUnit = createDateUnit(startYear, endYear);
		return new DateUnitEffort(dateUnit,  effort);
	}
	
	public DateUnitEffort createDateUnitEffort(MultiCalendar startDate, MultiCalendar endDate) throws Exception
	{
		DateRange dateRange = createDateRange(startDate, endDate);
		return new DateUnitEffort(DateUnit.createFromDateRange(dateRange), 0);
	}

	public static DateRange createDateRange(int startYear, int endYear) throws Exception
	{
		MultiCalendar startDate = createStartYear(startYear);
		MultiCalendar endDate = createEndYear(endYear);
		return createDateRange(startDate, endDate);
	}
	
	public static DateRange createDateRange(MultiCalendar startDate, MultiCalendar endDate) throws Exception
	{
		return new DateRange(startDate, endDate);
	}
	
	public DateUnit createDateUnit(int sameYear) throws Exception
	{
		return createDateUnit(sameYear, sameYear);
	}
	
	public DateUnit createDateUnit(int startYear, int endYear) throws Exception
	{
		return DateUnit.createFromDateRange(createDateRange(startYear, endYear));
	}
	
	public DateUnit createSingleYearDateUnit(int year) throws Exception
	{
		
		return createDateUnit(year, year);
	}
	
	public MultiCalendar createMultiCalendar(int year)
	{
		return createStartYear(year);
	}
	
	public TimePeriodCosts createTimePeriodCosts(double expenses, ORef projectResourceRef, double units)
	{
		TimePeriodCosts timePeriodCosts = createTimePeriodCosts(projectResourceRef, units);
		addExpense(timePeriodCosts, expenses);
		
		return timePeriodCosts;
	}
	
	public void addExpense(TimePeriodCosts timePeriodCosts, double expenses)
	{
		timePeriodCosts.add(createTimePeriodCosts((expenses)));
	}

	public TimePeriodCosts createTimePeriodCosts(double expenses)
	{
		return new TimePeriodCosts(ORef.INVALID, ORef.INVALID, ORef.INVALID, ORef.INVALID, new OptionalDouble(expenses));
	}
	
	public TimePeriodCosts createTimePeriodCosts(ORef projectResourceRef, double workUnits)
	{
		return new TimePeriodCosts(projectResourceRef, ORef.INVALID, ORef.INVALID, ORef.INVALID, ORef.INVALID, new OptionalDouble(workUnits));
	}
	
	public Task createActivity() throws Exception
	{
		Strategy strategy = createStrategy();
		return createActivity(strategy);
	}

	public Task createActivity(Strategy strategy) throws Exception
	{
		Task activity = createTask(strategy);
		
		return activity;
	}
	
	public void appendActivityToStrategy(Strategy strategy, Task activity) throws Exception
	{
		appendTaskToParentIdList(strategy, activity, Strategy.TAG_ACTIVITY_IDS);	
	}

	public void appendMethodToIndicator(Indicator indicator, Task method) throws Exception
	{
		appendMethodToParentIdList(indicator, method, Indicator.TAG_METHOD_IDS);
	}
	
	private void appendTaskToParentIdList(BaseObject parent, BaseObject child, String childListTag) throws Exception
	{
		ORefList childTaskRefs = new ORefList(child);
		fillObjectUsingCommand(parent, childListTag, childTaskRefs.convertToIdList(TaskSchema.getObjectType()).toString());
	}
	
	private void appendMethodToParentIdList(BaseObject parent, BaseObject child, String childListTag) throws Exception
	{
		ORefList childMethodRefs = new ORefList(child);
		fillObjectUsingCommand(parent, childListTag, childMethodRefs.convertToIdList(MethodSchema.getObjectType()).toString());
	}
	
	public static double calculateTimePeriodCosts(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		return calculateRawTimePeriodCosts(baseObject, dateUnit).getValue();
	}
	
	public static OptionalDouble calculateRawTimePeriodCosts(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		return baseObject.calculateTimePeriodCostsForAssignments(dateUnit).getTotalWorkUnits();
	}
	
	private String createSampleFormattedData(String value)
	{
		String[] allowdFormattingTags = new String[] {"b", "i", "strike", "u", };
		for(String formatting : allowdFormattingTags)
		{
			value += "<" + formatting + ">sample formatted text</" + formatting + ">";
		}
		
		return value;
	}
			
	private static int nextTargetId;
	private static int nextCauseId;
	private static int nextStrategyId;
	private static int nextOtherId;
	
	public static final String PROJECT_RESOURCE_LABEL_TEXT = "John Doe";
	public static final String SIMPLE_THREAT_RATING_COMMENT = "sample simple threat rating comment";
	public static final String STRESS_BASED_THREAT_RATING_COMMENT = "sample stress based threat rating comment";
}
