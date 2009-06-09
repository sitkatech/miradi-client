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

package org.miradi.diagram.arranger;

import java.awt.Point;
import java.util.Vector;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.Project;

public class MeglerArranger
{
	public MeglerArranger(DiagramObject diagramToArrange)
	{
		diagram = diagramToArrange;
		
	}

	public void arrange() throws Exception
	{
		extractFactorsOfInterest();

		setLocations();
	}

	private void setLocations() throws Exception
	{
		moveFactors(strategies, STRATEGY_COLUMN_X, TOP_Y);
		moveFactors(threats, THREAT_COLUMN_X, TOP_Y);
		moveFactors(targets, TARGET_COLUMN_X, TOP_Y);
	}

	private void moveFactors(Vector<DiagramFactor> factors, int x, int initialY) throws Exception
	{
		int y = initialY;
		FactorCommandHelper helper = new FactorCommandHelper(getProject(), diagram);
		for(DiagramFactor diagramFactor : factors)
		{
			Point newLocation = new Point(x, y);
			helper.setDiagramFactorLocation(diagramFactor.getDiagramFactorId(), newLocation);
			
			y += DELTA_Y;
		}
	}
	
	private Project getProject()
	{
		return diagram.getProject();
	}

	private void extractFactorsOfInterest()
	{
		strategies = new Vector<DiagramFactor>();
		threats = new Vector<DiagramFactor>();
		targets = new Vector<DiagramFactor>();
		
		Project project = diagram.getProject();
		ORefList diagramFactorRefs = diagram.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(i));
			ORef wrappedRef = diagramFactor.getWrappedORef();
			if(Strategy.is(wrappedRef))
				strategies.add(diagramFactor);
			if(Target.is(wrappedRef))
				targets.add(diagramFactor);
			if(Cause.is(wrappedRef))
			{
				Cause cause = Cause.find(project, wrappedRef);
				if(cause.isDirectThreat())
					threats.add(diagramFactor);
			}
		}
	}

	private static final int STRATEGY_COLUMN_X = 30;
	private static final int THREAT_COLUMN_X = 180;
	private static final int TARGET_COLUMN_X = 330;
	
	private static final int TOP_Y = 30;
	private static final int DELTA_Y = 90;

	private DiagramObject diagram;
	private Vector<DiagramFactor> strategies;
	private Vector<DiagramFactor> threats;
	private Vector<DiagramFactor> targets;
	
}
