package org.cloudbus.cloudsim.examples;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmScheduler;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisioner;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisioner;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisioner;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;

class CloudletCreator {
public ArrayList<Cloudlet> createRequiredCloulets(int numTasks, int brokerId)
	{
		ArrayList<Cloudlet> cloudletList = new ArrayList<Cloudlet>();
		Cloudlet task = null;
		for (int i = 0; i < numTasks; i++) {
				UtilizationModel utilizationModel = new UtilizationModelFull();
				long cloudletOutputSize = 200; 
				int pesNumber = 1;
				long len = 2000;
				
				Random rObj = new Random();
				
				long cloudletFileSize = 200;
				if(i == numTasks - 3)
					
					task = new Cloudlet(i, len *20, pesNumber ,cloudletFileSize , cloudletOutputSize , utilizationModel , utilizationModel,utilizationModel);
				else
					task = new Cloudlet(i, len + rObj.nextInt(1000) , pesNumber , cloudletFileSize ,cloudletOutputSize , utilizationModel , utilizationModel, utilizationModel);task.setUserId(brokerId);
					cloudletList.add(task);
											}
		return cloudletList;
	}
					}


	class VmsCreator {
			public List<Vm> createRequiredVms(int numVms, int brokerId) 
			{
					ArrayList<Vm> vmList= new ArrayList<Vm>();
					String vmm = "Xen";
					long size = 10000;
					long bw = 1000;
					int ram = 512;
					int numberOfPes = 1;
					double mips = 1000;
					for (int i = 0; i < numVms; i++) {
						vmList.add(new Vm(i, brokerId, mips, numberOfPes, ram, bw,size, vmm, new CloudletSchedulerSpaceShared()));
													 }
			return vmList;
			}
	}
	
	class FCFSBroker extends DatacenterBroker {
		public FCFSBroker(String name) throws Exception 
		{
			//calling superclass constructor
			super(name);
		}
		
		public void scheduleTaskstoVms(int numTasks, int numVms)
		{
			for (int i=0;i<numTasks;i++) {
				bindCloudletToVm(i, i%numVms);
			}
		}
	}
	
	public class Datacentre {
		//Task allocation on basis of first-come first-serve policy
		private static List<Cloudlet> cloudletList;
		
		private static List<Vm> vmList;
		
		private static int numTasks = 10;
		private static int numVms = 2;
		
			public static int main(String[] args) {
				
				Log.printLine("DATACENTRE MANAGEMENT USING FCFS Starting......");	
				//initialize CloudSim
				try {
				int numUser = 1;
				
				Calendar cal = Calendar.getInstance();
				boolean traceFlag = false;
				CloudSim.init(numUser , cal , traceFlag );
				//create one datacenter
				
				@SuppressWarnings("unused")
				Datacenter dc0 = createDatacenter("Datacenter_0");
				/*create Broker - abstraction of User
				 * we will create our own FCFSBroker that extends DatacenterBroker
				 */
				
				FCFSBroker brk0 = createBroker("Broker_0");
				int brokerId = brk0.getId();
				
				vmList = new VmsCreator().createRequiredVms(numVms, brokerId);
				brk0.submitVmList(vmList);
				
				cloudletList = new CloudletCreator().createRequiredCloulets(numTasks,brokerId);
				brk0.submitCloudletList(cloudletList);
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
				
				
				//This is the method that implements FcFs
				brk0.scheduleTaskstoVms(numTasks, numVms);
				CloudSim.startSimulation();
				CloudSim.stopSimulation();
				List<Cloudlet> receivedList = brk0.getCloudletReceivedList();
				
				//Print result
				printCloudletList(receivedList);
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
		
			private static FCFSBroker createBroker(String name)
				{
					FCFSBroker brk = null;
						try {
							brk = new FCFSBroker(name);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return brk;
				}
			private static Datacenter createDatacenter(String name)
				{
					Datacenter dc = null;
					//Datacenter consist of hosts
					ArrayList<Host> hostList = new ArrayList<Host>();
					//We are making a one host data center with a super powerful Pe
					ArrayList<Pe> peList = new ArrayList<Pe>();
					double mips = 1000*numVms;
					PeProvisioner peProvisioner = new PeProvisionerSimple(mips);
					peList.add(new Pe(0, peProvisioner));
					VmScheduler vmScheduler = new VmSchedulerTimeShared(peList);
					long storage = 1000000;
					long bw = 10000;
					BwProvisioner bwProvisioner = new BwProvisionerSimple(bw);
					int ram = 2048;
					RamProvisioner ramProvisioner = new RamProvisionerSimple(ram);
					int id = 0;
					hostList.add(new Host(id, ramProvisioner, bwProvisioner, storage, peList,vmScheduler));
					String architecture = "x64";
					String os = "Linux";
					String vmm = "Xen";
					double timeZone = 10;
					double costPerSec = 3;
					double costPerMem = 0.05;
					double costPerStorage = 0.001;
					DatacenterCharacteristics characteristics = new DatacenterCharacteristics(architecture , os , vmm , hostList, timeZone , costPerSec ,costPerMem , costPerStorage, costPerStorage);
					VmAllocationPolicy vmAllocationPolicy = new
							VmAllocationPolicySimple(hostList);
					List<Storage> storageList = new ArrayList<Storage>();
					try {
						dc = new Datacenter(name, characteristics, vmAllocationPolicy ,storageList , 0);
					} catch (Exception e) {
						e.printStackTrace();
					}
					return dc;
				}
			private static void printCloudletList(List<Cloudlet> list) 
			{
				
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
				Log.printLine("============================================================== OUTPUT ===============================================================");
				Log.printLine("\n");
				Log.printLine("Cloudlet ID" + indent + "STATUS" + indent + "Data center ID" + indent + "VM ID" + indent +"Length"+ indent +indent + "Time" + indent + "Start Time" + indent + "Finish Time" + indent +"Turn Around Time" + indent  + "Waiting Time");
				Log.printLine("----------------------------------------------------------------------------------------------------------------------------------");
				DecimalFormat dft = new DecimalFormat("###.##");
				for (int i = 0; i < size; i++) {
					cloudlet = list.get(i);
					Log.print(indent + cloudlet.getCloudletId() + indent + indent);
					if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
						Log.print("SUCCESS");
						String TurnAround = dft.format(cloudlet.getFinishTime() -
								cloudlet.getSubmissionTime());
						String Waiting = dft.format(cloudlet.getFinishTime()-
								cloudlet.getSubmissionTime()-cloudlet.getActualCPUTime());
						Log.printLine( indent + indent + cloudlet.getResourceId()+ indent + indent + indent + indent +cloudlet.getVmId() + indent + indent +cloudlet.getCloudletLength() + indent + indent +dft.format(cloudlet.getActualCPUTime()) +indent + indent +dft.format(cloudlet.getExecStartTime())+indent + indent + indent +dft.format(cloudlet.getFinishTime()) +indent + indent + indent + indent + TurnAround + indent+ indent + indent + indent +Waiting);
						//Log.printLine("----------------------------------------------------------------------------------------------------------------------------------");
					}
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
									indent + indent +indent + dft.format(cloudlet.getActualCPUTime()) + indent + indent + dft.format(cloudlet.getExecStartTime())+
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