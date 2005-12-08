/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

public class EAMGraphCell extends DefaultGraphCell
{
	public EAMGraphCell()
	{
	}

	public boolean isNode()
	{
		return false;
	}
	
	public boolean isProjectScope()
	{
		return false;
	}
	
	public boolean isLinkage()
	{
		return false;
	}
	
	public void setText(String text)
	{
		GraphConstants.setValue(getAttributes(), text);
	}

	public String getText()
	{
		return (String)GraphConstants.getValue(getAttributes());
	}

	public void populateDataMap(DataMap dataBin)
	{
		dataBin.putString(TAG_VISIBLE_LABEL, getText());
	}

	public static final String TAG_VISIBLE_LABEL = "text";
}
