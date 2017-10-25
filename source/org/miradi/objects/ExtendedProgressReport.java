/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.*;

public class ExtendedProgressReport extends AbstractProgressReport
{
	public ExtendedProgressReport(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static ExtendedProgressReportSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ExtendedProgressReportSchema createSchema(ObjectManager objectManager)
	{
		return (ExtendedProgressReportSchema) objectManager.getSchemas().get(ObjectType.EXTENDED_PROGRESS_REPORT);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			ProjectMetadataSchema.getObjectType(),
			ConceptualModelDiagramSchema.getObjectType(),
			ResultsChainDiagramSchema.getObjectType(),
		};
	}

	public String getNextSteps()
	{
		return getStringData(TAG_NEXT_STEPS);
	}

	public String getLessonsLearned()
	{
		return getStringData(TAG_LESSONS_LEARNED);
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}

	public static boolean is(int objectType)
	{
		return objectType == ExtendedProgressReportSchema.getObjectType();
	}

	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}

	public static ExtendedProgressReport find(ObjectManager objectManager, ORef progressReportRef)
	{
		return (ExtendedProgressReport) objectManager.findObject(progressReportRef);
	}

	public static ExtendedProgressReport find(Project project, ORef progressReportRef)
	{
		return find(project.getObjectManager(), progressReportRef);
	}

	public static final String TAG_NEXT_STEPS = "NextSteps";
	public static final String TAG_LESSONS_LEARNED = "LessonsLearned";
}