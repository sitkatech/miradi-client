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
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.actions.ActionEditAssumptionIndicatorRelevancyList;
import org.miradi.actions.Actions;
import org.miradi.icons.AssumptionIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractAssumption;
import org.miradi.objects.Assumption;
import org.miradi.project.Project;
import org.miradi.schemas.AssumptionSchema;

public class PlanningViewAssumptionPropertiesPanel extends MinimalFactorPropertiesPanel
{
    public PlanningViewAssumptionPropertiesPanel(Project projectToUse, MainWindow mainWindow) throws Exception
    {
        super(projectToUse, projectToUse.getObjectManager().getSchemas().get(ObjectType.ASSUMPTION));

        this.mainWindow = mainWindow;

        createAndAddFields(EAM.text("Assumption"), new AssumptionIcon());
    }

    @Override
    protected void addCustomFieldsStart()
    {
        super.addCustomFieldsStart();
    }

    @Override
    protected void addCustomFieldsMiddle()
	{
        Actions actionsToUse = this.mainWindow.getActions();
		addFieldWithEditButton(EAM.text("Indicators"), createReadOnlyObjectList(AssumptionSchema.getObjectType(), Assumption.PSEUDO_TAG_RELEVANT_INDICATOR_REFS), createObjectsActionButton(actionsToUse.getObjectsAction(ActionEditAssumptionIndicatorRelevancyList.class), getPicker()));
	}

    @Override
	protected void addCustomFieldsEnd() throws Exception
	{
        addField(createMultilineField(AssumptionSchema.getObjectType(), AbstractAssumption.TAG_IMPLICATIONS));
        addField(createMultilineField(AssumptionSchema.getObjectType(), AbstractAssumption.TAG_FUTURE_INFORMATION_NEEDS));

        addTaxonomyFields(AssumptionSchema.getObjectType());
	}

    @Override
    public String getPanelDescription()
    {
        return EAM.text("Title|Assumption Properties");
    }

    private MainWindow mainWindow;
}
