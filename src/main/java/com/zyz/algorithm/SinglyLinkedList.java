package com.zyz.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 单向链表
 * 1) 单链表反转
 * 2) 链表中环的检测
 * 3) 两个有序的链表合并
 * 4) 删除链表倒数第n个结点
 * 5) 求链表的中间结点
 *
 */
public class SinglyLinkedList<E> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SinglyLinkedList.class);


    private SinglyLinkedList.Node<E> head;


    private static class Node<E> {
        E item;
        SinglyLinkedList.Node<E> next;

        Node(E element, SinglyLinkedList.Node<E> next) {
            this.item = element;
            this.next = next;
        }
    }

    public Node addLast(E item){
        Node<E> newNode = new Node<>(item,null);
        if(null==head){
            head = newNode;
            return newNode;
        }
        Node<E> addNode = head;
        while (addNode.next!=null){
            addNode = addNode.next;
        }
        addNode.next=newNode;
        return newNode;
    }

    public void remove(E item){
        if(null==head){
            LOGGER.info("链表为空,删除失败");
            return;
        }
        Node<E> remove = head;
        Node<E> preNode = null;
        while(null!=remove){
            if(!remove.item.equals(item)){
                preNode = remove;
                remove = remove.next;
                continue;
            }
            if(remove==head){
                head=head.next;
            }else {
                preNode.next = remove.next;
            }
            remove = null;
        }
    }

    /**
     * 删除倒数第k个节点
     * @param headNode
     * @param k
     * @return Node 删除后的头结点
     */
    public static Node deleteLastKth(Node headNode, int k) {
        Node fastNode = headNode;
        int i=1;
        while (null!=fastNode&&i<k){
            fastNode=fastNode.next;
            i++;
        }
        if(null==fastNode){
            LOGGER.info("不存在倒数第[{}]个节点,链表长度=[{}]",k,i);
            return headNode;
        }
        //需要删除的节点
        Node slowNode = headNode;
        Node prev = null;
        while (null!=fastNode.next){
            fastNode = fastNode.next;
            prev = slowNode;
            slowNode = slowNode.next;
        }
        if (prev == null) {
            headNode = headNode.next;
        } else {
            prev.next = prev.next.next;
        }
        return headNode;
    }

    /**
     * 链表中环的检测，是否循环链表
     * @param headNode 第一个节点，头结点
     * @return
     */
    public static boolean checkCircle(Node headNode){
        if(null==headNode){
            return Boolean.FALSE;
        }
        Node slowNode = headNode;
        Node fastNode = headNode.next;

        while (null!=fastNode&&null!=fastNode.next){
            slowNode = slowNode.next;
            fastNode = fastNode.next.next;

            if(slowNode==fastNode){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    /**
     * 查找中间节点
     * @param headNode 第一个节点，头结点
     * @return
     */
    public static Node findMiddleNode(Node headNode){
        if(null==headNode){
            return headNode;
        }
        Node slowNode = headNode;
        Node fastNode = headNode.next;
        while (null!=fastNode&&null!=fastNode.next){
            slowNode = slowNode.next;
            fastNode = fastNode.next.next;
        }
        return slowNode;
    }

    /**
     * 合并两个有序的链表
     * @param headNode1
     * @param headNode2
     * @return
     */
    public static Node<Integer> mergeLinkedList(Node<Integer> headNode1,Node<Integer> headNode2){
        Node<Integer> soldier  = new Node(0,null);
        Node<Integer> p = soldier;
        while (null!=headNode1&&null!=headNode2){
            if(headNode1.item.compareTo(headNode2.item)<0){
                p.next = headNode1;
                headNode1 = headNode1.next;
            }else{
                p.next = headNode2;
                headNode2 = headNode2.next;
            }
            p = p.next;
        }

        if (null!=headNode1){
            p.next = headNode1;
        }

        if (null!=headNode2){
            p.next = headNode2;
        }

        return soldier.next;
    }

    public boolean palindrome(){
        if(head==null){
            return false;
        }else {
            LOGGER.info("开始执行查找中间节点");
            Node<E> p = head;
            Node<E> q = head;
            if(p.next==null){
                LOGGER.info("只有一个节点,break!");
                return true;
            }
            while (q.next!=null && q.next.next!=null){
                p = p.next;
                q = q.next.next;
            }

            LOGGER.info("中间节点[{}]",p.item);
            LOGGER.info("开始执行奇数节点的回文判断");
            Node<E> leftNode = null;
            Node<E> rightNode = null;
            if(q.next==null){
                rightNode = p.next;
                leftNode = inverseLinkList(p).next;
                LOGGER.info("左边第一个节点[{}]",leftNode.item);
                LOGGER.info("右边第一个节点[{}]",rightNode.item);
            }else {
                //p q　均为中点
                rightNode = p.next;
                leftNode = inverseLinkList(p);
            }
            return tfResult(leftNode, rightNode);
        }
    }


    public boolean tfResult(Node<E> leftNode,Node<E> rightNode) {

        if(null==leftNode||null==rightNode){
            LOGGER.info("不存在节点,无法比较");
            return false;
        }

        Node<E> lNode = leftNode;
        Node<E> rNode = rightNode;

        while (lNode!=null&&rNode!=null){
            if(lNode.item.equals(rightNode.item)){
                LOGGER.info("存在相同节点[{}]",lNode.item);
                lNode = lNode.next;
                rightNode = rightNode.next;
                continue;
            }else {
                LOGGER.info("存在不相同节点lNode=[{}]，rNode=[{}]",lNode.item,rNode.item);
               return false;
            }
        }
        return true;
    }


    public Node inverseLinkList(Node<E> p){

        Node<E> pre = null;
        Node<E> r = head;
        LOGGER.info("item=[{}]" , r.item);
        Node<E> next= null;
        while(r !=p){
            next = r.next;

            r.next = pre;
            pre = r;
            r = next;
        }

        r.next = pre;
        //　返回左半部分的中点之前的那个节点
        //　从此处开始同步向两边比较
        return r;
    }

    public Node<E> get(int i){
        Node<E> node =head;
        while (node!=null){
            if(node.item.equals(i)){

            }
        }
        return node;
    }

    public static void main(String[] args) {

        LOGGER.info("是否是环链表=[{}]",3>2);

        SinglyLinkedList<Integer> singlyLinkedList = new SinglyLinkedList<>();
        Node node1 = singlyLinkedList.addLast(1);
        singlyLinkedList.addLast(4);
        singlyLinkedList.addLast(5);

        SinglyLinkedList<Integer> singlyLinkedList1 = new SinglyLinkedList<>();
        Node node2 = singlyLinkedList1.addLast(2);
        singlyLinkedList1.addLast(3);
        singlyLinkedList1.addLast(6);


        Node node3 = mergeLinkedList(node1,node2);
        LOGGER.info("是否是环链表=[{}]",node3);


//        singlyLinkedList.addLast(3);
//
//        singlyLinkedList.remove(3);
//        singlyLinkedList.addLast(2);
//
//        singlyLinkedList.addLast(1);

//        LOGGER.info("是否是回文链表=[{}]",singlyLinkedList.palindrome());
    }
}
