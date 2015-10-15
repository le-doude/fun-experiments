package org.ledoude;

/**
 * Created by edouard on 14/10/2015.
 */
public interface Hash<T> {

    final class Defaults {
        public final static Hash<String> JAVA_HASH = new Hash<String>() {
            @Override
            public int hash(String s) {
                return s.hashCode();
            }

            @Override
            public String toString() {
                return "Object::hashCode";
            }
        };
    }

    int hash(T t);

}
