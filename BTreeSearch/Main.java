import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

public abstract class Main {

	public static void main(String[] args) {
		int PageSize;
		BufferedReader fileBuffer;
		File csv = new File("Pedestrian_volume__updated_monthly_.csv");
		
		if(args.length != 1) {
			System.out.println("Please enter desired page size as an integer in the comand line");
			System.out.println("EG: java -jar BTree.jar 4096");
			return;
		}
		else {
			PageSize = Integer.parseInt(args[0]);
		}
		
		//Open pedestrian data file and read each line into a JSON Object Array
		System.out.println("Reading data from 'Pedestrian_volume__updated_monthly_.csv'...");
		HashMap<Integer, ArrayList<JSONObject>> fullData = new HashMap<Integer, ArrayList<JSONObject>>();
		try {
			fileBuffer = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException fnfe) {
			System.out.println("ERROR: 'Pedestrian_volume__updated_monthly_.csv' not found!\n Is it in the same directory this program is being run from?");
			return;
		}
		try {
			fileBuffer.readLine();
		} catch (IOException e1) {
			System.out.println("ERROR: Could not read data from 'Pedestrian_volume__updated_monthly_.csv'");
			try {
				fileBuffer.close();
			} catch (IOException e) {
			}
			return;
		}
		
		String line = "";
		while(true) {
			try {
				line = fileBuffer.readLine();
				if(line == null) {
					fileBuffer.close();
					break;
				}
				
				//Split CSV at the commas
				String[] array = line.split(",");
				JSONObject obj = new JSONObject();
				obj.put("ID", Integer.parseInt(array[0]));
				obj.put("Date_Time", array[1]);
				obj.put("Year", Integer.parseInt(array[2]));
				obj.put("Month", array[3]);
				obj.put("Mdate", Integer.parseInt(array[4]));
				obj.put("Day", array[5]);
				obj.put("Time", Integer.parseInt(array[6]));
				obj.put("Sensor_ID", Integer.parseInt(array[7]));
				obj.put("Sensor_Name", array[8]);
				obj.put("Hourly_Counts", Integer.parseInt(array[9]));
				if(fullData.containsKey(Integer.parseInt(array[9]))) {
					ArrayList<JSONObject> rids = fullData.get(Integer.parseInt(array[9]));
					rids.add(obj);
					fullData.put(Integer.parseInt(array[9]), rids);
				} else {
					ArrayList<JSONObject> rids = new ArrayList<JSONObject>();
					rids.add(obj);
					fullData.put(Integer.parseInt(array[9]), rids);
				}
			} catch(IOException e) {
				System.out.println("ERROR: Could not read data from 'Pedestrian_volume__updated_monthly_.csv'");
				return;
			}
		}
		System.out.println("Data read!\n");
		
		System.out.println("Building trees of size " + PageSize + " from saved files...");	
		File folder = new File("/Trees/" + PageSize + "/");
		File[] listOfTrees = folder.listFiles();
		if(listOfTrees == null) {
			System.out.println("Could not build trees of size " + PageSize + ".\n No such trees found!");
			return;
		}
		long startTime = System.currentTimeMillis();
		ArrayList<BTree> BTreeList= new ArrayList<BTree>();
		String data = "";
		for(File tree:listOfTrees) {
			if(tree.isFile()) {
				try {
					fileBuffer = new BufferedReader(new FileReader(tree));
					data = fileBuffer.readLine();
					String[] nodes = data.split(",");
					BTreeList.add(new BTree());
					for(String node:nodes) {
						int iNode = Integer.parseInt(node);
						ArrayList<JSONObject> rids = fullData.get(iNode);
						try {
							BTreeList.get(BTreeList.size() - 1).insert(iNode, rids);
						} catch (BTreeException e) {
						}
					}
					System.out.println();
				} catch (IOException e) {
					System.out.println("Could not read tree " + tree);
				}
			}
		}
		System.out.println(BTreeList.size() + " Trees have been built from file!");
		long endTime = System.currentTimeMillis();
		System.out.println("Time taken (mins): " + (endTime-startTime)/60000);
		System.out.println("Time taken (secs): " + (endTime-startTime)/1000);
		System.out.println("Time taken (millis): " + (endTime-startTime));
		
		/**Preliminary code within this program to find indexed values**/
		System.out.println("Please enter an Hourly_Counts to find all records of!");
		System.out.println("Enter 'X' to quit.");
		InputStream inStream = System.in;
		BufferedReader readBuff = new BufferedReader(new InputStreamReader(inStream));
		
		while(true) {
			try {
				line = readBuff.readLine();
				if(line.equalsIgnoreCase("x")) {
					readBuff.close();
					break;
				} else if (line.equals("Head")) {
					long searchStartTime = System.currentTimeMillis();
					ArrayList<JSONObject> returnedData = new ArrayList<JSONObject>();
					for(BTree tree:BTreeList) {
						returnedData.addAll(tree.HeadNode.getRids());
					}
					for(JSONObject obj:returnedData) {
						System.out.println(obj.toString());
					}
					long searchEndTime = System.currentTimeMillis();
					System.out.println(returnedData.size() + " data points found.");
					System.out.println("That search took(mS): " + (searchEndTime - searchStartTime));
				} else {
					long searchStartTime = System.currentTimeMillis();
					int target = Integer.parseInt(line);
					boolean found = false;
					ArrayList<JSONObject> returnedData = new ArrayList<JSONObject>();
					for(BTree tree:BTreeList) {
						if(tree.search(target)) {
							 returnedData = tree.getRids(target);
							 found = true;
							 break;
						}
					}
					if(!found) {
						System.out.println("Node " + target + " doesn't exist!");
					}else if(returnedData == null) {
						System.out.println("0 results found");
					} else {
						for(JSONObject obj:returnedData) {
							System.out.println(obj.toString());
						}
						System.out.println(returnedData.size() + " data points found.");
					}
					long searchEndTime = System.currentTimeMillis();
					System.out.println("That search took(mS): " + (searchEndTime - searchStartTime));
				}
			} catch (IOException ie) {
				System.out.println("ERROR: Could not read data from command line");
			}
		}
		/***************************************************************************/
	}

}
