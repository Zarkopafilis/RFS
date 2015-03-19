package com.rfslabs.rfsdom.multithreading;

import com.rfslabs.rfsdom.util.LoopingUtil;

public class OutpostLooper implements Runnable{

	@Override
	public void run() {
		
		
		LoopingUtil.updatePlayersOnOutposts();
		
		
		LoopingUtil.updateOutpostsStatus();
		
	}

	
	
}
