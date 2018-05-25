package model;

public enum Menu {
    WOMEN("Women"),
    DRESSES("Dresses"),
    T_SHIRTS("T-shirts"),
    SUMMER_DRESSES("Summer Dresses");

    private String item;

    Menu(String item){
        this.item = item;
    }
    public String item() {
            return item;
    }
}