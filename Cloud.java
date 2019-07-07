package org.cloudbus.cloudsim.examples;
import java.util.Scanner;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.examples.Ccproject;
import org.cloudbus.cloudsim.examples.Datacentre;

public class Cloud {
	
	
	public static void main(String[] args) {
		Scanner S1 =new Scanner(System.in);
		int a=1;
		while(a!=3) {
			Log.printLine("\n\n1. SJF\n2. FCFS \n3. Exit \n");
			a = S1.nextInt();
		switch(a)
		{
			case 1: Ccproject cc = new Ccproject();
					int x=cc.main(args);
				break;
			case 2: Datacentre d = new Datacentre();
					int y =d.main(args);
				break;
			case 3: Log.printLine("Exiting..............................................................");
			    break;
	
		}
		}
	}

}
