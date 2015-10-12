package io.github.flbaue.vsdemo;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

/**
 * Created by florian on 12.10.15.
 */
public class ClientStarter {

    static public void main(String[] args) /*args[0]: host, args[1]: port*/ {
        System.setProperty("java.security.policy", "security.policy");
        String host = (args.length < 1) ? "localhost" : args[0];
        int port = (args.length < 2) ? 4711 : Integer.parseInt(args[1]);
        try {
            if (System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
            DiceRMI obj = (DiceRMI) Naming.lookup("rmi://" + host + ":" + port + "/" + "DiceRMI");
            System.out.println("Roll Dice: " + obj.roll().getNumber());
            System.out.println("Roll Dice: " + obj.roll().getNumber());
            System.out.println("Roll Dice: " + obj.roll().getNumber());
            System.out.println("Roll Dice: " + obj.roll().getNumber());
            System.out.println("Roll Dice: " + obj.roll().getNumber());
        } catch (Exception e) {
            System.out.println("HelloClient failed, caught exception " + e.getMessage());
        }
    }
}
