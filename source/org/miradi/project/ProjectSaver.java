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
import java.util.Vector;

import org.martus.util.UnicodeWriter;
import org.miradi.database.ProjectServer;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectSaver
{
	public static void saveProject(final Project project, UnicodeWriter writer) throws Exception
	{
		writeTagValue(writer, UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_HIGHEST_OBJECT_ID, Integer.toString(project.getProjectInfo().getNormalIdAssigner().getHighestAssignedId()));
		writeTagValue(writer, UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_PROJECT_METADATA_ID, project.getProjectInfo().getMetadataId().toString());
		writeTagValue(writer, UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, Integer.toString(ProjectServer.DATA_VERSION));
		writeSimpleThreatRating(writer, project);
		writeAllObjectTypes(writer, project);
	}
	
	private static void writeAllObjectTypes(UnicodeWriter writer, Project project) throws Exception
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			EAMObjectPool pool = project.getPool(type);
			if (pool != null)
			{
				ORefList sortedObjectRefs = pool.getSortedRefList();
				writeObjects(writer, project, sortedObjectRefs);
			}
		}
	}

	private static void writeObjects(UnicodeWriter writer, Project project, ORefList sortedObjectRefs) throws Exception
	{
		for (int index = 0; index < sortedObjectRefs.size(); ++index)
		{
			final ORef ref = sortedObjectRefs.get(index);
			writeObject(writer, project, ref);
		}
	}

	private static void writeObject(UnicodeWriter writer, Project project, ORef ref) throws Exception
	{
		BaseObject baseObject = project.findObject(ref);
		writeTagValue(writer, CREATE_OBJECT_CODE, "Ref", ref.toString());
		Vector<String> fieldTags = baseObject.getStoredFieldTags();
		for(int field = 0; field < fieldTags.size(); ++field)
		{
			String tag = fieldTags.get(field);
			writeTagValue(writer, UPDATE_OBJECT_CODE, ref, tag, baseObject.getData(tag));
		}
	}

	private static void writeSimpleThreatRating(UnicodeWriter writer, Project project) throws Exception
	{
		Collection<ThreatRatingBundle> allBundles = SimpleThreatRatingFramework.loadSimpleThreatRatingBundles(project.getDatabase());
		for(ThreatRatingBundle bundle : allBundles)
		{
			EnhancedJsonObject json = bundle.toJson();
			String bundleName = SimpleThreatRatingFramework.getBundleKey(bundle.getThreatId(), bundle.getTargetId());
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, SIMPLE_THREAT_RATING_BUNDLE_NAME_TAG, bundleName);
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_VALUES, json.getString(ThreatRatingBundle.TAG_VALUES));
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_DEFAULT_VALUE_ID, json.getString(ThreatRatingBundle.TAG_DEFAULT_VALUE_ID));
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_THREAT_ID, json.getString(ThreatRatingBundle.TAG_THREAT_ID));
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_TARGET_ID, json.getString(ThreatRatingBundle.TAG_TARGET_ID));
		}
	}

	private static void writeTagValue(final UnicodeWriter writer, final String actionCode, final String tag, final String value) throws Exception
	{
		writer.write(actionCode);
		writer.write(TAB);
		writer.write(tag);
		writer.write(EQUALS);
		writer.write(value);
		writer.writeln();
	}
	
	private static void writeTagValue(final UnicodeWriter writer, final String actionCode, ORef ref, final String tag, final String value) throws Exception
	{
		writer.write(actionCode);
		writer.write(TAB);
		
		writer.write(REF_TAG);
		writer.write(EQUALS);
		writer.write(ref.toString());
		writer.write(TAB);
		
		writer.write(tag);
		writer.write(EQUALS);
		writer.write(value);
		
		writer.writeln();
	}
	
	private static final String TAB = "\t";
	private static final String EQUALS = "=";
	private static final String UPDATE_PROJECT_INFO_CODE = "UP";
	private static final String UPDATE_PROJECT_VERSION_CODE = "UV";
	private static final String UPDATE_SIMPLE_THREAT_RATING = "UT";
	private static final String CREATE_OBJECT_CODE = "CO";
	private static final String UPDATE_OBJECT_CODE = "UO";
	
	private static final String SIMPLE_THREAT_RATING_BUNDLE_NAME_TAG = "BundleName";
	private static final String REF_TAG = "Ref";
}
