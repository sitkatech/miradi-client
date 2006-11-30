/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;

public class CreateDiagramFactorLinkParameter extends CreateObjectParameter
{
	public CreateDiagramFactorLinkParameter(FactorLinkId factorLinkIdToUse, DiagramFactorId fromIdToUse, DiagramFactorId toIdToUse)
	{
		factorLinkId = factorLinkIdToUse;
		fromId = fromIdToUse;
		toId = toIdToUse;
	}
	
	public FactorLinkId getFactorLinkId()
	{
		return factorLinkId;
	}
	
	public DiagramFactorId getFromFactorId()
	{
		return fromId;
	}
	
	public DiagramFactorId getToFactorId()
	{
		return toId;
	}
	
	private FactorLinkId factorLinkId;
	private DiagramFactorId fromId;
	private DiagramFactorId toId;
}
