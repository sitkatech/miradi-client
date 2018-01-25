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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.diagram.DiagramComponent;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.AbstractTransferableMiradiList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.utils.CommandVector;

public class PasteDiagramFactorFormatDoer extends AbstractPasteDoer
{
    @Override
    public boolean isAvailable()
    {
        boolean isSuperAvailable = super.isAvailable();
        if (!isSuperAvailable)
            return false;

        if (getSelectedDiagramFactors().length == 0)
            return false;

        return !hasMoreThanOneFactorInClipboard();
    }

    @Override
    protected void doIt() throws Exception
    {
        if (!isAvailable())
            return;

        getProject().executeCommand(new CommandBeginTransaction());
        try
        {
            AbstractTransferableMiradiList list = getTransferableMiradiList();
            if (list == null)
                return;

            ORefList copiedDiagramFactorRefs = list.getDiagramFactorRefs();
            if (copiedDiagramFactorRefs.isEmpty() || copiedDiagramFactorRefs.size() > 1)
                return;

            ORef selectedDiagramFactorCopiedFrom = copiedDiagramFactorRefs.get(0);
            DiagramFactor[] selectedDiagramFactors = getSelectedDiagramFactors();

            CommandVector commands = buildCommandsToCopyFormat(selectedDiagramFactorCopiedFrom, selectedDiagramFactors);
            getProject().executeCommands(commands);
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

    private CommandVector buildCommandsToCopyFormat(ORef diagramFactorRefCopiedFrom, DiagramFactor[] selectedDiagramFactors)
    {
        CommandVector commands = new CommandVector();

        DiagramFactor diagramFactorCopiedFrom = DiagramFactor.find(getProject(), diagramFactorRefCopiedFrom);

        for (DiagramFactor diagramFactor : selectedDiagramFactors)
        {
            commands.addAll(diagramFactorCopiedFrom.createCommandsToCopyFormat(diagramFactor.getDiagramFactorId()));
        }

        return commands;
    }

    private DiagramFactor[] getSelectedDiagramFactors()
    {
        DiagramComponent currentDiagramComponent = getDiagramView().getCurrentDiagramComponent();
        if (currentDiagramComponent == null)
            return new DiagramFactor[0];

        return currentDiagramComponent.getOnlySelectedDiagramFactors();
    }
}
