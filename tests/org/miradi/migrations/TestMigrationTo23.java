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

import org.miradi.migrations.forward.MigrationTo23;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;

public class TestMigrationTo23 extends AbstractTestMigration
{
	public TestMigrationTo23(String name)
	{
		super(name);
	}
	
	public void testProjectResourceTaxonomyClassificationsRemovedAfterReverseMigration() throws Exception
	{
		ProjectResource projectResource = getProject().createAndPopulateProjectResource();
		String taxonomyClassifications = projectResource.getData(MigrationTo23.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		assertNotNull(taxonomyClassifications);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo23.VERSION_TO));
        RawObject rawProjectResource = rawProject.findObject(projectResource.getRef());
        assertNotNull(rawProjectResource);
		assertFalse("Field should have been removed during reverse migration?", rawProjectResource.containsKey(MigrationTo23.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}
	
	public void testResourceAssignmentTaxonomyClassificationsRemovedAfterReverseMigration() throws Exception
	{
		ResourceAssignment resourceAssignment = getProject().createAndPopulateResourceAssignment();
		String taxonomyClassifications = resourceAssignment.getData(MigrationTo23.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		assertNotNull(taxonomyClassifications);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo23.VERSION_TO));
        RawObject rawResourceAssignment = rawProject.findObject(resourceAssignment.getRef());
        assertNotNull(rawResourceAssignment);
		assertFalse("Field should have been removed during reverse migration?", rawResourceAssignment.containsKey(MigrationTo23.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}
	
	public void testExpenseAssignmentTaxonomyClassificationsRemovedAfterReverseMigration() throws Exception
	{
		ExpenseAssignment expenseAssignment = getProject().createAndPopulateExpenseAssignment();
		String taxonomyClassifications = expenseAssignment.getData(MigrationTo23.TAG_TAXONOMY_CLASSIFICATION_CONTAINER);
		assertNotNull(taxonomyClassifications);

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo23.VERSION_TO));
        RawObject rawExpenseAssignment = rawProject.findObject(expenseAssignment.getRef());
        assertNotNull(rawExpenseAssignment);
		assertFalse("Field should have been removed during reverse migration?", rawExpenseAssignment.containsKey(MigrationTo23.TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
	}
	
	@Override
	protected int getFromVersion()
	{
		return MigrationTo23.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo23.VERSION_TO;
	}
}
