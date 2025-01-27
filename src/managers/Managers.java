package managers;

import Server.DurationAdapter;
import Server.LocalDateTimeAdapter;
import com.google.gson.*;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    private static TaskManager taskManager = new InMemoryTaskManager();
    private static Gson gson = new GsonBuilder() // Подумал, что можно объявить gson тут 1 раз и получать его везде, как taskManager
            .serializeNulls()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    private Managers() {

    }

    public static TaskManager getDefault() {
        return taskManager;
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static Gson getGson() {
        return gson;
    }
}
