/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.table.DefaultTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ThreatRatingBundle;

public class NonEditableThreatMatrixTableModel extends DefaultTableModel
{
	public NonEditableThreatMatrixTableModel(Project projectToShow)
	{
		project = projectToShow;
	}
	
	public Object getValueAt(int row, int column) {
		Object object = super.getValueAt(row, column);
		if (object instanceof ThreatRatingBundle) 
		{
			object = project.getThreatRatingFramework().getBundleValue((ThreatRatingBundle) object);
		} 
		return object;
	}
	
	
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	
	
	public boolean isBundle(int row, int column) {
		Object object = super.getValueAt(row, column);
		return (object instanceof ThreatRatingBundle);
	}
	

	public boolean setBundle(ThreatRatingBundle bundle)
	{
		for(int row = 0; row < getThreatCount(); ++row)
		{
			for(int column = 0; column < getTargetCount(); ++column)
			{
				Object object = super.getValueAt(row, column);
				if (object instanceof ThreatRatingBundle) 
				{
					if (object.equals(bundle)) 
					{
						setValueAt(bundle, row, column);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	
	
	
	public ThreatRatingBundle getBundle(ModelNodeId threatId, ModelNodeId targetId) throws Exception
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
		
		BaseId threatId = getDirectThreats()[threatIndex].getId();
		BaseId targetId = getTargets()[targetIndex].getId();
		return isActiveThreatIdTargetIdPair(threatId, targetId);
	}

	private boolean isActiveThreatIdTargetIdPair(BaseId threatId, BaseId targetId)
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

	private ConceptualModelNode getThreatNode(int threatIndex)
	{
		ConceptualModelNode cmNode = getDirectThreats()[threatIndex];
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
		String[] names = new String[getThreatCount()];
		for(int i = 0; i < names.length; ++i)
			names[i] = getThreatName(i);
		return names;
	}
	
	public String[] getTargetNames()
	{
		String[] names = new String[getTargetCount()];
		for(int i = 0; i < names.length; ++i)
			names[i] = getTargetName(i);
		return names;
	}

	private ConceptualModelNode getTargetNode(int targetIndex)
	{
		ConceptualModelNode cmNode = getTargets()[targetIndex];
		return cmNode;
	}
	
	public int findThreatIndexById(ModelNodeId threatId)
	{
		return findNodeIndexById(getDirectThreats(), threatId);
	}
	
	private int findNodeIndexById(ConceptualModelNode[] nodes, ModelNodeId id)
	{
		for(int i = 0; i < nodes.length; ++i)
			if(nodes[i].getId().equals(id))
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
	
	private ModelNodeId findNodeByName(ConceptualModelNode[] nodes, String name)
	{
		for(int i = 0; i < nodes.length; ++i)
			if(nodes[i].getLabel().equals(name))
				return nodes[i].getModelNodeId();
		
		return new ModelNodeId(BaseId.INVALID.asInt());
	}
	
	Project project;
}
