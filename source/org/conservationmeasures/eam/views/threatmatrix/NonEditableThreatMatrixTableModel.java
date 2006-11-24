/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class NonEditableThreatMatrixTableModel extends AbstractTableModel
{
	public NonEditableThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
		framework = getProject().getThreatRatingFramework();
		resetMatrix();
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
	
		if (isSumaryRow(row,column))
			return getFramework().getTargetThreatRatingValue(getTargetId(column));
	
		if (isSumaryColumn(row,column))
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
	
	private boolean isSumaryRow(int row, int column) 
	{
		return (row == getDirectThreats().length);
	}

	private boolean isSumaryColumn(int row, int column) 
	{
		return (column==getTargets().length);
	}
	
	private Object getDefaultValueOption() 
	{
		if (defaultValueOption!=null) 
			return defaultValueOption;
		
		try
		{
			defaultValueOption = new ValueOption(new BaseId(-1), "", -1, Color.WHITE);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		return defaultValueOption;
	}
	
	public String getColumnName(int columnIndex) 
	{
		if (columnIndex==getTargets().length)
			return EAM.text("Summary Target Rating");
		return getTargets()[columnIndex].toString();
	}
	
	public boolean isBundleTableCellABundle(int row, int column)
	{
		return realDataGetValueAt(row, column)!=null;
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

	public void resetMatrix() 
	{
		threatRows =  getProject().getNodePool().getDirectThreats();
		targetColumns =  getProject().getNodePool().getTargets();
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
	
	
	public ThreatRatingFramework getFramework() {
		return framework;
	}
	
	public Project getProject()
	{
		return project;
	}

	
	public ThreatRatingBundle getBundle(int threatIndex, int targetIndex)
			throws Exception
	{
		ModelNodeId threatId = getDirectThreats()[threatIndex].getModelNodeId();
		ModelNodeId targetId = getTargets()[targetIndex].getModelNodeId();
		ThreatRatingBundle bundle = getBundle(threatId, targetId);
		return bundle;
	}
	
	public ThreatRatingBundle getBundle(ModelNodeId threatId,
			ModelNodeId targetId) throws Exception
	{
		if(!isActiveThreatIdTargetIdPair(threatId, targetId))
			return null;

		return getFramework().getBundle(threatId, targetId);
	}


	public boolean isActiveCell(int threatIndex, int targetIndex)
	{
		if(threatIndex < 0 || targetIndex < 0)
			return false;
		
		ModelNodeId threatId = (ModelNodeId)getDirectThreats()[threatIndex].getId();
		ModelNodeId targetId = (ModelNodeId)getTargets()[targetIndex].getId();
		return isActiveThreatIdTargetIdPair(threatId, targetId);
	}
	
	public boolean isActiveThreatIdTargetIdPair(ModelNodeId threatId, ModelNodeId targetId)
	{
		return getProject().isLinked(threatId, targetId);
	}

	public ModelNodeId getThreatId(int threatIndex)
	{
		Factor cmNode = getThreatNode(threatIndex);
		return cmNode.getModelNodeId();
	}

	public Factor getThreatNode(int threatIndex)
	{
		return getDirectThreats()[threatIndex];
	}

	public String getThreatName(int threatIndex)
	{
		return getDirectThreats()[threatIndex].getLabel();
	}
	
	
	public ModelNodeId getTargetId(int targetIndex)
	{
		Factor cmNode = getTargetNode(targetIndex);
		return cmNode.getModelNodeId();
	}
	
	
	public int getTargetColumn(BaseId baseId) 
	{
		return findThreatIndexById(new ModelNodeId(baseId.asInt()));
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

	public Factor getTargetNode(int targetIndex)
	{
		return getTargets()[targetIndex];
	}

	public String getTargetName(int targetIndex)
	{
		return getTargets()[targetIndex].getLabel();
	}
	
	public int findTargetIndexById(ModelNodeId targetId)
	{
		for(int i = 0; i < getTargets().length; ++i)
			if(getTargets()[i].getId().equals(targetId))
				return i;
		return -1;
	}
	
	public int findThreatIndexById(ModelNodeId threatId)
	{
		for(int i = 0; i < getDirectThreats().length; ++i)
			if(getDirectThreats()[i].getId().equals(threatId))
				return i;
		return -1;
	}

	public ModelNodeId findThreatByName(String threatName)
	{
		return findNodeByName(getDirectThreats(), threatName);
	}

	public ModelNodeId findTargetByName(String targetName)
	{
		return findNodeByName(getTargets(), targetName);
	}

	private ModelNodeId findNodeByName(Factor[] nodes, String name)
	{
		for(int i = 0; i < nodes.length; ++i)
			if(nodes[i].getLabel().equals(name))
				return nodes[i].getModelNodeId();

		return new ModelNodeId(BaseId.INVALID.asInt());
	}

	
	Factor[] getTargets()
	{
		return targetColumns;
	}
	
	public int getTargetCount()
	{
		return getTargets().length;
	}

	Factor[] getDirectThreats()
	{
		return threatRows;
	}

	public int getThreatCount()
	{
		return getDirectThreats().length;
	}

	
	private Factor threatRows[] = null;
	private Factor targetColumns[] =  null;
	
	private Project project;
	private ThreatRatingFramework framework;
	private ValueOption defaultValueOption;
	private final static int SUMMARY_ROW_COLUMN_INCR = 1;


}
