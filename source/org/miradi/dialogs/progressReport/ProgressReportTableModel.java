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

import java.util.Comparator;

import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.dialogs.planning.upperPanel.rebuilder.AbstractTreeRebuilder;
import org.miradi.dialogs.threatrating.upperPanel.TableModelDateComparatorWithEmptyValuesAtEnd;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProgressReport;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.ProgressReportLongStatusQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.schemas.ProgressReportSchema;
import org.miradi.views.diagram.doers.AbstractCreateProgressDoer;

public class ProgressReportTableModel extends EditableObjectRefsTableModel
{
	public ProgressReportTableModel(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public String getUniqueTableModelIdentifier()
	{
		return "ProgressReportTableModel";
	}

	@Override
	protected ORefList extractOutEditableRefs(ORefList hierarchyToSelectedRef)
	{
		BaseObject progressReportParent = AbstractCreateProgressDoer.extractProgressParentCandidate(getProject(), hierarchyToSelectedRef, getObjectType());
		if (progressReportParent == null)
			return new ORefList();
		
		final ORefList safeRefListData = progressReportParent.getSafeRefListData(BaseObject.TAG_PROGRESS_REPORT_REFS);
		return AbstractTreeRebuilder.getSortedByFieldRefs(getProject(), safeRefListData, ProgressReport.TAG_PROGRESS_DATE);
	}
	
	@Override
	protected int getObjectType()
	{
		return ProgressReportSchema.getObjectType();
	}

	public Object getValueAt(int rowIndex, int columnIndex)
	{
		ProgressReport progressReport = getProgressReportForRow(rowIndex, columnIndex);
		if (isDateColumn(columnIndex))
			return new TaglessChoiceItem(progressReport.getDateAsString());
		
		if (isProgressStatusColumn(columnIndex))
			return progressReport.getProgressStatusChoice();
		
		if (isDetailsColumn(columnIndex))
			return new TaglessChoiceItem(progressReport.getDetails()); 
			
		return new EmptyChoiceItem();
	}

	@Override
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		ORef ref = getBaseObjectForRowColumn(row, column).getRef();
		if (isDateColumn(column))
			setProgressReportValue(ref, column, value.toString());
		
		if (isProgressStatusColumn(column))
			setProgressReportValue(ref, column, ((ChoiceItem)value).getCode());
		
		if (isDetailsColumn(column))
			setProgressReportValue(ref, column, value.toString());
	}

	private void setProgressReportValue(ORef ref, int column, String value)
	{
		setValueUsingCommand(ref, getColumnTag(column), value);
	}

	public boolean isDateColumn(int columnIndex)
	{
		return isColumnForTag(columnIndex, ProgressReport.TAG_PROGRESS_DATE);
	}

	public boolean isDetailsColumn(int columnIndex)
	{
		return isColumnForTag(columnIndex, ProgressReport.TAG_DETAILS);
	}

	public boolean isProgressStatusColumn(int columnIndex)
	{
		return isColumnForTag(columnIndex, ProgressReport.TAG_PROGRESS_STATUS);
	}
	
	private ProgressReport getProgressReportForRow(int rowIndex, int columnIndex)
	{
		return (ProgressReport) getBaseObjectForRowColumn(rowIndex, columnIndex);
	}
	
	@Override
	public boolean isChoiceItemColumn(int column)
	{
		if (isProgressStatusColumn(column))
			return true;
		
		return super.isChoiceItemColumn(column);
	}
	
	@Override
	public ChoiceQuestion getColumnQuestion(int column)
	{
		if (isProgressStatusColumn(column))
			return StaticQuestionManager.getQuestion(ProgressReportLongStatusQuestion.class);
		
		return super.getColumnQuestion(column);
	}
	
	@Override
	protected String[] getColumnTags()
	{
		return new String[]{
			ProgressReport.TAG_PROGRESS_DATE, 
			ProgressReport.TAG_PROGRESS_STATUS, 
			ProgressReport.TAG_DETAILS, 
			};
	}
	
	@Override
	public Comparator<ORef> createComparator(int columnToSortOn)
	{
		if (isDateColumn(columnToSortOn))
			return new TableModelDateComparatorWithEmptyValuesAtEnd(this, columnToSortOn);
		
		return super.createComparator(columnToSortOn);
	}
}
