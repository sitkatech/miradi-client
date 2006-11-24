/*
 * Copyright 2005, The Conservation Measures Partnership & Beneficent Technology, Inc. (Benetech, at www.benetech.org)
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodetypes;

import org.conservationmeasures.eam.main.EAM;




public class NodeTypeContributingFactor extends NodeTypeCause
{
	public boolean isContributingFactor()
	{
		return true;
	}

	public String toString()
	{
		return EAM.text("Type|Contributing Factor");
	}
}
