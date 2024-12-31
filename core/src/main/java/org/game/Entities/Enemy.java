package org.game.Entities;

public class Enemy extends MyGameEntities{
    private String name;
    private short health;

    public Enemy(String name,short health){
        this.name = name;
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getHealth() {
        return health;
    }

    public void setHealth(short health) {
        this.health = health;
    }

    public void introduceText(){
        System.out.println("Hello im "+ this.name + " im back");
    }
}
