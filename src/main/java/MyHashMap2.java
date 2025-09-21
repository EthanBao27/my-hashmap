/**
 * @author baoyuxiang
 * 哈希表手写
 * 使用拉链法解决哈希冲突
 * 数组长度在适合的时候自动扩容
 * 保持数组大小为2^n 这样就可以实现满映射length-1 充分利用空间
 */
public class MyHashMap2<K, V> implements MyHashMap<K, V> {

	Node<K, V>[] table = new Node[16];

	private int size = 0;

	@Override
	public V put(K key, V value) {
		int keyIndex = indexOf(key);
		Node<K, V> kvNode = table[keyIndex];
		if (kvNode == null) {
			table[keyIndex] = new Node<>(key, value);
			size++;
			resizeIfNecessary();
			return null;
		}
		while (true) {
			if (kvNode.key.equals(key)) {
				V oldValue = kvNode.value;
				kvNode.value = value;
				return oldValue;
			}
			if (kvNode.next == null) {
				kvNode.next = new Node<>(key, value);
				size++;
				resizeIfNecessary();
				return null;
			}
			kvNode = kvNode.next;
		}
	}

	@Override
	public V get(K key) {
		int keyIndex = indexOf(key);
		Node<K, V> kvNode = table[keyIndex];
		if (kvNode == null) {
			return null;
		}
		while (kvNode != null) {
			if (kvNode.key.equals(key)) {
				return kvNode.value;
			}
			kvNode = kvNode.next;
		}
		return null;
	}

	@Override
	public V remove(K key) {
		int keyIndex = indexOf(key);
		Node<K, V> head = table[keyIndex];
		if (head == null) {
			return null;
		}
		if (head.key.equals(key)) {
			table[keyIndex] = head.next;
			size--;
			return head.value;
		}
		Node<K, V> pre = head;
		Node<K, V> current = head.next;
		while (current != null) {
			if (current.key.equals(key)) {
				pre.next = current.next;
				size--;
				return current.value;
			}
			pre = pre.next;
			current = current.next;
		}
		return null;
	}

	private void resizeIfNecessary() {
		if (this.size < table.length * 0.75) {
			return;
		}
		Node<K, V>[] newTable = new Node[this.table.length * 2];
		for (Node<K, V> head : this.table) {
			if (head == null) {
				continue;
			}
			Node<K, V> current = head;
			while (current != null) {
				int newIndex = current.key.hashCode() & (newTable.length - 1);
				if (newTable[newIndex] == null) {
					newTable[newIndex] = current;
					Node<K, V> next = current.next;
					current.next = null;
					current = next;
				} else {
					// 头插法
					Node<K, V> next = current.next;
					current.next = newTable[newIndex];
					newTable[newIndex] = current;
					current = next;
				}
			}
		}
		this.table = newTable;
		System.out.println("扩容了，扩容到" + this.table.length);
	}

	@Override
	public int size() {
		return size;
	}

	private int indexOf(Object key) {
		return key.hashCode() & (table.length - 1);
	}

	class Node<K, V> {
		Node<K, V> next;
		K key;
		V value;

		public Node(K key, V value) {
			this.key = key;
			this.value = value;
		}
	}
}
