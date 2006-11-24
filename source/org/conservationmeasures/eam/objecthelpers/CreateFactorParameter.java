/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.diagram.factortypes.FactorType;

public class CreateFactorParameter extends CreateObjectParameter
{
	public CreateFactorParameter(FactorType type)
	{
		nodeType = type;
	}

	public FactorType getNodeType()
	{
		return nodeType;
	}
	
	FactorType nodeType;
}
