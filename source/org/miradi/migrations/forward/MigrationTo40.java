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
import org.miradi.migrations.*;
import org.miradi.objectdata.CodeToCodeListMapData;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;

import java.util.Vector;

public class MigrationTo40 extends AbstractMigration
{
	public MigrationTo40(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return migrate();
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return MigrationResult.createSuccess();
	}

	private MigrationResult migrate() throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();

		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			final WorkPlanProjectResourceFilterCodeListVisitor visitor = new WorkPlanProjectResourceFilterCodeListVisitor(typeToVisit);
			visitAllORefsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

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
		return EAM.text("This migration removes any invalid project resources from the work plan resource filter table settings.");
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.TABLE_SETTINGS);

		return typesToMigrate;
	}

	private class WorkPlanProjectResourceFilterCodeListVisitor extends AbstractMigrationORefVisitor
	{
		public WorkPlanProjectResourceFilterCodeListVisitor(int typeToVisit)
		{
			type = typeToVisit;
			getRawProject().ensurePoolExists(ObjectType.PROJECT_RESOURCE);
			projectResourceRefs = getRawProject().getAllRefsForType(ObjectType.PROJECT_RESOURCE);
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createUninitializedResult();

			RawObject rawObject = getRawProject().findObject(rawObjectRef);
			if (rawObject != null)
			{
				migrationResult = removeInvalidProjectResourceRefs(rawObject);
			}

			return migrationResult;
		}

		private MigrationResult removeInvalidProjectResourceRefs(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			CodeToCodeListMap tableSettingsMap = getCodeToCodeListMapData(rawObject, TAG_TABLE_SETTINGS_MAP);
			if (tableSettingsMap.contains(WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY))
			{
				ORefList invalidProjectResourceRefs = new ORefList();
				ORefList filterProjectResourceRefs = tableSettingsMap.getRefList(WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);

				for (ORef filterProjectResourceRef: filterProjectResourceRefs)
				{
					if (!projectResourceRefs.contains(filterProjectResourceRef))
						invalidProjectResourceRefs.add(filterProjectResourceRef);
				}

				if (invalidProjectResourceRefs.hasRefs())
				{
					filterProjectResourceRefs.removeAll(invalidProjectResourceRefs);
					tableSettingsMap.putRefList(WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY, filterProjectResourceRefs);
					rawObject.setData(TAG_TABLE_SETTINGS_MAP, tableSettingsMap.toJsonString());
				}
			}

			return migrationResult;
		}

		private CodeToCodeListMap getCodeToCodeListMapData(RawObject rawObject, String tag) throws Exception
		{
			String rawValue = rawObject.getData(tag);
			CodeToCodeListMapData map = new CodeToCodeListMapData(tag);
			if (rawValue != null)
				map.set(rawValue);
			return map.getStringToCodeListMap();
		}

		private int type;
		private ORefList projectResourceRefs;
	}

	public static final int VERSION_FROM = 39;
	public static final int VERSION_TO = 40;

	public static final String TAG_TABLE_SETTINGS_MAP = "TagTableSettingsMap";
	public static final String WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY = "WorkPlanProjectResourceFilterCodeListKey";
}