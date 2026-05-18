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

    public static Event createRegularFromDb(String name, String description, String location,
                                            String dateTime, String category,
                                            int numAttending, double avgRating, int numRatings) {
        return new Event(
                name, description, location,
                dateTime, category, getIconByCategory(category),
                numAttending, avgRating, numRatings
        );
    }

    public static Event createPromotedFromDb(String name, String description, String location,
                                             String dateTime, String category,
                                             int capacity, int numAttending, double avgRating, int numRatings) {
        return new Event(
                name, description, location,
                dateTime, category, getIconByCategory(category),
                true, capacity, numAttending, avgRating, numRatings
        );
    }

    public static int getIconByCategory(String category) {
        if (category == null) return android.R.drawable.ic_media_play;

        switch (category) {
            case "Party":
                return android.R.drawable.ic_menu_camera;
            case "Stand-Up & Theater":
                return android.R.drawable.ic_menu_info_details;
            case "Exhibition":
                return android.R.drawable.ic_menu_gallery;
            case "Festival":
                return android.R.drawable.ic_menu_slideshow;
            default:
                return android.R.drawable.ic_media_play;
        }
    }
}
