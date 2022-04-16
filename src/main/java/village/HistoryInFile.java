package village;

import village.race.Peasant;
import village.race.Vampire;
import village.race.Werewolf;
import village.race.Witch;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;


public class HistoryInFile {

    private PrintWriter historyWriter, citizenWriter;
    public static Village oldVillage = new Village();

    public void initHistoryWriter(boolean reWrite) throws IOException {
        File hisFile = new File("src/main/resources/villageHistory.txt");
        if (hisFile.isFile()) {
            if (reWrite) {
                Files.newBufferedWriter(hisFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                Files.newBufferedWriter(hisFile.toPath());
            }
        }
        FileWriter hisFileWriter = new FileWriter(hisFile, true);
        historyWriter = new PrintWriter(hisFileWriter);
    }

    public void saveHistoryToFile(String message) {
        historyWriter.println(message);
        historyWriter.flush();
    }

    public void saveCitizensToFile(ArrayList<Citizen> citizens) throws IOException {
        File citFile = new File("src/main/resources/citizens.txt");
        if (citFile.isFile()) {
            Files.newBufferedWriter(citFile.toPath(), StandardOpenOption.TRUNCATE_EXISTING);
        }
        FileWriter citFileWriter = new FileWriter(citFile);
        citizenWriter = new PrintWriter(citFileWriter);
        for (Citizen cit : citizens) {
            citizenWriter.printf("FirstName: %s, LastName: %s, Race: %s\n", cit.getFirstName(), cit.getLastName(), cit.getRace());
        }
        citizenWriter.flush();
    }

    public static Village getVillage() {
        return oldVillage;
    }

    public static void historyFromFile(boolean print) throws IOException {
        File fileName = new File("src/main/resources/villageHistory.txt");
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        String[] parts = new String[4];
        while ((line = br.readLine()) != null) {
            if (print) {
                System.out.println(line);
            }
            parts = line.split(", ", 4);
            if (parts[1].replaceFirst("FoolMoon: ", "").equals("true")) {
                oldVillage.setFirstMoonLightDay(Village.stringToDate(parts[0].replaceFirst("Day: ", "")));
            }
        }
        oldVillage.setStartDate(Village.stringToDate(parts[0].replaceFirst("Day: ", "")));
        oldVillage.getStartDate().add(Calendar.DATE, 1);

    }

    public static void citizensFromFile(boolean print) throws IOException {
        File fileName = new File("src/main/resources/citizens.txt");
        BufferedReader br = new BufferedReader(new FileReader(fileName));

        int countPeasant = 0;
        int countVampire = 0;
        int countWerewolf = 0;
        int countWitch = 0;
        String line;

        ArrayList<Citizen> hisCitizens = new ArrayList<>();

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(", ", 3);
            Race race = Race.valueOf(parts[2].replaceFirst("Race: ", ""));
            switch (race) {
                case Peasant: {
                    hisCitizens.add(new Peasant(parts[0].replaceFirst("FirstName: ", ""),
                            parts[1].replaceFirst("LastName: ", "")));
                    countPeasant++;
                    break;
                }
                case Vampire: {
                    hisCitizens.add(new Vampire(parts[0].replaceFirst("FirstName: ", ""),
                            parts[1].replaceFirst("LastName: ", "")));
                    countVampire++;
                    break;
                }
                case Werewolf: {
                    hisCitizens.add(new Werewolf(parts[0].replaceFirst("FirstName: ", ""),
                            parts[1].replaceFirst("LastName: ", "")));
                    countWerewolf++;
                    break;
                }
                case Witch: {
                    hisCitizens.add(new Witch(parts[0].replaceFirst("FirstName: ", ""),
                            parts[1].replaceFirst("LastName: ", "")));
                    countWitch++;
                    break;
                }
            }
        }
        oldVillage.setCountPeasant(countPeasant);
        oldVillage.setCountVampire(countVampire);
        oldVillage.setCountWerewolf(countWerewolf);
        oldVillage.setCountWitch(countWitch);
        oldVillage.setCitizens(hisCitizens);
        if (print) {
            Village.printCitizens(hisCitizens);
        }
    }
}
