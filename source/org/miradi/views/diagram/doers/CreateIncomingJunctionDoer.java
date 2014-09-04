/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.diagram.doers;

import java.awt.Rectangle;

import org.miradi.objects.DiagramLink;
import org.miradi.utils.PointList;

public class CreateIncomingJunctionDoer extends AbstractCreateJunctionDoer
{
	@Override
	protected int getDirection()
	{
		return DiagramLink.TO;
	}
	
	@Override
	protected int getInsertBendPointAtIndex(PointList bendPoints)
	{
		return bendPoints.size();
	}
	
	@Override
	protected int calculateJunctionX(Rectangle bounds)
	{		
		return bounds.x - JUNCTION_DISTANCE_FROM_FACTOR;
	}
}
