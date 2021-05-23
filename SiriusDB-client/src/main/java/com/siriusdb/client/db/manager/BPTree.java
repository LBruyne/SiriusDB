package com.siriusdb.client.db.manager;
import java.util.Vector;

/**
 * @Auther: ylx
 * @Date: 2021/05/23/14:11
 * @Description: B-plus Tree interface
 */
public class BPTree <K extends Comparable<? super K>, V>{  //// K:key type; V:value type

    private Node<K, V> root;

    public BPTree(int order) { //order: number of keys in one node
        this.root = new leafNode<>(order);
    }

    public Vector<V> findEq(K key) {
        Vector<V> res = new Vector<>();
        try {
            res.add(this.root.find(key));
        } catch (IllegalArgumentException e) {
            //do nothing
        }
        return res;
    }

    public Vector<V> findNeq(K key) {
        Vector<V> res = new Vector<>();
        if (this.root != null) {
            leafNode<K, V> leaf = this.root.getFirstLeaf();
            while (leaf != null) {
                if (leaf.keys[leaf.cnt - 1].compareTo(key) < 0 || leaf.keys[0].compareTo(key) > 0) {
                    for (int i = 0; i < leaf.cnt; i++) {
                        res.add(leaf.values[i]);
                    }
                } else {
                    int index = leaf.findIndex(key);
                    if (leaf.keys[index].equals(key)) {
                        for (int i = 0; i < index; i++) {
                            res.add(leaf.values[i]);
                        }
                        for (int i = index + 1; i < leaf.cnt; i++) {
                            res.add(leaf.values[i]);
                        }
                    } else {
                        for (int i = 0; i < leaf.cnt; i++) {
                            res.add(leaf.values[i]);
                        }
                    }
                }
                leaf = leaf.next;
            }
        }
        return res;
    }

    public Vector<V> findLeq(K key) {
        Vector<V> res = this.findLess(key);
        try {
            V value = this.findEq(key).get(0);
            res.add(value);
        } catch (IllegalArgumentException e) {
            //do nothing
        }
        return res;
    }

    public Vector<V> findLess(K key) {
        Vector<V> res = new Vector<>();
        if (this.root != null) {
            leafNode<K, V> leaf = this.root.getFirstLeaf();
            while (leaf != null) {
                if (leaf.keys[leaf.cnt - 1].compareTo(key) < 0) {
                    for (int i = 0; i < leaf.cnt; i++) {
                        res.add(leaf.values[i]);
                    }
                } else {
                    int index = leaf.findIndex(key);
                    for (int i = 0; i < index; i++) {
                        res.add(leaf.values[i]);
                    }
                    break;
                }
                leaf = leaf.next;
            }
        }
        return res;
    }

    public Vector<V> findGeq(K key) {
        Vector<V> res = new Vector<>();
        try {
            V value = this.findEq(key).get(0);
            res.add(value);
        } catch (IllegalArgumentException e) {
            //do nothing
        }
        res.addAll(this.findGreater(key));
        return res;
    }

    public Vector<V> findGreater(K key) {
        return this.root.findGreater(key);
    }

    public void insert(K key, V value) throws IllegalArgumentException {
        Node<K, V> ret = root.insert(key, value);
        if (ret != null) {
            this.root = ret;
        }
    }

    public void delete(K key) throws IllegalArgumentException {
        Node<K, V> ret = root.delete(key);
        if (ret != null) {
            this.root = ret;
        }
    }

    public void update(K key, V value) throws IllegalArgumentException {
        this.root.update(key, value);
    }

    public void print() { //pre-order traversal
        this.root.print();
    }

    public void checkStructure() throws RuntimeException { //used for debug: to check the legality of the structure of the B+ tree
        this.root.checkStructure();
    }

    static abstract class Node<K extends Comparable<? super K>, V> {
        protected internalNode<K, V> parent;
        protected Integer order;
        protected Integer cnt;
        protected K[] keys;

        public Node(Integer order) { //order: number of keys in one node
            this.order = order;
            this.cnt = 0;
            this.parent = null;
            this.keys = (K[]) new Comparable[order];
        }

        public abstract V find(K key) throws IllegalArgumentException;

        public abstract Vector<V> findGreater(K key);

        public abstract leafNode<K, V> getFirstLeaf();

        public abstract Node<K, V> insert(K key, V value) throws IllegalArgumentException;

        public abstract Node<K, V> delete(K key) throws IllegalArgumentException;

        public abstract void update(K key, V value) throws IllegalArgumentException;

        public boolean isRoot() {
            return this.parent == null;
        }

        public abstract void print(); //Pre-order traversal

        public abstract void checkStructure() throws RuntimeException;

        public abstract K getLeast();

        protected int findIndex(K key) { //binary-search
            if (this.cnt == 0) {
                return 0;
            }
            int left = 0, right = this.cnt;
            int index = 0;
            while (left < right) {
                index = (left + right) / 2;
                int cmp = key.compareTo(keys[index]);
                if (cmp < 0) {
                    right = index;
                } else if (cmp > 0) {
                    left = index + 1;
                } else {
                    break;
                }
            }
            if (key.compareTo(this.keys[index]) > 0) {
                index++; //if key not found, return the index of the first key whose value is no less than the key to find
            }
            return index;
        }

        protected void updateDeletedKeys(K oldKey, K newKey) {
            Node<K, V> node = this.parent;
            while (node != null) {
                int index = node.findIndex(oldKey);
                if (node.keys[index].equals(oldKey)) {
                    node.keys[index] = newKey; //update old key to new key
                    return;
                }
                node = node.parent;
            }
        }
    }

    static class internalNode<K extends Comparable<? super K>, V> extends Node<K, V> {

        protected Node<K, V>[] children;

        public internalNode(Integer order) {
            super(order);
            this.children = new Node[order + 1]; //number of children = number of keys + 1
        }

        @Override
        public V find(K key) throws IllegalArgumentException {
            int i = 0;
            while (i < this.cnt) {
                if (key.compareTo(keys[i]) < 0)
                    break;
                i++;
            }
            if (this.cnt == i && this.children[this.cnt] == null)
                return null;
            return children[i].find(key);
        }

        @Override
        public Vector<V> findGreater(K key) {
            int i = 0;
            while (i < this.cnt) {
                if (key.compareTo(keys[i]) < 0)
                    break;
                i++;
            }
            if (this.cnt == i && this.children[this.cnt] == null)
                return null;
            return children[i].findGreater(key);
        }

        @Override
        public leafNode<K, V> getFirstLeaf() {
            return this.children[0].getFirstLeaf();
        }

        @Override
        public void update(K key, V value) throws IllegalArgumentException {
            int i;
            for (i = 0; i < this.cnt; i++) {
                if (key.compareTo(this.keys[i]) < 0) {
                    break;
                }
            }
            this.children[i].update(key, value);
        }

        @Override
        public Node<K, V> insert(K key, V value) throws IllegalArgumentException {
            int i;
            for (i = 0; i < this.cnt; i++) {
                if (key.compareTo(this.keys[i]) < 0) {
                    break;
                }
            }
            return this.children[i].insert(key, value);
        }

        private internalNode<K, V> insertAndSplit(K newKey, Node<K, V> newChild, int index) { //insert the new child with the new key into the node at the specific index and split the node into 2
            internalNode<K, V> newNode = new internalNode<>(this.order);
            int pos; //the position to split the node
            if (index < this.order / 2) {
                pos = this.order / 2; //the new key inserted before the middle, thus split at the middle
                this.keys[pos - 1] = null;
                for (int i = pos; i < this.order; i++) {
                    newNode.keys[i - this.order / 2] = this.keys[i];
                    this.children[i].parent = newNode;
                    newNode.children[i - this.order / 2] = this.children[i];
                    this.keys[i] = null;
                    this.children[i] = null;
                }
                this.children[this.order].parent = newNode;
                newNode.children[this.order - this.order / 2] = this.children[this.order];
                this.children[this.order] = null;
                this.cnt = this.order / 2 - 1;
                newNode.cnt = this.order - this.order / 2;
                this.insertIntoArray(newKey, newChild, index);
            } else if (index == this.order / 2) {
                pos = this.order / 2; //the new key inserted at the middle, thus split at the middle
                for (int i = pos; i < this.order; i++) {
                    newNode.keys[i - this.order / 2] = this.keys[i];
                    this.children[i + 1].parent = newNode;
                    newNode.children[i - this.order / 2 + 1] = this.children[i + 1];
                    this.keys[i] = null;
                    this.children[i + 1] = null;
                }
                newChild.parent = newNode;
                newNode.children[0] = newChild;
                this.cnt = this.order / 2;
                newNode.cnt = this.order - this.order / 2;
            } else {
                pos = this.order / 2 + 1; //the new key inserted after the middle, thus split at middle + 1
                this.keys[pos - 1] = null;
                for (int i = pos; i < this.order; i++) {
                    newNode.keys[i - pos] = this.keys[i];
                    this.children[i].parent = newNode;
                    newNode.children[i - pos] = this.children[i];
                    this.keys[i] = null;
                    this.children[i] = null;
                }
                this.children[this.order].parent = newNode;
                newNode.children[this.order - pos] = this.children[this.order];
                this.children[this.order] = null;
                this.cnt = this.order / 2; //update the count
                newNode.cnt = this.order - pos; //update the count
                newNode.insertIntoArray(newKey, newChild, index - pos);
            }
            return newNode;
        }

        private void insertIntoArray(K key, Node<K, V> node, int index) { //new element will be in [index]
            for (int i = this.cnt; i > index; i--) { //move all elements after index forward
                this.keys[i] = this.keys[i - 1];
                this.children[i + 1] = this.children[i];
            }
            this.keys[index] = key; //insert new key and value
            this.children[index + 1] = node;
            node.parent = this;
            this.cnt++;
        }

        public Node<K, V> insert_node(Node<K, V> node, K key) { //insert a node into this
            int index = findIndex(key); //find the index the node should be inserted into
            if (this.cnt < this.order) {
                this.insertIntoArray(key, node, index);
                return null;
            }
            K oldKey; //old key depends on the position to split
            if (index < this.order / 2) {
                oldKey = this.keys[this.order / 2 - 1];
            } else if (index == this.order / 2) {
                oldKey = key;
            } else {
                oldKey = this.keys[this.order / 2];
            }
            internalNode<K, V> newNode = this.insertAndSplit(key, node, index);
            if (this.parent == null) { //this is root
                internalNode<K, V> newParent = new internalNode<>(this.order);
                newParent.keys[0] = oldKey;
                newParent.cnt = 1;
                this.parent = newParent;
                newParent.children[0] = this;
                newNode.parent = newParent;
                newParent.children[1] = newNode;
                return newParent; //new parent becomes the new root
            }
            return this.parent.insert_node(newNode, oldKey);
        }

        @Override
        public Node<K, V> delete(K key) throws IllegalArgumentException {
            int i;
            for (i = 0; i < this.cnt; i++) {
                if (key.compareTo(this.keys[i]) < 0) {
                    break;
                }
            }
            return this.children[i].delete(key);
        }

        private void deleteFromArray(int index) { //delete the key at index and the children at index + 1
            for (int i = index + 1; i < this.cnt; i++) {
                this.keys[i - 1] = this.keys[i];
                this.children[i] = this.children[i + 1];
            }
            this.keys[this.cnt - 1] = null;
            this.children[this.cnt] = null;
            this.cnt--;
        }

        private Node<K, V> delete_leaf_node(leafNode<K, V> node, K key) throws IllegalArgumentException { //delete a leaf node which is this's child
            int index = this.findIndex(key);
            if (this.children[index + 1] != node) { //node isn't child of this
                throw new IllegalArgumentException();
            }
            this.deleteFromArray(index); //delete node

            //Case 3: delete directly
            if (this.cnt >= this.order / 2) {
                return null;
            }
            if (this.isRoot()) {
                if (this.cnt > 0) {
                    return null;
                } else {
                    this.children[0].parent = null;
                    return this.children[0];
                }
            }

            //Case 4: delete and move a children from sibling to this
            internalNode<K, V> left = this.getLeftSibling(),
                    right = this.getRightSibling();
            if (left != null && left.cnt > this.order / 2) { //move a children from left sibling to this
                for (int i = this.cnt; i > 0; i--) {
                    this.keys[i] = this.keys[i - 1];
                    this.children[i + 1] = this.children[i];
                }
                this.keys[0] = this.children[0].keys[0];
                this.children[1] = this.children[0];
                this.children[0] = left.children[left.cnt]; //change the last children of left sibling to the first children of this
                this.children[0].parent = this;
                this.cnt++;
                this.updateDeletedKeys(this.keys[0], left.keys[left.cnt - 1]); //update the keys of this's ancestors
                left.keys[left.cnt - 1] = null;
                left.children[left.cnt] = null;
                left.cnt--;
                return null;
            }
            if (right != null && right.cnt > this.order / 2) { //move a children from right sibling to this
                this.keys[this.cnt] = right.children[0].keys[0];
                this.children[this.cnt + 1] = right.children[0]; //change the first children of left sibling to the last children of this
                this.children[this.cnt + 1].parent = this;
                this.cnt++;
                for (int i = 0; i < right.cnt - 1; i++) {
                    right.keys[i] = right.keys[i + 1];
                    right.children[i] = right.children[i + 1];
                }
                right.children[right.cnt - 1] = right.children[right.cnt];
                right.children[right.cnt] = null;
                right.keys[right.cnt - 1] = null;
                right.cnt--;
                right.updateDeletedKeys(this.keys[this.cnt - 1], right.children[0].keys[0]); //update the keys of right's ancestors
                return null;
            }

            //Case 5: need to merge
            if (right != null) {
                left = this;
            } else if (left != null) {
                right = this;
            } else {
                throw new IllegalArgumentException();
            }
            left.merge(right);
            return this.parent.delete_internal_node(right); //after merging right, right should be deleted from its parent
        }

        private internalNode<K, V> getLeftSibling() {
            int index = this.parent.find_node_index(this);
            return index > 0 ? (internalNode<K, V>) this.parent.children[index - 1] : null;
        }

        private internalNode<K, V> getRightSibling() {
            int index = this.parent.find_node_index(this);
            return index < this.parent.cnt ? (internalNode<K, V>) this.parent.children[index + 1] : null;
        }

        private void merge(internalNode<K, V> right) {
            for (int i = 0; i < right.cnt; i++) {
                this.keys[this.cnt + i + 1] = right.keys[i];
                this.children[this.cnt + i + 1] = right.children[i];
                right.children[i].parent = this;
            }
            this.keys[this.cnt] = right.parent.keys[right.parent.find_node_index(right) - 1];
            this.children[this.cnt + right.cnt + 1] = right.children[right.cnt];
            right.children[right.cnt].parent = this;
            this.cnt += right.cnt + 1;
        }

        private Node<K, V> delete_internal_node(internalNode<K, V> node) throws IllegalArgumentException { //delete an internal node which is this's child
            int index = this.find_node_index(node);
            if (this.children[index] != node) {
                throw new IllegalArgumentException(); //node isn't child
            }
            this.deleteFromArray(index - 1); //delete node

            //Case 3: delete directly
            if (this.cnt >= this.order / 2) {
                return null;
            }
            if (this.isRoot()) {
                if (this.cnt > 0) {
                    return null;
                } else {
                    this.children[0].parent = null;
                    return this.children[0];
                }
            }

            //Case 4: delete and move a children from sibling to this
            internalNode<K, V> left = this.getLeftSibling(),
                    right = this.getRightSibling();
            if (left != null && left.cnt > this.order / 2) { //move a children from left sibling to this
                for (int i = this.cnt; i > 0; i--) {
                    this.keys[i] = this.keys[i - 1];
                    this.children[i + 1] = this.children[i];
                }
                this.keys[0] = this.parent.keys[this.parent.find_node_index(this) - 1];
                this.parent.keys[this.parent.find_node_index(this) - 1] = left.keys[left.cnt - 1]; //update the keys of this's parent
                this.children[1] = this.children[0];
                this.children[0] = left.children[left.cnt]; //change the last children of left sibling to the first children of this
                this.children[0].parent = this;
                this.cnt++;
                left.keys[left.cnt - 1] = null;
                left.children[left.cnt] = null;
                left.cnt--;
                return null;
            }
            if (right != null && right.cnt > this.order / 2) { //move a children from right sibling to this
                this.keys[this.cnt] = right.parent.keys[right.parent.find_node_index(right) - 1];
                right.parent.keys[right.parent.find_node_index(right) - 1] = right.keys[0]; //update the keys of right's parent
                this.children[this.cnt + 1] = right.children[0]; //change the first children of left sibling to the last children of this
                this.children[this.cnt + 1].parent = this;
                this.cnt++;
                for (int i = 0; i < right.cnt - 1; i++) {
                    right.keys[i] = right.keys[i + 1];
                    right.children[i] = right.children[i + 1];
                }
                right.children[right.cnt - 1] = right.children[right.cnt];
                right.children[right.cnt] = null;
                right.keys[right.cnt - 1] = null;
                right.cnt--;
                return null;
            }

            //Case 5: need to merge
            if (right != null) {
                left = this;
            } else if (left != null) {
                right = this;
            } else {
                throw new IllegalArgumentException();
            }
            left.merge(right);
            return this.parent.delete_internal_node(right); //after merging right, right should be deleted from its parent
        }

        private int find_node_index(Node<K, V> node) {
            for (int i = 0; i <= this.cnt; i++) {
                if (this.children[i] == node) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public void print() throws RuntimeException {
            for (int i = 0; i < this.cnt; i++) {
                System.out.println(this.keys[i] + " ");
            }
            System.out.println("\n");
            for (int i = 0; i <= this.cnt; i++) {
                this.children[i].print();
                if (this.children[i].parent != this) {
                    throw new RuntimeException();
                }
            }
        }

        @Override
        public K getLeast() { //get the least key in this's subtree
            return this.children[0].getLeast();
        }

        @Override
        public void checkStructure() throws RuntimeException {
            this.children[0].checkStructure();
            for (int i = 1; i <= this.cnt; i++) {
                if (this.keys[i - 1] != this.children[i].getLeast()) {
                    throw new RuntimeException();
                }
                this.children[i].checkStructure();
            }
        }
    }

    static class leafNode<K extends Comparable<? super K>, V> extends Node<K, V> {

        private V[] values;
        private leafNode<K, V> next; //the next sibling

        public leafNode(Integer order) {
            super(order);
            this.values = (V[]) new Comparable[order];
            this.next = null;
        }

        @Override
        public V find(K key) throws IllegalArgumentException {
            int left = 0, right = this.cnt;
            int index;
            while (left < right) {
                index = (left + right) / 2;
                int cmp = key.compareTo(keys[index]);
                if (cmp < 0) {
                    right = index;
                } else if (cmp > 0) {
                    left = index + 1;
                } else {
                    return values[index];
                }
            }
            throw new IllegalArgumentException(); //not found
        }

        @Override
        public Vector<V> findGreater(K key) {
            Vector<V> res = new Vector<>();
            int index = this.findIndex(key);
            if (index < this.cnt) {
                if (this.keys[index].equals(key)){
                    index++;
                }
                for (int i = index; i < this.cnt; i++) {
                    res.add(this.values[i]);
                }
            }
            leafNode<K, V> next = this.next;
            while (next != null) {
                for (int i = 0; i < next.cnt; i++) {
                    res.add(next.values[i]);
                }
                next = next.next;
            }
            return res;
        }

        @Override
        public leafNode<K, V> getFirstLeaf() {
            return this;
        }

        @Override
        public void update(K key, V value) throws IllegalArgumentException {
            int index = findIndex(key);
            if (index >= this.cnt || !this.keys[index].equals(key)) {
                throw new IllegalArgumentException("Key " + key + " not found"); //not found
            }
            this.values[index] = value;
        }

        @Override
        public Node<K, V> insert(K key, V value) throws IllegalArgumentException {
            //Find the index of the key (binary search)
            int index = findIndex(key);
            if (index < this.cnt && this.keys[index].equals(key)) {
                throw new IllegalArgumentException("Key " + key + " already exists"); //already exists
            }
            if (this.cnt < this.order) { //needn't split
                this.insertIntoArray(key, value, index);
                return null;
            }

            leafNode<K, V> newSibling = this.insertAndSplit(key, value, index); //split
            if (this.parent == null) { //this is root
                internalNode<K, V> newParent = new internalNode<>(this.order);
                newParent.keys[0] = newSibling.keys[0];
                newParent.cnt = 1;
                this.parent = newParent;
                newParent.children[0] = this;
                newSibling.parent = newParent;
                newParent.children[1] = newSibling;
                return newParent; //new parent is the new root
            }
            return this.parent.insert_node(newSibling, newSibling.keys[0]); //insert the new node to this's parent
        }

        private leafNode<K, V> insertAndSplit(K newKey, V newValue, int index) { //insert the new value with the new key into the node at the specific index and split the node into 2
            leafNode<K, V> newSibling = new leafNode<>(this.order);
            int pos; //position to split
            if (index <= this.order / 2) {
                pos = this.order / 2; //the new key inserted before or at the middle, thus split at the middle
                for (int i = pos; i < this.order; i++) {
                    newSibling.keys[i - this.order / 2] = this.keys[i];
                    newSibling.values[i - this.order / 2] = this.values[i];
                    this.keys[i] = null;
                    this.values[i] = null;
                }
                this.cnt = this.order / 2;
                newSibling.cnt = this.order - this.order / 2;
                this.insertIntoArray(newKey, newValue, index);
            } else {
                pos = this.order / 2 + 1; //the new key inserted after the middle, thus split at middle + 1
                for (int i = pos; i < index; i++) {
                    newSibling.keys[i - this.order / 2 - 1] = this.keys[i];
                    newSibling.values[i - this.order / 2 - 1] = this.values[i];
                    this.keys[i] = null;
                    this.values[i] = null;
                }
                newSibling.keys[index - this.order / 2 - 1] = newKey;
                newSibling.values[index - this.order / 2 - 1] = newValue;
                for (int i = index; i < this.order; i++) {
                    newSibling.keys[i - this.order / 2] = this.keys[i];
                    newSibling.values[i - this.order / 2] = this.values[i];
                    this.keys[i] = null;
                    this.values[i] = null;
                }
                this.cnt = this.order / 2 + 1; //update the count
                newSibling.cnt = this.order - this.order / 2; //update the count
            }
            leafNode<K, V> temp = this.next;
            this.next = newSibling;
            newSibling.next = temp;
            return newSibling;
        }

        private void insertIntoArray(K key, V value, int index) { //new element will be in [index]
            for (int i = this.cnt; i > index; i--) { //move all elements after index forward
                this.keys[i] = this.keys[i - 1];
                this.values[i] = this.values[i - 1];
            }
            this.keys[index] = key; //insert new key and value
            this.values[index] = value;
            this.cnt++;
        }

        @Override
        public Node<K, V> delete(K key) throws IllegalArgumentException {
            int index = this.findIndex(key);
            if (!this.keys[index].equals(key)) {
                throw new IllegalArgumentException("Key " + key + " not found");
            }
            this.deleteFromArray(index);

            //Case 1: delete directly
            if (this.isRoot()) {
                return null;
            }
            if (this.cnt >= (this.order + 1) / 2) {
                if (index == 0) {
                    this.updateDeletedKeys(key, this.keys[0]);
                }
                return null;
            }

            //Case 2: delete and move a key from sibling to this
            K oldKey = index == 0 ? key : this.keys[0];
            leafNode<K, V> left = this.getLeftSibling(oldKey),
                    right = this.getRightSibling();
            if (left != null && left.cnt > (this.order + 1) / 2) {
                this.insertIntoArray(left.keys[left.cnt - 1], left.values[left.cnt - 1], 0);
                left.keys[left.cnt - 1] = null;
                left.values[left.cnt - 1] = null;
                left.cnt--;
                this.updateDeletedKeys(oldKey, this.keys[0]);
                return null;
            }
            if (right != null && right.cnt > (this.order + 1) / 2) {
                this.keys[this.cnt] = right.keys[0];
                this.values[this.cnt] = right.values[0];
                this.cnt++;
                right.deleteFromArray(0);
                right.updateDeletedKeys(this.keys[this.cnt - 1], right.keys[0]);
                if (index == 0) {
                    this.updateDeletedKeys(oldKey, this.keys[0]);
                }
                return null;
            }

            //Case 3\4\5: delete and merge
            if (right != null) {
                left = this;
                if (index == 0) {
                    //update the keys of this's ancestors
                    if (this.cnt > 0) {
                        this.updateDeletedKeys(oldKey, this.keys[0]);
                    } else {
                        this.updateDeletedKeys(oldKey, right.keys[0]);
                    }
                }
                oldKey = right.keys[0];
            } else if (left != null) {
                right = this;
            } else {
                throw new IllegalArgumentException();
            }
            left.merge(right);
            return this.parent.delete_leaf_node(right, oldKey); //after merging right, right should be deleted from its parent
        }

        private void deleteFromArray(int index) {
            for (int i = index + 1; i < this.cnt; i++) {
                this.keys[i - 1] = this.keys[i];
                this.values[i - 1] = this.values[i];
            }
            this.keys[this.cnt - 1] = null;
            this.values[this.cnt - 1] = null;
            this.cnt--;
        }

        private leafNode<K, V> getLeftSibling(K oldKey) {
            if (this.parent == null) {
                return null;
            }
            int index = this.parent.findIndex(oldKey);
            leafNode<K, V> left = (leafNode<K, V>)this.parent.children[index];
            if (left.next != this) {
                return null;
            }
            return left;
        }

        private leafNode<K, V> getRightSibling() {
            if (this.parent == null) {
                return null;
            }
            leafNode<K, V> right = this.next;
            if (right == null) {
                return null;
            }
            if (right.parent != this.parent) {
                return null;
            }
            return right;
        }

        private void merge(leafNode<K, V> right) throws IllegalArgumentException {
            if (this.next != right) {
                throw new IllegalArgumentException();
            }
            for (int i = 0; i < right.cnt; i++) {
                this.keys[this.cnt + i] = right.keys[i];
                this.values[this.cnt + i] = right.values[i];
            }
            this.cnt += right.cnt;
            this.next = right.next;
        }

        @Override
        public void print() {
            for (int i = 0; i < this.cnt; i++) {
                System.out.println(this.keys[i] + ":" + this.values[i] + " ");
            }
            System.out.println("\n");
        }

        @Override
        public K getLeast() {
            return this.keys[0];
        }

        @Override
        public void checkStructure() throws RuntimeException {
            return;
        }
    }
}
