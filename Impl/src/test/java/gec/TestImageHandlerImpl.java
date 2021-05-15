package gec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestImageHandlerImpl {
    @InjectMocks
    private ImageHandlerImpl unitUnderTest;

    @Test
    public void testFindImageUrl_BingWithoutAds(){
        Game game = new Game("Digimon World 1", ConsoleEnum.PLAYSTATION_ONE);
        unitUnderTest.downloadImage(game);
        // TODO extend test scenario: Check for image, then delete it
    }

    @Test
    public void testFindImageUrl_BingWithAds(){
        Game game = new Game("Final Fantasy 7", ConsoleEnum.PLAYSTATION_ONE);
        unitUnderTest.downloadImage(game);
        // TODO extend test scenario: Check for image, then delete it
    }
}
