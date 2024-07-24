package seamcarving;

public class BasicDynamicProgrammingSeamFinderTests extends BasicSeamFinderTests {

    protected SeamFinder createSeamFinder() {
        return new DynamicProgrammingSeamFinder();
    }
}
