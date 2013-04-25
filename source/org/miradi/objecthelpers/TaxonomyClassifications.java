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

import java.util.Vector;

import org.json.JSONArray;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class TaxonomyClassifications
{
	public TaxonomyClassifications()
	{
		taxonomyElementCodes = new Vector<String>();
	}

	public TaxonomyClassifications(String taxonomyClassificationsAsJsonString) throws Exception
	{
		this(new EnhancedJsonObject(taxonomyClassificationsAsJsonString));
	}
	
	public TaxonomyClassifications(EnhancedJsonObject json) throws Exception
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_ELEMENTS_CODES);
		for(int index = 0; index < array.length(); ++index)
		{
			taxonomyElementCodes.add(array.getString(index));
		}
		
		code = json.optString(TAG_TAXONOMY_CLASSIFICATION_TAXONOMY_CODE);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		JSONArray array = new JSONArray();
		for(String elementCode : taxonomyElementCodes)
		{
			array.put(elementCode);
		}
		
		json.put(TAG_ELEMENTS_CODES, array);
		json.put(TAG_TAXONOMY_CLASSIFICATION_TAXONOMY_CODE, getTaxonomyClassificationCode());
		
		return json;
	}
	
	public String toJsonString()
	{
		if (taxonomyElementCodes.size() == 0)
			return "";
		
		return toJson().toString();
	}
	
	public void addElementCode(String elementCodeToAdd)
	{
		taxonomyElementCodes.add(elementCodeToAdd);
	}
	
	public void setTaxonomyClassificationCode(String codeToUse)
	{
		code = codeToUse;
	}
	
	public String getTaxonomyClassificationCode()
	{
		return code;
	}
	
	private Vector<String> taxonomyElementCodes;
	private String code;
	private String TAG_ELEMENTS_CODES = "ElementCodes";
	private String TAG_TAXONOMY_CLASSIFICATION_TAXONOMY_CODE = "TaxonomyClassificationTaxonomyCode";
}
