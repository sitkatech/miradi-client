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
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.ViewDoer;

public class ShowConceptualModelDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		if (! isInDiagram())
			return false;

		if (! getDiagramView().isResultsChainTab())
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
            setToNormalMode();
            showConceptualModel(getDiagramView());
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

    public static void showConceptualModel(DiagramView diagramView) throws Exception
    {
        final int CONCEPTUAL_MODEL_INDEX = 0;

        Project project = diagramView.getProject();
        ViewData viewData = project.getViewData(diagramView.cardName());
        ORef currentConceptualModelRef = viewData.getCurrentConceptualModelRef();

        CommandSetObjectData setTabCommand = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_TAB, Integer.toString(CONCEPTUAL_MODEL_INDEX));
        project.executeCommand(setTabCommand);

        CommandSetObjectData setCurrentDiagram = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF, currentConceptualModelRef);
        project.executeCommand(setCurrentDiagram);
    }

    private void setToNormalMode() throws Exception
    {
        ShowFullModelModeDoer.showFullModelMode(getProject(), getDiagramView().getCurrentDiagramComponent());
    }

}
