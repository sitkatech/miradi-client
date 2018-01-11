/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigration;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawProject;
import org.miradi.objecthelpers.ObjectType;

public class MigrationTo36 extends AbstractMigration
{
	public MigrationTo36(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return MigrationResult.createSuccess();
	}
	
	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		getRawProject().deletePoolWithData(ObjectType.EXTENDED_PROGRESS_REPORT);

		return migrationResult;
	}

	@Override
	protected int getToVersion()
	{
		return VERSION_TO;
	}

	@Override
	protected int getFromVersion()
	{
		return VERSION_FROM;
	}
	
	@Override
	protected String getDescription()
	{
		return EAM.text("This migration handles the removal of extended progress reports.");
	}
	
	public static final int VERSION_FROM = 35;
	public static final int VERSION_TO = 36;
}
