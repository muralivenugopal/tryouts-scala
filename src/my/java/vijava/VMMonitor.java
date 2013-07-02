package my.java.vijava;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.vmware.vim25.PerfCounterInfo;
import com.vmware.vim25.PerfEntityMetric;
import com.vmware.vim25.PerfEntityMetricBase;
import com.vmware.vim25.PerfMetricId;
import com.vmware.vim25.PerfMetricIntSeries;
import com.vmware.vim25.PerfMetricSeries;
import com.vmware.vim25.PerfMetricSeriesCSV;
import com.vmware.vim25.PerfProviderSummary;
import com.vmware.vim25.PerfQuerySpec;
import com.vmware.vim25.PerfSampleInfo;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.PerformanceManager;
import com.vmware.vim25.mo.ServiceInstance;

/**
 * @author Murali V
 */
/*
 * finally got a clue in this website
 * http://communities.vmware.com/thread/273952?start=0&tstart=0
 */

public class VMMonitor {

	public static void main(String[] args) throws Exception {

		// connect to esx

//		 ServiceInstance si = new ServiceInstance(new URL("https://119.227.201.80/sdk"), "root", "cl0udl@b", true);
		ServiceInstance si = new ServiceInstance(new URL("https://119.227.201.83/sdk"), "CIDev", "Cl0udde^", true); // vsphere login

		// list all the vms

		Folder rootFolder = si.getRootFolder();
		ManagedEntity[] vms = new InventoryNavigator(rootFolder)
				.searchManagedEntities(new String[][] { { "VirtualMachine",
						"name" }, }, true);
		
/*		for (int i = 0; i < vms.length; i++) {
			System.out.println(i + " :: " + vms[i].getName());
		}*/

//		System.exit(1);
		// for each vm, collect parameters, for now only 17

		// collect available parameters
		String vmname = vms[3].getName();
		ManagedEntity vm = new InventoryNavigator(si.getRootFolder())
				.searchManagedEntity("VirtualMachine", vmname);

		System.out.println("vm details " + vm.getName());
		
		List<Integer> pids = new ArrayList<Integer>();
		pids.add(65537);
		

		/*
		 * if (vm == null) { System.out.println("Virtual Machine " + vmname +
		 * " cannot be found."); si.getServerConnection().logout(); return; }
		 */

		PerformanceManager perfMgr = si.getPerformanceManager();
		/*
		 * int counterIds[] = {149, 130}; PerfCounterInfo cinfo[] =
		 * perfMgr.queryPerfCounter(counterIds);
		 * 
		 * for (int i = 0; i < cinfo.length; i++) {
		 * System.out.println("counter info: " + cinfo[i].getNameInfo().label);
		 * }
		 */

		// PerfCounterInfo[] perfCounterInfo = perfMgr.

		// find out the refresh rate for the virtual machine
		PerfProviderSummary pps = perfMgr.queryPerfProviderSummary(vm);
		int refreshRate = pps.getRefreshRate().intValue();

		// retrieve all the available perf metrics for vm
		PerfMetricId[] pmis = perfMgr.queryAvailablePerfMetric(vm, null, null,
				refreshRate);

		int[] cid = new int[1];
		PerfCounterInfo cinfo[] = null;

		/*
		 * for (int i = 0; i < pmis.length; i++) {
		 * 
		 * cid[0] = pmis[i].getCounterId(); cinfo =
		 * perfMgr.queryPerfCounter(cid);
		 * System.out.println(cinfo[0].getGroupInfo().label + "/" +
		 * cinfo[0].getNameInfo().label + " in " + cinfo[0].getUnitInfo().label
		 * + ", counter id - " + pmis[i].getCounterId() + ", instance : " +
		 * pmis[i].getInstance()); }
		 */

		PerfQuerySpec qSpec = createPerfQuerySpec(vm, pmis, 1, refreshRate);

		PerfEntityMetricBase[] pValues = perfMgr.queryPerf(new PerfQuerySpec[] { qSpec });
		System.out.println(pValues);
		if (pValues != null) {
			System.out.println("pvalues is null");
			displayValues(pValues, perfMgr);
		}

	}

	static PerfQuerySpec createPerfQuerySpec(ManagedEntity me,
			PerfMetricId[] metricIds, int maxSample, int interval) {
		PerfQuerySpec qSpec = new PerfQuerySpec();
		qSpec.setEntity(me.getMOR());
		// set the maximum of metrics to be return
		// only appropriate in real-time performance collecting
		qSpec.setMaxSample(new Integer(maxSample));
		// qSpec.setMetricId(metricIds);
		// optionally you can set format as "normal"
		// qSpec.setFormat("csv");
		// set the interval to the refresh rate for the entity
		qSpec.setIntervalId(new Integer(interval));

		return qSpec;
	}

	static void displayValues(PerfEntityMetricBase[] values, PerformanceManager perfMgr) throws RuntimeFault, RemoteException {
		for (int i = 0; i < values.length; ++i) {
			String entityDesc = values[i].getEntity().getType() + ":" + values[i].getEntity().get_value();
			System.out.println("Entity:" + entityDesc);
			if (values[i] instanceof PerfEntityMetric) {
				printPerfMetric((PerfEntityMetric) values[i], perfMgr);
			} else {
				System.out.println("UnExpected sub-type of " + "PerfEntityMetricBase.");
			}
		}
	}

	static void printPerfMetric(PerfEntityMetric pem, PerformanceManager perfMgr) throws RuntimeFault, RemoteException {
		PerfMetricSeries[] vals = pem.getValue();
		PerfSampleInfo[] infos = pem.getSampleInfo();

		System.out.println("Sampling Times and Intervales:");
		for (int i = 0; infos != null && i < infos.length; i++) {
			System.out.println("Sample time: " + infos[i].getTimestamp().getTime());
			System.out.println("Sample interval (sec):" + infos[i].getInterval());
		}

		System.out.println("Sample values:");
		for (int j = 0; vals != null && j < vals.length; ++j) {
			System.out.print(printDetails(vals[j].getId().getCounterId(), perfMgr) + "counterID:"+ vals[j].getId().getCounterId() + "::instanceID:-" + vals[j].getId().getInstance() + "-::");
			if (vals[j] instanceof PerfMetricIntSeries) {
				PerfMetricIntSeries val = (PerfMetricIntSeries) vals[j];
				long[] longs = val.getValue();
				for (int k = 0; k < longs.length; k++) {
					System.out.print(longs[k] + " ");
				}
				System.out.println("Total:" + longs.length);
			} else if (vals[j] instanceof PerfMetricSeriesCSV) { // it is not
																	// likely
																	// coming
																	// here...
				PerfMetricSeriesCSV val = (PerfMetricSeriesCSV) vals[j];
				System.out.println("CSV value:" + val.getValue());
			}
		}
	}

	static String printDetails(int counterId, PerformanceManager perfMgr) throws RuntimeFault,
			RemoteException {

		int[] cid = new int[1];
		PerfCounterInfo cinfo[] = null;
		cid[0] = counterId;
		cinfo = perfMgr.queryPerfCounter(cid);
		return cinfo[0].getGroupInfo().label + "/" + cinfo[0].getNameInfo().label + " in "+ cinfo[0].getUnitInfo().label + "::";

	}
}