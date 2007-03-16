/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.utils.PointList;

public class DeleteBendPointDoer extends LocationDoer
{
	public boolean isAvailable()
	{
		if(!getProject().isOpen())
			return false;
		
		DiagramFactorLink[] links = getProject().getOnlySelectedLinks();
		if (links.length != 1)
			return false;
		
		PointList bendPoints = links[0].getBendPoints();
		if (bendPoints.size() <= 0)
			return false;
		
		//TODO remove commented code when done
		//EdgeHandle handle = (EdgeHandle) getMainWindow().getDiagramComponent().getUI().getHandle();
		
//		DiagramFactorLink link = links[0];
//		DiagramComponent diagramComponent = getMainWindow().getDiagramComponent();
//		LinkCell linkCell = diagramComponent.getDiagramModel().getDiagramFactorLink(link);
//		LinkCell[] singleElementArray = {linkCell};
//		CellView[] cellView = diagramComponent.getGraphLayoutCache().getMapping(singleElementArray);
//		FactorLinkView linkView = (FactorLinkView) cellView[0];
//		List handles = linkView.getPoints();
//		int handleSize = diagramComponent.getHandleSize(); 
//
//		for (int i = 0; i < bendPoints.size(); i++)
//		{
//			Point bendPoint = getLocation();
//			for (int j = 0; j < handles.size(); j++)
//			{
//				if (handles.get(j) instanceof PortView)
//				{
//					
//					PortView portView = (PortView) handles.get(j);
//					
//					Point2D handlePoint = portView.getLocation();
//					System.out.println("handle point = "+handlePoint);
//					Rectangle2D bounds = portView.getBounds();
//					//bounds.setFrame(handlePoint.getX() - handleSize, handlePoint.getY() - handleSize, 2 * handleSize, 2 * handleSize);
//
//					System.out.println("here "+bendPoint);
//					//System.out.println(bounds);
//					if (bounds.contains(bendPoint))
//						System.out.println("bend point contained");
//				}
//			}
//		}
		
		//TODO should check to see if a bend point was selected
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
	}
}
