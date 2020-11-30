package api;

/**
 * This class represents an ordered pair.
 * It contains a left and a right components, both are generic.
 * The user can set the left and the right component and get them using the relevant getters and setters.
 *
 * @param <K> - The left component of the pair.
 * @param <V> - The right component of the pair.
 * @author Tal.Zichlinsky
 */
public class OrderedPair<K, V> implements Comparable<OrderedPair<node_data, Double>> {
    private K left;
    private V right;

    public OrderedPair() {
        this.left = null;
        this.right = null;
    }

    public OrderedPair(K left, V right) {
        this.left = left;
        this.right = right;
    }

    public Object getLeft() {
        return left;
    }

    public Object getRight() {
        return right;
    }

    public void setLeft(K left) {
        this.left = left;
    }

    public void setRight(V right) {
        this.right = right;
    }

    @Override
    public int compareTo(OrderedPair<node_data, Double> other) {
        Double thisRight = (Double) this.getRight();
        Double otherRight = (Double) other.getRight();
        if (thisRight > otherRight) return 1;
        else if (thisRight == otherRight) return 0;
        else return -1;
    }

    public boolean equals(OrderedPair<node_data, Double> other) {
        if (other == null) return false;
        return (this.getRight() == other.getRight() && this.getLeft() == other.getLeft());
    }

    @Override
    public String toString() {
        return "ex1.src.OrderedPair{" +
                "left=" + left +
                ", right=" + right +
                '}';
    }

}
