package com.thinking.xml;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
   This program displays an XML document as a tree.
*/
public class DOMTreeTest
{ 
   public static void main(String[] args)
   {  
      JFrame frame = new DOMTreeFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame contains a tree that displays the contents of
   an XML document.
*/
class DOMTreeFrame extends JFrame
{  
   public DOMTreeFrame()
   {  
      setTitle("DOMTreeTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      JMenu fileMenu = new JMenu("File");
      JMenuItem openItem = new JMenuItem("Open");
      openItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event) { openFile(); }
         });
      fileMenu.add(openItem);

      JMenuItem exitItem = new JMenuItem("Exit");
      exitItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event) { System.exit(0); }
         });
      fileMenu.add(exitItem);

      JMenuBar menuBar = new JMenuBar();
      menuBar.add(fileMenu);
      setJMenuBar(menuBar);
   }

   /**
      Open a file and load the document.
   */
   public void openFile()
   {  
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File("."));

      chooser.setFileFilter(new
         javax.swing.filechooser.FileFilter()
         {  
            public boolean accept(File f)
            {  
               return f.isDirectory() || f.getName().toLowerCase().endsWith(".xml");
            }
            public String getDescription() { return "XML files"; }
         });
      int r = chooser.showOpenDialog(this);
      if (r != JFileChooser.APPROVE_OPTION) return;
      File f = chooser.getSelectedFile();
      try
      {
         if (builder == null)
         {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
         }
         //得到文档，构造树
         Document doc = builder.parse(f);
         JTree tree = new JTree(new DOMTreeModel(doc));
         tree.setCellRenderer(new DOMTreeCellRenderer());

         setContentPane(new JScrollPane(tree));
         validate();
      }
      catch (IOException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (ParserConfigurationException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
      catch (SAXException e)
      {
         JOptionPane.showMessageDialog(this, e);
      }
   }

   private DocumentBuilder builder;
   private static final int DEFAULT_WIDTH = 400;
   private static final int DEFAULT_HEIGHT = 400;
}

/**
   This tree model describes the tree structure of an XML document.
*/
class DOMTreeModel implements TreeModel
{ 
   /**
      Constructs a document tree model.
      @param doc the document
   */
   public DOMTreeModel(Document doc) { this.doc = doc; }

   public Object getRoot() { return doc.getDocumentElement(); }

   public int getChildCount(Object parent)
   {  
      Node node = (Node) parent;
      NodeList list = node.getChildNodes();
      return list.getLength();
   }

   public Object getChild(Object parent, int index)
   {  
      Node node = (Node) parent;
      NodeList list = node.getChildNodes();
      return list.item(index);
   }

   public int getIndexOfChild(Object parent, Object child)
   {  
      Node node = (Node) parent;
      NodeList list = node.getChildNodes();
      for (int i = 0; i < list.getLength(); i++)
         if (getChild(node, i) == child)
            return i;
      return -1;
   }

   public boolean isLeaf(Object node) { return getChildCount(node) == 0; }
   public void valueForPathChanged(TreePath path, Object newValue) {}
   public void addTreeModelListener(TreeModelListener l) {}
   public void removeTreeModelListener(TreeModelListener l) {}

   private Document doc;
}

/**
   This class renders an XML node.
*/
class DOMTreeCellRenderer extends DefaultTreeCellRenderer
{  
   public Component getTreeCellRendererComponent(JTree tree, Object value, 
      boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
   {  
      Node node = (Node) value;
      if (node instanceof Element) return elementPanel((Element) node);

      super.getTreeCellRendererComponent(tree, value,
         selected, expanded, leaf, row, hasFocus);
      if (node instanceof CharacterData)
         setText(characterString((CharacterData) node));
      else
         setText(node.getClass() + ": " + node.toString());
      return this;
   }

   public static JPanel elementPanel(Element e)
   {
      JPanel panel = new JPanel();
      panel.add(new JLabel("Element: " + e.getTagName()));
      panel.add(new JTable(new AttributeTableModel(e.getAttributes())));
      return panel;      
   }
   
   public static String characterString(CharacterData node)
   {
      StringBuilder builder = new StringBuilder(node.getData());
      for (int i = 0; i < builder.length(); i++)
      {
         if (builder.charAt(i) == '\r') 
         {
            builder.replace(i, i + 1, "\\r");
            i++;
         }
         else if (builder.charAt(i) == '\n') 
         {
            builder.replace(i, i + 1, "\\n");
            i++;
         }
         else if (builder.charAt(i) == '\t') 
         {
            builder.replace(i, i + 1, "\\t");
            i++;
         }
      }
      if (node instanceof CDATASection)
         builder.insert(0, "CDATASection: ");
      else if (node instanceof Text)
         builder.insert(0, "Text: ");
      else if (node instanceof Comment)
         builder.insert(0, "Comment: ");
      
      return builder.toString();
   }
}

/** 
   This table model describes the attributes of an XML element.
*/
class AttributeTableModel extends AbstractTableModel
{  
   /**
      Constructs an attribute table model.
      @param map the named node map
   */
   public AttributeTableModel(NamedNodeMap map) { this.map = map; }

   public int getRowCount() { return map.getLength(); }
   public int getColumnCount() { return 2; }
   public Object getValueAt(int r, int c)
   {  
      return c == 0 ? map.item(r).getNodeName() : map.item(r).getNodeValue();
   }

   private NamedNodeMap map;
}


