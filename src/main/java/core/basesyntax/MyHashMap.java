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
        Node<K, V> itemByIndex = table[index];

        if (itemByIndex == null) {
            table[index] = new Node<>(key, value);
            size++;
            return;
        }

        while (true) {
            if (Objects.equals(key, itemByIndex.key)) {
                itemByIndex.value = value;
                return;
            }

            if (itemByIndex.next == null) {
                itemByIndex.next = new Node<>(key, value);
                size++;
                return;
            }

            itemByIndex = itemByIndex.next;
        }
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
        capacity *= 2;
        threshold = (int) (capacity * DEFAULT_LOAD_FACTOR);

        Node<K, V>[] newTable = new Node[capacity];

        for (Node<K, V> item : table) {
            while (item != null) {
                Node<K, V> next = item.next;

                int index = getIndexByKey(item.key);

                item.next = null;

                if (newTable[index] == null) {
                    newTable[index] = item;
                } else {
                    Node<K, V> current = newTable[index];

                    while (current.next != null) {
                        current = current.next;
                    }

                    current.next = item;
                }

                item = next;
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
