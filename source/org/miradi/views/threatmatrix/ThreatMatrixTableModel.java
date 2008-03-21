/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.threatmatrix;

import java.awt.Color;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.objects.ValueOption;
import org.miradi.project.Project;
import org.miradi.project.SimpleThreatRatingFramework;
import org.miradi.project.ThreatRatingBundle;

public class ThreatMatrixTableModel extends AbstractThreatTargetTableModel
{
	public ThreatMatrixTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		framework = getProject().getSimpleThreatRatingFramework();
	}

	public int getColumnCount()
	{
		return getTargets().length + SUMMARY_ROW_COLUMN_INCR ;
	}

	public int getRowCount()
	{
		return getDirectThreats().length + SUMMARY_ROW_COLUMN_INCR;
	}
	
	public Object getValueAt(int row, int column)
	{
		Object value= null;
		
		if (isOverallProjectRating(row,column))
			return getFramework().getOverallProjectRating();
	
		if (isSumaryRow(row))
			return getFramework().getTargetThreatRatingValue(getTargetId(column));
	
		if (isSumaryColumn(column))
			return getFramework().getThreatThreatRatingValue(getThreatId(row));

		ThreatRatingBundle bundle = realDataGetValueAt(row,column);
		if (bundle==null)
			value = getDefaultValueOption();
		else 
			value = getFramework().getBundleValue(bundle);

		return value;
	}
	
	private boolean isOverallProjectRating(int row, int column) 
	{
		return (row == getDirectThreats().length && column==getTargets().length);
	}
	
	boolean isSumaryRow(int row) 
	{
		return (row == getDirectThreats().length);
	}

	boolean isSumaryColumn(int column) 
	{
		return (column==getTargets().length);
	}
	
	public boolean isSummaryData(int row, int column) 
	{
		return isSumaryRow(row)  ||  isSumaryColumn(column);
	}
	
	private Object getDefaultValueOption() 
	{
		if (defaultValueOption!=null) 
			return defaultValueOption;
		
		try
		{
			defaultValueOption = new ValueOption(getProject().getObjectManager(), new BaseId(-1), "", -1, Color.WHITE);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}

		return defaultValueOption;
	}
	
	public void  setDefaultValueOption(ValueOption valueOption) 
	{
		defaultValueOption = valueOption;
	}
	
	public String getColumnName(int columnIndex) 
	{
		if (columnIndex==getTargets().length)
			return EAM.text("Summary Threat Rating");
		return getTargets()[columnIndex].toString();
	}
	
	public ThreatRatingBundle realDataGetValueAt(int row, int column)
	{
		ThreatRatingBundle bundle = null;
		try
		{
			bundle = getBundle(row, column);
		}
		catch(Exception e)
		{
			return null;
		}
		return bundle;
	}

	public void setThreatRows(Factor threatRowsToUse[] ) 
	{
		threatRows = threatRowsToUse;
	}

	public boolean isCellEditable(int row, int column)
	{
		return false;
	}
	
	
	//***********************************************************************************
	
	
	public SimpleThreatRatingFramework getFramework() {
		return framework;
	}
		
	public ThreatRatingBundle getBundle(int threatIndex, int targetIndex)
			throws Exception
	{
		FactorId threatId = getDirectThreats()[threatIndex].getFactorId();
		FactorId targetId = getTargets()[targetIndex].getFactorId();
		ThreatRatingBundle bundle = getBundle(threatId, targetId);
		return bundle;
	}
	
	public ThreatRatingBundle getBundle(FactorId threatId,
			FactorId targetId) throws Exception
	{
		ORef threatRef = new ORef(Cause.getObjectType(), threatId);
		ORef targetRef = new ORef(Target.getObjectType(), targetId);
		if(!getProject().areLinked(threatRef, targetRef))
			return null;

		return getFramework().getBundle(threatId, targetId);
	}

	public int getTargetColumn(BaseId baseId) 
	{
		return findThreatIndexById(new FactorId(baseId.asInt()));
	}

	public String[] getThreatNames()
	{
		return getNames(getDirectThreats());
	}

	public String[] getTargetNames()
	{
		return getNames(getTargets());
	}
	
	public String[] getNames(Factor[] nodes)
	{
		String[] names = new String[nodes.length];
		for(int i = 0; i < names.length; ++i)
			names[i] = nodes[i].toString();
		return names;
	}

	public int findTargetIndexById(FactorId targetId)
	{
		for(int i = 0; i < getTargets().length; ++i)
			if(getTargets()[i].getId().equals(targetId))
				return i;
		return -1;
	}
	
	public int findThreatIndexById(FactorId threatId)
	{
		for(int i = 0; i < getDirectThreats().length; ++i)
			if(getDirectThreats()[i].getId().equals(threatId))
				return i;
		return -1;
	}

	public FactorId findThreatByName(String threatName)
	{
		return findNodeByName(getDirectThreats(), threatName);
	}

	public FactorId findTargetByName(String targetName)
	{
		return findNodeByName(getTargets(), targetName);
	}

	private FactorId findNodeByName(Factor[] nodes, String name)
	{
		for(int i = 0; i < nodes.length; ++i)
			if(nodes[i].getLabel().equals(name))
				return nodes[i].getFactorId();

		return new FactorId(BaseId.INVALID.asInt());
	}
	
	protected boolean isPopupSupportableCell(int row, int modelColumn)
	{
		return !isSummaryData(row, modelColumn);
	}
	
	private SimpleThreatRatingFramework framework;
	private ValueOption defaultValueOption;
	private final static int SUMMARY_ROW_COLUMN_INCR = 1;
}
