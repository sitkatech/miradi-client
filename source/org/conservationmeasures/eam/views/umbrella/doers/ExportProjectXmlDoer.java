/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.utils.ConstantButtonNames;
import org.conservationmeasures.eam.utils.EAMFileSaveChooser;
import org.conservationmeasures.eam.utils.EAMXmlFileChooser;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.diagram.DiagramImageCreator;
import org.conservationmeasures.eam.views.umbrella.SaveImageSVGDoer;
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
			exportProjectXml(chosen);
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

	private void exportProjectXml(File destination) throws Exception
	{
		UnicodeWriter out = new UnicodeWriter(destination);
		try
		{
			out.writeln("<MiradiProject>");
			getProject().toXml(out);
			exportDiagrams(out);
			out.writeln("</MiradiProject>");
		}
		finally
		{
			out.close();
		}
	}
	
	private void exportDiagrams(UnicodeWriter out) throws Exception
	{
		out.writeln("<Images>");
		exportDiagrams(out, "ConceptualModel", getProject().getConceptualModelDiagramPool().getRefList());

		exportDiagrams(out, "ResultsChain", getProject().getResultsChainDiagramPool().getRefList());
		out.writeln("</Images>");
	}

	private void exportDiagrams(UnicodeWriter out, String tag, ORefList diagramRefs) throws Exception
	{
		for(int i = 0; i < diagramRefs.size(); ++i)
			exportDiagram(out, tag, (DiagramObject)getProject().findObject(diagramRefs.get(i)));
	}

	private void exportDiagram(UnicodeWriter out, String tag, DiagramObject diagramObject) throws Exception
	{
		out.write("<" + tag + " ref='");
		diagramObject.getRef().toXml(out);
		out.writeln("'>");
		String svg = getSVGFragment(diagramObject);
		encodeXmlToWriter(out, svg);
		out.writeln("</" + tag + ">");
	}
	
	public static void encodeXmlToWriter(Writer writer, String text) throws IOException
	{
		StringBuffer buf = new StringBuffer(text);
		for(int i = 0; i < buf.length(); ++i)
		{
			char c = buf.charAt(i);
			if(c == '&')
			{
				writer.write("&amp;");
			}
			else if(c == '<')
			{
				writer.write("&lt;");
			}
			else if(c == '>')
			{
				writer.write("&gt;");
			}
			else if(c == '"')
			{
				writer.write("&quot;");
			}
			else if(c == '\'')
			{
				writer.write("&#39;");
			}
			else
			{
				writer.write(c);
			}
		}
	}



	private String getSVGFragment(DiagramObject diagramObject) throws Exception, SVGGraphics2DIOException
	{
		StringWriter svgFragment = new StringWriter();
		DiagramComponent component = DiagramImageCreator.getComponent(getMainWindow(), diagramObject);
		SaveImageSVGDoer.saveImage(svgFragment, component);
		svgFragment.close();
		return svgFragment.toString();
	}
}
