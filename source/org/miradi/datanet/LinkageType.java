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
package org.miradi.datanet;

public class LinkageType
{
	public LinkageType(String nameToUse, RecordType ownerTypeToUse, RecordType memberTypeToUse, String membershipToUse)
	{
		name = nameToUse;
		ownerType = ownerTypeToUse;
		memberType = memberTypeToUse;
		membershipType = membershipToUse;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getOwnerClassName()
	{
		return ownerType.getName();
	}

	public String getMemberClassName()
	{
		return memberType.getName();
	}
	
	public String getMembershipType()
	{
		return membershipType;
	}
	
	public static String CONTAINS = "Contains";
	
	private String name;
	private RecordType ownerType;
	private RecordType memberType;
	private String membershipType;
}
