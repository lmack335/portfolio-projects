package team66.VSCO_Game;

import javafx.event.Event;
import javafx.event.EventType;

public class VSCOGameEvent extends Event {
	
	public static final EventType<VSCOGameEvent> ANY =
	            new EventType(Event.ANY, "GAME_EVENT");
	
	public static final EventType<VSCOGameEvent> EXPLORE_FURNITURE =
            new EventType(ANY, "EXPLORE_FURNITURE");
	
	public static final EventType<VSCOGameEvent> OPEN_FURNITURE =
            new EventType(ANY, "OPEN_FURNITURE");
	
	public static final EventType<VSCOGameEvent> COLLECT_GOODIE =
            new EventType<VSCOGameEvent>(ANY, "COLLECT_GOODIE");
	
	public VSCOGameEvent(EventType<? extends Event> eventType) {
		super(eventType);
		// TODO Auto-generated constructor stub
	}

}
