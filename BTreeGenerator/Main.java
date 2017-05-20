import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONObject;

public abstract class Main {

	public static void main(String[] args) {
		long programStartTime = System.currentTimeMillis();
		BufferedReader readBuff = null;
		File csv = new File("Pedestrian_volume__updated_monthly_.csv");
		int PageSize;
		
		if(args.length != 1) {
			System.out.println("Please enter desired page size as an integer in the comand line");
			System.out.println("EG: java -jar BTree.jar 4096");
			return;
		}
		else {
			PageSize = Integer.parseInt(args[0]);
		}
		
		ArrayList<BTree> trees = new ArrayList<BTree>();
		
		try {
			readBuff = new BufferedReader(new FileReader(csv));
		} catch (FileNotFoundException e1) {
			System.out.println("ERROR: 'Pedestrian_volume__updated_monthly_.csv' not found!\n Is it in the same directory this program is being run from?");
			return;
		}
		System.out.println("Building trees from 'Pedestrian_volume__updated_monthly_.csv'\n THIS MAY TAKE SOME TIME!");
		try {
			readBuff.readLine(); //Skip header line
		} catch (IOException e1) {
			System.out.println("ERROR: Could not read data from 'Pedestrian_volume__updated_monthly_.csv'");
			try {
				readBuff.close();
			} catch (IOException e) {
			}
			return;
		}
		long treeBuildStart = System.currentTimeMillis();
		trees.add(new BTree(PageSize));
		String line = ""; //Represents a single line from the file
		int EntryCount = 0;
		while(true) {
			try {
				line = readBuff.readLine();
				if(line == null) {
					readBuff.close();
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
				for(BTree tree:trees) {
					try {
						tree.insert(obj);
						break;
					} catch (BTreeException e) { //This exception gets thrown when trying to add to a tree that is full
					}
					if(tree == trees.get(trees.size() - 1)) {
						trees.add(new BTree(PageSize));
						try {
							trees.get(trees.size() - 1).insert(obj);
						} catch (BTreeException e) {
							System.out.println("Exception when creating and inserting to new tree");
						}
						break;
					}
				}
				EntryCount++;
			} catch (IOException e) {
				System.out.println("ERROR: Could not read data from 'Pedestrian_volume__updated_monthly_.csv'");
			}
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Trees successfully built!");
		System.out.println(EntryCount + " records entered!");
		System.out.println(trees.size() + " trees generated!");
		System.out.println("Import Time(mS): " + (treeBuildStart - programStartTime));
		System.out.println("Build Time(mS): " + (endTime - treeBuildStart));
		System.out.println("Total Time Taken(mS): " + (endTime - programStartTime));
		System.out.println();
		
		
		/**Code section below saves the BTree to a series of CSV files**/
		long saveStart = System.currentTimeMillis();
		System.out.println("Saving trees to files");
		BufferedWriter writeBuff = null;
		int count = 0;
		File dir = new File("/Trees/" + PageSize + "/");
		dir.mkdir();
		for(BTree tree:trees) {
			String fileName = "/Trees/" + PageSize + "/BTree" + count + ".csv";
			try {
				System.out.print("Tree: " + count);
				writeBuff = new BufferedWriter(new FileWriter(fileName));
				String treeString = tree.toFileString();
				treeString = treeString.substring(0, treeString.length() - 1);
				writeBuff.write(treeString);
				System.out.println(" saved successfully.");
			} catch (IOException e) {
				System.out.println(" could not write to file: " + fileName);
			}
			count++;
			try {
				writeBuff.close();
			} catch (IOException e) {
				System.out.println("Could not close buffer to file: " + fileName);
			}
		}
		long saveEnd = System.currentTimeMillis();
		System.out.println("Time taken to save trees(mS): " + (saveEnd - saveStart));
		/***************************************************************************/

	}
}
