import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event {
    private String name;
    private String description;
    private String location;
    private String dateTime;
    private String category;
    private int imageResId;
    private boolean isPromoted;
    private int capacity;
    private int attendingCount;
    private double averageRating;
    private int ratingCount;

    public Event(String name, String description, String location, String dateTime,
                 String category, int imageResId, boolean isPromoted,
                 int capacity, int attendingCount, double averageRating, int ratingCount) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.category = category;
        this.imageResId = imageResId;
        this.isPromoted = isPromoted;
        this.capacity = capacity;
        this.attendingCount = attendingCount;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public Event(String name, String description, String location, String dateTime,
                 String category, int imageResId, int attendingCount,
                 double averageRating, int ratingCount) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.category = category;
        this.imageResId = imageResId;
        this.isPromoted = false;
        this.capacity = 0;
        this.attendingCount = attendingCount;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public boolean isPast() {

        if (this.dateTime == null || this.dateTime.isEmpty())
            return false;

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime eventTime = LocalDateTime.parse(this.dateTime, formatter);
            return eventTime.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            return false;
        }
    }

    public void addRating(int rating) {
        double sum = (this.averageRating * this.ratingCount) + rating;
        this.averageRating = sum / this.ratingCount;
        this.ratingCount++;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public int getImageResId() {
        return imageResId;
    }
    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public boolean isPromoted() {
        return isPromoted;
    }
    public void setPromoted(boolean promoted) {
        isPromoted = promoted;
    }

    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getAttendingCount() {
        return attendingCount;
    }
    public void setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

}
