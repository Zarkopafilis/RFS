package net.rfsdev.zarkopafilis;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Data {

	private Data(){
		throw new AssertionError();
	}
	
	private static List<Question> questions = new ArrayList<Question>();
	private static final Random r = new Random();
	
	public static void init() throws Exception{
		
		File directory = new File("questions");
		
		for(File questionFile : directory.listFiles()){
			if(questionFile.getName().endsWith(".properties")){
				Properties pr = new Properties();
				pr.load(new FileInputStream(questionFile));
				
				questions.add(new Question(pr.getProperty("question"),
						pr.getProperty("answer1") , pr.getProperty("answer2") , pr.getProperty("answer3") , pr.getProperty("answer4"),
						Integer.parseInt(pr.getProperty("correct"))));
				
			}
		}
		
	}
	
	public static Question getRandomQuestion(){
		
		if(questions.size() == 0){
			return null;
		}
		
		int questionNumber = r.nextInt(questions.size());//IKR not good performance but come on...
		
		Question q = questions.get(questionNumber);
		
		return q;
		
	}
	
}