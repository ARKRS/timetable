package com.timers.timetable.deptsmanagement;

import java.util.ArrayList;
import java.util.List;


public class DeptsTree<T>{
    private T data = null;
    private List<DeptsTree> children = new ArrayList<>();
    private DeptsTree parent = null;

    public DeptsTree(T data) {
        this.data = data;
    }

    public void addChild(DeptsTree child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        DeptsTree<T> newChild = new DeptsTree<>(data);
        this.addChild(newChild);
    }

    public void addChildren(List<DeptsTree> children) {
        for(DeptsTree t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<DeptsTree> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(DeptsTree parent) {
        this.parent = parent;
    }

    public DeptsTree getParent() {
        return parent;
    }
}