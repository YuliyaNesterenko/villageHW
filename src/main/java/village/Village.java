package village;

import com.github.javafaker.Faker;
import village.race.Peasant;
import village.race.Vampire;
import village.race.Werewolf;
import village.race.Witch;

import java.io.IOException;
import java.util.*;

public class Village {

    private static final String months[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private int CITIZENS_QUANTITY;
    private GregorianCalendar startDate;
    private GregorianCalendar finalDate;
    private int countPeasant;
    private int countVampire;
    private int countWerewolf;
    private int countWitch;
    private boolean consoleDisplay;
    private boolean writingInFile;
    private GregorianCalendar firstMoonLightDay;

    private List<Citizen> citizens = new ArrayList<>();
    private static final List<Calendar> moonLightDays = new ArrayList<>();

    private final static Random random = new Random();

    public Village() {
        this.CITIZENS_QUANTITY = 50;
        this.startDate = new GregorianCalendar(1650, 0, 1);
        this.finalDate = new GregorianCalendar(1750, 0, 1);
        this.countPeasant = 0;
        this.countVampire = 0;
        this.countWerewolf = 0;
        this.countWitch = 0;
        this.consoleDisplay = true;
        this.writingInFile = true;
        this.firstMoonLightDay = new GregorianCalendar(1650, 0, 1);
        getMoonLightDays();
    }

    public void getMoonLightDays() {
        /*
            формирование календаря полнолуний
        */
        GregorianCalendar day = new GregorianCalendar();
        day.setTime(firstMoonLightDay.getTime());

        while (startDate.before(day)) {
            day.add(Calendar.DAY_OF_MONTH, -28);
        }
        while (day.before(finalDate)) {
            day.add(Calendar.DAY_OF_MONTH, 28);
            GregorianCalendar newCal = new GregorianCalendar();
            newCal.setTime(day.getTime());
            moonLightDays.add(newCal);
        }
    }

    public void generateCitizens() {
        /*
            Первичное формирование жителей деревни
        */
        for (int i = 0; i < this.getCITIZENS_QUANTITY(); i++) {
            int citizenRace = random.nextInt(4);
            citizens.add(getRace(citizenRace));
        }
    }

    private Citizen getRace(int race) {
        /*
            формирование отдельного жителя
        */
        Citizen citizen = null;
        Faker faker = new Faker();
        switch (race) {
            case 0:
                citizen = new Peasant(faker.name().firstName(), faker.name().lastName());
                this.countPeasant++;
                break;
            case 1:
                citizen = new Vampire(faker.name().firstName(), faker.name().lastName());
                this.countVampire++;
                break;
            case 2:
                citizen = new Werewolf(faker.name().firstName(), faker.name().lastName());
                this.countWerewolf++;
                break;
            case 3:
                citizen = new Witch(faker.name().firstName(), faker.name().lastName());
                this.countWitch++;
                break;
        }
        return citizen;
    }

    public void lifeCycle(HistoryInFile hif) {
        /*
            Ежедневная жизнь деревни (одно из 4-х возможнх событий)
        */
        Calendar startLife = new GregorianCalendar(1650, 0, 1);
        startLife.setTime(startDate.getTime());
        while (startLife.before(finalDate)) {

            int action = random.nextInt(4);
            String event;

            switch (action) {
                case 1:
                    if (citizens.size() < CITIZENS_QUANTITY) {
                        int citizenRase = random.nextInt(4);
                        Citizen citizen = getRace(citizenRase);
                        citizens.add(citizen);
                        event = citizen + " come to village";
                        break;
                    }
                case 0:
                    if (!citizens.isEmpty()) {
                        int citizen = random.nextInt(citizens.size());
                        Citizen citizenRemove = citizens.get(citizen);
                        delCitizen(citizenRemove);
                        event = citizenRemove + " go out from village";
                        break;
                    }
                case 3:
                    if (!citizens.isEmpty()) {
                        int citizenRandomOne = random.nextInt(citizens.size());
                        int citizenRandomTwo = random.nextInt(citizens.size());
                        event = goVisiting(citizens.get(citizenRandomOne), citizens.get(citizenRandomTwo), startLife);
                        break;
                    }
                default:
                    event = "Sleeping....";
                    break;
            }

            String line = String.format("Day: %s, FoolMoon: %s, citizens: %s, action: %s",
                    DateToString(startLife), moonLightDays.contains(startLife), citizens.size(), event);
            if (isWritingInFile()) {
                hif.saveHistoryToFile(line);

                try {
                    hif.saveCitizensToFile((ArrayList<Citizen>) getCitizens());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (isConsoleDisplay()) {
                System.out.println(String.format("Day: %s, FoolMoon: %s\n" +
                                "citizens: %s (Pe: %s, Va; %s, Ww: %s, Wi: %s)\n" +
                                "action: %s\n",
                        DateToString(startLife), moonLightDays.contains(startLife), citizens.size(),
                        getCountPeasant(), getCountVampire(), getCountWerewolf(), getCountWitch(), event));
            }

            startLife.add(Calendar.DATE, 1);
        }
    }

    private String goVisiting(Citizen citizenOne, Citizen citizenTwo, Calendar calendar) {
        boolean bWitchDay = ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && calendar.get(Calendar.DAY_OF_MONTH) == 13) ||
                (calendar.get(Calendar.DAY_OF_MONTH) == 31 && calendar.get(Calendar.MONTH) == Calendar.OCTOBER)); // ведьмин день - пятница 13-е или 31.10

        // полнолуние в ведьмин день
        if (bWitchDay && moonLightDays.contains(calendar)) {
            Witch witch = (Witch) citizens.stream()
                    .filter(citizen -> citizen.getRace().equals(Race.Witch))
                    .findFirst().orElse(null);
            Werewolf werewolf = (Werewolf) citizens.stream()
                    .filter(citizen -> citizen.getRace().equals(Race.Werewolf))
                    .findFirst()
                    .orElse(null);
            if ((witch != null) && (werewolf != null)) {
                String temp;
                if (1 == random.nextInt(2)) {
                    delCitizen(citizens.get(citizens.indexOf(werewolf)));
                    temp = "BattleEXCEPTION " + werewolf + " " + "has been killed by " + witch;
                } else {
                    delCitizen(citizens.get(citizens.indexOf(witch)));
                    temp = "BattleEXCEPTION " + witch + " " + "has been killed by " + werewolf;
                }
                return temp;
            }
        }
        // если день ведьм
        if (!citizenOne.getRace().equals(Race.Witch) && bWitchDay) {

            Witch witch = (Witch) citizens.stream()
                    .filter(citizen -> citizen.getRace().equals(Race.Witch))
                    .findFirst().orElse(null);
            if (witch != null) {
                delCitizen(citizenOne);
                return "MurderException " + citizenOne + " " + "has been killed by" + witch;
            }
        }
        // если полнолуние
        if (moonLightDays.contains(calendar)) {
            Werewolf werewolf = (Werewolf) citizens.stream()
                    .filter(citizen -> citizen.getRace().equals(Race.Werewolf))
                    .findFirst()
                    .orElse(null);
            if (werewolf != null) {
                delCitizen(citizenOne);
                return "MurderException " + citizenOne + " has been killed by " + werewolf;
            }
        }

        // в любой другой день если один из гостей вампир
        if ((citizenOne.getRace().equals(Race.Vampire) || citizenTwo.getRace().equals(Race.Vampire))
                && (!citizenOne.getRace().equals(citizenTwo.getRace()))) {

            Citizen vampire = (citizenOne.getRace().equals(Race.Vampire) ? citizenOne : citizenTwo);
            Citizen citizen = (citizenOne.getRace().equals(Race.Vampire) ? citizenTwo : citizenOne);

            if (1 >= random.nextInt(100)) {
                return "DawnException " + vampire + " disappeared from the village";
            }
            if (((random.nextInt(100) < 5)) &&
                    (citizen.getRace().equals(Race.Peasant)) || citizen.getRace().equals(Race.Witch)) {
                String temp = "MurderException " + citizen + " turned into a vampire by " + vampire;
                countVampire++;
                if (citizen.getRace().equals(Race.Peasant)) {
                    countPeasant--;
                } else {
                    countWitch--;
                }
                citizen.setRace(Race.Vampire); // устанавливаем жителю рассу "вампир"
                citizens.set(citizens.indexOf(citizenOne), vampire);
                citizens.set(citizens.indexOf(citizenTwo), citizen);
                return temp;
            }
            if (random.nextInt(100) < 5) {
                citizens.set(citizens.indexOf(citizenOne), vampire);
                citizens.set(citizens.indexOf(citizenTwo), citizen);
                delCitizen(citizenTwo);
                return "MurderException " + citizen + " has been killed by " + vampire;
            }
        }
        // во всех других случаях
        return citizenOne + " drink tea with " + citizenTwo;
    }

    private void delCitizen(Citizen citizen) {
        /*
            Удаление жителя из деревни
        */
        switch (citizen.getRace()) {
            case Peasant: {
                countPeasant--;
                break;
            }
            case Vampire: {
                countVampire--;
                break;
            }
            case Werewolf: {
                countWerewolf--;
                break;
            }
            case Witch: {
                countWitch--;
                break;
            }
        }
        citizens.remove(citizen);
    }

    public List<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(List<Citizen> citizens) {
        //this.citizens.clear();
        this.citizens.addAll(citizens);
    }

    public static void printCitizens(ArrayList<Citizen> cits) {
        for (Citizen cit : cits) {
            System.out.println(String.format("FirstName: %s, LastName: %s, Race: %s", cit.getFirstName(), cit.getLastName(), cit.getRace()));
        }
    }

    public int getCITIZENS_QUANTITY() {
        return CITIZENS_QUANTITY;
    }

    public void setCITIZENS_QUANTITY(int CITIZENS_QUANTITY) {
        this.CITIZENS_QUANTITY = CITIZENS_QUANTITY;
    }

    public GregorianCalendar getStartDate() {
        return startDate;
    }

    public void setStartDate(GregorianCalendar startDate) {
        this.startDate = startDate;
    }

    public GregorianCalendar getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(GregorianCalendar finalDate) {
        this.finalDate = finalDate;
    }

    public int getCountPeasant() {
        return countPeasant;
    }

    public void setCountPeasant(int countPeasant) {
        this.countPeasant = countPeasant;
    }

    public int getCountVampire() {
        return countVampire;
    }

    public void setCountVampire(int countVampire) {
        this.countVampire = countVampire;
    }

    public int getCountWerewolf() {
        return countWerewolf;
    }

    public void setCountWerewolf(int countWerewolf) {
        this.countWerewolf = countWerewolf;
    }

    public int getCountWitch() {
        return countWitch;
    }

    public void setCountWitch(int countWitch) {
        this.countWitch = countWitch;
    }

    public boolean isConsoleDisplay() {
        return consoleDisplay;
    }

    public void setConsoleDisplay(boolean consoleDisplay) {
        this.consoleDisplay = consoleDisplay;
    }

    public boolean isWritingInFile() {
        return writingInFile;
    }

    public void setWritingInFile(boolean writingInFile) {
        this.writingInFile = writingInFile;
    }

    public GregorianCalendar getFirstMoonLightDay() {
        return firstMoonLightDay;
    }

    public void setFirstMoonLightDay(GregorianCalendar firstMoonLightDay) {
        this.firstMoonLightDay = firstMoonLightDay;
        getMoonLightDays();
    }

    public static GregorianCalendar stringToDate(String date) {
        GregorianCalendar gcDate = null;
        String[] sDate = date.split("\\.");
        if (!sDate[1].replaceAll("[0123456789]", "").equals("")) {
            for (int i = 0; i < months.length; i++) {
                if (months[i].equals(sDate[1])) {
                    gcDate = new GregorianCalendar(Integer.valueOf(sDate[2]), i, Integer.valueOf(sDate[0]));
                    break;
                }
            }
        } else {
            gcDate = new GregorianCalendar(Integer.valueOf(sDate[2]), (Integer.valueOf(sDate[1]) - 1), Integer.valueOf(sDate[0]));
        }
        return gcDate;
    }

    public static String DateToString(Calendar gcDate) {
        return gcDate.get(Calendar.DAY_OF_MONTH) + "." + months[gcDate.get(Calendar.MONTH)] + "." + gcDate.get(Calendar.YEAR);
    }
}