/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import javax.swing.AbstractAction;

public abstract class DiagramAction extends AbstractAction
{
	public DiagramAction(DiagramComponent diagramComponentToUse, String label)
	{
		super(label);
		diagramComponent = diagramComponentToUse;
	}
	
	public DiagramComponent getDiagramComponent()
	{
		return diagramComponent;
	}
	
	DiagramComponent diagramComponent;
}