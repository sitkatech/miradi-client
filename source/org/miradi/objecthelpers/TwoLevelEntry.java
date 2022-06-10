/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

public class TwoLevelEntry
{
	public TwoLevelEntry(String code, String labelToUse)
	{
		this(code, labelToUse, "");
	}
	
	public TwoLevelEntry(String code, String labelToUse, String descriptionToUse)
	{
		this(code, labelToUse, descriptionToUse, "", 1);
	}
	
	public TwoLevelEntry(String code, String labelToUse, String descriptionToUse, String longDescriptionToUse, int entryLevelToUse)
	{
		entryCode = code;
		entryLabel = labelToUse;
		description = descriptionToUse;
		longDescription = longDescriptionToUse;
		entryLevel = entryLevelToUse;
		parentEntryCode = "";
	}

	public TwoLevelEntry(String code, String labelToUse, String descriptionToUse, String longDescriptionToUse, int entryLevelToUse, String parentEntryCodeToUse)
	{
		entryCode = code;
		entryLabel = labelToUse;
		description = descriptionToUse;
		longDescription = longDescriptionToUse;
		entryLevel = entryLevelToUse;
		parentEntryCode = parentEntryCodeToUse;
	}

	public String getEntryCode()
	{
		return entryCode;
	}

	public String getEntryLabel()
	{
		return entryLabel;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public String getLongDescription()
	{
		return longDescription;
	}

	public int getEntryLevel()
	{
		return entryLevel;
	}

	public String getParentEntryCode()
	{
		return parentEntryCode;
	}

	@Override
	public String toString()
	{
		return getEntryLabel();
	}
	
	public boolean isSelectable() 
	{
		if(isEmptyItem())
			return true;

		if(entryCode.indexOf(".") >= 0)
			return true;
		
		return false;
	}

	private boolean isEmptyItem()
	{
		return entryCode.length()==0;
	}

	private String entryCode;
	private String entryLabel;
	private String description;
	private String longDescription;
	private int entryLevel;
	private String parentEntryCode;
}
