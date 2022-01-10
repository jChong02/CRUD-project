package DataStructure;

/**
 * LinkedBag.java CIS22C_Spring2020
 * 
 * GROUP:22C||!22C
 * 
 * @author Thanh LE
 * @author Chong Jun Jie
 * @author Tun Pyay Sone LIN
 * 
 * 
 *         Description: LinkedBag class have all the necessary methods for a
 *         successful LinkedList implementation of Bags. Class Node is nested
 *         within the LList class.
 *
 * 
 */

public class LinkedBag<T> implements BagInterface<T> {

	// Define NODE class
	private class Node {
		private T data;
		private Node next;

		// Constructor
		private Node(T dataP, Node nextN) {
			data = dataP;
			next = nextN;
		}

		private Node(T dataP) {
			this(dataP, null);
		}
		// Accessors

		private T getData() {
			return data;
		}

		private Node getNextNode() {
			return next;
		}

		// Mutators
		private void setData(T data) {
			this.data = data;
		}

		private void setNextNode(Node nNode) {
			next = nNode;
		}

	}// END OF NODE CLASS

	private Node firstNode; // Reference to first node of chain
	private int numberOfEntries;

//default constructor
	public LinkedBag() {
		firstNode = null;
		numberOfEntries = 0;
	} // end default constructor

// Implement the unimplemented methods 

// Locates a given entry within this bag.
// Returns a reference to the node containing the entry, if located,
// or null otherwise.
	private Node getReferenceTo(T anEntry) {
		boolean found = false;
		Node currentNode = firstNode;

		while (!found && (currentNode != null)) {
			if (anEntry.equals(currentNode.data))
				found = true;
			else
				currentNode = currentNode.next;
		} // end while

		return currentNode;
	} // end getReferenceTo

	/**
	 * Gets the current number of entries in this bag.
	 * 
	 * @return The integer number of entries currently in the bag.
	 */
	public int getCurrentSize() {
		return numberOfEntries;
	}

	/**
	 * Sees whether this bag is empty.
	 * 
	 * @return True if the bag is empty, or false if not.
	 */
	public boolean isEmpty() {
		return numberOfEntries == 0;
	}

	/**
	 * Adds a new entry to this bag.
	 * 
	 * @param newEntry The object to be added as a new entry.
	 * @return True if the addition is successful, or false if not.
	 */
	public boolean add(T newEntry) {
		Node newN = new Node(newEntry);
		newN.next = firstNode; // assign new node to point to firstNode
								// => newN = new firstNode & ori firstN = 2nd Node

		firstNode = newN;
		numberOfEntries++;
		return true;

	}

	/**
	 * Removes one unspecified entry from this bag, if possible.
	 * 
	 * @return Either the removed entry, if the removal. was successful, or null.
	 */
	public T remove() {
		T result = null;
		if (!isEmpty()) {
			result = firstNode.getData(); // Store the data of firstNode in newNode
			firstNode = firstNode.getNextNode();// point head to second node
			numberOfEntries--;
		}
		return result;
	}

	/**
	 * Removes one occurrence of a given entry from this bag.
	 * 
	 * @param anEntry The entry to be removed.
	 * @return True if the removal was successful, or false if not.
	 */
	public boolean remove(T anEntry) {
		boolean result = false;
		Node N = getReferenceTo(anEntry); // see if the entry node is contained in the list

		if (N != null) {
			N.data = firstNode.data; // Set the located node data with firstNode data
										// => at this moment you have 2 copies of firstNode at position 0 and located
										// position
			firstNode = firstNode.next;
			numberOfEntries--;
			result = true;
		}
		return result;
	}

	/** Removes all entries from this bag. */
	public void clear() {
		while (!isEmpty())
			remove();
	}

	/**
	 * Counts the number of times a given entry appears in this bag.
	 * 
	 * @param anEntry The entry to be counted.
	 * @return The number of times anEntry appears in the bag.
	 */
	public int getFrequencyOf(T anEntry) {
		int freq = 0;
		int loopCounter = 0;
		Node currentN = firstNode;
		while ((loopCounter < numberOfEntries) && (currentN != null)) {
			if (anEntry.equals(currentN.data))
				freq++; // increase the number of same items
			loopCounter++; // entries index
			currentN = currentN.next; // move to next node
		}
		return freq;
	}

	/**
	 * Tests whether this bag contains a given entry.
	 * 
	 * @param anEntry The entry to locate.
	 * @return True if the bag contains anEntry, or false if not.
	 */
	public boolean contains(T anEntry) {
		boolean found = false;
		Node currentN = firstNode;
		while (!found && (currentN != null)) {
			if (anEntry.equals(currentN != null))
				found = true;
			else
				currentN = currentN.next;
		}
		return found;
	}

	/**
	 * Retrieves all entries that are in this bag.
	 * 
	 * @return A newly allocated array of all the entries in the bag. Note: If the
	 *         bag is empty, the returned array is empty.
	 */
	public T[] toArray() {
		// public <T> T[] toArray(); // Alternate
		@SuppressWarnings("unchecked")
		T[] result = (T[]) new Object[numberOfEntries]; // Cast

		int index = 0;
		Node currentN = firstNode;
		while ((index < numberOfEntries) && (currentN != null)) {
			result[index] = currentN.data;
			index++;
			currentN = currentN.next;
		}
		return result;
	}

	/**
	 * Creates a new bag that combines the contents of this bag and anotherBag.
	 * 
	 * @param anotherBag The bag that is to be added.
	 * @return A combined bag.
	 */
//	public BagInterface<T> union (BagInterface<T> anotherBag);

	/**
	 * Creates a new bag that contains those objects that occur in both this bag and
	 * anotherBag.
	 * 
	 * @param anotherBag The bag that is to be compared.
	 * @return A combined bag.
	 */
//	public BagInterface<T> intersection (BagInterface<T> anotherBag);

	/**
	 * Creates a new bag of objects that would be left in this bag after removing
	 * those that also occur in anotherBag.
	 * 
	 * @param anotherBag The bag that is to be removed.
	 * @return A combined bag.
	 */
//	public BagInterface<T> difference (BagInterface<T> anotherBag);

}// end LinkedBag