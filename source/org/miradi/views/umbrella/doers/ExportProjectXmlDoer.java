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
package org.miradi.views.umbrella.doers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.martus.util.UnicodeWriter;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;
import org.miradi.utils.ConstantButtonNames;
import org.miradi.utils.EAMFileSaveChooser;
import org.miradi.utils.EAMXmlFileChooser;
import org.miradi.views.MainWindowDoer;
import org.miradi.views.diagram.DiagramImageCreator;
import org.miradi.views.umbrella.SaveImageJPEGDoer;
import org.miradi.xml.export.ReportXmlExporter;

public class ExportProjectXmlDoer extends MainWindowDoer
{
	public boolean isAvailable()
	{
		return (getProject().isOpen());
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;

		String title = EAM.text("Export Project XML");
		String[] body = new String[] {
			EAM.text("This feature is not yet fully supported. " +
			"It exports all the project data in an XML file, but the schema of that file is still in flux. " +
			"Future versions of Miradi will export the data in different formats."),
		};
		String[] buttons = new String[] {
			EAM.text("Export"),
			ConstantButtonNames.CANCEL,
		};
		if(!EAM.confirmDialog(title, body, buttons))
			return;
		
		
		EAMFileSaveChooser eamFileChooser = new EAMXmlFileChooser(getMainWindow());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) 
			return;

		try
		{
			exportJustProjectXml(getProject(), chosen);
			EAM.notifyDialog(EAM.text("Export complete"));
		}
		catch(IOException e)
		{
			EAM.errorDialog(EAM.text("Unable to write XML. Perhaps the disk was full, or you " +
					"don't have permission to write to it."));
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
	}

	public static File exportProjectToXml(Project project, File destinationDirectory) throws Exception
	{
		if(!destinationDirectory.isDirectory())
			throw new RuntimeException("Can only export to a directory");
		
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

	private static void exportJustProjectXml(Project project, File destination) throws IOException, Exception
	{
		UnicodeWriter out = new UnicodeWriter(destination);
		try
		{
			out.writeln("<MiradiProject>");
			project.toXml(out);
			out.writeln("</MiradiProject>");
		}
		finally
		{
			out.close();
		}
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

		new SaveImageJPEGDoer().saveImage(out, DiagramImageCreator.getImage(mainWindow, diagramObject));
	}
}
