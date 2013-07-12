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
package org.miradi.rtf.viewExporters;

import java.awt.image.BufferedImage;

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramObject;
import org.miradi.questions.ReportTemplateContentQuestion;
import org.miradi.rtf.RtfWriter;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.CodeList;

public class DiagramViewRtfExporter extends RtfViewExporter
{
	public DiagramViewRtfExporter(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	public void exportView(RtfWriter writer, CodeList reportTemplateContent) throws Exception
	{
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.DIAGRAM_VIEW_CONCEPTUAL_MODEL_TAB_CODE))
			exportDiagrams(writer, getProject().getConceptualModelDiagramPool().getRefList());
		
		if (reportTemplateContent.contains(ReportTemplateContentQuestion.DIAGRAM_VIEW_RESULTS_CHAINS_TAB_CODE))
			exportDiagrams(writer, getProject().getResultsChainDiagramPool().getRefList());
	}

	private void exportDiagrams(RtfWriter writer, ORefList diagramObjectRefs) throws Exception
	{
		for (int index = 0; index < diagramObjectRefs.size(); ++index)
		{
			writer.newParagraph();
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRefs.get(index));
			BufferedImage diagramAsImage = BufferedImageFactory.createImageFromDiagram(getMainWindow(), diagramObject);
			if (diagramAsImage == null)
				continue;
			
			writer.writeRaw(RtfWriter.BOLD_DIAGRAM_HEADER_FONT_COMMAND);
			writer.writeEncoded(diagramObject.toString());
			writer.newParagraph();
			
			writer.writeImage(diagramAsImage);
			
			writer.pageBreak();
		}
		
		writer.newParagraph();
	}
}