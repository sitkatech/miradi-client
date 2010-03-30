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

package org.miradi.views.diagram.doers;

import java.awt.Dimension;

import org.martus.swing.Utilities;
import org.miradi.diagram.arranger.MeglerArranger;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.utils.ProgressInterface;
import org.miradi.views.ViewDoer;

public class ArrangeConceptualModelDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		if(!isInDiagram())
			return false;
		
		return ConceptualModelDiagram.is(getCurrentDiagramObject().getType());
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		ProgressDialog progressDialog = new ProgressDialog(getMainWindow(), EAM.text("Arranging Diagram"));
		progressDialog.setMinimumSize(new Dimension(300, 0));
		Utilities.centerDlg(progressDialog);
		
		Worker worker = new Worker(progressDialog);
		getDiagramView().getCurrentDiagramComponent().setVisible(false);
		try
		{
			worker.start();
			progressDialog.setVisible(true);
			worker.cleanup();
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getDiagramView().getCurrentDiagramComponent().setVisible(true);
		}
	}
	
	class Worker extends Thread
	{
		public Worker(ProgressInterface progressToNotify)
		{
			progress = progressToNotify;
		}
		
		public void cleanup() throws Exception
		{
			if(exception != null)
				throw exception;
		}

		public void run()
		{
			try
			{
				doRealWork();
			}
			catch(Exception e)
			{
				exception = e;
			}
			finally
			{
				progress.finished();
			}
		}

		private void doRealWork() throws CommandFailedException
		{
			getProject().executeBeginTransaction();
			try
			{
				MeglerArranger meglerArranger = new MeglerArranger(getCurrentDiagramObject(), progress);
				if(!meglerArranger.arrange())
				{
					EAM.notifyDialog("The auto-arrange process was stopped before it completed, \n" +
							"potentially leaving factors in undesirable locations.  \n" +
							"If this is the case, use Edit/Undo to restore the diagram to its original state.");
				}
			}
			catch(Exception e)
			{
				throw new CommandFailedException(e);
			}
			finally
			{
				getProject().executeEndTransaction();
			}
		}
		
		private ProgressInterface progress;
		private Exception exception;
	}

	private DiagramObject getCurrentDiagramObject()
	{
		DiagramObject currentDiagramObject = getDiagramView().getCurrentDiagramObject();
		return currentDiagramObject;
	}
}
