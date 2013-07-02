package my.java.vijava;

import java.net.URL;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

public class PrintInventory 
{
	public static void main(String[] args) throws Exception
	{

		ServiceInstance si = new ServiceInstance(new URL("https://119.227.201.80/sdk"), "root", "cl0udl@b", true); // cloud lab login
//		ServiceInstance si = new ServiceInstance(new URL("https://119.227.201.83/sdk"), "CIDev", "Cl0udde^", true); // vsphere login
		Folder rootFolder = si.getRootFolder();
		
		System.out.println("============list params=============");
		
		System.out.println("============ Data Centers ============");
		ManagedEntity[] dcs = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"Datacenter", "name" }, }, true);
		for(int i=0; i<dcs.length; i++)
		{
			System.out.println("Datacenter["+i+"]=" + dcs[i].getName());
		}
		
		System.out.println("\n============ Virtual Machines ============");
		ManagedEntity[] vms = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"VirtualMachine", "name" }, }, true);
		for(int i=0; i<vms.length; i++)
		{
			System.out.println("vm["+i+"]=" + vms[i].getName() + " array entry: " + "vms[" + i+ "]");
		}

		System.out.println("\n============ Hosts ============");
		ManagedEntity[] hosts = new InventoryNavigator(rootFolder).searchManagedEntities(
				new String[][] { {"HostSystem", "name" }, }, true);
		for(int i=0; i<hosts.length; i++)
		{
			System.out.println("host["+i+"]=" + hosts[i].getName());
		}
		
		si.getServerConnection().logout();
	}

}
