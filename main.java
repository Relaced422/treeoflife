package treeoflife;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.*;

public class main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Player player = new Player();
        Shop shop = new Shop();

        // Tree type die je kan kopen
        Map<String, Integer> fruitPrices = new HashMap<>();
        fruitPrices.put("Appels", config.APPEL_PRICE);
        fruitPrices.put("Bananen", config.BANAAN_PRICE);
        fruitPrices.put("Mango's", config.MANGO_PRICE);
        fruitPrices.put("Avocado's", config.AVOCADO_PRICE);
        fruitPrices.put("Citroenen", config.CITROEN_PRICE);
        fruitPrices.put("Peren", config.PEER_PRICE);
        fruitPrices.put("Kersen", config.KERS_PRICE);
        fruitPrices.put("Druiven", config.DRUIF_PRICE);
        fruitPrices.put("Ananassen", config.ANANAS_PRICE);
        fruitPrices.put("Spaceweed", config.SPACEWEED_PRICE);

        // voeg hier je andere fruitsoorten toe


        boolean playing = true;
        while (playing) {
            clearConsole();  // Schoon scherm voor verse ronde
            System.out.println("Saldo: €" + player.balance);
            System.out.println("\n--- Menu ---");
            System.out.println("1. Boom kopen");
            System.out.println("2. Nieuwe ronde (oogsten)");
            System.out.println("3. Fruit verkopen");
            System.out.println("4. Toon status");
            System.out.println("5. Stoppen");
            System.out.print("Kies optie: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    clearConsole();  // ook hier als je wilt dat de shop clean toont
                    shop.showAvailableTrees();
                    System.out.print("Kies het nummer van de boom die je wil kopen: ");
                    while (!scanner.hasNextInt()) {
                        System.out.print("⚠️ Ongeldige invoer. Typ een getal: ");
                        scanner.next(); // skip foutieve input
                    }
                    int keuze = scanner.nextInt();
                    scanner.nextLine(); // buffer fix
                    shop.buyTreeByIndex(player, keuze);

                }

                case 2 -> {
                    clearConsole();

                    // ➕ random event toepassen
                    String event = applyRandomEvent(player, fruitPrices);
                    System.out.println("📢 EVENT: " + event + "\n");

                    player.collectFruits();
                    
                 // Reset tijdelijke boosts (fruitName + hoeveelheid)
                    for (Tree tree : player.trees) {
                        if (tree.fruitName.contains("3x Boost")) {
                            tree.fruitName = tree.fruitName.replace(" (3x Boost)", "");
                            tree.fruitPerHarvest /= 3;
                        }
                    }


                    // Reset tijdelijke boost (fruitName herstellen)
                    for (Tree tree : player.trees) {
                        if (tree.fruitName.contains("Boost")) {
                            tree.fruitName = tree.fruitName.replace(" (Boost)", "");
                            tree.fruitPerHarvest /= 2;
                            tree.cooldown++; // herstel cooldown
                        }
                    }
                }


                case 3 -> {
                    clearConsole();
                    player.sellFruitsManually(fruitPrices, scanner);
                }

                case 4 -> {
                    clearConsole();
                    player.showStats();
                }

                case 5 -> {
                    clearConsole();
                    playing = false;
                    System.out.println("Tot de volgende keer!");
                }

                default -> {
                    clearConsole();
                    System.out.println("Ongeldige keuze.");
                }
            }

            // Pauze tussen rondes zodat speler kan lezen
            System.out.println("\nDruk op Enter om door te gaan...");
            scanner.nextLine();
            scanner.nextLine();
        }


        scanner.close();
        System.out.println("Game over.");
    }

	public static void clearConsole() {
	    for (int i = 0; i < 50; ++i) System.out.println();
	}
	
	public static String applyRandomEvent(Player player, Map<String, Integer> fruitPrices) {
	    Random random = new Random();
	    int roll = random.nextInt(100); // 0 - 99
	    String eventMessage = "🌱 Normale ronde. Geen bijzonderheden.";

	    if (roll < 1) {
	        eventMessage = "🌀 Tornado! Een van je bomen is verwoest!";
	        if (!player.trees.isEmpty()) {
	            Tree lostTree = player.trees.remove(random.nextInt(player.trees.size()));
	            eventMessage += " Je verloor je " + lostTree.name + "!";
	        }
	    } else if (roll < 3) {
	        eventMessage = "🌟 Legendarische regen! Alle bomen geven deze ronde 3x zoveel fruit!";
	        for (Tree tree : player.trees) {
	            if (tree.canHarvest()) {
	                tree.fruitPerHarvest *= 3;
	                tree.fruitName += " (3x Boost)";
	            }
	        }
	    } else if (roll < 6) {
	        eventMessage = "🔥 Brand! Je opslag halveert...";
	        for (String fruit : player.fruits.keySet()) {
	            int current = player.fruits.get(fruit);
	            player.fruits.put(fruit, current / 2);
	        }
	    } else if (roll < 10) {
	        eventMessage = "🎁 Gratis upgrade! Je opslag is met 10 verhoogd!";
	        player.storageLimit += 10;
	    } else if (roll < 15) {
	        eventMessage = "💸 Marktinstorting! Fruitprijzen gehalveerd 😬";
	        for (String fruit : fruitPrices.keySet()) {
	            fruitPrices.put(fruit, fruitPrices.get(fruit) / 2);
	        }
	    } else if (roll < 20) {
	        eventMessage = "📈 Fruit hype! Fruitprijzen verdubbeld 💰";
	        for (String fruit : fruitPrices.keySet()) {
	            fruitPrices.put(fruit, fruitPrices.get(fruit) * 2);
	        }
	    } else if (roll < 25) {
	        eventMessage = "🌩️ Storm! Geen bomen kunnen oogsten.";
	        for (Tree tree : player.trees) {
                tree.cooldownLeft = Math.max(tree.cooldownLeft, 1);
            }
	    } else if (roll < 30) {
	        eventMessage = "🪱 Rotte oogst! 1 willekeurige fruitsoort rot weg.";
	        if (!player.fruits.isEmpty()) {
	            List<String> keys = new ArrayList<>(player.fruits.keySet());
	            String rotten = keys.get(random.nextInt(keys.size()));
	            player.fruits.remove(rotten);
	            eventMessage += " Je verloor al je " + rotten + ".";
	        }
	    }

	    return eventMessage;
	}


}

