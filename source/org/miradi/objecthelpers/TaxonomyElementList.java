/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

package org.miradi.objecthelpers;

import java.util.Collections;
import java.util.Vector;

import org.json.JSONArray;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class TaxonomyElementList extends Vector<TaxonomyElement>
{
	public TaxonomyElementList()
	{
		super();
	}

	public TaxonomyElementList(String taxonomyElementsAsJsonString) throws Exception
	{
		this(new EnhancedJsonObject(taxonomyElementsAsJsonString));
	}
	
	public TaxonomyElementList(EnhancedJsonObject json) throws Exception
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_TAXONOMY_ELEMENT_LIST);
		for(int index = 0; index < array.length(); ++index)
		{
			add(new TaxonomyElement(array.getJson(index)));
		}
	}

	public TaxonomyElementList(TaxonomyElementList dataToUse)
	{
		super(dataToUse);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray array = new JSONArray();
		Collections.sort(this);
		for(TaxonomyElement taxonomyElement : this)
		{
			array.put(taxonomyElement.toJson());
		}
		
		json.put(TAG_TAXONOMY_ELEMENT_LIST, array);

		return json;
	}
	
	public String toJsonString()
	{
		if (isEmpty())
			return "";
		
		return toJson().toString();
	}
	
	public static final String TAG_TAXONOMY_ELEMENT_LIST = "TaxonomyElementList";
}
