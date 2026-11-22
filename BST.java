public class BST<K extends Comparable<K>, T> implements IBST<K, T> {

    private BSTNode<K, T> root;
    private BSTNode<K, T> current;
    private int n;

    public BST() {
        root = null;
        current = null;
        n = 0;
    }

    @Override
    public boolean empty() {
        return root == null;
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean findKey(K key) {
        if (key == null) {
            return false;
        }

        BSTNode<K, T> p = root;

        while (p != null) {
            int comparison = key.compareTo(p.key);
            if (comparison == 0) {
                current = p;
                return true;
            }

            p = comparison < 0 ? p.left : p.right;
        }

        current = null;

        return false;
    }

    @Override
    public T retrieve() {
        return current == null ? null : current.data;
    }

    @Override
    public boolean insert(K key, T data) {
        if (key == null) {
            return false;
        }

        BSTNode<K, T> parent = null;
        BSTNode<K, T> p = root;

        while (p != null) {
            int comparison = key.compareTo(p.key);
            if (comparison == 0) {
                current = p;

                return false;
            }

            parent = p;
            p = comparison < 0 ? p.left : p.right;
        }

        BSTNode<K, T> newNode = new BSTNode<K, T>(key, data);

        if (parent == null) {
            root = newNode;
        } 
        else if (key.compareTo(parent.key) < 0) {
            parent.left = newNode;
        } 
        else {
            parent.right = newNode;
        }

        current = newNode;
        n++;

        return true;
    }

    @Override
    public boolean removeKey(K key) {
        if (key == null) {
            return false;
        }
        
        BSTNode<K, T> parent = null;
        BSTNode<K, T> p = root;

        while (p != null) {
            int comparison = key.compareTo(p.key);
            if (comparison == 0) {
                break;
            }
            parent = p;
            p = comparison < 0 ? p.left : p.right;
        }

        if (p == null) {
            return false;
        }

        BSTNode<K, T> replacement;

        if (p.left != null && p.right != null) {
            BSTNode<K, T> successorParent = p;
            BSTNode<K, T> successor = p.right;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            p.key = successor.key;
            p.data = successor.data;

            if (successorParent == p) {
                p.right = successor.right;
            } 
            else {
                successorParent.left = successor.right;
            }
        } else {
            replacement = p.left != null ? p.left : p.right;
            if (parent == null) {
                root = replacement;
            } 
            else if (parent.left == p) {
                parent.left = replacement;
            } 
            else {
                parent.right = replacement;
            }
        }
        
        n--;
        current = root;
        return true;
    }

    @Override
    public LinkedList<T> inOrder() {
        LinkedList<T> result = new LinkedList<T>();
        inOrder(root, result);
        return result;
    }

    private void inOrder(BSTNode<K, T> node, LinkedList<T> result) {
        if (node == null) {
            return;
        }
        inOrder(node.left, result);
        result.insert(node.data);
        inOrder(node.right, result);
    }

    @Override
    public LinkedList<T> rangeSearch(K low, K high) {
        LinkedList<T> result = new LinkedList<T>();
        if (low == null || high == null || low.compareTo(high) > 0) {
            return result;
        }
        rangeSearch(root, low, high, result);
        return result;
    }

    private void rangeSearch(BSTNode<K, T> node, K low, K high, LinkedList<T> result) {
        if (node == null) {
            return;
        }
        if (low.compareTo(node.key) < 0) {
            rangeSearch(node.left, low, high, result);
        }
        if (low.compareTo(node.key) <= 0 && node.key.compareTo(high) <= 0) {
            result.insert(node.data);
        }
        if (node.key.compareTo(high) < 0) {
            rangeSearch(node.right, low, high, result);
        }
    }

    @Override
    public int height() {
        return height(root);
    }

    private int height(BSTNode<K, T> node) {
        if (node == null) {
            return 0;
        }
        int left = height(node.left);
        int right = height(node.right);
        return 1 + (left > right ? left : right);
    }

    @Override
    public int nbKeyComp(K key) {
        if (key == null || root == null) {
            return 0;
        }
        int comparisons = 0;
        BSTNode<K, T> p = root;
        while (p != null) {
            comparisons++;
            int comparison = key.compareTo(p.key);
            if (comparison == 0) {
                return comparisons;
            }
            p = comparison < 0 ? p.left : p.right;
        }
        return comparisons;
    }
}
