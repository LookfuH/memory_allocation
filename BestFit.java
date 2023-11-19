import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

//find Smallest possible.
public class BestFit implements GenericFit {

    private int size;    // maximum memory size in bytes (B)
	private HashMap<String, Partition> allocMap;   // map process to partition
	private ArrayList<Partition> partList;    // list of memory partitions

    public BestFit (int size) {
        this.size = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		this.partList.add(new Partition(0, size));
    }

    public int add(String process, int size) {
		if (allocMap.containsKey(process))
		{
			return -1;
		}

		Partition targetPartition = null;

		for (Partition part : partList) {
			if (part.bFree && part.length >= size && (targetPartition == null || targetPartition.length > part.length)) {
				targetPartition = part;
			}
		}

		if (targetPartition == null) {
			return -1;
		}

		int index = partList.indexOf(targetPartition);

		Partition newPart = new Partition(targetPartition.base, size);
		newPart.bFree = false;
		newPart.process = process;
		partList.add(index, newPart);
		targetPartition.base += size;
		targetPartition.length -= size;
		allocMap.put(process, newPart);

		if(targetPartition.length <= 0)
			partList.remove(targetPartition);

		print();
		return size;
    }

    public void order_partitions() {
		Collections.sort(partList, (o1,o2) -> o1.base - o2.base);
	}

    public int remove (String process) {
        if(!allocMap.containsKey(process)) { System.err.println("FAILED TO REMOVE :("); return -1;}

		Partition part = allocMap.get(process);
		part.bFree = true;
		part.process = "Free Space";
		size = part.length;

		merge_holes();
		print();

		return size;
    }

    public void merge_holes() {
		order_partitions();
		ArrayList<Partition> removalQueue = new ArrayList<Partition>();
		Partition freePart = null;
		for (Partition part : partList) {
			if (part.bFree) {
				if (freePart != null) {
					freePart.length += part.length;
					removalQueue.add(part);
				} else {
					freePart = part;
				}
			} else {
				freePart = null;
			}
		}

		for (Partition remPart : removalQueue) {
			partList.remove(remPart);
		}
    }

    public void print() {
        String print = "";
        for (Partition part : partList) {
            print += part.toString() + "\n";
        }
        System.out.println(print);
    }
}
