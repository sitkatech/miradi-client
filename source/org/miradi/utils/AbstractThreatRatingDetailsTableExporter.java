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

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Target;
import org.miradi.project.Project;

abstract public class AbstractThreatRatingDetailsTableExporter extends AbstractSingleTableExporter
{
	public AbstractThreatRatingDetailsTableExporter(Project projectToUse, Target targetToUse, String[] columnTagsToUse)
	{
		super(projectToUse);
		
		target = targetToUse;
		columnTags = columnTagsToUse;
	}
	
	protected String getColumnTag(int column)
	{
		return columnTags[column];
	}

	@Override
	public int getColumnCount()
	{
		return columnTags.length;
	}

	protected ORef getTargetRef()
	{
		return target.getRef();
	}
	
	protected String getThreatColumnName()
	{
		return EAM.text("Threat");
	}
	
	public String getColumnGroupName(int modelColumn)
	{
		return getModelColumnName(modelColumn);
	}
	
	@Override
	public int getModelDepth(int row, int modelColumn)
	{
		return 0;
	}

	protected boolean isThreatNameColumn(int modelColumn)
	{
		return modelColumn == THREAT_NAME_COLUMN_INDEX;
	}

	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}

	@Override
	public int getRowType(int row)
	{
		return getBaseObjectForRow(row).getType();
	}

	@Override
	public String getModelTextAt(int row, int modelColumn)
	{
		return "";
	}

	@Override
	public ORefList getAllRefs(int objectType)
	{
		ORefList allObjectRefs = new ORefList();
		for (int row = 0; row < getRowCount(); ++row)
		{
			allObjectRefs.add(getBaseObjectForRow(row).getRef());
		}
		
		return allObjectRefs;
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		if (getRowCount() == 0)
			return new Vector<Integer>();
		
		Vector<Integer> rowTypes = new Vector<Integer>();
		rowTypes.add(getRowType(0));
		
		return rowTypes;
	}

	private static final int THREAT_NAME_COLUMN_INDEX = 0;
	private Target target;
	private String[] columnTags;
}
