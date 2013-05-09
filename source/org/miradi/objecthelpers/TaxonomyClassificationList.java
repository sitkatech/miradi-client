/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.objecthelpers;

import java.util.HashMap;
import java.util.Iterator;

import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class TaxonomyClassificationList extends HashMap<String, CodeList>
{
	public TaxonomyClassificationList()
	{
		super();
	}

	public TaxonomyClassificationList(String taxonomyCodeToTaxonomyElementCodesMap) throws Exception
	{
		this(new EnhancedJsonObject(taxonomyCodeToTaxonomyElementCodesMap));
	}
	
	public TaxonomyClassificationList(EnhancedJsonObject json) throws Exception
	{
		clear();
		EnhancedJsonObject jsonArray = json.optJson(TAG_TAXONOMY_CODE_TO_TAXONOMY_ELEMENT_CODES_MAP);
		if(jsonArray == null)
			jsonArray = new EnhancedJsonObject();
		
		Iterator iterator = jsonArray.keys();
		while (iterator.hasNext())
		{
			String taxonomyCodeAsKey = (String)iterator.next();
			CodeList taxonomyElementCodes = jsonArray.getCodeList(taxonomyCodeAsKey);
			put(taxonomyCodeAsKey, taxonomyElementCodes);
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		EnhancedJsonObject jsonArray = new EnhancedJsonObject();
		
		Iterator iterator = keySet().iterator();
		while (iterator.hasNext())
		{
			String taxonomyCodeAsKey = (String) iterator.next();
			CodeList taxonomyElementCodes = get(taxonomyCodeAsKey);
			jsonArray.put(taxonomyCodeAsKey, taxonomyElementCodes.toString());
		}
		
		json.put(TAG_TAXONOMY_CODE_TO_TAXONOMY_ELEMENT_CODES_MAP, jsonArray);
		return json;
	}
	
	public String toJsonString()
	{
		if (isEmpty())
			return "";
		
		return toJson().toString();
	}
	
	private static final String TAG_TAXONOMY_CODE_TO_TAXONOMY_ELEMENT_CODES_MAP = "TaxonomyCodeToTaxonomyElementCodesMap";
}
