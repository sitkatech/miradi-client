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
package org.miradi.views.umbrella.doers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.views.umbrella.SaveImageJPEGDoer;
import org.miradi.views.umbrella.XmlExporter;
import org.miradi.xml.reports.export.ReportXmlExporter;

public class ExportProjectXmlDoer extends XmlExporter
{
	@Override
	protected void export(File chosen) throws Exception
	{
		new ReportXmlExporter(getProject()).export(chosen);
	}
		
	public static File exportProjectToXml(Project project, File destinationDirectory) throws Exception
	{
		if(!destinationDirectory.isDirectory())
			throw new RuntimeException("Can only export to a folder");
		
		File mainXmlFile = exportProjectXml(project, destinationDirectory);
		exportDiagrams(project, destinationDirectory);

		return mainXmlFile;
	}

	private static File exportProjectXml(Project project, File destinationDirectory) throws IOException, Exception
	{
		File destination = new File(destinationDirectory, project.getFilename() + ".xml");
		new ReportXmlExporter(project).export(destination);
		return destination;
	}

	private static void exportDiagrams(Project project, File destinationDirectory) throws Exception
	{
		exportDiagrams(project, destinationDirectory, "ConceptualModel", project.getConceptualModelDiagramPool().getRefList());
		exportDiagrams(project, destinationDirectory, "ResultsChain", project.getResultsChainDiagramPool().getRefList());
	}

	private static void exportDiagrams(Project project, File destinationDirectory, String tag, ORefList diagramRefs) throws Exception
	{
		for(int i = 0; i < diagramRefs.size(); ++i)
			exportDiagram(destinationDirectory, tag, (DiagramObject)project.findObject(diagramRefs.get(i)));
	}

	private static void exportDiagram(File destinationDirectory, String tag, DiagramObject diagramObject) throws Exception
	{
		ORef ref = diagramObject.getRef();
		String refName = ref.toXmlString();
		File imageFile = new File(destinationDirectory, refName + ".jpg");
		FileOutputStream out = new FileOutputStream(imageFile); 
		writeJPEG(out, diagramObject);
		out.close();
	}
	
	private static void writeJPEG(OutputStream out, DiagramObject diagramObject) throws Exception
	{
		// TODO: component needs a main window to get preferences. 
		// Should just pass prefs into component instead of the whole main window
		MainWindow mainWindow = EAM.getMainWindow();

		new SaveImageJPEGDoer().saveImage(out, BufferedImageFactory.createImageFromDiagram(mainWindow, diagramObject));
	}
}
