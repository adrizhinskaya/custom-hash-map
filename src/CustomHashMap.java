import java.util.LinkedList;

class CustomHashMap<K, V> {
    private static class HashNode<K, V> {
        K key;
        V value;

        HashNode(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private LinkedList<HashNode<K, V>>[] map;
    private int size;
    private int capacity;
    private static final float LOAD_FACTOR = 0.75f;

    public CustomHashMap() {
        this.capacity = 16;
        this.size = 0;
        this.map = new LinkedList[capacity];
        fillMap(map);
    }

    private void fillMap(LinkedList<HashNode<K, V>>[] map) {
        for (int i = 0; i < capacity; i++) {
            map[i] = new LinkedList<>();
        }
    }

    private int getHash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }

    public boolean checkCapacity() {
        return !(size >= capacity * LOAD_FACTOR);
    }

    private void resize() {
        capacity *= 2;
        // Создать мапу с новым количеством ячеек (корзин)
        LinkedList<HashNode<K, V>>[] newMap = new LinkedList[capacity];
        fillMap(newMap);

        // Все ключи из старой мапы заново хешируются и перераспределяются в новую мапу
        for (LinkedList<HashNode<K, V>> bucket : map) {
            if (!bucket.isEmpty()) {
                for (HashNode<K, V> node : bucket) {
                    putBucket(newMap, node.key, node.value);// Перераспределяем узлы из старой мапы в новую
                }
            }
        }
        map = newMap;
    }

    public void putBucket(LinkedList<HashNode<K, V>>[] map, K key, V value) {
        int index = getHash(key);
        LinkedList<HashNode<K, V>> bucket = map[index];
        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
        }
        bucket.add(new HashNode<>(key, value));
    }

    public void put(K key, V value) {
        if (!checkCapacity()) {
            resize();
        }
        putBucket(map, key, value);
        size++;
    }

    public V get(K key) {
        int index = getHash(key);
        LinkedList<HashNode<K, V>> bucket = map[index];

        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    public void remove(K key) {
        int index = getHash(key);
        LinkedList<HashNode<K, V>> bucket = map[index];

        for (HashNode<K, V> node : bucket) {
            if (node.key.equals(key)) {
                bucket.remove(node);
                size--;
                return;
            }
        }
    }

    public static void main(String[] args) {
        CustomHashMap<String, Integer> customMap = new CustomHashMap<>();
        customMap.put("apple", 1);
        customMap.put("banana", 2);
        customMap.put("apricot", 3);
        customMap.put("pineapple", 4);
        customMap.put("pear", 5);
        customMap.put("grapefruit", 6);
        customMap.put("kiwi", 7);
        customMap.put("lime", 8);
        customMap.put("lemon", 9);
        customMap.put("mango", 10);
        customMap.put("orange", 11);
        customMap.put("peach", 12);
        customMap.put("nectarine", 13);// Меняется capacity. Пересчитываются хэши

        System.out.println(customMap.get("apple")); // Вывод: 1
        System.out.println(customMap.get("banana")); // Вывод: 2

        customMap.remove("apple");
        System.out.println(customMap.get("apple")); // Вывод: null
    }
}
