/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Output;
import org.miradi.project.Project;
import org.miradi.questions.ObjectiveChoiceQuestion;
import org.miradi.schemas.OutputSchema;

public class OutputObjectiveRelevancyOverrideListField extends RefListEditorField
{
    public OutputObjectiveRelevancyOverrideListField(Project projectToUse, BaseId objectIdToUse)
    {
        super(projectToUse, OutputSchema.getObjectType(), objectIdToUse, Output.TAG_OBJECTIVE_IDS, new ObjectiveChoiceQuestion(projectToUse));
    }

    @Override
    public String getText()
    {
        try
        {
            Output output = Output.find(getProject(), getORef());
            ORefList all = new ORefList(refListEditor.getText());
            return output.getCalculatedRelevantObjectiveOverrides(all).toString();
        }
        catch(Exception e)
        {
            EAM.alertUserOfNonFatalException(e);
        }

        return "";
    }

    @Override
    public void setText(String codes)
    {
        try
        {
            Output output = Output.find(getProject(), getORef());
            ORefList relevantRefList = output.getRelevantObjectiveRefList();
            refListEditor.setText(relevantRefList.toString());
        }
        catch(Exception e)
        {
            EAM.alertUserOfNonFatalException(e);
        }
    }
}
