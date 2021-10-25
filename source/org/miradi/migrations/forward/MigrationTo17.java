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

import java.util.Vector;

public class MigrationTo17 extends AbstractSingleTypeMigration
{
	public MigrationTo17(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected int getToVersion()
	{
		return TO_VERSION;
	}

	@Override
	protected int getFromVersion()
	{
		return FROM_VERSION;
	}

	@Override
	protected MigrationResult migrateForward() throws Exception {
		return MigrationResult.createSuccess();
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception {
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();

        Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			final BiophysicalFactorVisitor visitor = new BiophysicalFactorVisitor(typeToVisit);
			visitAllObjectsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
	}

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.BIOPHYSICAL_FACTOR);
        typesToMigrate.add(ObjectType.BIOPHYSICAL_RESULT);

        return typesToMigrate;
    }

    @Override
	protected String getDescription()
	{
		return EAM.text("This migration prevents the removal of Biophysical Factors and / or Results if reversing (to support an older file format / Miradi version).");
	}

    private class BiophysicalFactorVisitor extends AbstractMigrationVisitor
    {
        public BiophysicalFactorVisitor(int typeToUse)
        {
            type = typeToUse;
        }

        public int getTypeToVisit()
        {
            return type;
        }

        @Override
        public MigrationResult internalVisit(RawObject rawObject) throws Exception
        {
            return MigrationResult.createCannotMigrate(EAM.text("Biophysical Factors and Results are not supported in prior versions of Miradi."));
        }

        private int type;
    }

	public static final int TO_VERSION = 17;
	public static final int FROM_VERSION = 16;
}
