/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;

import org.miradi.ids.BaseId;
import org.miradi.utils.EnhancedJsonObject;

public class RatingValueSet
{
	public RatingValueSet()
	{
		map = new HashMap();
	}
	
	public RatingValueSet(EnhancedJsonObject json)
	{
		this();
		fillFrom(json);
	}
	
	public void fillFrom(String data) throws ParseException
	{
		fillFrom(new EnhancedJsonObject(data));
	}

	public void fillFrom(EnhancedJsonObject json)
	{
		Iterator iter = json.keys();
		while(iter.hasNext())
		{
			String key = (String)iter.next();
			BaseId criterionId = new BaseId(Integer.parseInt(key));
			BaseId valueId = new BaseId(json.getInt(key));
			setValueId(criterionId, valueId);
		}
	}
	
	public BaseId getValueId(BaseId criterionId, BaseId defaultValueId)
	{
		BaseId valueObjectId = getRawValueOptionId(criterionId);
		if(valueObjectId == null)
			return defaultValueId;
		return valueObjectId;
	}

	public void setValueId(BaseId criterionId, BaseId valueId)
	{
		map.put(criterionId, valueId);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject values = new EnhancedJsonObject();
		Iterator iter = map.keySet().iterator();
		while(iter.hasNext())
		{
			BaseId criterion = (BaseId)iter.next();
			values.put(criterion.toString(), getRawValueOptionId(criterion).asInt());
		}

		return values;
	}
	
	public String toString()
	{
		return toJson().toString();
	}

	private BaseId getRawValueOptionId(BaseId criterionId)
	{
		return (BaseId)map.get(criterionId);
	}
	
	HashMap map;

}
