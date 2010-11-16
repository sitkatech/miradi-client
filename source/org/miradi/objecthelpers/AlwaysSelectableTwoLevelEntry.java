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

public class AlwaysSelectableTwoLevelEntry extends TwoLevelEntry
{
	public AlwaysSelectableTwoLevelEntry(String code, String label, String description)
	{
		super(code, label, description);
	}
	
	public AlwaysSelectableTwoLevelEntry(String code, String label)
	{
		super(code, label);
	}
	
	@Override
	public boolean isSelectable()
	{
		return true;
	}
}
