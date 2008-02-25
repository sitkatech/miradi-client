/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.views.diagram;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.martus.swing.UiScrollPane;
import org.miradi.diagram.DiagramComponent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.CodeList;

public class DiagramImageCreator
{
	static public BufferedImage getImage(MainWindow mainWindow, DiagramObject diagramObject)
	{
		try
		{
			DiagramComponent comp = getComponent(mainWindow, diagramObject);
			Rectangle bounds = comp.getCurrentUsedBounds();
			BufferedImage image = comp.getImage();
			
			int imageWidth = image.getWidth() - bounds.x;
			int imageHeight = image.getHeight() - bounds.y;
			
			return image.getSubimage(bounds.x, bounds.y, imageWidth, imageHeight);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}

	public static DiagramComponent getComponent(MainWindow mainWindow, DiagramObject diagramObject) throws Exception
	{
		DiagramComponent comp =  DiagramSplitPane.createDiagram(mainWindow, diagramObject);
		comp.getDiagramModel().updateVisibilityOfFactorsAndLinks();
		
		// TODO: This is here because setting a factor/link to be visible also has
		// the side effect of selecting it, so the last item added is selected but 
		// shouldn't be. So our quick fix is to clear the selection. 
		// Cleaner fixes ran into strange problems where Windows and Linux systems
		// behaved differently. SEE ALSO DiagramSplitPane.showCard()
		comp.clearSelection();
		
		//TODO: is there a better way to do this
		JFrame frame = new JFrame();
		frame.add(new UiScrollPane(comp));
		frame.pack();
		return comp;
	}
	
	static public BufferedImage getImageWithLegendSetting(MainWindow mainWindow, DiagramObject diagramObject, CodeList list)
	{
		DiagramLegendPanel panel = mainWindow.getDiagramView().getDiagramPanel().getDiagramLegendPanel();
		panel.updateLegendPanel(list);
		return getImage( mainWindow, diagramObject);	
	}
}
