package valentina.ilicic.eventsApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class AppData {

    public static ArrayList<Event> allEvents = new ArrayList<>();
    public static ArrayList<Event> interestedEvents = new ArrayList<>();
    public static ArrayList<Event> attendingEvents = new ArrayList<>();

    // privremeno cuvanje dok se ne doda baza
    public static ArrayList<String> ratedEvents = new ArrayList<>();

    static {
        // PROŠLI DOGAĐAJI
        Event e1 = EventFactory.createRegularEvent(
                "Jakov Jozinović",
                "Koncert mlade muzičke senzacije.",
                "SPENS, Novi Sad",
                "10.11.2025 20:30",
                "Concert",
                android.R.drawable.ic_media_play
        );

        Event e2 = EventFactory.createRegularEvent(
                "NEON Party",
                "Sjajan neon doživljaj uz najbolje DJ izvođače.",
                "Štark Arena, Beograd",
                "10.05.2026 22:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e3 = EventFactory.createRegularEvent(
                "Predstava: Kir Janja",
                "Vrhunska izvedba večnog klasika Jovana Sterije Popovića na daskama teatra.",
                "Knjaževsko-srpski teatar, Kragujevac",
                "05.10.2025 20:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details
        );

        Event e4 = EventFactory.createRegularEvent(
                "Jesenja izložba",
                "Godišnja izložba radova eminentnih likovnih umetnika Srbije.",
                "Galerija ULUS, Beograd",
                "20.09.2025 18:00",
                "Exhibition",
                android.R.drawable.ic_menu_gallery
        );

// PROMOTED
        Event e5 = EventFactory.createPromotedEvent(
                "EXIT Festival",
                "Najveći muzički festival u regiji.",
                "Petrovaradin, Novi Sad",
                "10.07.2025 18:00",
                "Festival",
                android.R.drawable.ic_menu_slideshow,
                50000
        );

        Event e6 = EventFactory.createPromotedEvent(
                "BITEF pod zvezdama",
                "Svečano otvaranje prestižnog beogradskog pozorišnog festivala.",
                "Stari Bitef Teatar, Beograd",
                "20.06.2026 21:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details,
                350
        );

        Event e7 = EventFactory.createPromotedEvent(
                "Belgrade Beer Fest",
                "Ogromni letnji festival piva, druženja i muzike na beogradskom Ušću.",
                "Ušće, Beograd",
                "12.08.2026 17:00",
                "Festival",
                android.R.drawable.ic_menu_slideshow,
                15000
        );

// REGULARNI
        Event e8 = EventFactory.createRegularEvent(
                "Yu Grupa na Letnjoj",
                "Koncert legendarnog benda pod otvorenim nebom u Tvrđavi.",
                "Letnja pozornica, Niš",
                "18.05.2026 21:00",
                "Concert",
                android.R.drawable.ic_media_play
        );

        Event e9 = EventFactory.createRegularEvent(
                "Party Barutana",
                "Letnja žurka svetski poznatih DJ-a na Kalemegdanu.",
                "Barutana, Beograd",
                "22.05.2026 23:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e10 = EventFactory.createRegularEvent(
                "Beach Party Palić",
                "Provod na obali jezera uz najbolje letnje hitove.",
                "Ženski štrand, Palić",
                "15.07.2026 16:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e11 = EventFactory.createRegularEvent(
                "Predstava: Gospođa ministarka",
                "Čuveno Nušićevo delo u izvedbi ansambla kragujevačkog pozorišta.",
                "Knjaževsko-srpski teatar, Kragujevac",
                "02.06.2026 20:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details
        );

        Event e12 = EventFactory.createRegularEvent(
                "Lovefest 2026",
                "Tradicionalni i mega popularni festival elektronske muzike u srcu banje.",
                "Vrnjačka Banja",
                "06.08.2026 18:00",
                "Festival",
                android.R.drawable.ic_menu_slideshow
        );

        Event e13 = EventFactory.createRegularEvent(
                "Noć muzeja u Subotici",
                "Godišnja manifestacija sa besplatnim ulazom i specijalnim umetničkim vođenjem.",
                "Savremena Galerija, Subotica",
                "28.05.2026 19:00",
                "Exhibition",
                android.R.drawable.ic_menu_gallery
        );

        Event e14 = EventFactory.createRegularEvent(
                "Jazz na Paliću",
                "Letnje muzičko veče džeza i promocija lokalnih vinarija sa Palića.",
                "Velika terasa, Palić",
                "10.06.2026 20:00",
                "Concert",
                android.R.drawable.ic_media_play
        );

        Event e15 = EventFactory.createRegularEvent(
                "Latino Night u KC Grad",
                "Tradicionalno plesno latino veče pod otvorenim nebom Beograda.",
                "Kulturni Centar Grad, Beograd",
                "25.06.2026 21:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e16 = EventFactory.createRegularEvent(
                "Stand-up: Srđan Dinčić",
                "Gostovanje i celovečernji šou jednog od najboljih srpskih komičara.",
                "Mts Dvorana, Beograd",
                "11.06.2026 20:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details
        );

        Event e17 = EventFactory.createRegularEvent(
                "Summer Foam Party",
                "Veliki popodnevni i večernji pena party na otvorenim bazenima.",
                "Gradski Bazeni, Kragujevac",
                "28.07.2026 19:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e18 = EventFactory.createRegularEvent(
                "Retrospektiva slika MSUB",
                "Istorijska postavka i izložba slika jugoslovenskog modernizma.",
                "Muzej Savremene Umetnosti, Beograd",
                "15.05.2026 12:00",
                "Exhibition",
                android.R.drawable.ic_menu_gallery
        );

        allEvents.addAll(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18));
        interestedEvents.addAll(Arrays.asList(e2, e5, e10, e13, e14));
        attendingEvents.addAll(Arrays.asList(e1, e3, e5, e6, e9, e12, e4, e16));
    }

    public static ArrayList<Event> getSorted(ArrayList<Event> list) {
        ArrayList<Event> sortedList = new ArrayList<>(list);
        sortedList.sort(new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                int promotedCompare = Boolean.compare(e2.isPromoted(), e1.isPromoted());
                if (promotedCompare != 0) return promotedCompare;

                return e1.getDateTime().compareTo(e2.getDateTime());
            }
        });
        return sortedList;
    }

    public static ArrayList<Event> getSortedEvents() {
        return getSorted(allEvents);
    }

    public static ArrayList<Event> getSortedEventsByCategory(String category) {
        ArrayList<Event> filteredList = new ArrayList<>();

        for (Event event : allEvents) {
            if (event.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(event);
            }
        }

        filteredList.sort(new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return Boolean.compare(e2.isPromoted(), e1.isPromoted());
            }
        });

        return filteredList;
    }

    public static Event findByName(String name) {
        if (name == null) {
            return null;
        }

        for (Event event : allEvents) {
            if (event.getName().equalsIgnoreCase(name)) {
                return event;
            }
        }
        return null;
    }
}
