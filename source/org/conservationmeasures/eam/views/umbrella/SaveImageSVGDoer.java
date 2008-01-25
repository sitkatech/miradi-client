/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;

import javax.swing.JComponent;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMSVGFileChooser;
import org.conservationmeasures.eam.views.ViewDoer;
import org.martus.util.UnicodeWriter;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

public class SaveImageSVGDoer extends ViewDoer
{
	public boolean isAvailable() 
	{
		Project project = getMainWindow().getProject();
		return project.isOpen();
	}

	public void doIt() throws CommandFailedException 
	{
		if (!isAvailable())
			return;
		
		EAMSVGFileChooser eamFileChooser = new EAMSVGFileChooser(getMainWindow());
		File chosen = eamFileChooser.displayChooser();
		if (chosen==null) 
			return;
		
		try 
		{
			FileOutputStream out = new FileOutputStream(chosen);
			saveImage(out); 
			out.close();
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
		} 
	}

	private void saveImage(FileOutputStream out) throws IOException 
	{
		SVGGraphics2D svgGenerator = createGeneratorSVG();
		JComponent component = getView().getPrintableComponent();
		component.paint(svgGenerator);
		
		boolean useCSS = true;
        Writer writer = new UnicodeWriter(out);
        svgGenerator.stream(writer, useCSS);
        writer.close();
	}
	
	private SVGGraphics2D createGeneratorSVG()
	{
	       // Get a DOMImplementation.
        DOMImplementation domImpl =
            GenericDOMImplementation.getDOMImplementation();

        // Create an instance of org.w3c.dom.Document.
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);

        // Create an instance of the SVG Generator.
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        return svgGenerator;
	}
}
