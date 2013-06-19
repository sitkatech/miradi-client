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

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.project.AbstractMiradiProjectSaver;
import org.miradi.project.RawProjectSaver;

public class MigrationManager
{
	public MigrationManager()
	{
	}
	
	public String migrate(String mpfAsString) throws Exception
	{
		VersionRange mpfVersionRange = RawProjectLoader.loadVersionRange(new UnicodeStringReader(mpfAsString));
		final String migrationType = getMigrationType(AbstractMiradiProjectSaver.getMiradiVersionRange(), mpfVersionRange);
		if (migrationType.equals(MIGRATION))
		{
			RawProject rawProject = RawProjectLoader.loadProject(new UnicodeStringReader(mpfAsString));

			final RawProject migratedPools = IndicatorFutureStatusDataToNewFutureStatusTypeMigration.migrate(rawProject);
			return convertToMpfString(migratedPools);
		}

		return mpfAsString;
	}
	
	public static String getMigrationType(VersionRange miradiVersionRange, VersionRange mpfVersionRange) throws Exception
	{
		if (mpfVersionRange.upperOverlaps(miradiVersionRange))
			return MIGRATION;
		
		if (mpfVersionRange.getHighVersion() < miradiVersionRange.getLowVersion())
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
