package com.example.android.dragrace;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import java.util.Random;
import android.os.AsyncTask;

public class XmasTree extends LinearLayout  {

	StageBulb staging;
	Bulb yellow1;
	Bulb yellow2;
	Bulb yellow3;
	Bulb green;
	Bulb red;

	Random rnd = new Random();
	boolean proTree = false;
	boolean raceStarted;

	long stageTime;
	long greenTime;
	long actualGreenTime;
	long nextStepTime;
	long treeTime;
    long startTime;

	public enum TreeState {
		AwaitingStart,
		Yellow1,
		Yellow2,
		Yellow3,
		YellowAll,
		Green,
		Red	
	}

	TreeState state = TreeState.AwaitingStart;

	public XmasTree(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void init() {
		
	   staging = (StageBulb) findViewById(R.id.staging);
	   yellow1 = (Bulb) findViewById(R.id.yellow1);
	   yellow2 = (Bulb) findViewById(R.id.yellow2);
	   yellow3 = (Bulb) findViewById(R.id.yellow3);
	   green = (Bulb) findViewById(R.id.green);
	   red = (Bulb) findViewById(R.id.red);
	   
	   resetTree();
    }

	public void resetTree() {
		
		 staging.setLamp(false);
		 yellow1.setLamp(false);
		 yellow2.setLamp(false);
		 yellow3.setLamp(false);
		 green.setLamp(false);
		 red.setLamp(false);
	}

	public void stageTree() {
		 staging.setLamp(true);
		 yellow1.setLamp(false);
		 yellow2.setLamp(false);
		 yellow3.setLamp(false);
		 green.setLamp(false);
		 red.setLamp(false);
	}
	
    public void proYellow() {
		 yellow1.setLamp(true);
		 yellow2.setLamp(true);
		 yellow3.setLamp(true);
		 green.setLamp(false);
		 red.setLamp(false);
    }

    public void goGreen() {
		 staging.setLamp(false);
		 yellow1.setLamp(false);
		 yellow2.setLamp(false);
		 yellow3.setLamp(false);
		 green.setLamp(true);
		 red.setLamp(false);
    }

    public void goRed() {
		 staging.setLamp(false);
		 yellow1.setLamp(false);
		 yellow2.setLamp(false);
		 yellow3.setLamp(false);    	
		 green.setLamp(false);
		 red.setLamp(true);
    }

    public void cueStart() {

        stageTime = System.currentTimeMillis();
    	nextStepTime = stageTime + 800L + (long) (500.0f * rnd.nextFloat());
    	state = TreeState.AwaitingStart;
		raceStarted = false;
    	stageTree();
    	Thread runner = new Thread(new TimeMonitor());
    	runner.start();
    }


    public long checkTime() {
    	long now = System.currentTimeMillis();

    	if(nextStepTime > now) return nextStepTime - now;
    	switch (state) {

    	case AwaitingStart:
    		
    		treeTime = now;
    		if(proTree) {
    			greenTime = treeTime + 400;
    			nextStepTime = greenTime;
    			state = TreeState.YellowAll;
    			proYellow();
    			
    		} else {
    			greenTime = treeTime + 1500;
    			nextStepTime = treeTime + 500;
    			state = TreeState.Yellow1;
    			yellow1.setLamp(true);
    		}
    		actualGreenTime = greenTime;
    		break;
 
    	case Yellow1:

			nextStepTime = treeTime + 1000;
			state = TreeState.Yellow2;
			yellow1.setLamp(false);
			yellow2.setLamp(true);
    		break;
    	
    	case Yellow2:

    		nextStepTime = treeTime + 1500;
			state = TreeState.Yellow3;
			yellow2.setLamp(false);
			yellow3.setLamp(true);
    		break;

    	case Yellow3:
    	case YellowAll:
 
     	   state = TreeState.Green;
    	   actualGreenTime = now;
   		   green.setLamp(true);
   		   yellow1.setLamp(false);
   		   yellow2.setLamp(false);
   		   yellow3.setLamp(false);
    	   break;

    	default:
    		nextStepTime = now;
    		break;

    	}

    	return nextStepTime - now;
    }

    private class TimeMonitor implements Runnable  {
		
		 public void run() {
			while (true) {
			long dT = checkTime();

			if(dT <= 0) return;
			try {
				Thread.sleep(dT);
			} catch (InterruptedException e) {
				e.printStackTrace();
				return;
			}
		}
    }
    };

    public void startRace() {
    	raceStarted = true;

    	startTime = System.currentTimeMillis();
    	// We were scoring some 0.000 reaction times as red, so
    	// make sure RT is at least -0.001 to prevent that.
    	if(state != TreeState.Green && startTime < actualGreenTime) {
    		state = TreeState.Red;
    		goRed();
    	}
    	staging.setLamp(false);
    }
    
    public TreeState getTreeState() {
    	return state;
    }

    // Return true if the tree has begun timing (the top yellow indicator is ON)
    // AND if a startRace() has not already occurred in this cycle.
    public boolean getTreeActive() {
    	return !raceStarted && state != TreeState.AwaitingStart;
    }

    public long getRT() {
    	
    	return startTime - actualGreenTime;	
    }

    public void setProTree(boolean proTree) {
    	this.proTree = proTree;
    }

    public boolean getProTree() {
    	return proTree;
    }
}