package my.java.vijava;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import com.vmware.vim25.GuestDiskInfo;
import com.vmware.vim25.GuestInfo;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;

public class DiskDetails {

	/**
	 * @param args
	 * @throws MalformedURLException
	 * @throws RemoteException
	 */
	public static void main(String[] args) throws RemoteException,
			MalformedURLException {

		// ServiceInstance si = new ServiceInstance(new
		// URL("https://119.227.201.80/sdk"), "root", "cl0udl@b", true);
		ServiceInstance si = new ServiceInstance(new URL(
				"https://119.227.201.83/sdk"), "CIDev", "Cl0udde^", true); // vsphere
																			// login

		// list all the vms

		Folder rootFolder = si.getRootFolder();
		ManagedEntity[] vms = new InventoryNavigator(rootFolder)
				.searchManagedEntities(new String[][] { { "VirtualMachine",
						"name" }, }, true);
		
		

		for (int i = 0; i < vms.length; i++) {
			System.out.println(i + " :: name : " + vms[i].getName() );
			System.out.println("UUID : " + ((VirtualMachine)vms[i]).getConfig().uuid );
			GuestDiskInfo[] gdis = ((VirtualMachine) vms[i]).getGuest().getDisk();
//			String uuid = ((VirtualMachine) vms[i]).get
			if (gdis != null) {
				for (int j = 0; j < gdis.length; j++) {
					float cap = gdis[j].capacity / (1024 * 1024 * 1024);
					float free = gdis[j].freeSpace / (1024 * 1024 * 1024);
					System.out.println(gdis[j].diskPath + " : " + "(" + gdis[j].capacity + " b ) "+ cap + " : " + "(" + gdis[j].freeSpace + " b ) "+ free);
				}
			}
		}

		/*
		 * // collect available parameters String vmname = "clab-vcenter01";
		 * ManagedEntity vm = new InventoryNavigator(si.getRootFolder())
		 * .searchManagedEntity("VirtualMachine", vmname);
		 * 
		 * System.out.println("vm details " + vm.getName());
		 * 
		 * VirtualMachine vmi = (VirtualMachine) vm;
		 * 
		 * GuestInfo gi = vmi.getGuest();
		 * 
		 * GuestDiskInfo[] gdis = gi.getDisk();
		 * 
		 * System.out.println(gdis.length);
		 * 
		 * System.out.println(gdis[0].diskPath + " : " + gdis[0].capacity +
		 * " : " + gdis[0].freeSpace);
		 */

		// ManagedEntity[] vms = new InventoryNavigator(rootFolder)

		/*
		 * if (vm == null) { System.out.println("Virtual Machine " + vmname +
		 * " cannot be found."); si.getServerConnection().logout(); return; }
		 */

	}
}
