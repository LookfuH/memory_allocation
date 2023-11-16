

public class Partition {

	public String toString() {
		return "Partition [base=" + base + ", length=" + length + ", bFree=" + bFree + ", process=" + process + "]";
	}

	// the representation of each memory partition
	public int base;         // base address
	public int length;       // partition size
	public boolean bFree;    // status: free or allocated
	public String process;   // assigned process if allocated

	// constructor method
	public Partition(int base, int length) {
		this.base = base;
		this.length = length;
		this.bFree = true;     // free by default when creating
		this.process = "Free Space";   // unallocated to any process
	}
} 

