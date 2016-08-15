/*
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.utils.Translation;

import java.util.Comparator;
import java.util.Vector;

public class MigrationTo39 extends AbstractMigration
{
	public MigrationTo39(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return migrate(false);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return migrate(true);
	}

	private MigrationResult migrate(boolean reverseMigration) throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();

		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			final ProjectMetadataVisitor visitor = new ProjectMetadataVisitor(typeToVisit, reverseMigration);
			visitAllORefsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
	}

	@Override
	protected int getToVersion()
	{
		return VERSION_TO;
	}

	@Override
	protected int getFromVersion()
	{
		return VERSION_FROM;
	}

	@Override
	protected String getDescription()
	{
		return EAM.text("This migration adds Progress Reports to the Project Summary.");
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.PROJECT_METADATA);

		return typesToMigrate;
	}

	private class ProjectMetadataVisitor extends AbstractMigrationORefVisitor
	{
		public ProjectMetadataVisitor(int typeToVisit, boolean reverseMigration)
		{
			type = typeToVisit;
			isReverseMigration = reverseMigration;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createUninitializedResult();

			RawObject rawObject = getRawProject().findObject(rawObjectRef);
			if (rawObject != null)
			{
				if (isReverseMigration)
					migrationResult = moveProgressReportsToProjectStatus(rawObject);
				else
					migrationResult = moveProjectStatusToProgressReports(rawObject);
			}

			return migrationResult;
		}

		private MigrationResult moveProgressReportsToProjectStatus(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			String progressReportRefsAsString = safeGetTag(rawObject, TAG_EXTENDED_PROGRESS_REPORT_REFS);
			if (!progressReportRefsAsString.isEmpty())
			{
				ORefList progressReportRefs = new ORefList(rawObject.get(TAG_EXTENDED_PROGRESS_REPORT_REFS));
				if (!progressReportRefs.isEmpty())
				{
					RawObject latestProgressReport = getLatestProgressReport(progressReportRefs);

					String latestStatus = safeGetTag(latestProgressReport, TAG_DETAILS);
					rawObject.setData(TAG_PROJECT_STATUS, latestStatus);

					String latestLessonsLearned = safeGetTag(latestProgressReport, TAG_LESSONS_LEARNED);
					rawObject.setData(TAG_TNC_LESSONS_LEARNED, latestLessonsLearned);

					String latestNextSteps = safeGetTag(latestProgressReport, TAG_NEXT_STEPS);
					rawObject.setData(TAG_NEXT_STEPS, latestNextSteps);

					for (ORef progressReportRef : progressReportRefs)
					{
						getRawProject().deleteRawObject(progressReportRef);
					}

					final String dataLossMessage = EAM.substituteSingleString(EAM.text("%s field data will be lost"), Translation.fieldLabel(rawObject.getObjectType(), TAG_EXTENDED_PROGRESS_REPORT_REFS));
					migrationResult.addDataLoss(dataLossMessage);
				}
			}

			if (rawObject.hasValue(TAG_EXTENDED_PROGRESS_REPORT_REFS))
				rawObject.remove(TAG_EXTENDED_PROGRESS_REPORT_REFS);

			return migrationResult;
		}

		private MigrationResult moveProjectStatusToProgressReports(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			String projectStatus = safeGetTag(rawObject, TAG_PROJECT_STATUS);
			String lessonsLearned = safeGetTag(rawObject, TAG_TNC_LESSONS_LEARNED);
			String nextSteps = safeGetTag(rawObject, TAG_NEXT_STEPS);

			if (!(projectStatus.isEmpty() && lessonsLearned.isEmpty() && nextSteps.isEmpty()))
			{
				ORef progressReportRef = createProgressReport();
				RawObject progressReport = getRawProject().findObject(progressReportRef);
				progressReport.setData(TAG_DETAILS, projectStatus);
				progressReport.setData(TAG_LESSONS_LEARNED, lessonsLearned);
				progressReport.setData(TAG_NEXT_STEPS, nextSteps);

				ORefList progressReportRefs = new ORefList(progressReportRef);
				rawObject.setData(TAG_EXTENDED_PROGRESS_REPORT_REFS, progressReportRefs.toJson().toString());

				rawObject.setData(TAG_PROJECT_STATUS, "");
				rawObject.setData(TAG_TNC_LESSONS_LEARNED, "");
				rawObject.setData(TAG_NEXT_STEPS, "");
			}

			if (rawObject.hasValue(TAG_PROGRESS_STATUS))
				rawObject.remove(TAG_PROGRESS_STATUS);
			if (rawObject.hasValue(TAG_TNC_LESSONS_LEARNED))
				rawObject.remove(TAG_TNC_LESSONS_LEARNED);
			if (rawObject.hasValue(TAG_NEXT_STEPS))
				rawObject.remove(TAG_NEXT_STEPS);

			return migrationResult;
		}

		private ORef createProgressReport()
		{
			getRawProject().ensurePoolExists(ObjectType.EXTENDED_PROGRESS_REPORT);
			return getRawProject().createObject(ObjectType.EXTENDED_PROGRESS_REPORT);
		}

		private String safeGetTag(RawObject rawObject, String tag)
		{
			if (rawObject.hasValue(tag))
				return rawObject.getData(tag);

			return "";
		}

		private RawObject getLatestProgressReport(ORefList progressReportRefs)
		{
			progressReportRefs.sort(new NullableDateSorter());
			ORef latestProgressReportRef = progressReportRefs.getFirstElement();

			return getRawProject().findObject(latestProgressReportRef);
		}

		private class NullableDateSorter implements Comparator<ORef>
		{
			public int compare(ORef ref1, ORef ref2)
			{
				if(ref1 == null && ref2 == null)
					return 0;
				if(ref1 == null)
					return -1;
				if(ref2 == null)
					return 1;

				BaseId id1 = ref1.getObjectId();
				BaseId id2 = ref2.getObjectId();

				RawObject rawObject1 = getRawProject().findObject(ref1);
				RawObject rawObject2 = getRawProject().findObject(ref2);

				String date1 = safeGetTag(rawObject1, TAG_PROGRESS_DATE);
				String date2 = safeGetTag(rawObject2, TAG_PROGRESS_DATE);

				if (date1.isEmpty() && date2.isEmpty())
					return id2.compareTo(id1);

				if (date1.isEmpty())
					return 1;

				if (date2.isEmpty())
					return -1;

				return date2.compareTo(date1);
			}
		}

		private int type;
		private boolean isReverseMigration;
	}

	public static final String TAG_EXTENDED_PROGRESS_REPORT_REFS = "ExtendedProgressReportRefs";

	public static final String TAG_PROJECT_STATUS = "ProjectStatus";
	public static final String TAG_TNC_LESSONS_LEARNED = "TNC.LessonsLearned";

	public static final String TAG_PROGRESS_STATUS = "ProgressStatus";
	public static final String TAG_PROGRESS_DATE = "ProgressDate";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_NEXT_STEPS = "NextSteps";
	public static final String TAG_LESSONS_LEARNED = "LessonsLearned";

	public static final int VERSION_FROM = 38;
	public static final int VERSION_TO = 39;
}