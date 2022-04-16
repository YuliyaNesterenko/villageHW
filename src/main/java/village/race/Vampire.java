package village.race;

import village.Citizen;
import village.Race;

public class Vampire extends Citizen {

    public  Vampire(String firstName, String lastName) {
        race = Race.Vampire;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

