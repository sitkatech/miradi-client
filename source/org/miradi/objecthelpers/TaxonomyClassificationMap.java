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

import org.miradi.main.EAM;
import org.miradi.objects.TaxonomyAssociation;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class TaxonomyClassificationMap extends HashMap<String, CodeList>
{
	public TaxonomyClassificationMap()
	{
		super();
	}

	public TaxonomyClassificationMap(String taxonomyCodeToTaxonomyElementCodesMap) throws Exception
	{
		this(new EnhancedJsonObject(taxonomyCodeToTaxonomyElementCodesMap));
	}
	
	public TaxonomyClassificationMap(EnhancedJsonObject jsonToUse) throws Exception
	{
		clear();
		EnhancedJsonObject json = jsonToUse.optJson(TAG_TAXONOMY_CODE_TO_TAXONOMY_ELEMENT_CODES_MAP);
		if(json == null)
			json = new EnhancedJsonObject();
		
		Iterator iterator = json.keys();
		while (iterator.hasNext())
		{
			String taxonomyCodeAsKey = (String)iterator.next();
			CodeList taxonomyElementCodes = json.getCodeList(taxonomyCodeAsKey);
			put(taxonomyCodeAsKey, taxonomyElementCodes);
		}
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject mapAsJson = new EnhancedJsonObject();
		Iterator iterator = keySet().iterator();
		while (iterator.hasNext())
		{
			String taxonomyCodeAsKey = (String) iterator.next();
			CodeList taxonomyElementCodes = get(taxonomyCodeAsKey);
			mapAsJson.put(taxonomyCodeAsKey, taxonomyElementCodes.toString());
		}
		
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_TAXONOMY_CODE_TO_TAXONOMY_ELEMENT_CODES_MAP, mapAsJson);
		return json;
	}
	
	public String toJsonString()
	{
		if (isEmpty())
			return "";
		
		return toJson().toString();
	}
	
	public CodeList getTaxonomyElementCodes(Project projectToUse, String taxonomyAssociationCode)
	{
		try
		{
			TaxonomyAssociation taxonomyAssociation = TaxonomyHelper.findTaxonomyAssociation(projectToUse, taxonomyAssociationCode);
			if (taxonomyAssociation == null)
				return new CodeList();
			
			String taxonomyCode = taxonomyAssociation.getTaxonomyCode();
			if (containsKey(taxonomyCode))
			{
				return get(taxonomyCode);
			}
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		return new CodeList();
	}
	
	private static final String TAG_TAXONOMY_CODE_TO_TAXONOMY_ELEMENT_CODES_MAP = "TaxonomyCodeToTaxonomyElementCodesMap";
}
