package io.github.flbaue.vsdemo;

import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by florian on 12.10.15.
 */
public class ServerStarter {


    public static void main(String[] args) throws RemoteException {
        System.setProperty("java.security.policy", "security.policy");
        int port = (args.length > 0) ? Integer.parseInt(args[0]) : 4711;
        DiceRMI obj = new DiceImpl();
        String objName = "DiceRMI";
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        Registry registry = LocateRegistry.getRegistry(port);
        boolean bound = false;
        for (int i = 0; !bound && i < 2; i++) {
            try {
                registry.rebind(objName, obj);
                bound = true;
                System.out.println(objName + " bound to registry, port " + port + ".");
            } catch (RemoteException e) {
                System.out.println("Rebinding " + objName + " failed, " + "retrying ...");
                registry = LocateRegistry.createRegistry(port);
                System.out.println("Registry started on port " + port + ".");
            }
        }
        System.out.println("Dice Server ready.");
    }
}