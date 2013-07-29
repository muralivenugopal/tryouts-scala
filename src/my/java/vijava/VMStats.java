/*package my.java.vijava;

import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.api.rest.schema.extension.VimObjectRefType;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.VM;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;
import com.vmware.vcloud.sdk.VirtualDisk;
import com.vmware.vcloud.sdk.constants.Version;
import com.vmware.vim25.GuestDiskInfo;
import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.GuestNicInfo;
import com.vmware.vim25.GuestScreenInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfEntityMetricCSV;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.VirtualMachineSummary;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class VMStats {	
	private static ManagedEntity[] mes;

	public static void main(String args[]) throws Exception {

		// vCloud Director login
		System.out.println("Logging into VCD...");
		VcloudClient.setLogLevel(Level.OFF);
		VcloudClient vcloudClient = new VcloudClient("https://my.vcloud.server.com", Version.V1_5);
		vcloudClient.registerScheme("https", 443, FakeSSLSocketFactory.getInstance());
		vcloudClient.login("administrator@System", "password");

		// vCenter login
		System.out.println("Logging into vCenter...");
		ServiceInstance si = new ServiceInstance(new URL("https://my.vcenter.server.com/sdk"), "administrator", "password", true);

		// Grab data?
		Folder rootFolder = si.getRootFolder();
		System.out.println("Gathering VM details from vCenter root folder: " + rootFolder.getName());
		InventoryNavigator nav = new InventoryNavigator(rootFolder);
		mes = nav.searchManagedEntities("VirtualMachine");

		// Look up the VMs
		HashMap<String, ReferenceType> orgsList = vcloudClient.getOrgRefsByName();
		for (ReferenceType orgRef : orgsList.values()) {
			for (ReferenceType vdcRef : Organization
					.getOrganizationByReference(vcloudClient, orgRef)
					.getVdcRefs()) {
				Vdc vdc = Vdc.getVdcByReference(vcloudClient, vdcRef);
				System.out.println("\n==> VDC : " + vdcRef.getName() + " : " + vdc.getResource().getAllocationModel());

				for (ReferenceType vAppRef : Vdc.getVdcByReference(
						vcloudClient, vdcRef).getVappRefs()) {
					System.out.println("\nvApp : " + vAppRef.getName());
					Vapp vapp = Vapp.getVappByReference(vcloudClient, vAppRef);
					List<VM> vms = vapp.getChildrenVms();
					for (VM vcd_vm : vms) {

						// Show the data you can get from VCD

						System.out.println("\n	        VM Name : " + vcd_vm.getResource().getName());
						System.out.println("	     Local Name : " + vcd_vm.getResource().getVAppScopedLocalId());
						System.out.println("	         Status : " + vcd_vm.getVMStatus());
						System.out.println("	            CPU : " + vcd_vm.getCpu().getNoOfCpus());
						System.out.println("	         Memory : " + vcd_vm.getMemory().getMemorySize() + " Mb");
						
						for (VirtualDisk disk : vcd_vm.getDisks())
							if (disk.isHardDisk())
								System.out.println("	       HardDisk : " + disk.getHardDiskSize() + " Mb");

						VimObjectRefType vimRef= vcd_vm.getVMVimRef();
						String moRef = vimRef.getMoRef();
						System.out.println("	          MORef : " + moRef );

						// Show the basic data you can get from vSphere

						VirtualMachine vsphere_vm = getVmFromMor(moRef); // Crosswalk the id from VCD to vSphere

						VirtualMachineConfigInfo vminfo = vsphere_vm.getConfig();
						VirtualMachineCapability vmc = vsphere_vm.getCapability();

						System.out.println("	   vCenter name : " + vsphere_vm.getName());
						System.out.println("	        GuestOS : " + vminfo.getGuestFullName());
						System.out.println("	     Multisnaps : " + vmc.isMultipleSnapshotsSupported());

						// Some of the QuickStats data from vSphere
						// Details here: http://vijava.sourceforge.net/vSphereAPIDoc/ver5/ReferenceGuide/vim.vm.Summary.QuickStats.html

						VirtualMachineSummary summary = vsphere_vm.getSummary();
						VirtualMachineQuickStats quickStats = summary.getQuickStats();

						System.out.println("	Guest Mem Usage : " + quickStats.getGuestMemoryUsage() + " MB");
						System.out.println("	         Uptime : " + quickStats.uptimeSeconds + " seconds");
						System.out.println("	      CPU usage : " + quickStats.getOverallCpuUsage() + " Mhtz");
						
						// Some of the VirtualMachineRuntimeInfo data from vSphere
						// Details here: http://www.vmware.com/support/developer/vc-sdk/visdk25pubs/ReferenceGuide/vim.vm.RuntimeInfo.html
						
						VirtualMachineRuntimeInfo runTime = summary.getRuntime();
						
						System.out.println("	      Boot Time : " + runTime.bootTime );
						System.out.println("	           Host : " + runTime.host );
						System.out.println("	        Max CPU : " + runTime.maxCpuUsage );
						System.out.println("	        Max Mem : " + runTime.maxMemoryUsage );
						
						// Some of the GuestInfo from vSphere
						// Details here: http://www.vmware.com/support/developer/vc-sdk/visdk25pubs/ReferenceGuide/vim.vm.GuestInfo.html#field_detail
						
						GuestInfo guestInfo = vsphere_vm.getGuest();
						
						System.out.println("	        Os Info : " + guestInfo.guestFullName );
						System.out.println("	     IP Address : " + guestInfo.ipAddress );
						System.out.println("	   Tools Status : " + guestInfo.toolsStatus );
						System.out.println("	  Tools version : " + guestInfo.toolsVersion );

						if ( guestInfo.getToolsStatus().toString() != "toolsNotRunning" ) {
							System.out.println("	    Screen size : " + guestInfo.getScreen().getWidth() + "x" + guestInfo.getScreen().getHeight() );
						}						
						
						// Some of the GuestDiskInfo data from vSphere
						// Details here: http://www.vmware.com/support/developer/vc-sdk/visdk25pubs/ReferenceGuide/vim.vm.GuestInfo.DiskInfo.html
						
						GuestDiskInfo[] diskInfo = guestInfo.getDisk();
						if ( diskInfo != null && diskInfo.length > 0 ) { 
							GuestDiskInfo firstDisk = diskInfo[0];

							System.out.println("	  Disk0 capcity : " + firstDisk.capacity + " MB");
							System.out.println("	     Disk0 Free : " + firstDisk.capacity + " Free");
							System.out.println("	      Disk Path : " + firstDisk.diskPath );
						}
						
						// Some of the GuestNicInfo data from vSphere
						// Details here: http://www.vmware.com/support/developer/vc-sdk/visdk25pubs/ReferenceGuide/vim.vm.GuestInfo.NicInfo.html
						
						GuestNicInfo[] nicInfo = guestInfo.getNet();
						if ( nicInfo != null && nicInfo.length > 0 ) { 
							GuestNicInfo firstNic = nicInfo[0];

							System.out.println("	 Nic0 Connected : " + firstNic.connected );
							System.out.println("	        Nic0 IP : " + firstNic.getIpAddress()[0] );
							System.out.println("	  Nic0 Mac Addr : " + firstNic.macAddress );
							System.out.println("	   Nic0 Network : " + firstNic.network );
						}
						
								
						// Show data collected from PerfManager() in vSphere
						// This is a bit more complex

						if ( vcd_vm.getVMStatus().toString() == "POWERED_ON" ) { // Don't bother if the VM is turned off.

							PerformanceManager perfMgr = si.getPerformanceManager();

							PerfMetricId[] pmis = perfMgr.queryAvailablePerfMetric(vsphere_vm, null, null, 1800);

							PerfQuerySpec qSpec = new PerfQuerySpec();
							qSpec.setMetricId(pmis);
							qSpec.setEntity(vsphere_vm.getMOR());
							qSpec.setFormat("normal");

							// Finally, gather the data
							PerfEntityMetricBase[] pValues = perfMgr.queryPerf(
									new PerfQuerySpec[] {qSpec});
							if(pValues != null)
							{
								displayValues(pValues);
							}
						}
					}
				}
			}
		}
		System.out.println("\nDone.\n");
		si.getServerConnection().logout(); // logout of vCenter.
	}

	private static VirtualMachine getVmFromMor(String morName) {
		for(int i=0; i<mes.length; i++) {
			if ( mes[i].getMOR().get_value().equals(morName) ) { return (VirtualMachine) mes[i];
			}
		}
		System.out.println("VM NOT FOUND?!");
		return null;

	}

	static void displayValues(PerfEntityMetricBase[] values)
	{
		for(int i=0; i<values.length; ++i) 
		{
			String entityDesc = values[i].getEntity().getType() + ":" + values[i].getEntity().get_value();
			System.out.println("\nEntity: " + entityDesc);
			if(values[i] instanceof PerfEntityMetric)
			{
				printPerfMetric((PerfEntityMetric)values[i]);
			}
			else if(values[i] instanceof PerfEntityMetricCSV)
			{
				printPerfMetricCSV((PerfEntityMetricCSV)values[i]);
			}
			else
			{
				System.out.println("UnExpected sub-type of PerfEntityMetricBase.");
			}
		}
	}

	static void printPerfMetric(PerfEntityMetric pem)
	{
		PerfMetricSeries[] vals = pem.getValue();
		PerfSampleInfo[]  infos = pem.getSampleInfo();

		System.out.println("Sampling Times and Intervals:");
		for(int i=0; infos!=null && i <infos.length; i++)
		{
			System.out.println("Sample time: " + infos[i].getTimestamp().getTime());
			System.out.println("Sample interval (sec):" + infos[i].getInterval());
		}
		System.out.println("Sample values:");
		for(int j=0; vals!=null && j<vals.length; ++j)
		{
			System.out.println("\nPerf counter ID: " + vals[j].getId().getCounterId());
			System.out.println("Device instance ID: " + vals[j].getId().getInstance());

			if (vals[j] instanceof PerfMetricIntSeries)
			{
				PerfMetricIntSeries val = (PerfMetricIntSeries) vals[j];
				long[] longs = val.getValue();
				for (int k=0; k<longs.length; k++) 
				{
					System.out.print(longs[k] + " ");
				}
				System.out.println(" (Total:"+longs.length + ")");
			}
			else if(vals[j] instanceof PerfMetricSeriesCSV)
			{ // it is not likely coming here...
				PerfMetricSeriesCSV val = (PerfMetricSeriesCSV) vals[j];
				System.out.println("CSV value:" + val.getValue());
			}
		}
	}

	static void printPerfMetricCSV(PerfEntityMetricCSV pems)
	{
		System.out.println("SampleInfoCSV:" 
				+ pems.getSampleInfoCSV());
		PerfMetricSeriesCSV[] csvs = pems.getValue();
		for(int i=0; i<csvs.length; i++)
		{
			System.out.println("PerfCounterId:" 
					+ csvs[i].getId().getCounterId());
			System.out.println("CSV sample values:" 
					+ csvs[i].getValue());
		}
	}
}

*/