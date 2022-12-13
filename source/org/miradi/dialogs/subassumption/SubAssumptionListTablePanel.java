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
package org.miradi.dialogs.subassumption;

import org.miradi.actions.ActionCreateSubAssumption;
import org.miradi.actions.ActionDeleteSubAssumption;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Assumption;
import org.miradi.schemas.AssumptionSchema;
import org.miradi.views.umbrella.StaticPicker;

public class SubAssumptionListTablePanel extends ObjectListTablePanel
{
    public SubAssumptionListTablePanel(MainWindow mainWindowToUse, ORefList selectedHierarchy)
    {
        super(mainWindowToUse, new SubAssumptionListTableModel(mainWindowToUse.getProject(), selectedHierarchy), new StaticPicker(selectedHierarchy));

        addObjectActionButton(ActionCreateSubAssumption.class, getParentPicker());
        addUnknownTypeOfButton(ActionDeleteSubAssumption.class);
    }

    @Override
    public void handleCommandEventImmediately(CommandExecutedEvent event)
    {
        super.handleCommandEventImmediately(event);

        if (event.isSetDataCommandWithThisTypeAndTag(AssumptionSchema.getObjectType(), Assumption.TAG_SUB_ASSUMPTION_IDS))
        {
            ORef selectedObjectRef = getSelectedObjectRef();
            getTable().getObjectTableModel().resetRows();
            getTable().setSelectedRow(selectedObjectRef);
        }
    }
}
