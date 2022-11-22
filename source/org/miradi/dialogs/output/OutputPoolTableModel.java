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
package org.miradi.dialogs.output;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objects.Factor;
import org.miradi.objects.Output;
import org.miradi.project.Project;
import org.miradi.schemas.OutputSchema;

public class OutputPoolTableModel extends ObjectPoolTableModel
{
    public OutputPoolTableModel(Project projectToUse)
    {
        super(projectToUse, OutputSchema.getObjectType(), getColumnTags());
    }

	@Override
	public boolean isPseudoFieldColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if (columnTag.equals(Output.PSEUDO_TAG_FACTOR))
			return true;

		return false;
	}

    @Override
    public String getUniqueTableModelIdentifier()
    {
        return UNIQUE_MODEL_IDENTIFIER;
    }

    private static final String UNIQUE_MODEL_IDENTIFIER = "OutputPoolTableModel";

    private static String[] getColumnTags()
    {
        return new String[]{
                Factor.TAG_SHORT_LABEL,
                Factor.TAG_LABEL,
                Factor.TAG_TEXT,
                Output.PSEUDO_TAG_FACTOR,
        };
    }
}
