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
import org.conservationmeasures.eam.main.EAM;
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
	
	public ORefList getDirectOwners()
	{
		ORefList directOwners = new ORefList();
		int[] objectTypes = getReleventObjectTypes();
		for (int i=0; i<objectTypes.length; ++i)
		{
			ORefList orefs = findReferencesInPool(objectTypes[i]);
			directOwners.addAll(orefs);
		}
		return directOwners;
	}

	private ORefList findReferencesInPool(int objectType)
	{
		ORefList matchList = new ORefList();
		ORefList orefsInPool = getProject().getPool(objectType).getORefList();
		for (int i=0; i<orefsInPool.size(); ++i)
		{
			BaseObject objectInPool = getProject().findObject(orefsInPool.get(i));
			ORefList children = objectInPool.getDirectChildren(getType());
			for (int childIdx=0; childIdx<children.size(); ++i)
			{
				if (children.get(childIdx).getObjectId().equals(getId()))
					matchList.add(children.get(childIdx));
			}
		}
		return matchList;
	}

	private Project getProject()
	{
		return EAM.mainWindow.getProject();
	}
	
	
	public ORefList getDirectChildren(int objectType)
	{
		return new ORefList();
	}
	
	
	public int[] getReleventObjectTypes()
	{
		return new int[0];
	}
	
	
	public static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	
	public static final String DEFAULT_LABEL = "";

	BaseId id;
	StringData label;

	private HashMap fields;
	private Vector noneClearedFieldTags;
}
