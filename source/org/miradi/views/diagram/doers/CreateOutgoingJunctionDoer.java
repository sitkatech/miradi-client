/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Dimension;
import java.awt.Point;

import org.miradi.objects.DiagramFactor;
import org.miradi.objects.FactorLink;
import org.miradi.utils.PointList;

public class CreateOutgoingJunctionDoer extends AbstractCreateJunctionDoer
{
	protected int getDirection()
	{
		return FactorLink.FROM;
	}
	
	protected void setBendPoint(PointList bendPoints, Point junctionPointToInsert)
	{
		bendPoints.set(0, junctionPointToInsert);
	}
	
	protected Point getJunctionPoint(DiagramFactor diagramFactor)
	{
		Point location = diagramFactor.getLocation();
		Dimension size = diagramFactor.getSize();
		
		int junctionX = location.x + size.width;
		int junctionY = getVerticalCenter(location, size);
		
		Point junctionPoint = new Point(junctionX, junctionY);
		junctionPoint.translate(JUNCTION_DISTANCE_FROM_FACTOR, 0);
		
		return junctionPoint;
	}
}
