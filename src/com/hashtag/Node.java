package com.hashtag;

/**
 * Created by Surya on 6/21/2017.
 */
public class Node {
    private String name;
    private int value;
    private boolean isChildCut;
    private int degree;
    private Node parentNode;
    private Node rightNode;
    private Node leftNode;
    private Node childNode;

    public Node(String tag, int value) {
        name = tag;
        this.value = value;
        isChildCut = false;
        degree = 0;
        parentNode = null;
        rightNode = this;
        leftNode = this;
        childNode = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isChildCut() {
        return isChildCut;
    }

    public void setChildCut(boolean childCut) {
        isChildCut = childCut;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }

    public Node getChildNode() {
        return childNode;
    }

    public void setChildNode(Node childNode) {
        this.childNode = childNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
