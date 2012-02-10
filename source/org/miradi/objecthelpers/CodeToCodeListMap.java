/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.text.ParseException;

import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class CodeToCodeListMap extends AbstractStringToStringMap
{
	public CodeToCodeListMap()
	{
		super();
	}
	
	public CodeToCodeListMap(CodeToCodeListMap copyFrom)
	{
		super(copyFrom);
	}

	public CodeToCodeListMap(EnhancedJsonObject json)
	{
		super(json);
	}
	
	public CodeToCodeListMap(String mapAsJsonString) throws ParseException
	{
		super(mapAsJsonString);
	}
	
	public CodeList getCodeList(String key) throws ParseException
	{
		return new CodeList(get(key));
	}

	public ORefList getRefList(String key) throws ParseException
	{
		return new ORefList(get(key));
	}

	public void putCodeList(String key, CodeList codes)
	{
		rawPut(key, codes.toString());
	}

	public void putRefList(String key, ORefList refs)
	{
		rawPut(key, refs.toString());
	}

	@Override
	protected String getMapTag()
	{
		return TAG_STRING_CODELIST_MAP;
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(!(rawOther instanceof CodeToCodeListMap))
			return false;

		CodeToCodeListMap other = (CodeToCodeListMap) rawOther;
		return data.equals(other.data);
	}
	
	private static final String TAG_STRING_CODELIST_MAP = "StringCodeListMap";

}
