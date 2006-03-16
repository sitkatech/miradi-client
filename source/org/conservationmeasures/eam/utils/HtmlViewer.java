/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlViewer extends JEditorPane implements HyperlinkListener
{
	public HtmlViewer(String htmlSource, HyperlinkHandler hyperLinkHandler)
	{
		linkHandler = hyperLinkHandler;
		
		setEditorKit(new OurHtmlEditorKit(linkHandler));
		setEditable(false);
		setText(htmlSource);
		setBackground(Color.LIGHT_GRAY);
		addHyperlinkListener(this);
	}

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if(e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
		{
			String clicked = e.getDescription();
			linkHandler.clicked(clicked);
		}

	}
	
	class OurHtmlEditorKit extends HTMLEditorKit
	{
		public OurHtmlEditorKit(HyperlinkHandler handler)
		{
			factory = new OurHtmlViewFactory(handler);
		}
		
		public ViewFactory getViewFactory()
		{
			return factory;
		}

		ViewFactory factory;
	}
	
	class OurHtmlViewFactory extends HTMLEditorKit.HTMLFactory
	{
		public OurHtmlViewFactory(HyperlinkHandler handlerToUse)
		{
			handler = handlerToUse;
		}
		
		public View create(Element elem)
		{
			if(elem.getName().equals("select"))
			{
				return new OurSelectView(elem, handler);
			}
			return super.create(elem);
		}
		
		HyperlinkHandler handler;
	}
	
	class OurSelectView extends FormView implements ItemListener
	{
		public OurSelectView(Element elem, HyperlinkHandler handlerToUse)
		{
			super(elem);
			handler = handlerToUse;
			element = elem;
		}

		protected Component createComponent()
		{
			comboBox = (JComboBox)super.createComponent();
			comboBox.addItemListener(this);
			return comboBox;
		}

		public void itemStateChanged(ItemEvent e)
		{
			String name = (String)element.getAttributes().getAttribute(HTML.Attribute.NAME);
			handler.valueChanged(name, comboBox.getSelectedItem().toString());
		}
		
		HyperlinkHandler handler;
		Element element;
		JComboBox comboBox;
	}
	
	HyperlinkHandler linkHandler;
}