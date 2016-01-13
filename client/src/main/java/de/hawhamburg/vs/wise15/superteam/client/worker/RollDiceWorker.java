package de.hawhamburg.vs.wise15.superteam.client.worker;

import de.hawhamburg.vs.wise15.superteam.client.api.DiceAdapter;
import de.hawhamburg.vs.wise15.superteam.client.callback.CallbackAB;
import de.hawhamburg.vs.wise15.superteam.client.model.Game;
import de.hawhamburg.vs.wise15.superteam.client.model.Roll;

import javax.swing.*;

/**
 * Created by florian on 12.01.16.
 */
public class RollDiceWorker extends SwingWorker<Roll[], Void> {

    private final Game game;
    private final DiceAdapter diceAdapter;
    private final CallbackAB<Roll[], Exception> callback;

    public RollDiceWorker(Game game, DiceAdapter diceAdapter, CallbackAB<Roll[], Exception> callback) {

        this.game = game;
        this.diceAdapter = diceAdapter;
        this.callback = callback;
    }

    @Override
    protected Roll[] doInBackground() throws Exception {
        Roll roll1 = diceAdapter.rollDice(game.getComponents().getDice());
        Roll roll2 = diceAdapter.rollDice(game.getComponents().getDice());
        Roll[] rolls = {roll1, roll2};
        return rolls;
    }

    @Override
    protected void done() {
        try {
            callback.callback(get(), null);
        } catch (Exception e) {
            callback.callback(null, e);
        }
    }
}
