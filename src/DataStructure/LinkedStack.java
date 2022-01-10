package DataStructure;
/**
 *  GROUP:22C||!22C
 * 
 * @author Chong Jun Jie
 * @author Thanh LE
 * @author Tun Pyay Sone LIN
 * 
 * Starter Code:
 * Linked Stack Exercise
 * 
 */
public class LinkedStack<T> implements StackInterface<T> {

	private Node<T> topNode;
	
	public LinkedStack() {
		topNode = null;
	} //end default constructor
	
	
	
	public void push(T newEntry) {
		Node<T> newNode = new Node<T>(newEntry, topNode);
		topNode = newNode;
	} //end push
	
	
	
	public T pop() {
		T top = peek();
		assert topNode != null;
		topNode = topNode.getNextNode();
		
		return top;
	} //end pop
	
	
	
	public boolean isEmpty() {
		return topNode == null;
	} //end isEmpty
	
	
	public void clear() {
		topNode = null;
	} //end clear
	
	
	
	public T peek() {
		if(topNode == null)
			throw new EmptyStackException("");
		else
			return topNode.getData();
	} //end peek
	
	
	
	//Node class
	private class Node<T>
	{
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
Test run:

Create a stack: 
isEmpty() returns true

Add to stack to get
Joe Jane Jill Jess Jim

isEmpty() returns false

Testing peek and pop:

Joe is at the top of the stack.
Joe is removed from the stack.

Jane is at the top of the stack.
Jane is removed from the stack.

Jill is at the top of the stack.
Jill is removed from the stack.

Jess is at the top of the stack.
Jess is removed from the stack.

Jim is at the top of the stack.
Jim is removed from the stack.

The stack should be empty: isEmpty() returns true

Add to stack to get
Jim Jess Joe


Testing clear:
The stack should be empty: 

isEmpty() returns true

 myStack.peek() returns 
Exception in thread "main" EmptyStackException: 
	at LinkedStack.peek(LinkedStack.java:42)
	at Driver.testStackOperations(Driver.java:58)
	at Driver.main(Driver.java:12)

*/
