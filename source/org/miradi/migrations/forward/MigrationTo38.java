/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ObjectType;

import java.util.HashMap;

public class MigrationTo38 extends NewlyAddedFieldsMigration
{
	public MigrationTo38(RawProject rawProjectToUse)
	{
		super(rawProjectToUse, ObjectType.CONCEPTUAL_MODEL_DIAGRAM);
	}

	@Override
	protected HashMap<String, String> createFieldsToLabelMapToModify()
	{
		HashMap<String, String> fieldsToAdd = new HashMap<String, String>();
		fieldsToAdd.put(TAG_EXTENDED_PROGRESS_REPORT_REFS, EAM.text("Conceptual Model Progress Reports"));

		return fieldsToAdd;
	}

	@Override
	public AbstractMigrationVisitor createMigrateForwardVisitor() throws Exception
	{
		return new ProgressReportVisitor(type);
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
		return EAM.text("This migration adds Progress Reports to Conceptual Model Diagrams.");
	}

	private class ProgressReportVisitor extends AbstractMigrationVisitor
	{
		private ProgressReportVisitor(int typeToVisit)
		{
			type = typeToVisit;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		protected MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createUninitializedResult();

			if (rawObject != null)
				migrationResult = addFields(rawObject);

			return migrationResult;
		}

		private MigrationResult addFields(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			rawObject.setData(TAG_EXTENDED_PROGRESS_REPORT_REFS, "");

			return migrationResult;
		}

		private int type;
	}

	public static final int VERSION_FROM = 37;
	public static final int VERSION_TO = 38;

	public static final String TAG_EXTENDED_PROGRESS_REPORT_REFS = "ExtendedProgressReportRefs";
}
