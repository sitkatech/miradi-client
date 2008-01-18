/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JComponent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.ViewDoer;
import org.martus.swing.PrintPage;
import org.martus.swing.PrintPageFormat;

abstract public class PrintDoer extends ViewDoer
{
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
		JComponent view = getMainWindow().getCurrentView().getPrintableComponent();
		//PrintPage.showPreview(view);
		PrintPage.printJComponent(view, job, format, attributes);
	}
}
