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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.utils.BiDirectionalHashMap;

import java.util.HashSet;
import java.util.Vector;

public class MigrationTo19 extends AbstractMigration
{
	public MigrationTo19(RawProject rawProjectToUse)
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
			final RenameLeaderResourceFieldVisitor visitor = new RenameLeaderResourceFieldVisitor(typeToVisit, reverseMigration);
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
		return EAM.text("This migration renames the leader resource assignment field on Strategy and Task objects.");
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.STRATEGY);
		typesToMigrate.add(ObjectType.TASK);

		return typesToMigrate;
	}

	private class RenameLeaderResourceFieldVisitor extends AbstractMigrationORefVisitor
	{
		public RenameLeaderResourceFieldVisitor(int typeToVisit, boolean reverseMigration)
		{
			type = typeToVisit;
			isReverseMigration = reverseMigration;
			oldToNewTagMap = createLegacyToNewMap();
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			RawObject rawObject = getRawProject().findObject(rawObjectRef);
			if (rawObject != null)
			{
				if (isReverseMigration)
					return renameFields(rawObject, oldToNewTagMap.reverseMap());
				else
					return renameFields(rawObject, oldToNewTagMap);
			}

			return MigrationResult.createSuccess();
		}

		private MigrationResult renameFields(RawObject rawObject, BiDirectionalHashMap oldToNewTagMapToUse)
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();
			HashSet<String> legacyTags = oldToNewTagMapToUse.getKeys();
			for(String legacyTag : legacyTags)
			{
				if (rawObject.containsKey(legacyTag))
				{
					String newTag = oldToNewTagMapToUse.getValue(legacyTag);
					String data = rawObject.get(legacyTag);
					rawObject.remove(legacyTag);
					rawObject.put(newTag, data);
				}
			}

			return migrationResult;
		}

		protected BiDirectionalHashMap createLegacyToNewMap()
		{
			BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
			oldToNewTagMap.put(LEGACY_TAG_LEADER_RESOURCE, TAG_ASSIGNED_LEADER_RESOURCE);

			return oldToNewTagMap;
		}

		private int type;
		private boolean isReverseMigration;
		private BiDirectionalHashMap oldToNewTagMap;
	}

	public static final int VERSION_FROM = 18;
	public static final int VERSION_TO = 19;

	public static final String LEGACY_TAG_LEADER_RESOURCE = "LeaderResource";
	public static final String TAG_ASSIGNED_LEADER_RESOURCE = "AssignedLeaderResource";
}