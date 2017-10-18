package lambda.part2.exercise;

import data.Person;
import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class ArrowNotationExercise {
    @Test
    public void getAge() {
        // Person -> Integer
        final Function<Person, Integer> getAge = Person::getAge;
        assertEquals(Integer.valueOf(33), getAge.apply(new Person("", "", 33)));
    }

    @Test
    public void compareAges() {
        // use BiPredicate
        // compareAges: (Person, Person) -> boolean
        final BiPredicate<Person, Person> compareAges = (o1, o2) -> o1.getAge() == o2.getAge();
        assertEquals(true, compareAges.test(new Person("a", "b", 22), new Person("c", "d", 22)));
    }

    // getFullName: Person -> String
    private String getFullName(Person person) {
        return person.getFirstName() + " " + person.getLastName();
    }

    // ageOfPersonWithTheLongestFullName: (Person -> String) -> ((Person, Person) -> int)
    private BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName(Function<Person, String> func) {
        return (o1, o2) -> func.apply(o1).length() > func.apply(o2).length() ? o1.getAge() : o2.getAge();
    }

    @Test
    public void getAgeOfPersonWithTheLongestFullName() {
        // getFullName: Person -> String
        // решение без использования методов выше: o -> o.getFirstName() + " " + o.getLastName();
        final Function<Person, String> getFullName = this::getFullName;
        // ageOfPersonWithTheLongestFullName: (Person -> String) -> (Person, Person) -> Integer
        // решение без использования методов выше:
        // (o1, o2) -> getFullName.apply(o1).length() > getFullName.apply(o2).length() ? o1.getAge() : o2.getAge();
        final BiFunction<Person, Person, Integer> ageOfPersonWithTheLongestFullName = ageOfPersonWithTheLongestFullName(getFullName);
        assertEquals(
                Integer.valueOf(1),
                ageOfPersonWithTheLongestFullName.apply(
                        new Person("a", "b", 2),
                        new Person("aa", "b", 1)));
    }
}
