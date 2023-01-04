package edu.toronto.cs.util.gui;

import java.util.*;
import java.io.*;
import javax.swing.*;

import javax.swing.filechooser.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;



/**
 ** A panel for inputting a file.
 **/
public class FilePanel extends JPanel
{
  JLabel label;
  JTextField filename;
  JButton browse;
  JPanel browsePanel;
  JPanel labelPanel;
  boolean withLabel;
  String dir;

  JFileChooser filechooser;
  
  //leave open the possibility of no directory as in CTL
  public FilePanel (String text, JFileChooser fc)
  {
    super ();
    init (text, fc, java.lang.System.getProperty("user.dir"));
  }
  public FilePanel (String text, JFileChooser fc, String _dir)
  {
    super ();
    init(text, fc, _dir);
    
  }
  
  void init (String text, JFileChooser fc, String _dir)
  {

    filechooser = fc;
    dir = _dir;

    // set up the components
    if (text.length () != 0)  //fixed used to be ==
      {
	withLabel = true;
	label = new JLabel (text);
	labelPanel = new JPanel();
	labelPanel.setLayout (new BoxLayout (labelPanel, BoxLayout.X_AXIS));
      }
    else
      withLabel = false;
    
    
    

    filename = new JTextField (30);
    browse = new JButton ("Browse...");
    browsePanel = new JPanel();
    browsePanel.setLayout (new BoxLayout (browsePanel, BoxLayout.X_AXIS));

    // Stupid hack.
    final FilePanel thisthis = this;
    
    // set up the action listener for the button
    browse.addActionListener (getFileGlueListener ());

    filename.getDocument ().addDocumentListener (new DocumentListener ()
      {
	public void changedUpdate(DocumentEvent e) 
	{
	  filename.postActionEvent ();
	}

	public void insertUpdate(DocumentEvent e) 
	{
	  changedUpdate (e);
	}
	
	public void removeUpdate(DocumentEvent e) 
	{
	  changedUpdate (e);
	}
      });
    
    // set the layout and put all of it together
    setLayout (new BoxLayout (this, BoxLayout.Y_AXIS));
    if (withLabel)
      {
	labelPanel.add (label);
	//	labelPanel.add (StandardFiller.makeHstrut ());
	labelPanel.add (Box.createHorizontalGlue());
	
      }
    browsePanel.add (filename);
    browsePanel.add (StandardFiller.makeHstrut ());
    browsePanel.add (browse);
    browsePanel.add (Box.createHorizontalGlue());

    if (withLabel)
      add(labelPanel);
    
    add(browsePanel);

    
  }

  public void setFileName (String name)
  {
    filename.setText (name);
  }

  public String getFileName ()
  {
    return filename.getText ();
  }
  
  public void addActionListener (ActionListener l)
  {
    filename.addActionListener (l);
  }

  public void removeActionListener (ActionListener l)
  {
    filename.removeActionListener (l);
  }

  public Document getDocument ()
  {
    return filename.getDocument ();
  }
  
  // Factory method to be overridden
  protected ActionListener getFileGlueListener ()
  {
    return new ActionListener ()
       {
	 public void actionPerformed (ActionEvent e)
	   {
	     String dirSet;
	     File dirFile;
	     
	     //Set the directory the Browse Button will open to
	     if (getFileName() == "")
	       dirFile = new File (dir);
	     else
	       dirFile = new File (getFileName ());
	     
	     filechooser.setSelectedFile (dirFile);
	     
	     
	     
	     //System.out.println ("Got action: " + e);
	     // get the file  
	     if(withLabel)
	       filechooser.setDialogTitle (label.getText ());
	     int result = filechooser.showOpenDialog (null);
	     if (result == JFileChooser.APPROVE_OPTION)
	       filename.setText (filechooser.getSelectedFile ().
				 getAbsolutePath ());
	     //filename.postActionEvent ();
	   }
      };
  }
  
}
