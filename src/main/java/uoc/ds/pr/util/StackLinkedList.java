package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.Stack;

public class StackLinkedList<E> extends LinkedList<E> implements Stack<E> {

  public StackLinkedList() {
    super();
  }


  @Override
  public void push(E e) {
    super.insertEnd(e);
  }

  @Override
  public E pop() {
    return super.deleteLast();
  }

  @Override
  public E peek() {
    LinkedNode<E> primer = this.last;
    return (primer != null ? primer.getElem() : null);
  }
}
