import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;


public class NextFit implements GenericFit{

    private int size;    // maximum memory size in bytes (B)
	private HashMap<String, Partition> allocMap;   // map process to partition
	private ArrayList<Partition> partList;    // list of memory partitions

    public NextFit (int size) {
        this.size = size;
		this.allocMap = new HashMap<>();
		this.partList = new ArrayList<>();
		this.partList.add(new Partition(0, size));
    }

    int index = 0;

    public int add (String process, int size) {
        //TODO: add code below
		if(allocMap.containsKey(process))
		{
			return -1;
		}
		int startingIndex = index;
		int alloc = -1;
		do {

			Partition part = partList.get(index);


			if (part.bFree && part.length >= size)
			{

                //change to be Nxtfit
				Partition allocPart = new Partition(part.base, size);
				allocPart.bFree = false;
                allocPart.process = process;
				partList.add(index, allocPart); //inserts allocated into index
				allocMap.put(process, allocPart);
				part.base = part.base + size;
				part.length = part.length - size;
				if(part.length == 0)
					partList.remove(part);
				alloc = size;
				break;


			}
			index++;
            if (index >= partList.size()) {
                 index = 0;
             }
		} while (index != startingIndex);
            print();
			return alloc;
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
