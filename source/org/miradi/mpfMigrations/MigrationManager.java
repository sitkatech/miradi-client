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

import org.martus.util.UnicodeStringWriter;
import org.miradi.project.RawProjectSaver;

public class MigrationManager
{
	public MigrationManager()
	{
	}
	
	public String migrate(String mpfAsString) throws Exception
	{
		final RawProject migratedPools = IndicatorFutureStatusDataToNewFutureStatusTypeMigration.migrate(mpfAsString);
		
		return convertToMpfString(migratedPools);
	}
	
	public static String getMigrationType(VersionRange miradiVersionRange, VersionRange mpfVersionRange) throws Exception
	{
		if (mpfVersionRange.upperOverlaps(miradiVersionRange))
			return MIGRATION;
		
		return NO_MIGRATION;
	}
	
	private String convertToMpfString(RawProject migratedPools) throws Exception
	{
		UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
		RawProjectSaver.saveProject(migratedPools, stringWriter);
		
		return stringWriter.toString();
	}
	
	public static final String NO_MIGRATION = "NoMigration";
	public static final String MIGRATION = "Migration";
}
