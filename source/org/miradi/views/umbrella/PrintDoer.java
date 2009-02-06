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
package org.miradi.views.umbrella;

import java.awt.print.PrinterJob;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JComponent;

import org.martus.swing.PrintPage;
import org.martus.swing.PrintPageFormat;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.views.ViewDoer;

abstract public class PrintDoer extends ViewDoer
{
	public void doIt() throws CommandFailedException 
	{
		try
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
			JComponent componentToPrint = getMainWindow().getCurrentView().getPrintableComponent();
			BufferedImageFactory.realizeComponent(componentToPrint);
			PrintPage.printJComponent(componentToPrint, job, format, attributes);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
}
