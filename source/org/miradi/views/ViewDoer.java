/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views;

import org.miradi.views.diagram.DiagramView;
import org.miradi.views.planning.PlanningView;
import org.miradi.views.umbrella.UmbrellaView;

abstract public class ViewDoer extends MainWindowDoer
{
	public void setView(UmbrellaView view)
	{
		this.view = view;
	}

	public UmbrellaView getView()
	{
		return view;
	}
	
	public DiagramView getDiagramView() throws RuntimeException
	{
		if (view.cardName().equals(DiagramView.getViewName()))
			return (DiagramView)getView();
		
		throw new RuntimeException("Not in DiagramView");
	}

	public boolean isPlanningView()
	{
		if (! getView().cardName().equals(PlanningView.getViewName()))
			return false;
		
		return true;
	}
	
	public boolean isInDiagram()
	{
		if (! getView().cardName().equals(DiagramView.getViewName()))
			return false;
		
		if (getDiagramView().getDiagramComponent() == null)
			return false;
		
		if (getDiagramView().getDiagramModel() == null)
			return false;
		
		return true;
	}

	private UmbrellaView view;
}
