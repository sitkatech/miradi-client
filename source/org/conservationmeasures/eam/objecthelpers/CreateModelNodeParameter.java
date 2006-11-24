/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.diagram.nodetypes.FactorType;

public class CreateModelNodeParameter extends CreateObjectParameter
{
	public CreateModelNodeParameter(FactorType type)
	{
		nodeType = type;
	}

	public FactorType getNodeType()
	{
		return nodeType;
	}
	
	FactorType nodeType;
}
