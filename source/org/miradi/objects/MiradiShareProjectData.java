/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
import org.miradi.schemas.MiradiShareProjectDataSchema;

public class MiradiShareProjectData extends BaseObject
{
	public MiradiShareProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema(objectManager));
	}

	public static MiradiShareProjectDataSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static MiradiShareProjectDataSchema createSchema(ObjectManager objectManager)
	{
		return (MiradiShareProjectDataSchema) objectManager.getSchemas().get(ObjectType.MIRADI_SHARE_PROJECT_DATA);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}

	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}

	public static boolean is(BaseObject object)
	{
		return is(object.getRef());
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}

	public static boolean is(final int otherObjectType)
	{
		return MiradiShareProjectDataSchema.getObjectType() == otherObjectType;
	}

	public static MiradiShareProjectData find(ObjectManager objectManager, ORef miradiShareProjectDataRef)
	{
		return (MiradiShareProjectData) objectManager.findObject(miradiShareProjectDataRef);
	}
	
	public static MiradiShareProjectData find(Project project, ORef miradiShareProjectDataRef)
	{
		return find(project.getObjectManager(), miradiShareProjectDataRef);
	}

	public static final String TAG_PROJECT_ID = "ProjectId";
	public static final String TAG_PROJECT_URL = "ProjectUrl";
	public static final String TAG_PROGRAM_ID = "ProgramId";
	public static final String TAG_PROGRAM_NAME = "ProgramName";
	public static final String TAG_PROGRAM_URL = "ProgramUrl";
	public static final String TAG_PROJECT_TEMPLATE_ID = "ProjectTemplateId";
	public static final String TAG_PROJECT_TEMPLATE_NAME = "ProjectTemplateName";
	public static final String TAG_PROJECT_VERSION = "ProjectVersion";
	public static final String TAG_PROGRAM_TAXONOMY_SET_NAME = "ProgramTaxonomySetName";
	public static final String TAG_PROGRAM_TAXONOMY_SET_VERSION_ID = "ProgramTaxonomySetVersionId";
	public static final String TAG_PROGRAM_TAXONOMY_SET_VERSION = "ProgramTaxonomySetVersion";
	public static final String TAG_EXTRA_DATA = "ExtraData";

	public static final String MIRADI_SHARE_PROJECT_CODE = "MiradiShare";
}
