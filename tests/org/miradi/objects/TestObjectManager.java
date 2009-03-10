/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.objects;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.martus.util.DirectoryUtils;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.ids.KeyEcologicalAttributeId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.ViabilityModeQuestion;


public class TestObjectManager extends EAMTestCase
{
	public TestObjectManager(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		manager = project.getObjectManager();		
		db = project.getDatabase();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
	}

	public void testObjectLifecycles() throws Exception
	{
		int[] types = new int[] {
			ObjectType.RATING_CRITERION, 
			ObjectType.VALUE_OPTION,  
			ObjectType.VIEW_DATA, 
			ObjectType.PROJECT_RESOURCE,
			ObjectType.INDICATOR,
			ObjectType.OBJECTIVE,
			ObjectType.GOAL,
			ObjectType.PROJECT_METADATA,
		};
		
		for(int i = 0; i < types.length; ++i)
		{
			verifyObjectLifecycle(types[i]);
		}
		
		verifyObjectLifecycle(ObjectType.CAUSE);
		
		DiagramFactor cause = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		DiagramFactor target = project.createDiagramFactorAndAddToDiagram(ObjectType.TARGET);		
		CreateFactorLinkParameter link = new CreateFactorLinkParameter(cause.getWrappedORef(), target.getWrappedORef());
		verifyBasicObjectLifecycle(ObjectType.FACTOR_LINK, link);
	}

	public void testPseudoTagTargetViability() throws Exception
	{
		String NOT_SPECIFIED = "";
		String FAIR = "2";
		String sampleStatusCode = FAIR;

		ORef targetRef = project.createObject(ObjectType.TARGET);
		Target target = Target.find(project, targetRef);
		target.setData(Target.TAG_TARGET_STATUS, sampleStatusCode);
		
		String simple = project.getObjectData(target.getRef(), Target.PSEUDO_TAG_TARGET_VIABILITY);
		assertEquals("Didn't return simple viability?", sampleStatusCode, simple);
		
		target.setData(Target.TAG_VIABILITY_MODE, ViabilityModeQuestion.TNC_STYLE_CODE);
		String notRated = project.getObjectData(target.getRef(), Target.PSEUDO_TAG_TARGET_VIABILITY);
		assertEquals("Didn't return detailed viability?", NOT_SPECIFIED, notRated);
		
		Indicator condition1Indicator = createIndicator(FAIR);
		KeyEcologicalAttribute conditionKea = createKEA(new Indicator[] {condition1Indicator});

		IdList keas = new IdList(KeyEcologicalAttribute.getObjectType());
		keas.add(conditionKea.id);
		target.setData(Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keas.toString());

		String keaWithoutCategory = project.getObjectData(target.getRef(), Target.PSEUDO_TAG_TARGET_VIABILITY);
		assertEquals("Included uncategorized KEA?", NOT_SPECIFIED, keaWithoutCategory);

		conditionKea.setData(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, KeyEcologicalAttributeTypeQuestion.CONDITION);
		String fair = project.getObjectData(target.getRef(), Target.PSEUDO_TAG_TARGET_VIABILITY);
		assertEquals("Didn't compute for one kea one indicator?", FAIR, fair);
		
	}

	private Indicator createIndicator(String status) throws Exception
	{
		ORef indicatorRef = project.createObject(Indicator.getObjectType());
		ORef measurementRef = project.createObject(Measurement.getObjectType());
		project.setObjectData(measurementRef, Measurement.TAG_STATUS, status);
		ORefList measurementRefs = new ORefList();
		measurementRefs.add(measurementRef);
		
		project.setObjectData(indicatorRef, Indicator.TAG_MEASUREMENT_REFS, measurementRefs.toString());
		return (Indicator)project.findObject(indicatorRef);
	}

	private KeyEcologicalAttribute createKEA(Indicator[] indicators) throws Exception
	{
		KeyEcologicalAttributeId keaId1 = (KeyEcologicalAttributeId)project.createObjectAndReturnId(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE);
		KeyEcologicalAttribute kea = (KeyEcologicalAttribute)project.findObject(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, keaId1);

		IdList indicatorIds = new IdList(Indicator.getObjectType());
		for(int i = 0; i < indicators.length; ++i)
			indicatorIds.add(indicators[i].getId());
		kea.setData(KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorIds.toString());
		
		return kea;
	}
	
	public void testComputeTNCViabilityOfKEA() throws Exception
	{
		String FAIR = "2";
		String GOOD = "3";
		String VERY_GOOD = "4";

		Indicator fair = createIndicator(FAIR);
		Indicator veryGood = createIndicator(VERY_GOOD);
		KeyEcologicalAttribute kea = createKEA(new Indicator[] {fair, veryGood});
		assertEquals(GOOD, kea.getData(KeyEcologicalAttribute.PSEUDO_TAG_VIABILITY_STATUS));
	}
	
	private void verifyObjectLifecycle(int type) throws Exception
	{
		verifyBasicObjectLifecycle(type, null);
		verifyObjectWriteAndRead(type, null);
		verifyGetPool(type);
	}
	
	private void verifyBasicObjectLifecycle(int type, CreateObjectParameter parameter) throws Exception, IOException, ParseException
	{
		BaseId createdId = manager.createObject(type, BaseId.INVALID, parameter);
		assertNotEquals(type + " Created with invalid id", BaseId.INVALID, createdId);
		db.readObject(manager, type, createdId);
		
		String tag = RatingCriterion.TAG_LABEL;
		manager.setObjectData(type, createdId, tag, "data");
		BaseObject withData = db.readObject(manager, type, createdId);
		assertEquals(type + " didn't write/read data for " + tag + "?", "data", withData.getData(tag));
		assertEquals(type + " can't get data from project?", "data", manager.getObjectData(type, createdId, tag));
		
		manager.deleteObject(withData);
		try
		{
			manager.getObjectData(type, createdId, tag);
			fail(type + " Should have thrown getting data from deleted object");
		}
		catch(Exception ignoreExpected)
		{
		}
		
		try
		{
			db.readObject(manager, type, createdId);
			fail(type + " Should have thrown reading deleted object");
		}
		catch(Exception ignoreExpected)
		{
		}
		
		BaseId desiredId = new BaseId(2323);
		assertEquals(type + " didn't use requested id?", desiredId, manager.createObject(type, desiredId, parameter));
	}

	private void verifyObjectWriteAndRead(int type, CreateObjectParameter parameter) throws IOException, Exception
	{
		File tempDirectory = createTempDirectory();
		String projectName = "verifyObjectReadAndWrite";
		try
		{
			Project projectToWrite = new Project();
			projectToWrite.setLocalDataLocation(tempDirectory);
			projectToWrite.createOrOpen(projectName);
			BaseId idToReload = projectToWrite.createObject(type, BaseId.INVALID, parameter);
			projectToWrite.close();
			
			Project projectToRead = new Project();
			projectToRead.setLocalDataLocation(tempDirectory);
			projectToRead.createOrOpen(projectName);
			try
			{
				projectToRead.getObjectData(type, idToReload, BaseObject.TAG_LABEL);
			}
			catch (NullPointerException e)
			{
				fail("Didn't reload object from disk, type: " + type + " (did the pool get loaded?)");
			}
			projectToRead.close();
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}
	
	private void verifyGetPool(int type) throws Exception
	{
		CreateObjectParameter cop = null;
		if(type == ObjectType.FACTOR_LINK)
		{
			DiagramFactor cause1 = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
			DiagramFactor cause2 = project.createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
			cop = new CreateFactorLinkParameter(cause1.getWrappedORef(), cause2.getWrappedORef());			
		}
			
		BaseId createdId = manager.createObject(type, BaseId.INVALID, cop);
		EAMObjectPool pool = manager.getPool(type);
		assertNotNull("Missing pool type " + type, pool);
		BaseObject created = (BaseObject)pool.getRawObject(createdId);
		assertNotNull("Pool doesn't have object type " + created);
	}
	
	ProjectForTesting project;
	ObjectManager manager;
	ProjectServer db;
}
