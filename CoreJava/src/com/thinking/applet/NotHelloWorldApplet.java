package com.thinking.applet;

/*
  The following HTML tags are required to display this applet in a browser:
  <applet code="NotHelloWorldApplet.class" width="300" height="100">
  </applet>
*/

import javax.swing.*;

public class NotHelloWorldApplet extends JApplet
{
   public void init()
   {
      JLabel label = new JLabel("Not a Hello, World applet", SwingConstants.CENTER);
      add(label);
   }
}
