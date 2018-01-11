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
import org.miradi.objectdata.CodeToCodeMapData;
import org.miradi.objecthelpers.CodeToCodeMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.utils.BiDirectionalHashMap;
import org.miradi.utils.CodeList;

import java.util.HashSet;
import java.util.Vector;

public class MigrationTo20 extends AbstractMigration
{
	public MigrationTo20(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return migrate(false);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return migrate(true);
	}

	private MigrationResult migrate(boolean reverseMigration) throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();

		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			final RenameAssignmentRelatedCodeFieldsVisitor visitor = new RenameAssignmentRelatedCodeFieldsVisitor(typeToVisit, reverseMigration);
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
		return EAM.text("This migration renames the total who and total effort dates 'meta' fields on the object tree table and table setting objects, and adds a new 'meta' field for the total planned effort (timeframe) dates.");
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.OBJECT_TREE_TABLE_CONFIGURATION);
		typesToMigrate.add(ObjectType.TABLE_SETTINGS);

		return typesToMigrate;
	}

	private class RenameAssignmentRelatedCodeFieldsVisitor extends AbstractMigrationORefVisitor
	{
		public RenameAssignmentRelatedCodeFieldsVisitor(int typeToVisit, boolean reverseMigration)
		{
			type = typeToVisit;
			isReverseMigration = reverseMigration;
			oldToNewTagMap = createLegacyToNewMap();
			codeListTagsToVisit = getCodeListTagsToVisit();
			stringMapTagsToVisit = getStringMapTagsToVisit();
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
				if (isReverseMigration)
				{
					migrationResult = renameFields(rawObject, codeListTagsToVisit, stringMapTagsToVisit, oldToNewTagMap.reverseMap());
					final MigrationResult thisMigrationResult = removeFields(rawObject, codeListTagsToVisit);
					migrationResult.merge(thisMigrationResult);
				}
				else
				{
					migrationResult = renameFields(rawObject, codeListTagsToVisit, stringMapTagsToVisit, oldToNewTagMap);
					final MigrationResult thisMigrationResult = addFields(rawObject, codeListTagsToVisit);
					migrationResult.merge(thisMigrationResult);
				}
			}

			return migrationResult;
		}

		private MigrationResult renameFields(RawObject rawObject, Vector<String> codeListTagsToVisit, Vector<String> stringMapTagsToVisit, BiDirectionalHashMap oldToNewTagMapToUse) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			HashSet<String> legacyTags = oldToNewTagMapToUse.getKeys();

			for(String codeListTag : codeListTagsToVisit)
			{
				CodeList codeListToMigrate = getCodeList(rawObject, codeListTag);
				CodeList codeListMigrated = new CodeList();

				for (String code : codeListToMigrate)
				{
					if (legacyTags.contains(code))
					{
						String newTag = oldToNewTagMapToUse.getValue(code);
						codeListMigrated.add(newTag);
					}
					else
					{
						codeListMigrated.add(code);
					}
				}

				rawObject.setData(codeListTag, codeListMigrated.toJsonString());
			}

			for(String stringMapTag : stringMapTagsToVisit)
			{
				CodeToCodeMap stringMapToMigrate = getStringMap(rawObject, stringMapTag);
				for(String legacyTag : legacyTags)
				{
					if (stringMapToMigrate.contains(legacyTag))
					{
						String newTag = oldToNewTagMapToUse.getValue(legacyTag);
						String data = stringMapToMigrate.getCode(legacyTag);
						stringMapToMigrate.removeCode(legacyTag);
						stringMapToMigrate.putCode(newTag, data);
					}
				}
				rawObject.setData(stringMapTag, stringMapToMigrate.toJsonString());
			}

			return migrationResult;
		}

		private MigrationResult addFields(RawObject rawObject, Vector<String> codeListTagsToVisit) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			if (getTypeToVisit() == ObjectType.OBJECT_TREE_TABLE_CONFIGURATION)
				return migrationResult;

			String tableIdentifier = rawObject.getData(TAG_TABLE_IDENTIFIER);
			if (tableIdentifier.equals(WORK_PLAN_MULTI_TABLE_MODEL_UNIQUE_TREE_TABLE_IDENTIFIER))
			{
				for(String codeListTag : codeListTagsToVisit)
				{
					CodeList codeListToMigrate = getCodeList(rawObject, codeListTag);

					if (codeListTag.equals(TAG_COLUMN_SEQUENCE_CODES))
					{
						CodeList codeListMigrated = new CodeList();

						codeListMigrated.add(PSEUDO_TAG_TIMEFRAME_TOTAL);

						for (String code : codeListToMigrate)
						{
							codeListMigrated.add(code);
						}

						rawObject.setData(codeListTag, codeListMigrated.toJsonString());
					}
				}
			}

			return migrationResult;
		}

		private MigrationResult removeFields(RawObject rawObject, Vector<String> codeListTagsToVisit) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			for(String codeListTag : codeListTagsToVisit)
			{
				CodeList codeListToMigrate = getCodeList(rawObject, codeListTag);

				if (codeListTag.equals(TAG_COLUMN_SEQUENCE_CODES))
				{
					if (codeListToMigrate.contains(PSEUDO_TAG_TIMEFRAME_TOTAL))
						codeListToMigrate.removeCode(PSEUDO_TAG_TIMEFRAME_TOTAL);

					rawObject.setData(codeListTag, codeListToMigrate.toJsonString());
				}
			}

			return migrationResult;
		}

		private CodeList getCodeList(RawObject rawObject, String tag) throws Exception
		{
			String data = rawObject.getData(tag);
			if (data != null)
				return new CodeList(data);
			else
				return new CodeList();
		}

		private CodeToCodeMap getStringMap(RawObject rawObject, String tag) throws Exception
		{
			String rawValue = rawObject.getData(tag);
			CodeToCodeMapData codeToCodeMapData = new CodeToCodeMapData(tag);
			if (rawValue != null)
				codeToCodeMapData.set(rawObject.getData(tag));
			return codeToCodeMapData.getCodeToCodeMap();
		}

		private Vector<String> getCodeListTagsToVisit()
		{
			Vector<String> codeListTagsToVisit = new Vector<String>();
			codeListTagsToVisit.add(TAG_COL_CONFIGURATION);
			codeListTagsToVisit.add(TAG_COLUMN_SEQUENCE_CODES);

			return codeListTagsToVisit;
		}

		private Vector<String> getStringMapTagsToVisit()
		{
			Vector<String> stringMapTagsToVisit = new Vector<String>();
			stringMapTagsToVisit.add(TAG_COLUMN_WIDTHS);

			return stringMapTagsToVisit;
		}

		protected BiDirectionalHashMap createLegacyToNewMap()
		{
			BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
			oldToNewTagMap.put(LEGACY_META_ASSIGNED_WHO_TOTAL, META_ASSIGNED_WHO_TOTAL);
			oldToNewTagMap.put(LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL, PSEUDO_TAG_ASSIGNED_WHEN_TOTAL);

			return oldToNewTagMap;
		}

		private int type;
		private boolean isReverseMigration;
		private BiDirectionalHashMap oldToNewTagMap;
		private Vector<String> codeListTagsToVisit;
		private Vector<String> stringMapTagsToVisit;
	}

	public static final int VERSION_FROM = 19;
	public static final int VERSION_TO = 20;

	public static final String TAG_TABLE_IDENTIFIER = "TableIdentifier";
	public static final String WORK_PLAN_MULTI_TABLE_MODEL_UNIQUE_TREE_TABLE_IDENTIFIER = "MultiTableModelWorkPlanTreeTableModel";

	public static final String TAG_COL_CONFIGURATION = "TagColConfiguration";
	public static final String TAG_COLUMN_SEQUENCE_CODES = "ColumnSequenceCodes";
	public static final String TAG_COLUMN_WIDTHS = "ColumnWidths";

	public final static String LEGACY_META_ASSIGNED_WHO_TOTAL = "MetaWhoTotal";
	public final static String META_ASSIGNED_WHO_TOTAL = "MetaAssignedWhoTotal";

	public final static String LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL = "EffortDatesTotal";
	public final static String PSEUDO_TAG_ASSIGNED_WHEN_TOTAL = "AssignedEffortDatesTotal";

	public final static String LEGACY_READABLE_ASSIGNED_WHO_TOTAL_CODE = "WhoTotal";
	public final static String LEGACY_READABLE_ASSIGNED_WHEN_TOTAL_CODE = "WhenTotal";

	public final static String READABLE_TIMEFRAME_TOTAL_CODE = "TimeframeTotal";

	public final static String PSEUDO_TAG_TIMEFRAME_TOTAL = "TimeframeDatesTotal";
}