package village.race;

import village.Citizen;
import village.Race;

public class Peasant extends Citizen {

    public Peasant(String firstName, String lastName) {
        race = Race.Peasant;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}