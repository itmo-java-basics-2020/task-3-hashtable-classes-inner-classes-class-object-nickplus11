package ru.itmo.java;

public class HashTable {


    private static final int DEFAULT_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;
    private double loadFactor;
    private int threshold, usedCells;
    private Entry[] keyValueArray;

    public HashTable(int size, double loadFactor) {
        keyValueArray = new Entry[size];
        for (int i = 0; i < keyValueArray.length; i++) {
            keyValueArray[i] = new Entry(null, null);
        }
        this.loadFactor = loadFactor;
        this.threshold = (int) (this.loadFactor * this.keyValueArray.length);
        this.usedCells = 0;
    }

    public HashTable(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public HashTable() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }

    private static class Entry {
        Object key, value;
        int hashCode;
        boolean deleted = false;

        public Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
            this.hashCode = this.key != null ? Math.abs(this.key.hashCode()) : -1;
        }
    }

    public Object put(Object key, Object value) {

        Entry newObject = new Entry(key, value);
        Object out = null;
        Integer index = getIndex(key);

        if (index != null) {
            out = keyValueArray[index].value;
            keyValueArray[index].value = value;
        } else {
            if (this.usedCells >= threshold) {
                resize();
            }

            index = newObject.hashCode % keyValueArray.length;

            while (keyValueArray[index].key != null) {
                index++;
                if (index == keyValueArray.length) {
                    index = 0;
                }
            }

            keyValueArray[index] = newObject;
            ++usedCells;
        }

        return out;
    }

    public Object get(Object key) {

        Integer indexInArray = getIndex(key);

        if (indexInArray == null) {
            return null;
        }

        return keyValueArray[indexInArray].value;
    }

    public Object remove(Object key) {

        Integer index = getIndex(key);
        Object result = null;

        if (index != null) {
            result = keyValueArray[index].value;
            keyValueArray[index].deleted = true;
            keyValueArray[index].key = null;
            keyValueArray[index].value = null;
            keyValueArray[index].hashCode = -1;
            --usedCells;
        }

        return result;
    }

    public int size() {
        return this.usedCells;
    }

    private void resize() {
        Entry[] oldData = this.keyValueArray;
        this.keyValueArray = new Entry[oldData.length * 2];
        usedCells = 0;
        threshold = (int) (this.keyValueArray.length * loadFactor);
        for (int i = 0; i < keyValueArray.length; i++) {
            keyValueArray[i] = new Entry(null, null);
        }

        for (int i = 0; i < oldData.length; i++) {
            if (oldData[i] == null) {
                continue;
            }
            if (oldData[i].hashCode != -1) {
                put(oldData[i].key, oldData[i].value);
            }
        }
    }

    private Integer getIndex(Object key) {
        int hash = Math.abs(key.hashCode());
        int firstIndex = hash % keyValueArray.length;
        Integer index = null;

        for (int i = firstIndex; i < keyValueArray.length; i++) {
            if (keyValueArray[i] == null) {
                return index;
            }
            if (keyValueArray[i].hashCode == hash && keyValueArray[i].key.equals(key)) {
                index = i;
                break;
            }
            if (i == keyValueArray.length - 1) {
                i = -1;
            }
            if (i == firstIndex - 1 ||
                    (i != -1 && keyValueArray[i].value == null && !keyValueArray[i].deleted)) {
                index = null;
                break;
            }
        }

        return index;
    }
}