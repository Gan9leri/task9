package JsonModels;

public class Fridge {

    String title;
    Boolean selfDefrosting;
    Capacity capacity;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getSelfDefrosting() {
        return selfDefrosting;
    }

    public void setSelfDefrosting(Boolean selfDefrosting) {
        this.selfDefrosting = selfDefrosting;
    }

    public Capacity getCapacity() {
        return capacity;
    }

    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }
}