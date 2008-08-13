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
package org.miradi.rtf.viewExporters;

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramObject;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.BufferedImageFactory;

public class DiagramViewRtfExporter extends RtfViewExporter
{
	public DiagramViewRtfExporter(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	public void ExportView(RtfWriter writer) throws Exception
	{
		exportDiagrams(writer, getProject().getConceptualModelDiagramPool().getRefList());
		exportDiagrams(writer, getProject().getResultsChainDiagramPool().getRefList());
	}

	private void exportDiagrams(RtfWriter writer, ORefList diagramObjectRefs) throws Exception
	{
		for (int index = 0; index < diagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRefs.get(index));
			writer.writeImage(BufferedImageFactory.createImageFromDiagram(getMainWindow(), diagramObject));
			writer.newParagraph();
		}
	}
}