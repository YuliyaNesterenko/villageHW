package village.race;

import village.Citizen;
import village.Race;

public class Witch extends Citizen {

    public  Witch(String firstName, String lastName) {
        race = Race.Witch;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
