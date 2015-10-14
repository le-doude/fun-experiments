package org.ledoude;

/**
 * Created by edouard on 14/10/2015.
 */
public interface Hash<T> {

    public static final class Defaults {

        public final static Hash<String> JAVA_HASH = new Hash<String>() {
            @Override
            public int hash(String s) {
                return s.hashCode();
            }
        };

    }

    static long intToUnsignedLong(int i) {
        return 0L | i;
    }

    int hash(T t);

}
