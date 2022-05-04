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

package org.miradi.project;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.schemas.CauseSchema;
import org.miradi.utils.CommandVector;

public class ThreatReductionResultEnsurer implements CommandExecutedListener
{
    public ThreatReductionResultEnsurer(Project projectToUse)
    {
        project = projectToUse;
    }

    public void disable()
    {
        getProject().removeCommandExecutedListener(this);
    }

    public void enable()
    {
        getProject().addCommandExecutedListener(this);
    }

    public void commandExecuted(CommandExecutedEvent event)
    {
        try
        {
            if (event.isSetDataCommandWithThisTypeAndTag(CauseSchema.getObjectType(), Cause.TAG_IS_DIRECT_THREAT))
                possiblyClearRelatedDirectThreat(event);
        }
        catch (Exception e)
        {
            EAM.logException(e);
            throw new RuntimeException(e);
        }
    }

    private void possiblyClearRelatedDirectThreat(CommandExecutedEvent event) throws Exception
    {
        CommandSetObjectData setCommand = event.getSetCommand();
        Cause threat = Cause.find(getProject(), setCommand.getObjectORef());
        if (!threat.isDirectThreat())
        {
            CommandVector commandsToRemoveFromThreatReductionResults  = threat.getCommandsToRemoveFromThreatReductionResults();
            getProject().executeAsSideEffect(commandsToRemoveFromThreatReductionResults);
        }
    }

    private Project getProject()
    {
        return project;
    }

    private Project project;
}
