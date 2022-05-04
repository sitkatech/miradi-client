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

import org.miradi.views.ViewDoer;

public class ClearAllDoer extends ViewDoer
{
    public ClearAllDoer()
    {
        super();
    }

    @Override
    public boolean isAvailable()
    {
        if(!getProject().isOpen())
            return false;

        if (!isInDiagram())
            return false;

        DiagramView diagramView = (DiagramView)getView();
        return (diagramView.getCurrentDiagramComponent().getSelectionCount() > 0);
    }

    @Override
    protected void doIt() throws Exception
    {
        getMainWindow().preventActionUpdates();
        try
        {
            DiagramView diagramView = (DiagramView)getView();
            diagramView.getCurrentDiagramComponent().clearSelection();
        }
        finally
        {
            getMainWindow().allowActionUpdates();
            getMainWindow().updateActionsAndStatusBar();
        }
    }
}
