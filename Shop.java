package treeoflife;

import java.util.*;

public class Shop {
    private Map<String, Tree> treeCatalog = new HashMap<>();

    public Shop() {
        // Voeg hier de soorten bomen toe die je kunt kopen
        treeCatalog.put("Appelboom", new Tree("Appelboom", "Appels", config.APPELBOOM_PRICE, 2, config.APPELBOOM_COOLDOWN));
        treeCatalog.put("Bananenplant", new Tree("Bananenplant", "Bananen", config.BANANENPLANT_PRICE, 2, config.BANANENPLANT_COOLDOWN));
        treeCatalog.put("Mangoboom", new Tree("Mangoboom", "Mango's", config.MANGOBOOM_PRICE, 4, config.MANGOBOOM_COOLDOWN));
        treeCatalog.put("Avocadoboom", new Tree("Avocadoboom", "Avocado's", config.AVOCADOBOOM_PRICE, 3, config.AVOCADOBOOM_COOLDOWN));
    	treeCatalog.put("Citroenboom", new Tree("Citroenboom", "Citroenen", config.CITROENBOOM_PRICE, 2, config.CITROENBOOM_COOLDOWN));
    	treeCatalog.put("Perenboom", new Tree("Perenboom", "Peren", config.PERENBOOM_PRICE, 2, config.PERENBOOM_COOLDOWN));
    	treeCatalog.put("Kersenboom", new Tree("Kersenboom", "Kersen", config.KERSENBOOM_PRICE, 3, config.KERSENBOOM_COOLDOWN));
    	treeCatalog.put("Druivenplant", new Tree("Druivenplant", "Druiven", config.DRUIVENPLANT_PRICE, 5, config.DRUIVENPLANT_COOLDOWN));
        treeCatalog.put("Ananasstruik", new Tree("Ananasstruik", "Ananassen", config.ANANASSTRUIK_PRICE, 2, config.ANANASSTRUIK_COOLDOWN));
    	treeCatalog.put("Spaceweedplant", new Tree("Spaceweedplant", "Spaceweed", config.SPACEWEEDPLANT_PRICE, 1, config.SPACEWEEDPLANT_COOLDOWN));
    }

    public void showAvailableTrees() {
        System.out.println("🌳 Beschikbare bomen:");
        int index = 1;
        for (Tree tree : treeCatalog.values()) {
            System.out.println(index + ". " + tree.name + " (€" + tree.buyPrice + ") → " +
                    tree.fruitPerHarvest + "x " + tree.fruitName + " per " + tree.cooldown + " ronde(s)");
            index++;
        }
    }
    
    public void buyTreeByIndex(Player player, int index) {
        List<Tree> treeList = new ArrayList<>(treeCatalog.values());

        // Check of index geldig is
        if (index < 1 || index > treeList.size()) {
            System.out.println("❌ Ongeldige keuze. Kies een nummer tussen 1 en " + treeList.size() + ".");
            return;
        }

        Tree selected = treeList.get(index - 1);

        if (player.balance >= selected.buyPrice) {
            Tree bought = new Tree(selected.name, selected.fruitName, selected.buyPrice, selected.fruitPerHarvest, selected.cooldown);
            player.trees.add(bought);
            player.balance -= selected.buyPrice;
            System.out.println("✅ Je hebt een " + bought.name + " gekocht!");
        } else {
            System.out.println("❌ Niet genoeg geld voor een " + selected.name);
        }
    }


}

