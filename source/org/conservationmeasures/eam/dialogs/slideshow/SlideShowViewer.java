/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

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
	public SlideShowViewer(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		mainWindow = mainWindowToUse;
		loadSlides();
		showSlides();
		setAlwaysOnTop(true);
		setSize(600,600);
	}

	private void loadSlides() 
	{
		try 
		{
			SlideShow show = getSlideShow();
			ORefList slideRefs =  new ORefList(show.getData(SlideShow.TAG_SLIDE_REFS));
			slides  = (Slide[])show.getObjectManager().findObjectsAsVector(slideRefs).toArray(new Slide[0]);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			slides = new Slide[0];
		}
	}

	private void showSlides()
	{
		if (slides.length==0)
		{
			EAM.errorDialog("No slides to show");
			dispose();
			return;
		}
		
		imgArray = new BufferedImage[slides.length];
		
        for (int i=0; i<slides.length; ++i)
        {
        	Slide slide = slides[i];
        	DiagramObject diagramObject = (DiagramObject) getProject().findObject(slide.getDiagramRef());
        	BufferedImage img = createImage(diagramObject);
        	imgArray[i]=img;
        }
	}

	 protected void processKeyEvent(KeyEvent e) 
	 {
		 int keyCode = e.getKeyCode();
		switch (keyCode)
		{
			case KeyEvent.VK_1:
				if(e.getID() != KeyEvent.KEY_PRESSED)
					break;
				setTitle(slides[current].getLabel());
				determineDialogSizeing();
				repaint();
				break;
			case KeyEvent.VK_2:
				dispose();
				break;
		}
	 }

	private void determineDialogSizeing()
	{
		imgArray[current].getScaledInstance(600, 600, 0);
		setSize(imgArray[current].getWidth(), imgArray[current].getHeight());
	}
	
	 public void dispose()
	 {
		 super.dispose();
		 setVisible(false);
		 mainWindow.updateActionStates();

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


	
	public BufferedImage createImage(DiagramObject diagramObject)
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
	
    
    private BufferedImage[] imgArray = null;
    private int current  = 0;
    private MainWindow mainWindow;
    private Slide slides[];

}