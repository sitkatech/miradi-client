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

package org.miradi.dialogs.resultReport;

import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.dialogs.planning.upperPanel.rebuilder.AbstractTreeRebuilder;
import org.miradi.dialogs.threatrating.upperPanel.TableModelDateComparatorWithEmptyValuesAtEnd;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ResultReport;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.ResultReportSchema;
import org.miradi.views.diagram.doers.CreateResultReportDoer;

import java.util.Comparator;

public class ResultReportTableModel extends EditableObjectRefsTableModel
{
    public ResultReportTableModel(Project projectToUse)
    {
        super(projectToUse);
    }

    @Override
    protected ORefList extractOutEditableRefs(ORefList hierarchyToSelectedRef)
    {
        BaseObject resultReportParent = CreateResultReportDoer.extractAnnotationParentCandidate(getProject(), hierarchyToSelectedRef, getObjectType());
        if (resultReportParent == null)
            return new ORefList();

        final ORefList resultReportRefList = resultReportParent.getSafeRefListData(BaseObject.TAG_RESULT_REPORT_REFS);

        final ORefList cleanedResultReportRefList = new ORefList();
        for (ORef ref : resultReportRefList)
        {
            if (ref.getObjectType() == getObjectType()) {
                cleanedResultReportRefList.add(ref);
            }
        }

        return AbstractTreeRebuilder.getSortedByFieldRefs(getProject(), cleanedResultReportRefList, ResultReport.TAG_RESULT_DATE);
    }

    @Override
    protected int getObjectType()
    {
        return ResultReportSchema.getObjectType();
    }

    @Override
    public String getUniqueTableModelIdentifier()
    {
        return "ResultReportTableModel";
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        ResultReport resultReport = getResultReportForRow(rowIndex, columnIndex);
        if (isDateColumn(columnIndex))
            return new TaglessChoiceItem(resultReport.getDateAsString());

        if (isResultStatusColumn(columnIndex))
            return resultReport.getResultStatusChoice();

        if (isDetailsColumn(columnIndex))
            return new TaglessChoiceItem(resultReport.getDetails());

        return new EmptyChoiceItem();
    }

    @Override
    public void setValueAt(Object value, int row, int column)
    {
        if (value == null)
            return;

        ORef ref = getBaseObjectForRowColumn(row, column).getRef();
        if (isDateColumn(column))
            setResultReportValue(ref, column, value.toString());

        if (isResultStatusColumn(column))
            setResultReportValue(ref, column, ((ChoiceItem)value).getCode());

        if (isDetailsColumn(column))
            setResultReportValue(ref, column, value.toString());
    }

    private void setResultReportValue(ORef ref, int column, String value)
    {
        setValueUsingCommand(ref, getColumnTag(column), value);
    }

    public boolean isDateColumn(int columnIndex)
    {
        return isColumnForTag(columnIndex, ResultReport.TAG_RESULT_DATE);
    }

    public boolean isDetailsColumn(int columnIndex)
    {
        return isColumnForTag(columnIndex, ResultReport.TAG_DETAILS);
    }

    public boolean isResultStatusColumn(int columnIndex)
    {
        return isColumnForTag(columnIndex, ResultReport.TAG_RESULT_STATUS);
    }

    private ResultReport getResultReportForRow(int rowIndex, int columnIndex)
    {
        return (ResultReport) getBaseObjectForRowColumn(rowIndex, columnIndex);
    }

    @Override
    public boolean isChoiceItemColumn(int column)
    {
        if (isResultStatusColumn(column))
            return true;

        return super.isChoiceItemColumn(column);
    }

    @Override
    public ChoiceQuestion getColumnQuestion(int column)
    {
        if (isResultStatusColumn(column))
            return StaticQuestionManager.getQuestion(ResultReportLongStatusQuestion.class);

        return super.getColumnQuestion(column);
    }

    @Override
    protected String[] getColumnTags()
    {
        return new String[]{
                ResultReport.TAG_RESULT_DATE,
                ResultReport.TAG_RESULT_STATUS,
                ResultReport.TAG_DETAILS,
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

