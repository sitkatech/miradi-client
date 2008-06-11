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
package org.miradi.views.diagram;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.martus.swing.UiScrollPane;
import org.miradi.diagram.DiagramComponent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.CodeList;

public class DiagramImageCreator
{
	public static BufferedImage createImageFromDiagram(MainWindow mainWindow, DiagramObject diagramObject)
	{
		try
		{
			DiagramComponent diagram = createComponent(mainWindow, diagramObject);
			
			Rectangle bounds = new Rectangle(diagram.getTotalBoundsUsed().getBounds());
			diagram.toScreen(bounds);
			diagram.setToDefaultBackgroundColor();
			diagram.setGridVisible(false);
			
			//TODO: is there a better way to do this
			JFrame frame = new JFrame();
			frame.add(new UiScrollPane(diagram));
			frame.pack();

			BufferedImage image = BufferedImageFactory.getImage(diagram, 5);
			
			int x = Math.max(bounds.x, 0);
			int y = Math.max(bounds.y, 0);
			int imageWidth = image.getWidth() - x; 
			int imageHeight = image.getHeight() - y;
			
			return image.getSubimage(x, y, imageWidth, imageHeight);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}

	public static DiagramComponent createComponent(MainWindow mainWindow, DiagramObject diagramObject) throws Exception
	{
		DiagramComponent diagram =  DiagramSplitPane.createDiagram(mainWindow, diagramObject);
		diagram.setScale(1.0);
		diagram.getDiagramModel().updateVisibilityOfFactorsAndLinks();
		
		// TODO: This is here because setting a factor/link to be visible also has
		// the side effect of selecting it, so the last item added is selected but 
		// shouldn't be. So our quick fix is to clear the selection. 
		// Cleaner fixes ran into strange problems where Windows and Linux systems
		// behaved differently. SEE ALSO DiagramSplitPane.showCard()
		diagram.clearSelection();
		
		return diagram;
	}
	
	static public BufferedImage getImageWithLegendSetting(MainWindow mainWindow, DiagramObject diagramObject, CodeList list)
	{
		DiagramLegendPanel panel = mainWindow.getDiagramView().getDiagramPanel().getDiagramLegendPanel();
		panel.updateLegendPanel(list);
		return createImageFromDiagram( mainWindow, diagramObject);	
	}
}
