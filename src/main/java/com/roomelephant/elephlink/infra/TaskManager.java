package com.roomelephant.elephlink.infra;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.roomelephant.elephlink.domain.model.RecordsConfig;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TaskManager {
  private final RecordsConfig recordsConfig;
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private final CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
  private Cron cron;

  public TaskManager(RecordsConfig recordsConfig) {
    this.recordsConfig = recordsConfig;
  }

  public boolean validateCronExpression() {
    try {
      cron = parser.parse(recordsConfig.cronExpression());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  public void scheduleNext(Runnable task) {
    ExecutionTime executionTime = ExecutionTime.forCron(cron);
    ZonedDateTime now = ZonedDateTime.now();
    Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);

    if (nextExecution.isPresent()) {
      long delay = Duration.between(now, nextExecution.get()).toMillis();

      executor.schedule(() -> {
        try {
          task.run();
        } catch (Exception e) {
          log.error("Scheduled task execution failed", e);
        } finally {
          scheduleNext(task);
        }
      }, delay, TimeUnit.MILLISECONDS);
    }
  }

  public void shutdown() {
    executor.shutdown();
    try {
      if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
        executor.shutdownNow();
      }
    } catch (InterruptedException e) {
      executor.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
