/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.slideshow;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.text.ParseException;

import javax.swing.JDialog;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.diagram.DiagramImageCreator;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class SlideShowViewer extends JDialog implements WindowListener
{
	public SlideShowViewer(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		mainWindow = mainWindowToUse;
		loadSlides();
		showSlides();
		setAlwaysOnTop(true);
		setSize(600,600);
		addWindowListener(this);
		displaySlide();
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
        	BufferedImage img = createImage(diagramObject, slide);
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
				displaySlide();
				break;
			case KeyEvent.VK_2:
				dispose();
				break;
			case KeyEvent.VK_3:
				setSize(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height);
		        image = imgArray[current].getScaledInstance(getWidth(), -1, 0);
				repaint();
				break;
			case KeyEvent.VK_4:
				setSize(600,600);
		        image = imgArray[current].getScaledInstance(getWidth(), -1, 0);
				repaint();
				break;
		}
	 }

	private void displaySlide()
	{
		setTitle(slides[current].getLabel());
		image = imgArray[current].getScaledInstance(getWidth(), -1, 0);
		current =  (++current%imgArray.length);
		repaint();
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
        g.fillRect(0,0, getWidth(), getHeight());
        g.setColor(Color.black);
        g.drawImage(image, 0, 0, this);
    }


	
	public BufferedImage createImage(DiagramObject diagramObject, Slide slide)
	{
		return  DiagramImageCreator.getImageWithLegendSetting(mainWindow, diagramObject, getDiagarmLegendSettingsForSlide(slide));
	}

	
	private CodeList getDiagarmLegendSettingsForSlide(Slide slide)
	{
		try
		{
			return  new CodeList(slide.getData(Slide.TAG_DIAGRAM_LEGEND_SETTINGS));
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			EAM.errorDialog("Unable to read slide settings:" + e.getMessage());
			return new CodeList();
		}
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
	
	public void windowActivated(WindowEvent arg0)
	{
	}

	public void windowClosed(WindowEvent arg0)
	{
	}

	public void windowClosing(WindowEvent arg0)
	{
		dispose();
	}

	public void windowDeactivated(WindowEvent arg0)
	{
	}

	public void windowDeiconified(WindowEvent arg0)
	{
	}

	public void windowIconified(WindowEvent arg0)
	{
	}

	public void windowOpened(WindowEvent arg0)
	{
	}
	
    
    private BufferedImage[] imgArray = null;
    private int current  = 0;
    private MainWindow mainWindow;
    private Slide slides[];
    Image image;

}