import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HorseTest {
    @Test
    public void nullNameException(){
        assertThrows(IllegalArgumentException.class, () -> new Horse(null, 1, 1));
    }

    @Test
    public void nullNameMessage(){
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> new Horse(null, 1, 1));
        assertEquals("Name cannot be null.", exception.getMessage());
    }
    //Проверить, что при передаче в конструктор первым параметром пустой
    //строки или строки содержащей только пробельные символы (пробел, табуляция
    //и т.д.), будет выброшено IllegalArgumentException. Чтобы выполнить
    //проверку с разными вариантами пробельных символов, нужно сделать тест
    //параметризованным;

    @ParameterizedTest(name = "Iteration #{index} -> string = {0}")
    @ValueSource(strings = {"", " ", "\t"})
    public void blankNameException(String name){
        assertThrows(IllegalArgumentException.class, () -> new Horse(name, 1, 1));
    }


    @ParameterizedTest
    @MethodSource("argsProviderFactory")
    void blankMessageException(String argument) {
        assertEquals(assertThrows(IllegalArgumentException.class, () -> new Horse(argument, 1, 1)).getMessage(), "Name cannot be blank.");
    }

    static Stream<String> argsProviderFactory() {
        return Stream.of("\s", " ",  "  ", "\t", "\t\t");
    }

    @ParameterizedTest()
    @ValueSource(ints = {-1, -2, -1000})
    public void secondParameterNegativeTest(int param){
        assertThrows(IllegalArgumentException.class, () -> new Horse("x", param, 1));
    }
    @ParameterizedTest(name = "Iteration #{index} -> param = {0}")
    @ValueSource(ints = {-1, -2, -1000})
    public void thirdParameterNegativeExceptionTest(int param){
        assertEquals(assertThrows(IllegalArgumentException.class, () -> new Horse("x", 1, param)).getMessage(),"Distance cannot be negative." );
    }

    @Test
    public void getNameTest() throws NoSuchFieldException {
        Field nameField = Horse.class.getDeclaredField("name");
        assertEquals(nameField.getType().getSimpleName(), "String");
    }
    @Test
    public void getSpeedTest() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        Horse horse = new Horse("fe", 2, 2);
        Field speed = Horse.class.getDeclaredField("speed");
        speed.setAccessible(true);
        double speedValue = (double) speed.get(horse);
        assertEquals(speedValue, horse.getSpeed());
    }
    @Test
    public void getDistanceTest()  {
        Horse horse = new Horse("g", 1);
        assertEquals(0, horse.getDistance());
    }

    @Test
    public void moveUsesGetRandom(){
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            new Horse("f", 31, 283).move();
            mockedStatic.verify(()->Horse.getRandomDouble(anyDouble(), anyDouble()));
        }
    }
    @ParameterizedTest
    @CsvSource({
            "0.3, 1.9",
            "0.4, 2.2",
            "0.5, 2.5"
    })
    public void moveUsesGetRandomMock(double arg, double res) throws NoSuchFieldException, IllegalAccessException {
        try (MockedStatic<Horse> mockedStatic = mockStatic(Horse.class)){
            mockedStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(arg);
            Horse horse = new Horse("name", 3, 1);
            horse.move();
            Field field = Horse.class.getDeclaredField("distance");
            field.setAccessible(true);
            System.out.println(res + " " + field.get(horse));
            assertEquals(res, field.get(horse));
        }
    }
}
