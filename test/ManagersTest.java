import managers.HistoryManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldGetWorkedTaskManager() {
        TaskManager test = Managers.getDefault();
        assertNotNull(test, "Менеджер не создался");
    }

    @Test
    void shouldGetWorkedHistoryManager() {
        HistoryManager test = Managers.getDefaultHistory();
        assertNotNull(test, "История не создалась");
    }
}