/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeIntermediateResult;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTextBox;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeThreatReductionResult;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.FactorSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.martus.util.xml.XmlUtilities;

abstract public class BaseObject
{

	public BaseObject(ObjectManager objectManagerToUse, BaseId idToUse)
	{
		this(idToUse);
		objectManager = objectManagerToUse;
	}
	
	public BaseObject(BaseId idToUse)
	{
		setId(idToUse);
		clear();
	}
	
	BaseObject(ObjectManager objectManagerToUse, BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		this(idToUse, json);
		objectManager = objectManagerToUse;
	}
	
	BaseObject(BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		this(idToUse);
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			if (!getField(tag).isPseudoField())
				setData(tag, json.optString(tag));
		}
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
				return new Task(objectManager, idAsInt, json);
			
			case ObjectType.TEXT_BOX:
			case ObjectType.THREAT_REDUCTION_RESULT:
			case ObjectType.INTERMEDIATE_RESULT:	
			case ObjectType.CAUSE:
			case ObjectType.STRATEGY:
			case ObjectType.TARGET:
			case ObjectType.FACTOR:
			{
				String typeString = json.getString(Factor.TAG_NODE_TYPE);
			
				if(typeString.equals(FactorTypeStrategy.STRATEGY_TYPE))
					return new Strategy(objectManager, new FactorId(idAsInt), json);
				
				if(typeString.equals(FactorTypeCause.CAUSE_TYPE))
					return new Cause(objectManager, new FactorId(idAsInt), json);
				
				if(typeString.equals(FactorTypeTarget.TARGET_TYPE))
					return new Target(objectManager, new FactorId(idAsInt), json);
				
				if (typeString.equals(FactorTypeIntermediateResult.INTERMEDIATE_RESULT))
					return new IntermediateResult(objectManager, new FactorId(idAsInt), json);
				
				if (typeString.equals(FactorTypeThreatReductionResult.THREAT_REDUCTION_RESULT))
					return new ThreatReductionResult(objectManager, new FactorId(idAsInt), json);
				
				if (typeString.equals(FactorTypeTextBox.TEXT_BOX_TYPE))
					return new TextBox(objectManager, new FactorId(idAsInt), json);
				
				throw new RuntimeException("Read unknown node type: " + typeString);
			}

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
				return new DiagramFactorLink(objectManager, idAsInt, json);
				
			case ObjectType.ASSIGNMENT:
				return new Assignment(objectManager, idAsInt, json);
				
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
				
			default:
				throw new RuntimeException("Attempted to create unknown EAMObject type " + type);
		}
	}
	
	abstract public int getType();
	
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof BaseObject))
			return false;
		
		BaseObject other = (BaseObject)rawOther;
		return other.getId().equals(getId());
	}
	
	public String getLabel()
	{
		return label.get();
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
		
		if(!fields.containsKey(fieldTag))
			throw new RuntimeException("Attempted to set data for bad field: " + fieldTag);

		getField(fieldTag).set(dataValue);
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
		
		if(!fields.containsKey(fieldTag))
			throw new RuntimeException("Attempted to get data for bad field: " + fieldTag + " in object type: " + getClass().getSimpleName());

		return getField(fieldTag).get();
	}
	

	public BaseId getId()
	{
		return id;
	}
	
	private void setId(BaseId newId)
	{
		id = newId;
	}
	
	void clear()
	{
		label = new StringData();
		
		fields = new HashMap();
		noneClearedFieldTags = new Vector();
		addField(TAG_LABEL, label);

	}
	
	void addField(String tag, ObjectData data)
	{
		fields.put(tag, data);
	}
	
	
	void addNoClearField(String tag, ObjectData data)
	{
		noneClearedFieldTags.add(tag);
		fields.put(tag, data);
	}
	
	public String[] getFieldTags()
	{
		return (String[])fields.keySet().toArray(new String[0]);
	}

	ObjectData getField(String fieldTag)
	{
		ObjectData data = (ObjectData)fields.get(fieldTag);
		return data;
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		return null;
	}
	
	public CommandSetObjectData[] createCommandsToClear()
	{
		Vector commands = new Vector();
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			if (noneClearedFieldTags.contains(tag))
				continue;
			if(isPseudoField(tag))
				continue;

			commands.add(new CommandSetObjectData(getType(), getId(), tag, ""));
		}
		return (CommandSetObjectData[])commands.toArray(new CommandSetObjectData[0]);
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

	public Vector getNoneClearedFieldTags()
	{
		return noneClearedFieldTags;
	}

	
	public String getRelatedLabelsAsMultiLine(FactorSet filterSet)
	{
		Factor[] cmNodes = getRelatedFactors().toNodeArray();
		filterSet.attemptToAddAll(cmNodes);
		return getLabelsAsMultiline(filterSet);
	}
	
	public FactorSet getRelatedFactors()
	{	
		try
		{
			return objectManager.getChainManager().findAllFactorsRelatedToThisObject(getRef());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new FactorSet();
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
		
		return shortLabel + "." + Longlabel;
	}
	
	public BaseObject getOwner()
	{
		ORef oref = getOwnerRef();
		if (oref==null)
			return null;
		return objectManager.findObject(oref);
	}

	public ORef getOwnerRef()
	{
		int[] objectTypes = getTypesThatCanOwnUs(getType());
		for (int i=0; i<objectTypes.length; ++i)
		{
			ORef oref = findObjectWhoOwnesUs(objectManager, objectTypes[i], getRef());
			if (oref != null)
				return oref;
		}
		return null;
	}

	
	static public ORef findObjectWhoOwnesUs(ObjectManager objectManager, int objectType, ORef oref)
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
		return null;
	}

	public ORefList findObjectThatReferToUs()
	{
		ORefList owners = new ORefList();
		int[] objectTypes = getTypesThatCanReferToUs(getType());
		for (int i=0; i<objectTypes.length; ++i)
		{
			ORefList orefs = findObjectsThatReferToUs(objectTypes[i]);
			owners.addAll(orefs);
		}
		return owners;
	}
	
	public ORefList findObjectsThatReferToUs(int objectType)
	{
		return findObjectsThatReferToUs(objectManager, objectType, getRef());
	}
	
	
	static public ORefList findObjectsThatReferToUs(ObjectManager objectManager, int objectType, ORef oref)
	{
		ORefList matchList = new ORefList();
		ORefList orefsInPool = objectManager.getPool(objectType).getORefList();
		for (int i=0; i<orefsInPool.size(); ++i)
		{
			BaseObject objectInPool = objectManager.findObject(orefsInPool.get(i));
			ORefList children = objectInPool.getReferencedObjects(oref.getObjectType());
			for (int childIdx=0; childIdx<children.size(); ++childIdx)
			{
				if (children.get(childIdx).getObjectId().equals(oref.getObjectId()))
					matchList.add(objectInPool.getRef());
			}
		}
		return matchList;
	}
	

	public ORefList getReferencedObjects(int objectType)
	{
		return new ORefList();
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		return new ORefList();
	}
	
	static public int[] getTypesThatCanOwnUs(int type)
	{
		// TODO: get rid of static number
		int[] objectTypes = new int[300];
		int i = 0;

		if (RatingCriterion.canOwnThisType(type))
			objectTypes[i++] = RatingCriterion.getObjectType();

		if (ValueOption.canOwnThisType(type))
			objectTypes[i++] = ValueOption.getObjectType();

		if (Task.canOwnThisType(type))
			objectTypes[i++] = Task.getObjectType();

		if (Cause.canOwnThisType(type))
			objectTypes[i++] = Cause.getObjectType();
		
		if (Strategy.canOwnThisType(type))
			objectTypes[i++] = Strategy.getObjectType();
		
		if (Target.canOwnThisType(type))
			objectTypes[i++] = Target.getObjectType();
		
		if (ViewData.canOwnThisType(type))
			objectTypes[i++] = ViewData.getObjectType();

		if (FactorLink.canOwnThisType(type))
			objectTypes[i++] = FactorLink.getObjectType();

		if (ProjectResource.canOwnThisType(type))
			objectTypes[i++] = ProjectResource.getObjectType();

		if (Indicator.canOwnThisType(type))
			objectTypes[i++] = Indicator.getObjectType();

		if (Objective.canOwnThisType(type))
			objectTypes[i++] = Objective.getObjectType();

		if (Goal.canOwnThisType(type))
			objectTypes[i++] = Goal.getObjectType();

		if (ProjectMetadata.canOwnThisType(type))
			objectTypes[i++] = ProjectMetadata.getObjectType();
		
		if (DiagramFactorLink.canOwnThisType(type))
			objectTypes[i++] = DiagramFactorLink.getObjectType();

		if (Assignment.canOwnThisType(type))
			objectTypes[i++] = Assignment.getObjectType();

		if (AccountingCode.canOwnThisType(type))
			objectTypes[i++] = AccountingCode.getObjectType();
		
		if (FundingSource.canOwnThisType(type))
			objectTypes[i++] = FundingSource.getObjectType();

		if (KeyEcologicalAttribute.canOwnThisType(type))
			objectTypes[i++] = KeyEcologicalAttribute.getObjectType();

		if (DiagramFactor.canOwnThisType(type))
			objectTypes[i++] = DiagramFactor.getObjectType();

		if (ConceptualModelDiagram.canOwnThisType(type))
			objectTypes[i++] = ConceptualModelDiagram.getObjectType();
		
		if (ResultsChainDiagram.canOwnThisType(type))
			objectTypes[i++] = ResultsChainDiagram.getObjectType();

		if (IntermediateResult.canOwnThisType(type))
			objectTypes[i++] = IntermediateResult.getObjectType();
		
		if (ThreatReductionResult.canOwnThisType(type))
			objectTypes[i++] = ThreatReductionResult.getObjectType();
		
		int[] outArray = new int[i];
		System.arraycopy(objectTypes, 0, outArray, 0, i);
		return outArray;
	}

	static public int[] getTypesThatCanReferToUs(int type)
	{
		// TODO: get rid of static number
		int[] objectTypes = new int[300];
		int i = 0;

		if (RatingCriterion.canReferToThisType(type))
			objectTypes[i++] = RatingCriterion.getObjectType();

		if (ValueOption.canReferToThisType(type))
			objectTypes[i++] = ValueOption.getObjectType();

		if (Task.canReferToThisType(type))
			objectTypes[i++] = Task.getObjectType();

		if (Cause.canReferToThisType(type))
			objectTypes[i++] = Cause.getObjectType();
		
		if (Strategy.canReferToThisType(type))
			objectTypes[i++] = Strategy.getObjectType();
		
		if (Target.canReferToThisType(type))
			objectTypes[i++] = Target.getObjectType();
		
		if (ViewData.canReferToThisType(type))
			objectTypes[i++] = ViewData.getObjectType();

		if (FactorLink.canReferToThisType(type))
			objectTypes[i++] = FactorLink.getObjectType();

		if (ProjectResource.canReferToThisType(type))
			objectTypes[i++] = ProjectResource.getObjectType();

		if (Indicator.canReferToThisType(type))
			objectTypes[i++] = Indicator.getObjectType();

		if (Objective.canReferToThisType(type))
			objectTypes[i++] = Objective.getObjectType();

		if (Goal.canReferToThisType(type))
			objectTypes[i++] = Goal.getObjectType();

		if (ProjectMetadata.canReferToThisType(type))
			objectTypes[i++] = ProjectMetadata.getObjectType();
		
		if (DiagramFactorLink.canReferToThisType(type))
			objectTypes[i++] = DiagramFactorLink.getObjectType();

		if (Assignment.canReferToThisType(type))
			objectTypes[i++] = Assignment.getObjectType();

		if (AccountingCode.canReferToThisType(type))
			objectTypes[i++] = AccountingCode.getObjectType();
		
		if (FundingSource.canReferToThisType(type))
			objectTypes[i++] = FundingSource.getObjectType();

		if (KeyEcologicalAttribute.canReferToThisType(type))
			objectTypes[i++] = KeyEcologicalAttribute.getObjectType();

		if (DiagramFactor.canReferToThisType(type))
			objectTypes[i++] = DiagramFactor.getObjectType();

		if (ConceptualModelDiagram.canReferToThisType(type))
			objectTypes[i++] = ConceptualModelDiagram.getObjectType();

		if (ResultsChainDiagram.canReferToThisType(type))
			objectTypes[i++] = ResultsChainDiagram.getObjectType();
		
		int[] outArray = new int[i];
		System.arraycopy(objectTypes, 0, outArray, 0, i);
		return outArray;
	}

	public String getPseudoData(String fieldTag)
	{
		return getData(fieldTag);
	}
	
	public class PseudoQuestionData  extends ObjectData
	{
	
		public PseudoQuestionData(ChoiceQuestion questionToUse)
		{
			question = questionToUse;
		}
		
		public boolean isPseudoField()
		{
			return true;
		}
		
		public void set(String newValue) throws Exception
		{
		}

		public String get()
		{
			return  question.findChoiceByCode(getPseudoData(question.getTag())).getLabel();
		}

		public boolean equals(Object rawOther)
		{
			if(!(rawOther instanceof PseudoQuestionData))
				return false;
			
			PseudoQuestionData other = (PseudoQuestionData)rawOther;
			return get().equals(other.get());
		}

		public int hashCode()
		{
			return get().hashCode();
		}
		
		ChoiceQuestion question;
	}
	
	
	public class PseudoStringData  extends StringData
	{

		public PseudoStringData(String tag)
		{
			psuedoTag = tag;
		}

		public boolean isPseudoField()
		{
			return true;
		}
		
		public void set(String newValue) throws Exception
		{
			if (newValue.length()!=0)
				throw new RuntimeException("Set not allowed in a pseuod field");
		}

		public String get()
		{
			return getPseudoData(psuedoTag);
		}
		
		public boolean equals(Object rawOther)
		{
			if(!(rawOther instanceof StringData))
				return false;
			
			StringData other = (StringData)rawOther;
			return get().equals(other.get());
		}

		public int hashCode()
		{
			return get().hashCode();
		}
		
		String psuedoTag;
	}
	
	public static final String TAG_TIME_STAMP_MODIFIED = "TimeStampModified";
	public static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	public static final String TAG_EMPTY = "EMPTY";
	
	public static final String DEFAULT_LABEL = "";


	
	BaseId id;
	StringData label;
	protected ObjectManager objectManager;
	private HashMap fields;
	private Vector noneClearedFieldTags;
}
