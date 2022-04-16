package village.race;

import village.Citizen;
import village.Race;

public class Werewolf extends Citizen {

    public  Werewolf(String firstName, String lastName) {
        race = Race.Werewolf;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
