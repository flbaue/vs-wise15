package de.hawhamburg.vs.wise15.superteam.client.callback;

/**
 * Created by florian on 07.12.15.
 */
public interface CallbackABC<A, B, C> {

    void callback(A a, B b, C c);
}
