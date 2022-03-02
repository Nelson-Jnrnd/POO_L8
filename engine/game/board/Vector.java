package engine.game.board;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Vector modelisation
 * @author Alen Bijelic
 * @author Nelson Jeanrenaud
 */
public class Vector {
    private int i;
    private int j;

    /**
     * Vector constructor
     * @param i Index i of the vector
     * @param j Index j of the vector
     */
    public Vector(int i, int j) {
        this.i = i;
        this.j = j;
    }

    /**
     * Get the i index
     * @return i index
     */
    public int getI() {
        return i;
    }

    /**
     * Get the j index
     * @return j index
     */
    public int getJ() {
        return j;
    }

    /**
     * Set the i index
     * @param i Index to be set
     */
    public void setI(int i) {
        this.i = i;
    }

    /**
     * Set the j index
     * @param j Index to be set
     */
    public void setJ(int j) {
        this.j = j;
    }

    /**
     * Perform addition with another Vector
     * @param other Vector to be added to this
     * @return The new obtained vector
     */
    public Vector add(Vector other){
        Objects.requireNonNull(other, "other vector must be non null");
        return new Vector(i + other.i, j + other.j);
    }

    /**
     * Perform substraction with another Vector
     * @param other Vector to be substracted to this
     * @return The new obtained vector
     */
    public Vector sub(Vector other){
        Objects.requireNonNull(other, "other vector must be non null");
        return new Vector(i - other.i, j - other.j);
    }

    /**
     * Perform multiplication with a factor
     * @param factor The factor to apply to the vector
     * @return The new obtained vector
     */
    public Vector multiply(int factor){
        return new Vector(factor * i, factor * j);
    }

    /**
     * Get the vector norm
     * @return Vector norm
     */
    public double norm(){
        return Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
    }

    /**
     * Get the smallest collinear Vector
     * @return Smallest collinear Vector
     */
    public Vector getSmallestCollinearVector(){
        int gcd = gcd(i, j);
        if(gcd != 0)
            return new Vector(i / gcd, j / gcd);
        return new Vector(i, j);
    }

    /**
     * Lists all included vectors
     * @return All included vectors
     */
    public ArrayList<Vector> includedVectors(){
        ArrayList<Vector> includedVectors = new ArrayList<>();
        Vector base = getSmallestCollinearVector();
        int nb = Math.abs(gcd(i, j));
        for (int factor = 1; factor < nb; factor++) {
            includedVectors.add(base.multiply(factor));
        }
        return includedVectors;
    }

    /**
     * Perform a cross product
     * @param other Vector to perform cross product
     * @return Cross product between both vectors
     */
    public int crossProduct(Vector other){
        Objects.requireNonNull(other, "other vector must be non null");
        return i * other.j - j * other.i;
    }

    /**
     * Check if vectors are collinear
     * @param other Vector to check with
     * @return Either the vetors are collinear or not
     */
    public boolean areCollinear(Vector other){
        return crossProduct(Objects.requireNonNull(other, "other vector must be non null")) == 0;
    }

    /**
     * Check if vectors are collinear and also in the same direction
     * @param other Vector to check with
     * @return Either the vetors are collinear and in the same direction or not
     */
    public boolean areCollinearAndSameDirection(Vector other) {
        return getSmallestCollinearVector().equals(other.getSmallestCollinearVector());
    }

    /**
     * Get vertically mirrored vector
     * @return Vertically mirrored vector
     */
    public Vector getMirrorYVector(){
        return new Vector(-i, j);
    }

    /**
     * Get horizontally mirrored vector
     * @return Horizontally mirrored vector
     */
    public Vector getMirrorXVector(){
        return new Vector(i, -j);
    }

    /**
     * Get opposed vector
     * @return Opposed vector
     */
    public Vector getOpposedVector(){
        return new Vector(-i, -j);
    }

    /**
     * Get the greatest common divisor between two values
     * @param a Value A
     * @param b Value B
     * @return The greatest common divisor
     */
    private static int gcd(int a, int b) {
        a = Math.abs(a);
        b = Math.abs(b);
        if (a == 0) return b;
        if (b == 0) return a;
        if (a > b) return gcd(b, a);
        return gcd(b%a, a);
    }

    /**
     * Transform Vector to a String representation
     * @return String of a Vector
     */
    @Override
    public String toString() {
        return "Vector{" +
                "i=" + i +
                ", j=" + j +
                '}';
    }

    /**
     * Check if the vector is equal to an object
     * @param o Object to check equality
     * @return Either the vector and the object are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return i == vector.i && j == vector.j;
    }

    /**
     * Get the hash code of the vector
     * @return Hash code of the vector
     */
    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}