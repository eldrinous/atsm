package marf.util;

import java.util.Vector;
import java.util.Collection;
import java.util.List;


/**
 * <p>Adaptive extension of the java.util.Vector class.</p>
 *
 * <p>You may access elements of a Vector beyond it's initial length --- the Vector
 * will be automatically adjusted as appropriate.</p>
 *
 * <p>Useful in the applications where desirable vector's growth by setting an element
 * beyond its upper boundary automatically lengthens the vector to accommodate the
 * change (similar to Perl arrays).</p>
 *
 * <p>Similarly, getting an element beyond the upper boundary is not desirable failure, but
 * an empty element returned. This makes the application to see as vector of a theoretically infinite
 * in length.</p>
 *
 * TODO: allow negative index boundaries.
 *
 * @author Serguei Mokhov
 * @version $Id: FreeVector.java,v 1.16 2010/03/06 02:40:10 mokhov Exp $
 * @since 0.3.0.1
 */
public class FreeVector<E>
extends Vector<E>
{
	/**
	 * For serialization versioning.
	 * When adding new members or make other structural
	 * changes regenerate this number with the
	 * <code>serialver</code> tool that comes with JDK.
	 * @since 0.3.0.4
	 */
	private static final long serialVersionUID = 8706834105778495182L;

	/**
	 * A free vector with default capacity as specified by java.util.Vector.
	 */
	public FreeVector()
	{
		super();
	}

	/**
	 * Constructs this vector given capacity other than default.
	 * Inherited from java.util.Vector.
	 * @param piInitialCapacity initial element capacity (number of object placeholders)
	 */
	public FreeVector(int piInitialCapacity)
	{
		super(piInitialCapacity);
	}

	/**
	 * Constructs this vector given capacity and its increment.
	 * Inherited from java.util.Vector.
	 * @param piInitialCapacity initial element capacity (number of object placeholders)
	 * @param piCapacityIncrement when current capacity reached, until how much capacity should be extended
	 */
	public FreeVector(int piInitialCapacity, int piCapacityIncrement)
	{
		super(piInitialCapacity, piCapacityIncrement);
	}
	
	/**
	 * Constructs this vector out of a collection.
	 * Inherited from java.util.Vector.
	 * @param poCollection collection for the vector elements.
	 */
	public FreeVector(Collection<? extends E> poCollection)
	{
		super(poCollection);
	}


	/**
	 * Access an element of the vector given index. 
	 * Overridden from java.util.Vector.
	 * @param piIndex vector element index to retrieve
	 * @return object contained at specified index, null if beyond boundary
	 */
	public synchronized E elementAt(int piIndex)
	{
		if(piIndex > size() - 1)
		{
			return null;
		}

		return super.elementAt(piIndex);
	}

	/**
	 * Set an element of the vector given index.
	 * Capacity is always ensured to be able to accommodate
	 * any positive index (barring out of memory problems). 
	 * Overridden from java.util.Vector.
	 * 
	 * @param poElement element to set at the index
	 * @param piIndex the index
	 */
	public synchronized void setElementAt(E poElement, int piIndex)
	{
		ensureIndexCapacity(piIndex);
		super.setElementAt(poElement, piIndex);
	}

	/**
	 * Inserts an element of the vector after given index.
	 * Capacity is always ensured to be able to accommodate
	 * any positive index (barring out of memory problems). 
	 * Overridden from java.util.Vector.
	 * 
	 * @param poElement element to set after the index
	 * @param piIndex the index
	 */
	public synchronized void insertElementAt(E poElement, int piIndex)
	{
		ensureIndexCapacity(piIndex);
		super.insertElementAt(poElement, piIndex);
	}

	/**
	 * Make sure the capacity of the vector is enough
	 * to hold an element with the specified index.
	 * Has effect only if the index is greater than
	 * the current vector's size.
	 * 
	 * @param piIndex the index to accommodate
	 */
	public synchronized void ensureIndexCapacity(int piIndex)
	{
		if(piIndex > size() - 1)
		{
			ensureCapacity(piIndex + 1);
			setSize(piIndex + 1);
		}
	}

	/**
	 * Access an element of the vector given index. 
	 * Overridden from java.util.Vector. Calls the overridden elementAt().
	 * 
	 * @param piIndex vector element index to retrieve
	 * @return object contained at specified index, null if beyond boundary
	 */
	public synchronized E get(int piIndex)
	{
		return elementAt(piIndex);
	}

	/**
	 * Set an element of the vector given index.
	 * Capacity is always ensured to be able to accommodate
	 * any positive index (barring out of memory problems).
	 * Overridden from java.util.Vector.
	 * 
	 * @param poElement element to set at the index
	 * @param piIndex the index
	 * @return object that was previously at that index.
	 */
	public synchronized E set(int piIndex, E poElement)
	{
		E oOldElement = elementAt(piIndex);
		setElementAt(poElement, piIndex);
		return oOldElement;
	}

	/**
	 * Adds an element of the vector at the specified index. 
	 * Overridden from java.util.Vector. Calls the overridden insertElementAt().
	 * @param piIndex the index
	 * @param poElement element to set after the index
	 */
	public synchronized void add(int piIndex, E poElement)
	{
		insertElementAt(poElement, piIndex);
	}

	/**
	 * Removes an element at index.
	 * If the index is beyond the upper boundary, returns null.  
	 * Overrides java.util.Vector.
	 * @param piIndex index of the element to be removed
	 * @return object reference of the element just removed; null if index exceeds the upper bound
	 */
	public synchronized E remove(int piIndex)
	{
		if(piIndex >= size())
		{
			//XXX:
			//???
			// 1 less than the index
			//ensureIndexCapacity(piIndex - 1);

			return null;
		}

		return super.remove(piIndex);
	}

	/**
	 * Adds a collection of elements to this vector starting at given index.
	 * Makes sure the capacity of the current vector reaches the piIndex. 
	 * Overrides java.util.Vector.
	 * 
	 * @param piIndex starting index to add elements from
	 * @param poCollection collection of elements to add
	 * 
	 * @return <code>true</code> if the vector has changed
	 */
    public synchronized boolean addAll(int piIndex, Collection<? extends E> poCollection)
    {
		ensureIndexCapacity(piIndex);
		return super.addAll(piIndex, poCollection);
	}

	/**
	 * Retrieves a sublist subset of vector elements given index boundaries.
	 * Makes sure the capacity of the current vector reaches the piToIndex. 
	 * Overrides java.util.Vector.
	 * 
	 * @param piFromIndex starting index to fetch elements from
	 * @param piToIndex last index to fetch elements to
	 * 
	 * @return a corresponding List reference.
	 */
	public synchronized List<E> subList(int piFromIndex, int piToIndex)
	{
		ensureIndexCapacity(piToIndex);
		return super.subList(piFromIndex, piToIndex);
	}

	/**
	 * Not implemented.
	 * Meant to remove a set of elements between two specified indices.
	 * @param piFromIndex starting index to remove elements from
	 * @param piToIndex last index to remove elements to
	 * @throws NotImplementedException
	 */
	public synchronized void removeRange(int piFromIndex, int piToIndex)
	{
		// TODO: implement
		throw new NotImplementedException(this, "removeRange()");
	}

	/**
	 * Retrieves class' revision.
	 * @return revision string
	 * @since 0.3.0.2
	 */
	public static String getMARFSourceCodeRevision()
	{
		return "$Revision: 1.16 $";
	}
}

// EOF
