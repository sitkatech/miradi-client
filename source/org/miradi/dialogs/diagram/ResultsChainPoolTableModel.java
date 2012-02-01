/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.project.Project;

public class ResultsChainPoolTableModel extends ObjectPoolTableModel
{
	public ResultsChainPoolTableModel(Project project)
	{
		super(project, ResultsChainDiagram.getObjectType(), getTags());
	}
	
	@Override
	public boolean isPseudoFieldColumn(int column)
	{
		String columnTag = getColumnTag(column);
		if (columnTag.equals(ResultsChainDiagram.PSEUDO_COMBINED_LABEL))
			return true;

		return super.isPseudoFieldColumn(column);
	}
	
	private static String[] getTags()
	{
		return new String[] {ResultsChainDiagram.PSEUDO_COMBINED_LABEL};
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return UNIQUE_MODEL_IDENTIFIER;
	}
	
	private static final String UNIQUE_MODEL_IDENTIFIER = "ResultsChainPoolTableModel";
}
