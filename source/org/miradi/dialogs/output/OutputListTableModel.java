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
package org.miradi.dialogs.output;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.project.Project;

public class OutputListTableModel extends ObjectListTableModel
{
    public OutputListTableModel(Project projectToUse, ORefList selectedHierarchy)
    {
        super(projectToUse, selectedHierarchy, Factor.TAG_OUTPUT_REFS, ObjectType.OUTPUT, getColumnTags());
    }

    private static String[] getColumnTags()
    {
        return new String[] {
            Factor.TAG_SHORT_LABEL,
            Factor.TAG_LABEL,
            Factor.TAG_TEXT,
        };
    }

    @Override
    public String getUniqueTableModelIdentifier()
    {
        return UNIQUE_MODEL_IDENTIFIER;
    }

    private static final String UNIQUE_MODEL_IDENTIFIER = "OutputListTableModel";
}
