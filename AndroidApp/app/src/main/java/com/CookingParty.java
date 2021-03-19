package com;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class CookingParty {

    private int partyId;
    private List<Integer> recipeId;
    private int creatorId;
    private List<Integer> participants;
    private String address;
    @SerializedName("location")
    private String locationString;

    //Array used to store the location as numbers, so they can more easily be used elsewhere
    private double[] location = new double[2];

    public CookingParty(int partyId, List<Integer> recipeId, int creatorId,
                        List<Integer> participants, String address, String location) {
        this.partyId = partyId;
        this.recipeId = recipeId;
        this.creatorId = creatorId;
        this.participants = participants;
        this.address = address;
        this.locationString = location;

        getLocationFromString(location);
    }

    public int getPartyId() {
        return partyId;
    }

    public List<Integer> getRecipeId() {
        return recipeId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public List<Integer> getParticipants() {
        return participants;
    }

    public String getAddress() {
        return address;
    }

    public String getLocationString() {
        return locationString;
    }

    public double[] getLocation(){
        return location;
    }

    public void getLocationFromString(String locationString){
        String[] numbers = locationString.split(",");
        this.location[0] = Double.parseDouble(numbers[0]);
        this.location[1] = Double.parseDouble(numbers[1]);
    }

    public void setLocation(float longitude, float latitude){
        this.location[0] = longitude;
        this.location[1] = latitude;
        this.locationString = longitude + "," + latitude;
    }
}
