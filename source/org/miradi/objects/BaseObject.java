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

import java.awt.Dimension;
import java.awt.Point;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.martus.util.xml.XmlUtilities;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.ChainWalker;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.BaseIdData;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.CodeData;
import org.miradi.objectdata.DimensionData;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.IntegerData;
import org.miradi.objectdata.ORefData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objectdata.PointData;
import org.miradi.objectdata.PseudoQuestionData;
import org.miradi.objectdata.PseudoStringData;
import org.miradi.objectdata.RefListData;
import org.miradi.objectdata.StringData;
import org.miradi.objectdata.UserTextData;
import org.miradi.objecthelpers.BaseObjectByNameSorter;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.InvalidNumberException;
import org.miradi.utils.OptionalDouble;

abstract public class BaseObject
{
	public BaseObject(ObjectManager objectManagerToUse, BaseId idToUse)
	{
		objectManager = objectManagerToUse;
		setId(idToUse);
		clear();
	}
	
	BaseObject(ObjectManager objectManagerToUse, BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		this(objectManagerToUse, idToUse);
		
		loadFromJson(json);
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_ID.equals(fieldTag))
			return id.toString();
		
		if (TAG_EMPTY.equals(fieldTag))
			return "";
		
		if(!doesFieldExist(fieldTag))
			throw new RuntimeException("Attempted to get data for bad field: " + fieldTag + " in object type: " + getClass().getSimpleName());

		String data = getField(fieldTag).get();
		if(data == null)
			throw new RuntimeException("BaseObject " + getRef() + " tag " + fieldTag + " was null");
		return data;
	}
	
	public String getStringData(String tag)
	{
		StringData data = (StringData)getField(tag);
		return data.get();
	}
	
	public int getIntegerData(String tag)
	{
		IntegerData data = (IntegerData)getField(tag);
		return data.asInt();
	}
	
	public Dimension getDimensionData(String tag)
	{
		DimensionData data = (DimensionData)getField(tag);
		return data.getDimension();
	}
	
	public void setDimensionData(String tag, Dimension value)
	{
		DimensionData data = (DimensionData)getField(tag);
		data.setDimension(value);
	}
	
	public Point getPointData(String tag)
	{
		PointData data = (PointData)getField(tag);
		return data.getPoint();
	}
	
	public void setPointData(String tag, Point point)
	{
		PointData data = (PointData)getField(tag);
		data.setPoint(point);
	}
	
	public IdList getIdListData(String fieldTag)
	{
		ObjectData field = getField(fieldTag);
		if(field.isIdListData())
			return ((IdListData)field).getIdList().createClone();

		throw new RuntimeException("Attempted to get IdList data from non-IdList field " + fieldTag);
	}
	
	public ORefList getRefListData(String fieldTag)
	{
		ObjectData field = getField(fieldTag);
		if(field.isRefListData())
		{
			RefListData refListField = (RefListData)field;
			ORefList refList = refListField.getRefList();
			return new ORefList(refList);
		}
		
		if(field.isIdListData())
		{
			IdList isList = getIdListData(fieldTag);
			ORefList refList = new ORefList(isList);
			return new ORefList(refList);
		}
			
		throw new RuntimeException("Attempted to get RefList data from non-RefList field " + fieldTag);
	}
	
	public ORef getRefData(String fieldTag)
	{
		ObjectData field = getField(fieldTag);
		if(field.isRefData())
			return new ORef(((ORefData)field).getRef());
		
		if(field.isBaseIdData())
			return new ORef(((BaseIdData)field).getRef());
		
		throw new RuntimeException("Attempted to get Ref data from non-Ref field " + fieldTag);
	}
	
	public void setRefData(String fieldTag, ORef ref)
	{
		ObjectData field = getField(fieldTag);
		if(field.isRefData())
			((ORefData)field).set(ref);
		else if(field.isBaseIdData())
			((BaseIdData)field).setRef(ref);
		else
			throw new RuntimeException("Attempted to set Ref data on non-Ref field " + fieldTag);
	}

	public void loadFromJson(EnhancedJsonObject json) throws Exception
	{
		Set<String> tags = getTags();
		for (String tag : tags)
		{
			if (!getField(tag).isPseudoField())
			{
				String value = json.optString(tag);
				try
				{
					setData(tag, value);
				}
				catch(InvalidNumberException e)
				{
					String newValue = value.replaceAll("[^0-9\\-\\.,]", "");
					EAM.logWarning("Fixing bad numeric data in " + tag + " from " + value + " to " + newValue);
					setData(tag, newValue);
				}
			}
		}
	}

	private Set<String> getTags()
	{
		return getFields().keySet();
	}

	private HashMap<String, ObjectData> getFields()
	{
		return fields;
	}
	
	public ORef getORef(String tag) throws Exception
	{
		return ORef.createFromString(getData(tag));
	}
	
	public CodeList getCodeList(String tag) throws Exception
	{
		return new CodeList(getData(tag));
	}
	
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;
		
		return false;
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_EXPENSE_ASSIGNMENT_REFS))
			return true;
		
		if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return true;
		
		return false;
	}
	
	public ORefList getRefList(String tag) throws Exception
	{
		return getRefListForField(getField(tag));
	}
	
	public ORef getRef(String tag)
	{
		return (getField(tag).getRef());
	}
	
	public boolean isRelevancyOverrideSet(String tag)
	{
		return false;
	}
			
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_RESOURCE_ASSIGNMENT_IDS))
			return ResourceAssignment.getObjectType();
		
		if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return ProgressReport.getObjectType();
		
		throw new RuntimeException("Cannot find annotation type for " + tag);
	}
	
	public ORef getRef()
	{
		return new ORef(getType(), getId());
	}
	
	public ObjectManager getObjectManager()
	{
		return objectManager;
	}
	
	public Project getProject()
	{
		return objectManager.getProject();
	}
	
	public ChainWalker getNonDiagramChainWalker()
	{
		return getObjectManager().getDiagramChainWalker();
	}
	
	public ChainWalker getDiagramChainWalker()
	{
		return getObjectManager().getDiagramChainWalker();
	}
		
	public static BaseObject createFromJson(ObjectManager objectManager, int type, EnhancedJsonObject json) throws Exception
	{
		int idAsInt = json.getInt(TAG_ID);
		switch(type)
		{
			case ObjectType.RATING_CRITERION:
				return new RatingCriterion(objectManager, idAsInt, json);
				
			case ObjectType.VALUE_OPTION:
				return new ValueOption(objectManager, idAsInt, json);
				
			case ObjectType.TASK:
				return new Task(objectManager, new FactorId(idAsInt), json);
			
			case ObjectType.STRESS:
				return new Stress(objectManager, new FactorId(idAsInt), json);

			case ObjectType.GROUP_BOX:
				return new GroupBox(objectManager, new FactorId(idAsInt), json);

			case ObjectType.TEXT_BOX:
				return new TextBox(objectManager, new FactorId(idAsInt), json);

			case ObjectType.THREAT_REDUCTION_RESULT:
				return new ThreatReductionResult(objectManager, new FactorId(idAsInt), json);

			case ObjectType.INTERMEDIATE_RESULT:	
				return new IntermediateResult(objectManager, new FactorId(idAsInt), json);

			case ObjectType.CAUSE:
				return new Cause(objectManager, new FactorId(idAsInt), json);
				
			case ObjectType.STRATEGY:
				return new Strategy(objectManager, new FactorId(idAsInt), json);
				
			case ObjectType.TARGET:
				return new Target(objectManager, new FactorId(idAsInt), json);

			case ObjectType.VIEW_DATA:
				return new ViewData(objectManager, idAsInt, json);
				
			case ObjectType.FACTOR_LINK:
				return new FactorLink(objectManager, idAsInt, json);
				
			case ObjectType.PROJECT_RESOURCE:
				return new ProjectResource(objectManager, idAsInt, json);
				
			case ObjectType.INDICATOR:
				return new Indicator(objectManager, idAsInt, json);
				
			case ObjectType.OBJECTIVE:
				return new Objective(objectManager, idAsInt, json);
				
			case ObjectType.GOAL:
				return new Goal(objectManager, idAsInt, json);
				
			case ObjectType.PROJECT_METADATA:
				return new ProjectMetadata(objectManager, idAsInt, json);
				
			case ObjectType.DIAGRAM_LINK:
				return new DiagramLink(objectManager, idAsInt, json);
				
			case ObjectType.RESOURCE_ASSIGNMENT:
				return new ResourceAssignment(objectManager, idAsInt, json);
				
			case ObjectType.ACCOUNTING_CODE:
				return new AccountingCode(objectManager, idAsInt, json);
				
			case ObjectType.FUNDING_SOURCE:
				return new FundingSource(objectManager, idAsInt, json);
				
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE:
				return new KeyEcologicalAttribute(objectManager, idAsInt, json);
			
			case ObjectType.DIAGRAM_FACTOR:
				return new DiagramFactor(objectManager, idAsInt, json);
				
			case ObjectType.CONCEPTUAL_MODEL_DIAGRAM:
				return new ConceptualModelDiagram(objectManager, idAsInt, json);
			
			case ObjectType.RESULTS_CHAIN_DIAGRAM:
				return new ResultsChainDiagram(objectManager, idAsInt, json);
				
			case ObjectType.OBJECT_TREE_TABLE_CONFIGURATION:
				return new ObjectTreeTableConfiguration(objectManager, idAsInt, json);
				
			case ObjectType.WWF_PROJECT_DATA:
				return new WwfProjectData(objectManager, idAsInt, json);
			
			case ObjectType.COST_ALLOCATION_RULE:
				return new CostAllocationRule(objectManager, idAsInt, json);
				
			case ObjectType.MEASUREMENT:
				return new Measurement(objectManager, idAsInt, json);
			
			case ObjectType.THREAT_STRESS_RATING:
				return new ThreatStressRating(objectManager, idAsInt, json);
			
			case ObjectType.SUB_TARGET:
				return new SubTarget(objectManager, idAsInt, json);
			
			case ObjectType.PROGRESS_REPORT:
				return new ProgressReport(objectManager, idAsInt, json);
			
			case ObjectType.RARE_PROJECT_DATA:
				return new RareProjectData(objectManager, idAsInt, json);
				
			case ObjectType.WCS_PROJECT_DATA:
				return new WcsProjectData(objectManager, idAsInt, json);	
			
			case ObjectType.TNC_PROJECT_DATA:
				return new TncProjectData(objectManager, idAsInt, json);
				
			case ObjectType.FOS_PROJECT_DATA:
				return new FosProjectData(objectManager, idAsInt, json);
			
			case ObjectType.ORGANIZATION:
				return new Organization(objectManager, idAsInt, json);
				
			case ObjectType.WCPA_PROJECT_DATA:
				return new WcpaProjectData(objectManager, idAsInt, json);
	
			case ObjectType.XENODATA:
				return new Xenodata(objectManager, idAsInt, json);
			
			case ObjectType.PROGRESS_PERCENT:
				return new ProgressPercent(objectManager, idAsInt, json);
				
			case ObjectType.REPORT_TEMPLATE:
				return new ReportTemplate(objectManager, idAsInt, json);
				
			case ObjectType.TAGGED_OBJECT_SET:
				return new TaggedObjectSet(objectManager, idAsInt, json);
				
			case ObjectType.TABLE_SETTINGS:
				return new TableSettings(objectManager, idAsInt, json);
				
			case ObjectType.THREAT_RATING_COMMENTS_DATA:
				return new ThreatRatingCommentsData(objectManager, idAsInt, json);
				
			case ObjectType.SCOPE_BOX:
				return new ScopeBox(objectManager, new FactorId(idAsInt), json);
				
			case ObjectType.EXPENSE_ASSIGNMENT:
				return new ExpenseAssignment(objectManager, idAsInt, json);
				
			case ObjectType.HUMAN_WELFARE_TARGET:
				return new HumanWelfareTarget(objectManager, new FactorId(idAsInt), json);
				
			case ObjectType.IUCN_REDLIST_SPECIES:
				return new IucnRedlistSpecies(objectManager, idAsInt, json);
				
			case ObjectType.OTHER_NOTABLE_SPECIES:
				return new OtherNotableSpecies(objectManager, idAsInt, json);
				
			case ObjectType.AUDIENCE:
				return new Audience(objectManager, idAsInt, json);
			
			case ObjectType.BUDGET_CATEGORY_ONE:
				return new BudgetCategoryOne(objectManager, idAsInt, json);
				
			case ObjectType.BUDGET_CATEGORY_TWO:
				return new BudgetCategoryTwo(objectManager, idAsInt, json);
			
			case ObjectType.DASHBOARD:
				return new Dashboard(objectManager, idAsInt, json);
				
			default:
				throw new RuntimeException("Attempted to create unknown EAMObject type " + type);
		}
	}

	abstract public int getType();
	abstract public String getTypeName();
	
	@Override
	public int hashCode()
	{
		return getRef().hashCode();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof BaseObject))
			return false;
		
		BaseObject other = (BaseObject)rawOther;
		return other.getRef().equals(getRef());
	}
	
	public String getSafeLabel(BaseObject baseObject)
	{
		if (baseObject == null)
			return "";
		
		return baseObject.getLabel();
	}
		
	public String getLabel()
	{
		return getData(TAG_LABEL);
	}
	
	public String getShortLabel()
	{
		return "";
	}
	
	public String getFullName()
	{
		return combineShortLabelAndLabel();
	}
	
	protected String toFullNameWithCode(String codeToUse)
	{
		String result = "";
		if(codeToUse.length() > 0)
			result += codeToUse + ": ";
		
		result += combineShortLabelAndLabel();
		
		return result;
	}
	
	protected String toString(String defaultValue)
	{
		String result = getFullName();
		if(result.length() > 0)
			return result;
		
		return defaultValue;
	}
	
	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_ID.equals(fieldTag))
		{
			id = new BaseId(Integer.parseInt(dataValue));
			return;
		} 
		
		if(!doesFieldExist(fieldTag))
			throw new RuntimeException("Object Ref = " + getRef() + ". Attempted to set data for bad field: " + fieldTag);

		ORefSet oldReferrals = getAllReferencedObjects();
		getField(fieldTag).set(dataValue);
		ORefSet newReferrals = getAllReferencedObjects();
		if(getObjectManager() != null)
		{
			getObjectManager().updateReferrerCache(getRef(), oldReferrals, newReferrals);
		}
	}
	
	public boolean isPseudoField(String fieldTag)
	{
		return getField(fieldTag).isPseudoField();
	}
	
	public boolean doesFieldExist(String fieldTag)
	{
		return getFields().containsKey(fieldTag);
	}
	
	public boolean isEmpty()
	{
		Vector<String> fieldTags = getStoredFieldTags();
		for (int index = 0; index < fieldTags.size(); ++index)
		{
			String tag = fieldTags.get(index);
			if (!getField(tag).isEmpty())
				return false;
		}
		
		return true;
	}
	

	public BaseId getId()
	{
		return id;
	}
	
	private void setId(BaseId newId)
	{
		id = newId;
	}
	
	public boolean isAssignmentDataSuperseded(DateUnit dateUnit) throws Exception
	{		
		return false;
	}
	
	protected boolean hasAnySubtaskResourceData(DateUnit dateUnit) throws Exception
	{
		return hasAnySubtaskAssignmentData(dateUnit, TAG_RESOURCE_ASSIGNMENT_IDS);
	}
	
	protected boolean hasAnySubtaskExpenseData(DateUnit dateUnit) throws Exception
	{	
		return hasAnySubtaskAssignmentData(dateUnit, TAG_EXPENSE_ASSIGNMENT_REFS);
	}

	private boolean hasAnySubtaskAssignmentData(DateUnit dateUnit, String assignmentRefsTag) throws Exception
	{
		TimePeriodCostsMap subTaskTimePeriodCostsMap = getTotalTimePeriodCostsMapForSubTasks(getSubTaskRefs(), assignmentRefsTag);
		
		return subTaskTimePeriodCostsMap.hasDateUnitsContained(dateUnit);
	}
	
	public OptionalDouble getTotalBudgetCost() throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = getTotalTimePeriodCostsMap();
		return totalTimePeriodCostsMap.calculateTotalBudgetCost(getProject());
	}

	public OptionalDouble getTotalBudgetCostWithoutRollup() throws Exception
	{
		TimePeriodCostsMap assignmentTimePeriodCostsMap = getTotalTimePeriodCostsMapForAssignments(TAG_RESOURCE_ASSIGNMENT_IDS);
		TimePeriodCostsMap expenseTimePeriodCostsMap = getTotalTimePeriodCostsMapForAssignments(TAG_EXPENSE_ASSIGNMENT_REFS);
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		mergedTimePeriodCostsMap.mergeNonConflicting(expenseTimePeriodCostsMap);
		mergedTimePeriodCostsMap.mergeNonConflicting(assignmentTimePeriodCostsMap);
		
		return mergedTimePeriodCostsMap.calculateTotalBudgetCost(getProject());
	}
	
	public int getTotalShareCount()
	{
		return 1;
	}
	
	public TimePeriodCosts calculateTimePeriodCosts(DateUnit dateUnitToUse)throws Exception
	{
		return getTotalTimePeriodCostsMap().calculateTimePeriodCosts(dateUnitToUse);
	}
	
	public TimePeriodCostsMap getTotalTimePeriodCostsMap() throws Exception
	{
		TimePeriodCostsMap expenseAssignmentsTimePeriodCostsMap = getExpenseAssignmentsTimePeriodCostsMap();
		TimePeriodCostsMap resourceAssignmentsTimePeriodCostsMap = getResourceAssignmentsTimePeriodCostsMap();
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		mergedTimePeriodCostsMap.mergeAll(expenseAssignmentsTimePeriodCostsMap);
		mergedTimePeriodCostsMap.mergeAll(resourceAssignmentsTimePeriodCostsMap);
		
		return mergedTimePeriodCostsMap;
	}

	public TimePeriodCostsMap getExpenseAssignmentsTimePeriodCostsMap() throws Exception
	{
		return getTimePeriodCostsMap(TAG_EXPENSE_ASSIGNMENT_REFS);
	}

	public TimePeriodCostsMap getResourceAssignmentsTimePeriodCostsMap() throws Exception
	{
		return getTimePeriodCostsMap(TAG_RESOURCE_ASSIGNMENT_IDS);
	}
	
	protected TimePeriodCostsMap getTimePeriodCostsMap(String tag) throws Exception
	{
		TimePeriodCostsMap subTaskTimePeriodCosts = getTotalTimePeriodCostsMapForSubTasks(getSubTaskRefs(), tag);
		TimePeriodCostsMap assignmentTimePeriodCostsMap = getTotalTimePeriodCostsMapForAssignments(tag);
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		mergedTimePeriodCostsMap.mergeNonConflicting(subTaskTimePeriodCosts);
		mergedTimePeriodCostsMap.mergeNonConflicting(assignmentTimePeriodCostsMap);
		
		return mergedTimePeriodCostsMap;	
	}
	
	public TimePeriodCostsMap getTotalTimePeriodCostsMapForSubTasks(ORefList subTaskRefs, String tag) throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		for (int index = 0; index < subTaskRefs.size(); ++index)
		{
			Task task = Task.find(getProject(), subTaskRefs.get(index));
			timePeriodCostsMap.mergeAll(task.getTimePeriodCostsMap(tag));
		}
		
		return timePeriodCostsMap;
	}

	protected TimePeriodCostsMap getTotalTimePeriodCostsMapForAssignments(String tag) throws Exception
	{
		TimePeriodCostsMap timePeriodCostsMap = new TimePeriodCostsMap();
		ORefList assignmentRefs = getRefList(tag);
		for(int i = 0; i < assignmentRefs.size(); ++i)
		{
			BaseObject assignment = BaseObject.find(getObjectManager(), assignmentRefs.get(i));
			timePeriodCostsMap.mergeAll(assignment.getTimePeriodCostsMap(tag));
		}
		
		return timePeriodCostsMap;
	}
	
	public ORefList getSubTaskRefs()
	{
		return new ORefList();
	}
					
	public ORefSet getAssignedResourceRefs() throws Exception
	{
		ORefSet projectResourceRefs = new ORefSet();
		ORefList resourceAssignmentRefs = getRefList(TAG_RESOURCE_ASSIGNMENT_IDS);
		for (int index = 0; index < resourceAssignmentRefs.size(); ++index)
		{
			ResourceAssignment resourceAssignment = ResourceAssignment.find(getProject(),resourceAssignmentRefs.get(index)); 
			projectResourceRefs.add(resourceAssignment.getResourceRef());
		}
		
		return projectResourceRefs;
	}

	public String formatCurrency(double cost)
	{
		if(cost == 0.0)
			return "";
		
		CurrencyFormat formater = objectManager.getProject().getCurrencyFormatterWithCommas();
		return formater.format(cost);
	}
	
	public String getWhenTotalAsString()
	{
		try
		{
			return getProject().getProjectCalendar().convertToSafeString(getWhenRollup());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "";
		} 
	}
	
	public DateRange getWhenRollup() throws Exception
	{
		final DateRange projectStartEndDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
		return getTotalTimePeriodCostsMap().getRolledUpDateRange(projectStartEndDateRange);
	}
	

	protected ORefList calculateRelevantRefList(ORefSet relevantRefList, RelevancyOverrideSet relevantOverrides)
	{
		for(RelevancyOverride override : relevantOverrides)
		{
			if (override.getRef().isInvalid())
			{
				EAM.logWarning("An invalid ref was found inside the relevancy list for Desire with ref = " + getRef());
				continue;
			}
			
			if (getProject().findObject(override.getRef()) == null)
				continue;
			
			if (override.isOverride())
				relevantRefList.add(override.getRef());
			else
				relevantRefList.remove(override.getRef());
		}
		return new ORefList(relevantRefList);
	}
	
	protected RelevancyOverrideSet computeRelevancyOverrides(ORefList refList1, ORefList refList2,	boolean relevancyValue)
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList overrideRefs = ORefList.subtract(refList1, refList2);
		for (int i = 0; i < overrideRefs.size(); ++i)
		{
			RelevancyOverride thisOverride = new RelevancyOverride(overrideRefs.get(i), relevancyValue);
			relevantOverrides.add(thisOverride);
		}
		
		return relevantOverrides;
	}

	void clear()
	{
		fields = new HashMap<String, ObjectData>();
		presentationDataFields = new HashSet<String>();

		addField(new UserTextData(TAG_LABEL));
		addField(new IdListData(TAG_RESOURCE_ASSIGNMENT_IDS, ResourceAssignment.getObjectType()));
		addField(new RefListData(TAG_EXPENSE_ASSIGNMENT_REFS));
		addField(new RefListData(TAG_PROGRESS_REPORT_REFS));
		
		addField(new PseudoStringData(this, PSEUDO_TAG_WHEN_TOTAL));
		addField(new PseudoQuestionData(this, PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, createSet(TAG_PROGRESS_REPORT_REFS)));
		addField(new PseudoStringData(this, PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS));
	}
	
	public static HashSet<String> createSet(String parentTagToUse)
	{
		HashSet<String> singleItemSet = new HashSet<String>();
		singleItemSet.add(parentTagToUse);
		
		return singleItemSet;
	}
	
	protected ChoiceQuestion getQuestion(Class questionClass)
	{
		return StaticQuestionManager.getQuestion(questionClass);
	}
	
	protected void createUserTextField(String tag)
	{
		addField(new UserTextData(tag));
	}
	
	protected void createChoiceField(String tag, Class questionClass)
	{
		addField(new ChoiceData(tag, getQuestion(questionClass)));
	}
	
	protected void createCodeField(String tag)
	{
		addField(new CodeData(tag));
	}
	
	protected void createIdListField(String tag, int type)
	{
		addField(new IdListData(tag, type));
	}

	protected void createRefListField(String tag)
	{
		addField(new RefListData(tag));
	}

	protected void createRefField(String tag)
	{
		addField(new ORefData(tag));
	}

	protected void createPointField(String tag)
	{
		addField(new PointData(tag));
	}

	protected void createDimensionField(String tag)
	{
		addField(new DimensionData(tag));
	}
	
	protected void createPseudoStringField(String tag)
	{
		addField(new PseudoStringData(this, tag));
	}
	
	protected void addField(ObjectData data)
	{
		addField(data.getTag(), data);
	}

	protected void addField(String tag, ObjectData data)
	{
		if(!data.getTag().equals(tag))
			throw new RuntimeException("Wrong tag: " + tag + " in " + data.getTag() + " for " + getRef());
		getFields().put(tag, data);
	}
	
	protected void addPresentationDataField(String tag, ObjectData data)
	{
		addField(tag, data);
		presentationDataFields.add(tag);
	}
	
	public boolean isPresentationDataField(String tag)
	{
		return presentationDataFields.contains(tag);
	}
	
	public String[] getFieldTags()
	{
		return getTags().toArray(new String[0]);
	}
	
	public Vector<String> getStoredFieldTags()
	{
		Vector<String> storedFieldTags = new Vector<String>();
		String[] fieldTags = getFieldTags();
		for (int index = 0; index < fieldTags.length; ++index)
		{
			if (!isPseudoField(fieldTags[index]))
				storedFieldTags.add(fieldTags[index]);
		}
		
		return storedFieldTags;
	}

	public ObjectData getField(String fieldTag)
	{
		ObjectData data = getFields().get(fieldTag);
		if(data == null)
			EAM.logWarning("BaseObject.getField called for unknown tag " + fieldTag + " in " + getRef());
		return data;
	}
	
	public Vector<CommandSetObjectData> createCommandsToClear()
	{
		Vector<CommandSetObjectData> commands = new Vector<CommandSetObjectData>();
		Set<String> tags = getTags();
		for (String tag : tags)
		{
			if(isPseudoField(tag))
				continue;
		
			commands.add(new CommandSetObjectData(getType(), getId(), tag, ""));
		}
		
		return commands;
	}
	
	public CommandVector createCommandsToDeleteChildrenAndObject() throws Exception
	{
		CommandVector commandsToDeleteChildrenAndObject = new CommandVector();
		commandsToDeleteChildrenAndObject.addAll(createCommandsToClear());
		commandsToDeleteChildrenAndObject.addAll(createCommandsToDeleteChildren());
		commandsToDeleteChildrenAndObject.addAll(createCommandsToDereferenceObject());
		commandsToDeleteChildrenAndObject.add(new CommandDeleteObject(this));
		
		return commandsToDeleteChildrenAndObject;
	}
	
	protected CommandVector createCommandsToDereferenceObject() throws Exception
	{
		return new CommandVector();
	}

	public CommandVector createCommandsToDeleteChildren() throws Exception
	{
		CommandVector commandsToDeleteChildren  = new CommandVector();
		commandsToDeleteChildren.addAll(createCommandsToDeleteBudgetChildren());
		commandsToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_PROGRESS_REPORT_REFS));
		
		return commandsToDeleteChildren;
	}
	
	protected CommandVector createCommandsToDeleteBudgetChildren() throws Exception
	{
		CommandVector commandToDeleteChildren = new CommandVector();
		commandToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_EXPENSE_ASSIGNMENT_REFS));
		commandToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_RESOURCE_ASSIGNMENT_IDS));
		
		return commandToDeleteChildren;
	}

	protected CommandVector createCommandsToDeleteRefs(String tag) throws Exception
	{
		ORefList refsToDelete = getRefList(tag);
		CommandVector commandsToDeleteRefList = new CommandVector();
		commandsToDeleteRefList.add(new CommandSetObjectData(this, tag, ""));
		commandsToDeleteRefList.addAll(createDeleteCommands(refsToDelete));
		
		return commandsToDeleteRefList;
	}

	private CommandVector createDeleteCommands(ORefList refs)
	{
		CommandVector deleteCommands = new CommandVector();
		for (int index = 0; index < refs.size(); ++index)
		{
			BaseObject objectToDelete = BaseObject.find(getProject(), refs.get(index));
			deleteCommands.addAll(objectToDelete.createCommandsToShallowDelete());
		}
		
		return deleteCommands;
	}
	
	public CommandVector createCommandsToShallowDelete()
	{
		CommandVector commandsToShallowDelete = new CommandVector();
		commandsToShallowDelete.addAll(createCommandsToClear());
		commandsToShallowDelete.add(new CommandDeleteObject(this));
		
		return commandsToShallowDelete;
	}
	
	//Note this method does not clone referenced or owned objects
	public CommandSetObjectData[] createCommandsToClone(BaseId baseId)
	{
		Vector<CommandSetObjectData> commands = new Vector<CommandSetObjectData>();
		Set<String> tags = getTags();
		for (String tag : tags)
		{
			if(isPseudoField(tag))
				continue;
			if(isIdListTag(tag))
				continue;
			if(isRefList(tag))
				continue;
			
			commands.add(new CommandSetObjectData(getType(), baseId, tag, getData(tag)));
		}
		return commands.toArray(new CommandSetObjectData[0]);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_TIME_STAMP_MODIFIED, Long.toString(new Date().getTime()));
		json.put(TAG_ID, id.asInt());
		Set<String> tags = getTags();
		for (String tag : tags)
		{
			if(isPseudoField(tag))
				continue;
			ObjectData data = getField(tag);
			json.put(tag, data.get());
		}
		
		return json;
	}
	
	public static String toHtml(BaseObject[] resources)
	{
		StringBuffer result = new StringBuffer();
		result.append("<html>");
		for(int i = 0; i < resources.length; ++i)
		{
			if(i > 0)
				result.append("; ");
			result.append(XmlUtilities.getXmlEncoded(resources[i].toString()));
		}
		result.append("</html>");
		
		return result.toString();
	}

	public Factor[] getUpstreamDownstreamFactors()
	{
		Factor owner = getDirectOrIndirectOwningFactor();
		if(owner == null)
			return new Factor[0];
		
		ChainWalker chainObject = getNonDiagramChainWalker();
		return chainObject.buildNormalChainAndGetFactors(owner).toFactorArray();
	}
	
	public String getRelatedLabelsAsMultiLine(FactorSet filterSet)
	{
		try
		{
			Factor[] upstreamDownstreamFactors = getUpstreamDownstreamFactors();
			filterSet.attemptToAddAll(upstreamDownstreamFactors);
			return getLabelsAsMultiline(filterSet);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	public String getLabelsAsMultiline(FactorSet factors)
	{
		StringBuffer result = new StringBuffer();
		Iterator iter = factors.iterator();
		while(iter.hasNext())
		{
			if(result.length() > 0)
				result.append("\n");
			
			Factor factor = (Factor)iter.next();
			result.append(factor.getLabel());
		}
		
		return result.toString();
	}
	
	public String combineShortLabelAndLabel(String shortLabel, String Longlabel)
	{
		if (shortLabel.length() <= 0)
			return Longlabel;
		
		if (Longlabel.length() <= 0)
			return shortLabel;
		
		return shortLabel + ". " + Longlabel;
	}
	
	public String combineShortLabelAndLabel()
	{
		return combineShortLabelAndLabel(getShortLabel(), getLabel());
	}
	
	public BaseObject getOwner()
	{
		ORef oref = getOwnerRef();
		if (oref==null || oref.isInvalid())
			return null;
		return objectManager.findObject(oref);
	}

	public ORef getOwnerRef()
	{
		int[] possibleOwningTypes = getTypesThatCanOwnUs();
		if(possibleOwningTypes.length == 0)
			return ORef.INVALID;
		
		ORefList possibleOwners = findObjectsThatReferToUs();
		ORef ownerRef = possibleOwners.getRefForTypes(possibleOwningTypes);
		if(ownerRef.isInvalid())
			EAM.logVerbose("getOwnerRef didn't find owner for: " + getRef());
		
		return ownerRef;
	}
	
	abstract public int[] getTypesThatCanOwnUs();

	public ORefList findObjectsThatReferToUs()
	{
		return new ORefList(getObjectManager().getReferringObjects(getRef()));
	}
	
	public ORefList findObjectsThatReferToUs(int[] objectTypes)
	{
		ORefList referrers = new ORefList();
		for (int index = 0; index < objectTypes.length; ++index)
		{
			referrers.addAll(findObjectsThatReferToUs(objectTypes[index]));
		}
		
		return referrers; 
	}
	
	public ORefList findObjectsThatReferToUs(int objectType)
	{
		return findObjectsThatReferToUs().getFilteredBy(objectType);
	}
	
	static public ORefList findObjectsThatReferToUs(ObjectManager objectManager, int objectType, ORef oref)
	{
		BaseObject object = objectManager.findObject(oref);
		return object.findObjectsThatReferToUs(objectType);
	}
	
	public boolean hasReferrers()
	{
		ORefList referrers = findObjectsThatReferToUs();
		if (referrers.size() > 0)
			return true;

		return false;
	}
	
	public ORefList getReferencedObjects(int objectType)
	{
		ORefList referenced = new ORefList();
		ORefSet all = getAllReferencedObjects();
		for(ORef ref : all)
		{
			if(ref.getObjectType() == objectType)
				referenced.add(ref);
		}
		return referenced;
	}
	
	public ORefSet getAllReferencedObjects()
	{
		ORefSet list = new ORefSet();
		for(ObjectData field : getFields().values())
		{
			ORefList refList = getRefListForField(field);
			list.addAllRefs(refList);
		}
		return list;
	}

	protected ORefList getRefListForField(ObjectData field)
	{
		ORefList refList = field.getRefList();
		return refList;
	}
	
	public ORefList createRefList(String tag, String unknonwListTypeAsString) throws ParseException 
	{
		if (isRefList(tag))
		{
			return new ORefList(unknonwListTypeAsString);
		}
		
		if (isIdListTag(tag))
		{
			final int type = getType();
			IdList idList = new IdList(type, unknonwListTypeAsString);

			return new ORefList(type, idList);
		}
		
		throw new RuntimeException("List as string is not a known list type: " + unknonwListTypeAsString + " for ref:" + getRef());
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = new ORefList();
		switch(objectType)
		{
			case ObjectType.RESOURCE_ASSIGNMENT: 
				list.addAll(getResourceAssignmentRefs());
				break;
			case ObjectType.EXPENSE_ASSIGNMENT:
				list.addAll(getExpenseAssignmentRefs());
				break;
			case ObjectType.PROGRESS_REPORT:
				list.addAll(getRefListData(TAG_PROGRESS_REPORT_REFS));
				break;
		}
		
		return list;
	}

	public ORefList getExpenseAssignmentRefs()
	{
		return getRefListData(TAG_EXPENSE_ASSIGNMENT_REFS);
	}
	
	public ORefList getResourceAssignmentRefs()
	{
		return new ORefList(ResourceAssignment.getObjectType(), getIdListData(TAG_RESOURCE_ASSIGNMENT_IDS));
	}
	
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		return new ORefList(getAllOwnedObjects());
	}
	
	public ORefList getAllOwnedObjects()
	{
		ORefList allOwnedObjects = new ORefList();
		for (int objectTypeIndex = 0; objectTypeIndex < ObjectType.OBJECT_TYPE_COUNT; ++objectTypeIndex)
		{
			ORefList ownedObjects = getOwnedObjects(objectTypeIndex);
			allOwnedObjects.addAll(ownedObjects);
		}
		
		return allOwnedObjects;
	}
	
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_WHEN_TOTAL))
			return getWhenTotalAsString();
		
		if(fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return getLatestProgressReportDate();
		
		if(fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS))
			return getLatestProgressReportDetails();
		
		ObjectData field = getField(fieldTag);
		if(field.isPseudoField())
		{
			EAM.logError("BaseObject.getPseudoData called for: " + fieldTag);
			return "";
		}
		
		return getData(fieldTag);
	}
	
	public String  getLatestProgressReportDate()
	{
		ProgressReport progressReport = getLatestProgressReport();
		if (progressReport == null)
			return "";
		
		return progressReport.getProgressStatusChoice().getCode();
	}

	protected String getLatestProgressReportDetails()
	{
		ProgressReport progressReport = getLatestProgressReport();
		if (progressReport == null)
			return "";
		
		return progressReport.getData(ProgressReport.TAG_DETAILS);
	}

	public ProgressReport getLatestProgressReport()
	{
		return (ProgressReport) getLatestObject(getObjectManager(), getRefListData(TAG_PROGRESS_REPORT_REFS), ProgressReport.TAG_PROGRESS_DATE);
	}

	protected static BaseObject getLatestObject(ObjectManager objectManagerToUse, ORefList objectRefs, String dateTag)
	{
		BaseObject latestObjectToFind = null; 
		for (int i = 0; i < objectRefs.size(); ++i)
		{
			BaseObject thisObject = objectManagerToUse.findObject(objectRefs.get(i));
			if (i == 0)
				latestObjectToFind = thisObject;
			
			String thisDateAsString = thisObject.getData(dateTag);
			String latestDateAsString = latestObjectToFind.getData(dateTag);
			if (thisDateAsString.compareTo(latestDateAsString) > 0)
			{
				latestObjectToFind = thisObject;
			}
		}
		
		return latestObjectToFind;		
	}
	
	public Factor getDirectOrIndirectOwningFactor()
	{
		ORef ownerRef = getRef();
		int AVOID_INFINITE_LOOP = 10000;
		for(int i = 0; i < AVOID_INFINITE_LOOP; ++i)
		{
			if(ownerRef.isInvalid())
				return null;
			
			BaseObject owner = getObjectManager().findObject(ownerRef);
			if(Factor.isFactor(owner.getType()))
				return (Factor)owner;
			
			ownerRef = owner.getOwnerRef();
		}
		return null;
	}

	public static BaseObject find(ObjectManager objectManager, ORef objectRef)
	{
		return objectManager.findObject(objectRef);
	}
	
	public static BaseObject find(Project project, ORef objectRef)
	{
		return find(project.getObjectManager(), objectRef);
	}
	
	public boolean isType(int typeToUse)
	{
		return getType() == typeToUse;
	}
	
	public String getBaseObjectLabelsOnASingleLine(ORefList refs)
	{
		Vector<BaseObject> objects = new Vector<BaseObject>();
		for(int i = 0; i < refs.size(); ++i)
			objects.add(BaseObject.find(getObjectManager(), refs.get(i)));
		
		Collections.sort(objects, new BaseObjectByNameSorter());
		
		final String FAKE_BULLET = "- ";
		final String NEW_LINE = "\n";
		StringBuffer result = new StringBuffer();
		for(int index = 0; index < objects.size(); ++index)
		{
			if(objects.size() > 1)
				result.append(FAKE_BULLET);
			
			result.append(objects.get(index).getData(BaseObject.TAG_LABEL));

			if(objects.size() > 1)
				result.append(NEW_LINE);
		}
		
		return result.toString();
	}

	public static final String TAG_TIME_STAMP_MODIFIED = "TimeStampModified";
	public static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	public static final String TAG_EMPTY = "EMPTY";
	protected static final int[] NO_OWNERS = new int[] {};
	
	public static final String DEFAULT_LABEL = "";
	
	public final static String PSEUDO_TAG_WHEN_TOTAL = "EffortDatesTotal";
	
	public static final String PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE = "PseudoLatestProgressReportCode";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS = "PseudoLatestProgressReportDetails";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_EXPENSE_ASSIGNMENT_REFS = "ExpenseRefs";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";

	protected BaseId id;

	protected ObjectManager objectManager;
	private HashMap<String, ObjectData> fields;
	private HashSet<String> presentationDataFields; 
}
