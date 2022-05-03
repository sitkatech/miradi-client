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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo44;
import org.miradi.migrations.forward.MigrationTo45;
import org.miradi.objects.*;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationTo45 extends AbstractTestMigration
{
	public TestMigrationTo45(String name)
	{
		super(name);
	}
	
	public void testConceptualModelTaxonomyClassificationsRemovedAfterReverseMigration() throws Exception
	{
		ConceptualModelDiagram conceptualModelDiagram = ConceptualModelDiagram.find(getProject(), getProject().createConceptualModelDiagram());

		String taxonomyClassifications = conceptualModelDiagram.getData(MigrationTo45.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		assertNotNull(taxonomyClassifications);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo45.VERSION_TO));
        RawObject rawConceptualModelDiagram = rawProject.findObject(conceptualModelDiagram.getRef());
        assertNotNull(rawConceptualModelDiagram);
		assertFalse("Field should have been removed during reverse migration?", rawConceptualModelDiagram.containsKey(MigrationTo45.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}
	
	public void testBiophysicalResultTaxonomyClassificationsRemovedAfterReverseMigration() throws Exception
	{
		BiophysicalResult biophysicalResult = getProject().createBiophysicalResult();

		String taxonomyClassifications = biophysicalResult.getData(MigrationTo45.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		assertNotNull(taxonomyClassifications);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo45.VERSION_TO));
        RawObject rawBiophysicalResult = rawProject.findObject(biophysicalResult.getRef());
        assertNotNull(rawBiophysicalResult);
		assertFalse("Field should have been removed during reverse migration?", rawBiophysicalResult.containsKey(MigrationTo45.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}
	
	public void testMethodTaxonomyClassificationsRemovedAfterReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createAndPopulateStrategy();
		Indicator indicator = getProject().createIndicator(strategy);
		Method method = getProject().createAndPopulateMethod(indicator, "Method to Migrate");

		String taxonomyClassifications = method.getData(MigrationTo45.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		assertNotNull(taxonomyClassifications);

		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
		final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
		migrateProject(projectToMigrate, new VersionRange(MigrationTo44.VERSION_TO));

        RawObject rawMethod = projectToMigrate.findObject(method.getRef());
        assertNotNull(rawMethod);
		assertFalse("Field should have been removed during reverse migration?", rawMethod.containsKey(MigrationTo45.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}
	
	@Override
	protected int getFromVersion()
	{
		return MigrationTo45.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo45.VERSION_TO;
	}
}
