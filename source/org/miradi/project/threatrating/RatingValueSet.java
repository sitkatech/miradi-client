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
package org.miradi.project.threatrating;

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
