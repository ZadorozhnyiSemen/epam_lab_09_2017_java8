package part1.exercise;

import data.raw.Employee;
import data.raw.Generator;
import data.raw.Person;
import db.SlowCompletableFutureDb;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CompletableFutureBasics {

    private static SlowCompletableFutureDb<Employee> employeeDb;
    private static List<String> keys;

    @BeforeClass
    public static void before() {
        Map<String, Employee> employeeMap = Generator.generateEmployeeList(1000)
                                                     .stream()
                                                     .collect(toMap(e -> getKeyByPerson(e.getPerson()), Function.identity(), (a, b) -> a));
        employeeDb = new SlowCompletableFutureDb<>(employeeMap);
        keys = new ArrayList<>(employeeMap.keySet());
    }

    private static String getKeyByPerson(Person person) {
        return person.getFirstName() + "_" + person.getLastName() + "_" + person.getAge();
    }

    @AfterClass
    public static void after() {
        try {
            employeeDb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createNonEmpty() throws ExecutionException, InterruptedException {
        Person person = new Person("John", "Galt", 33);

        Optional<Person> optPerson = Optional.of(person);

        assertTrue(optPerson.isPresent());
        assertEquals(person, optPerson.get());

        Stream<Person> streamPerson = Stream.of(person);

        List<Person> persons = streamPerson.collect(toList());
        assertThat(persons.size(), is(1));
        assertEquals(person, persons.get(0));

        CompletableFuture<Person> futurePerson = CompletableFuture.completedFuture(person);

        assertTrue(futurePerson.isDone());
        assertEquals(person, futurePerson.get());
    }

    @Test
    public void createEmpty() throws ExecutionException, InterruptedException {
        Optional<Person> optPerson = Optional.empty();

        assertFalse(optPerson.isPresent());

        Stream<Person> streamPerson = Stream.empty();

        List<Person> persons = streamPerson.collect(toList());
        assertThat(persons.size(), is(0));

        CompletableFuture<Person> futurePerson = new CompletableFuture<>();
        futurePerson.completeExceptionally(new NoSuchElementException());

        assertTrue(futurePerson.isCompletedExceptionally());
        assertTrue(futurePerson
                .thenApply(x -> false)
                .exceptionally(t -> t.getCause() instanceof NoSuchElementException).get());
    }

    @Test
    public void forEach() throws ExecutionException, InterruptedException {
        Person person = new Person("John", "Galt", 33);

        Optional<Person> optPerson = Optional.of(person);

        CompletableFuture<Person> result1 = new CompletableFuture<>();

        optPerson.ifPresent(result1::complete);
        assertEquals(person, result1.get());

        Stream<Person> streamPerson = Stream.of(person);

        CompletableFuture<Person> result2 = new CompletableFuture<>();

        streamPerson.forEach(result2::complete);
        assertEquals(person, result2.get());

        CompletableFuture<Person> futurePerson = CompletableFuture.completedFuture(person);

        CompletableFuture<Person> result3 = new CompletableFuture<>();

        futurePerson.thenAccept(result3::complete);
        assertEquals(person, result3.get());
    }

    @Test
    public void map() throws ExecutionException, InterruptedException {
        Person person = new Person("John", "Galt", 33);

        Optional<Person> optPerson = Optional.of(person);
        Optional<String> optFirstName = optPerson.map(Person::getFirstName);
        assertEquals(person.getFirstName(), optFirstName.get());

        Stream<Person> streamPerson = Stream.of(person);
        Stream<String> streamFirstName = streamPerson.map(Person::getFirstName);
        assertEquals(person.getFirstName(), streamFirstName.collect(toList()).get(0));

        CompletableFuture<Person> futurePerson = CompletableFuture.completedFuture(person);
        CompletableFuture<String> futureFirstName = futurePerson.thenApply(Person::getFirstName);
        assertEquals(person.getFirstName(), futureFirstName.get());
    }

    @Test
    public void flatMap() throws ExecutionException, InterruptedException {
        Person person = employeeDb.get(keys.get(0)).thenApply(Employee::getPerson).get();


        Optional<Person> optPerson = Optional.of(person);
        Optional<Integer> optFirstCodePointOfFirstName = optPerson.flatMap(person1 ->
                person.getFirstName().codePoints().boxed().findFirst());
        assertEquals(Integer.valueOf(65), optFirstCodePointOfFirstName.get());

        Stream<Person> streamPerson = Stream.of(person);
        IntStream codePoints = streamPerson.flatMapToInt(person1 -> person.getFirstName().codePoints());
        int[] codePointsArray = codePoints.toArray();
        assertEquals(person.getFirstName(), new String(codePointsArray, 0, codePointsArray.length));

        CompletableFuture<Person> futurePerson = CompletableFuture.completedFuture(person);
        CompletableFuture<Employee> futureEmployee = futurePerson.thenCompose(person1 -> employeeDb.get(getKeyByPerson(person1)));
        assertEquals(person, futureEmployee.thenApply(Employee::getPerson).get());
    }
}
