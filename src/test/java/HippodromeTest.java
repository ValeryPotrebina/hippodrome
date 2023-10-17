import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HippodromeTest {
    @Test
    public void NullConstructionException(){
        assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null));
    }

    @Test
    public void NullConstructionMessage(){
        assertEquals(assertThrows(IllegalArgumentException.class, () -> new Hippodrome(null)).getMessage(), "Horses cannot be null.");
    }
    @Test
    public void NullConstructionListException(){
        assertEquals(assertThrows(IllegalArgumentException.class, () -> new Hippodrome(List.of())).getMessage(), "Horses cannot be empty.");
    }
    @Test
    public void getHorsesTest(){
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            horses.add(new Horse("" + i, i, i));
        }
        Hippodrome hippodrome = new Hippodrome(horses);
        assertEquals(horses, hippodrome.getHorses());
    }

    @Test
    public void MoveTest(){
        int n = 50;
        List<Horse> horses = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            horses.add(mock(Horse.class));
        }
        new Hippodrome(horses).move();


         for (Horse horse : horses){
             verify(horse, atLeastOnce()).move();
         }
    }

    @Test
    public void getWinnerTest() {
        Horse horse1 = new Horse("1", 1, 1);
        Horse horse2 = new Horse("2", 1, 2);
        Horse horse3 = new Horse("3", 1, 3);
        Hippodrome hippodrome = new Hippodrome(List.of(horse1, horse2, horse3));
        assertSame(horse3, hippodrome.getWinner());
    }




}
