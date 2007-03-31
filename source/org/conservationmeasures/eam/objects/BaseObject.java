/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.martus.util.xml.XmlUtilities;

abstract public class BaseObject
{
	public BaseObject(BaseId idToUse)
	{
		setId(idToUse);
		clear();
	}
	
	public ORef getRef()
	{
		return new ORef(getType(), getId());
	}
	
	BaseObject(BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		setId(idToUse);
		clear();
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
			setData(tag, json.optString(tag));
		}
	}
	
	public static BaseObject createFromJson(int type, EnhancedJsonObject json) throws Exception
	{
		int idAsInt = json.getInt(TAG_ID);
		switch(type)
		{
			case ObjectType.RATING_CRITERION:
				return new RatingCriterion(idAsInt, json);
				
			case ObjectType.VALUE_OPTION:
				return new ValueOption(idAsInt, json);
				
			case ObjectType.TASK:
				return new Task(idAsInt, json);
			
			case ObjectType.FACTOR:
				return Factor.createFrom(idAsInt, json);

			case ObjectType.VIEW_DATA:
				return new ViewData(idAsInt, json);
				
			case ObjectType.FACTOR_LINK:
				return new FactorLink(idAsInt, json);
				
			case ObjectType.PROJECT_RESOURCE:
				return new ProjectResource(idAsInt, json);
				
			case ObjectType.INDICATOR:
				return new Indicator(idAsInt, json);
				
			case ObjectType.OBJECTIVE:
				return new Objective(idAsInt, json);
				
			case ObjectType.GOAL:
				return new Goal(idAsInt, json);
				
			case ObjectType.PROJECT_METADATA:
				return new ProjectMetadata(idAsInt, json);
				
			case ObjectType.DIAGRAM_LINK:
				return new DiagramFactorLink(idAsInt, json);
				
			case ObjectType.ASSIGNMENT:
				return new Assignment(idAsInt, json);
				
			case ObjectType.ACCOUNTING_CODE:
				return new AccountingCode(idAsInt, json);
				
			case ObjectType.FUNDING_SOURCE:
				return new FundingSource(idAsInt, json);
				
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE:
				return new KeyEcologicalAttribute(idAsInt, json);
			
			case ObjectType.DIAGRAM_FACTOR:
				return new DiagramFactor(idAsInt, json);
				
			case ObjectType.DIAGRAM_CONTENTS:
				return new DiagramContentsObject(idAsInt, json);
				
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
	
	public String getData(String fieldTag)
	{
		if(TAG_ID.equals(fieldTag))
			return id.toString();
		
		if(!fields.containsKey(fieldTag))
			throw new RuntimeException("Attempted to get data for bad field: " + fieldTag);

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
			if (!noneClearedFieldTags.contains(tag))
				commands.add(new CommandSetObjectData(getType(), getId(), tag, ""));
		}
		return (CommandSetObjectData[])commands.toArray(new CommandSetObjectData[0]);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_ID, id.asInt());
		Iterator iter = fields.keySet().iterator();
		while(iter.hasNext())
		{
			String tag = (String)iter.next();
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
	
	public String combineShortLabelAndLabel(String shortLabel, String Longlabel)
	{
		if (shortLabel.length() <= 0)
			return Longlabel;
		
		if (Longlabel.length() <= 0)
			return shortLabel;
		
		return shortLabel + "." + Longlabel;
	}
	
	//TODO: commented out until we can pass in project
//	public ORef getOwner()
//	{
//		int[] objectTypes = getTypesThatCanOwnUs(getType());
//		for (int i=0; i<objectTypes.length; ++i)
//		{
//			ORef oref = findObjectWhoOwnesUs(getProject(), objectTypes[i], getRef());
//			if (oref != null)
//				return oref;
//		}
//		return null;
//	}

	
	static public ORef findObjectWhoOwnesUs(Project project, int objectType, ORef oref)
	{
		ORefList orefsInPool = project.getPool(objectType).getORefList();
		for (int i=0; i<orefsInPool.size(); ++i)
		{
			BaseObject objectInPool = project.findObject(orefsInPool.get(i));
			ORefList children = objectInPool.getOwnedObjects(oref.getObjectType());
			for (int childIdx=0; childIdx<children.size(); ++i)
			{
				if (children.get(childIdx).getObjectId().equals(oref.getObjectId()))
					return objectInPool.getRef();
			}
		}
		return null;
	}

	//TODO: commented out until we can pass in project	
//	public ORefList findObjectThatReferToUs()
//	{
//		ORefList owners = new ORefList();
//		int[] objectTypes = getTypesThatCanReferToUS(getType());
//		for (int i=0; i<objectTypes.length; ++i)
//		{
//			ORefList orefs = findObjectThatReferToUs(objectTypes[i]);
//			owners.addAll(orefs);
//		}
//		return owners;
//	}
	
	
	static public ORefList findObjectsThatReferToUs(Project project, int objectType, ORef oref)
	{
		ORefList matchList = new ORefList();
		ORefList orefsInPool = project.getPool(objectType).getORefList();
		for (int i=0; i<orefsInPool.size(); ++i)
		{
			BaseObject objectInPool = project.findObject(orefsInPool.get(i));
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

		if (Factor.canOwnThisType(type))
			objectTypes[i++] = Factor.getObjectType();

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

		if (DiagramContentsObject.canOwnThisType(type))
			objectTypes[i++] = DiagramContentsObject.getObjectType();

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

		if (Factor.canReferToThisType(type))
			objectTypes[i++] = Factor.getObjectType();

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

		if (DiagramContentsObject.canReferToThisType(type))
			objectTypes[i++] = DiagramContentsObject.getObjectType();

		int[] outArray = new int[i];
		System.arraycopy(objectTypes, 0, outArray, 0, i);
		return outArray;
	}
	
	public static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	
	public static final String DEFAULT_LABEL = "";

	
	BaseId id;
	StringData label;

	private HashMap fields;
	private Vector noneClearedFieldTags;
}
