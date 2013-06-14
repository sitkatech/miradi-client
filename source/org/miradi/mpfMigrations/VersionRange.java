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

package org.miradi.mpfMigrations;

public class VersionRange
{
	public VersionRange(final int lowVersionToUse, final int highVersionToUse) throws Exception
	{
		lowVersion = lowVersionToUse;
		highVersion = highVersionToUse;
		ensureLowIsLessThanHigh();
	}
	
	private void ensureLowIsLessThanHigh() throws Exception
	{
		if (getLowVersion() > getHighVersion())
			throw new Exception("Low version must be less than high version!");
	}
	
	public boolean isLessThan(VersionRange otherVersionRange)
	{
		if (getHighVersion() < otherVersionRange.getLowVersion())
			return true;
		
		return false;
	}
	
	public boolean isGreaterThan(VersionRange otherVersionRange)
	{
		if (getLowVersion() > otherVersionRange.getHighVersion())
			return true;
		
		return false;
	}
	
	public boolean upperOverlaps(VersionRange versionRange)
	{
		if (getHighVersion() > versionRange.getHighVersion() && getLowVersion() < versionRange.getHighVersion())
			return true;
			
		return false;
	}

	public int getLowVersion()
	{
		return lowVersion;
	}
	
	public int getHighVersion()
	{
		return highVersion;
	}
	
	private int lowVersion;
	private int highVersion;
}
