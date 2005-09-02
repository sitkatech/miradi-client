/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.diagram;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JComponent;

import org.conservationmeasures.eam.diagram.DiagramComponent;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.martus.swing.PrintPage;
import org.martus.swing.PrintPageFormat;

public class Print extends MainWindowDoer
{

	public Print(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public boolean isAvailable() 
	{
		Project project = getMainWindow().getProject();
		if(!project.isOpen())
			return false;
		return project.getDiagramModel().getCellCount() > 0;
	}

	public void doIt() throws CommandFailedException 
	{
		PrintPageFormat format = new PrintPageFormat();
		PrinterJob job = PrinterJob.getPrinterJob();
		HashPrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
		while(true)
		{
			if (!job.printDialog(attributes))
				return;
			format.setFromAttributes(attributes);
			if(!format.possiblePaperSizeAndTrayMismatch)
				break;
			//TODO: Allow user to either go back and change the setting or continue to print
		}
		JComponent view = createDiagramView();
	//	PrintPage.showPreview(view);
		PrintPage.printJComponent(view, job, format, attributes);
	}
	
	private JComponent createDiagramView()
	{
		DiagramComponent diagramComponent = getMainWindow().getDiagramComponent();
		BufferedImage image = diagramComponent.getImage();
		JImage view = new JImage(image);
		view.setSize(diagramComponent.getPreferredSize());
		return view;
	}
	
	class JImage extends JComponent
	{
		public JImage(BufferedImage imageToUse)
		{
			image = imageToUse;
		}
		public void paint(Graphics g) 
		{
			g.drawImage(image, 0, 0, null);
		}
		BufferedImage image;
	}

}
