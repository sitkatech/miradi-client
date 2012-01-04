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
import org.miradi.objects.Target;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.HtmlUtilities;

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
		getProject().appendToQuarantineFile("Stuff in quarantine<br>More <<stuff>>");
		getProject().appendToExceptionLog("Some exceptions & more stuff<br><br>");
	}
	
	public void testBasics() throws Exception
	{
		saveProjectToString();
	}
	
	public void testLoadWithNewLines() throws Exception
	{
		Target target = getProject().createTarget();
		getProject().fillObjectUsingCommand(target, Target.TAG_COMMENTS, "has comments with <br> new line");
		String contents = saveProjectToString();
		UnicodeStringReader reader = new UnicodeStringReader(contents);
		ProjectLoader.loadProject(reader, getProject());
	}
	
	public void testLoadTooOld() throws Exception
	{
		String tooOld = AbstractMiradiProjectSaver.getBasicFileHeader() + " " + 0 + " " + 0 + "\n";
		try
		{
			ProjectLoader.loadProject(new UnicodeStringReader(tooOld), getProject());
			fail("Should have thrown for project too old");
		}
		catch(ProjectLoader.ProjectFileTooOldException ignoreExpected)
		{
		}
	}

	public void testLoadTooNew() throws Exception
	{
		final int NEWER_VERSION = AbstractMiradiProjectSaver.VERSION_HIGH + 1; 
		String tooNew = AbstractMiradiProjectSaver.getBasicFileHeader() + " " + NEWER_VERSION + " " + NEWER_VERSION + "\n";
		try
		{
			ProjectLoader.loadProject(new UnicodeStringReader(tooNew), getProject());
			fail("Should have thrown for project too new");
		}
		catch(ProjectLoader.ProjectFileTooNewException ignoreExpected)
		{
		}
	}

	public void testSaveAndLoad() throws Exception
	{
		String contents = saveProjectToString();
		assertContains(HtmlUtilities.BR_TAG, contents);
		assertContains("\"", contents);

		Project project2 = ProjectForTesting.createProjectWithDefaultObjects(getName());
		project2.clear();
		UnicodeStringReader reader = new UnicodeStringReader(contents);
		ProjectLoader.loadProject(reader, project2);
		
		final ProjectForTesting project = getProject();
		verifyProjectsAreTheSame(project, project2);
	}

	public static void verifyProjectsAreTheSame(final ProjectForTesting project1, final Project project2) throws Exception
	{
		verifyNormalObjectsAreTheSame(project1, project2);
		verifySimpleThreatRatingsAreTheSame(project1, project2);
		assertEquals(project1.getQuarantineFileContents(), project2.getQuarantineFileContents());
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

		ProjectForTesting project2 = ProjectForTesting.createProjectWithDefaultObjects(getName());
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
