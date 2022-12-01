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

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Assumption;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class SubAssumptionListTableModel extends ObjectListTableModel
{
    public SubAssumptionListTableModel(Project projectToUse, ORefList selectedHierarchy)
    {
        super(projectToUse, selectedHierarchy, Assumption.TAG_SUB_ASSUMPTION_IDS, ObjectType.SUB_ASSUMPTION, COLUMN_TAGS);
    }

    private static String[] COLUMN_TAGS = {
            Task.TAG_SHORT_LABEL,
            Task.TAG_LABEL,
    };

    @Override
    public String getUniqueTableModelIdentifier()
    {
        return UNIQUE_MODEL_IDENTIFIER;
    }

    private static final String UNIQUE_MODEL_IDENTIFIER = "SubAssumptionListTableModel";
}
