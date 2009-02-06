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

import org.miradi.actions.ActionCreateConceptualModel;
import org.miradi.actions.ActionDeleteConceptualModel;
import org.miradi.actions.ActionDiagramProperties;
import org.miradi.actions.ActionRenameConceptualModel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;

public class ConceptualModelDiagramSplitPane extends DiagramSplitPane
{
	public ConceptualModelDiagramSplitPane(MainWindow mainWindow) throws Exception
	{
		super(mainWindow, ObjectType.CONCEPTUAL_MODEL_DIAGRAM, "ConceptualModelDiagramSplitPane");
	}

	public DiagramLegendPanel createLegendPanel(MainWindow mainWindow)
	{
		return new ConceptualModelDiagramLegendPanel(mainWindow);
	}
	
	public DiagramPageList createPageList(MainWindow mainWindowToUse)
	{
		return new ConceptualModelPageList(mainWindowToUse);
	}
	
	public Class[] getPopUpMenuActions()
	{
		return  new Class[] {
				ActionDiagramProperties.class,
				null,
				ActionCreateConceptualModel.class,
				ActionRenameConceptualModel.class,
				ActionDeleteConceptualModel.class,
		};
	}
}
