package io.github.flbaue.vsdemo;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

/**
 * Created by florian on 12.10.15.
 */
public class DiceImpl extends UnicastRemoteObject implements DiceRMI {

    private final Random random = new Random();

    protected DiceImpl() throws RemoteException {
        super();
    }

    protected DiceImpl(int port) throws RemoteException {
        super(port);
    }

    protected DiceImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public Roll roll() throws RemoteException {


        return new Roll(random.nextInt(6) + 1);
    }
}
