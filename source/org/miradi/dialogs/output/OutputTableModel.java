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

package org.miradi.dialogs.output;

import org.miradi.dialogs.base.EditableObjectRefsTableModel;
import org.miradi.dialogs.planning.upperPanel.rebuilder.AbstractTreeRebuilder;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Output;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.schemas.OutputSchema;
import org.miradi.views.diagram.doers.CreateOutputDoer;

public class OutputTableModel extends EditableObjectRefsTableModel
{
    public OutputTableModel(Project projectToUse)
    {
        super(projectToUse);
    }

    @Override
    protected ORefList extractOutEditableRefs(ORefList hierarchyToSelectedRef)
    {
        BaseObject outputParent = CreateOutputDoer.extractAnnotationParentCandidate(getProject(), hierarchyToSelectedRef, getObjectType());
        if (outputParent == null)
            return new ORefList();

        final ORefList outputRefList = outputParent.getSafeRefListData(BaseObject.TAG_OUTPUT_REFS);

        final ORefList cleanedOutputRefList = new ORefList();
        for (ORef ref : outputRefList)
        {
            if (ref.getObjectType() == getObjectType()) {
                cleanedOutputRefList.add(ref);
            }
        }

        return AbstractTreeRebuilder.getSortedByFieldRefs(getProject(), cleanedOutputRefList, Output.TAG_LABEL);
    }

    @Override
    protected int getObjectType()
    {
        return OutputSchema.getObjectType();
    }

    @Override
    public String getUniqueTableModelIdentifier()
    {
        return "OutputTableModel";
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        Output output = getOutputForRow(rowIndex, columnIndex);

        if (isDetailsColumn(columnIndex))
            return new TaglessChoiceItem(output.getDetails());

        return new EmptyChoiceItem();
    }

    @Override
    public void setValueAt(Object value, int row, int column)
    {
        if (value == null)
            return;

        ORef ref = getBaseObjectForRowColumn(row, column).getRef();

        if (isDetailsColumn(column))
            setOutputValue(ref, column, value.toString());
    }

    private void setOutputValue(ORef ref, int column, String value)
    {
        setValueUsingCommand(ref, getColumnTag(column), value);
    }

    public boolean isDetailsColumn(int columnIndex)
    {
        return isColumnForTag(columnIndex, Factor.TAG_TEXT);
    }

    private Output getOutputForRow(int rowIndex, int columnIndex)
    {
        return (Output) getBaseObjectForRowColumn(rowIndex, columnIndex);
    }

    @Override
    protected String[] getColumnTags()
    {
        return new String[]{
            Factor.TAG_SHORT_LABEL,
            Factor.TAG_LABEL,
            Factor.TAG_TEXT,
        };
    }
}

