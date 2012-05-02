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

import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatTargetTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.questions.ThreatRatingQuestion;

public class MainThreatTableModelExporter extends AbstractSingleTableExporter
{
	public MainThreatTableModelExporter(AbstractThreatTargetTableModel mainThreatTableModelToUse)
	{
		super(mainThreatTableModelToUse.getProject(), mainThreatTableModelToUse.getUniqueTableModelIdentifier());
		
		mainThreatTableModel = mainThreatTableModelToUse;
		threatRatingQuestion = (ThreatRatingQuestion) mainThreatTableModel.getProject().getQuestion(ThreatRatingQuestion.class);
	}
	
	@Override
	public ORefList getAllRefs(int objectType)
	{
		EAM.logError("getAllRefs is not implemented");
		return new ORefList();
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		EAM.logError("getAllTypes is not implemented");
		return new Vector<Integer>();
	}

	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		return mainThreatTableModel.getDirectThreat(row);
	}

	@Override
	public int getColumnCount()
	{
		return mainThreatTableModel.getColumnCount();
	}

	@Override
	public int getModelDepth(int row, int modelColumn)
	{
		return 0;
	}

	@Override
	public String getModelColumnName(int modelColumn)
	{
		return mainThreatTableModel.getColumnName(modelColumn);
	}
	
	public String getColumnGroupName(int modelColumn)
	{
		return getModelColumnName(modelColumn);
	}

	@Override
	public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
	{
		Object value = mainThreatTableModel.getValueAt(row, modelColumn);
		if (value == null)
			return new EmptyChoiceItem();
		
		ChoiceItem foundChoiceItem = threatRatingQuestion.findChoiceByLabel(value.toString());
		if (foundChoiceItem != null)
			return foundChoiceItem;
		
		return new TaglessChoiceItem(value.toString());
	}

	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}

	@Override
	public int getRowCount()
	{
		return mainThreatTableModel.getRowCount();
	}

	@Override
	public int getRowType(int row)
	{
		return getBaseObjectForRow(row).getType();
	}

	@Override
	public String getModelTextAt(int row, int modelColumn)
	{
		return getSafeValue(mainThreatTableModel.getValueAt(row, modelColumn));
	}
	
	protected AbstractThreatTargetTableModel getMainThreatTableModel()
	{
		return mainThreatTableModel;
	}
	
	private AbstractThreatTargetTableModel mainThreatTableModel;
	private ThreatRatingQuestion threatRatingQuestion;
}
