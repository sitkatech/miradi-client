/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.EAMGraphCell;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.ConceptualModelLinkage;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;

public class DiagramLinkage extends EAMGraphCell implements Edge
{
	public DiagramLinkage(DiagramModel modelToUse, ConceptualModelLinkage cmLinkage) throws Exception
	{
		model = modelToUse;
		underlyingObject = cmLinkage;
		String label = "";
		fillConnectorAttributeMap(label);
	}
	
	public boolean isLinkage()
	{
		return true;
	}
	
	public ConnectionSet getConnectionSet()
	{
		return new ConnectionSet(this, getSource(), getTarget());		
	}

	public DiagramNode getFromNode()
	{
		try
		{
			return model.getNodeById(getFromId());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("From node not found");
		}
	}
	public DiagramNode getToNode()
	{
		try
		{
			return model.getNodeById(getToId());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new RuntimeException("To node not found");
		}
	}
	
	public String getStressLabel()
	{
		return underlyingObject.getStressLabel();
	}
	
	private void fillConnectorAttributeMap(String label)
	{
	    GraphConstants.setLineEnd(getAttributes(), GraphConstants.ARROW_SIMPLE);
	    GraphConstants.setValue(getAttributes(), label);
	    GraphConstants.setOpaque(getAttributes(), true);
	    GraphConstants.setBackground(getAttributes(), Color.BLACK);
	    GraphConstants.setForeground(getAttributes(), Color.BLACK);
	    GraphConstants.setGradientColor(getAttributes(), Color.BLACK); //Windows 2000 quirk required to see line.
//		Font font = getFont().deriveFont(Font.BOLD);
//		GraphConstants.setFont(thisMap, font);
	}

	public Object getSource()
	{
		return getFromNode().getPort();
	}

	public Object getTarget()
	{
		return getToNode().getPort();
	}

	public void setSource(Object source)
	{
		// not allowed--ignore attempts to reset the source
	}

	public void setTarget(Object target)
	{
		// not allowed--ignore attempts to reset the target
	}

	public BaseId getId()
	{
		return underlyingObject.getId();
	}
	
	public BaseId getFromId()
	{
		return underlyingObject.getFromNodeId();
	}
	
	public BaseId getToId()
	{
		return underlyingObject.getToNodeId();
	}
	
	public LinkageDataMap createLinkageDataMap()
	{
		LinkageDataMap dataMap = new LinkageDataMap();
		dataMap.setId(getId());
		dataMap.setFromId(getFromId().asInt());
		dataMap.setToId(getToId().asInt());
		return dataMap;
	}
	
	private DiagramModel model;
	private ConceptualModelLinkage underlyingObject;
}
