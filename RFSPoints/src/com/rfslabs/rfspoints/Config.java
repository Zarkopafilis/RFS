package com.rfslabs.rfspoints;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Config
{
  public static void createFile()
  {
    Properties prop = new Properties();
    try
    {
      prop.setProperty("server", "db4free.net");
      prop.setProperty("database", "rfstest");
      prop.setProperty("dbuser", "padakoys");
      prop.setProperty("dbpassword", "test123");
      

      prop.store(new FileOutputStream("plugins/OnlineEx/config.properties"), null);
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }
  
  public static void loadFile()
  {
    Properties prop = new Properties();
    try
    {
      prop.load(new FileInputStream("plugins/OnlineEx/config.properties"));
      

      OnlineExMain.serv = prop.getProperty("server");
      OnlineExMain.db = prop.getProperty("database");
      OnlineExMain.user = prop.getProperty("dbuser");
      OnlineExMain.pass = prop.getProperty("dbpassword");
      
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }
}

