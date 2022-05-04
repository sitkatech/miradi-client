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
package org.miradi.views.diagram.doers;

import org.miradi.actions.ActionShowAssumptionBubble;
import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AnalyticalQuestion;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.views.ObjectsDoer;

public class CreateAssumptionDoer extends ObjectsDoer
{
    @Override
    public boolean isAvailable()
    {
        BaseObject selectedParent = getSelectedParentFactor();
        if (selectedParent == null)
            return false;

        return AnalyticalQuestion.is(selectedParent);
    }

    @Override
    protected void doIt() throws Exception
    {
        doInsertAssumption();

        ShowAssumptionBubbleDoer showAssumptionBubbleDoer = (ShowAssumptionBubbleDoer)getView().getDoer(ActionShowAssumptionBubble.class);
        if (showAssumptionBubbleDoer.isAvailable())
            showAssumptionBubbleDoer.safeDoIt();
    }

    private void doInsertAssumption() throws CommandFailedException
    {
        if(!isAvailable())
            return;

        AnalyticalQuestion analyticalQuestion = (AnalyticalQuestion) getSelectedParentFactor();

        try
        {
            insertAssumption(getProject(), analyticalQuestion, analyticalQuestion.getAssumptionIds().size());
        }
        catch (Exception e)
        {
            EAM.logException(e);
            throw new CommandFailedException(e);
        }
    }

    private void insertAssumption(Project project, AnalyticalQuestion analyticalQuestion, int childIndex) throws Exception
    {
        project.executeCommand(new CommandBeginTransaction());
        try
        {
            CommandCreateObject create = new CommandCreateObject(ObjectType.ASSUMPTION);
            project.executeCommand(create);
            BaseId createdId = create.getCreatedId();

            CommandSetObjectData addChild = CommandSetObjectData.createInsertIdCommand(analyticalQuestion, AnalyticalQuestion.TAG_ASSUMPTION_IDS, createdId, childIndex);
            project.executeCommand(addChild);
        }
        finally
        {
            project.executeCommand(new CommandEndTransaction());
        }

    }
}
