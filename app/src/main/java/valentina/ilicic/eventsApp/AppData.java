package valentina.ilicic.eventsApp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class AppData {

    public static ArrayList<Event> allEvents = new ArrayList<>();
    public static ArrayList<Event> interestedEvents = new ArrayList<>();
    public static ArrayList<Event> attendingEvents = new ArrayList<>();

    static {
        // PROŠLI DOGAĐAJI
        Event e1 = EventFactory.createRegularEvent(
                "Jakov Jozinović u Spensu",
                "Koncert mlade muzičke senzacije.",
                "SPENS, Novi Sad",
                "2025-11-10 20:30",
                "Concert",
                android.R.drawable.ic_media_play
        );

        Event e2 = EventFactory.createRegularEvent(
                "NEON Party",
                "Sjajan neon doživljaj uz najbolje DJ izvođače.",
                "Štark Arena, Beograd",
                "2026-05-10 22:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e3 = EventFactory.createRegularEvent(
                "Predstava: Kir Janja",
                "Vrhunska izvedba večnog klasika Jovana Sterije Popovića na daskama teatra.",
                "Knjaževsko-srpski teatar, Kragujevac",
                "2025-10-05 20:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details
        );

        Event e4 = EventFactory.createRegularEvent(
                "Jesenja izložba ULUS",
                "Godišnja izložba radova eminentnih likovnih umetnika Srbije.",
                "Galerija ULUS, Beograd",
                "2025-09-20 18:00",
                "Exhibition",
                android.R.drawable.ic_menu_gallery
        );

        // PROMOTED
        Event e5 = EventFactory.createPromotedEvent(
                "EXIT Festival",
                "Najveći muzički festival u regiji.",
                "Petrovaradin, Novi Sad",
                "2025-07-10 18:00",
                "Festival",
                android.R.drawable.ic_menu_slideshow,
                50000
        );

        Event e6 = EventFactory.createPromotedEvent(
                "BITEF pod zvezdama",
                "Svečano otvaranje prestižnog beogradskog pozorišnog festivala.",
                "Stari Bitef Teatar, Beograd",
                "2026-06-20 21:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details,
                350
        );

        Event e7 = EventFactory.createPromotedEvent(
                "Belgrade Beer Fest",
                "Ogromni letnji festival piva, druženja i muzike na beogradskom Ušću.",
                "Ušće, Beograd",
                "2026-08-12 17:00",
                "Festival",
                android.R.drawable.ic_menu_slideshow,
                15000
        );

        //  REGULARNI
        Event e8 = EventFactory.createRegularEvent(
                "Yu Grupa na Letnjoj",
                "Koncert legendarnog benda pod otvorenim nebom u Tvrđavi.",
                "Letnja pozornica, Niš",
                "2026-05-18 21:00",
                "Concert",
                android.R.drawable.ic_media_play
        );

        Event e9 = EventFactory.createRegularEvent(
                "Techno u Barutani",
                "Letnja žurka svetski poznatih DJ-a na Kalemegdanu.",
                "Barutana, Beograd",
                "2026-05-22 23:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e10 = EventFactory.createRegularEvent(
                "Beach Party Palić",
                "Provod na obali jezera uz najbolje letnje hitove.",
                "Ženski štrand, Palić",
                "2026-07-15 16:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e11 = EventFactory.createRegularEvent(
                "Predstava: Gospođa ministarka",
                "Čuveno Nušićevo delo u izvedbi ansambla kragujevačkog pozorišta.",
                "Knjaževsko-srpski teatar, Kragujevac",
                "2026-06-02 20:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details
        );

        Event e12 = EventFactory.createRegularEvent(
                "Lovefest 2026",
                "Tradicionalni i mega popularni festival elektronske muzike u srcu banje.",
                "Vrnjačka Banja",
                "2026-08-06 18:00",
                "Festival",
                android.R.drawable.ic_menu_slideshow
        );

        Event e13 = EventFactory.createRegularEvent(
                "Noć muzeja u Subotici",
                "Godišnja manifestacija sa besplatnim ulazom i specijalnim umetničkim vođenjem.",
                "Savremena Galerija, Subotica",
                "2026-05-28 19:00",
                "Exhibition",
                android.R.drawable.ic_menu_gallery
        );

        Event e14 = EventFactory.createRegularEvent(
                "Jazz na Paliću",
                "Letnje muzičko veče džeza i promocija lokalnih vinarija sa Palića.",
                "Velika terasa, Palić",
                "2026-06-10 20:00",
                "Concert",
                android.R.drawable.ic_media_play
        );

        Event e15 = EventFactory.createRegularEvent(
                "Latino Night u KC Grad",
                "Tradicionalno plesno latino veče pod otvorenim nebom Beograda.",
                "Kulturni Centar Grad, Beograd",
                "2026-06-25 21:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e16 = EventFactory.createRegularEvent(
                "Stand-up: Srđan Dinčić",
                "Gostovanje i celovečernji šou jednog od najboljih srpskih komičara.",
                "Mts Dvorana, Beograd",
                "2026-06-11 20:00",
                "Stand-Up & Theater",
                android.R.drawable.ic_menu_info_details
        );

        Event e17 = EventFactory.createRegularEvent(
                "Summer Foam Party",
                "Veliki popodnevni i večernji pena party na otvorenim bazenima.",
                "Gradski Bazeni, Kragujevac",
                "2026-07-28 19:00",
                "Party",
                android.R.drawable.ic_menu_camera
        );

        Event e18 = EventFactory.createRegularEvent(
                "Retrospektiva slika MSUB",
                "Istorijska postavka i izložba slika jugoslovenskog modernizma.",
                "Muzej Savremene Umetnosti, Beograd",
                "2026-05-15 12:00",
                "Exhibition",
                android.R.drawable.ic_menu_gallery
        );

        allEvents.addAll(Arrays.asList(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14, e15, e16, e17, e18));
        interestedEvents.addAll(Arrays.asList(e5, e8, e11, e13, e14));
        attendingEvents.addAll(Arrays.asList(e1, e3, e5, e6, e9, e12, e4, e15));
    }

    public static ArrayList<Event> getSortedEvents() {
        ArrayList<Event> sortedList = new ArrayList<>(allEvents);

        sortedList.sort(new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return Boolean.compare(e2.isPromoted(), e1.isPromoted());
            }
        });
        return sortedList;
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
