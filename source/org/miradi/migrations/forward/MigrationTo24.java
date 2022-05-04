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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.utils.Translation;

import java.util.Vector;

public class MigrationTo24 extends AbstractMigration
{
	public MigrationTo24(RawProject rawProjectToUse)
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
		Vector<Integer> typesWithAccountingClassifications = getTypesWithAccountingClassifications();
		for(Integer typeWithTaxonomy : typesWithAccountingClassifications)
		{
			final RemoveAccountingClassificationFieldVisitor visitor = new RemoveAccountingClassificationFieldVisitor(typeWithTaxonomy);
			visitAllObjectsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		getRawProject().deletePoolWithData(ObjectType.ACCOUNTING_CLASSIFICATION_ASSOCIATION);

		return migrationResult;
	}

	private Vector<Integer> getTypesWithAccountingClassifications()
	{
		Vector<Integer> typesWithAccountingClassifications = new Vector<Integer>();
		typesWithAccountingClassifications.add(ObjectType.RESOURCE_ASSIGNMENT);
		typesWithAccountingClassifications.add(ObjectType.EXPENSE_ASSIGNMENT);

		return typesWithAccountingClassifications;
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
		return EAM.text("This migration only has reverse to remove Miradi share accounting classification related objects.");
	}
	
	private class RemoveAccountingClassificationFieldVisitor extends AbstractMigrationVisitor
	{
		public RemoveAccountingClassificationFieldVisitor(int typeToUse)
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
			if (rawObject.containsKey(TAG_ACCOUNTING_CLASSIFICATION_CONTAINER))
			{
				removeField(rawObject, TAG_ACCOUNTING_CLASSIFICATION_CONTAINER);
				final String dataLossMessage = EAM.substituteSingleString(EAM.text("%s field data will be lost"), Translation.fieldLabel(rawObject.getObjectType(), TAG_ACCOUNTING_CLASSIFICATION_CONTAINER));
				return MigrationResult.createDataLoss(dataLossMessage);
			}
			
			return MigrationResult.createSuccess();
		}

		private int type;
	}

	public static final String TAG_ACCOUNTING_CLASSIFICATION_CONTAINER = "AccountingClassificationContainer";

	public static final int VERSION_FROM = 23;
	public static final int VERSION_TO = 24;
}
