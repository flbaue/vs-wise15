package de.haw_hamburg.vs.wise15.superteam.brokers;

/**
 * Created by masha on 16.11.15.
 */
public class Place {

    private String placeId;
    private String name;
    private String ownerId;   //PlayerId

    public Place(String placeid){
        this.placeId=placeid;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
