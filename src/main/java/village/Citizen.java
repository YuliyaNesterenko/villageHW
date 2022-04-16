package village;

public abstract class Citizen {
    protected String firstName;
    protected String lastName;
    protected Race race;

    @Override
    public String toString() {
        return "" +
                "" + firstName +
                " " + lastName +
                " (" + race +
                ')';
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }
}
