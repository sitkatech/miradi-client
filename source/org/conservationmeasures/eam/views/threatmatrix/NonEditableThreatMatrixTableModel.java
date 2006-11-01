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
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;
import org.conservationmeasures.eam.project.ThreatRatingFramework;

public class NonEditableThreatMatrixTableModel extends AbstractTableModel
{
	public NonEditableThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
		threatRows = getDirectThreats();
		targetColumns = getTargets();
	}

	public int getColumnCount()
	{
		return targetColumns.length + SUMMARY_ROW_COLUMN_INCR ;
	}

	public int getRowCount()
	{
		return threatRows.length + SUMMARY_ROW_COLUMN_INCR;
	}
	
	public Object getValueAt(int row, int column)
	{
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		
		Object value= null;
		
		if (row == threatRows.length && column==targetColumns.length)
			return framework.getOverallProjectRating();
	
		if (row == threatRows.length) {
			return framework.getTargetThreatRatingValue(getTargetId(column));
		}
	
		if (column==targetColumns.length) {
			return framework.getThreatThreatRatingValue(getThreatId(row));
		}

		ThreatRatingBundle bundle = realDataGetValueAt(row,column);
		if (bundle==null)
			value = getDefaultValueOption();
		else 
			value = project.getThreatRatingFramework().getBundleValue(bundle);

		return value;
	}


	private Object getDefaultValueOption() 
	{
		Object value = null;
		try
		{
			value = new ValueOption(new BaseId(-1), "", -1, Color.WHITE);
		}
		catch(Exception e)
		{
		}

		return value;
	}
	
	public String getColumnName(int columnIndex) 
	{
		if (columnIndex==targetColumns.length)
			return EAM.text("Summary Target Rating");
		return targetColumns[columnIndex].toString();
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

	public void setThreatRows(ConceptualModelNode threatRowsToUse[] ) {
		threatRows = threatRowsToUse;
	}
	
	public ThreatRatingFramework getFramework() {
		return project.getThreatRatingFramework();
	}
	
	public boolean isCellEditable(int row, int column)
	{
		return false;
	}

	
	//***********************************************************************************
	
	public ThreatRatingBundle getBundle(int threatIndex, int targetIndex)
			throws Exception
	{
		ModelNodeId threatId = threatRows[threatIndex].getModelNodeId();
		ModelNodeId targetId = targetColumns[targetIndex].getModelNodeId();
		ThreatRatingBundle bundle = getBundle(threatId, targetId);
		return bundle;
	}
	
	public ThreatRatingBundle getBundle(ModelNodeId threatId,
			ModelNodeId targetId) throws Exception
	{
		if(!isActiveThreatIdTargetIdPair(threatId, targetId))
			return null;

		return project.getThreatRatingFramework().getBundle(threatId, targetId);
	}

	public Project getProject()
	{
		return project;
	}

	public int getTargetCount()
	{
		return getTargets().length;
	}

	ConceptualModelNode[] getDirectThreats()
	{
		return project.getNodePool().getDirectThreats();
	}

	public int getThreatCount()
	{
		return getDirectThreats().length;
	}

	ConceptualModelNode[] getTargets()
	{
		return project.getNodePool().getTargets();
	}

	public boolean isActiveCell(int threatIndex, int targetIndex)
	{
		if(threatIndex < 0 || targetIndex < 0)
			return false;

		BaseId threatId = threatRows[threatIndex].getId();
		BaseId targetId = targetColumns[targetIndex].getId();
		return isActiveThreatIdTargetIdPair(threatId, targetId);
	}

	private boolean isActiveThreatIdTargetIdPair(BaseId threatId,
			BaseId targetId)
	{
		return project.isLinked(threatId, targetId);
	}

	public String getThreatName(int threatIndex)
	{
		ConceptualModelNode cmNode = getThreatNode(threatIndex);
		return cmNode.getLabel();
	}

	public ModelNodeId getThreatId(int threatIndex)
	{
		ConceptualModelNode cmNode = getThreatNode(threatIndex);
		return cmNode.getModelNodeId();
	}

	public ConceptualModelNode getThreatNode(int threatIndex)
	{
		ConceptualModelNode cmNode = threatRows[threatIndex];
		return cmNode;
	}

	public String getTargetName(int targetIndex)
	{
		ConceptualModelNode cmNode = getTargetNode(targetIndex);
		return cmNode.getLabel();
	}

	public ModelNodeId getTargetId(int targetIndex)
	{
		ConceptualModelNode cmNode = getTargetNode(targetIndex);
		return cmNode.getModelNodeId();
	}

	public String[] getThreatNames()
	{
		String[] names = new String[threatRows.length];
		for(int i = 0; i < names.length; ++i)
			names[i] = threatRows[i].toString();
		return names;
	}

	public String[] getTargetNames()
	{
		String[] names = new String[targetColumns.length];
		for(int i = 0; i < names.length; ++i)
			names[i] = targetColumns[i].toString();
		return names;
	}

	public ConceptualModelNode getTargetNode(int targetIndex)
	{
		ConceptualModelNode cmNode = targetColumns[targetIndex];
		return cmNode;
	}

	public int findThreatIndexById(ModelNodeId threatId)
	{
		for(int i = 0; i < threatRows.length; ++i)
			if(threatRows[i].getId().equals(threatId))
				return i;
		return -1;
	}


	public ModelNodeId findThreatByName(String threatName)
	{
		return findNodeByName(threatRows, threatName);
	}

	public ModelNodeId findTargetByName(String targetName)
	{
		return findNodeByName(targetColumns, targetName);
	}

	private ModelNodeId findNodeByName(ConceptualModelNode[] nodes, String name)
	{
		for(int i = 0; i < nodes.length; ++i)
			if(nodes[i].getLabel().equals(name))
				return nodes[i].getModelNodeId();

		return new ModelNodeId(BaseId.INVALID.asInt());
	}

	ConceptualModelNode threatRows[] = null;
	ConceptualModelNode targetColumns[] =  null;
	Project project;
	int SUMMARY_ROW_COLUMN_INCR = 1;


}
