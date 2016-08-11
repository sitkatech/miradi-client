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

package org.miradi.dialogs.progressReport;

import org.apache.commons.lang3.ArrayUtils;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AbstractProgressReport;
import org.miradi.objects.ExtendedProgressReport;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.schemas.ExtendedProgressReportSchema;

public class ExtendedProgressReportTableModel extends AbstractProgressReportTableModel
{
	public ExtendedProgressReportTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return "ExtendedProgressReportTableModel";
	}

	@Override
	protected int getObjectType()
	{
		return ExtendedProgressReportSchema.getObjectType();
	}

	@Override
	protected String[] getColumnTags()
	{
		String[] baseColumnTags = super.getColumnTags();
		String[] columnTags = new String[]{
			ExtendedProgressReport.TAG_NEXT_STEPS,
			ExtendedProgressReport.TAG_LESSONS_LEARNED,
		};
		return (String[]) ArrayUtils.addAll(baseColumnTags, columnTags);
	}

	@Override
	protected Object getExtendedValueAt(AbstractProgressReport progressReport, int rowIndex, int columnIndex)
	{
		ExtendedProgressReport extendedProgressReport = (ExtendedProgressReport) progressReport;

		if (isNextStepsColumn(columnIndex))
			return new TaglessChoiceItem(extendedProgressReport.getNextSteps());

		if (isLessonsLearnedColumn(columnIndex))
			return new TaglessChoiceItem(extendedProgressReport.getLessonsLearned());

		return new EmptyChoiceItem();
	}

	@Override
	protected void setExtendedValueAt(ORef ref, Object value, int row, int column)
	{
		if (isNextStepsColumn(column))
			setProgressReportValue(ref, column, value.toString());

		if (isLessonsLearnedColumn(column))
			setProgressReportValue(ref, column, value.toString());
	}

	public boolean isNextStepsColumn(int columnIndex)
	{
		return isColumnForTag(columnIndex, ExtendedProgressReport.TAG_NEXT_STEPS);
	}

	public boolean isLessonsLearnedColumn(int columnIndex)
	{
		return isColumnForTag(columnIndex, ExtendedProgressReport.TAG_LESSONS_LEARNED);
	}
}
