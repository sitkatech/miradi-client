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

import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class TaxonomyClassification
{
	public TaxonomyClassification()
	{
		taxonomyCode = "";
		taxonomyElementCodes = new Vector<String>();
	}
	
	public TaxonomyClassification(EnhancedJsonObject json) throws Exception
	{
		this();
		EnhancedJsonArray array = json.optJsonArray(TAG_ELEMENTS_CODES);
		for(int index = 0; index < array.length(); ++index)
		{
			taxonomyElementCodes.add(array.getString(index));
		}
		
		taxonomyCode = json.optString(TAG_TAXONOMY_CLASSIFICATION_CODE);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonArray array = new EnhancedJsonArray();
		for(String elementCode : taxonomyElementCodes)
		{
			array.put(elementCode);
		}
		
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_ELEMENTS_CODES, array);
		json.put(TAG_TAXONOMY_CLASSIFICATION_CODE, getTaxonomyClassificationCode());
		
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
	
	public void addAllElementCodes(Vector<String> taxonomyClassificationElementCodes)
	{
		taxonomyElementCodes.addAll(taxonomyClassificationElementCodes);
	}
	
	public void setTaxonomyClassificationCode(String taxonomyCodeToUse)
	{
		taxonomyCode = taxonomyCodeToUse;
	}
	
	public String getTaxonomyClassificationCode()
	{
		return taxonomyCode;
	}
	
	public Vector<String> getTaxonomyElementCodes()
	{
		return new Vector<String>(taxonomyElementCodes);
	}
	
	private String taxonomyCode;
	private Vector<String> taxonomyElementCodes;
	private String TAG_ELEMENTS_CODES = "ElementCodes";
	private String TAG_TAXONOMY_CLASSIFICATION_CODE = "TaxonomyClassificationTaxonomyCode";
}
