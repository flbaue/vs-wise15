package io.github.flbaue.vsdemo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by florian on 12.10.15.
 */
public interface DiceRMI extends Remote {
    Roll roll() throws RemoteException;
}
