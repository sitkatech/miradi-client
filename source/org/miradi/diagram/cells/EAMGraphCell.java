/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.diagram.cells;

import java.awt.Color;

import org.jgraph.graph.DefaultGraphCell;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;

public class EAMGraphCell extends DefaultGraphCell
{
	public EAMGraphCell()
	{
	}

	public boolean isFactor()
	{
		return false;
	}
	
	public boolean isProjectScope()
	{
		return false;
	}
	
	public boolean isFactorLink()
	{
		return false;
	}
	
	public DiagramLink getDiagramLink()
	{
		return null;
	}
	
	public DiagramFactor getDiagramFactor()
	{
		return null;
	}
	
	public ORef getDiagramFactorRef()
	{
		return ORef.INVALID;
	}
	
	public ORef getWrappedFactorRef()
	{
		return ORef.INVALID;
	}
	
	public ORef getDiagramLinkRef()
	{
		return ORef.INVALID;
	}
	
	public ORef getWrappedFactorLinkRef()
	{
		return ORef.INVALID;
	}
	
	public Color getColor()
	{
		return Color.BLACK;
	}
	
}
