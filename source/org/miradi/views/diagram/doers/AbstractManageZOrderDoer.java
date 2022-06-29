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

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.CommandVector;
import org.miradi.views.ViewDoer;

import java.util.Vector;

abstract public class AbstractManageZOrderDoer extends ViewDoer
{
    @Override
    public boolean isAvailable()
    {
        if (!isInDiagram())
            return false;

        FactorCell[] selectedFactors = getSelectedFactors();
		DiagramLink[] selectedLinks = getSelectedLinks();
		int combinedLengths = selectedLinks.length + selectedFactors.length;

		if (combinedLengths == 0)
			return false;

        return true;
    }

    @Override
    protected void doIt() throws Exception
    {
        if (! isAvailable())
            return;

        getProject().executeCommand(new CommandBeginTransaction());
        try
        {
            CommandVector commandsToAdjustZOrder = new CommandVector(createCommandsToAdjustZOrder());
            getProject().executeCommands(commandsToAdjustZOrder);
        }
        catch (Exception e)
        {
            throw new CommandFailedException(e);
        }
        finally
        {
            getProject().executeCommand(new CommandEndTransaction());
        }
    }

    protected FactorCell[] getSelectedFactors()
    {
        return getDiagramView().getDiagramPanel().getOnlySelectedFactorCells();
    }

    protected DiagramLink[] getSelectedLinks()
    {
        return getDiagramView().getDiagramPanel().getOnlySelectedLinks();
    }

    protected DiagramObject getDiagramObject()
    {
        return getDiagramView().getCurrentDiagramObject();
    }

    abstract protected Vector<CommandSetObjectData> createCommandsToAdjustZOrder() throws Exception;
}
