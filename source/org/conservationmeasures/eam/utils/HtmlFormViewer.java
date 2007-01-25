/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.ImageView;
import javax.swing.text.html.StyleSheet;

import org.conservationmeasures.eam.main.EAMResourceImageIcon;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiEditorPane;


public class HtmlFormViewer extends UiEditorPane implements HyperlinkListener
{
	public HtmlFormViewer(String htmlSource, HyperlinkHandler hyperLinkHandler)
	{
		linkHandler = hyperLinkHandler;
		
		setEditable(false);
		setText(htmlSource);
		addHyperlinkListener(this);
	}
	
	public void setText(String text)
	{
		HTMLEditorKit htmlKit = new OurHtmlEditorKit(linkHandler);
		StyleSheet style = htmlKit.getStyleSheet();
		customizeStyleSheet(style);
		htmlKit.setStyleSheet(style);
		setEditorKit(htmlKit);

		Document doc = htmlKit.createDefaultDocument();
		setDocument(doc);
		
		super.setText(text);
		setCaretPosition(0);
	}

	public void setFixedWidth( Component component, int width )
	{
		component.setSize( new Dimension( width, Short.MAX_VALUE ) );
		Dimension preferredSize = component.getPreferredSize();
		component.setPreferredSize( new Dimension( width, preferredSize.height ) );
	}
	
	protected void customizeStyleSheet(StyleSheet style)
	{
		style.addRule("body {background: #ffffff;}");
	}

	public void hyperlinkUpdate(HyperlinkEvent e)
	{
		if(e.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED))
		{
			String clicked = e.getDescription();
			linkHandler.linkClicked(clicked);
		}

	}
	
	class OurHtmlEditorKit extends HTMLEditorKit
	{
		public OurHtmlEditorKit(HyperlinkHandler handler)
		{
			factory = new OurHtmlViewFactory(handler);
			ourStyleSheet = new StyleSheet();
			ourStyleSheet.addStyleSheet(super.getStyleSheet());
		}
		
		public ViewFactory getViewFactory()
		{
			return factory;
		}

		public StyleSheet getStyleSheet()
		{
			return ourStyleSheet;
		}

		public void setStyleSheet(StyleSheet s)
		{
			ourStyleSheet = s;
		}

		ViewFactory factory;
		StyleSheet ourStyleSheet;
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
			if(elem.getName().equals("input"))
			{
				AttributeSet attributes = elem.getAttributes();
				Object typeAttribute = attributes.getAttribute(HTML.Attribute.TYPE);
				if(typeAttribute.equals("submit"))
				{
					return new OurButtonView(elem, handler);
				}
				if(typeAttribute.equals("text"))
				{
					return new OurTextView(elem, handler);
				}
				if(typeAttribute.equals("textarea"))
				{
					return new OurTextView(elem, handler);
				}
				if(typeAttribute.equals("label"))
				{
					return new OurLabelView(elem, handler);
				}
			}
			else if(elem.getName().equals("img"))
			{
				return new OurImageView(elem);
			}
			return super.create(elem);
		}
		
		HyperlinkHandler handler;
	}
	
	class OurButtonView extends FormView
	{
		public OurButtonView(Element elem, HyperlinkHandler handlerToUse)
		{
			super(elem);
			handler = handlerToUse;
		}

		protected void submitData(String data)
		{
			String buttonName = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
			handler.buttonPressed(buttonName);
		}
		
		HyperlinkHandler handler;
	}
	
	class OurSelectView extends FormView implements ItemListener
	{
		public OurSelectView(Element elem, HyperlinkHandler handlerToUse)
		{
			super(elem);
			handler = handlerToUse;
		}

		protected Component createComponent()
		{
			comboBox = (JComboBox)super.createComponent();
			comboBox.addItemListener(this);
			String fieldName = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
			((HtmlFormEventHandler)handler).setComponent(fieldName, comboBox);
			return comboBox;
		}

		public void itemStateChanged(ItemEvent e)
		{
			String name = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
			handler.valueChanged(name, comboBox.getSelectedItem().toString());
		}
		
		HyperlinkHandler handler;
		JComboBox comboBox;
	}
	
	
	class OurTextView extends FormView implements DocumentListener
	{
		public OurTextView(Element elem, HyperlinkHandler handlerToUse)
		{
			super(elem);
			handler = handlerToUse;
		}

		protected Component createComponent()
		{
			String fieldName = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
			textField = (JTextComponent)super.createComponent();
			textField.getDocument().addDocumentListener(this);
			((HtmlFormEventHandler)handler).setComponent(fieldName, textField);
			return textField;
		}

		public void changedUpdate(DocumentEvent event) 
		{
			notifyHandler();
		}


		public void insertUpdate(DocumentEvent event) 
		{
			notifyHandler();
		}

		public void removeUpdate(DocumentEvent event) 
		{
			notifyHandler();
		}
		
		private void notifyHandler() 
		{
			String name = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
			handler.valueChanged(name, textField.getText());
		}
		
		protected void submitData(String data)
		{
		}
		
		HyperlinkHandler handler;
		JTextComponent textField;

	}

	
	class OurLabelView extends FormView
	{
		public OurLabelView(Element elem, HyperlinkHandler handlerToUse)
		{
			super(elem);
			handler = handlerToUse;
		}

		protected Component createComponent()
		{
			String fieldName = (String)getElement().getAttributes().getAttribute(HTML.Attribute.NAME);
			JLabel label = new JLabel("");
			((HtmlFormEventHandler)handler).setComponent(fieldName, label);
			return label;
		}

		HyperlinkHandler handler;
	}
	
	class OurImageView extends ImageView
	{
		public OurImageView(Element elem)
		{
			super(elem);
			name = (String)elem.getAttributes().getAttribute(HTML.Attribute.SRC);
		}

		public Image getImage()
		{
			if(image == null)
			{
				try
				{
					EAMResourceImageIcon icon = new EAMResourceImageIcon(name);
					image = icon.getImage();
				}
				catch(NullPointerException e)
				{
					throw new RuntimeException(name, e);
				}
			}
			return image;
		}
		
		String name;
		Image image;
	}
	
	HyperlinkHandler linkHandler;
}

