/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.views.umbrella;

import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JComponent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.martus.swing.PrintPage;
import org.martus.swing.PrintPageFormat;

abstract public class PrintDoer extends MainWindowDoer
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
