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
package org.miradi.database.migrations;

import org.miradi.database.DataUpgrader;

public class MigrationsForMiradi3
{
	public static void upgradeToVersion41() throws Exception
	{
		//NOTE: Allow saving work units for more types of date units
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 41);
	}

	public static void upgradeToVersion42() throws Exception
	{
		ConvertDateRangeEffortListToDateUnitEffortList.convertToDateUnitEffortList();
		DataUpgrader.writeLocalVersion(DataUpgrader.getTopDirectory(), 42);
	}
}
