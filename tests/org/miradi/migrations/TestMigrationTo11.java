/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import org.miradi.ids.BaseId;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.migrations.forward.MigrationTo11;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TncProjectData;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.InternalQuestionWithoutValues;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.utils.CodeList;

public class TestMigrationTo11 extends AbstractTestMigration
{
	public TestMigrationTo11(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		ORef tncProjectDataRef = getProject().getTncProjectDataRef();
		getProject().deleteObject(TncProjectData.find(getProject(), tncProjectDataRef));
		BaseId nextId = getProject().getNormalIdAssigner().takeNextId();
		LegacyTncProjectData legacyTncProjectData = new LegacyTncProjectData(getObjectManager(), nextId);
		getProject().getPool(TncProjectDataSchema.getObjectType()).put(legacyTncProjectData);
		getProject().fillObjectUsingCommand(legacyTncProjectData, MigrationTo11.LEGACY_TAG_TNC_PROJET_TYPES, new CodeList(new String[]{"randomX", }).toJsonString());
		getProject().fillObjectUsingCommand(legacyTncProjectData, MigrationTo11.LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES, new CodeList(new String[]{"randomY", }).toJsonString());
		assertTrue("field should have been removed?", legacyTncProjectData.getStoredFieldTags().contains(MigrationTo11.LEGACY_TAG_TNC_PROJET_TYPES));
		assertTrue("field should have been removed?", legacyTncProjectData.getStoredFieldTags().contains(MigrationTo11.LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES));

		String legacyProject = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationManager.OLDEST_VERSION_TO_HANDLE));
		RawProject migratedProject = migrateProjectAndReturnRawProject(legacyProject);
		RawPool tncProjectDataRawObjects = migratedProject.getRawPoolForType(TncProjectDataSchema.getObjectType());
		assertEquals("Incorrect singelton Tnc Project data count?", 1, tncProjectDataRawObjects.size());
		
		ORef migratedTncProjectDataRef = tncProjectDataRawObjects.getSortedReflist().getFirstElement();
		RawObject migratedTncProjectData = tncProjectDataRawObjects.get(migratedTncProjectDataRef);
		assertFalse("field should have been removed?", migratedTncProjectData.containsKey(MigrationTo11.LEGACY_TAG_TNC_PROJET_TYPES));
		assertFalse("field should have been removed?", migratedTncProjectData.containsKey(MigrationTo11.LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES));
	}
	
	@Override
	protected int getFromVersion()
	{
		return 10;
	}
	
	@Override
	protected int getToVersion()
	{
		return 11;
	}
	
	private class LegacyTncProjectData extends TncProjectData
	{
		public LegacyTncProjectData(ObjectManager objectManager, BaseId id)
		{
			super(objectManager, id, new LegacyTncProjectDataSchema());
		}
	}
	
	private class LegacyTncProjectDataSchema extends TncProjectDataSchema
	{
		@Override
		protected void fillFieldSchemas()
		{
			super.fillFieldSchemas();
			
			createFieldSchemaCodeList(MigrationTo11.LEGACY_TAG_TNC_PROJET_TYPES, new InternalQuestionWithoutValues());
			createFieldSchemaCodeList(MigrationTo11.LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES, new InternalQuestionWithoutValues());
		}
	}
}
