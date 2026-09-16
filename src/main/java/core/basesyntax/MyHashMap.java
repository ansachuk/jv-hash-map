package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private Node<K, V>[] table;
    private int size;
    private int capacity;
    private int threshold;

    public MyHashMap() {
        this.table = new Node[DEFAULT_INITIAL_CAPACITY];
        this.size = 0;
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        checkIfExtensionNeedful();

        int index = getIndexByKey(key);

        Node<K, V> newNode = new Node<>(key, value);
        Node<K, V> itemByIndex = table[index];

        if (itemByIndex == null) {
            table[index] = newNode;
        } else {
            while (itemByIndex != null) {
                if (Objects.equals(key, itemByIndex.key)) {
                    itemByIndex.value = value;
                    return;
                }

                if (itemByIndex.next == null) {
                    itemByIndex.next = newNode;
                    break;
                }

                itemByIndex = itemByIndex.next;
            }
        }

        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndexByKey(key);
        Node<K, V> itemByIndex = table[index];

        if (itemByIndex == null) {
            return null;
        }

        while (itemByIndex != null) {
            if (Objects.equals(key, itemByIndex.key)) {
                return itemByIndex.value;
            }

            itemByIndex = itemByIndex.next;
        }

        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private int getIndexByKey(K key) {
        int i = key != null ? key.hashCode() % capacity : 0;
        return i < 0 ? i * -1 : i;
    }

    private void checkIfExtensionNeedful() {
        if (size == threshold) {
            extend();
        }
    }

    private void extend() {
        capacity = capacity * 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);
        Node<K, V>[] newTable = new Node[capacity];

        for (Node<K, V> item : table) {
            while (item != null) {
                int indexInNewTable = getIndexByKey(item.key);
                Node<K, V> itemByIndexInNewTable = newTable[indexInNewTable];
                Node<K, V> newNode = new Node<>(item.key, item.value);

                if (itemByIndexInNewTable == null) {
                    newTable[indexInNewTable] = newNode;
                } else {
                    itemByIndexInNewTable.next = newNode;
                }
                item = item.next;
            }
        }

        table = newTable;
    }

    class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
        }
    }
}
