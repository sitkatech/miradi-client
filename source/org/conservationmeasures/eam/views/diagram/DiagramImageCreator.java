/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.views.diagram;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.utils.CodeList;
import org.martus.swing.UiScrollPane;

public class DiagramImageCreator
{
	static public BufferedImage getImage(MainWindow mainWindow, DiagramObject diagramObject)
	{
		try
		{
			DiagramComponent comp =  DiagramSplitPane.createDiagram(mainWindow, diagramObject);
			comp.getDiagramModel().updateVisibilityOfFactorsAndLinks();
			//TODO: is there a better way to do this
			JFrame frame = new JFrame();
			frame.add(new UiScrollPane(comp));
			frame.pack();
			return comp.getImage();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}
	
	static public BufferedImage getImageWithLegendSetting(MainWindow mainWindow, DiagramObject diagramObject, CodeList list)
	{
		DiagramLegendPanel panel = mainWindow.getDiagramView().getDiagramPanel().getDiagramLegendPanel();
		panel.updateLegendPanel(list);
		return getImage( mainWindow, diagramObject);
	}
}
