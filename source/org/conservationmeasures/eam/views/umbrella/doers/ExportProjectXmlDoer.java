/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ConstantButtonNames;
import org.conservationmeasures.eam.utils.EAMFileSaveChooser;
import org.conservationmeasures.eam.utils.EAMXmlFileChooser;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.diagram.DiagramImageCreator;
import org.conservationmeasures.eam.views.umbrella.SaveImageDoer;
import org.martus.util.UnicodeWriter;

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
		exportJustProjectXml(project, destination);
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

		SaveImageDoer.saveImage(out, DiagramImageCreator.getImage(mainWindow, diagramObject));
	}
}
