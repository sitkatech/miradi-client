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

package org.miradi.project;

import org.martus.util.UnicodeStringWriter;
import org.miradi.migrations.VersionRange;

public class ProjectSaverForTesting extends ProjectSaver
{
	private ProjectSaverForTesting(Project projectToUse, UnicodeStringWriter writerToUse, VersionRange versionRangeToUse) throws Exception
	{
		super(projectToUse, writerToUse);
		
		versionRange = versionRangeToUse;
	}
	
	public static String createSnapShot(Project projectToUse, VersionRange versionRangeToUse) throws Exception
	{
		UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
		saveProject(projectToUse, stringWriter, versionRangeToUse);
		
		return stringWriter.toString();
	}

	private static void saveProject(final Project projectToUse, final UnicodeStringWriter writerToUse, VersionRange versionRangeToUse) throws Exception
	{
		final ProjectSaverForTesting projectSaver = new ProjectSaverForTesting(projectToUse, writerToUse, versionRangeToUse);
		projectSaver.saveProject();
	}

	@Override
	protected int getHighVersion()
	{
		return getVersionRange().getHighVersion();
	}

	@Override
	protected int getLowVersion()
	{
		return getVersionRange().getLowVersion();
	}

	private VersionRange getVersionRange()
	{
		return versionRange;
	}
	
	private VersionRange versionRange;
}
