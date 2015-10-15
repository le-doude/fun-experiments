package org.ledoude;

/**
 * Created by edouard_pelosi on 10/15/15.
 */
public interface Byter<T> {

    byte[] bytes(T t);

}
