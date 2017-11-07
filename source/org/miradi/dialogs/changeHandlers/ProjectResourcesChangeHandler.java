/*
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.changeHandlers;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objecthelpers.ObjectType;

public class ProjectResourcesChangeHandler implements CommandExecutedListener
{

    public ProjectResourcesChangeHandler()
    {
        rebuildRequired = false;
    }

    @Override
    public void commandExecuted(CommandExecutedEvent event)
    {
        if (eventForcesRebuild(event))
            rebuildRequired = true;
    }

    public boolean getRebuildRequired()
    {
        return rebuildRequired;
    }

    public void setRebuildRequired(boolean rebuildRequiredToUse)
    {
        rebuildRequired = rebuildRequiredToUse;
    }

    private boolean eventForcesRebuild(CommandExecutedEvent event)
    {
        if (event.isCreateCommandForThisType(ObjectType.PROJECT_RESOURCE))
            return true;

        if (event.isDeleteCommandForThisType(ObjectType.PROJECT_RESOURCE))
            return true;

        if (event.isSetDataCommandWithThisType(ObjectType.PROJECT_RESOURCE))
            return true;

        return false;
    }

    private boolean rebuildRequired;
}
