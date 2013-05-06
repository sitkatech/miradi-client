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

import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class TaxonomyElement
{
	public TaxonomyElement()
	{
		this("", new CodeList(), "", "");
	}
	
	public TaxonomyElement(String codeToUse, CodeList childCodesToUse, String labelToUse, String descriptionToUse)
	{
		code = codeToUse;
		childCodes = childCodesToUse;
		label = labelToUse;
		description = descriptionToUse;
	}
	
	public TaxonomyElement(EnhancedJsonObject json) throws Exception
	{
		this();
		
		code = json.optString(TAG_CODE);
		childCodes = json.optCodeList(TAG_CHILD_CODES);
		label = json.optString(TAG_LABEL);
		description = json.optString(TAG_DESCRIPTION);
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_CODE, getCode());
		json.put(TAG_CHILD_CODES, getChildCodes().toJson());
		json.put(TAG_LABEL, getLabel());
		json.put(TAG_DESCRIPTION, getDescription());
		
		return json;
	}
	
	public String toJsonString()
	{
		return toJson().toString();
	}

	public String getCode()
	{
		return code;
	}
	
	public CodeList getChildCodes()
	{
		return childCodes;
	}
	
	public String getLabel()
	{
		return label;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setCode(String codeToUse)
	{
		code = codeToUse;
	}
	
	public void setLabel(String labelToUse)
	{
		label = labelToUse;
	}
	
	public void setDescription(String descriptionToUse)
	{
		description = descriptionToUse;
	}
	
	public void setChildCodes(CodeList childCodesToUse)
	{
		childCodes = childCodesToUse;
	}
	
	private String code;
	private CodeList childCodes;
	private String label;
	private String description;
	
	private static final String TAG_CODE = "Code";
	private static final String TAG_CHILD_CODES = "ChildCodes";
	private static final String TAG_LABEL = "Label";
	private static final String TAG_DESCRIPTION = "Description";
}
