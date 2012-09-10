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

package org.miradi.utils;

import java.util.HashMap;

import org.miradi.main.EAM;
import org.miradi.project.Project;

public abstract class AbstractSingleTableExporter extends AbstractTableExporter
{
	public AbstractSingleTableExporter(Project projectToUse, String uniqueModelIdentifierToUse)
	{
		super(projectToUse, uniqueModelIdentifierToUse);
	}
	
	public AbstractSingleTableExporter(Project projectToUse)
	{
		super(projectToUse);
	}

	public int convertToModelColumn(int tableColumn)
	{
		if (tableToModelColumnIndexMap == null)
			buildTableToModelColumnIndexMap();
		
		if (!tableToModelColumnIndexMap.containsKey(tableColumn))
			throw new RuntimeException(EAM.text("Could not find tableColumn in map. tableColumn = " + tableColumn + ".") + EAM.text("UniqueModelIdentifier=" + uniqueModelIdentifier));
		
		return tableToModelColumnIndexMap.get(tableColumn);
	}

	private void buildTableToModelColumnIndexMap()
	{
		CodeList modelColumnSequence = getModelColumnSequence();
		CodeList arrangedColumnCodes = calculateArrangedColumnCodes(new CodeList(modelColumnSequence));
		tableToModelColumnIndexMap  = buildModelColumnIndexMap(arrangedColumnCodes, modelColumnSequence);
	}
	
	private HashMap<Integer, Integer> tableToModelColumnIndexMap;
}
