package com.roomelephant.elephlink.infra;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import com.roomelephant.elephlink.domain.TaskManager;
import com.roomelephant.elephlink.domain.model.DnsRecordsConfig;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TaskManagerImpl implements TaskManager {
  private final DnsRecordsConfig dnsRecordsConfig;
  private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
  private final CronParser parser = new CronParser(CronDefinitionBuilder.instanceDefinitionFor(CronType.UNIX));
  private Cron cron;

  public TaskManagerImpl(DnsRecordsConfig dnsRecordsConfig) {
    this.dnsRecordsConfig = dnsRecordsConfig;
  }

  @Override
  public boolean validateCronExpression() {
    try {
      cron = parser.parse(dnsRecordsConfig.cronExpression());
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  @Override
  public void execute(Runnable task, long delay) {
    executor.schedule(() -> {
      try {
        task.run();
      } catch (Exception e) {
        log.error("Scheduled task execution failed.", e);
      } finally {
        scheduleNext(task);
      }
    }, delay, TimeUnit.MILLISECONDS);
  }

  private void scheduleNext(Runnable task) {
    ExecutionTime executionTime = ExecutionTime.forCron(cron);
    ZonedDateTime now = ZonedDateTime.now();
    Optional<ZonedDateTime> nextExecution = executionTime.nextExecution(now);

    if (nextExecution.isPresent()) {
      Duration between = Duration.between(now, nextExecution.get());
      log.info("Next execution time: {}.", nextExecution.get());
      long delay = between.toMillis();

      execute(task, delay);
    }
  }

  @Override
  public void shutdown() {
    log.debug("Shutting down.");
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
