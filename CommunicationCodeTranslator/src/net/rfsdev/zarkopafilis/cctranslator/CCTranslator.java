package net.rfsdev.zarkopafilis.cctranslator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

public class CCTranslator {

	private Properties greek , eng;
	public boolean english;
	
	public CCTranslator(){
		
		english = true;
		
		greek = new Properties();
		eng = new Properties();
		
		
		try {
			greek.load(new FileInputStream("res" + File.separator + "greek.properties"));
			eng.load(new FileInputStream("res" + File.separator + "eng.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public String doIt(String phrase , boolean encrypt){
		
		Properties p;
		
		if(english){
			p = eng;
		}else{
			p = greek;
		}
		
		String s = phrase;
		
		Enumeration e = p.propertyNames();
		
		while(e.hasMoreElements()){
			String key = (String) e.nextElement();
			String value = p.getProperty(key);
			if(encrypt){
				s = s.replaceAll(key, value);
			}else{
				s = s.replaceAll(value , key);
			}
		}
		return s;
	}
	
}
