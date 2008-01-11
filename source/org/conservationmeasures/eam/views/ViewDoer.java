/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.views.diagram.DiagramView;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

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
	
	//TODO either rename this, or change its functionality,  alot of the doers have same redundant checks
	public boolean inInDiagram()
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
