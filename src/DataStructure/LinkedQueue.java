package DataStructure;
/**
 *  GROUP:22C||!22C
 * 
 * @author Thanh LE
 * @author Chong Jun Jie
 * @author Tun Pyay Sone LIN
 * 
 * 
 * Linked Queue Exercise
 * 6 May 2020
 * 
 */
public class LinkedQueue<T> implements QueueInterface<T> {

	private Node<T> firstNode;
	private Node<T> lastNode;
	
	public LinkedQueue() {
		firstNode = null;
		lastNode = null;
	}

	
	
	public void enqueue(T newEntry) {
		Node<T> newNode = new Node<T>(newEntry, null);
		if(isEmpty())
			firstNode = newNode;
		else
			lastNode.setNextNode(newNode);
		
		lastNode = newNode;
	} //end enqueue
	
	
	
	public T getFront() {
		if(isEmpty())
			throw new EmptyQueueException();
		else
			return firstNode.getData();
	} //end getFront
	
	
	
	public T dequeue() {
		T front = getFront();
		assert firstNode != null;
		firstNode.setData(null);
		firstNode = firstNode.getNextNode();
		
		if(firstNode == null)
			lastNode = null;
		
		return front;
	} //end dequeue
	
	
	
	public boolean isEmpty() {
		return (firstNode == null) && (lastNode == null);
	} //end isEmpty
	
	
	
	public void clear() {
		firstNode = null;
		lastNode = null;
	} //end clear 
	
	
	
	//Node class
	private class Node <T> {
		private T data;
		private Node<T> next;
		private Node(T dataPortion)
		{
			this(dataPortion, null);
		}
		private Node (T dataPortion,Node<T> nextNode)
		{
			data = dataPortion;
			next = nextNode;
		}
		private T getData() 
		{
			return data;
		}
		private Node<T> getNextNode()
		{
			return next;
		}
		private void setData(T newData)
		{
			data = newData;
		}
		private void setNextNode(Node<T> nextNode)
		{
			next = nextNode;
		}
	}
}


/*
Test Run
Create a queue: 


isEmpty() returns true

Add to queue to get
Joe Jess Jim Jill Jane Jerry


isEmpty() returns false



Testing getFront and dequeue:

Joe is at the front of the queue.
Joe is removed from the front of the queue.

Jess is at the front of the queue.
Jess is removed from the front of the queue.

Jim is at the front of the queue.
Jim is removed from the front of the queue.

Jill is at the front of the queue.
Jill is removed from the front of the queue.

Jane is at the front of the queue.
Jane is removed from the front of the queue.

Jerry is at the front of the queue.
Jerry is removed from the front of the queue.


The queue should be empty: isEmpty() returns true


Add to queue to get
Joe Jess Jim


Testing clear:


isEmpty() returns true


Add to queue to get
Joe Jess Jim

Joe is at the front of the queue.
Joe is removed from the front of the queue.

Jess is at the front of the queue.
Jess is removed from the front of the queue.

Jim is at the front of the queue.
Jim is removed from the front of the queue.



The queue should be empty: isEmpty() returns true

The next calls will throw an exception.

Exception in thread "main" EmptyQueueException
	at LinkedQueue.getFront(LinkedQueue.java:28)
	at Driver.testQueueOperations(Driver.java:76)
	at Driver.main(Driver.java:12)
*/
