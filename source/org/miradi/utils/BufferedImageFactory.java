/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.miradi.diagram.DiagramComponent;
import org.miradi.dialogs.treetables.TreeTableWithRowHeightSaver;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.views.diagram.DiagramSplitPane;
import org.miradi.views.diagram.DiagramView;
import org.miradi.views.umbrella.XmlExporterDoer;


public  class BufferedImageFactory
{
	public static BufferedImage getImage(Icon icon)
	{
		int width = icon.getIconWidth() + 2 * ICON_INSET;
		int height = icon.getIconHeight() + 2 * ICON_INSET;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Color.WHITE);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		icon.paintIcon(null, graphics, ICON_INSET, ICON_INSET);
		graphics.dispose();
		
		return image;
	}
	
	public static BufferedImage getImage(JComponent swingComponent,  int inset) throws ImageTooLargeException
	{
		RealizedComponentWrapper realizedComponentWrapper = realizeComponent(swingComponent);
		try
		{
			JComponent realizedComponent = realizedComponentWrapper.getComponent();
			return createImage(realizedComponent, inset);
		}
		finally
		{
			realizedComponentWrapper.cleanup();
		}
	}

	private static BufferedImage createImage(JComponent swingComponent, int inset) throws OutOfMemoryError, ImageTooLargeException
	{
		Rectangle2D bounds = new Rectangle(swingComponent.getPreferredSize());
		toScreen(bounds);
		int width = (int) bounds.getWidth() + 2 * inset;
		int height = (int) bounds.getHeight() + 2 * inset;

		try
		{
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			Graphics2D graphics = image.createGraphics();
			graphics.setColor(Color.WHITE);
			graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
			swingComponent.print(graphics);
			graphics.dispose();
			return image;
		}
		catch(OutOfMemoryError e)
		{
			double size = width * height;
			double max = 30000000;
			int tooBigByPercent = (int)((size / max - 1) * 100);
			if(tooBigByPercent < 0)
				throw e;
			
			throw new ImageTooLargeException(tooBigByPercent);
		}
	}

	public static RealizedComponentWrapper realizeComponent(JComponent swingComponent)
	{
		return new RealizedComponentWrapper(swingComponent);
	}
	
	public static HashMap<String, BufferedImage> createNamesToImagesMap(MainWindow mainWindowToUse) throws Exception
	{
		HashMap<String, BufferedImage> namesToDiagramImagesMap = new HashMap<String, BufferedImage>();
		ORefList allDiagramObjectRefs = mainWindowToUse.getProject().getAllDiagramObjectRefs();
		for (int refIndex = 0; refIndex < allDiagramObjectRefs.size(); ++refIndex)
		{
			DiagramObject diagramObject = (DiagramObject) mainWindowToUse.getProject().findObject(allDiagramObjectRefs.get(refIndex));
			ORef diagramRef = diagramObject.getRef();
			String imageName = createImageFileName(diagramRef.getObjectId().asInt(), diagramRef);
			BufferedImage diagramImage = createImageFromDiagram(mainWindowToUse, diagramObject);
			namesToDiagramImagesMap.put(imageName, diagramImage);
		}
		
		return namesToDiagramImagesMap;
	}
	
	public static String createImageFileName(int uniqueId, ORef diagramRef)
	{
		return getDiagramPrefix(diagramRef) + uniqueId + PNGFileFilter.EXTENSION;
	}

	public static String getDiagramPrefix(ORef diagramObjectRef)
	{
		if (ConceptualModelDiagram.is(diagramObjectRef))
			return XmlExporterDoer.CM_IMAGE_PREFIX;
		
		return XmlExporterDoer.RC_IMAGE_PREFIX;
	}
	
	public static BufferedImage createImageFromDiagram(MainWindow mainWindow, DiagramObject diagramObject) throws Exception
	{
		DiagramComponent diagram = BufferedImageFactory.createDiagramComponent(mainWindow, diagramObject);
		return createImageFromDiagram(diagram);
	}
	
	public static BufferedImage createImageFromDiagramWithCurrentSettings(MainWindow mainWindow, DiagramObject diagramObject, int scalePercent) throws Exception
	{
		final double DEFAULT_ZOOM_FOR_GOOD_PRINTING = 2.0; 
		double scaleRelativeToOne = DEFAULT_ZOOM_FOR_GOOD_PRINTING * scalePercent / 100.0;
		DiagramComponent diagram = BufferedImageFactory.createDiagramComponentWithCurrentSettings(mainWindow, diagramObject, scaleRelativeToOne);
		return createImageFromDiagram(diagram);
	}
	
	private static BufferedImage createImageFromDiagram(DiagramComponent diagram) throws ImageTooLargeException
	{
		Rectangle totalBoundsIgnoringVisibilityOfFactors = diagram.getDiagramObject().getBoundsOfFactorsAndBendPoints();
		if (totalBoundsIgnoringVisibilityOfFactors == null)
			totalBoundsIgnoringVisibilityOfFactors = new Rectangle();
		
		final double scale = diagram.getScale();
		final Rectangle unscaledBounds = totalBoundsIgnoringVisibilityOfFactors.getBounds();

		Rectangle scaledBounds = new Rectangle(unscaledBounds);
		scaledBounds.x *= scale;
		scaledBounds.y *= scale;
		scaledBounds.width *= scale;
		scaledBounds.height *= scale;
		
		scaledBounds.grow((int)diagram.getGridSize(), (int)diagram.getGridSize());
		final Dimension size = new Dimension(scaledBounds.x + scaledBounds.width, scaledBounds.y + scaledBounds.height);
		forceDiagramSize(diagram, size);
		diagram.setToDefaultBackgroundColor();
		diagram.setGridVisible(false);

		BufferedImage image = BufferedImageFactory.getImage(diagram, 5);

		int x = Math.max(scaledBounds.x, 0);
		int y = Math.max(scaledBounds.y, 0);
		int imageWidth = image.getWidth() - x; 
		int imageHeight = image.getHeight() - y;

		return image.getSubimage(x, y, imageWidth, imageHeight);
	}

	private static void forceDiagramSize(DiagramComponent diagram, final Dimension size)
	{
		diagram.setAutoResizeGraph(false);
		diagram.setSize(size);
		diagram.setPreferredSize(size);
		diagram.setMinimumSize(size);
		diagram.setMaximumSize(size);
	}
	
	private static void toScreen(Rectangle2D rect) 
	{
		rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}
	
	public static BufferedImage createImageFromTreeTable(TreeTableWithRowHeightSaver table) throws ImageTooLargeException
	{
		table.updateAutomaticRowHeights();
		return createImageForTableInScrollPane(table);
	}

	public static BufferedImage createImageFromTable(TableWithRowHeightSaver table) throws ImageTooLargeException
	{
		table.updateAutomaticRowHeights();	
		return createImageForTableInScrollPane(table);
	}

	private static BufferedImage createImageForTableInScrollPane(JTable table) throws ImageTooLargeException
	{
		JScrollPane scrollerToShowHeaders = new JScrollPane(table);
		scrollerToShowHeaders.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollerToShowHeaders.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollerToShowHeaders.getViewport().setPreferredSize(table.getPreferredSize());
		
		return createImageFromComponent(scrollerToShowHeaders);
	}
	
	public static BufferedImage createImageFromComponent(JComponent component) throws ImageTooLargeException
	{
		return getImage(component, COMPONENT_INSET);
	}
	
	public static DiagramComponent createDiagramComponent(MainWindow mainWindow, DiagramObject diagramObject) throws Exception
	{
		DiagramComponent diagram =  DiagramSplitPane.createMemoryDiagram(mainWindow, diagramObject);
		String currentMode = mainWindow.getProject().getDiagramViewData().getCurrentMode();
		DiagramView.hideFactorsForMode(diagram, currentMode);
		diagram.getDiagramModel().updateVisibilityOfFactorsAndLinks();
		diagram.setScale(1.0);
		
		// TODO: This is here because setting a factor/link to be visible also has
		// the side effect of selecting it, so the last item added is selected but 
		// shouldn't be. So our quick fix is to clear the selection. 
		// Cleaner fixes ran into strange problems where Windows and Linux systems
		// behaved differently. SEE ALSO DiagramSplitPane.showCard()
		diagram.clearSelection();
		
		return diagram;
	}
	
	public static DiagramComponent createDiagramComponentWithCurrentSettings(MainWindow mainWindow, DiagramObject diagramObject, double scaleRelativeToOne) throws Exception
	{
		DiagramComponent diagram = createDiagramComponent(mainWindow, diagramObject);
		diagram.setScale(scaleRelativeToOne);
		
		return diagram;
	}
	
	private static final int COMPONENT_INSET = 5;
	private static final int ICON_INSET = 1;
}
