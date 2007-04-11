/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.views.diagram.DiagramView;
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
	
	public DiagramView getDiagramView() throws Exception
	{
		if (view.getName().equals(DiagramView.getViewName()))
			return (DiagramView)getView();
		
		throw new Exception("Not in DiagramView");
	}

	private UmbrellaView view;
}
