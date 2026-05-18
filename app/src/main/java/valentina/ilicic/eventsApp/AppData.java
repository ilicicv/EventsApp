//package valentina.ilicic.eventsApp;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Comparator;
//
//public class AppData {
//
//    public static ArrayList<Event> allEvents = new ArrayList<>();
//    public static ArrayList<Event> interestedEvents = new ArrayList<>();
//    public static ArrayList<Event> attendingEvents = new ArrayList<>();
//
//    public static ArrayList<Event> getSorted(ArrayList<Event> list) {
//        ArrayList<Event> upcoming = new ArrayList<>();
//        ArrayList<Event> past = new ArrayList<>();
//
//        for (Event e : list) {
//            if (e.isPast()) {
//                past.add(e);
//            } else {
//                upcoming.add(e);
//            }
//        }
//
//        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
//
//        upcoming.sort(new Comparator<Event>() {
//            @Override
//            public int compare(Event e1, Event e2) {
//                int promotedCompare = Boolean.compare(e2.isPromoted(), e1.isPromoted());
//                if (promotedCompare != 0) return promotedCompare;
//
//                try {
//                    return sdf.parse(e1.getDateTime()).compareTo(sdf.parse(e2.getDateTime()));
//                } catch (java.text.ParseException e) {
//                    return 0;
//                }
//            }
//        });
//
//        past.sort(new Comparator<Event>() {
//            @Override
//            public int compare(Event e1, Event e2) {
//                try {
//                    return sdf.parse(e2.getDateTime()).compareTo(sdf.parse(e1.getDateTime()));
//                } catch (Exception e) {
//                    return 0;
//                }
//            }
//        });
//
//        ArrayList<Event> finalResult = new ArrayList<>(upcoming);
//        finalResult.addAll(past);
//        return finalResult;
//    }
//
//    public static ArrayList<Event> getSortedEvents() {
//        return getSorted(allEvents);
//    }
//
//    public static ArrayList<Event> getSortedEventsByCategory(String category) {
//        ArrayList<Event> filteredList = new ArrayList<>();
//
//        for (Event event : allEvents) {
//            if (event.getCategory().equalsIgnoreCase(category)) {
//                filteredList.add(event);
//            }
//        }
//
//        return getSorted(filteredList);
//    }
//
//    public static Event findByName(String name) {
//        if (name == null) {
//            return null;
//        }
//
//        for (Event event : allEvents) {
//            if (event.getName().equalsIgnoreCase(name)) {
//                return event;
//            }
//        }
//        return null;
//    }
//}
