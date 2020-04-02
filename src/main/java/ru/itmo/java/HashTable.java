package ru.itmo.java;

public class HashTable {

    private static final int DEFAULT_SIZE = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;
    private double loadFactor;
    private int usedCells;
    private KeyValuePair[] keyValueArray;

    public HashTable(int size, double loadFactor) {
        usedCells = 0;
        this.loadFactor = loadFactor;
        keyValueArray = new KeyValuePair[size];
    }

    public HashTable(int size) {
        this(size, DEFAULT_LOAD_FACTOR);
    }

    public HashTable() {
        this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
    }


    public Object put(Object key, Object value) {
        Object previousValue = null;
        int hashCode = (Math.abs(key.hashCode()) % keyValueArray.length);

        if (get(key) == null) {
            for (int i = hashCode; i < keyValueArray.length; i = (i + 1) % keyValueArray.length) {
                if (keyValueArray[i] == null || keyValueArray[i].isDeleted) {
                    keyValueArray[i] = new KeyValuePair(key, value);
                    ++usedCells;
                    break;
                }
                if (i == hashCode - 1) {
                    break;
                }
            }
        } else {
            for (int i = hashCode; i < keyValueArray.length; i = (i + 1) % keyValueArray.length) {
                if (keyValueArray[i] == null || keyValueArray[i].isDeleted) {
                    continue;
                }
                if (keyValueArray[i].key.equals(key)) {
                    previousValue = keyValueArray[i].value;
                    keyValueArray[i].value = value;
                    keyValueArray[i].isDeleted = false;
                    break;
                }
                if (i == hashCode - 1) {
                    break;
                }
            }
        }

        if ((double) usedCells / keyValueArray.length > loadFactor) {
            resize();
        }

        return previousValue;
    }

    public Object get(Object key) {
        Object result = null;

        int hashCode = (Math.abs(key.hashCode()) % keyValueArray.length);

        for (int i = hashCode; i < keyValueArray.length; i = (i + 1) % keyValueArray.length) {
            if (keyValueArray[i] == null) {
                break;
            }
            if (!keyValueArray[i].isDeleted && keyValueArray[i].key.equals(key)) {
                result = keyValueArray[i].value;
                break;
            }
            if (i == hashCode - 1) {
                break;
            }
        }

        return result;
    }

    public Object remove(Object key) {
        Object result = null;
        int hashCode = (Math.abs(key.hashCode()) % keyValueArray.length);

        for (int i = hashCode; i < keyValueArray.length; i = (i + 1) % keyValueArray.length) {
            if (keyValueArray[i] == null) {
                break;
            }
            if (!keyValueArray[i].isDeleted && keyValueArray[i].key.equals(key)) {
                result = keyValueArray[i].value;
                keyValueArray[i].isDeleted = true;
                --usedCells;
                break;
            }
            if (i == hashCode - 1) {
                break;
            }
        }

        return result;
    }

    public int size() {
        return this.usedCells;
    }

    private void resize() {
        KeyValuePair[] previousKeyValueArray = keyValueArray;
        keyValueArray = new KeyValuePair[keyValueArray.length * 2];
        usedCells = 0;

        for (int i = 0; i < previousKeyValueArray.length; ++i) {
            if (previousKeyValueArray[i] != null && !previousKeyValueArray[i].isDeleted) {
                put(previousKeyValueArray[i].key, previousKeyValueArray[i].value);
            }
        }
    }

    private static class KeyValuePair {
        Object key, value;
        boolean isDeleted;

        KeyValuePair(Object key, Object value) {
            this.key = key;
            this.value = value;
            isDeleted = false;
        }
    }
}