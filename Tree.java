package treeoflife;

public class Tree {
    public String name;
    public String fruitName;
    public int buyPrice;
    public int fruitPerHarvest;
    public int cooldown;      // aantal rondes tussen harvests
    public int cooldownLeft;  // hoeveel rondes nog wachten

    public Tree(String name, String fruitName, int buyPrice, int fruitPerHarvest, int cooldown) {
        this.name = name;
        this.fruitName = fruitName;
        this.buyPrice = buyPrice;
        this.fruitPerHarvest = fruitPerHarvest;
        this.cooldown = cooldown;
        this.cooldownLeft = 0; // start direct klaar om te oogsten
    }


    public boolean canHarvest() {
        return cooldownLeft == 0;
    }

    public int harvest() {
        if (canHarvest()) {
            cooldownLeft = cooldown;
            return fruitPerHarvest;
        } else {
            return 0;
        }
    }

    public void nextRound() {
        if (cooldownLeft > 0) cooldownLeft--;
    }
}


