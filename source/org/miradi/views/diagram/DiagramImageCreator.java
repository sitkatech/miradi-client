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
package org.miradi.views.diagram;

import java.awt.image.BufferedImage;

import org.miradi.main.MainWindow;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.CodeList;

public class DiagramImageCreator
{
	static public BufferedImage getImageWithLegendSetting(MainWindow mainWindow, DiagramObject diagramObject, CodeList list) throws Exception
	{
		DiagramLegendPanel panel = mainWindow.getDiagramView().getDiagramPanel().getDiagramLegendPanel();
		panel.updateLegendPanel(list);
		return BufferedImageFactory.createImageFromDiagram( mainWindow, diagramObject);	
	}
}
