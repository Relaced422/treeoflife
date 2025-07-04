package treeoflife;

import java.util.*;

public class Player {
    public int balance = config.START_BALANCE;
    public int storageLimit = config.START_STORAGE;
    public int storageUpgradePrice = config.STORAGE_UPGRADE_PRICE;
    public Map<String, Integer> fruits = new HashMap<>();
    public List<Tree> trees = new ArrayList<>();

    public void buyTree(Tree tree) {
        if (balance >= tree.buyPrice) {
            trees.add(tree);
            balance -= tree.buyPrice;
            System.out.println("Je hebt een " + tree.name + " gekocht!");
        } else {
            System.out.println("Niet genoeg geld.");
        }
    }

    public void collectFruits() {
        for (Tree tree : trees) {
            tree.nextRound();
            int harvested = tree.harvest();

            if (harvested > 0) {
                int freeSpace = storageLimit - getTotalFruitStored();
                if (freeSpace <= 0) {
                    System.out.println("Opslag vol! Geen " + tree.fruitName + " geoogst.");
                    continue;
                }
                int amount = Math.min(harvested, freeSpace);
                fruits.put(tree.fruitName, fruits.getOrDefault(tree.fruitName, 0) + amount);
                System.out.println("Je oogstte " + amount + " " + tree.fruitName + " van je " + tree.name + ".");
            } else {
                System.out.println(tree.name + " is nog in cooldown (" + tree.cooldownLeft + " rondes).");
            }
        }
    }

    public void sellFruits(String fruit, int pricePerFruit) {
        int amount = fruits.getOrDefault(fruit, 0);
        if (amount > 0) {
            int total = amount * pricePerFruit;
            balance += total;
            fruits.put(fruit, 0);
            System.out.println("Je verkocht " + amount + " " + fruit + " voor €" + total);
        } else {
            System.out.println("Geen " + fruit + " om te verkopen.");
        }
    }

    public void showStats() {
    	System.out.println("--------------------------------------");
        System.out.println("💰 Saldo: €" + balance);
        System.out.println("🌳 Aantal bomen: " + trees.size());

        System.out.println("🍇 Fruitvoorraad:");
        for (String fruit : fruits.keySet()) {
            System.out.println("- " + fruit + ": " + fruits.get(fruit));
        }

        System.out.println("📦 Opslag: " + getTotalFruitStored() + " / " + storageLimit);
        System.out.println("           " + getStorageBar());
    }

    
    public void sellAllFruits(Map<String, Integer> fruitPrices) {
        int totalEarned = 0;

        for (String fruit : fruits.keySet()) {
            int amount = fruits.get(fruit);
            int price = fruitPrices.getOrDefault(fruit, 0);
            int value = amount * price;
            totalEarned += value;
            System.out.println("Verkocht " + amount + " " + fruit + " voor €" + value);
        }

        balance += totalEarned;
        fruits.clear(); // alles verkocht = voorraad leeg
        System.out.println("Totale opbrengst: €" + totalEarned);
    }
    
    public void sellFruitsManually(Map<String, Integer> fruitPrices, Scanner scanner) {
        int totalEarned = 0;

        if (fruits.isEmpty()) {
            System.out.println("❌ Je hebt geen fruit om te verkopen.");
            return;
        }

        for (String fruit : fruits.keySet()) {
            int owned = fruits.get(fruit);
            int price = fruitPrices.getOrDefault(fruit, 0);

            System.out.println("Je hebt " + owned + " " + fruit + " (verkoopprijs: €" + price + " per stuk)");
            System.out.print("Hoeveel wil je verkopen? (0 = overslaan): ");

            int amount;
            while (true) {
                try {
                    amount = Integer.parseInt(scanner.nextLine());
                    if (amount < 0 || amount > owned) {
                        System.out.println("⚠️ Ongeldige hoeveelheid. Kies iets tussen 0 en " + owned);
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Vul een geldig getal in.");
                }
            }

            if (amount > 0) {
                int value = amount * price;
                totalEarned += value;
                fruits.put(fruit, owned - amount);
                System.out.println("✅ Verkocht " + amount + " " + fruit + " voor €" + value);
            } else {
                System.out.println("⏩ " + fruit + " niet verkocht.");
            }
        }

        balance += totalEarned;
        System.out.println("💰 Totale opbrengst: €" + totalEarned);
    }
    
	    public int getTotalFruitStored() {
	        int total = 0;
	        for (int amount : fruits.values()) {
	            total += amount;
	        }
	        return total;
	    }
	    
    public String getStorageBar() {
        int total = getTotalFruitStored();
        int max = storageLimit;
        int barLength = 20;
	
	        double percentage = (double) total / max;
	        int filled = (int) (percentage * barLength);
	        int empty = barLength - filled;
	
	        StringBuilder bar = new StringBuilder("[");
	        for (int i = 0; i < filled; i++) bar.append("▓");
	        for (int i = 0; i < empty; i++) bar.append("░");
	        bar.append("] ");
	
	        int percentDisplay = (int) (percentage * 100);
	        if (percentDisplay > 100) percentDisplay = 100;
	
        bar.append(percentDisplay).append("%");
        return bar.toString();
    }

    public void upgradeStorage() {
        if (balance < storageUpgradePrice) {
            System.out.println("Niet genoeg geld om je opslag te upgraden.");
            return;
        }
        balance -= storageUpgradePrice;
        storageLimit += config.STORAGE_UPGRADE_AMOUNT;
        System.out.println("Opslag vergroot naar " + storageLimit + ".");
    }



}

