/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.project;

import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ThreatRatingBundleSorter;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class TestProjectSaver extends TestCaseWithProject
{
	public TestProjectSaver(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		getProject().populateEverything();
		getProject().populateSimpleThreatRatingValues();
	}
	
	public void testBasics() throws Exception
	{
		saveProjectToString();
	}

	public void testSaveAndLoad() throws Exception
	{
		String contents = saveProjectToString();
		assertContains("<br/>", contents);
		assertContains("&quot;", contents);

		Project project2 = new ProjectForTesting(getName());
		project2.clear();
		UnicodeStringReader reader = new UnicodeStringReader(contents);
		ProjectLoader.loadProject(reader, project2);
		
		final ProjectForTesting project = getProject();
		verifyProjectsAreTheSame(project, project2);
	}

	public static void verifyProjectsAreTheSame(final ProjectForTesting project1, final Project project2)
	{
		verifyNormalObjectsAreTheSame(project1, project2);
		verifySimpleThreatRatingsAreTheSame(project1, project2);
	}

	private static void verifyNormalObjectsAreTheSame(
			final ProjectForTesting project1, final Project project2)
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			EAMObjectPool oldPool = project1.getPool(type);
			if (oldPool == null)
				continue;
			ORefList oldRefs = oldPool.getSortedRefList();

			EAMObjectPool newPool = project2.getPool(type);
			ORefList newRefs = newPool.getSortedRefList();
			assertEquals(oldRefs, newRefs);
			verifyIdenticalObjects(project1, project2, newRefs);
		}
	}
	
	private static void verifySimpleThreatRatingsAreTheSame(ProjectForTesting project1, Project project2)
	{
		Vector<ThreatRatingBundle> bundles1 = getAllBundlesSorted(project1);
		Vector<ThreatRatingBundle> bundles2 = getAllBundlesSorted(project2);
		assertEquals(bundles1, bundles2);
	}

	private static Vector<ThreatRatingBundle> getAllBundlesSorted(Project project)
	{
		Collection<ThreatRatingBundle> rawBundles = project.getSimpleThreatRatingFramework().getAllBundles();
		Vector<ThreatRatingBundle> bundles = new Vector<ThreatRatingBundle>(rawBundles);
		Collections.sort(bundles, new ThreatRatingBundleSorter());
		return bundles;
	}

	public void testSaveAndLoadSimpleThreatRating() throws Exception
	{
		String contents = saveProjectToString();

		ProjectForTesting project2 = new ProjectForTesting(getName());
		UnicodeStringReader reader = new UnicodeStringReader(contents);
		ProjectLoader.loadProject(reader, project2);
		project2.finishOpeningAfterLoad(getName() + "2");
		String afterLoading = saveProjectToString(project2);
		assertEquals(contents, afterLoading);
	}

	private static void verifyIdenticalObjects(ProjectForTesting project, Project project2, ORefList refs)
	{
		for(int i = 0; i < refs.size(); ++i)
		{
			ORef ref = refs.get(i);
			BaseObject oldObject = BaseObject.find(project, ref);
			BaseObject newObject = BaseObject.find(project2, ref);
			EnhancedJsonObject oldJson = oldObject.toJson();
			oldJson.remove(BaseObject.TAG_TIME_STAMP_MODIFIED);
			EnhancedJsonObject newJson = newObject.toJson();
			newJson.remove(BaseObject.TAG_TIME_STAMP_MODIFIED);
			assertEquals(oldJson, newJson);
		}
	}

	private String saveProjectToString() throws Exception
	{
		return saveProjectToString(getProject());
	}

	public static String saveProjectToString(final ProjectForTesting project) throws Exception
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		ProjectSaver.saveProject(project, writer);
		writer.close();
		String result = writer.toString();
		return result;
	}
	
}
