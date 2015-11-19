package de.hawhamburg.vs.wise15.superteam.client.components;

import de.hawhamburg.vs.wise15.superteam.client.model.Service;

/**
 * Created by florian on 19.11.15.
 */
public class ComponentsHolder {

    private Service gameService;
    private Service boardsService;
    private Service cardsService;
    private Service banksService;
    private Service jailService;
    private Service brokersService;
    private Service playerService;
    private Service diceService;

    public Service getGameService() {
        return gameService;
    }

    public void setGameService(Service gameService) {
        this.gameService = gameService;
    }

    public Service getBoardsService() {
        return boardsService;
    }

    public void setBoardsService(Service boardsService) {
        this.boardsService = boardsService;
    }

    public Service getCardsService() {
        return cardsService;
    }

    public void setCardsService(Service cardsService) {
        this.cardsService = cardsService;
    }

    public Service getBanksService() {
        return banksService;
    }

    public void setBanksService(Service banksService) {
        this.banksService = banksService;
    }

    public Service getJailService() {
        return jailService;
    }

    public void setJailService(Service jailService) {
        this.jailService = jailService;
    }

    public Service getBrokersService() {
        return brokersService;
    }

    public void setBrokersService(Service brokersService) {
        this.brokersService = brokersService;
    }

    public Service getPlayerService() {
        return playerService;
    }

    public void setPlayerService(Service playerService) {
        this.playerService = playerService;
    }

    public Service getDiceService() {
        return diceService;
    }

    public void setDiceService(Service diceService) {
        this.diceService = diceService;
    }

    public void setService(Service service, ComponentType type) {
        //TODO
        switch (type) {
            case games:
                setGameService(service); break;
        }
    }
}
