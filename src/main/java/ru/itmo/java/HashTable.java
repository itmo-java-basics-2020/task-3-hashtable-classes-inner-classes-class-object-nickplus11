package ru.itmo.java;

public class HashTable {

    private Entry[] keyValueArray;
    private int elementsCount = 0;      // count of used cells
    private float loadFactor = 0.5f;
    private int threshold = 0;          // number of cells when we have to resize the table


    HashTable() {
        keyValueArray = new Entry[8];
    }

    HashTable(int newSize) {
        keyValueArray = new Entry[newSize];
    }

    HashTable(int newSize, float loadFactor) {
        this.loadFactor = loadFactor;
        keyValueArray = new Entry[newSize];
    }

    Object put(Object key, Object value) {

        for (int i = Math.abs(key.hashCode()) % keyValueArray.length; i < keyValueArray.length; ++i) {
            if (keyValueArray[i] == null || keyValueArray[i].deleted && keyValueArray[i].key.equals(key)) {
                keyValueArray[i] = new Entry(key, value);
                ++elementsCount;
                resize();
                return null;
            }
            if (keyValueArray[i].key.equals(key)) {
                Object save = keyValueArray[i].value;
                keyValueArray[i].value = value;
                return save;
            }
            if (i == keyValueArray.length - 1) {
                i = 0;
            }
        }

        return null;
    }

    Object get(Object key) {
        for (int i = Math.abs(key.hashCode()) % keyValueArray.length; i < keyValueArray.length; ++i) {
            if (keyValueArray[i] == null) {
                return null;
            }
            if (keyValueArray[i].key.equals(key) && !keyValueArray[i].deleted) {
                return keyValueArray[i].value;
            }
            if (i == keyValueArray.length - 1) {
                i = 0;
            }
        }

        return null;
    }

    Object remove(Object key) {

        for (int i = Math.abs(key.hashCode()) % keyValueArray.length; i < keyValueArray.length; ++i) {
            if (keyValueArray[i] == null) {
                return null;
            }
            if (keyValueArray[i].key.equals(key) && !keyValueArray[i].deleted) {
                keyValueArray[i].deleted = true;
                --elementsCount;
                return keyValueArray[i].value;
            }
            if (i == keyValueArray.length - 1) {
                i = 0;
            }
        }

        return null;
    }

    private void resize() {
        if (this.size() < getThreshold()) {
            return;
        }

        Entry[] save = keyValueArray;
        keyValueArray = new Entry[save.length * 2];
        threshold = (int) (keyValueArray.length * loadFactor);
        elementsCount = 0;
        for (int i = 0; i < save.length; ++i) {
            if (save[i] != null && !save[i].deleted) {
                put(save[i].key, save[i].value);
            }
        }
    }

    int getThreshold() {
        threshold = (int) (keyValueArray.length * loadFactor);
        return threshold;
    }

    int size() {
        return elementsCount;
    }

    float getLoadFactor() {
        return loadFactor;
    }

    private class Entry {
        private Object key, value;
        private boolean deleted = false;

        Entry(Object newKey, Object newValue) {
            key = newKey;
            value = newValue;
        }
    }
}
