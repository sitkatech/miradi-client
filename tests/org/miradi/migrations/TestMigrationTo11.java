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
import org.miradi.migrations.forward.MigrationTo11;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.TncProjectData;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectForTesting;
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
		getProject().fillObjectUsingCommand(legacyTncProjectData, MigrationTo11.LEGACY_TAG_TNC_PROJET_TYPES, new CodeList(new String[]{"1", }).toJsonString());
		getProject().fillObjectUsingCommand(legacyTncProjectData, MigrationTo11.LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES, new CodeList(new String[]{"x", }).toJsonString());
		
		ProjectForTesting migratedProject = verifyFullCircleMigrations(new VersionRange(10, 11));
		ORefList tncProjectDataRefs = migratedProject.getPool(TncProjectDataSchema.getObjectType()).getORefList();
		assertEquals("Incorrect singelton Tnc Project data count?", 1, tncProjectDataRefs.size());
		
		ORef migratedTncProjectDataRef = tncProjectDataRefs.getFirstElement();
		TncProjectData tncProjectData = TncProjectData.find(migratedProject, migratedTncProjectDataRef);
		assertFalse("field should have been removed?", tncProjectData.getStoredFieldTags().contains(MigrationTo11.LEGACY_TAG_TNC_PROJET_TYPES));
		assertFalse("field should have been removed?", tncProjectData.getStoredFieldTags().contains(MigrationTo11.LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES));
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
