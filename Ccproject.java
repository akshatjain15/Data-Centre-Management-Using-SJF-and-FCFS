package org.cloudbus.cloudsim.examples;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

public class Ccproject {
	private static List<Cloudlet> cloudletList;


	private static List<Vm> vmlist;
	
	private static List<Vm> vmlist1;
	
	private static List<Vm> vmlist2;
	
	static int vmid=0;
	
	public static int main(String[] args) {
	
	Log.printLine("DATACENTRE MANAGEMENT USING SJF Starting..");

	try {
		
		int num_user = 1;   // number of grid users
		Calendar calendar = Calendar.getInstance();
		boolean trace_flag = false;  // mean trace events

		
		CloudSim.init(num_user, calendar, trace_flag);

	
		@SuppressWarnings("unused")
		Datacenter datacenter0 = createDatacenter("Primary_Datacenter");
		
		@SuppressWarnings("unused")
		Datacenter datacenter1 = createDatacenter("Secondary_Datacenter");
		
		DatacenterBroker broker=createBroker();
		int brokerId=broker.getId();
		
		vmlist = createLowVM(brokerId,3,1);
		
		vmlist1=createLowVM(brokerId,5,2);
		
		vmlist2=createLowVM(brokerId,5,3);
		
		broker.submitVmList(vmlist);
		broker.submitVmList(vmlist1);
		broker.submitVmList(vmlist2);
		
		cloudletList=createCloudlet(brokerId, 40);
		
		broker.submitCloudletList(cloudletList);
		String anim= "LOADING";
        for (int x =0 ; x <7  ; x++) {
            String data = "   " + anim.charAt(x % anim.length()) + " " ;
            System.out.write(data.getBytes());
            Thread.sleep(100);
        }
        String anim1= ".......";
        for (int x =0 ; x <12  ; x++) {
            String data = "   " + anim1.charAt(x % anim1.length()) + " " ;
            System.out.write(data.getBytes());
            Thread.sleep(100);
        }
		CloudSim.startSimulation();
		
		List<Cloudlet> newList = broker.getCloudletReceivedList();
		
		CloudSim.stopSimulation();
		
		printCloudletList(newList);
		 String anim3="========================================THANK YOU===================================================";
	        for (int x =0 ; x <anim3.length()  ; x++) {
	            String data = "" + anim3.charAt(x % anim3.length()) ;
	            System.out.write(data.getBytes());
	            Thread.sleep(50);
	        }
	        
		
	}
	catch (Exception e)
	{
		e.printStackTrace();
		Log.printLine("The simulation has been terminated due to an unexpected error");
	}
	return 0;
	}
	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	private static Datacenter createDatacenter(String name){

		
		List<Host> hostList = new ArrayList<Host>();

		
		List<Pe> peList1 = new ArrayList<Pe>();

		int mips = 4500;

		
		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

	
		List<Pe> peList2 = new ArrayList<Pe>();

		peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

		
		int hostId=0;
		int ram = 2048; //host memory (MB)
		long storage = 1000000; //host storage
		int bw = 10000;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerTimeShared(peList1)
    			)
    		); // This is our first machine

		hostId++;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList2,
    				new VmSchedulerTimeShared(peList2)
    			)
    		); // Second machine



		
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}
	private static List<Vm> createLowVM(int userId, int vms,int id) {

		
		LinkedList<Vm> list = new LinkedList<Vm>();

		//VM Parameters
		long size = 10000; //image size (MB)
		int ram = 512; //vm memory (MB)
		int mips = 500;
		long bw = 1000;
		int pesNumber = 1; //number  of cpus
		String vmm = "Xen"; //VMM name

		//create VMs
		Vm[] vm = new Vm[vms];
		if(id==1)
		{
		for(int i=0;i<vms;i++){
			vm[i] = new Vm(vmid, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
			
			list.add(vm[i]);
			
			++vmid;
		}

		
		}
		else if(id==2)
		{
		for(int i=0;i<vms;i++){
			vm[i] = new Vm(vmid, userId, mips*2,2,ram,bw, size, vmm, new CloudletSchedulerSpaceShared());
			
			list.add(vm[i]);
			
			++vmid;
		}

		
		}
		else if(id==3)
		{
		
		for(int i=0;i<vms;i++){
			vm[i] = new Vm(vmid, userId, mips*3, 3, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
			
			list.add(vm[i]);
			++vmid;
		}

		
		}
		return list;
	}

private static List<Cloudlet> createCloudlet(int userId, int cloudlets){
	
	LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

	
	long length = 1000;
	long fileSize = 300;
	long outputSize = 300;
	int pesNumber = 1;
	UtilizationModel utilizationModel = new UtilizationModelFull();

	Cloudlet[] cloudlet = new Cloudlet[cloudlets];

	for(int i=0;i<cloudlets;i++){
		Random rObj = new Random();
		cloudlet[i] = new Cloudlet(i, (length + rObj.nextInt(3000)), pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
		
		cloudlet[i].setUserId(userId);
		
		list.add(cloudlet[i]);
	}

	return list;
}
private static void printCloudletList(List<Cloudlet> list) {
	int size = list.size();
	Cloudlet cloudlet;

	String indent = "    ";
	try{
		Log.printLine("Progress...");
		Thread.sleep(1000);
		 String anim1= " PLEASE WAIT ......\n\n";
	        for (int x =0 ; x <12  ; x++) {
	            String data = "   " + anim1.charAt(x % anim1.length()) + " " ;
	            Log.print(data);
	            Thread.sleep(100);
	        }
	}catch (InterruptedException e) {
		e.printStackTrace();
	}
	Log.printLine("\n");
Log.printLine();
	Log.printLine("========================================== OUTPUT ==========================================");
	Log.printLine("\n");
	Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +"CLOUDLET LENGTH"+indent+
			"Data center ID" + indent + "VM ID" + indent + indent + "  Time" + indent + " Start Time" + indent + " Finish Time" + indent +" Waiting time ");
	Log.printLine("----------------------------------------------------------------------------------------------------------------------------------");
	DecimalFormat dft = new DecimalFormat("###.##");
	for (int i = 0; i < size; i++) {
		cloudlet = list.get(i);
		String x=String.valueOf(cloudlet.getCloudletId());
		if(x.length()==1)
		{
		Log.print(indent + "0"+cloudlet.getCloudletId() + indent + indent);
		}
		else
		{
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);
		}

		if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
			Log.print("SUCCESS"+indent+indent+cloudlet.getCloudletLength()+indent+indent);
			if(String.valueOf(cloudlet.getVmId()).length()==1)
			{
			Log.print(indent+indent + cloudlet.getResourceId() + indent + indent + indent + " "+"0"+cloudlet.getVmId());
			}
			else
			{
				Log.print(indent+indent + cloudlet.getResourceId() + indent + indent + indent + " "+cloudlet.getVmId());
		
			}
			
			Log.print(indent + indent + indent + new DecimalFormat("##.##").format(cloudlet.getActualCPUTime()));
			
			Log.print(indent + indent + indent + new DecimalFormat("##.##").format(cloudlet.getExecStartTime()));
			Log.printLine(indent + indent + indent + new DecimalFormat("##.##").format(cloudlet.getFinishTime())+indent+indent+ new DecimalFormat("##.##").format(cloudlet.getWaitingTime()));
			//Log.printLine("----------------------------------------------------------------------------------------------------------------------------------");
		}
	}
	
	
	try {
		Thread.sleep(2000);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	Log.printLine("\n\n\n");
	for (int i = 0; i < size; i++) {
		Log.printLine("========================================== OUTPUT ==========================================");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
					"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
		cloudlet = list.get(i);
		Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
						indent + indent + dft.format(cloudlet.getFinishTime()));
			
			}
			Log.printLine("============================================================================================");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.print("\n\n\n\n\n");

		}

}
}
