/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JDialog;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.DiagramImageCreator;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class SlideShowViewer extends JDialog 
{
	//TODO: early test code for slide show.....do not review 
	public SlideShowViewer(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		mainWindow = mainWindowToUse;
		slides = new Vector();
		loadSlides();
		showSlides();
	}

	private void loadSlides() 
	{
		try 
		{
			ORefList slideRefs =  new ORefList(getSlideShow().getData(SlideShow.TAG_SLIDE_REFS));
			for  (int i=0; i<slideRefs.size(); ++i)
			{
				slides.add(getProject().findObject(slideRefs.get(i)));
			}
		}
		catch (Exception e)
		{
			EAM.errorDialog("No slides to show");
			dispose();
		}
	}

	private void showSlides()
	{
		if (slides.size()==0)
		{
			EAM.errorDialog("No slides to show");
			dispose();
			return;
		}
		
		imgArray = new Image[slides.size()];
		
        for (int i=0; i<slides.size(); ++i)
        {
        	Slide slide = (Slide)slides.get(i);
        	DiagramObject diagramObject = (DiagramObject) getProject().findObject(slide.getDiagramRef());
        	Image img = createImage(diagramObject);
        	imgArray[i]=img;
        }
	}

	 protected void processKeyEvent(KeyEvent e) 
	 {
		 int keyCode = e.getKeyCode();
		 switch (keyCode)
		{
			case KeyEvent.VK_1:
			      repaint();
			      break;
			case KeyEvent.VK_2:
			      dispose();
			      break;
		}
	 }
	

    public void paint (Graphics g)
    {
        g.setColor(Color.white);
        g.fillRect(0,0, 200, 200);
        g.setColor(Color.black);
        g.drawImage(imgArray[current++], 0, 0, this);
        if (current >= imgArray.length) 
        	current=0;
    }


	
	public Image createImage(DiagramObject diagramObject)
	{
		return  DiagramImageCreator.getImage(mainWindow, diagramObject);
	}

	private SlideShow getSlideShow() throws CommandFailedException
	{
		return getDiagramView().getSlideShow();
	}

	private DiagramView getDiagramView()
	{
		return mainWindow.getDiagramView();
	}

	private Project getProject()
	{
		return mainWindow.getProject();
	}
	
    
    private Image[] imgArray = null;
    private int current  = 0;
    private MainWindow mainWindow;
    private Vector slides;

}