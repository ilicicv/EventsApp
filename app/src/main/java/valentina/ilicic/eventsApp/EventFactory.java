package valentina.ilicic.eventsApp;

public class EventFactory {

    public static Event createRegularEvent(String name, String description, String location,
                                           String dateTime, String category, int imageResId) {
        return new Event(
                name, description, location,
                dateTime, category, imageResId,
                0, 0.0, 0
        );
    }

    public static Event createPromotedEvent(String name, String description, String location,
                                            String dateTime, String category, int imageResId, int capacity) {
        return new Event(
                name, description, location,
                dateTime, category, imageResId,
                true, capacity, 0, 0.0, 0
        );
    }
}
