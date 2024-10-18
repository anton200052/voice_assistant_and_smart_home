package me.vasylkov.smart_home_module.component;

import jakarta.annotation.PreDestroy;
import me.vasylkov.smart_home_module.model.GettableExposeValue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class GettableExposeValuesManager {
    private final List<GettableExposeValue> exposeStates = new ArrayList<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public GettableExposeValue getLastStateByIEEAddress(String ieeAddress) {
        for (int i = exposeStates.size() - 1; i >= 0; i--) {
            GettableExposeValue exposeState = exposeStates.get(i);
            if (exposeState.getDeviceIEEAddress().equals(ieeAddress)) {
                return exposeStates.get(i);
            }
        }
        return null;
    }

    public void addExposeState(GettableExposeValue exposeState) {
        exposeStates.add(exposeState);

        scheduler.schedule(() -> {
            removeExposeState(exposeState);
        }, 1, TimeUnit.MINUTES);
    }

    private void removeExposeState(GettableExposeValue exposeState) {
        exposeStates.remove(exposeState);
    }

    @PreDestroy
    public void shutdownScheduler() {
        scheduler.shutdown();
    }
}

