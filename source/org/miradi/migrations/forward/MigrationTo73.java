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
import org.miradi.utils.Translation;

import java.util.Vector;

public class MigrationTo73 extends AbstractMigration
{
	public MigrationTo73(RawProject rawProjectToUse)
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
		Vector<Integer> typesWithTaxonomyClassifications = getTypesWithTaxonomyClassifications();
		for(Integer typeWithTaxonomy : typesWithTaxonomyClassifications)
		{
			final RemoveTaxonomyClassificationFieldVisitor visitor = new RemoveTaxonomyClassificationFieldVisitor(typeWithTaxonomy);
			visitAllObjectsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
	}

	private Vector<Integer> getTypesWithTaxonomyClassifications()
	{
		Vector<Integer> typesWithTaxonomyClassifications = new Vector<Integer>();
		typesWithTaxonomyClassifications.add(ObjectType.INTERMEDIATE_RESULT);

		return typesWithTaxonomyClassifications;
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
        return EAM.text("This migration adds a taxonomy classifications to Intermediate Results.");
	}

	private class RemoveTaxonomyClassificationFieldVisitor extends AbstractMigrationVisitor
	{
		public RemoveTaxonomyClassificationFieldVisitor(int typeToUse)
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
			if (rawObject.containsKey(TAG_TAXONOMY_CLASSIFICATION_CONTAINER))
			{
				removeField(rawObject, TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
				final String dataLossMessage = EAM.substituteSingleString(EAM.text("%s field data will be lost"), Translation.fieldLabel(rawObject.getObjectType(), TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
				return MigrationResult.createDataLoss(dataLossMessage);
			}

			return MigrationResult.createSuccess();
		}

		private int type;
	}

	public static final String TAG_TAXONOMY_CLASSIFICATION_CONTAINER = "TaxonomyClassificationContainer";

    public static final int VERSION_FROM = 72;
    public static final int VERSION_TO = 73;
}
