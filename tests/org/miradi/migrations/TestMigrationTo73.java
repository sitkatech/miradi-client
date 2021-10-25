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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo73;
import org.miradi.objects.*;

public class TestMigrationTo73 extends AbstractTestMigration
{
	public TestMigrationTo73(String name)
	{
		super(name);
	}

	public void testIntermediateResultTaxonomyClassificationsRemovedAfterReverseMigration() throws Exception
	{
		IntermediateResult intermediateResult = getProject().createIntermediateResult();

		String taxonomyClassifications = intermediateResult.getData(MigrationTo73.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		assertNotNull(taxonomyClassifications);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo73.VERSION_TO));
        RawObject rawIntermediateResult = rawProject.findObject(intermediateResult.getRef());
        assertNotNull(rawIntermediateResult);
		assertFalse("Field should have been removed during reverse migration?", rawIntermediateResult.containsKey(MigrationTo73.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo73.VERSION_FROM;
	}

	@Override
	protected int getToVersion()
	{
		return MigrationTo73.VERSION_TO;
	}
}
