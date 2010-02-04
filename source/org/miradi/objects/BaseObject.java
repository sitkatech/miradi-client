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

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.martus.util.xml.XmlUtilities;
import org.miradi.commands.Command;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramChainObject;
import org.miradi.diagram.factortypes.FactorTypeCause;
import org.miradi.diagram.factortypes.FactorTypeStrategy;
import org.miradi.diagram.factortypes.FactorTypeTarget;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.ObjectData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.ProjectChainObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ProgressReportLongStatusQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.InvalidNumberException;
import org.miradi.utils.OptionalDouble;
import org.miradi.utils.Utility;

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
	
	public void loadFromJson(EnhancedJsonObject json) throws Exception
	{
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
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
	
	public Command[] createCommandsToLoadFromJson(EnhancedJsonObject json) throws Exception
	{
		Vector commands = new Vector();
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			if (getField(tag).isPseudoField() || nonClearedFieldTags.contains(tag))
				continue;
			
			CommandSetObjectData setDataCommand = new CommandSetObjectData(getRef(), tag, json.optString(tag));
			commands.add(setDataCommand);
		}
		
		return (Command[]) commands.toArray(new Command[0]);
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
	
	public ProjectChainObject getProjectChainBuilder()
	{
		return getObjectManager().getProjectChainBuilder();
	}
	
	public DiagramChainObject getDiagramChainBuilder()
	{
		return getObjectManager().getDiagramChainBuilder();
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
			case ObjectType.STRATEGY:
			case ObjectType.TARGET:
			case ObjectType.FACTOR:
				return createFactorFromJson(objectManager, json, idAsInt);

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
				
			case ObjectType.PLANNING_VIEW_CONFIGURATION:
				return new PlanningViewConfiguration(objectManager, idAsInt, json);
				
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
				
			default:
				throw new RuntimeException("Attempted to create unknown EAMObject type " + type);
		}
	}

	private static Factor createFactorFromJson(ObjectManager objectManager, EnhancedJsonObject json, int idAsInt) throws Exception
	{
		String typeString = json.optString(Factor.TAG_NODE_TYPE);

		if(typeString.equals(FactorTypeStrategy.STRATEGY_TYPE))
			return new Strategy(objectManager, new FactorId(idAsInt), json);
		
		if(typeString.equals(FactorTypeCause.CAUSE_TYPE))
			return new Cause(objectManager, new FactorId(idAsInt), json);
		
		if(typeString.equals(FactorTypeTarget.TARGET_TYPE))
			return new Target(objectManager, new FactorId(idAsInt), json);

		
		throw new RuntimeException("Read unknown node type: " + typeString);
	}
	
	abstract public int getType();
	abstract public String getTypeName();
	
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
		return label.get();
	}
	
	public String getShortLabel()
	{
		return "";
	}
	
	public String getFullName()
	{
		return combineShortLabelAndLabel(getShortLabel(), getLabel());
	}
	
	protected String toFullNameWithCode(String codeToUse)
	{
		String result = "";
		if(codeToUse.length() > 0)
			result += codeToUse + ": ";
		
		result += toString();
		
		return result;
	}
	
	protected String toString(String defaultValue)
	{
		String result = getLabel();
		if(result.length() > 0)
			return result;
		
		return defaultValue;
	}
	
	public void setLabel(String newLabel) throws Exception
	{
		label.set(newLabel);
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
	
	public String getData(String fieldTag)
	{
		if(TAG_ID.equals(fieldTag))
			return id.toString();
		
		if (TAG_EMPTY.equals(fieldTag))
			return "";
		
		if(!doesFieldExist(fieldTag))
			throw new RuntimeException("Attempted to get data for bad field: " + fieldTag + " in object type: " + getClass().getSimpleName());

		return getField(fieldTag).get();
	}

	public boolean doesFieldExist(String fieldTag)
	{
		return fields.containsKey(fieldTag);
	}
	
	public boolean isEmpty()
	{
		Vector<String> fieldTags = getStoredFieldTags();
		for (int index = 0; index < fieldTags.size(); ++index)
		{
			String tag = fieldTags.get(index);
			if (getNonClearedFieldTags().contains(tag))
				continue;
			
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
		TimePeriodCostsMap expenseAssignmentsTimePeriodCostsMap = getTimePeriodCostsMap(TAG_EXPENSE_ASSIGNMENT_REFS);
		TimePeriodCostsMap resourceAssignmentsTimePeriodCostsMap = getResourceAssignmentsTimePeriodCostsMap();
		
		TimePeriodCostsMap mergedTimePeriodCostsMap = new TimePeriodCostsMap();
		mergedTimePeriodCostsMap.mergeAll(expenseAssignmentsTimePeriodCostsMap);
		mergedTimePeriodCostsMap.mergeAll(resourceAssignmentsTimePeriodCostsMap);
		
		return mergedTimePeriodCostsMap;
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
	
	private String getWhenTotalAsString()
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

	void clear()
	{
		label = new StringData(TAG_LABEL);
		resourceAssignmentIds = new IdListData(TAG_RESOURCE_ASSIGNMENT_IDS, ResourceAssignment.getObjectType());
		expenseAssignmentRefs = new ORefListData(TAG_EXPENSE_ASSIGNMENT_REFS);
		progressReportRefs = new ORefListData(TAG_PROGRESS_REPORT_REFS);
		whenTotal = new PseudoStringData(PSEUDO_TAG_WHEN_TOTAL);
		 
		latestProgressReport = new PseudoQuestionData(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, createSet(TAG_PROGRESS_REPORT_REFS), new ProgressReportLongStatusQuestion());
		latestProgressReportDetails = new PseudoStringData(PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS);

		fields = new HashMap();
		presentationDataFields = new HashSet();
		nonClearedFieldTags = new Vector();
		addField(TAG_LABEL, label);
		addField(TAG_RESOURCE_ASSIGNMENT_IDS, resourceAssignmentIds);
		addField(TAG_EXPENSE_ASSIGNMENT_REFS, expenseAssignmentRefs);
		addField(TAG_PROGRESS_REPORT_REFS, progressReportRefs);
		
		addField(PSEUDO_TAG_WHEN_TOTAL, whenTotal);
		addField(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, latestProgressReport);
		addField(PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS, latestProgressReportDetails);
	}
	
	public static HashSet<String> createSet(String parentTagToUse)
	{
		HashSet<String> singleItemSet = new HashSet<String>();
		singleItemSet.add(parentTagToUse);
		
		return singleItemSet;
	}
	
	protected ChoiceQuestion getQuestion(Class questionClass)
	{
		return getProject().getQuestion(questionClass);
	}
	
	protected void addField(ObjectData data)
	{
		addField(data.getTag(), data);
	}

	protected void addField(String tag, ObjectData data)
	{
		if(!data.getTag().equals(tag))
			throw new RuntimeException("Wrong tag: " + tag + " in " + data.getTag() + " for " + getRef());
		fields.put(tag, data);
	}
	
	protected void addPresentationDataField(String tag, ObjectData data)
	{
		addField(tag, data);
		presentationDataFields.add(tag);
	}
	
	protected void addNoClearField(String tag, ObjectData data)
	{
		nonClearedFieldTags.add(tag);
		fields.put(tag, data);
	}
	
	public boolean isPresentationDataField(String tag)
	{
		return presentationDataFields.contains(tag);
	}
	
	public String[] getFieldTags()
	{
		return fields.keySet().toArray(new String[0]);
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
		ObjectData data = fields.get(fieldTag);
		return data;
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return null;
	}
	
	public Collection<CommandSetObjectData> createCommandsToClearAsList()
	{
		return Arrays.asList(createCommandsToClear());
	}
	
	public Vector<Command> createCommandsToDeleteChildren() throws Exception
	{
		return new Vector<Command>();
	}
	
	protected Vector<Command> createCommandsToDeleteBudgetChildren() throws Exception
	{
		Vector<Command> commandToDeleteChildren = new Vector<Command>();
		commandToDeleteChildren.addAll(creatCommandsToDeleteRefs(TAG_EXPENSE_ASSIGNMENT_REFS));
		commandToDeleteChildren.addAll(creatCommandsToDeleteRefs(TAG_RESOURCE_ASSIGNMENT_IDS));
		
		return commandToDeleteChildren;
	}

	private Vector<Command> creatCommandsToDeleteRefs(String tag) throws Exception
	{
		ORefList refsToDelete = getRefList(tag);
		Vector<Command> commandsToDeleteRefList = createDeleteCommands(refsToDelete);
		commandsToDeleteRefList.add(new CommandSetObjectData(this, tag, ""));
		
		return commandsToDeleteRefList;
	}

	private Vector<Command> createDeleteCommands(ORefList refs)
	{
		Vector<Command> deleteCommands = new Vector<Command>();
		for (int index = 0; index < refs.size(); ++index)
		{
			BaseObject objectToDelete = BaseObject.find(getProject(), refs.get(index));
			deleteCommands.addAll(objectToDelete.createCommandsToClearAsList());
			deleteCommands.add(new CommandDeleteObject(objectToDelete));
		}
		
		return deleteCommands;
	}
	
	public CommandSetObjectData[] createCommandsToClear()
	{
		Vector commands = new Vector();
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			if (nonClearedFieldTags.contains(tag))
				continue;
			if(isPseudoField(tag))
				continue;

			commands.add(new CommandSetObjectData(getType(), getId(), tag, ""));
		}
		return (CommandSetObjectData[])commands.toArray(new CommandSetObjectData[0]);
	}
	
	
	//Note this method does not clone referenced or owned objects
	public CommandSetObjectData[] createCommandsToClone(BaseId baseId)
	{
		Vector commands = new Vector();
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			if (nonClearedFieldTags.contains(tag))
				continue;
			if(isPseudoField(tag))
				continue;
			if(isIdListField(tag))
				continue;
			if(isRefList(tag))
				continue;
			
			commands.add(new CommandSetObjectData(getType(), baseId, tag, getData(tag)));
		}
		return (CommandSetObjectData[])commands.toArray(new CommandSetObjectData[0]);
	}

	private boolean isIdListField(String tag)
	{
		return tag.indexOf("_IDS")>0;
	}
	
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_TIME_STAMP_MODIFIED, Long.toString(new Date().getTime()));
		json.put(TAG_ID, id.asInt());
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
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

	public Vector<String> getNonClearedFieldTags()
	{
		return nonClearedFieldTags;
	}

	public Factor[] getUpstreamDownstreamFactors()
	{
		Factor owner = getDirectOrIndirectOwningFactor();
		if(owner == null)
			return new Factor[0];
		
		ProjectChainObject chainObject = getProjectChainBuilder();
		return chainObject.buildUpstreamDownstreamChainAndGetFactors(owner).toFactorArray();
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
		int[] objectTypes = getTypesThatCanOwnUs(getType());
		for (int i=0; i<objectTypes.length; ++i)
		{
			ORef ownerRef = findObjectWhoOwnsUs(objectManager, objectTypes[i], getRef());
			if (ownerRef.isValid())
			{
				return ownerRef;
			}
		}
		
		return ORef.INVALID;
	}

	static public ORef findObjectWhoOwnsUs(ObjectManager objectManager, int objectType, ORef oref)
	{
		ORefList orefsInPool = objectManager.getPool(objectType).getORefList();
		for (int i=0; i<orefsInPool.size(); ++i)
		{
			BaseObject objectInPool = objectManager.findObject(orefsInPool.get(i));
			ORefList children = objectInPool.getOwnedObjects(oref.getObjectType());
			for (int childIdx=0; childIdx<children.size(); ++childIdx)
			{
				if (children.get(childIdx).getObjectId().equals(oref.getObjectId()))
					return objectInPool.getRef();
			}
		}
		return ORef.INVALID;
	}

	public ORefList findObjectsThatReferToUs()
	{
		return new ORefList(getObjectManager().getReferringObjects(getRef()));
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
		for(ObjectData field : fields.values())
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
				list.addAll(getProgressReportRefs());
				break;
		}
		
		return list;
	}
	
	public IdList getResourceAssignmentIdList()
	{
		return resourceAssignmentIds.getIdList().createClone();
	}
	
	public ORefList getResourceAssignmentRefs()
	{
		return new ORefList(ResourceAssignment.getObjectType(), getResourceAssignmentIdList());
	}
	
	public ORefList getExpenseAssignmentRefs()
	{
		return expenseAssignmentRefs.getRefList();
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
	
	//FIXME medium: This method is not contain all objects and is not reliable
	static public int[] getTypesThatCanOwnUs(int type)
	{
		Vector<Integer> objectTypes = new Vector<Integer>();
		if (RatingCriterion.canOwnThisType(type))
			objectTypes.add(RatingCriterion.getObjectType());

		if (ValueOption.canOwnThisType(type))
			objectTypes.add(ValueOption.getObjectType());

		if (Cause.canOwnThisType(type))
			objectTypes.add(Cause.getObjectType());
		
		if (Strategy.canOwnThisType(type))
			objectTypes.add(Strategy.getObjectType());
		
		if (Target.canOwnThisType(type))
			objectTypes.add(Target.getObjectType());
		
		if (HumanWelfareTarget.canOwnThisType(type))
			objectTypes.add(HumanWelfareTarget.getObjectType());
		
		if (ViewData.canOwnThisType(type))
			objectTypes.add(ViewData.getObjectType());

		if (FactorLink.canOwnThisType(type))
			objectTypes.add(FactorLink.getObjectType());

		if (ProjectResource.canOwnThisType(type))
			objectTypes.add(ProjectResource.getObjectType());

		if (Indicator.canOwnThisType(type))
			objectTypes.add(Indicator.getObjectType());

		if (Objective.canOwnThisType(type))
			objectTypes.add(Objective.getObjectType());

		if (Goal.canOwnThisType(type))
			objectTypes.add(Goal.getObjectType());

		if (ProjectMetadata.canOwnThisType(type))
			objectTypes.add(ProjectMetadata.getObjectType());
		
		if (DiagramLink.canOwnThisType(type))
			objectTypes.add(DiagramLink.getObjectType());

		if (ResourceAssignment.canOwnThisType(type))
			objectTypes.add(ResourceAssignment.getObjectType());

		if (AccountingCode.canOwnThisType(type))
			objectTypes.add(AccountingCode.getObjectType());
		
		if (FundingSource.canOwnThisType(type))
			objectTypes.add(FundingSource.getObjectType());

		if (KeyEcologicalAttribute.canOwnThisType(type))
			objectTypes.add(KeyEcologicalAttribute.getObjectType());

		if (DiagramFactor.canOwnThisType(type))
			objectTypes.add(DiagramFactor.getObjectType());

		if (ConceptualModelDiagram.canOwnThisType(type))
			objectTypes.add(ConceptualModelDiagram.getObjectType());
		
		if (ResultsChainDiagram.canOwnThisType(type))
			objectTypes.add(ResultsChainDiagram.getObjectType());

		if (IntermediateResult.canOwnThisType(type))
			objectTypes.add(IntermediateResult.getObjectType());
		
		if (ThreatReductionResult.canOwnThisType(type))
			objectTypes.add(ThreatReductionResult.getObjectType());
		
		if (Task.canOwnThisType(type))
			objectTypes.add(Task.getObjectType());
		
		if (objectTypes.isEmpty())
			EAM.logError("Object type:" + type + " can not be owned by anyone.");
		
		return Utility.convertToIntArray(objectTypes);
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
		return (ProgressReport) getLatestObject(getObjectManager(), getProgressReportRefs(), ProgressReport.TAG_PROGRESS_DATE);
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

	public ORefList getProgressReportRefs()
	{
		return progressReportRefs.getRefList();
	}

	public static BaseObject find(ObjectManager objectManager, ORef objectRef)
	{
		return objectManager.findObject(objectRef);
	}
	
	public static BaseObject find(Project project, ORef objectRef)
	{
		return find(project.getObjectManager(), objectRef);
	}
	
	//FIXME medium: move these classes into their own class in order to avoid dup code and inner classes
	public class PseudoQuestionData  extends ObjectData
	{
		public PseudoQuestionData(String tagToUse, ChoiceQuestion questionToUse)
		{
			this(tagToUse, new HashSet<String>(), questionToUse);
		}
		
		public PseudoQuestionData(String tagToUse, HashSet<String> dependencyTagsToUse, ChoiceQuestion questionToUse)
		{
			super(tagToUse, dependencyTagsToUse);
			
			question = questionToUse;
		}
		
		@Override
		public boolean isPseudoField()
		{
			return true;
		}
		
		@Override
		public void set(String newValue) throws Exception
		{
		}

		@Override
		public String get()
		{
			return getPseudoData(getTag());
		}
		
		//NOTE: as of 2009-06-03 this is never called
		public ChoiceItem getChoiceItem()
		{
			return question.findChoiceByCode(getPseudoData(getTag()));
		}

		@Override
		public boolean equals(Object rawOther)
		{
			if(!(rawOther instanceof PseudoQuestionData))
				return false;
			
			PseudoQuestionData other = (PseudoQuestionData)rawOther;
			return get().equals(other.get());
		}

		@Override
		public int hashCode()
		{
			return get().hashCode();
		}

		private ChoiceQuestion question;
	}
	
	
	public class PseudoStringData  extends StringData
	{

		public PseudoStringData(String tag)
		{
			super(tag);
		}

		@Override
		public boolean isPseudoField()
		{
			return true;
		}
		
		@Override
		public void set(String newValue) throws Exception
		{
			if (newValue.length()!=0)
				throw new RuntimeException("Set not allowed in a pseuod field");
		}

		@Override
		public String get()
		{
			return getPseudoData(getTag());
		}
		
		@Override
		public boolean equals(Object rawOther)
		{
			if(!(rawOther instanceof StringData))
				return false;
			
			StringData other = (StringData)rawOther;
			return get().equals(other.get());
		}

		@Override
		public int hashCode()
		{
			return get().hashCode();
		}
	}

	public class PseudoORefListData extends ORefListData
	{
		public PseudoORefListData(String tag)
		{
			super(tag);
		}

		@Override
		public boolean isPseudoField()
		{
			return true;
		}

		@Override
		public void set(String newValue) throws Exception
		{
			if (newValue.length()!=0)
				throw new RuntimeException("Set not allowed in a pseuod field");
		}

		@Override
		public String get()
		{
			return getPseudoData(getTag());
		}
		
		@Override
		public boolean equals(Object rawOther)
		{
			if(!(rawOther instanceof StringData))
				return false;

			StringData other = (StringData)rawOther;
			return get().equals(other.get());
		}

		@Override
		public int hashCode()
		{
			return get().hashCode();
		}
	}

	public static final String TAG_TIME_STAMP_MODIFIED = "TimeStampModified";
	public static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	public static final String TAG_EMPTY = "EMPTY";
	
	public static final String DEFAULT_LABEL = "";
	
	public static final String TAG_BUDGET_COST_MODE = "BudgetCostMode";
	
	public final static String PSEUDO_TAG_WHEN_TOTAL = "EffortDatesTotal";
	
	public static final String PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE = "PseudoLatestProgressReportCode";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS = "PseudoLatestProgressReportDetails";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_EXPENSE_ASSIGNMENT_REFS = "ExpenseRefs";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";

	protected BaseId id;
	protected StringData label;
	
	protected PseudoStringData whenTotal;

	protected ObjectManager objectManager;
	private HashMap<String, ObjectData> fields;
	private HashSet<String> presentationDataFields; 
	private Vector<String> nonClearedFieldTags;
	
	private PseudoQuestionData latestProgressReport;
	private PseudoStringData latestProgressReportDetails;
	protected IdListData resourceAssignmentIds;
	protected ORefListData expenseAssignmentRefs;
	protected ORefListData progressReportRefs;
}
